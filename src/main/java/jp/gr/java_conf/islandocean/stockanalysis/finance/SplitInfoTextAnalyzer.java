package jp.gr.java_conf.islandocean.stockanalysis.finance;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jp.gr.java_conf.islandocean.stockanalysis.util.CalendarUtil;

public class SplitInfoTextAnalyzer {

	public static final String DELIM_LINE = "\t";
	private static final char DELIM_ORG_SPLIT_INFORMATION_TEXT = '、';
	public static final char DELIM_NORMALIZED = ',';
	private static final String NO_SPLIT_DATA = "なし";

	List<StockSplitInfo> stockSplitInfoList;

	public SplitInfoTextAnalyzer() {
		super();
	}

	public void analyze(List<String> lines) {
		this.stockSplitInfoList = new ArrayList<StockSplitInfo>();
		for (int idxLine = 0; idxLine < lines.size(); ++idxLine) {
			String line = lines.get(idxLine);
			// System.out.println("idxLine=" + idxLine + " Line=" + line);
			if (line == null || line.length() < 1) {
				System.out.println("Warning: Invalid line. line number="
						+ idxLine + " line=" + line);
				continue;
			}
			int idx = line.indexOf(DELIM_LINE);
			if (idx < 0) {
				System.out
						.println("Warning: Invalid line. Not contain delimiter. line number="
								+ idxLine + " line=" + line);
				continue;
			}
			String code = line.substring(0, idx);
			if (code == null || code.length() <= 0) {
				System.out.println("Warning: Invalid code. line number="
						+ idxLine + " line=" + line);
				continue;
			}
			// System.out.println("code=" + code);
			String allSplitsStr = line.substring(idx + 1);
			if (allSplitsStr.equals("null") || allSplitsStr.equals("")) {
				StockSplitInfo stockSplitInfo = new StockSplitInfo();
				stockSplitInfo.setSplitSearchStockCode(code);
				stockSplitInfo.setExplicitlyNoSplit(false);
				stockSplitInfoList.add(stockSplitInfo);
				// System.out
				// .println("Info: No split data for this stock. line number="
				// + idxLine + " line=" + line);
				continue;
			}

			if (allSplitsStr.equals(NO_SPLIT_DATA)) {
				StockSplitInfo stockSplitInfo = new StockSplitInfo();
				stockSplitInfo.setSplitSearchStockCode(code);
				stockSplitInfo.setExplicitlyNoSplit(true);
				stockSplitInfoList.add(stockSplitInfo);
				continue;
			}

			allSplitsStr = allSplitsStr.replace(
					DELIM_ORG_SPLIT_INFORMATION_TEXT, DELIM_NORMALIZED);

			String[] splits = allSplitsStr.split("" + DELIM_NORMALIZED);
			List<StockSplit> stockSplitList = new ArrayList<StockSplit>();
			for (int idxSplit = 0; idxSplit < splits.length; ++idxSplit) {
				String split = splits[idxSplit];
				StockSplit stockSplit = analyzeStockSplit(split);
				if (stockSplit == null) {
					System.out
							.println("Warning: stockSplit is null. continue...");
					continue;
				}
				// System.out.println("stockSplit=" + stockSplit.toString());
				stockSplitList.add(stockSplit);
			}

			if (stockSplitList.size() > 0) {
				StockSplitInfo stockSplitInfo = new StockSplitInfo();
				stockSplitInfo.setSplitSearchStockCode(code);
				stockSplitInfo.setStockSplitList(stockSplitList);
				stockSplitInfoList.add(stockSplitInfo);
			}
		}
	}

	private StockSplit analyzeStockSplit(String split) {
		if (split == null) {
			return null;
		}
		split = split.trim();

		// multi byte brace '(',')' to ascii brace.
		split = Normalizer.normalize(split, Normalizer.Form.NFKC);

		if (split.length() < 9) { // ?:?]?/?/?
			return null;
		}

		String[] ratioAndDate;
		ratioAndDate = split.split("]");
		if (ratioAndDate == null || ratioAndDate.length < 2) {
			ratioAndDate = split.split("(");
		}
		if (ratioAndDate == null || ratioAndDate.length < 2) {
			System.out
					.print("Error: Failed to devide split to ratio and date.");
			System.out.println("split=" + split);
			return null;
		}
		String ratioStr = ratioAndDate[0];
		String dateStr = ratioAndDate[1];

		String[] ratios = ratioStr.split(":");
		if (ratios == null || ratios.length < 2) {
			System.out
					.print("Error: Failed to devide ratio information to pre an post part.");
			System.out.println("split=" + split);
			return null;
		}
		String preSplitSharesStr = ratios[0];
		String postSplitSharesStr = ratios[1];

		String[] dates = dateStr.split("/");
		if (dates == null || dates.length < 3) {
			System.out
					.print("Error: Failed to devide date information to year,month,day.");
			System.out.println("split=" + split);
			return null;
		}
		String yearStr = dates[0];
		String monthStr = dates[1];
		String dayStr = dates[2];

		preSplitSharesStr = toDigitAndPeriodOnly(preSplitSharesStr);
		postSplitSharesStr = toDigitAndPeriodOnly(postSplitSharesStr);
		yearStr = toDigitAndPeriodOnly(yearStr);
		monthStr = toDigitAndPeriodOnly(monthStr);
		dayStr = toDigitAndPeriodOnly(dayStr);

		StockSplit stockSplit = new StockSplit();
		stockSplit.setPreSplitShares(Double.parseDouble(preSplitSharesStr));
		stockSplit.setPostSplitShares(Double.parseDouble(postSplitSharesStr));
		Calendar splitDay = CalendarUtil.createDay(
				conv2DigitYearTo4DigitYear(Integer.parseInt(yearStr)),
				Integer.parseInt(monthStr) - 1, Integer.parseInt(dayStr));
		stockSplit.setSplitDay(splitDay);

		return stockSplit;
	}

	private String toDigitAndPeriodOnly(String org) {
		int len;
		if (org == null || (len = org.length()) == 0) {
			return org;
		}
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; ++i) {
			char c = org.charAt(i);
			if (!Character.isDigit(c) && c != '.') {
				continue;
			}
			sb.append(c);
		}
		return sb.toString();
	}

	private int conv2DigitYearTo4DigitYear(int year) {
		if (year >= 1000) {
			return year;
		}
		if (year >= 100 || year < 0) {
			throw new IllegalArgumentException(
					"2 Digit year must be between 0 and 99. Specified year="
							+ year);
		}
		if (year >= 80) {
			return year + 1900;
		}
		return year + 2000;
	}

	public List<StockSplitInfo> getStockSplitInfoList() {
		return this.stockSplitInfoList;
	}
}
