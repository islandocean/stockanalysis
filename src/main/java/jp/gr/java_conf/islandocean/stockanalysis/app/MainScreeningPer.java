package jp.gr.java_conf.islandocean.stockanalysis.app;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import jp.gr.java_conf.islandocean.stockanalysis.common.InvalidDataException;
import jp.gr.java_conf.islandocean.stockanalysis.finance.DetailEnum;
import jp.gr.java_conf.islandocean.stockanalysis.finance.DetailRecord;
import jp.gr.java_conf.islandocean.stockanalysis.finance.FinanceManager;
import jp.gr.java_conf.islandocean.stockanalysis.price.DataStore;
import jp.gr.java_conf.islandocean.stockanalysis.price.StockManager;
import jp.gr.java_conf.islandocean.stockanalysis.price.StockRecord;
import jp.gr.java_conf.islandocean.stockanalysis.util.CalendarRange;

public class MainScreeningPer implements CorpsScannerTemplate {

	public MainScreeningPer() {
	}

	public DataStore selectDataStore() {
		return null;
	}

	public CalendarRange selectCalendarRange() {
		return null;
	}

	public static void main(String[] args) {
		MainScreeningPer app = new MainScreeningPer();
		app.scanMain();
	}

	public void scanMain() {
		try {
			boolean useStockPrice = false;
			boolean useDetailInfo = true;
			boolean useProfileInfo = false;
			CorpsAllData allData = initializeCorpsAllData(useStockPrice,
					selectDataStore(), selectCalendarRange(), useDetailInfo,
					useProfileInfo);
			doScanCorps(allData);
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

		Map<String, DetailRecord> map = financeManager
				.getStockCodeToDetailRecordMap();
		DetailRecord detailRecord = map.get(stockCode);
		if (detailRecord == null) {
			System.out.println("Warning: Cannot get detail record. stockCode="
					+ stockCode);
			return hit;
		}

		//
		// Screening.
		//

		Double per = (Double) detailRecord.get(DetailEnum.PER);
		if (per != null && per.doubleValue() < 5.0d) {
			hit = true;
		}
		if (hit) {
			System.out.println(detailRecord.toTsvString());
		}
		return hit;
	}

	@Override
	public void printHeader() {
		System.out.println("------------------------------");
		System.out.println(new DetailRecord().header());
	}

	@Override
	public void printFooter(int count) {
		System.out.println("------------------------------");
		System.out.println("hit count=" + count);
	}
}
