package jp.gr.java_conf.islandocean.stockanalysis.app;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import jp.gr.java_conf.islandocean.stockanalysis.common.InvalidDataException;
import jp.gr.java_conf.islandocean.stockanalysis.finance.FinanceManager;
import jp.gr.java_conf.islandocean.stockanalysis.finance.StockSplitInfo;
import jp.gr.java_conf.islandocean.stockanalysis.price.DataStore;
import jp.gr.java_conf.islandocean.stockanalysis.price.DataStoreKdb;
import jp.gr.java_conf.islandocean.stockanalysis.price.DataStoreSouko;
import jp.gr.java_conf.islandocean.stockanalysis.price.StockEnum;
import jp.gr.java_conf.islandocean.stockanalysis.price.StockManager;
import jp.gr.java_conf.islandocean.stockanalysis.price.StockRecord;
import jp.gr.java_conf.islandocean.stockanalysis.util.CalendarRange;
import jp.gr.java_conf.islandocean.stockanalysis.util.CalendarUtil;

public class MainScreeningSplitNumInPeriod extends AbstractScanning {

	private static final String DELIM = "\t";

	private int CONDITION_RECENT_DAYS = 365 * 3;
	private int CONDITION_SPLIT_COUNT_IS_AND_OVER = 3;

	public MainScreeningSplitNumInPeriod() {
	}

	@SuppressWarnings("unused")
	public DataStore selectDataStore() {
		DataStore store;

		store = new DataStoreKdb();
		if (false) {
			store = new DataStoreKdb();
			store = new DataStoreSouko();
		}

		return store;
	}

	public CalendarRange selectCalendarRange() {
		return CalendarUtil.createCalendarRangeRecentDays(14);
	}

	public CalendarRange selectSplitSearchCalendarRange() {
		return CalendarUtil
				.createCalendarRangeRecentDays(CONDITION_RECENT_DAYS);
	}

	public static void main(String[] args) {
		MainScreeningSplitNumInPeriod app = new MainScreeningSplitNumInPeriod();
		try {
			boolean useStockPrice = true;
			boolean useDetailInfo = false;
			boolean useProfileInfo = false;
			app.scanningMain(useStockPrice, app.selectDataStore(),
					app.selectCalendarRange(), useDetailInfo, useProfileInfo);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String[] selectCorps(StockManager stockManager,
			List<StockRecord> list, FinanceManager financeManager) {
		return stockManager.toStockCodeArray(list);
	}

	@Override
	public boolean scanOneCorp(String stockCode,
			List<StockRecord> oneCorpRecords, StockManager stockManager,
			FinanceManager financeManager) {
		boolean hit = false;
		String stockName = null;
		int splitCount = 0;
		for (StockRecord record : oneCorpRecords) {
			stockName = (String) record.get(StockEnum.STOCK_NAME);
			if (stockName != null) {
				break;
			}
		}
		Map<String, StockSplitInfo> stockCodeToSplitInfoMap = financeManager
				.getStockCodeToSplitInfoMap();
		String splitSerachStockCode = financeManager
				.toSplitSearchStockCode(stockCode);
		StockSplitInfo stockSplitInfo = stockCodeToSplitInfoMap
				.get(splitSerachStockCode);

		splitCount = financeManager.countSplitsInCalendarRange(stockSplitInfo,
				selectSplitSearchCalendarRange());
		if (splitCount >= CONDITION_SPLIT_COUNT_IS_AND_OVER) {
			hit = true;
			System.out.print(stockCode);
			System.out.print(DELIM + stockName);
			System.out.print(DELIM + splitCount);
			System.out.print(DELIM + stockSplitInfo.getOrgSplitStr());
			System.out.print(DELIM
					+ financeManager.getHtmlChartPageSpec(financeManager
							.toSplitSearchStockCode(stockCode)));
			System.out.println();
		}
		return hit;
	}

	@Override
	public void printHeader() {
		System.out.println("------------------------------");
		System.out.print("stock code");
		System.out.print(DELIM + "stock name");
		System.out.print(DELIM + "split count in range");
		System.out.print(DELIM + "split info");
		System.out.print(DELIM + "url");
		System.out.println();
	}

	@Override
	public void printFooter(int count) {
		System.out.println("------------------------------");
		System.out.println("hit count=" + count);
	}
}
