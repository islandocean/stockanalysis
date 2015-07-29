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

public class MainScreeningPer extends AbstractScanning {

	public MainScreeningPer() {
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
		MainScreeningPer app = new MainScreeningPer();
		try {
			boolean useStockPrice = false;
			boolean useDetailInfo = true;
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
