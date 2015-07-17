package jp.gr.java_conf.islandocean.stockanalysis.finance;

import java.util.Calendar;

import jp.gr.java_conf.islandocean.stockanalysis.common.FailedToFindElementException;
import jp.gr.java_conf.islandocean.stockanalysis.util.Util;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class YahooFinanceDetailPageHtmlAnalyzer {

	private static final String CSS_QUERY_IN_DETAIL_PAGE_TO_FIND_STOCKS_INFO = ".stocksInfo";
	private static final String CSS_QUERY_IN_DETAIL_PAGE_TO_FIND_STOCKS_TABLE = ".stocksTable";
	private static final String CSS_QUERY_IN_DETAIL_PAGE_TO_FIND_DETAIL = "#detail dl";
	private static final String CSS_QUERY_IN_DETAIL_PAGE_TO_FIND_RFINDEX = "#rfindex dl";
	private static final String CSS_QUERY_IN_DETAIL_PAGE_TO_FIND_MARGIN = "#margin dl";

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
	private StockDetailInfo stockDetailInfo;

	public YahooFinanceDetailPageHtmlAnalyzer() {
		super();
	}

	public void analyze(Document doc, Calendar dataGetDate)
			throws FailedToFindElementException {
		extractDataAsString(doc);
		this.stockDetailInfo = convertStringToStockDetailInfo(dataGetDate);
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

		Elements tables = doc
				.select(CSS_QUERY_IN_DETAIL_PAGE_TO_FIND_STOCKS_TABLE);
		if (tables == null || tables.size() < 1) {
			throw new FailedToFindElementException(
					"Cannot find stock table element.");
		}
		Element table = tables.get(0);

		Elements symbol = table.select(".symbol");
		if (symbol != null) {
			stockNameStr = symbol.text().trim();
		}

		Elements tds = table.select("td");
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

		Elements detailElements = doc
				.select(CSS_QUERY_IN_DETAIL_PAGE_TO_FIND_DETAIL);
		for (Element dl : detailElements) {
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
			}
		}

		Elements rfindexElements = doc
				.select(CSS_QUERY_IN_DETAIL_PAGE_TO_FIND_RFINDEX);
		for (Element dl : rfindexElements) {
			Elements dt = dl.getElementsByTag("dt");
			Elements dd = dl.getElementsByTag("dd");
			String caption = dt.text().trim();
			String value = Util.normalizeRoundParentheses(dd.text().trim());

			if (caption.startsWith(CAPTION_MARKET_CAPITALIZATION)) {
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
			}
		}

		Elements marginElements = doc
				.select(CSS_QUERY_IN_DETAIL_PAGE_TO_FIND_MARGIN);
		boolean isDebt = false;
		boolean isSelling = false;
		for (Element dl : marginElements) {
			Elements dt = dl.getElementsByTag("dt");
			Elements dd = dl.getElementsByTag("dd");
			String caption = dt.text().trim();
			String value = Util.normalizeRoundParentheses(dd.text().trim());

			if (caption.startsWith(CAPTION_MARGIN_DEBT_BALANCE)) {
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
			}
		}
	}

	private StockDetailInfo convertStringToStockDetailInfo(Calendar dataGetDate) {
		StockDetailInfo stockDetailInfo = new StockDetailInfo();
		String org = null;
		String s = null;
		stockDetailInfo.setDataGetDate(dataGetDate);

		try {
			// コード
			stockDetailInfo.setStockCode(stockCodeStr);

			// 銘柄名
			stockDetailInfo.setStockName(stockNameStr);

			// 業種
			stockDetailInfo.setSector(sectorStr);

			// リアルタイム株価
			org = realtimePriceStr;
			if ((s = org) != null) {
				s = Util.substringChopStartIfMatch(s, "ストップ高");
				s = Util.substringChopStartIfMatch(s, "ストップ安");
				s = Util.removeCommaAndTrim(s);
				if (!isNoData(s)) {
					stockDetailInfo.setRealtimePrice(Double.parseDouble(s));
				}
			}

			// 前日比
			org = priceComparisonWithPreviousDayStr;
			if ((s = org) != null) {
				s = Util.substringBeforeLastOpeningRoundParentheses(s);
				s = Util.substringChopStartIfMatch(s, "前日比");
				s = Util.removeCommaAndTrim(s);
				if (!isNoData(s)) {
					stockDetailInfo.setPriceComparisonWithPreviousDay(Double
							.parseDouble(s));
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
					stockDetailInfo.setPreviousClosingPrice(Double
							.parseDouble(s));
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
					stockDetailInfo.setOpeningPrice(Double.parseDouble(s));
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
					stockDetailInfo.setHighPrice(Double.parseDouble(s));
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
					stockDetailInfo.setLowPrice(Double.parseDouble(s));
				}
			}

			// 出来高
			org = tradingVolumeOfStocksStr;
			if ((s = org) != null) {
				s = Util.substringBeforeLastOpeningRoundParentheses(s);
				s = Util.substringChopEndIfMatch(s, "株");
				s = Util.removeCommaAndTrim(s);
				if (!isNoData(s)) {
					stockDetailInfo.setTradingVolumeOfStocks(Double
							.parseDouble(s));
				}
			}

			// 売買代金
			org = tradingValueOfMoneyStr;
			if ((s = org) != null) {
				s = Util.substringBeforeLastOpeningRoundParentheses(s);
				s = Util.substringChopEndIfMatch(s, "千円");
				s = Util.removeCommaAndTrim(s);
				if (!isNoData(s)) {
					stockDetailInfo.setTradingValueOfMoney(Double
							.parseDouble(s));
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
						stockDetailInfo.setLowPriceLimit(Double.parseDouble(s));
					}
				}
				if (ar.length >= 2) {
					s = ar[1];
					if (!isNoData(s)) {
						stockDetailInfo
								.setHighPriceLimit(Double.parseDouble(s));
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
					stockDetailInfo.setMarketCapitalization(Double
							.parseDouble(s));
				}
			}

			// 発行済株式数
			org = outstandingStockVolumeStr;
			if ((s = org) != null) {
				s = Util.substringBeforeLastOpeningRoundParentheses(s);
				s = Util.substringChopEndIfMatch(s, "株");
				s = Util.removeCommaAndTrim(s);
				if (!isNoData(s)) {
					stockDetailInfo.setOutstandingStockVolume(Double
							.parseDouble(s));
				}
			}

			// 配当利回り
			org = annualInterestRateStr;
			if ((s = org) != null) {
				s = Util.substringBeforeLastOpeningRoundParentheses(s);
				s = Util.substringChopEndIfMatch(s, "%");
				s = Util.removeCommaAndTrim(s);
				if (!isNoData(s)) {
					stockDetailInfo
							.setAnnualInterestRate(Double.parseDouble(s));
				}
			}

			// 1株配当
			org = dividendsPerShareStr;
			if ((s = org) != null) {
				s = Util.substringBeforeLastOpeningRoundParentheses(s);
				s = Util.removeCommaAndTrim(s);
				if (!isNoData(s)) {
					stockDetailInfo.setDividendsPerShare(Double.parseDouble(s));
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
					stockDetailInfo.setPer(Double.parseDouble(s));
				}
			}

			// PER
			org = pbrStr;
			if ((s = org) != null) {
				s = Util.substringBeforeLastOpeningRoundParentheses(s);
				s = Util.substringChopStartIfMatch(s, "(連)");
				s = Util.substringChopStartIfMatch(s, "(単)");
				s = Util.substringChopEndIfMatch(s, "倍");
				s = Util.removeCommaAndTrim(s);
				if (!isNoData(s)) {
					stockDetailInfo.setPbr(Double.parseDouble(s));
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
					stockDetailInfo.setEps(Double.parseDouble(s));
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
					stockDetailInfo.setBps(Double.parseDouble(s));
				}
			}

			// 最低購入代金
			org = minimumPurchaseAmountStr;
			if ((s = org) != null) {
				s = Util.substringBeforeLastOpeningRoundParentheses(s);
				s = Util.removeCommaAndTrim(s);
				if (!isNoData(s)) {
					stockDetailInfo.setMinimumPurchaseAmount(Double
							.parseDouble(s));
				}
			}

			// 単元株数
			org = shareUnitNumberStr;
			if ((s = org) != null) {
				s = Util.substringChopEndIfMatch(s, "株");
				s = Util.removeCommaAndTrim(s);
				if (!isNoData(s)) {
					stockDetailInfo.setShareUnitNumber(Double.parseDouble(s));
				}
			}

			// 年初来高値
			org = yearlyHighStr;
			if ((s = org) != null) {
				s = Util.substringBeforeLastOpeningRoundParentheses(s);
				s = Util.substringChopStartIfMatch(s, "更新");
				s = Util.removeCommaAndTrim(s);
				if (!isNoData(s)) {
					stockDetailInfo.setYearlyHigh(Double.parseDouble(s));
				}
			}

			// 年初来安値
			org = yearlyLowStr;
			if ((s = org) != null) {
				s = Util.substringBeforeLastOpeningRoundParentheses(s);
				s = Util.substringChopStartIfMatch(s, "更新");
				s = Util.removeCommaAndTrim(s);
				if (!isNoData(s)) {
					stockDetailInfo.setYearlyLow(Double.parseDouble(s));
				}
			}

			// 信用買残
			org = marginDebtBalanceStr;
			if ((s = org) != null) {
				s = Util.substringBeforeLastOpeningRoundParentheses(s);
				s = Util.substringChopEndIfMatch(s, "株");
				s = Util.removeCommaAndTrim(s);
				if (!isNoData(s)) {
					stockDetailInfo.setMarginDebtBalance(Double.parseDouble(s));
				}
			}

			// 信用買残 前週比
			org = marginDebtBalanceRatioComparisonWithPreviousWeekStr;
			if ((s = org) != null) {
				s = Util.substringBeforeLastOpeningRoundParentheses(s);
				s = Util.substringChopEndIfMatch(s, "株");
				s = Util.removeCommaAndTrim(s);
				if (!isNoData(s)) {
					stockDetailInfo
							.setMarginDebtBalanceRatioComparisonWithPreviousWeek(Double
									.parseDouble(s));
				}
			}

			// 信用売残
			org = marginSellingBalanceStr;
			if ((s = org) != null) {
				s = Util.substringBeforeLastOpeningRoundParentheses(s);
				s = Util.substringChopEndIfMatch(s, "株");
				s = Util.removeCommaAndTrim(s);
				if (!isNoData(s)) {
					stockDetailInfo.setMarginSellingBalance(Double
							.parseDouble(s));
				}
			}

			// 信用売残 前週比
			org = marginSellingBalanceRatioComparisonWithPreviousWeekStr;
			if ((s = org) != null) {
				s = Util.substringBeforeLastOpeningRoundParentheses(s);
				s = Util.substringChopEndIfMatch(s, "株");
				s = Util.removeCommaAndTrim(s);
				if (!isNoData(s)) {
					stockDetailInfo
							.setMarginSellingBalanceRatioComparisonWithPreviousWeek(Double
									.parseDouble(s));
				}
			}

			// 貸借倍率
			org = ratioOfMarginBalanceStr;
			if ((s = org) != null) {
				s = Util.substringBeforeLastOpeningRoundParentheses(s);
				s = Util.substringChopEndIfMatch(s, "倍");
				s = Util.removeCommaAndTrim(s);
				if (!isNoData(s)) {
					stockDetailInfo.setRatioOfMarginBalance(Double
							.parseDouble(s));
				}
			}

		} catch (NumberFormatException e) {
			// TODO: debug
			System.out
					.println("##### NumberFormatException was caused by : org="
							+ org + " stockCodeStr=" + stockCodeStr);
			throw e;
		}

		return stockDetailInfo;
	}

	private boolean isNoData(String s) {
		if (s == null || s.length() == 0 || s.equals(NO_DATA)) {
			return true;
		}
		return false;
	}

	public void printAll() {
		System.out.println("------------------------------");
		System.out.println("stockCodeStr=" + stockCodeStr);
		System.out.println("stockNameStr=" + stockNameStr);
		System.out.println("sectorStr=" + sectorStr);
		System.out.println();

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
		System.out.println();

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
		System.out.println();

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

	public StockDetailInfo getStockDetailInfo() {
		return stockDetailInfo;
	}
}
