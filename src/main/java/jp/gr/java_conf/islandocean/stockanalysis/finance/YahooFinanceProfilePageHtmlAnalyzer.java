package jp.gr.java_conf.islandocean.stockanalysis.finance;

import java.util.Calendar;
import java.util.Iterator;

import jp.gr.java_conf.islandocean.stockanalysis.common.FailedToFindElementException;
import jp.gr.java_conf.islandocean.stockanalysis.common.InvalidDataException;
import jp.gr.java_conf.islandocean.stockanalysis.util.Util;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class YahooFinanceProfilePageHtmlAnalyzer {

	private static final String CSS_QUERY_TO_FIND_STOCKS_INFO = ".stocksInfo";
	private static final String CSS_QUERY_TO_FIND_STOCKS_TABLE = ".stocksTable";
	private static final String CSS_QUERY_IN_PROFILE_PAGE_TO_FIND_TABLE_UNDER_DIV_ID_MAIN = "#main table";

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
	private static final String CAPTION_NON_CONSOLIDATED_NUMBER_OF_EMPLOYEES = "従業員数 (単独)";
	private static final String CAPTION_CONSOLIDATED_NUMBER_OF_EMPLOYEES = "従業員数 (連結)";
	private static final String CAPTION_AVERAGE_AGE = "平均年齢";
	private static final String CAPTION_AVERAGE_ANNUAL_SALARY = "平均年収";

	/**
	 * Raw String data
	 */
	private String stockCodeStr;
	private String stockNameStr;
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

	/**
	 * Special value of raw detail info String
	 */
	private static final String NO_DATA_IN_PROFILE_1 = "-"; // 0x2d
	private static final String NO_DATA_IN_PROFILE_2 = "‐"; // 0x2010

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
		Elements infoElements = doc.select(CSS_QUERY_TO_FIND_STOCKS_INFO);
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
				// sectorStr = category.text().trim();
			}
		}

		Elements stocksTables = doc.select(CSS_QUERY_TO_FIND_STOCKS_TABLE);
		if (stocksTables == null || stocksTables.size() < 1) {
			throw new FailedToFindElementException(
					"Cannot find stock table element.");
		}
		Element stocksTable = stocksTables.get(0);

		Elements symbol = stocksTable.select(".symbol");
		if (symbol != null) {
			stockNameStr = symbol.text().trim();
		}

		Elements tables = doc
				.select(CSS_QUERY_IN_PROFILE_PAGE_TO_FIND_TABLE_UNDER_DIV_ID_MAIN);
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

				String caption = Util.normalizeRoundParentheses(th.text()
						.trim());
				String value = Util.normalizeRoundParentheses(td.text().trim());

				if (caption.startsWith(CAPTION_FEATURE)) {
					featureStr = value;
				} else if (caption.startsWith(CAPTION_CONSOLIDATED_OPERATIONS)) {
					consolidatedOperationsStr = value;
				} else if (caption.startsWith(CAPTION_LOCATION_OF_HEAD_OFFICE)) {
					locationOfHeadOfficeStr = value;
				} else if (caption.startsWith(CAPTION_NEAREST_STATION)) {
					nearestStationStr = value;
				} else if (caption.startsWith(CAPTION_TELEPHONE_NUMBER)) {
					telephoneNumberStr = value;
				} else if (caption.startsWith(CAPTION_SECTOR)) {
					sectorStr = value;
				} else if (caption.startsWith(CAPTION_STOCK_NAME_IN_ENGLISH)) {
					stockNameInEnglishStr = value;
				} else if (caption.startsWith(CAPTION_REPRESENTATIVE)) {
					representativeStr = value;
				} else if (caption.startsWith(CAPTION_FOUNDATION_DATE)) {
					foundationDateStr = value;
				} else if (caption.startsWith(CAPTION_MARKET)) {
					marketStr = value;
				} else if (caption.startsWith(CAPTION_LISTED_DATE)) {
					listedDateStr = value;
				} else if (caption.startsWith(CAPTION_SETTLING_DATE)) {
					settlingDateStr = value;
				} else if (caption.startsWith(CAPTION_SHARE_UNIT_NUMBER)) {
					shareUnitNumberStr = value;
				} else if (caption
						.startsWith(CAPTION_NON_CONSOLIDATED_NUMBER_OF_EMPLOYEES)) {
					nonConsolidatedNumberOfEmployeesStr = value;
				} else if (caption
						.startsWith(CAPTION_CONSOLIDATED_NUMBER_OF_EMPLOYEES)) {
					consolidatedNumberOfEmployeesStr = value;
				} else if (caption.startsWith(CAPTION_AVERAGE_AGE)) {
					averageAgeStr = value;
				} else if (caption.startsWith(CAPTION_AVERAGE_ANNUAL_SALARY)) {
					averageAnnualSalaryStr = value;
				} else {
					if (caption.equals("詳細情報")
							&& value.equals("銘柄情報、保有物件、開示資料、分配金 リンク先は、外部サイトとなります")) {
					} else {
						System.out
								.println("Warning: Unknown caption was found. stockCode="
										+ stockCodeStr);
						System.out.println("unknown caption=" + caption);
						System.out.println("unknown value=" + value);
					}

				}
			}
		}
	}

	private ProfileRecord convertStringToRecord(Calendar dataGetDate)
			throws InvalidDataException {
		ProfileRecord record = new ProfileRecord();
		String org = null;
		String s = null;

		try {
			// コード
			record.put(ProfileEnum.STOCK_CODE, stockCodeStr);

			// 銘柄名
			record.put(ProfileEnum.STOCK_NAME, stockNameStr);

			// 特色
			org = featureStr;
			if ((s = org) != null) {
				s = s.trim();
				if (!isNoDataInProfile(s)) {
					record.put(ProfileEnum.FEATURE, s);
				}
			}

			// 連結事業
			org = consolidatedOperationsStr;
			if ((s = org) != null) {
				s = s.trim();
				if (!isNoDataInProfile(s)) {
					record.put(ProfileEnum.CONSOLIDATED_OPERATIONS, s);
				}
			}

			// 本社所在地
			org = locationOfHeadOfficeStr;
			if ((s = org) != null) {
				s = Util.substringChopEndIfMatch(s, "[主要事業所]");
				s = s.trim();
				s = Util.substringChopEndIfMatch(s, "[周辺地図]");
				s = s.trim();
				if (!isNoDataInProfile(s)) {
					record.put(ProfileEnum.LOCATION_OF_HEAD_OFFICE, s);
				}
			}

			// 最寄り駅
			org = nearestStationStr;
			if ((s = org) != null) {
				s = s.trim();
				s = Util.substringChopStartIfMatch(s, "～");
				s = s.trim();
				if (!isNoDataInProfile(s)) {
					record.put(ProfileEnum.NEAREST_STATION, s);
				}
			}

			// 電話番号
			org = telephoneNumberStr;
			if ((s = org) != null) {
				s = s.trim();
				if (!isNoDataInProfile(s)) {
					record.put(ProfileEnum.TELEPHONE_NUMBER, s);
				}
			}

			// 業種分類
			org = sectorStr;
			if ((s = org) != null) {
				s = s.trim();
				if (!isNoDataInProfile(s)) {
					record.put(ProfileEnum.SECTOR, s);
				}
			}

			// 英文社名
			org = stockNameInEnglishStr;
			if ((s = org) != null) {
				s = s.trim();
				if (!isNoDataInProfile(s)) {
					record.put(ProfileEnum.STOCK_NAME_IN_ENGLISH, s);
				}
			}

			// 代表者名
			org = representativeStr;
			if ((s = org) != null) {
				s = Util.substringChopEndIfMatch(s, "[役員]");
				s = s.trim();
				if (!isNoDataInProfile(s)) {
					record.put(ProfileEnum.REPRESENTATIVE, s);
				}
			}

			// 設立年月日
			org = foundationDateStr;
			if ((s = org) != null) {
				s = s.trim();
				if (!isNoDataInProfile(s)) {
					record.put(ProfileEnum.FOUNDATION_DATE, s);
				}
			}

			// 市場名
			org = marketStr;
			if ((s = org) != null) {
				s = Util.substringChopEndIfMatch(s, "[株主情報]");
				s = s.trim();
				if (!isNoDataInProfile(s)) {
					record.put(ProfileEnum.MARKET, s);
				}
			}

			// 上場年月日
			org = listedDateStr;
			if ((s = org) != null) {
				s = s.trim();
				if (!isNoDataInProfile(s)) {
					record.put(ProfileEnum.LISTED_DATE, s);
				}
			}

			// 決算
			org = settlingDateStr;
			if ((s = org) != null) {
				s = Util.substringChopEndIfMatch(s, "[決算情報　年次]");
				s = s.trim();
				if (!isNoDataInProfile(s)) {
					record.put(ProfileEnum.SETTLING_DATE, s);
				}
			}

			// 単元株数
			org = shareUnitNumberStr;
			if ((s = org) != null) {
				s = Util.substringChopStartIfMatch(s, "単元株制度なし");
				s = Util.substringChopEndIfMatch(s, "株");
				s = Util.removeCommaAndTrim(s);
				if (!isNoDataInProfile(s)) {
					record.put(ProfileEnum.SHARE_UNIT_NUMBER,
							Double.parseDouble(s));
				}
			}

			// 従業員数 (単独)
			org = nonConsolidatedNumberOfEmployeesStr;
			if ((s = org) != null) {
				s = Util.substringChopEndIfMatch(s, "人");
				s = Util.removeCommaAndTrim(s);
				if (!isNoDataInProfile(s)) {
					record.put(
							ProfileEnum.NON_CONSOLIDATED_NUMBER_OF_EMPLOYEES,
							Integer.parseInt(s));
				}
			}

			// 従業員数 (連結)
			org = consolidatedNumberOfEmployeesStr;
			if ((s = org) != null) {
				s = Util.substringChopEndIfMatch(s, "人");
				s = Util.removeCommaAndTrim(s);
				if (!isNoDataInProfile(s)) {
					record.put(ProfileEnum.CONSOLIDATED_NUMBER_OF_EMPLOYEES,
							Integer.parseInt(s));
				}
			}

			// 平均年齢
			org = averageAgeStr;
			if ((s = org) != null) {
				s = Util.substringChopEndIfMatch(s, "歳");
				s = Util.removeCommaAndTrim(s);
				if (!isNoDataInProfile(s)) {
					record.put(ProfileEnum.AVERAGE_AGE, Double.parseDouble(s));
				}
			}

			// 平均年収
			org = averageAnnualSalaryStr;
			if ((s = org) != null) {
				s = Util.substringChopEndIfMatch(s, "千円"); // thousand yen
				s = Util.removeCommaAndTrim(s);
				if (!isNoDataInProfile(s)) {
					record.put(ProfileEnum.AVERAGE_ANNUAL_SALARY,
							Double.parseDouble(s));
				}
			}

		} catch (NumberFormatException e) { //
			// TODO: debug
			System.out
					.println("Error: NumberFormatException was caused by : org="
							+ org
							+ " stockCodeStr="
							+ stockCodeStr
							+ " s="
							+ s
							+ " len(s)=" + s.length());
			throw e;
		}
		return record;
	}

	private boolean isNoDataInProfile(String s) {
		if (s == null || s.length() == 0 || s.equals(NO_DATA_IN_PROFILE_1)
				|| s.equals(NO_DATA_IN_PROFILE_2)) {
			return true;
		}
		return false;
	}

	public void printAll() {
		System.out.println("stockCodeStr=" + stockCodeStr);
		System.out.println("stockNameStr=" + stockNameStr);
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
	}

	public ProfileRecord getProfileRecord() {
		return this.profileRecord;
	}
}
