package jp.gr.java_conf.islandocean.stockanalysis.finance;

import java.util.Calendar;

import jp.gr.java_conf.islandocean.stockanalysis.common.FailedToFindElementException;
import jp.gr.java_conf.islandocean.stockanalysis.common.InvalidDataException;
import jp.gr.java_conf.islandocean.stockanalysis.util.CalendarUtil;
import jp.gr.java_conf.islandocean.stockanalysis.util.Util;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class YahooFinanceDetailPageHtmlAnalyzer {

	private static final String CSS_QUERY_IN_DETAIL_PAGE_TO_FIND_STOCKS_INFO = ".stocksInfo";
	private static final String CSS_QUERY_IN_DETAIL_PAGE_TO_FIND_STOCKS_TABLE = ".stocksTable";
	// private static final String CSS_QUERY_IN_DETAIL_PAGE_TO_FIND_DETAIL =
	// "#detail dl";
	// private static final String CSS_QUERY_IN_DETAIL_PAGE_TO_FIND_RFINDEX =
	// "#rfindex dl";
	// private static final String CSS_QUERY_IN_DETAIL_PAGE_TO_FIND_MARGIN =
	// "#margin dl";
	private static final String CSS_QUERY_IN_DETAIL_PAGE_TO_FIND_ALL_DL = "dl";

	private static final String CAPTION_PREVIOUS_CLOSING_PRICE = "前日終値";
	private static final String CAPTION_OPENING_PRICE = "始値";
	private static final String CAPTION_HIGH_PRICE = "高値";
	private static final String CAPTION_LOW_PRICE = "安値";
	private static final String CAPTION_TRADING_VOLUME_OF_STOCKS = "出来高";
	private static final String CAPTION_TRADING_VALUE_OF_MONEY = "売買代金";
	private static final String CAPTION_PRICE_LIMIT = "値幅制限";

	private static final String CAPTION_MARKET_CAPITALIZATION = "時価総額";
	private static final String CAPTION_OUTSTANDING_STOCK_VOLUME = "発行済株式数";
	private static final String CAPTION_ANNUAL_INTEREST_RATE = "配当利回り";
	private static final String CAPTION_DIVIDENDS_PER_SHARE = "1株配当";
	private static final String CAPTION_PER = "PER";
	private static final String CAPTION_PBR = "PBR";
	private static final String CAPTION_EPS = "EPS";
	private static final String CAPTION_BPS = "BPS";
	private static final String CAPTION_MINIMUM_PURCHASE_AMOUNT = "最低購入代金";
	private static final String CAPTION_SHARE_UNIT_NUMBER = "単元株数";
	private static final String CAPTION_YEARLY_HIGH = "年初来高値";
	private static final String CAPTION_YEARLY_LOW = "年初来安値";

	private static final String CAPTION_NET_ASSETS = "純資産";
	private static final String CAPTION_UNIT_OF_TRADING = "売買単位";
	private static final String CAPTION_MANAGEMENT_COMPANY = "運用会社";
	private static final String CAPTION_TYPE_OF_ASSETS_TO_BE_INVESTED = "投資対象資産";
	private static final String CAPTION_REGION_TO_BE_INVESTED = "投資対象地域";
	private static final String CAPTION_UNDERLYING_INDEX = "連動対象";
	private static final String CAPTION_SETTLEMENT_FREQUENCY = "決算頻度";
	private static final String CAPTION_SETTLEMENT_MONTH = "決算月";
	private static final String CAPTION_LISTED_DATE = "上場年月日";
	private static final String CAPTION_TRUST_FEE = "信託報酬（税抜）";

	private static final String CAPTION_MARGIN_DEBT_BALANCE = "信用買残";
	private static final String CAPTION_MARGIN_RATIO_COMPARISON_WITH_PREVIOUS_WEEK = "前週比";
	private static final String CAPTION_MARGIN_SELLING_BALANCE = "信用売残";
	private static final String CAPTION_RATIO_OF_MARGIN_BALANCE = "貸借倍率";

	/**
	 * Raw String data
	 */
	private String stockCodeStr;
	private String stockNameStr;
	private String sectorStr;

	private String realtimePriceStr;
	private String priceComparisonWithPreviousDayStr;

	private String previousClosingPriceStr;
	private String openingPriceStr;
	private String highPriceStr;
	private String lowPriceStr;
	private String tradingVolumeOfStocksStr;
	private String tradingValueOfMoneyStr;
	private String priceLimitStr;

	private String marketCapitalizationStr;
	private String outstandingStockVolumeStr;
	private String annualInterestRateStr;
	private String dividendsPerShareStr;
	private String perStr;
	private String pbrStr;
	private String epsStr;
	private String bpsStr;
	private String minimumPurchaseAmountStr;
	private String shareUnitNumberStr;
	private String yearlyHighStr;
	private String yearlyLowStr;

	private String netAssetsStr;
	private String unitOfTradingStr;
	private String managementCompanyStr;
	private String typeOfAssetsToBeInvestedStr;
	private String regionToBeInvestedStr;
	private String underlyingIndexStr;
	private String settlementFrequencyStr;
	private String settlementMonthStr;
	private String listedDateStr;
	private String trustFeeStr;

	private String marginDebtBalanceStr;
	private String marginDebtBalanceRatioComparisonWithPreviousWeekStr;
	private String marginSellingBalanceStr;
	private String marginSellingBalanceRatioComparisonWithPreviousWeekStr;
	private String ratioOfMarginBalanceStr;

	/**
	 * Special value of raw detail info String
	 */
	private static final String NO_DATA = "---";

	/**
	 * Analyzed data
	 */
	private DetailRecord detailRecord;

	public YahooFinanceDetailPageHtmlAnalyzer() {
		super();
	}

	public void analyze(Document doc, Calendar dataGetDate)
			throws FailedToFindElementException, InvalidDataException {
		extractDataAsString(doc);
		this.detailRecord = convertStringToRecord(dataGetDate);
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

		Elements tds = stocksTable.select("td");
		for (Element td : tds) {
			String text = Util.normalizeRoundParentheses(td.text().trim());
			if (text.length() == 0) {
			} else if (td.classNames().contains("change")) {
				priceComparisonWithPreviousDayStr = text;
			} else {
				try {
					Double.parseDouble(Util.removeCommaAndNbsp(text));
				} catch (NumberFormatException e) {
					continue;
				}
				realtimePriceStr = text;
			}
		}

		boolean isDebt = false;
		boolean isSelling = false;
		Elements dls = doc.select(CSS_QUERY_IN_DETAIL_PAGE_TO_FIND_ALL_DL);
		for (Element dl : dls) {
			Elements dt = dl.getElementsByTag("dt");
			Elements dd = dl.getElementsByTag("dd");
			String caption = dt.text().trim();
			String value = Util.normalizeRoundParentheses(dd.text().trim());

			if (caption.startsWith(CAPTION_PREVIOUS_CLOSING_PRICE)) {
				previousClosingPriceStr = value;
			} else if (caption.startsWith(CAPTION_OPENING_PRICE)) {
				openingPriceStr = value;
			} else if (caption.startsWith(CAPTION_HIGH_PRICE)) {
				highPriceStr = value;
			} else if (caption.startsWith(CAPTION_LOW_PRICE)) {
				lowPriceStr = value;
			} else if (caption.startsWith(CAPTION_TRADING_VOLUME_OF_STOCKS)) {
				tradingVolumeOfStocksStr = value;
			} else if (caption.startsWith(CAPTION_TRADING_VALUE_OF_MONEY)) {
				tradingValueOfMoneyStr = value;
			} else if (caption.startsWith(CAPTION_PRICE_LIMIT)) {
				priceLimitStr = value;
			} else if (caption.startsWith(CAPTION_MARKET_CAPITALIZATION)) {
				marketCapitalizationStr = value;
			} else if (caption.startsWith(CAPTION_OUTSTANDING_STOCK_VOLUME)) {
				outstandingStockVolumeStr = value;
			} else if (caption.startsWith(CAPTION_ANNUAL_INTEREST_RATE)) {
				annualInterestRateStr = value;
			} else if (caption.startsWith(CAPTION_DIVIDENDS_PER_SHARE)) {
				dividendsPerShareStr = value;
			} else if (caption.startsWith(CAPTION_PER)) {
				perStr = value;
			} else if (caption.startsWith(CAPTION_PBR)) {
				pbrStr = value;
			} else if (caption.startsWith(CAPTION_EPS)) {
				epsStr = value;
			} else if (caption.startsWith(CAPTION_BPS)) {
				bpsStr = value;
			} else if (caption.startsWith(CAPTION_MINIMUM_PURCHASE_AMOUNT)) {
				minimumPurchaseAmountStr = value;
			} else if (caption.startsWith(CAPTION_SHARE_UNIT_NUMBER)) {
				shareUnitNumberStr = value;
			} else if (caption.startsWith(CAPTION_YEARLY_HIGH)) {
				yearlyHighStr = value;
			} else if (caption.startsWith(CAPTION_YEARLY_LOW)) {
				yearlyLowStr = value;
			} else if (caption.startsWith(CAPTION_NET_ASSETS)) {
				netAssetsStr = value;
			} else if (caption.startsWith(CAPTION_UNIT_OF_TRADING)) {
				unitOfTradingStr = value;
			} else if (caption.startsWith(CAPTION_MANAGEMENT_COMPANY)) {
				managementCompanyStr = value;
			} else if (caption
					.startsWith(CAPTION_TYPE_OF_ASSETS_TO_BE_INVESTED)) {
				typeOfAssetsToBeInvestedStr = value;
			} else if (caption.startsWith(CAPTION_REGION_TO_BE_INVESTED)) {
				regionToBeInvestedStr = value;
			} else if (caption.startsWith(CAPTION_UNDERLYING_INDEX)) {
				underlyingIndexStr = value;
			} else if (caption.startsWith(CAPTION_SETTLEMENT_FREQUENCY)) {
				settlementFrequencyStr = value;
			} else if (caption.startsWith(CAPTION_SETTLEMENT_MONTH)) {
				settlementMonthStr = value;
			} else if (caption.startsWith(CAPTION_LISTED_DATE)) {
				listedDateStr = value;
			} else if (caption.startsWith(CAPTION_TRUST_FEE)) {
				trustFeeStr = value;
			} else if (caption.startsWith(CAPTION_MARGIN_DEBT_BALANCE)) {
				marginDebtBalanceStr = value;
				isDebt = true;
				isSelling = false;
			} else if (caption
					.startsWith(CAPTION_MARGIN_RATIO_COMPARISON_WITH_PREVIOUS_WEEK)) {
				if (isDebt) {
					marginDebtBalanceRatioComparisonWithPreviousWeekStr = value;
				} else if (isSelling) {
					marginSellingBalanceRatioComparisonWithPreviousWeekStr = value;
				}
			} else if (caption.startsWith(CAPTION_MARGIN_SELLING_BALANCE)) {
				marginSellingBalanceStr = value;
				isDebt = false;
				isSelling = true;
			} else if (caption.startsWith(CAPTION_RATIO_OF_MARGIN_BALANCE)) {
				ratioOfMarginBalanceStr = value;
			} else {
				if (!caption.equals("") && !caption.startsWith("値上がり率")
						&& !caption.startsWith("値下がり率")
						&& !caption.startsWith("[買い]")
						&& !caption.startsWith("[売り]")
						&& value.indexOf("リアルタイム株価") < 0) {

					//
					// TODO: unknown data format
					//
					System.out.println("caption=" + caption);
					System.out.println("value=" + value);
				}
			}
		}
	}

	private DetailRecord convertStringToRecord(Calendar dataGetDate)
			throws InvalidDataException {
		DetailRecord record = new DetailRecord();
		String org = null;
		String s = null;
		record.put(DetailEnum.DATA_GET_DATE, dataGetDate);

		try {
			// コード
			record.put(DetailEnum.STOCK_CODE, stockCodeStr);

			// 銘柄名
			record.put(DetailEnum.STOCK_NAME, stockNameStr);

			// 業種
			record.put(DetailEnum.SECTOR, sectorStr);

			// リアルタイム株価
			org = realtimePriceStr;
			if ((s = org) != null) {
				s = Util.substringChopStartIfMatch(s, "ストップ高");
				s = Util.substringChopStartIfMatch(s, "ストップ安");
				s = Util.removeCommaAndTrim(s);
				if (!isNoData(s)) {
					record.put(DetailEnum.REALTIME_PRICE, Double.parseDouble(s));
				}
			}

			// 前日比
			org = priceComparisonWithPreviousDayStr;
			if ((s = org) != null) {
				s = Util.substringBeforeLastOpeningRoundParentheses(s);
				s = Util.substringChopStartIfMatch(s, "前日比");
				s = Util.removeCommaAndTrim(s);
				if (!isNoData(s)) {
					record.put(DetailEnum.PRICE_COMPARISON_WITH_PREVIOUS_DAY,
							Double.parseDouble(s));
				}
			}

			// 前日終値
			org = previousClosingPriceStr;
			if ((s = org) != null) {
				s = Util.substringBeforeLastOpeningRoundParentheses(s);
				s = Util.substringChopStartIfMatch(s, "ストップ高");
				s = Util.substringChopStartIfMatch(s, "ストップ安");
				s = Util.removeCommaAndTrim(s);
				if (!isNoData(s)) {
					record.put(DetailEnum.PREVIOUS_CLOSING_PRICE,
							Double.parseDouble(s));
				}
			}

			// 始値
			org = openingPriceStr;
			if ((s = org) != null) {
				s = Util.substringBeforeLastOpeningRoundParentheses(s);
				s = Util.substringChopStartIfMatch(s, "ストップ高");
				s = Util.substringChopStartIfMatch(s, "ストップ安");
				s = Util.removeCommaAndTrim(s);
				if (!isNoData(s)) {
					record.put(DetailEnum.OPENING_PRICE, Double.parseDouble(s));
				}
			}

			// 高値
			org = highPriceStr;
			if ((s = org) != null) {
				s = Util.substringBeforeLastOpeningRoundParentheses(s);
				s = Util.substringChopStartIfMatch(s, "ストップ高");
				s = Util.substringChopStartIfMatch(s, "ストップ安");
				s = Util.removeCommaAndTrim(s);
				if (!isNoData(s)) {
					record.put(DetailEnum.HIGH_PRICE, Double.parseDouble(s));
				}
			}

			// 安値
			org = lowPriceStr;
			if ((s = org) != null) {
				s = Util.substringBeforeLastOpeningRoundParentheses(s);
				s = Util.substringChopStartIfMatch(s, "ストップ高");
				s = Util.substringChopStartIfMatch(s, "ストップ安");
				s = Util.removeCommaAndTrim(s);
				if (!isNoData(s)) {
					record.put(DetailEnum.LOW_PRICE, Double.parseDouble(s));
				}
			}

			// 出来高
			org = tradingVolumeOfStocksStr;
			if ((s = org) != null) {
				s = Util.substringBeforeLastOpeningRoundParentheses(s);
				s = Util.substringChopEndIfMatch(s, "株");
				s = Util.removeCommaAndTrim(s);
				if (!isNoData(s)) {
					record.put(DetailEnum.TRADING_VOLUME_OF_STOCKS,
							Double.parseDouble(s));
				}
			}

			// 売買代金
			org = tradingValueOfMoneyStr;
			if ((s = org) != null) {
				s = Util.substringBeforeLastOpeningRoundParentheses(s);
				s = Util.substringChopEndIfMatch(s, "千円");
				s = Util.removeCommaAndTrim(s);
				if (!isNoData(s)) {
					record.put(DetailEnum.TRADING_VALUE_OF_MONEY,
							Double.parseDouble(s));
				}
			}

			// 値幅制限
			org = priceLimitStr;
			if ((s = org) != null) {
				s = Util.substringBeforeLastOpeningRoundParentheses(s);
				s = Util.removeCommaAndTrim(s);
				String[] ar = s.split("～");
				if (ar.length >= 1) {
					s = ar[0];
					if (!isNoData(s)) {
						record.put(DetailEnum.LOW_PRICE_LIMIT,
								Double.parseDouble(s));
					}
				}
				if (ar.length >= 2) {
					s = ar[1];
					if (!isNoData(s)) {
						record.put(DetailEnum.HIGH_PRICE_LIMIT,
								Double.parseDouble(s));
					}
				}
			}

			// 時価総額
			org = marketCapitalizationStr;
			if ((s = org) != null) {
				s = Util.substringBeforeLastOpeningRoundParentheses(s);
				s = Util.substringChopEndIfMatch(s, "百万円");
				s = Util.removeCommaAndTrim(s);
				if (!isNoData(s)) {
					record.put(DetailEnum.MARKET_CAPITALIZATION,
							Double.parseDouble(s));
				}
			}

			// 発行済株式数
			org = outstandingStockVolumeStr;
			if ((s = org) != null) {
				s = Util.substringBeforeLastOpeningRoundParentheses(s);
				s = Util.substringChopEndIfMatch(s, "株");
				s = Util.removeCommaAndTrim(s);
				if (!isNoData(s)) {
					record.put(DetailEnum.OUTSTANDING_STOCK_VOLUME,
							Double.parseDouble(s));
				}
			}

			// 配当利回り
			org = annualInterestRateStr;
			if ((s = org) != null) {
				s = Util.substringBeforeLastOpeningRoundParentheses(s);
				s = Util.substringChopEndIfMatch(s, "%");
				s = Util.removeCommaAndTrim(s);
				if (!isNoData(s)) {
					record.put(DetailEnum.ANNUAL_INTEREST_RATE,
							Double.parseDouble(s));
				}
			}

			// 1株配当
			org = dividendsPerShareStr;
			if ((s = org) != null) {
				s = Util.substringBeforeLastOpeningRoundParentheses(s);
				s = Util.removeCommaAndTrim(s);
				if (!isNoData(s)) {
					record.put(DetailEnum.DIVIDENDS_PER_SHARE,
							Double.parseDouble(s));
				}
			}

			// PER
			org = perStr;
			if ((s = org) != null) {
				s = Util.substringBeforeLastOpeningRoundParentheses(s);
				s = Util.substringChopStartIfMatch(s, "(連)");
				s = Util.substringChopStartIfMatch(s, "(単)");
				s = Util.substringChopEndIfMatch(s, "倍");
				s = Util.removeCommaAndTrim(s);
				if (!isNoData(s)) {
					record.put(DetailEnum.PER, Double.parseDouble(s));
				}
			}

			// PBR
			org = pbrStr;
			if ((s = org) != null) {
				s = Util.substringBeforeLastOpeningRoundParentheses(s);
				s = Util.substringChopStartIfMatch(s, "(連)");
				s = Util.substringChopStartIfMatch(s, "(単)");
				s = Util.substringChopEndIfMatch(s, "倍");
				s = Util.removeCommaAndTrim(s);
				if (!isNoData(s)) {
					record.put(DetailEnum.PBR, Double.parseDouble(s));
				}
			}

			// EPS
			org = epsStr;
			if ((s = org) != null) {
				s = Util.substringBeforeLastOpeningRoundParentheses(s);
				s = Util.substringChopStartIfMatch(s, "(連)");
				s = Util.substringChopStartIfMatch(s, "(単)");
				s = Util.removeCommaAndTrim(s);
				if (!isNoData(s)) {
					record.put(DetailEnum.EPS, Double.parseDouble(s));
				}
			}

			// BPS
			org = bpsStr;
			if ((s = org) != null) {
				s = Util.substringBeforeLastOpeningRoundParentheses(s);
				s = Util.substringChopStartIfMatch(s, "(連)");
				s = Util.substringChopStartIfMatch(s, "(単)");
				s = Util.removeCommaAndTrim(s);
				if (!isNoData(s)) {
					record.put(DetailEnum.BPS, Double.parseDouble(s));
				}
			}

			// 最低購入代金
			org = minimumPurchaseAmountStr;
			if ((s = org) != null) {
				s = Util.substringBeforeLastOpeningRoundParentheses(s);
				s = Util.removeCommaAndTrim(s);
				if (!isNoData(s)) {
					record.put(DetailEnum.MINIMUM_PURCHASE_AMOUNT,
							Double.parseDouble(s));
				}
			}

			// 単元株数
			org = shareUnitNumberStr;
			if ((s = org) != null) {
				s = Util.substringChopEndIfMatch(s, "株");
				s = Util.removeCommaAndTrim(s);
				if (!isNoData(s)) {
					record.put(DetailEnum.SHARE_UNIT_NUMBER,
							Double.parseDouble(s));
				}
			}

			// 年初来高値
			org = yearlyHighStr;
			if ((s = org) != null) {
				s = Util.substringBeforeLastOpeningRoundParentheses(s);
				s = Util.substringChopStartIfMatch(s, "更新");
				s = Util.removeCommaAndTrim(s);
				if (!isNoData(s)) {
					record.put(DetailEnum.YEARLY_HIGH, Double.parseDouble(s));
				}
			}

			// 年初来安値
			org = yearlyLowStr;
			if ((s = org) != null) {
				s = Util.substringBeforeLastOpeningRoundParentheses(s);
				s = Util.substringChopStartIfMatch(s, "更新");
				s = Util.removeCommaAndTrim(s);
				if (!isNoData(s)) {
					record.put(DetailEnum.YEARLY_LOW, Double.parseDouble(s));
				}
			}

			// 純資産
			org = netAssetsStr;
			if ((s = org) != null) {
				s = Util.substringBeforeLastOpeningRoundParentheses(s);
				if (!s.endsWith("百万円")) {
					// TODO: error or unknown data format
				}
				s = Util.substringChopEndIfMatch(s, "百万円");
				s = Util.removeCommaAndTrim(s);
				if (!isNoData(s)) {
					record.put(DetailEnum.NET_ASSETS, Double.parseDouble(s));
				}
			}

			// 売買単位
			org = unitOfTradingStr;
			if ((s = org) != null) {
				s = Util.substringChopEndIfMatch(s, "株");
				s = Util.removeCommaAndTrim(s);
				if (!isNoData(s)) {
					record.put(DetailEnum.UNIT_OF_TRADING,
							Double.parseDouble(s));
				}
			}

			// 運用会社
			org = managementCompanyStr;
			if ((s = org) != null) {
				s = s.trim();
				if (!isNoData(s)) {
					record.put(DetailEnum.MANAGEMENT_COMPANY, s);
				}
			}

			// 投資対象資産
			org = typeOfAssetsToBeInvestedStr;
			if ((s = org) != null) {
				s = s.trim();
				if (!isNoData(s)) {
					record.put(DetailEnum.TYPE_OF_ASSETS_TO_BE_INVESTED, s);
				}
			}

			// 投資対象地域
			org = regionToBeInvestedStr;
			if ((s = org) != null) {
				s = s.trim();
				if (!isNoData(s)) {
					record.put(DetailEnum.REGION_TO_BE_INVESTED, s);
				}
			}

			// 連動対象
			org = underlyingIndexStr;
			if ((s = org) != null) {
				s = s.trim();
				if (!isNoData(s)) {
					record.put(DetailEnum.UNDERLYING_INDEX, s);
				}
			}

			// 決算頻度
			org = settlementFrequencyStr;
			if ((s = org) != null) {
				s = Util.substringChopEndIfMatch(s, "回");
				s = Util.removeCommaAndTrim(s);
				if (!isNoData(s)) {
					record.put(DetailEnum.SETTLEMENT_FREQUENCY,
							Integer.parseInt(s));
				}
			}

			// 決算月
			org = settlementMonthStr; // Comma separated numerics, January is 1.
			if ((s = org) != null) {
				s = Util.substringChopEndIfMatch(s, "月");
				s = s.trim();
				if (!isNoData(s)) {
					record.put(DetailEnum.SETTLEMENT_MONTH, s);
				}
			}

			// 上場年月日
			org = listedDateStr;
			if ((s = org) != null) {
				s = Util.substringChopEndIfMatch(s, "日");
				s = s.trim();
				int year;
				int month;
				int day;
				if (s.length() != 0) {
					String[] p = s.split("年");
					if (p.length != 2) {
						throw new InvalidDataException(
								"Error: listedDateStr is invalid. listedDateStr="
										+ listedDateStr);
					}
					year = Integer.parseInt(p[0]);

					String[] q = p[1].split("月");
					if (p.length != 2) {
						throw new InvalidDataException(
								"Error: listedDateStr is invalid. listedDateStr="
										+ listedDateStr);
					}
					month = Integer.parseInt(q[0]) - 1; /* because January==0 */
					day = Integer.parseInt(q[1]);
					Calendar cal = CalendarUtil.createDay(year, month, day);

					if (!isNoData(s)) {
						record.put(DetailEnum.LISTED_DATE, cal);
					}
				}
			}

			// 信託報酬（税抜）
			org = trustFeeStr;
			if ((s = org) != null) {
				s = Util.substringChopEndIfMatch(s, "%");
				s = Util.removeCommaAndTrim(s);
				if (!isNoData(s)) {
					record.put(DetailEnum.TRUST_FEE, Double.parseDouble(s));
				}
			}

			// 信用買残
			org = marginDebtBalanceStr;
			if ((s = org) != null) {
				s = Util.substringBeforeLastOpeningRoundParentheses(s);
				s = Util.substringChopEndIfMatch(s, "株");
				s = Util.removeCommaAndTrim(s);
				if (!isNoData(s)) {
					record.put(DetailEnum.MARGIN_DEBT_BALANCE,
							Double.parseDouble(s));
				}
			}

			// 信用買残 前週比
			org = marginDebtBalanceRatioComparisonWithPreviousWeekStr;
			if ((s = org) != null) {
				s = Util.substringBeforeLastOpeningRoundParentheses(s);
				s = Util.substringChopEndIfMatch(s, "株");
				s = Util.removeCommaAndTrim(s);
				if (!isNoData(s)) {
					record.put(
							DetailEnum.MARGIN_DEBT_BALANCE_RATIO_COMPARISON_WITH_PREVIOUS_WEEK,
							Double.parseDouble(s));
				}
			}

			// 信用売残
			org = marginSellingBalanceStr;
			if ((s = org) != null) {
				s = Util.substringBeforeLastOpeningRoundParentheses(s);
				s = Util.substringChopEndIfMatch(s, "株");
				s = Util.removeCommaAndTrim(s);
				if (!isNoData(s)) {
					record.put(DetailEnum.MARGIN_SELLING_BALANCE,
							Double.parseDouble(s));
				}
			}

			// 信用売残 前週比
			org = marginSellingBalanceRatioComparisonWithPreviousWeekStr;
			if ((s = org) != null) {
				s = Util.substringBeforeLastOpeningRoundParentheses(s);
				s = Util.substringChopEndIfMatch(s, "株");
				s = Util.removeCommaAndTrim(s);
				if (!isNoData(s)) {
					record.put(
							DetailEnum.MARGIN_SELLING_BALANCE_RATIO_COMPARISON_WITH_PREVIOUS_WEEK,
							Double.parseDouble(s));
				}
			}

			// 貸借倍率
			org = ratioOfMarginBalanceStr;
			if ((s = org) != null) {
				s = Util.substringBeforeLastOpeningRoundParentheses(s);
				s = Util.substringChopEndIfMatch(s, "倍");
				s = Util.removeCommaAndTrim(s);
				if (!isNoData(s)) {
					record.put(DetailEnum.RATIO_OF_MARGIN_BALANCE,
							Double.parseDouble(s));
				}
			}
		} catch (NumberFormatException e) {
			// TODO: debug
			System.out
					.println("##### NumberFormatException was caused by : org="
							+ org + " stockCodeStr=" + stockCodeStr);
			throw e;
		}

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
		System.out.println("stockNameStr=" + stockNameStr);
		System.out.println("sectorStr=" + sectorStr);
		System.out.println("realtimePriceStr=" + realtimePriceStr);
		System.out.println("priceComparisonWithPreviousDayStr="
				+ priceComparisonWithPreviousDayStr);
		System.out
				.println("previousClosingPriceStr=" + previousClosingPriceStr);
		System.out.println("openingPriceStr=" + openingPriceStr);
		System.out.println("highPriceStr=" + highPriceStr);
		System.out.println("lowPriceStr=" + lowPriceStr);
		System.out.println("tradingVolumeOfStocksStr="
				+ tradingVolumeOfStocksStr);
		System.out.println("tradingValueOfMoneyStr=" + tradingValueOfMoneyStr);
		System.out.println("priceLimitStr=" + priceLimitStr);
		System.out
				.println("marketCapitalizationStr=" + marketCapitalizationStr);
		System.out.println("outstandingStockVolumeStr="
				+ outstandingStockVolumeStr);
		System.out.println("annualInterestRateStr=" + annualInterestRateStr);
		System.out.println("dividendsPerShareStr=" + dividendsPerShareStr);
		System.out.println("perStr=" + perStr);
		System.out.println("pbrStr=" + pbrStr);
		System.out.println("epsStr=" + epsStr);
		System.out.println("bpsStr=" + bpsStr);
		System.out.println("minimumPurchaseAmountStr="
				+ minimumPurchaseAmountStr);
		System.out.println("shareUnitNumberStr=" + shareUnitNumberStr);
		System.out.println("yearlyHighStr=" + yearlyHighStr);
		System.out.println("yearlyLowStr=" + yearlyLowStr);
		System.out.println("NetAssetsStr=" + netAssetsStr);
		System.out.println("UnitOfTradingStr=" + unitOfTradingStr);
		System.out.println("ManagementCompanyStr=" + managementCompanyStr);
		System.out.println("TypeOfAssetsToBeInvestedStr="
				+ typeOfAssetsToBeInvestedStr);
		System.out.println("RegionToBeInvestedStr=" + regionToBeInvestedStr);
		System.out.println("UnderlyingIndexStr=" + underlyingIndexStr);
		System.out.println("SettlementFrequencyStr=" + settlementFrequencyStr);
		System.out.println("SettlementMonthStr=" + settlementMonthStr);
		System.out.println("ListedDateStr=" + listedDateStr);
		System.out.println("TrustFeeStr=" + trustFeeStr);
		System.out.println("marginDebtBalanceStr=" + marginDebtBalanceStr);
		System.out
				.println("marginDebtBalanceRatioComparisonWithPreviousWeekStr="
						+ marginDebtBalanceRatioComparisonWithPreviousWeekStr);
		System.out
				.println("marginSellingBalanceStr=" + marginSellingBalanceStr);
		System.out
				.println("marginSellingBalanceRatioComparisonWithPreviousWeekStr="
						+ marginSellingBalanceRatioComparisonWithPreviousWeekStr);
		System.out
				.println("ratioOfMarginBalanceStr=" + ratioOfMarginBalanceStr);
	}

	public DetailRecord getDetailRecord() {
		return this.detailRecord;
	}
}
