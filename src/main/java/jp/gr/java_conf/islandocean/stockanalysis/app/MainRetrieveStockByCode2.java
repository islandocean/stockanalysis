package jp.gr.java_conf.islandocean.stockanalysis.app;

import java.io.IOException;
import java.util.List;

import jp.gr.java_conf.islandocean.stockanalysis.finance.FinanceManager;
import jp.gr.java_conf.islandocean.stockanalysis.price.DataStore;
import jp.gr.java_conf.islandocean.stockanalysis.price.DataStoreKdb;
import jp.gr.java_conf.islandocean.stockanalysis.price.DataStoreSouko;
import jp.gr.java_conf.islandocean.stockanalysis.price.StockManager;
import jp.gr.java_conf.islandocean.stockanalysis.price.StockRecord;
import jp.gr.java_conf.islandocean.stockanalysis.util.CalendarRange;
import jp.gr.java_conf.islandocean.stockanalysis.util.CalendarUtil;

public class MainRetrieveStockByCode2 extends AbstractScanning {

	public MainRetrieveStockByCode2() {
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
		return CalendarUtil.createCalendarRangeRecent(365 * 1);
	}

	public String[] selectCorps(StockManager stockManager,
			List<StockRecord> list) {
		String[] stockCodes = new String[] { "2928-S", "3093-T", "3598-T",
				"3810-T", "4563-T", "7638-T" };
		return stockCodes;
	}

	public static void main(String[] args) {
		MainRetrieveStockByCode2 app = new MainRetrieveStockByCode2();
		try {
			app.scanningMain();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean scanOneCorp(String stockCode,
			List<StockRecord> oneCorpRecords, StockManager stockManager,
			FinanceManager financeManager) {

		boolean hit = false;
		for (StockRecord record : oneCorpRecords) {
			hit = true;
			System.out.println(record.toTsvString());
		}
		System.out.println();
		return hit;
	}

	public void printHeader() {
	}

	public void printFooter(int count) {
	}
}
