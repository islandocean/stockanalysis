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

public class MainCalculatePriceRatioFromYearlyHigh extends AbstractScanning {

	private static final Character DELIM = '\t';

	public MainCalculatePriceRatioFromYearlyHigh() {
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
		Calendar end = CalendarUtil.createToday();
		Calendar firstDay = CalendarUtil.createFirstDayOfThisYear(end);
		Calendar begin = firstDay;
		CalendarRange calendarRange = new CalendarRange(begin, end);
		return calendarRange;
	}

	@SuppressWarnings("unused")
	public String[] selectCorps(StockManager stockManager,
			List<StockRecord> list) {
		String[] stockCodes;
		stockCodes = new String[] { "8951", "8952", "8953", "8954", "8955",
				"8956", "8957", "8958", "8959", "8960", "8961", "8964", "8966",
				"8967", "8968", "8972", "8973", "8975", "8976", "8977", "8982",
				"8984", "8985", "8986", "8987", "8963", "3226", "3227", "3234",
				"3240", "3249", "3269", "8979", "3278", "3279", "3263", "3281",
				"3282", "3283", "3285", "3287", "3290", "3292", "3295", "3296",
				"3298", "3308", "3451", "3309", "3453", "3455" };
		String suffix = stockManager.getStockCodeSuffixOfDefaultMarket();
		for (int i = 0; i < stockCodes.length; ++i) {
			stockCodes[i] += suffix;
		}
		return stockCodes;
	}

	public static void main(String[] args) {
		MainCalculatePriceRatioFromYearlyHigh app = new MainCalculatePriceRatioFromYearlyHigh();
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

		if (ratio != null) {
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
		System.out.print(DELIM + "url");
		System.out.println();
	}

	public void printFooter(int count) {
		System.out.println("------------------------------");
		System.out.println("hit count=" + count);
	}
}
