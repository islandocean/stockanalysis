package jp.gr.java_conf.islandocean.stockanalysis.finance;

import java.util.Calendar;

import jp.gr.java_conf.islandocean.stockanalysis.enumex.HasDataValueClass;

public enum DetailEnum implements HasDataValueClass {

	/** データ取得日付 */
	DATA_GET_DATE(Calendar.class),

	/** コード */
	STOCK_CODE(String.class),

	/** 市場 */
	MARKET(String.class),

	/** 銘柄名 */
	STOCK_NAME(String.class),

	/** 業種 */
	SECTOR(String.class),

	/** リアルタイム株価 */
	REALTIME_PRICE(Double.class),

	/** 前日比 */
	PRICE_COMPARISON_WITH_PREVIOUS_DAY(Double.class),

	/** 前日終値 */
	PREVIOUS_CLOSING_PRICE(Double.class),

	/** 始値 */
	OPENING_PRICE(Double.class),

	/** 高値 */
	HIGH_PRICE(Double.class),

	/** 安値 */
	LOW_PRICE(Double.class),

	/** 出来高 */
	TRADING_VOLUME_OF_STOCKS(Double.class),

	/** 売買代金 */
	TRADING_VALUE_OF_MONEY(Double.class),

	/** 値幅制限上限 */
	HIGH_PRICE_LIMIT(Double.class),

	/** 値幅制限下限 */
	LOW_PRICE_LIMIT(Double.class),

	/** 時価総額 */
	MARKET_CAPITALIZATION(Double.class),

	/** 発行済株式数 */
	OUTSTANDING_STOCK_VOLUME(Double.class),

	/** 配当利回り */
	ANNUAL_INTEREST_RATE(Double.class),

	/** 1株配当 */
	DIVIDENDS_PER_SHARE(Double.class),

	/** PER */
	PER(Double.class),

	/** PBR */
	PBR(Double.class),

	/** EPS */
	EPS(Double.class),

	/** BPS */
	BPS(Double.class),

	/** ROE */
	ROE(Double.class),

	/** 最低購入代金 */
	MINIMUM_PURCHASE_AMOUNT(Double.class),

	/** 単元株数 */
	SHARE_UNIT_NUMBER(Double.class),

	/** 年初来高値 */
	YEARLY_HIGH(Double.class),

	/** 年初来安値 */
	YEARLY_LOW(Double.class),

	/** 純資産 */
	NET_ASSETS(Double.class),

	/** 売買単位 */
	UNIT_OF_TRADING(Double.class),

	/** 運用会社 */
	MANAGEMENT_COMPANY(String.class),

	/** 投資対象資産 */
	TYPE_OF_ASSETS_TO_BE_INVESTED(String.class),

	/** 投資対象地域 */
	REGION_TO_BE_INVESTED(String.class),

	/** 連動対象 */
	UNDERLYING_INDEX(String.class),

	/** 決算頻度 */
	SETTLEMENT_FREQUENCY(Integer.class),

	/** 決算月 */
	SETTLEMENT_MONTH(String.class),

	/** 上場年月日 */
	LISTED_DATE(Calendar.class),

	/** 信託報酬（税抜） */
	TRUST_FEE(Double.class),

	/** 信用買残 */
	MARGIN_DEBT_BALANCE(Double.class),

	/** 信用買残前週比 */
	MARGIN_DEBT_BALANCE_RATIO_COMPARISON_WITH_PREVIOUS_WEEK(Double.class),

	/** 信用売残 */
	MARGIN_SELLING_BALANCE(Double.class),

	/** 信用売残前週比 */
	MARGIN_SELLING_BALANCE_RATIO_COMPARISON_WITH_PREVIOUS_WEEK(Double.class),

	/** 貸借倍率 */
	RATIO_OF_MARGIN_BALANCE(Double.class);

	private Class<?> dataValueClass;

	DetailEnum(Class<?> dataValueClass) {
		this.dataValueClass = dataValueClass;
	}

	public Class<?> getDataValueClass() {
		return this.dataValueClass;
	}

	public void setDataValueClass(Class<?> dataValueClass) {
		this.dataValueClass = dataValueClass;
	}
}
