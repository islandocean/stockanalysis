package jp.gr.java_conf.islandocean.stockanalysis.app;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import jp.gr.java_conf.islandocean.stockanalysis.common.InvalidDataException;
import jp.gr.java_conf.islandocean.stockanalysis.finance.FinanceManager;
import jp.gr.java_conf.islandocean.stockanalysis.price.DataStore;
import jp.gr.java_conf.islandocean.stockanalysis.price.DataStoreKdb;
import jp.gr.java_conf.islandocean.stockanalysis.price.DataStoreSouko;
import jp.gr.java_conf.islandocean.stockanalysis.price.StockEnum;
import jp.gr.java_conf.islandocean.stockanalysis.price.StockManager;
import jp.gr.java_conf.islandocean.stockanalysis.price.StockRecord;
import jp.gr.java_conf.islandocean.stockanalysis.util.CalendarRange;
import jp.gr.java_conf.islandocean.stockanalysis.util.CalendarUtil;
import jp.gr.java_conf.islandocean.stockanalysis.util.Util;

public class MainScreeningPriceUpDownFromPeriodHighLow implements
		CorpsScannerTemplate {

	private static final String DELIM = "\t";

	private CorpsAllData allData;

	public MainScreeningPriceUpDownFromPeriodHighLow() {
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
		return CalendarUtil.createCalendarRangeRecentDays(30);
	}

	public static void main(String[] args) {
		MainScreeningPriceUpDownFromPeriodHighLow app = new MainScreeningPriceUpDownFromPeriodHighLow();
		app.scanInit();
		app.scanMain();
	}

	private void scanInit() {
		boolean useStockPrice = true;
		boolean useDetailInfo = false;
		boolean useProfileInfo = false;
		try {
			allData = initializeCorpsAllData(useStockPrice, selectDataStore(),
					selectCalendarRange(), useDetailInfo, useProfileInfo);
		} catch (IOException | InvalidDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
	}

	private void scanMain() {
		try {
			doScanCorps(allData);
		} catch (IOException | InvalidDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String[] doSelectCorps(StockManager stockManager,
			List<StockRecord> list, FinanceManager financeManager) {
		return stockManager.toStockCodeArray(list);
	}

	@Override
	public boolean doScanOneCorp(String stockCode,
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

		Double lastHighRatio = null;
		if (periodHighPrice != null) {
			lastHighRatio = lastPrice / periodHighPrice;
		}

		Double lastLowRatio = null;
		if (periodLowPrice != null) {
			lastLowRatio = lastPrice / periodLowPrice;
		}

		if (lastHighRatio != null && lastHighRatio < 0.65d
		/* && lastLowRatio < 1.1d */) {
			hit = true;
		}
		if (hit) {
			System.out.print(stockCode);
			System.out.print(DELIM + stockName);
			System.out.print(DELIM + periodHighPrice);
			System.out.print(DELIM
					+ CalendarUtil.format_yyyyMMdd(periodHighDay));
			System.out.print(DELIM + periodLowPrice);
			System.out
					.print(DELIM + CalendarUtil.format_yyyyMMdd(periodLowDay));
			System.out.print(DELIM + lastPrice);
			System.out.print(DELIM
					+ Util.formatPercent(lastHighRatio.doubleValue()));
			System.out.print(DELIM
					+ Util.formatPercent(lastLowRatio.doubleValue()));
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
		System.out.print(DELIM + "period high price");
		System.out.print(DELIM + "period high day");
		System.out.print(DELIM + "period low price");
		System.out.print(DELIM + "period low day");
		System.out.print(DELIM + "lastest price");
		System.out.print(DELIM + "latest / high ratio");
		System.out.print(DELIM + "latest / low ratio");
		System.out.print(DELIM + "url");
		System.out.println();
	}

	@Override
	public void printFooter(int count) {
		System.out.println("------------------------------");
		System.out.println("hit count=" + count);
	}
}
