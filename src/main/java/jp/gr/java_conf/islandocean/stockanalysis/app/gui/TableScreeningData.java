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
			ProfileRecord profile) {
		super(record, detail, profile);

		//
		// TODO:
		//
		percentageChangeOfPast = null; // Under construction !!!
		percentageChangeOfHigh = null; // Under construction !!!
		percentageChangeOfLow = null; // Under construction !!!
		periodHigh = null; // Under construction !!!
		periodLow = null; // Under construction !!!
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
