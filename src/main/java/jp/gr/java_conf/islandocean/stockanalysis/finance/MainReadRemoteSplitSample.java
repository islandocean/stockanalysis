package jp.gr.java_conf.islandocean.stockanalysis.finance;

import java.io.IOException;

import org.jsoup.nodes.Document;

public class MainReadRemoteSplitSample {

	public MainReadRemoteSplitSample() {
		super();
	}

	public static void main(String[] args) throws IOException {

		FinanceManager financeManager = FinanceManager.getInstance();
		String[] codes = new String[] { "4536", "8086", "6502", "4686" };
		for (String code : codes) {
			Document doc = financeManager.readRemoteHtmlChartPage(code);
			YahooFinanceChartPageHtmlAnalyzer analyzer = new YahooFinanceChartPageHtmlAnalyzer();
			analyzer.analyze(doc);
			System.out.println("main() SplitInfoString="
					+ analyzer.getSplitInfoString());
		}
	}
}
