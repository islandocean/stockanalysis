package jp.gr.java_conf.islandocean.stockanalysis.price;

public enum CsvSoukoEnum {

	// Ordinal of enum is used as index of array to access csv field.

	/** コード */
	STOCK_CODE,

	/** 銘柄名 */
	STOCK_NAME,

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
}
