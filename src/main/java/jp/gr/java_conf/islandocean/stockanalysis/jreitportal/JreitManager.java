package jp.gr.java_conf.islandocean.stockanalysis.jreitportal;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import jp.gr.java_conf.islandocean.stockanalysis.util.HtmlUtil;

import org.jsoup.nodes.Document;

public class JreitManager {

	private static JreitManager instance = new JreitManager();

	private JreitManager() {
		super();
	}

	public static JreitManager getInstance() {
		return instance;
	}

	public Document readRemoteHtml() throws IOException {
		Document doc = HtmlUtil.readRemoteHtml(Config.getRemoteLocation());
		return doc;
	}

	public Document readLocalHtml() throws IOException {
		String folder = Config.getBaseFolder();
		Document doc = HtmlUtil
				.readLocalHtml(folder + Config.getFilenameHtml());
		return doc;
	}

	public void saveLocalCsv(List<JreitRecord> records) throws IOException {
		String folder = Config.getBaseFolder();
		File file = new File(folder + Config.getFilenameCsv());
		FileWriter writer = new FileWriter(file);
		for (JreitRecord record : records) {
			writer.write(record.toString() + System.lineSeparator());
		}
		writer.close();
	}
}
