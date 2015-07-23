package jp.gr.java_conf.islandocean.stockanalysis.finance;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Calendar;
import java.util.List;

import jp.gr.java_conf.islandocean.stockanalysis.common.FailedToFindElementException;
import jp.gr.java_conf.islandocean.stockanalysis.price.DataStore;
import jp.gr.java_conf.islandocean.stockanalysis.price.DataStoreKdb;
import jp.gr.java_conf.islandocean.stockanalysis.price.StockEnum;
import jp.gr.java_conf.islandocean.stockanalysis.price.StockManager;
import jp.gr.java_conf.islandocean.stockanalysis.price.StockRecord;
import jp.gr.java_conf.islandocean.stockanalysis.util.CalendarRange;
import jp.gr.java_conf.islandocean.stockanalysis.util.CalendarUtil;

import org.jsoup.nodes.Document;

public class MainDownloadDetailInfoOfAllStocks {

	public MainDownloadDetailInfoOfAllStocks() {
		super();
	}

	private static DataStore selectDataStore() {
		DataStore store = new DataStoreKdb();
		return store;
	}

	public static void main(String[] args) throws IOException {
		DataStore store = selectDataStore();
		StockManager stockManager = StockManager.getInstance(store);
		FinanceManager financeManager = FinanceManager.getInstance();

		Calendar end = CalendarUtil.createToday();
		Calendar daysAgo = (Calendar) end.clone();
		daysAgo.add(Calendar.DAY_OF_MONTH, -30);
		Calendar begin = daysAgo;

		System.out.println("load() begin.");
		stockManager.load(new CalendarRange(begin, end), true);
		System.out.println("load() end.");

		List<List<StockRecord>> dailyDataList = stockManager
				.getAllCorpDataListInDailyList();
		if (dailyDataList.size() <= 0) {
			System.out
					.println("Error: Failed to load data. dailyDataList.size() <= 0");
			return;
		}
		List<StockRecord> stockList = dailyDataList
				.get(dailyDataList.size() - 1);

		FileSystem fs = FileSystems.getDefault();
		String filename = Config.getDetailInformationFilename();
		String tempSuffix = "."
				+ CalendarUtil.format_yyyyMMdd(CalendarUtil.createToday());
		Path pathTemp = fs.getPath(filename + tempSuffix
				+ Config.getDetailInformationExt());

		try {
			if (Files.exists(pathTemp)) {
				Files.delete(pathTemp);
			}
		} catch (IOException eTemp) {
		}

		Calendar today = CalendarUtil.createToday();
		try (BufferedWriter writer = Files.newBufferedWriter(pathTemp,
				StandardCharsets.UTF_8)) {
			int errorCount = 0;
			for (int idxStockList = 0; idxStockList < stockList.size(); ++idxStockList) {
				String stockCode = (String) ((StockRecord) stockList
						.get(idxStockList)).get(StockEnum.STOCK_CODE);
				String searchCode = financeManager
						.toDetailSearchStockCode(stockCode);

				Document doc = null;

				int maxTry = 7;
				for (int retry = 0; retry < maxTry; ++retry) {
					try {
						doc = financeManager
								.readRemoteHtmlDetailPage(searchCode);
						break;
					} catch (IOException e) {
						System.out
								.println("Warning: IOException occurred. Current attempt counter="
										+ retry + " e=" + e.getMessage());
						if (retry >= maxTry - 1) {
							System.out
									.println("Error: Count of IOException exceeds the limit. Aborting...");
							throw e;
						}
						try {
							Thread.sleep(3000L + retry * 1000L);
						} catch (InterruptedException e1) {
						}
					}
				}

				YahooFinanceDetailPageHtmlAnalyzer analyzer = new YahooFinanceDetailPageHtmlAnalyzer();
				try {
					analyzer.analyze(doc, today);
				} catch (FailedToFindElementException e) {
					++errorCount;
					System.out
							.println("Error: Failed to find element. Skip this stock. stockCode="
									+ stockCode);
					continue;
				}
				DetailRecord detailRecord = analyzer.getDetailRecord();
				detailRecord.put(DetailEnum.STOCK_CODE, stockCode);
				String line = detailRecord.toTsvString();
				writer.write(line);
				writer.newLine();

				if ((idxStockList % 100) == 0) {
					System.out.println("counter=" + idxStockList);
				}
			}

			System.out.println("errorCount=" + errorCount);
		}

		Path pathRegular = fs.getPath(filename
				+ Config.getDetailInformationExt());
		Files.copy(pathTemp, pathRegular, StandardCopyOption.COPY_ATTRIBUTES,
				StandardCopyOption.REPLACE_EXISTING);
	}
}
