package jp.gr.java_conf.islandocean.stockanalysis.price;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jp.gr.java_conf.islandocean.stockanalysis.util.CalendarRange;
import jp.gr.java_conf.islandocean.stockanalysis.util.CalendarUtil;
import jp.gr.java_conf.islandocean.stockanalysis.util.IOUtil;
import jp.gr.java_conf.islandocean.stockanalysis.util.Util;

abstract public class DataStore {

	private static final String NO_DATA_FIELD = "-";
	private static final String TEMP_SUFFIX_OF_DOWNLOAD_FILE = ".temp";

	private String storeDbFolder;
	private String encoding;
	private String delim;
	private String stockCodeSuffixOfDefaultMarket;

	abstract public String getRelativeDownloadFilePath(Calendar cal);

	abstract public String[] getRelativeCsvFilePathCandidates(Calendar cal);

	abstract public String[] getRemoteUrlHeadCandidates();

	abstract public String[] getRemoteUrlTailCandidates(Calendar cal);

	abstract public List<StockRecord> createStockRecords(Calendar day,
			List<String> lines);

	abstract public StockRecord createStockRecord(Calendar day, String line);

	abstract public boolean isValid(String fullPathTemp, Calendar day)
			throws IOException;

	abstract public void processAfterDownload(String fullPath, Calendar day)
			throws IOException;

	public DataStore(String storeDbFolder, String encoding, String delim,
			String stockCodeSuffixOfDefaultMarket) {
		super();
		this.storeDbFolder = storeDbFolder;
		this.encoding = encoding;
		this.delim = delim;
		this.stockCodeSuffixOfDefaultMarket = stockCodeSuffixOfDefaultMarket;
	}

	public boolean isNoDataField(String field) {
		if (field == null || field.length() == 0 || field.equals(NO_DATA_FIELD)) {
			return true;
		}
		return false;
	}

	public int download(CalendarRange calendarRange) {
		Calendar today = CalendarUtil.createToday();
		Calendar begin = calendarRange.getBegin();
		Calendar end = calendarRange.getEnd();
		int count = 0;
		String dbFolder = getStoreDbFolder();
		System.out.println("Info: dbFolder=" + dbFolder);
		FileSystem fs = FileSystems.getDefault();
		Calendar day = (Calendar) end.clone();
		String[] headCandidates = getRemoteUrlHeadCandidates();
		List<String> headCandidateList = Util.toList(headCandidates);
		for (; day.compareTo(begin) >= 0; day.add(Calendar.DAY_OF_MONTH, -1)) {
			if (day.compareTo(today) > 0) {
				System.out.println("Info: Skip future day="
						+ CalendarUtil.format_yyyyMMdd(day));
				continue;
			}

			String relativeDownloadFilePath = getRelativeDownloadFilePath(day);
			String fullPathRegular = dbFolder + relativeDownloadFilePath;
			Path pathRegular = fs.getPath(fullPathRegular);

			System.out.println();
			System.out.println("Info: fullPath=" + fullPathRegular);

			// Create parent folder if does not exist.
			int idx = fullPathRegular.lastIndexOf(File.separator);
			String parent = fullPathRegular.substring(0, idx);
			File newfile = new File(parent);
			newfile.mkdirs();

			try {
				if (Files.exists(pathRegular)) {
					Files.delete(pathRegular);
				}
			} catch (IOException e) {
				if (Files.exists(pathRegular)) {
					System.out.println("Warning: Cannot delete old file. file="
							+ fullPathRegular);
				}
				continue;
			}

			String[] tailCandidates = getRemoteUrlTailCandidates(day);
			loop: for (String headCandidate : headCandidateList) {
				for (String tailCandidate : tailCandidates) {
					String spec = headCandidate + tailCandidate;
					System.out.println("Info: Remote spec=" + spec);
					try {
						downloadSingleFile(spec, day, fullPathRegular,
								pathRegular);
					} catch (IOException e) {
						continue;
					}
					if (Files.exists(pathRegular)) {
						headCandidateList.remove(headCandidate);
						headCandidateList.add(0, headCandidate);
						break loop;
					}
				}
			}

			if (Files.exists(pathRegular)) {
				try {
					processAfterDownload(fullPathRegular, day);
					++count;
				} catch (IOException e) {
					// TODO
					e.printStackTrace();
					System.out
							.println("Warning: Failed to process after download.");
				}

			} else {
				System.out.println("Warning: Failed to download.");
			}
		}
		return count;
	}

	private void downloadSingleFile(String spec, Calendar day,
			String fullPathRegular, Path pathRegular) throws IOException {

		FileSystem fs = FileSystems.getDefault();
		String fullPathTemp = fullPathRegular + TEMP_SUFFIX_OF_DOWNLOAD_FILE;
		Path pathTemp = fs.getPath(fullPathTemp);

		try {
			Files.delete(pathTemp);
		} catch (IOException eTemp) {
		}

		// Copy HTTP to file.
		try {
			IOUtil.copyRemoteToLocal(spec, fullPathTemp);
		} catch (IOException e) {
			System.out.println("Error: Failed to download from remote site.");
			throw e;
		}

		// Check file.
		boolean valid = false;
		try {
			valid = isValid(fullPathTemp, day);
		} catch (IOException e) {
			// TODO
			e.printStackTrace();
			System.out.println("Error: Failed to validate file.");
			throw e;
		}

		// Rename temp filename to regular file.
		if (valid) {
			try {
				Files.move(pathTemp, pathRegular,
						StandardCopyOption.REPLACE_EXISTING);
				System.out
						.println("Info: Succeeded in downloading and renaming.");
			} catch (IOException e) {
				// TODO
				e.printStackTrace();
				System.out.println("Error: Failed to rename file.");
			}
		} else {
			System.out.println("Info: Invalid file. It is deleted.");
			try {
				Files.delete(pathTemp);
			} catch (IOException eTemp) {
			}
		}
	}

	public List<StockRecord> createStockRecords(Calendar day,
			List<String> lines, boolean skipFirstLine, boolean skipSecondLine) {
		List<StockRecord> records = new ArrayList<StockRecord>();
		for (int i = 0; i < lines.size(); ++i) {
			String line = lines.get(i);
			if (i == 0 && skipFirstLine) {
				continue;
			}
			if (i == 1 && skipSecondLine) {
				continue;
			}
			StockRecord record = null;
			try {
				record = createStockRecord(day, line);
			} catch (NumberFormatException e) {
				System.out.println(e);
			}

			if (record != null) {
				records.add(record);
			} else {
				// TODO: debug
				System.out.println("Warning: Invalid line. line=" + line);
			}
		}
		return records;
	}

	public String getStoreDbFolder() {
		return storeDbFolder;
	}

	protected void setStoreDbFolder(String storeDbFolder) {
		this.storeDbFolder = storeDbFolder;
	}

	public String getEncoding() {
		return encoding;
	}

	protected void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getDelim() {
		return delim;
	}

	protected void setDelim(String delim) {
		this.delim = delim;
	}

	public String getStockCodeSuffixOfDefaultMarket() {
		return stockCodeSuffixOfDefaultMarket;
	}

	protected void setStockCodeSuffixOfDefaultMarket(
			String stockCodeSuffixOfDefaultMarket) {
		this.stockCodeSuffixOfDefaultMarket = stockCodeSuffixOfDefaultMarket;
	}
}
