package jp.gr.java_conf.islandocean.stockanalysis.app;

import java.util.Calendar;
import java.util.List;

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

public class MainScreeningPriceDownFromPeriodHigh extends AbstractScanning {

	private static final String TAB = "\t";

	public MainScreeningPriceDownFromPeriodHigh() {
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
		int lastDays = 180;
		Calendar end = CalendarUtil.createToday();
		Calendar daysAgo = (Calendar) end.clone();
		daysAgo.add(Calendar.DAY_OF_MONTH, -lastDays);
		Calendar begin = daysAgo;

		// Calendar firstDay = CalendarUtil.createFirstDayOfThisYear(end);
		// Calendar begin = firstDay;

		CalendarRange calendarRange = new CalendarRange(begin, end);
		return calendarRange;
	}

	public String[] selectCorps(StockManager stockManager,
			List<StockRecord> list) {
		String[] stockCodes;
		stockCodes = stockManager.toStockCodeArray(list);
		return stockCodes;
	}

	public static void main(String[] args) {
		MainScreeningPriceDownFromPeriodHigh app = new MainScreeningPriceDownFromPeriodHigh();
		app.scanningMain();
	}

	public boolean scanOneCorp(String stockCode,
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
			System.out.print(TAB + stockName);
			System.out.print(TAB + periodHighPrice);
			System.out.print(TAB + CalendarUtil.format_yyyyMMdd(periodHighDay));
			System.out.print(TAB + periodLowPrice);
			System.out.print(TAB + CalendarUtil.format_yyyyMMdd(periodLowDay));
			System.out.print(TAB + lastPrice);
			System.out.print(TAB + Util.formatPercent(ratio.doubleValue()));
			System.out.print(TAB
					+ financeManager.getHtmlChartPageSpec(financeManager
							.toSplitSearchStockCode(stockCode)));
			System.out.println();
		}

		return hit;
	}

	public void printHeader() {
		System.out.println("------------------------------");
		System.out.print("stock code");
		System.out.print(TAB + "stock name");
		System.out.print(TAB + "period high price");
		System.out.print(TAB + "period high day");
		System.out.print(TAB + "period low price");
		System.out.print(TAB + "period low day");
		System.out.print(TAB + "lastest price");
		System.out.print(TAB + "latest / high ratio");
		System.out.print(TAB + "url");
		System.out.println();
	}

	public void printFooter(int count) {
		System.out.println("------------------------------");
		System.out.println("hit count=" + count);
	}
}
