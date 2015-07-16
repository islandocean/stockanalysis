package jp.gr.java_conf.islandocean.stockanalysis.common;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

abstract public class AbstractConfig {

	private static final String PROPERTY_FILENAME = "stock.properties";
	private static final String KEY_ROOT_FOLDER = "rootFolder";
	private static final String ENVNAME_USERPROFILE = "%USERPROFILE%";
	private static final String ENVNAME_HOME = "$HOME";

	private static final Properties properties = loadProperties();
	private static final String rootFolder = normalizeFolder(getProperty(KEY_ROOT_FOLDER));
	private static final String absoluteRootFolder = createFolder(rootFolder);

	protected AbstractConfig() {
		super();
	}

	private static Properties loadProperties() {
		Properties props = new Properties();
		InputStream inStream = null;
		try {
			inStream = new BufferedInputStream(new FileInputStream(
					PROPERTY_FILENAME));
			props.load(inStream);
		} catch (IOException e) {
			throw new RuntimeException(
					"Internal error. Failed to load property file. Cannot continue execution. Property filename="
							+ PROPERTY_FILENAME, e);
		} finally {
			try {
				if (inStream != null) {
					inStream.close();
				}
			} catch (IOException e) {
			}
		}
		return props;
	}

	private static String normalizeFolder(String org) {
		if (org == null || org.length() == 0) {
			return org;
		}

		String head = "";
		String userHome = System.getProperty("user.home");
		if (org.startsWith(ENVNAME_USERPROFILE)) {
			head = userHome;
			org = org.substring(ENVNAME_USERPROFILE.length());
		} else if (org.startsWith(ENVNAME_HOME)) {
			head = userHome;
			org = org.substring(ENVNAME_HOME.length());
		}

		int len = org.length();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; ++i) {
			char c = org.charAt(i);
			if (c == '/' || c == '\\') {
				sb.append(File.separatorChar);
			} else {
				sb.append(c);
			}
		}
		if (!sb.toString().endsWith(File.separator)) {
			sb.append(File.separatorChar);
		}
		String ret = head + sb.toString();
		return ret;
	}

	private static String createFolder(String folder) {
		if (folder == null || folder.length() == 0) {
			throw new RuntimeException(
					"Internal error. Failed to get root data folder path. Cannot continue execution. ");
		}
		File rootFile = new File(folder);
		if (!rootFile.exists()) {
			rootFile.mkdirs();
		}
		if (!rootFile.exists()) {
			throw new RuntimeException(
					"Internal error. Failed to create root data folder. Cannot continue execution. Folder="
							+ rootFile.getAbsolutePath());
		}
		return rootFile.getAbsolutePath() + File.separator;
	}

	protected static String getProperty(String key) {
		return properties.getProperty(key);
	}

	protected static String getAbsoluteRootFolder() {
		return absoluteRootFolder;
	}

	public static void main(String[] args) {
		System.out.println("rootFolder=" + rootFolder);
		System.out
				.println("getAbsoluteRootFolder()=" + getAbsoluteRootFolder());
	}
}
