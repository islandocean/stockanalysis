package jp.gr.java_conf.islandocean.stockanalysis.finance;

import java.io.IOException;
import java.util.Calendar;

import jp.gr.java_conf.islandocean.stockanalysis.common.FailedToFindElementException;
import jp.gr.java_conf.islandocean.stockanalysis.util.CalendarUtil;

import org.jsoup.nodes.Document;

public class MainReadDetailSample {

	public MainReadDetailSample() {
		super();
	}

	public static void main(String[] args) throws IOException {

		FinanceManager financeManager = FinanceManager.getInstance();
		String[] codes = new String[] { "4536" };
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
			}
			analyzer.printAll();
			DetailRecord detailRecord = analyzer.getDetailRecord();
			detailRecord.printAllNamesAndValues();
		}
	}
}
