package jp.gr.java_conf.islandocean.stockanalysis.price;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.List;

import jp.gr.java_conf.islandocean.stockanalysis.util.CalendarUtil;
import jp.gr.java_conf.islandocean.stockanalysis.util.Util;
import jp.gr.java_conf.islandocean.stockanalysis.util.ZipDecompresser;

public class DataStoreSouko extends DataStore {

	private static final String STORE_DB_FOLDER = Config.getBaseFolder()
			+ "souko" + File.separator;
	private static final String ENCODING = "MS932";
	private static final String DELIM = "\t";
	private static final String STOCK_CODE_SUFFIX_OF_DEFAULT_MARKET = "";
	private static final boolean skipFirstLine = true;
	private static final boolean skipSecondLine = false;
	private static final String[] soukoFilePrefix = { "y", "d" };

	public DataStoreSouko() {
		super(STORE_DB_FOLDER, ENCODING, DELIM,
				STOCK_CODE_SUFFIX_OF_DEFAULT_MARKET);
	}

	@Override
	public String getRelativeDownloadFilePath(Calendar cal) {
		String folder = CalendarUtil.format_yyyy(cal) + File.separator;
		String filename = CalendarUtil.format_yyMMdd(cal) + ".zip";
		return folder + filename;
	}

	@Override
	public String[] getRelativeCsvFilePathCandidates(Calendar cal) {
		String folder = CalendarUtil.format_yyyy(cal) + File.separator;
		String tail = CalendarUtil.format_yyMMdd(cal) + ".txt";
		String[] a = new String[soukoFilePrefix.length];
		for (int i = 0; i < soukoFilePrefix.length; ++i) {
			a[i] = folder + soukoFilePrefix[i] + tail;
		}
		return a;
	}

	@Override
	public String[] getRemoteUrlHeadCandidates() {
		return Config.getRemoteLocationCandidatesSouko();
	}

	@Override
	public String[] getRemoteUrlTailCandidates(Calendar cal) {
		String tail = CalendarUtil.format_yyMMdd(cal) + ".zip";
		String[] a = new String[soukoFilePrefix.length];
		for (int i = 0; i < soukoFilePrefix.length; ++i) {
			a[i] = soukoFilePrefix[i] + tail;
		}
		return a;
	}

	@Override
	public List<StockRecord> createStockRecords(Calendar day, List<String> lines) {
		List<StockRecord> records = createStockRecords(day, lines,
				skipFirstLine, skipSecondLine);
		return records;
	}

	@Override
	public StockRecord createStockRecord(Calendar day, String line) {

		// TODO: if invaid data, throw exception.

		if (day == null || line == null || line.length() == 0) {
			return null;
		}
		if (line.indexOf(getDelim()) < 0) {
			return null;
		}

		String[] fields = line.split(getDelim());
		if (fields.length != CsvSoukoEnum.values().length) {
			// TODO: warning or error
			System.out
					.println("Warning: Invalid number of fields. Ignored line="
							+ line);
			return null;
		}

		StockRecord record = new StockRecord();
		for (StockEnum stockEnum : StockEnum.values()) {
			Class<?> dataValueClass = stockEnum.getDataValueClass();
			String field = null;
			Object obj = null;

			switch (stockEnum) {
			case DATE:
				obj = day;
				break;
			case STOCK_CODE:
				field = fields[CsvSoukoEnum.STOCK_CODE.ordinal()];
				break;
			case MARKET:
				break;
			case STOCK_NAME:
				field = fields[CsvSoukoEnum.STOCK_NAME.ordinal()];
				break;
			case SECTOR:
				break;
			case OPENING_PRICE:
				field = fields[CsvSoukoEnum.OPENING_PRICE.ordinal()];
				break;
			case HIGH_PRICE:
				field = fields[CsvSoukoEnum.HIGH_PRICE.ordinal()];
				break;
			case LOW_PRICE:
				field = fields[CsvSoukoEnum.LOW_PRICE.ordinal()];
				break;
			case CLOSING_PRICE:
				field = fields[CsvSoukoEnum.CLOSING_PRICE.ordinal()];
				break;
			case TRADING_VOLUME_OF_STOCKS:
				field = fields[CsvSoukoEnum.TRADING_VOLUME_OF_STOCKS.ordinal()];
				break;
			case TRADING_VALUE_OF_MONEY:
				break;
			}

			if (obj == null && field != null) {
				if (!isNoDataField(field)) {
					obj = Util.convClass(field, dataValueClass);
				}
			}
			record.put(stockEnum, obj);
		}

		return record;
	}

	@Override
	public boolean isValid(String fullPathTemp, Calendar cal)
			throws IOException {
		boolean ret = false;
		FileSystem fs = FileSystems.getDefault();
		Path path = fs.getPath(fullPathTemp);
		int size = (int) Files.size(path);
		if (size > 20 * 1024) { // Estimate that small file is invalid.
			ret = true;
		}
		return ret;
	}

	@Override
	public void processAfterDownload(String fullPath, Calendar day)
			throws IOException {
		File file = new File(fullPath);
		ZipDecompresser za = new ZipDecompresser();
		za.unzip(file, false);
		file.delete();
	}
}
