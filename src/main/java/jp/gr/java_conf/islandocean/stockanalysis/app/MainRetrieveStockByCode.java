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
		return CalendarUtil.createCalendarRangeRecentDays(30);
	}

	public static void main(String[] args) {
		scanMain();
	}

	public static void scanMain() {
		MainRetrieveStockByCode app = new MainRetrieveStockByCode();
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
	public String[] doSelectCorps(StockManager stockManager,
			List<StockRecord> list, FinanceManager financeManager) {
		String[] stockCodes = new String[] { "8951-T", "8952-T", "8953-T",
				"8954-T", "8955-T", };
		return stockCodes;
	}

	@Override
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
