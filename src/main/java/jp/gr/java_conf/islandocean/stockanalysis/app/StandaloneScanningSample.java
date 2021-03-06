package jp.gr.java_conf.islandocean.stockanalysis.app;

import java.io.IOException;
import java.util.Calendar;
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
import jp.gr.java_conf.islandocean.stockanalysis.util.Util;

public class StandaloneScanningSample {

	private static final String DELIM = "\t";

	public StandaloneScanningSample() {
	}

	@SuppressWarnings("unused")
	private static DataStore selectDataStore() {
		DataStore store;

		store = new DataStoreKdb();
		if (false) {
			store = new DataStoreKdb();
			store = new DataStoreSouko();
		}

		return store;
	}

	private static CalendarRange selectCalendarRange() {
		return CalendarUtil.createCalendarRangeRecentDays(180);
	}

	@SuppressWarnings("unused")
	private static String[] selectCorps(StockManager stockManager,
			List<StockRecord> list) {
		String[] stockCodes;

		stockCodes = stockManager.toStockCodeArray(list);
		if (false) {
			stockCodes = stockManager.toStockCodeArray(list);
			stockCodes = new String[] { "9479-T" };
		}

		return stockCodes;
	}

	public static void main(String[] args) {
		scanMain();
	}

	public static void scanMain() {
		StandaloneScanningSample app = new StandaloneScanningSample();
		try {
			app.scanningMain(false, false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void scanningMain(boolean useDetailInfo, boolean useProfileInfo)
			throws IOException, InvalidDataException {
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
		Map<String, StockSplitInfo> stockCodeToSplitInfoMap;
		try {
			financeManager.generateStockCodeToSplitInfoMap();
			stockCodeToSplitInfoMap = financeManager
					.getStockCodeToSplitInfoMap();
		} catch (IOException e) {
			System.out
					.println("Error: Failed to create stock split infomation.");
			e.printStackTrace();
			return;
		}
		financeManager.checkAndWarnSplitInfo(lastData, stockCodeToSplitInfoMap);

		if (useDetailInfo) {
			financeManager.generateStockCodeToDetailRecordMap();
		}

		if (useProfileInfo) {
			financeManager.generateStockCodeToProfileRecordMap();
		}

		printHeader();
		Calendar currentDay = CalendarUtil.createToday();
		int count = 0;
		for (int idxCorp = 0; idxCorp < stockCodes.length; ++idxCorp) {
			String stockCode = stockCodes[idxCorp];
			List<StockRecord> oneCorpRecords = stockManager.retrieve(stockCode);
			if (oneCorpRecords.size() == 0) {
				System.out
						.println("Warning: No stock price record for one corp. stockCode="
								+ stockCode);
			}
			String splitSerachStockCode = financeManager
					.toSplitSearchStockCode(stockCode);
			StockSplitInfo stockSplitInfo = stockCodeToSplitInfoMap
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

	private static boolean scanOneCorp(String stockCode,
			List<StockRecord> oneCorpRecords, StockManager stockManager,
			FinanceManager financeManager) {

		boolean hit = false;
		String stockName = null;
		Double periodHighPrice = null;
		Calendar periodHighDay = null;
		Double periodLowPrice = null;
		Calendar periodLowDay = null;
		Double lastPrice = null;
		for (StockRecord record : oneCorpRecords) {
			stockName = (String) record.get(StockEnum.STOCK_NAME);
			Calendar day = (Calendar) record.get(StockEnum.DATE);
			Double highPrice = (Double) record
					.get(StockEnum.ADJUSTED_HIGH_PRICE);
			if (highPrice != null) {
				if (periodHighPrice == null || periodHighPrice <= highPrice) {
					periodHighPrice = highPrice;
					periodHighDay = day;
				}
			}
			Double lowPrice = (Double) record.get(StockEnum.ADJUSTED_LOW_PRICE);
			if (lowPrice != null) {
				if (periodLowPrice == null || periodLowPrice >= lowPrice) {
					periodLowPrice = lowPrice;
					periodLowDay = day;
				}
			}
			Double closingPrice = (Double) record
					.get(StockEnum.ADJUSTED_CLOSING_PRICE);
			if (closingPrice != null) {
				lastPrice = closingPrice;
			}
		}

		Double ratio = null;
		if (periodHighPrice != null) {
			ratio = lastPrice / periodHighPrice;
		}

		if (ratio != null && ratio < 0.65d) {
			hit = true;
			System.out.print(stockCode);
			System.out.print(DELIM + stockName);
			System.out.print(DELIM + periodHighPrice);
			System.out.print(DELIM
					+ CalendarUtil.format_yyyyMMdd(periodHighDay));
			System.out.print(DELIM + periodLowPrice);
			System.out
					.print(DELIM + CalendarUtil.format_yyyyMMdd(periodLowDay));
			System.out.print(DELIM + lastPrice);
			System.out.print(DELIM + Util.formatPercent(ratio.doubleValue()));
			System.out.print(DELIM
					+ financeManager.getHtmlChartPageSpec(financeManager
							.toSplitSearchStockCode(stockCode)));
			System.out.println();
		}

		return hit;
	}

	private static void printHeader() {
		System.out.println("------------------------------");
		System.out.print("stock code");
		System.out.print(DELIM + "stock name");
		System.out.print(DELIM + "period high price");
		System.out.print(DELIM + "period high day");
		System.out.print(DELIM + "period low price");
		System.out.print(DELIM + "period low day");
		System.out.print(DELIM + "lastest price");
		System.out.print(DELIM + "latest / high ratio");
		System.out.print(DELIM + "url");
		System.out.println();
	}

	private static void printFooter(int count) {
		System.out.println("------------------------------");
		System.out.println("hit count=" + count);
	}
}
