package jp.gr.java_conf.islandocean.stockanalysis.app;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import jp.gr.java_conf.islandocean.stockanalysis.common.InvalidDataException;
import jp.gr.java_conf.islandocean.stockanalysis.finance.FinanceManager;
import jp.gr.java_conf.islandocean.stockanalysis.finance.StockSplitInfo;
import jp.gr.java_conf.islandocean.stockanalysis.price.DataStore;
import jp.gr.java_conf.islandocean.stockanalysis.price.StockManager;
import jp.gr.java_conf.islandocean.stockanalysis.price.StockRecord;
import jp.gr.java_conf.islandocean.stockanalysis.util.CalendarRange;
import jp.gr.java_conf.islandocean.stockanalysis.util.CalendarUtil;

public interface CorpsScannerTemplate {

	String[] doSelectCorps(StockManager stockManager, List<StockRecord> list,
			FinanceManager financeManager) throws IOException;

	boolean doScanOneCorp(String stockCode, List<StockRecord> oneCorpRecords,
			StockManager stockManager, FinanceManager financeManager);

	void printHeader();

	void printFooter(int count);

	default CorpsAllData initializeCorpsAllData(boolean useStockPrice,
			DataStore store, CalendarRange calendarRange,
			boolean useDetailInfo, boolean useProfileInfo) throws IOException,
			InvalidDataException {

		CorpsAllData corpsAllData = new CorpsAllData(useStockPrice, store,
				calendarRange, useDetailInfo, useProfileInfo);

		//
		// stock manager
		//
		StockManager stockManager = null;
		List<StockRecord> lastData = null;

		if (useStockPrice) {
			stockManager = StockManager.getInstance(store);
			int recordCount = stockManager.load(calendarRange, false);
			if (recordCount <= 0) {
				System.out.println("Error: Cannot load any stock prirce data.");
				throw new InvalidDataException(
						"Cannot load any stock prirce data.");
			} else {
				System.out
						.println("Info: Succeeded in loading stock prirce data. recordCount="
								+ recordCount);
			}

			stockManager.generateAllCorpDataMapInDailyList();
			List<List<StockRecord>> allCorpDataListInDailyList = stockManager
					.getAllCorpDataListInDailyList();
			lastData = allCorpDataListInDailyList
					.get(allCorpDataListInDailyList.size() - 1);
		}

		//
		// finance manager
		//
		FinanceManager financeManager = FinanceManager.getInstance();
		Map<String, StockSplitInfo> stockCodeToSplitInfoMap;
		try {
			financeManager.generateStockCodeToSplitInfoMap();
			stockCodeToSplitInfoMap = financeManager
					.getStockCodeToSplitInfoMap();
		} catch (IOException e) {
			System.out
					.println("Error: Failed to create stock split infomation.");
			e.printStackTrace();
			throw e;
		}
		if (lastData != null) {
			financeManager.checkAndWarnSplitInfo(lastData,
					stockCodeToSplitInfoMap);
		}

		if (useDetailInfo) {
			financeManager.generateStockCodeToDetailRecordMap();
		}

		if (useProfileInfo) {
			financeManager.generateStockCodeToProfileRecordMap();
		}

		//
		// stock codes
		//
		String[] stockCodes = doSelectCorps(stockManager, lastData,
				financeManager);

		//
		// Set data
		//
		corpsAllData.setStockManager(stockManager);
		corpsAllData.setLastData(lastData);
		corpsAllData.setFinanceManager(financeManager);
		corpsAllData.setStockCodes(stockCodes);

		return corpsAllData;
	}

	default void doScanCorps(CorpsAllData corpsAllData) throws IOException,
			InvalidDataException {

		//
		// Get data
		//
		StockManager stockManager = corpsAllData.getStockManager();
		List<StockRecord> lastData = corpsAllData.getLastData();
		FinanceManager financeManager = corpsAllData.getFinanceManager();
		Map<String, StockSplitInfo> stockCodeToSplitInfoMap = financeManager
				.getStockCodeToSplitInfoMap();
		String[] stockCodes = corpsAllData.getStockCodes();

		//
		// scan
		//
		printHeader();
		Calendar currentDay = CalendarUtil.createToday();
		int count = 0;
		for (int idxCorp = 0; idxCorp < stockCodes.length; ++idxCorp) {
			String stockCode = stockCodes[idxCorp];
			List<StockRecord> oneCorpRecords = null;
			if (stockManager != null) {
				oneCorpRecords = stockManager.retrieve(stockCode);
			}

			if (oneCorpRecords == null) {

			} else if (oneCorpRecords.size() == 0) {
				System.out
						.println("Warning: No stock price record for one corp. stockCode="
								+ stockCode);
			} else {
				String splitSerachStockCode = financeManager
						.toSplitSearchStockCode(stockCode);
				StockSplitInfo stockSplitInfo = stockCodeToSplitInfoMap
						.get(splitSerachStockCode);
				stockManager.calcAdjustedPricesForOneCorp(oneCorpRecords,
						stockSplitInfo, currentDay);
			}

			if (doScanOneCorp(stockCode, oneCorpRecords, stockManager,
					financeManager)) {
				++count;
			}
		}
		printFooter(count);
	}
}
