package jp.gr.java_conf.islandocean.stockanalysis.jreitportal;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

import jp.gr.java_conf.islandocean.stockanalysis.util.CalendarUtil;
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
		Document doc = HtmlUtil.readLocalHtml(folder + Config.getFilenameHtml()
				+ Config.getExtHtml());
		return doc;
	}

	public void saveLocalCsv(List<JreitRecord> records) throws IOException {
		FileSystem fs = FileSystems.getDefault();
		String folder = Config.getBaseFolder();

		String tempSuffix = "."
				+ CalendarUtil.format_yyyyMMdd(CalendarUtil.createToday());
		Path pathTemp = fs.getPath(folder + Config.getFilenameCsv()
				+ tempSuffix + Config.getExtCsv());
		BufferedWriter writer = Files.newBufferedWriter(pathTemp,
				StandardCharsets.UTF_8);
		for (JreitRecord record : records) {
			writer.write(record.toTsvString() + System.lineSeparator());
		}
		writer.close();

		Path pathRegular = fs.getPath(folder + Config.getFilenameCsv()
				+ Config.getExtCsv());
		Files.copy(pathTemp, pathRegular, StandardCopyOption.COPY_ATTRIBUTES,
				StandardCopyOption.REPLACE_EXISTING);
	}

	public List<JreitRecord> readRemoteJreitRecords() throws IOException {
		Document doc = readRemoteHtml();
		JreitPortalPageHtmlAnalyzer analyzer = new JreitPortalPageHtmlAnalyzer();
		analyzer.analyze(doc);
		List<JreitRecord> records = analyzer.getJreitRecords();
		return records;
	}
}
