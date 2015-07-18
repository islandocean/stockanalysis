package jp.gr.java_conf.islandocean.stockanalysis.jreitportal;

import java.util.ArrayList;
import java.util.List;

import jp.gr.java_conf.islandocean.stockanalysis.util.Util;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JreitPortalPageHtmlAnalyzer {

	private static final String CSS_QUERY_TO_FIND_JREIT_TABLE = ".list-rimawari";
	private List<JreitRecord> jreitRecords;

	public JreitPortalPageHtmlAnalyzer() {
		super();
	}

	public void analyze(Document doc) {
		Elements tables = doc.select(CSS_QUERY_TO_FIND_JREIT_TABLE);
		if (tables == null) {
			this.jreitRecords = null;
		}
		Element table = tables.first();
		List<JreitRecord> records = analyzeTable(table);
		this.jreitRecords = records;
	}

	private List<JreitRecord> analyzeTable(Element table) {
		List<JreitRecord> records = new ArrayList<JreitRecord>();
		for (Element theadtbody : table.children()) {
			for (Element tr : theadtbody.children()) {
				JreitRecord record = analyzeTr(tr);
				if (record != null) {
					records.add(record);
				}
			}
		}
		return records;
	}

	private JreitRecord analyzeTr(Element tr) {
		List<String> texts = analyzeThtd(tr);
		JreitRecord record = new JreitRecord();
		Enum<?>[] allKeys = record.getEnumConstants();
		for (int idx = 0; idx < texts.size(); ++idx) {
			JreitEnum key = (JreitEnum) allKeys[idx];
			String text = (String) texts.get(idx);
			record.put(key, text);
		}
		return record;
	}

	private List<String> analyzeThtd(Element tr) {
		List<String> texts = new ArrayList<String>();
		for (Element thtd : tr.children()) {
			String text = thtd.text();
			text = text.trim();
			text = Util.removeCommaAndNbsp(text);
			texts.add(text);
		}
		return texts;
	}

	public List<JreitRecord> getJreitRecords() {
		return jreitRecords;
	}
}
