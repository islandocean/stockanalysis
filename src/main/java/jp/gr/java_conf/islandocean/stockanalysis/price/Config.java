package jp.gr.java_conf.islandocean.stockanalysis.price;

import java.io.File;

import jp.gr.java_conf.islandocean.stockanalysis.common.AbstractConfig;

public class Config extends AbstractConfig {

	private static final String BASE_PATH = "stockPrice" + File.separator;

	private static final String[] REMOTE_LOCATION_CANDIDATES_KDB = { "http://k-db.com/stocks/" };

	private static final String[] REMOTE_LOCATION_CANDIDATES_SOUKO = {
			"http://www.geocities.co.jp/WallStreet-Stock/9256/",
			"http://1st.geocities.jp/p777org/",
			"http://www.geocities.jp/p99org/",
			"http://www.geocities.co.jp/WallStreet-Stock/8619/" };

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

	public static String[] getRemoteLocationCandidatesKdb() {
		return REMOTE_LOCATION_CANDIDATES_KDB;
	}

	public static String[] getRemoteLocationCandidatesSouko() {
		return REMOTE_LOCATION_CANDIDATES_SOUKO;
	}
}
