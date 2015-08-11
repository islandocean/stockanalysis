package jp.gr.java_conf.islandocean.stockanalysis.app.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class ResourceBundleWithUtf8 {
	public static ResourceBundle.Control UTF8_ENCODING_CONTROL = new ResourceBundle.Control() {
		@Override
		public ResourceBundle newBundle(String baseName, Locale locale,
				String format, ClassLoader loader, boolean reload)
				throws IllegalAccessException, InstantiationException,
				IOException {
			String bundleName = toBundleName(baseName, locale);
			String resourceName = toResourceName(bundleName, "properties");
			try (BufferedReader reader = new BufferedReader(
					new InputStreamReader(
							loader.getResourceAsStream(resourceName), "UTF-8"))) {
				return new PropertyResourceBundle(reader);
			}
		}
	};
}