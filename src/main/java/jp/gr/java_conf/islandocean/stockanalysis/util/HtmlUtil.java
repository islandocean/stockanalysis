package jp.gr.java_conf.islandocean.stockanalysis.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public final class HtmlUtil {

	private HtmlUtil() {
	}

	public static Document readRemoteHtml(String spec) throws IOException {
		Document doc = Jsoup.connect(spec).get();
		return doc;
	}

	public static Document readLocalHtml(String filename) throws IOException {
		Document doc = readLocalHtml(filename, StandardCharsets.UTF_8.name());
		return doc;
	}

	public static Document readLocalHtml(String filename, String charesetName)
			throws IOException {
		Document doc = Jsoup.parse(new File(filename), charesetName);
		return doc;
	}
}
