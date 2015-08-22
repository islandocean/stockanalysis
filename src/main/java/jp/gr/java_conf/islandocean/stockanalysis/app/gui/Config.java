package jp.gr.java_conf.islandocean.stockanalysis.app.gui;

import java.io.File;

import jp.gr.java_conf.islandocean.stockanalysis.common.AbstractConfig;

public class Config extends AbstractConfig {

	private static final String BASE_PATH = "pref" + File.separator;

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
}
