package jp.gr.java_conf.islandocean.stockanalysis.price;

import java.util.Calendar;

import jp.gr.java_conf.islandocean.stockanalysis.enumex.HasDataValueClass;

public enum StockEnum implements HasDataValueClass {

	/** 日付 */
	DATE(Calendar.class),

	/** コード */
	STOCK_CODE(String.class),

	/** 市場 */
	MARKET(String.class),

	/** 銘柄名 */
	STOCK_NAME(String.class),

	/** 業種 */
	SECTOR(String.class),

	/** 始値 */
	OPENING_PRICE(Double.class),

	/** 高値 */
	HIGH_PRICE(Double.class),

	/** 安値 */
	LOW_PRICE(Double.class),

	/** 終値 */
	CLOSING_PRICE(Double.class),

	/** 出来高 */
	TRADING_VOLUME_OF_STOCKS(Double.class),

	/** 売買代金 */
	TRADING_VALUE_OF_MONEY(Double.class),

	/** 始値（株式分割調整後） */
	ADJUSTED_OPENING_PRICE(Double.class),

	/** 高値（株式分割調整後） */
	ADJUSTED_HIGH_PRICE(Double.class),

	/** 安値 （株式分割調整後） */
	ADJUSTED_LOW_PRICE(Double.class),

	/** 終値 （株式分割調整後） */
	ADJUSTED_CLOSING_PRICE(Double.class),

	/** 調整適用回数 */
	SPLIT_COUNT(Integer.class);

	private Class<?> dataValueClass;

	StockEnum(Class<?> dataValueClass) {
		this.dataValueClass = dataValueClass;
	}

	public Class<?> getDataValueClass() {
		return this.dataValueClass;
	}

	public void setDataValueClass(Class<?> dataValueClass) {
		this.dataValueClass = dataValueClass;
	}
}
