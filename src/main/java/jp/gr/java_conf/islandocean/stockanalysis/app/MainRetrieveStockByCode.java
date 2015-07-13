package jp.gr.java_conf.islandocean.stockanalysis.app;

import java.util.Calendar;
import java.util.List;

import jp.gr.java_conf.islandocean.stockanalysis.finance.FinanceManager;
import jp.gr.java_conf.islandocean.stockanalysis.price.DataStore;
import jp.gr.java_conf.islandocean.stockanalysis.price.DataStoreKdb;
import jp.gr.java_conf.islandocean.stockanalysis.price.DataStoreSouko;
import jp.gr.java_conf.islandocean.stockanalysis.price.StockManager;
import jp.gr.java_conf.islandocean.stockanalysis.price.StockRecord;
import jp.gr.java_conf.islandocean.stockanalysis.util.CalendarRange;
import jp.gr.java_conf.islandocean.stockanalysis.util.CalendarUtil;

public class MainRetrieveStockByCode extends AbstractScanning {

	public MainRetrieveStockByCode() {
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
		int lastDays = 30;
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
		stockCodes = new String[] { "8951", "8952", "8953", "8954", "8955", };
		String suffix = stockManager.getStockCodeSuffixOfDefaultMarket();
		for (int i = 0; i < stockCodes.length; ++i) {
			stockCodes[i] += suffix;
		}
		return stockCodes;
	}

	public static void main(String[] args) {
		MainRetrieveStockByCode app = new MainRetrieveStockByCode();
		app.scanningMain();
	}

	public boolean scanOneCorp(String stockCode,
			List<StockRecord> oneCorpRecords, StockManager stockManager,
			FinanceManager financeManager) {

		boolean hit = false;
		for (StockRecord record : oneCorpRecords) {
			hit = true;
			System.out.println(record.toString());
		}
		System.out.println();
		return hit;
	}

	public void printHeader() {
	}

	public void printFooter(int count) {
	}
}
