package jp.gr.java_conf.islandocean.stockanalysis.price;

import java.util.Calendar;

public enum StockEnum {

	/** 日付 */
	DATE,

	/** コード */
	STOCK_CODE,

	/** 市場 */
	MARKET,

	/** 銘柄名 */
	STOCK_NAME,

	/** 業種 */
	SECTOR,

	/** 始値 */
	OPENING_PRICE,

	/** 高値 */
	HIGH_PRICE,

	/** 安値 */
	LOW_PRICE,

	/** 終値 */
	CLOSING_PRICE,

	/** 出来高 */
	TRADING_VOLUME_OF_STOCKS,

	/** 売買代金 */
	TRADING_VALUE_OF_MONEY,

	/** 始値（株式分割調整後） */
	ADJUSTED_OPENING_PRICE,

	/** 高値（株式分割調整後） */
	ADJUSTED_HIGH_PRICE,

	/** 安値 （株式分割調整後） */
	ADJUSTED_LOW_PRICE,

	/** 終値 （株式分割調整後） */
	ADJUSTED_CLOSING_PRICE,

	/** 調整適用回数 */
	SPLIT_COUNT;

	private Class<?> dataValueClass;

	public Class<?> getDataValueClass() {
		return this.dataValueClass;
	}

	public void setDataValueClass(Class<?> valueClass) {
		this.dataValueClass = valueClass;
	}

	// Class of data value in the map.
	static {
		DATE.setDataValueClass(Calendar.class);
		STOCK_CODE.setDataValueClass(String.class);
		MARKET.setDataValueClass(String.class);
		STOCK_NAME.setDataValueClass(String.class);
		SECTOR.setDataValueClass(String.class);
		OPENING_PRICE.setDataValueClass(Double.class);
		HIGH_PRICE.setDataValueClass(Double.class);
		LOW_PRICE.setDataValueClass(Double.class);
		CLOSING_PRICE.setDataValueClass(Double.class);
		TRADING_VOLUME_OF_STOCKS.setDataValueClass(Double.class);
		TRADING_VALUE_OF_MONEY.setDataValueClass(Double.class);
		ADJUSTED_OPENING_PRICE.setDataValueClass(Double.class);
		ADJUSTED_HIGH_PRICE.setDataValueClass(Double.class);
		ADJUSTED_LOW_PRICE.setDataValueClass(Double.class);
		ADJUSTED_CLOSING_PRICE.setDataValueClass(Double.class);
		SPLIT_COUNT.setDataValueClass(Integer.class);
	};
}
