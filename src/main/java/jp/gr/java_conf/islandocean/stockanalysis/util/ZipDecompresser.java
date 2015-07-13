package jp.gr.java_conf.islandocean.stockanalysis.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class ZipDecompresser {

	public void unzip(File file, boolean createUnzipFolder)
			throws ZipException, IOException {

		File folder;
		if (createUnzipFolder) {
			String filename = file.getName();
			int index = filename.lastIndexOf(".");
			if (index < 0) {
				throw new ZipException(
						"Failed to unzip file. Filename does not end with .zip.");
			}
			String folderName = filename.substring(0, index);
			folder = new File(file.getParent(), folderName);
			if (!folder.exists()) {
				if (!folder.mkdir()) {
					throw new IOException("Failed to create directory. dir="
							+ folder + ".");
				}
			}
		} else {
			folder = new File(file.getParent());
		}

		ZipFile zipFile = null;
		try {
			zipFile = new ZipFile(file);
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				File outFile = new File(folder, entry.getName());
				if (entry.isDirectory()) {
					outFile.mkdirs();
					continue;
				}

				BufferedInputStream in = null;
				BufferedOutputStream out = null;
				try {
					in = new BufferedInputStream(zipFile.getInputStream(entry));
					if (!outFile.getParentFile().exists()) {
						outFile.getParentFile().mkdirs();
					}
					out = new BufferedOutputStream(
							new FileOutputStream(outFile));
					int available;
					while ((available = in.available()) > 0) {
						byte[] bytes = new byte[available];
						in.read(bytes);
						out.write(bytes);
					}
				} catch (FileNotFoundException e) {
					throw e;
				} catch (IOException e) {
					throw e;
				} finally {
					try {
						if (in != null) {
							in.close();
						}
					} catch (IOException e) {
					}
					try {
						if (out != null) {
							out.close();
						}
					} catch (IOException e) {
					}
				}
			}
		} finally {
			if (zipFile != null) {
				zipFile.close();
			}
		}
	}
}