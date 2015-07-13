package jp.gr.java_conf.islandocean.stockanalysis.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public final class IOUtil {

	private IOUtil() {
	}

	public static List<String> readLocalText(String filename, String encoding)
			throws IOException {
		FileSystem fs = FileSystems.getDefault();
		Path src = fs.getPath(filename);
		List<String> list = Files.readAllLines(src, Charset.forName(encoding));
		return list;
	}

	public static List<String> readRemoteText(String spec, String encoding)
			throws IOException {
		HttpURLConnection connect = null;
		InputStream in = null;
		BufferedReader reader = null;
		List<String> list = new ArrayList<String>();

		URL url = null;
		try {
			url = new URL(spec);
		} catch (MalformedURLException e) {
			throw e;
		}

		try {
			connect = (HttpURLConnection) url.openConnection();
			in = connect.getInputStream();
			reader = new BufferedReader(new InputStreamReader(in, encoding));
			String line;
			while ((line = reader.readLine()) != null) {
				list.add(line);
			}
			return list;
		} catch (IOException e) {
			throw e;
		} finally {
			if (reader != null) {
				reader.close();
			}
			if (in != null) {
				in.close();
			}
			if (connect != null) {
				connect.disconnect();
			}
		}
	}

	public static void copyRemoteToLocal(String spec, String filename)
			throws IOException {
		FileSystem fs = FileSystems.getDefault();
		HttpURLConnection connect = null;
		InputStream in = null;

		URL url = null;
		try {
			url = new URL(spec);
		} catch (MalformedURLException e) {
			throw e;
		}

		Path dst = fs.getPath(filename);
		try {
			connect = (HttpURLConnection) url.openConnection();
			in = connect.getInputStream();
			Files.copy(in, dst, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			throw e;
		} finally {
			if (in != null) {
				in.close();
			}
			if (connect != null) {
				connect.disconnect();
			}
		}
	}
}
