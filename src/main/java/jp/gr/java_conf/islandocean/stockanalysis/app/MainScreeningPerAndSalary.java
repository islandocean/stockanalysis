package jp.gr.java_conf.islandocean.stockanalysis.app;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import jp.gr.java_conf.islandocean.stockanalysis.common.InvalidDataException;
import jp.gr.java_conf.islandocean.stockanalysis.finance.DetailEnum;
import jp.gr.java_conf.islandocean.stockanalysis.finance.DetailRecord;
import jp.gr.java_conf.islandocean.stockanalysis.finance.FinanceManager;
import jp.gr.java_conf.islandocean.stockanalysis.finance.ProfileEnum;
import jp.gr.java_conf.islandocean.stockanalysis.finance.ProfileRecord;
import jp.gr.java_conf.islandocean.stockanalysis.price.DataStore;
import jp.gr.java_conf.islandocean.stockanalysis.price.DataStoreKdb;
import jp.gr.java_conf.islandocean.stockanalysis.price.DataStoreSouko;
import jp.gr.java_conf.islandocean.stockanalysis.price.StockManager;
import jp.gr.java_conf.islandocean.stockanalysis.price.StockRecord;
import jp.gr.java_conf.islandocean.stockanalysis.util.CalendarRange;
import jp.gr.java_conf.islandocean.stockanalysis.util.CalendarUtil;

public class MainScreeningPerAndSalary extends AbstractScanning {

	public MainScreeningPerAndSalary() {
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
		return CalendarUtil.createCalendarRangeRecent(14);
	}

	public String[] selectCorps(StockManager stockManager,
			List<StockRecord> list) {
		String[] stockCodes;
		stockCodes = stockManager.toStockCodeArray(list);
		return stockCodes;
	}

	public boolean useDetailInfo() {
		return true;
	}

	public boolean useProfileInfo() {
		return true;
	}

	public static void main(String[] args) {
		MainScreeningPerAndSalary app = new MainScreeningPerAndSalary();
		try {
			app.scanningMain();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean scanOneCorp(String stockCode,
			List<StockRecord> oneCorpRecords, StockManager stockManager,
			FinanceManager financeManager) {
		boolean hit = false;

		//
		// Get information record of the corp.
		//

		Map<String, DetailRecord> mapDetail = financeManager
				.getStockCodeToDetailRecordMap();
		DetailRecord detailRecord = mapDetail.get(stockCode);
		if (detailRecord == null) {
			System.out.println("Warning: Cannot get detail record. stockCode="
					+ stockCode);
			return hit;
		}

		Map<String, ProfileRecord> mapProfile = financeManager
				.getStockCodeToProfileRecordMap();
		ProfileRecord profileRecord = mapProfile.get(stockCode);
		if (profileRecord == null) {
			System.out.println("Warning: Cannot get profile record. stockCode="
					+ stockCode);
			return hit;
		}

		//
		// Screening.
		//

		Double per = (Double) detailRecord.get(DetailEnum.PER);
		Double salary = (Double) profileRecord
				.get(ProfileEnum.AVERAGE_ANNUAL_SALARY);
		if (per != null && per.doubleValue() < 5.0d && salary != null
				&& salary.doubleValue() >= 8000.0) {
			hit = true;
			System.out.println(detailRecord.toTsvString() + "\t"
					+ profileRecord.toTsvString());
		}

		return hit;
	}

	public void printHeader() {
		System.out.println("------------------------------");
		System.out.print(new DetailRecord().header());
		System.out.print("\t");
		System.out.print(new ProfileRecord().header());
		System.out.println();
	}

	public void printFooter(int count) {
		System.out.println("------------------------------");
		System.out.println("hit count=" + count);
	}
}
