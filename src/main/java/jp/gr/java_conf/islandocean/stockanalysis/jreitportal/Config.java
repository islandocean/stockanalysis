package jp.gr.java_conf.islandocean.stockanalysis.jreitportal;

import java.io.File;

import jp.gr.java_conf.islandocean.stockanalysis.common.AbstractConfig;

public class Config extends AbstractConfig {

	private static final String BASE_PATH = "jreitPortal" + File.separator;
	private static final String REMOTE_LOCATION = "http://www.japan-reit.com/list/rimawari/";
	private static final String FILENAME_HTML = "JAPAN-REIT";
	private static final String EXT_HTML = ".htm";
	private static final String FILENAME_CSV = "JAPAN-REIT";
	private static final String EXT_CSV = ".csv";

	private static final String baseFolder = AbstractConfig
			.getAbsoluteRootFolder() + BASE_PATH;

	static {
		File baseFile = new File(baseFolder);
		if (!baseFile.exists()) {
			baseFile.mkdirs();
		}
		if (!baseFile.exists()) {
			throw new RuntimeException(
					"Error: Internal error. Failed to create base folder. Cannot continue execution. Folder="
							+ baseFile.getAbsolutePath());
		}
	}

	private Config() {
	}

	public static String getBaseFolder() {
		return baseFolder;
	}

	public static String getRemoteLocation() {
		return REMOTE_LOCATION;
	}

	public static String getFilenameHtml() {
		return FILENAME_HTML;
	}

	public static String getExtHtml() {
		return EXT_HTML;
	}

	public static String getFilenameCsv() {
		return FILENAME_CSV;
	}

	public static String getExtCsv() {
		return EXT_CSV;
	}
}
