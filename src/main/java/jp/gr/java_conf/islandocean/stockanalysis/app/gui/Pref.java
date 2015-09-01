package jp.gr.java_conf.islandocean.stockanalysis.app.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Pref {
	private String preferenceFilename;
	private Properties properties;

	public Pref(Class clazz) {
		super();

		String className = clazz.getSimpleName();
		preferenceFilename = Config.getBaseFolder() + className + ".properties";
		File prefFile = new File(preferenceFilename);
		if (!prefFile.exists()) {
			Properties emptyProperties = new Properties();
			try {
				emptyProperties.store(new FileOutputStream(preferenceFilename),
						"Comments");
			} catch (IOException e) {
				throw new RuntimeException(
						"Error: Internal error. Failed to create property file. Cannot continue execution. File="
								+ prefFile.getAbsolutePath(), e);
			}
		}

		this.properties = new Properties();
		try {
			properties.load(new FileInputStream(preferenceFilename));
		} catch (IOException e) {
			throw new RuntimeException(
					"Error: Internal error. Failed to create property file. Cannot continue execution. File="
							+ preferenceFilename, e);
		}
	}

	public Object getProperty(String key) {
		return properties.getProperty(key);
	}

	public Object setProperty(String key, String value) {
		return properties.setProperty(key, value);
	}

	public void save() throws FileNotFoundException, IOException {
		properties.store(new FileOutputStream(preferenceFilename), "Comments");
	}
}