package jp.gr.java_conf.islandocean.stockanalysis.finance;

import java.io.IOException;
import java.util.Calendar;

import jp.gr.java_conf.islandocean.stockanalysis.common.FailedToFindElementException;
import jp.gr.java_conf.islandocean.stockanalysis.common.InvalidDataException;
import jp.gr.java_conf.islandocean.stockanalysis.util.CalendarUtil;

import org.jsoup.nodes.Document;

public class MainReadRemoteDetailSample {

	public MainReadRemoteDetailSample() {
		super();
	}

	public static void main(String[] args) throws IOException {

		FinanceManager financeManager = FinanceManager.getInstance();
		String[] codes = new String[] { "6143", "6674", "4565", "1305" };
		Calendar today = CalendarUtil.createToday();
		for (String stockCode : codes) {
			Document doc = financeManager.readRemoteHtmlDetailPage(stockCode);
			YahooFinanceDetailPageHtmlAnalyzer analyzer = new YahooFinanceDetailPageHtmlAnalyzer();
			try {
				analyzer.analyze(doc, today);
			} catch (FailedToFindElementException e) {
				System.out.println("Error: Failed to find element. stockCode="
						+ stockCode);
				continue;
			} catch (InvalidDataException e) {
				System.out
						.println("Error: Invalid or unexpected data. Skip this stock. stockCode="
								+ stockCode);
				System.out.println(e);
				continue;
			}
			System.out.println("------------------------------");
			analyzer.printAll();

			DetailRecord detailRecord = analyzer.getDetailRecord();
			System.out.println("------------------------------");
			detailRecord.printAllNamesAndValues();
		}
	}
}
