package jp.gr.java_conf.islandocean.stockanalysis.finance;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class YahooFinanceChartPageHtmlAnalyzer {

	private static final String CSS_QUERY_IN_CHART_PAGE_TO_FIND_TABLE = ".optionFi table";
	private static final String CAPTION_IN_CHART_PAGE_OF_SPLIT_INFORMATION = "分割情報";

	private String splitInfoString;

	public YahooFinanceChartPageHtmlAnalyzer() {
		super();
	}

	public void analyze(Document doc) {
		Elements tableElements = doc
				.select(CSS_QUERY_IN_CHART_PAGE_TO_FIND_TABLE);
		if (tableElements == null || tableElements.size() < 1) {
			System.out.println("Error: Cannot find table element.");
			return;
		}
		analyzeTable(tableElements.get(0));
	}

	private void analyzeTable(Element table) {
		for (Element theadtbody : table.children()) {
			for (Element tr : theadtbody.children()) {
				analyzeTr(tr);
			}
		}
	}

	private void analyzeTr(Element tr) {
		boolean nextIsSplit = false;
		for (Element thtd : tr.children()) {
			String text = thtd.text().trim();
			if (nextIsSplit) {
				this.splitInfoString = text;
			}

			if (text.equals(CAPTION_IN_CHART_PAGE_OF_SPLIT_INFORMATION)) {
				nextIsSplit = true;
			} else {
				nextIsSplit = false;
			}
		}
	}

	public String getSplitInfoString() {
		return this.splitInfoString;
	}
}
