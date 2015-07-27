package jp.gr.java_conf.islandocean.stockanalysis.finance;

import jp.gr.java_conf.islandocean.stockanalysis.enumex.HasDataValueClass;

public enum ProfileEnum implements HasDataValueClass {

	/** コード */
	STOCK_CODE(String.class),

	/** 銘柄名 */
	STOCK_NAME(String.class),

	/** 特色 */
	FEATURE(String.class),

	/** 連結事業 */
	CONSOLIDATED_OPERATIONS(String.class),

	/** 本社所在地 */
	LOCATION_OF_HEAD_OFFICE(String.class),

	/** 最寄り駅 */
	NEAREST_STATION(String.class),

	/** 電話番号 */
	TELEPHONE_NUMBER(String.class),

	/** 業種分類 */
	SECTOR(String.class),

	/** 英文社名 */
	STOCK_NAME_IN_ENGLISH(String.class),

	/** 代表者名 */
	REPRESENTATIVE(String.class),

	/** 設立年月日 */
	FOUNDATION_DATE(String.class),
	// FOUNDATION_DATE(Calendar.class),

	/** 市場名 */
	MARKET(String.class),

	/** 上場年月日 */
	LISTED_DATE(String.class),
	// LISTED_DATE(Calendar.class),

	/** 決算 */
	SETTLING_DATE(String.class),

	/** 単元株数 */
	SHARE_UNIT_NUMBER(Double.class),

	/** 従業員数（単独） */
	NON_CONSOLIDATED_NUMBER_OF_EMPLOYEES(Integer.class),

	/** 従業員数 （連結） */
	CONSOLIDATED_NUMBER_OF_EMPLOYEES(Integer.class),

	/** 平均年齢 */
	AVERAGE_AGE(Double.class),

	/** 平均年収 */
	AVERAGE_ANNUAL_SALARY(Double.class);

	private Class<?> dataValueClass;

	ProfileEnum(Class<?> dataValueClass) {
		this.dataValueClass = dataValueClass;
	}

	public Class<?> getDataValueClass() {
		return this.dataValueClass;
	}

	public void setDataValueClass(Class<?> dataValueClass) {
		this.dataValueClass = dataValueClass;
	}
}
