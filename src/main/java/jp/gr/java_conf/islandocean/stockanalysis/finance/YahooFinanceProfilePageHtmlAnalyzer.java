package jp.gr.java_conf.islandocean.stockanalysis.finance;

import java.util.Calendar;
import java.util.Iterator;

import jp.gr.java_conf.islandocean.stockanalysis.common.FailedToFindElementException;
import jp.gr.java_conf.islandocean.stockanalysis.common.InvalidDataException;
import jp.gr.java_conf.islandocean.stockanalysis.util.CalendarUtil;
import jp.gr.java_conf.islandocean.stockanalysis.util.Util;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class YahooFinanceProfilePageHtmlAnalyzer {

	private static final String CSS_QUERY_IN_DETAIL_PAGE_TO_FIND_STOCKS_INFO = ".stocksInfo";
	private static final String CSS_QUERY_IN_DETAIL_PAGE_TO_FIND_STOCKS_TABLE = ".stocksTable";
	private static final String CSS_QUERY_IN_PROFILE_PAGE_TO_FIND_TR_UNDER_DIV_MAIN = "#main table";

	private static final String CAPTION_FEATURE = "特色";
	private static final String CAPTION_CONSOLIDATED_OPERATIONS = "連結事業";
	private static final String CAPTION_LOCATION_OF_HEAD_OFFICE = "本社所在地";
	private static final String CAPTION_NEAREST_STATION = "最寄り駅";
	private static final String CAPTION_TELEPHONE_NUMBER = "電話番号";
	private static final String CAPTION_SECTOR = "業種分類";
	private static final String CAPTION_STOCK_NAME_IN_ENGLISH = "英文社名";
	private static final String CAPTION_REPRESENTATIVE = "代表者名";
	private static final String CAPTION_FOUNDATION_DATE = "設立年月日";
	private static final String CAPTION_MARKET = "市場名";
	private static final String CAPTION_LISTED_DATE = "上場年月日";
	private static final String CAPTION_SETTLING_DATE = "決算";
	private static final String CAPTION_SHARE_UNIT_NUMBER = "単元株数";
	private static final String CAPTION_NON_CONSOLIDATED_NUMBER_OF_EMPLOYEES = "従業員数（単独）";
	private static final String CAPTION_CONSOLIDATED_NUMBER_OF_EMPLOYEES = "従業員数 （連結）";
	private static final String CAPTION_AVERAGE_AGE = "平均年齢";
	private static final String CAPTION_AVERAGE_ANNUAL_SALARY = "平均年収";

	/**
	 * Raw String data
	 */
	private String stockCodeStr;
	private String featureStr;
	private String consolidatedOperationsStr;
	private String locationOfHeadOfficeStr;
	private String nearestStationStr;
	private String telephoneNumberStr;
	private String sectorStr;
	private String stockNameInEnglishStr;
	private String representativeStr;
	private String foundationDateStr;
	private String marketStr;
	private String listedDateStr;
	private String settlingDateStr;
	private String shareUnitNumberStr;
	private String nonConsolidatedNumberOfEmployeesStr;
	private String consolidatedNumberOfEmployeesStr;
	private String averageAgeStr;
	private String averageAnnualSalaryStr;

	//
	private String stockNameStr;

	/**
	 * Special value of raw detail info String
	 */
	private static final String NO_DATA = "---";

	/**
	 * Analyzed data
	 */
	private ProfileRecord profileRecord;

	public YahooFinanceProfilePageHtmlAnalyzer() {
		super();
	}

	public void analyze(Document doc, Calendar dataGetDate)
			throws FailedToFindElementException, InvalidDataException {
		extractDataAsString(doc);
		this.profileRecord = convertStringToRecord(dataGetDate);
	}

	private void extractDataAsString(Document doc)
			throws FailedToFindElementException {
		Elements infoElements = doc
				.select(CSS_QUERY_IN_DETAIL_PAGE_TO_FIND_STOCKS_INFO);
		if (infoElements == null || infoElements.size() < 1) {
			throw new FailedToFindElementException(
					"Cannot find stock info element.");
		}
		Element info = infoElements.get(0);
		if (info != null) {
			Elements dts = info.select("dt");
			if (dts != null) {
				stockCodeStr = dts.text().trim();
			}
			Elements category = info.select(".category");
			if (category != null) {
				sectorStr = category.text().trim();
			}
		}

		Elements stocksTables = doc
				.select(CSS_QUERY_IN_DETAIL_PAGE_TO_FIND_STOCKS_TABLE);
		if (stocksTables == null || stocksTables.size() < 1) {
			throw new FailedToFindElementException(
					"Cannot find stock table element.");
		}
		Element stocksTable = stocksTables.get(0);

		Elements symbol = stocksTable.select(".symbol");
		if (symbol != null) {
			stockNameStr = symbol.text().trim();
		}

		// Elements tds = table.select("td");
		// for (Element td : tds) {
		// String text = Util.normalizeRoundParentheses(td.text().trim());
		// if (text.length() == 0) {
		// } else if (td.classNames().contains("change")) {
		// priceComparisonWithPreviousDayStr = text;
		// } else {
		// try {
		// Double.parseDouble(Util.removeCommaAndNbsp(text));
		// } catch (NumberFormatException e) {
		// continue;
		// }
		// realtimePriceStr = text;
		// }
		// }

		Elements tables = doc
				.select(CSS_QUERY_IN_PROFILE_PAGE_TO_FIND_TR_UNDER_DIV_MAIN);
		Iterator<Element> iterator = tables.iterator();
		Element profileTable = null;
		while (iterator.hasNext()) {
			Element table = iterator.next();
			if (table.attr("class").contains("stocksTable")) {
				// skip stocksTable
				continue;
			}
			profileTable = table;
			break;
		}
		if (profileTable == null) {
			throw new FailedToFindElementException(
					"Failed to find profile table.");
		}
		Elements trs = profileTable.select("tr");
		for (Element tr : trs) {
			Elements ths = tr.getElementsByTag("th");
			Elements tds = tr.getElementsByTag("td");

			for (int i = 0; i < ths.size(); ++i) {
				if (tds.size() <= i) {
					break;
				}
				Element th = ths.get(i);
				Element td = tds.get(i);

				String caption = th.text().trim();
				String value = Util.normalizeRoundParentheses(td.text().trim());

				System.out.println("caption=" + caption);
				System.out.println("value=" + value);
			}

			//
			// TODO: under construction
			//

			/*****
			 * if (caption.startsWith(CAPTION_PREVIOUS_CLOSING_PRICE)) {
			 * previousClosingPriceStr = value; } else if
			 * (caption.startsWith(CAPTION_OPENING_PRICE)) { openingPriceStr =
			 * value; } else if (caption.startsWith(CAPTION_HIGH_PRICE)) {
			 * highPriceStr = value; } else if
			 * (caption.startsWith(CAPTION_LOW_PRICE)) { lowPriceStr = value; }
			 * else if (caption.startsWith(CAPTION_TRADING_VOLUME_OF_STOCKS)) {
			 * tradingVolumeOfStocksStr = value; } else if
			 * (caption.startsWith(CAPTION_TRADING_VALUE_OF_MONEY)) {
			 * tradingValueOfMoneyStr = value; } else if
			 * (caption.startsWith(CAPTION_PRICE_LIMIT)) { priceLimitStr =
			 * value; } else if
			 * (caption.startsWith(CAPTION_MARKET_CAPITALIZATION)) {
			 * marketCapitalizationStr = value; } else if
			 * (caption.startsWith(CAPTION_OUTSTANDING_STOCK_VOLUME)) {
			 * outstandingStockVolumeStr = value; } else if
			 * (caption.startsWith(CAPTION_ANNUAL_INTEREST_RATE)) {
			 * annualInterestRateStr = value; } else if
			 * (caption.startsWith(CAPTION_DIVIDENDS_PER_SHARE)) {
			 * dividendsPerShareStr = value; } else if
			 * (caption.startsWith(CAPTION_PER)) { perStr = value; } else if
			 * (caption.startsWith(CAPTION_PBR)) { pbrStr = value; } else if
			 * (caption.startsWith(CAPTION_EPS)) { epsStr = value; } else if
			 * (caption.startsWith(CAPTION_BPS)) { bpsStr = value; } else if
			 * (caption.startsWith(CAPTION_MINIMUM_PURCHASE_AMOUNT)) {
			 * minimumPurchaseAmountStr = value; } else if
			 * (caption.startsWith(CAPTION_SHARE_UNIT_NUMBER)) {
			 * shareUnitNumberStr = value; } else if
			 * (caption.startsWith(CAPTION_YEARLY_HIGH)) { yearlyHighStr =
			 * value; } else if (caption.startsWith(CAPTION_YEARLY_LOW)) {
			 * yearlyLowStr = value; } else if
			 * (caption.startsWith(CAPTION_NET_ASSETS)) { netAssetsStr = value;
			 * } else if (caption.startsWith(CAPTION_UNIT_OF_TRADING)) {
			 * unitOfTradingStr = value; } else if
			 * (caption.startsWith(CAPTION_MANAGEMENT_COMPANY)) {
			 * managementCompanyStr = value; } else if (caption
			 * .startsWith(CAPTION_TYPE_OF_ASSETS_TO_BE_INVESTED)) {
			 * typeOfAssetsToBeInvestedStr = value; } else if
			 * (caption.startsWith(CAPTION_REGION_TO_BE_INVESTED)) {
			 * regionToBeInvestedStr = value; } else if
			 * (caption.startsWith(CAPTION_UNDERLYING_INDEX)) {
			 * underlyingIndexStr = value; } else if
			 * (caption.startsWith(CAPTION_SETTLEMENT_FREQUENCY)) {
			 * settlementFrequencyStr = value; } else if
			 * (caption.startsWith(CAPTION_SETTLEMENT_MONTH)) {
			 * settlementMonthStr = value; } else if
			 * (caption.startsWith(CAPTION_LISTED_DATE)) { listedDateStr =
			 * value; } else if (caption.startsWith(CAPTION_TRUST_FEE)) {
			 * trustFeeStr = value; } else if
			 * (caption.startsWith(CAPTION_MARGIN_DEBT_BALANCE)) {
			 * marginDebtBalanceStr = value; isDebt = true; isSelling = false; }
			 * else if (caption
			 * .startsWith(CAPTION_MARGIN_RATIO_COMPARISON_WITH_PREVIOUS_WEEK))
			 * { if (isDebt) {
			 * marginDebtBalanceRatioComparisonWithPreviousWeekStr = value; }
			 * else if (isSelling) {
			 * marginSellingBalanceRatioComparisonWithPreviousWeekStr = value; }
			 * } else if (caption.startsWith(CAPTION_MARGIN_SELLING_BALANCE)) {
			 * marginSellingBalanceStr = value; isDebt = false; isSelling =
			 * true; } else if
			 * (caption.startsWith(CAPTION_RATIO_OF_MARGIN_BALANCE)) {
			 * ratioOfMarginBalanceStr = value; } else { if (!caption.equals("")
			 * && !caption.startsWith("値上がり率") && !caption.startsWith("値下がり率")
			 * && !caption.startsWith("[買い]") && !caption.startsWith("[売り]") &&
			 * value.indexOf("リアルタイム株価") < 0) {
			 * 
			 * // // TODO: unknown data format // System.out.println("caption="
			 * + caption); System.out.println("value=" + value); } }
			 ****/
		}
	}

	private ProfileRecord convertStringToRecord(Calendar dataGetDate)
			throws InvalidDataException {
		ProfileRecord record = new ProfileRecord();
		String org = null;
		String s = null;
		/*****
		 * record.put(DetailEnum.DATA_GET_DATE, dataGetDate);
		 * 
		 * try { // コード record.put(DetailEnum.STOCK_CODE, stockCodeStr);
		 * 
		 * // 銘柄名 record.put(DetailEnum.STOCK_NAME, stockNameStr);
		 * 
		 * // 業種 record.put(DetailEnum.SECTOR, sectorStr);
		 * 
		 * // リアルタイム株価 org = realtimePriceStr; if ((s = org) != null) { s =
		 * Util.substringChopStartIfMatch(s, "ストップ高"); s =
		 * Util.substringChopStartIfMatch(s, "ストップ安"); s =
		 * Util.removeCommaAndTrim(s); if (!isNoData(s)) {
		 * record.put(DetailEnum.REALTIME_PRICE, Double.parseDouble(s)); } }
		 * 
		 * // 前日比 org = priceComparisonWithPreviousDayStr; if ((s = org) !=
		 * null) { s = Util.substringBeforeLastOpeningRoundParentheses(s); s =
		 * Util.substringChopStartIfMatch(s, "前日比"); s =
		 * Util.removeCommaAndTrim(s); if (!isNoData(s)) {
		 * record.put(DetailEnum.PRICE_COMPARISON_WITH_PREVIOUS_DAY,
		 * Double.parseDouble(s)); } }
		 * 
		 * // 前日終値 org = previousClosingPriceStr; if ((s = org) != null) { s =
		 * Util.substringBeforeLastOpeningRoundParentheses(s); s =
		 * Util.substringChopStartIfMatch(s, "ストップ高"); s =
		 * Util.substringChopStartIfMatch(s, "ストップ安"); s =
		 * Util.removeCommaAndTrim(s); if (!isNoData(s)) {
		 * record.put(DetailEnum.PREVIOUS_CLOSING_PRICE, Double.parseDouble(s));
		 * } }
		 * 
		 * // 始値 org = openingPriceStr; if ((s = org) != null) { s =
		 * Util.substringBeforeLastOpeningRoundParentheses(s); s =
		 * Util.substringChopStartIfMatch(s, "ストップ高"); s =
		 * Util.substringChopStartIfMatch(s, "ストップ安"); s =
		 * Util.removeCommaAndTrim(s); if (!isNoData(s)) {
		 * record.put(DetailEnum.OPENING_PRICE, Double.parseDouble(s)); } }
		 * 
		 * // 高値 org = highPriceStr; if ((s = org) != null) { s =
		 * Util.substringBeforeLastOpeningRoundParentheses(s); s =
		 * Util.substringChopStartIfMatch(s, "ストップ高"); s =
		 * Util.substringChopStartIfMatch(s, "ストップ安"); s =
		 * Util.removeCommaAndTrim(s); if (!isNoData(s)) {
		 * record.put(DetailEnum.HIGH_PRICE, Double.parseDouble(s)); } }
		 * 
		 * // 安値 org = lowPriceStr; if ((s = org) != null) { s =
		 * Util.substringBeforeLastOpeningRoundParentheses(s); s =
		 * Util.substringChopStartIfMatch(s, "ストップ高"); s =
		 * Util.substringChopStartIfMatch(s, "ストップ安"); s =
		 * Util.removeCommaAndTrim(s); if (!isNoData(s)) {
		 * record.put(DetailEnum.LOW_PRICE, Double.parseDouble(s)); } }
		 * 
		 * // 出来高 org = tradingVolumeOfStocksStr; if ((s = org) != null) { s =
		 * Util.substringBeforeLastOpeningRoundParentheses(s); s =
		 * Util.substringChopEndIfMatch(s, "株"); s = Util.removeCommaAndTrim(s);
		 * if (!isNoData(s)) { record.put(DetailEnum.TRADING_VOLUME_OF_STOCKS,
		 * Double.parseDouble(s)); } }
		 * 
		 * // 売買代金 org = tradingValueOfMoneyStr; if ((s = org) != null) { s =
		 * Util.substringBeforeLastOpeningRoundParentheses(s); s =
		 * Util.substringChopEndIfMatch(s, "千円"); s =
		 * Util.removeCommaAndTrim(s); if (!isNoData(s)) {
		 * record.put(DetailEnum.TRADING_VALUE_OF_MONEY, Double.parseDouble(s));
		 * } }
		 * 
		 * // 値幅制限 org = priceLimitStr; if ((s = org) != null) { s =
		 * Util.substringBeforeLastOpeningRoundParentheses(s); s =
		 * Util.removeCommaAndTrim(s); String[] ar = s.split("～"); if (ar.length
		 * >= 1) { s = ar[0]; if (!isNoData(s)) {
		 * record.put(DetailEnum.LOW_PRICE_LIMIT, Double.parseDouble(s)); } } if
		 * (ar.length >= 2) { s = ar[1]; if (!isNoData(s)) {
		 * record.put(DetailEnum.HIGH_PRICE_LIMIT, Double.parseDouble(s)); } } }
		 * 
		 * // 時価総額 org = marketCapitalizationStr; if ((s = org) != null) { s =
		 * Util.substringBeforeLastOpeningRoundParentheses(s); s =
		 * Util.substringChopEndIfMatch(s, "百万円"); s =
		 * Util.removeCommaAndTrim(s); if (!isNoData(s)) {
		 * record.put(DetailEnum.MARKET_CAPITALIZATION, Double.parseDouble(s));
		 * } }
		 * 
		 * // 発行済株式数 org = outstandingStockVolumeStr; if ((s = org) != null) { s
		 * = Util.substringBeforeLastOpeningRoundParentheses(s); s =
		 * Util.substringChopEndIfMatch(s, "株"); s = Util.removeCommaAndTrim(s);
		 * if (!isNoData(s)) { record.put(DetailEnum.OUTSTANDING_STOCK_VOLUME,
		 * Double.parseDouble(s)); } }
		 * 
		 * // 配当利回り org = annualInterestRateStr; if ((s = org) != null) { s =
		 * Util.substringBeforeLastOpeningRoundParentheses(s); s =
		 * Util.substringChopEndIfMatch(s, "%"); s = Util.removeCommaAndTrim(s);
		 * if (!isNoData(s)) { record.put(DetailEnum.ANNUAL_INTEREST_RATE,
		 * Double.parseDouble(s)); } }
		 * 
		 * // 1株配当 org = dividendsPerShareStr; if ((s = org) != null) { s =
		 * Util.substringBeforeLastOpeningRoundParentheses(s); s =
		 * Util.removeCommaAndTrim(s); if (!isNoData(s)) {
		 * record.put(DetailEnum.DIVIDENDS_PER_SHARE, Double.parseDouble(s)); }
		 * }
		 * 
		 * // PER org = perStr; if ((s = org) != null) { s =
		 * Util.substringBeforeLastOpeningRoundParentheses(s); s =
		 * Util.substringChopStartIfMatch(s, "(連)"); s =
		 * Util.substringChopStartIfMatch(s, "(単)"); s =
		 * Util.substringChopEndIfMatch(s, "倍"); s = Util.removeCommaAndTrim(s);
		 * if (!isNoData(s)) { record.put(DetailEnum.PER,
		 * Double.parseDouble(s)); } }
		 * 
		 * // PBR org = pbrStr; if ((s = org) != null) { s =
		 * Util.substringBeforeLastOpeningRoundParentheses(s); s =
		 * Util.substringChopStartIfMatch(s, "(連)"); s =
		 * Util.substringChopStartIfMatch(s, "(単)"); s =
		 * Util.substringChopEndIfMatch(s, "倍"); s = Util.removeCommaAndTrim(s);
		 * if (!isNoData(s)) { record.put(DetailEnum.PBR,
		 * Double.parseDouble(s)); } }
		 * 
		 * // EPS org = epsStr; if ((s = org) != null) { s =
		 * Util.substringBeforeLastOpeningRoundParentheses(s); s =
		 * Util.substringChopStartIfMatch(s, "(連)"); s =
		 * Util.substringChopStartIfMatch(s, "(単)"); s =
		 * Util.removeCommaAndTrim(s); if (!isNoData(s)) {
		 * record.put(DetailEnum.EPS, Double.parseDouble(s)); } }
		 * 
		 * // BPS org = bpsStr; if ((s = org) != null) { s =
		 * Util.substringBeforeLastOpeningRoundParentheses(s); s =
		 * Util.substringChopStartIfMatch(s, "(連)"); s =
		 * Util.substringChopStartIfMatch(s, "(単)"); s =
		 * Util.removeCommaAndTrim(s); if (!isNoData(s)) {
		 * record.put(DetailEnum.BPS, Double.parseDouble(s)); } }
		 * 
		 * // 最低購入代金 org = minimumPurchaseAmountStr; if ((s = org) != null) { s
		 * = Util.substringBeforeLastOpeningRoundParentheses(s); s =
		 * Util.removeCommaAndTrim(s); if (!isNoData(s)) {
		 * record.put(DetailEnum.MINIMUM_PURCHASE_AMOUNT,
		 * Double.parseDouble(s)); } }
		 * 
		 * // 単元株数 org = shareUnitNumberStr; if ((s = org) != null) { s =
		 * Util.substringChopEndIfMatch(s, "株"); s = Util.removeCommaAndTrim(s);
		 * if (!isNoData(s)) { record.put(DetailEnum.SHARE_UNIT_NUMBER,
		 * Double.parseDouble(s)); } }
		 * 
		 * // 年初来高値 org = yearlyHighStr; if ((s = org) != null) { s =
		 * Util.substringBeforeLastOpeningRoundParentheses(s); s =
		 * Util.substringChopStartIfMatch(s, "更新"); s =
		 * Util.removeCommaAndTrim(s); if (!isNoData(s)) {
		 * record.put(DetailEnum.YEARLY_HIGH, Double.parseDouble(s)); } }
		 * 
		 * // 年初来安値 org = yearlyLowStr; if ((s = org) != null) { s =
		 * Util.substringBeforeLastOpeningRoundParentheses(s); s =
		 * Util.substringChopStartIfMatch(s, "更新"); s =
		 * Util.removeCommaAndTrim(s); if (!isNoData(s)) {
		 * record.put(DetailEnum.YEARLY_LOW, Double.parseDouble(s)); } }
		 * 
		 * // 純資産 org = netAssetsStr; if ((s = org) != null) { s =
		 * Util.substringBeforeLastOpeningRoundParentheses(s); if
		 * (!s.endsWith("百万円")) { // TODO: error or unknown data format } s =
		 * Util.substringChopEndIfMatch(s, "百万円"); s =
		 * Util.removeCommaAndTrim(s); if (!isNoData(s)) {
		 * record.put(DetailEnum.NET_ASSETS, Double.parseDouble(s)); } }
		 * 
		 * // 売買単位 org = unitOfTradingStr; if ((s = org) != null) { s =
		 * Util.substringChopEndIfMatch(s, "株"); s = Util.removeCommaAndTrim(s);
		 * if (!isNoData(s)) { record.put(DetailEnum.UNIT_OF_TRADING,
		 * Double.parseDouble(s)); } }
		 * 
		 * // 運用会社 org = managementCompanyStr; if ((s = org) != null) { s =
		 * s.trim(); if (!isNoData(s)) {
		 * record.put(DetailEnum.MANAGEMENT_COMPANY, s); } }
		 * 
		 * // 投資対象資産 org = typeOfAssetsToBeInvestedStr; if ((s = org) != null) {
		 * s = s.trim(); if (!isNoData(s)) {
		 * record.put(DetailEnum.TYPE_OF_ASSETS_TO_BE_INVESTED, s); } }
		 * 
		 * // 投資対象地域 org = regionToBeInvestedStr; if ((s = org) != null) { s =
		 * s.trim(); if (!isNoData(s)) {
		 * record.put(DetailEnum.REGION_TO_BE_INVESTED, s); } }
		 * 
		 * // 連動対象 org = underlyingIndexStr; if ((s = org) != null) { s =
		 * s.trim(); if (!isNoData(s)) { record.put(DetailEnum.UNDERLYING_INDEX,
		 * s); } }
		 * 
		 * // 決算頻度 org = settlementFrequencyStr; if ((s = org) != null) { s =
		 * Util.substringChopEndIfMatch(s, "回"); s = Util.removeCommaAndTrim(s);
		 * if (!isNoData(s)) { record.put(DetailEnum.SETTLEMENT_FREQUENCY,
		 * Integer.parseInt(s)); } }
		 * 
		 * // 決算月 org = settlementMonthStr; // Comma separated numerics, January
		 * is 1. if ((s = org) != null) { s = Util.substringChopEndIfMatch(s,
		 * "月"); s = s.trim(); if (!isNoData(s)) {
		 * record.put(DetailEnum.SETTLEMENT_MONTH, s); } }
		 * 
		 * // 上場年月日 org = listedDateStr; if ((s = org) != null) { s =
		 * Util.substringChopEndIfMatch(s, "日"); s = s.trim(); int year; int
		 * month; int day; if (s.length() != 0) { String[] p = s.split("年"); if
		 * (p.length != 2) { throw new InvalidDataException(
		 * "Error: listedDateStr is invalid. listedDateStr=" + listedDateStr); }
		 * year = Integer.parseInt(p[0]);
		 * 
		 * String[] q = p[1].split("月"); if (p.length != 2) { throw new
		 * InvalidDataException(
		 * "Error: listedDateStr is invalid. listedDateStr=" + listedDateStr); }
		 * month = Integer.parseInt(q[0]) - 1; day = Integer.parseInt(q[1]);
		 * Calendar cal = CalendarUtil.createDay(year, month, day);
		 * 
		 * if (!isNoData(s)) { record.put(DetailEnum.LISTED_DATE, cal); } } }
		 * 
		 * // 信託報酬（税抜） org = trustFeeStr; if ((s = org) != null) { s =
		 * Util.substringChopEndIfMatch(s, "%"); s = Util.removeCommaAndTrim(s);
		 * if (!isNoData(s)) { record.put(DetailEnum.TRUST_FEE,
		 * Double.parseDouble(s)); } }
		 * 
		 * // 信用買残 org = marginDebtBalanceStr; if ((s = org) != null) { s =
		 * Util.substringBeforeLastOpeningRoundParentheses(s); s =
		 * Util.substringChopEndIfMatch(s, "株"); s = Util.removeCommaAndTrim(s);
		 * if (!isNoData(s)) { record.put(DetailEnum.MARGIN_DEBT_BALANCE,
		 * Double.parseDouble(s)); } }
		 * 
		 * // 信用買残 前週比 org =
		 * marginDebtBalanceRatioComparisonWithPreviousWeekStr; if ((s = org) !=
		 * null) { s = Util.substringBeforeLastOpeningRoundParentheses(s); s =
		 * Util.substringChopEndIfMatch(s, "株"); s = Util.removeCommaAndTrim(s);
		 * if (!isNoData(s)) { record.put(
		 * DetailEnum.MARGIN_DEBT_BALANCE_RATIO_COMPARISON_WITH_PREVIOUS_WEEK,
		 * Double.parseDouble(s)); } }
		 * 
		 * // 信用売残 org = marginSellingBalanceStr; if ((s = org) != null) { s =
		 * Util.substringBeforeLastOpeningRoundParentheses(s); s =
		 * Util.substringChopEndIfMatch(s, "株"); s = Util.removeCommaAndTrim(s);
		 * if (!isNoData(s)) { record.put(DetailEnum.MARGIN_SELLING_BALANCE,
		 * Double.parseDouble(s)); } }
		 * 
		 * // 信用売残 前週比 org =
		 * marginSellingBalanceRatioComparisonWithPreviousWeekStr; if ((s = org)
		 * != null) { s = Util.substringBeforeLastOpeningRoundParentheses(s); s
		 * = Util.substringChopEndIfMatch(s, "株"); s =
		 * Util.removeCommaAndTrim(s); if (!isNoData(s)) { record.put(
		 * DetailEnum
		 * .MARGIN_SELLING_BALANCE_RATIO_COMPARISON_WITH_PREVIOUS_WEEK,
		 * Double.parseDouble(s)); } }
		 * 
		 * // 貸借倍率 org = ratioOfMarginBalanceStr; if ((s = org) != null) { s =
		 * Util.substringBeforeLastOpeningRoundParentheses(s); s =
		 * Util.substringChopEndIfMatch(s, "倍"); s = Util.removeCommaAndTrim(s);
		 * if (!isNoData(s)) { record.put(DetailEnum.RATIO_OF_MARGIN_BALANCE,
		 * Double.parseDouble(s)); } } } catch (NumberFormatException e) { //
		 * TODO: debug System.out
		 * .println("##### NumberFormatException was caused by : org=" + org +
		 * " stockCodeStr=" + stockCodeStr); throw e; }
		 *****/
		return record;
	}

	private boolean isNoData(String s) {
		if (s == null || s.length() == 0 || s.equals(NO_DATA)) {
			return true;
		}
		return false;
	}

	public void printAll() {
		System.out.println("stockCodeStr=" + stockCodeStr);
		System.out.println("featureStr=" + featureStr);
		System.out.println("consolidatedOperationsStr="
				+ consolidatedOperationsStr);
		System.out
				.println("locationOfHeadOfficeStr=" + locationOfHeadOfficeStr);
		System.out.println("nearestStationStr=" + nearestStationStr);
		System.out.println("telephoneNumberStr=" + telephoneNumberStr);
		System.out.println("sectorStr=" + sectorStr);
		System.out.println("stockNameInEnglishStr=" + stockNameInEnglishStr);
		System.out.println("representativeStr=" + representativeStr);
		System.out.println("foundationDateStr=" + foundationDateStr);
		System.out.println("marketStr=" + marketStr);
		System.out.println("listedDateStr=" + listedDateStr);
		System.out.println("settlingDateStr=" + settlingDateStr);
		System.out.println("shareUnitNumberStr=" + shareUnitNumberStr);
		System.out.println("nonConsolidatedNumberOfEmployeesStr="
				+ nonConsolidatedNumberOfEmployeesStr);
		System.out.println("consolidatedNumberOfEmployeesStr="
				+ consolidatedNumberOfEmployeesStr);
		System.out.println("averageAgeStr=" + averageAgeStr);
		System.out.println("averageAnnualSalaryStr=" + averageAnnualSalaryStr);

		System.out.println("stockNameStr=" + stockNameStr);
	}

	public ProfileRecord getProfileRecord() {
		return this.profileRecord;
	}
}
