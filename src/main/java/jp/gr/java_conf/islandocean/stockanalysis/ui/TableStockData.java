package jp.gr.java_conf.islandocean.stockanalysis.ui;

import javafx.beans.property.SimpleStringProperty;
import jp.gr.java_conf.islandocean.stockanalysis.price.StockRecord;

public class TableStockData {
	private final SimpleStringProperty stockCode;
	private final SimpleStringProperty stockName;
	private final SimpleStringProperty market;
	private final SimpleStringProperty sector;

	public TableStockData(String stockCode, String stockName, String market,
			String sector) {
		this.stockCode = new SimpleStringProperty(stockCode);
		this.stockName = new SimpleStringProperty(stockName);
		this.market = new SimpleStringProperty(market);
		this.sector = new SimpleStringProperty(sector);
	}

	public TableStockData(StockRecord record) {
		this.stockCode = new SimpleStringProperty(record.getStockCode());
		this.stockName = new SimpleStringProperty(record.getStockName());
		this.market = new SimpleStringProperty(record.getMarket());
		this.sector = new SimpleStringProperty(record.getSector());
	}

	public String getStockCode() {
		return stockCode.get();
	}

	public void setStockCode(String s) {
		stockCode.set(s);
	}

	public String getStockName() {
		return stockName.get();
	}

	public void setStockName(String s) {
		stockName.set(s);
	}

	public String getMarket() {
		return market.get();
	}

	public void setMarket(String s) {
		market.set(s);
	}

	public String getSector() {
		return sector.get();
	}

	public void setSector(String s) {
		sector.set(s);
	}
}
