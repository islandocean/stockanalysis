package jp.gr.java_conf.islandocean.stockanalysis.finance;

import java.io.File;

import jp.gr.java_conf.islandocean.stockanalysis.common.AbstractConfig;

public class Config extends AbstractConfig {

	private static final String BASE_PATH = "finance" + File.separator;
	private static final String REMOTE_LOCATION_STOCKS = "http://stocks.finance.yahoo.co.jp/stocks/";
	private static final String REMOTE_LOCATION_STOCKS_CHART = REMOTE_LOCATION_STOCKS
			+ "chart/?code=";
	private static final String REMOTE_LOCATION_STOCKS_DETAIL = REMOTE_LOCATION_STOCKS
			+ "detail/?code=";
	private static final String REMOTE_LOCATION_STOCKS_PROFILE = REMOTE_LOCATION_STOCKS
			+ "profile/?code=";
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

	private static final String SPLIT_INFORMATION = "split_information";
	private static final String SPLIT_INFORMATION_FILENAME = getBaseFolder()
			+ SPLIT_INFORMATION;

	private static final String DETAIL_INFORMATION = "detail_information";
	private static final String DETAIL_INFORMATION_FILENAME = getBaseFolder()
			+ DETAIL_INFORMATION;

	private static final String PROFILE_INFORMATION = "profile_information";
	private static final String PROFILE_INFORMATION_FILENAME = getBaseFolder()
			+ PROFILE_INFORMATION;

	private Config() {
	}

	public static String getBaseFolder() {
		return baseFolder;
	}

	public static String getRemoteLocationStocks() {
		return REMOTE_LOCATION_STOCKS;
	}

	public static String getRemoteLocationStocksChart() {
		return REMOTE_LOCATION_STOCKS_CHART;
	}

	public static String getRemoteLocationStocksDetail() {
		return REMOTE_LOCATION_STOCKS_DETAIL;
	}

	public static String getRemoteLocationStocksProfile() {
		return REMOTE_LOCATION_STOCKS_PROFILE;
	}

	public static String getSplitInformationFilename() {
		return SPLIT_INFORMATION_FILENAME;
	}

	public static String getSplitInformationExt() {
		return ".csv";
	}

	public static String getDetailInformationFilename() {
		return DETAIL_INFORMATION_FILENAME;
	}

	public static String getDetailInformationExt() {
		return ".csv";
	}

	public static String getProfileInformationFilename() {
		return PROFILE_INFORMATION_FILENAME;
	}

	public static String getProfileInformationExt() {
		return ".csv";
	}
}
