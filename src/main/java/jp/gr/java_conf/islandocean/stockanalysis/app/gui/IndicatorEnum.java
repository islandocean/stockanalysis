package jp.gr.java_conf.islandocean.stockanalysis.app.gui;

import jp.gr.java_conf.islandocean.stockanalysis.enumex.HasDataValueClass;

public enum IndicatorEnum implements HasDataValueClass {

	/**  */
	PERCENTAGE_CHANGE_OF_PAST(Double.class),

	/**  */
	PERCENTAGE_CHANGE_OF_HIGH(Double.class),

	/**  */
	PERCENTAGE_CHANGE_OF_LOW(Double.class),

	/**  */
	PERIOD_HIGH(Double.class),

	/**  */
	PERIOD_LOW(Double.class);

	private Class<?> dataValueClass;

	IndicatorEnum(Class<?> dataValueClass) {
		this.dataValueClass = dataValueClass;
	}

	public Class<?> getDataValueClass() {
		return this.dataValueClass;
	}

	public void setDataValueClass(Class<?> dataValueClass) {
		this.dataValueClass = dataValueClass;
	}
}
