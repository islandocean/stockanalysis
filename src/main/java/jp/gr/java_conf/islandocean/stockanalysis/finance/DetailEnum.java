package jp.gr.java_conf.islandocean.stockanalysis.finance;

import java.util.Calendar;

import jp.gr.java_conf.islandocean.stockanalysis.common.ValueClassHolder;

public enum DetailEnum {

	/** データ取得日付 */
	DATA_GET_DATE,

	/** コード */
	STOCK_CODE,

	/** 銘柄名 */
	STOCK_NAME,

	/** 業種 */
	SECTOR,

	/** リアルタイム株価 */
	REALTIME_PRICE,

	/** 前日比 */
	PRICE_COMPARISON_WITH_PREVIOUS_DAY,

	/** 前日終値 */
	PREVIOUS_CLOSING_PRICE,

	/** 始値 */
	OPENING_PRICE,

	/** 高値 */
	HIGH_PRICE,

	/** 安値 */
	LOW_PRICE,

	/** 出来高 */
	TRADING_VOLUME_OF_STOCKS,

	/** 売買代金 */
	TRADING_VALUE_OF_MONEY,

	/** 値幅制限上限 */
	HIGH_PRICE_LIMIT,

	/** 値幅制限下限 */
	LOW_PRICE_LIMIT,

	/** 時価総額 */
	MARKET_CAPITALIZATION,

	/** 発行済株式数 */
	OUTSTANDING_STOCK_VOLUME,

	/** 配当利回り */
	ANNUAL_INTEREST_RATE,

	/** 1株配当 */
	DIVIDENDS_PER_SHARE,

	/** PER */
	PER,

	/** PBR */
	PBR,

	/** EPS */
	EPS,

	/** BPS */
	BPS,

	/** 最低購入代金 */
	MINIMUM_PURCHASE_AMOUNT,

	/** 単元株数 */
	SHARE_UNIT_NUMBER,

	/** 年初来高値 */
	YEARLY_HIGH,

	/** 年初来安値 */
	YEARLY_LOW,

	/** 信用買残 */
	MARGIN_DEBT_BALANCE,

	/** 信用買残前週比 */
	MARGIN_DEBT_BALANCE_RATIO_COMPARISON_WITH_PREVIOUS_WEEK,

	/** 信用売残 */
	MARGIN_SELLING_BALANCE,

	/** 信用売残前週比 */
	MARGIN_SELLING_BALANCE_RATIO_COMPARISON_WITH_PREVIOUS_WEEK,

	/** 貸借倍率 */
	RATIO_OF_MARGIN_BALANCE;

	private Class<?> dataValueClass;

	public Class<?> getDataValueClass() {
		return this.dataValueClass;
	}

	public void setDataValueClass(Class<?> valueClass) {
		this.dataValueClass = valueClass;
	}

	// Class of data value in the map.
	static {
		DATA_GET_DATE.setDataValueClass(Calendar.class);
		STOCK_CODE.setDataValueClass(String.class);
		STOCK_NAME.setDataValueClass(String.class);
		SECTOR.setDataValueClass(String.class);
		REALTIME_PRICE.setDataValueClass(Double.class);
		PRICE_COMPARISON_WITH_PREVIOUS_DAY.setDataValueClass(Double.class);
		PREVIOUS_CLOSING_PRICE.setDataValueClass(Double.class);
		OPENING_PRICE.setDataValueClass(Double.class);
		HIGH_PRICE.setDataValueClass(Double.class);
		LOW_PRICE.setDataValueClass(Double.class);
		TRADING_VOLUME_OF_STOCKS.setDataValueClass(Double.class);
		TRADING_VALUE_OF_MONEY.setDataValueClass(Double.class);
		HIGH_PRICE_LIMIT.setDataValueClass(Double.class);
		LOW_PRICE_LIMIT.setDataValueClass(Double.class);
		MARKET_CAPITALIZATION.setDataValueClass(Double.class);
		OUTSTANDING_STOCK_VOLUME.setDataValueClass(Double.class);
		ANNUAL_INTEREST_RATE.setDataValueClass(Double.class);
		DIVIDENDS_PER_SHARE.setDataValueClass(Double.class);
		PER.setDataValueClass(Double.class);
		PBR.setDataValueClass(Double.class);
		EPS.setDataValueClass(Double.class);
		BPS.setDataValueClass(Double.class);
		MINIMUM_PURCHASE_AMOUNT.setDataValueClass(Double.class);
		SHARE_UNIT_NUMBER.setDataValueClass(Double.class);
		YEARLY_HIGH.setDataValueClass(Double.class);
		YEARLY_LOW.setDataValueClass(Double.class);
		MARGIN_DEBT_BALANCE.setDataValueClass(Double.class);
		MARGIN_DEBT_BALANCE_RATIO_COMPARISON_WITH_PREVIOUS_WEEK
				.setDataValueClass(Double.class);
		MARGIN_SELLING_BALANCE.setDataValueClass(Double.class);
		MARGIN_SELLING_BALANCE_RATIO_COMPARISON_WITH_PREVIOUS_WEEK
				.setDataValueClass(Double.class);
		RATIO_OF_MARGIN_BALANCE.setDataValueClass(Double.class);

		DetailEnum[] enums = DetailEnum.values();
		Class[] array = new Class[enums.length];
		for (int idx = 0; idx < enums.length; ++idx) {
			array[idx] = enums[idx].getDataValueClass();
			if (array[idx] == null) {
				throw new RuntimeException(
						"Definition of data value class is invalid.");
			}
		}
		ValueClassHolder v = ValueClassHolder.getInstance();
		v.registValueClass(DetailEnum.class, array);
	};
}
