package jp.gr.java_conf.islandocean.stockanalysis.jreitportal;

public enum JreitEnum {

	/** 証券コード */
	STOCK_CODE,

	/** 投資法人 */
	STOCK_NAME,

	/** 投資口価格（円） */
	PRICE,

	/** 投資口価格前日比 */
	PRICE_CHANGE_PERCENT_OF_PREVIOUS_DAY,

	/** 分配金利回り(%) */
	ANNUAL_INTEREST_RATE,

	/** 一口NAV（円） */
	NAV,

	/** NAV倍率 */
	PRICE_NAV_RATIO,

	/** 時価総額（百万円） */
	MARKET_CAPITALIZATION,

	/** 出来高（口数） */
	TRADING_VOLUME_OF_STOCKS,

	/** 決算期（月） */
	ACCOUNTING_PERIOD,

	/** 運用資産 */
	TYPE_OF_PORTFOLIO,
}
