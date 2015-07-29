package jp.gr.java_conf.islandocean.stockanalysis.app;

import java.io.IOException;
import java.util.List;

import jp.gr.java_conf.islandocean.stockanalysis.common.InvalidDataException;
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
		return CalendarUtil.createCalendarRangeRecentDays(365 * 1);
	}

	public static void main(String[] args) {
		scanMain();
	}

	public static void scanMain() {
		MainRetrieveStockByCode2 app = new MainRetrieveStockByCode2();
		try {
			boolean useStockPrice = true;
			boolean useDetailInfo = false;
			boolean useProfileInfo = false;
			app.doScanCorps(useStockPrice, app.selectDataStore(),
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
		String[] stockCodes = new String[] { "2928-S", "3093-T", "3598-T",
				"3810-T", "4563-T", "7638-T" };
		return stockCodes;
	}

	public boolean doScanOneCorp(String stockCode,
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

	@Override
	public void printHeader() {
	}

	@Override
	public void printFooter(int count) {
	}
}
