package jp.gr.java_conf.islandocean.stockanalysis.price;

public enum CsvKdbEnum {

	// Ordinal of enum is used as index of array to access csv field.

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
}
