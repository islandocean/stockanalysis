package jp.gr.java_conf.islandocean.stockanalysis.jreitportal;

import java.io.IOException;
import java.util.List;

import org.jsoup.nodes.Document;

public class MainSaveJreitCsv {

	public MainSaveJreitCsv() {
		super();
	}

	private static Document selectHtml(JreitManager jreitManager)
			throws IOException {
		Document doc = jreitManager.readRemoteHtml();
		return doc;
	}

	public static void main(String[] args) throws IOException {

		JreitManager jreitManager = JreitManager.getInstance();
		Document doc = selectHtml(jreitManager);
		JreitPortalPageHtmlAnalyzer analyzer = new JreitPortalPageHtmlAnalyzer();
		analyzer.analyze(doc);
		List<JreitRecord> records = analyzer.getJreitRecords();
		if (records == null || records.size() == 0) {
			System.out.println("Error: Cannot find table data from HTML page.");
		} else {
			jreitManager.saveLocalCsv(records);

			for (JreitRecord record : records) {
				System.out.println(record.toTsvString());
			}
		}
	}
}
