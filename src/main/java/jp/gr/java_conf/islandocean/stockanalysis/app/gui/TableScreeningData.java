package jp.gr.java_conf.islandocean.stockanalysis.app.gui;

import javafx.beans.property.SimpleDoubleProperty;
import jp.gr.java_conf.islandocean.stockanalysis.finance.DetailRecord;
import jp.gr.java_conf.islandocean.stockanalysis.price.StockRecord;

public class TableScreeningData extends TableStockData {

	private final SimpleDoubleProperty percentageChangeInPrice;

	public TableScreeningData(StockRecord record, DetailRecord detail) {
		super(record, detail);

		//
		// TODO:
		//
		percentageChangeInPrice = null; // Under construction !!!
	}

	public SimpleDoubleProperty getPercentageChangeInPrice() {
		return percentageChangeInPrice;
	}
}
