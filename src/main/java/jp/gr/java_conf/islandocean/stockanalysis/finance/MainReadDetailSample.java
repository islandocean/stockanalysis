package jp.gr.java_conf.islandocean.stockanalysis.finance;

import java.io.IOException;

import org.jsoup.nodes.Document;

public class MainReadDetailSample {

	public MainReadDetailSample() {
		super();
	}

	public static void main(String[] args) throws IOException {

		FinanceManager financeManager = FinanceManager.getInstance();
		String[] codes = new String[] { "4536" };
		for (String code : codes) {
			Document doc = financeManager.readRemoteHtmlDetailPage(code);
			YahooFinanceDetailPageHtmlAnalyzer analyzer = new YahooFinanceDetailPageHtmlAnalyzer();
			analyzer.analyze(doc);
			analyzer.printAll();
			StockDetailInfo stockDetailIfo = analyzer.getStockDetailInfo();
			stockDetailIfo.printAll();
		}
	}
}
