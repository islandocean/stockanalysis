package jp.gr.java_conf.islandocean.stockanalysis.finance;

import java.io.File;

import jp.gr.java_conf.islandocean.stockanalysis.util.AbstractConfig;

public class Config extends AbstractConfig {

	private static final String BASE_PATH = "finance" + File.separator;
	private static final String REMOTE_LOCATION_STOCKS = "http://stocks.finance.yahoo.co.jp/stocks/";
	private static final String REMOTE_LOCATION_STOCKS_DETAIL = REMOTE_LOCATION_STOCKS
			+ "detail/?code=";
	private static final String REMOTE_LOCATION_STOCKS_CHART = REMOTE_LOCATION_STOCKS
			+ "chart/?code=";
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

	private static final String SPLIT_INFORMATION = "split_information.csv";
	private static final String SPLIT_INFORMATION_FILENAME = getBaseFolder()
			+ SPLIT_INFORMATION;

	private Config() {
	}

	public static String getBaseFolder() {
		return baseFolder;
	}

	public static String getRemoteLocationStocks() {
		return REMOTE_LOCATION_STOCKS;
	}

	public static String getRemoteLocationStocksDetail() {
		return REMOTE_LOCATION_STOCKS_DETAIL;
	}

	public static String getRemoteLocationStocksChart() {
		return REMOTE_LOCATION_STOCKS_CHART;
	}

	public static String getSplitInformationFilename() {
		return SPLIT_INFORMATION_FILENAME;
	}
}
