package jp.gr.java_conf.islandocean.stockanalysis.app.gui;

import javafx.beans.property.SimpleDoubleProperty;
import jp.gr.java_conf.islandocean.stockanalysis.finance.DetailRecord;
import jp.gr.java_conf.islandocean.stockanalysis.finance.ProfileRecord;
import jp.gr.java_conf.islandocean.stockanalysis.price.StockRecord;

public class TableScreeningData extends TableStockData {

	private final SimpleDoubleProperty percentageChangeOfPast;
	private final SimpleDoubleProperty percentageChangeOfHigh;
	private final SimpleDoubleProperty percentageChangeOfLow;
	private final SimpleDoubleProperty periodHigh;
	private final SimpleDoubleProperty periodLow;

	public TableScreeningData(StockRecord record, DetailRecord detail,
			ProfileRecord profile, IndicatorRecord indicator) {
		super(record, detail, profile);

		Double d;
		if (indicator == null) {
			this.percentageChangeOfPast = null;
			this.percentageChangeOfHigh = null;
			this.percentageChangeOfLow = null;
			this.periodHigh = null;
			this.periodLow = null;
		} else {
			d = (Double) indicator.get(IndicatorEnum.PERCENTAGE_CHANGE_OF_PAST);
			this.percentageChangeOfPast = createSimpleDoublePropertyOrNull(d);

			d = (Double) indicator.get(IndicatorEnum.PERCENTAGE_CHANGE_OF_HIGH);
			this.percentageChangeOfHigh = createSimpleDoublePropertyOrNull(d);

			d = (Double) indicator.get(IndicatorEnum.PERCENTAGE_CHANGE_OF_LOW);
			this.percentageChangeOfLow = createSimpleDoublePropertyOrNull(d);

			d = (Double) indicator.get(IndicatorEnum.PERIOD_HIGH);
			this.periodHigh = createSimpleDoublePropertyOrNull(d);

			d = (Double) indicator.get(IndicatorEnum.PERIOD_LOW);
			this.periodLow = createSimpleDoublePropertyOrNull(d);
		}
	}

	public Double getPercentageChangeOfPast() {
		if (percentageChangeOfPast == null) {
			return null;
		}
		return percentageChangeOfPast.get();
	}

	public Double getPercentageChangeOfHigh() {
		if (percentageChangeOfHigh == null) {
			return null;
		}
		return percentageChangeOfHigh.get();
	}

	public Double getPercentageChangeOfLow() {
		if (percentageChangeOfLow == null) {
			return null;
		}
		return percentageChangeOfLow.get();
	}

	public Double getPeriodHigh() {
		if (periodHigh == null) {
			return null;
		}
		return periodHigh.get();
	}

	public Double getPeriodLow() {
		if (periodLow == null) {
			return null;
		}
		return periodLow.get();
	}
}
