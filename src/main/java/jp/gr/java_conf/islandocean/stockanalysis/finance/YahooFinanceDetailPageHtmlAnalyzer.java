package jp.gr.java_conf.islandocean.stockanalysis.finance;

import jp.gr.java_conf.islandocean.stockanalysis.util.Util;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class YahooFinanceDetailPageHtmlAnalyzer {

	private static final String CSS_QUERY_IN_DETAIL_PAGE_TO_FIND_TABLE = ".stocksTable";
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

	public YahooFinanceDetailPageHtmlAnalyzer() {
		super();
	}

	public void analyze(Document doc) {
		Elements tables = doc.select(CSS_QUERY_IN_DETAIL_PAGE_TO_FIND_TABLE);
		if (tables == null || tables.size() < 1) {
			System.out.println("Error: Cannot find table element.");
			return;
		}
		Element table = tables.get(0);
		// System.out.println("table=" + table);

		Elements tds = table.select("td");
		for (Element td : tds) {
			String text = td.text();
			System.out.println("td=" + td);
			System.out.println("text=[" + text + "]");
			if (text == null || text.length() == 0) {
			} else if (text.startsWith("前日比")) {
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

		System.out.println("---------------------------");

		Elements detailElements = doc
				.select(CSS_QUERY_IN_DETAIL_PAGE_TO_FIND_DETAIL);
		for (Element dl : detailElements) {
			Elements dt = dl.getElementsByTag("dt");
			Elements dd = dl.getElementsByTag("dd");
			String caption = dt.text();
			String value = dd.text();
			System.out.println("dt=" + dt.text());
			System.out.println("dd=" + dd.text());

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

		System.out.println("---------------------------");

		Elements rfindexElements = doc
				.select(CSS_QUERY_IN_DETAIL_PAGE_TO_FIND_RFINDEX);
		for (Element dl : rfindexElements) {
			Elements dt = dl.getElementsByTag("dt");
			Elements dd = dl.getElementsByTag("dd");
			String caption = dt.text();
			String value = dd.text();
			System.out.println("dt=" + dt.text());
			System.out.println("dd=" + dd.text());

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

		System.out.println("---------------------------");

		Elements marginElements = doc
				.select(CSS_QUERY_IN_DETAIL_PAGE_TO_FIND_MARGIN);
		boolean isDebt = false;
		boolean isSelling = false;
		for (Element dl : marginElements) {
			Elements dt = dl.getElementsByTag("dt");
			Elements dd = dl.getElementsByTag("dd");
			String caption = dt.text();
			String value = dd.text();
			System.out.println("dt=" + dt.text());
			System.out.println("dd=" + dd.text());

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

		System.out.println("---------------------------");

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

		System.out.println("---------------------------");

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

		System.out.println("---------------------------");
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
}
