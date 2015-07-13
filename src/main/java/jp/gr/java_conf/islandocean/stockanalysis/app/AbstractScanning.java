package jp.gr.java_conf.islandocean.stockanalysis.app;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import jp.gr.java_conf.islandocean.stockanalysis.finance.FinanceManager;
import jp.gr.java_conf.islandocean.stockanalysis.finance.StockSplitInfo;
import jp.gr.java_conf.islandocean.stockanalysis.price.DataStore;
import jp.gr.java_conf.islandocean.stockanalysis.price.StockManager;
import jp.gr.java_conf.islandocean.stockanalysis.price.StockRecord;
import jp.gr.java_conf.islandocean.stockanalysis.util.CalendarRange;
import jp.gr.java_conf.islandocean.stockanalysis.util.CalendarUtil;

abstract public class AbstractScanning {

	public AbstractScanning() {
		super();
	}

	abstract public DataStore selectDataStore();

	abstract public CalendarRange selectCalendarRange();

	abstract public String[] selectCorps(StockManager stockManager,
			List<StockRecord> list);

	abstract public boolean scanOneCorp(String stockCode,
			List<StockRecord> oneCorpRecords, StockManager stockManager,
			FinanceManager financeManager);

	abstract public void printHeader();

	abstract public void printFooter(int count);

	public void scanningMain() {
		DataStore store = selectDataStore();
		StockManager stockManager = StockManager.getInstance(store);
		CalendarRange calendarRange = selectCalendarRange();
		String stockCodeSuffixOfDefaultMarket = store
				.getStockCodeSuffixOfDefaultMarket();

		int recordCount = stockManager.load(calendarRange, false);
		if (recordCount <= 0) {
			System.out.println("Error: Cannot load any stock prirce data.");
			return;
		} else {
			System.out
					.println("Info: Succeeded in loading stock prirce data. recordCount="
							+ recordCount);
		}

		stockManager.generateAllCorpDataMapInDailyList();
		List<List<StockRecord>> allCorpDataListInDailyList = stockManager
				.getAllCorpDataListInDailyList();
		List<StockRecord> lastData = allCorpDataListInDailyList
				.get(allCorpDataListInDailyList.size() - 1);
		List<Calendar> dailyDayList = stockManager.getDayList();
		Calendar lastDay = dailyDayList.get(dailyDayList.size() - 1);
		String[] stockCodes = selectCorps(stockManager, lastData);

		FinanceManager financeManager = FinanceManager.getInstance();
		Map<String, StockSplitInfo> code2SplitInfoMap;
		try {
			code2SplitInfoMap = financeManager.generateStockSplitInfoMap();
		} catch (IOException e) {
			System.out
					.println("Error: Failed to create stock split infomation.");
			e.printStackTrace();
			return;
		}
		financeManager.checkAndWarnSplitInfo(lastData, code2SplitInfoMap);

		printHeader();
		Calendar currentDay = CalendarUtil.createToday();
		int count = 0;
		for (int idxCorp = 0; idxCorp < stockCodes.length; ++idxCorp) {
			String stockCode = stockCodes[idxCorp];
			List<StockRecord> oneCorpRecords = stockManager.retrieve(stockCode);
			String splitSerachStockCode = financeManager
					.toSplitSearchStockCode(stockCode);
			StockSplitInfo stockSplitInfo = code2SplitInfoMap
					.get(splitSerachStockCode);
			stockManager.calcAdjustedPricesForOneCorp(oneCorpRecords,
					stockSplitInfo, currentDay);
			if (scanOneCorp(stockCode, oneCorpRecords, stockManager,
					financeManager)) {
				++count;
			}
		}
		printFooter(count);
	}
}