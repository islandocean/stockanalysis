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
import jp.gr.java_conf.islandocean.stockanalysis.price.StockManager;
import jp.gr.java_conf.islandocean.stockanalysis.price.StockRecord;
import jp.gr.java_conf.islandocean.stockanalysis.util.CalendarRange;

public class MainScreeningPerAndSalary extends AbstractScanning {

	public MainScreeningPerAndSalary() {
	}

	public DataStore selectDataStore() {
		return null;
	}

	public CalendarRange selectCalendarRange() {
		return null;
	}

	public static void main(String[] args) {
		scanMain();
	}

	public static void scanMain() {
		MainScreeningPerAndSalary app = new MainScreeningPerAndSalary();
		try {
			boolean useStockPrice = false;
			boolean useDetailInfo = true;
			boolean useProfileInfo = true;
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
		return financeManager.toStockCodeArrayFromDetailRecordlist();
	}

	@Override
	public boolean doScanOneCorp(String stockCode,
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
		if (per != null && per.doubleValue() < 7.0d && salary != null
				&& salary.doubleValue() >= 7000.0) {
			hit = true;
			System.out.println(detailRecord.toTsvString() + "\t"
					+ profileRecord.toTsvString());
		}

		return hit;
	}

	@Override
	public void printHeader() {
		System.out.println("------------------------------");
		System.out.print(new DetailRecord().header());
		System.out.print("\t");
		System.out.print(new ProfileRecord().header());
		System.out.println();
	}

	@Override
	public void printFooter(int count) {
		System.out.println("------------------------------");
		System.out.println("hit count=" + count);
	}
}
