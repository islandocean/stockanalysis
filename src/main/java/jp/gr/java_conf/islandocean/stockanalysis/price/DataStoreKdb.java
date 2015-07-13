package jp.gr.java_conf.islandocean.stockanalysis.price;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.gr.java_conf.islandocean.stockanalysis.util.CalendarUtil;
import jp.gr.java_conf.islandocean.stockanalysis.util.IOUtil;
import jp.gr.java_conf.islandocean.stockanalysis.util.Util;

public class DataStoreKdb extends DataStore {

	private static final String STORE_DB_FOLDER = Config.getBaseFolder()
			+ "kdb" + File.separator;
	private static final String ENCODING = "MS932";
	private static final String DELIM = ",";
	private static final String STOCK_CODE_SUFFIX_OF_DEFAULT_MARKET = "-T";
	private static final boolean skipFirstLine = true;
	private static final boolean skipSecondLine = true;

	public DataStoreKdb() {
		super(STORE_DB_FOLDER, ENCODING, DELIM,
				STOCK_CODE_SUFFIX_OF_DEFAULT_MARKET);
	}

	@Override
	public String getRelativeDownloadFilePath(Calendar cal) {
		String folder = CalendarUtil.format_yyyy(cal) + File.separator;
		String filename = "stocks_"
				+ CalendarUtil.format_yyyy_MM_dd_SeparatedByHyphen(cal)
				+ ".csv";
		return folder + filename;
	}

	@Override
	public String[] getRelativeCsvFilePathCandidates(Calendar cal) {
		return new String[] { getRelativeDownloadFilePath(cal) };
	}

	@Override
	public String[] getRemoteUrlHeadCandidates() {
		return Config.getRemoteLocationCandidatesKdb();
	}

	@Override
	public String[] getRemoteUrlTailCandidates(Calendar cal) {
		return new String[] { CalendarUtil
				.format_yyyy_MM_dd_SeparatedByHyphen(cal) + "?download=csv" };
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
		if (fields.length != CsvKdbEnum.values().length) {
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
				field = fields[CsvKdbEnum.STOCK_CODE.ordinal()];
				break;
			case MARKET:
				field = fields[CsvKdbEnum.MARKET.ordinal()];
				break;
			case STOCK_NAME:
				field = fields[CsvKdbEnum.STOCK_NAME.ordinal()];
				break;
			case SECTOR:
				field = fields[CsvKdbEnum.SECTOR.ordinal()];
				break;
			case OPENING_PRICE:
				field = fields[CsvKdbEnum.OPENING_PRICE.ordinal()];
				break;
			case HIGH_PRICE:
				field = fields[CsvKdbEnum.HIGH_PRICE.ordinal()];
				break;
			case LOW_PRICE:
				field = fields[CsvKdbEnum.LOW_PRICE.ordinal()];
				break;
			case CLOSING_PRICE:
				field = fields[CsvKdbEnum.CLOSING_PRICE.ordinal()];
				break;
			case TRADING_VOLUME_OF_STOCKS:
				field = fields[CsvKdbEnum.TRADING_VOLUME_OF_STOCKS.ordinal()];
				break;
			case TRADING_VALUE_OF_MONEY:
				field = fields[CsvKdbEnum.TRADING_VALUE_OF_MONEY.ordinal()];
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
		List<String> texts = IOUtil.readLocalText(fullPathTemp, getEncoding());
		if (texts != null && texts.size() > 0) {
			String topLine = texts.get(0);
			System.out.println("Info: TopLine=" + topLine);
			String yyyy = CalendarUtil.format_yyyy(cal);
			String MM = CalendarUtil.format_MM(cal);
			String dd = CalendarUtil.format_dd(cal);
			String regex = ".*" + yyyy + ".*" + MM + ".*" + dd + ".*";
			Pattern pattern = Pattern.compile(regex);
			Matcher m = pattern.matcher(topLine);
			if (m.find()) {
				ret = true;
			}
		}
		return ret;
	}

	@Override
	public void processAfterDownload(String fullPath, Calendar day)
			throws IOException {
	}
}
