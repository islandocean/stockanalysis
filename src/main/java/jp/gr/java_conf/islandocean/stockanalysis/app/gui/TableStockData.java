package jp.gr.java_conf.islandocean.stockanalysis.app.gui;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import jp.gr.java_conf.islandocean.stockanalysis.finance.DetailEnum;
import jp.gr.java_conf.islandocean.stockanalysis.finance.DetailRecord;
import jp.gr.java_conf.islandocean.stockanalysis.price.StockRecord;

public class TableStockData {
	private final SimpleStringProperty stockCode;
	private final SimpleStringProperty stockName;
	private final SimpleStringProperty market;
	private final SimpleStringProperty sector;
	private final SimpleDoubleProperty marketCapitalization;
	private final SimpleDoubleProperty annualInterestRate;
	private final SimpleDoubleProperty per;
	private final SimpleDoubleProperty pbr;
	private final SimpleDoubleProperty eps;
	private final SimpleDoubleProperty bps;

	public TableStockData(StockRecord record, DetailRecord detail) {
		Double d;

		this.stockCode = new SimpleStringProperty(record.getStockCode());
		this.stockName = new SimpleStringProperty(record.getStockName());
		this.market = new SimpleStringProperty(record.getMarket());
		this.sector = new SimpleStringProperty(record.getSector());

		if (detail == null) {
			this.marketCapitalization = null;
			this.annualInterestRate = null;
			this.per = null;
			this.pbr = null;
			this.eps = null;
			this.bps = null;
			return;
		}

		d = (Double) detail.get(DetailEnum.MARKET_CAPITALIZATION);
		if (d != null) {
			this.marketCapitalization = new SimpleDoubleProperty(d);
		} else {
			this.marketCapitalization = null;
		}

		d = (Double) detail.get(DetailEnum.ANNUAL_INTEREST_RATE);
		if (d != null) {
			this.annualInterestRate = new SimpleDoubleProperty(d);
		} else {
			this.annualInterestRate = null;
		}

		d = (Double) detail.get(DetailEnum.PER);
		if (d != null) {
			this.per = new SimpleDoubleProperty(d);
		} else {
			this.per = null;
		}

		d = (Double) detail.get(DetailEnum.PBR);
		if (d != null) {
			this.pbr = new SimpleDoubleProperty(d);
		} else {
			this.pbr = null;
		}

		d = (Double) detail.get(DetailEnum.EPS);
		if (d != null) {
			this.eps = new SimpleDoubleProperty(d);
		} else {
			this.eps = null;
		}

		d = (Double) detail.get(DetailEnum.BPS);
		if (d != null) {
			this.bps = new SimpleDoubleProperty(d);
		} else {
			this.bps = null;
		}
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

	public Double getMarketCapitalization() {
		if (marketCapitalization == null) {
			return null;
		}
		return marketCapitalization.get();
	}

	public void setMarketCapitalization(double d) {
		if (marketCapitalization == null) {
			return;
		}
		marketCapitalization.set(d);
	}

	public Double getAnnualInterestRate() {
		if (annualInterestRate == null) {
			return null;
		}
		return annualInterestRate.get();
	}

	public void setAnnualInterestRate(double d) {
		if (annualInterestRate == null) {
			return;
		}
		annualInterestRate.set(d);
	}

	public Double getPer() {
		if (per == null) {
			return null;
		}
		return per.get();
	}

	public void setPer(double d) {
		if (per == null) {
			return;
		}
		per.set(d);
	}

	public Double getPbr() {
		if (pbr == null) {
			return null;
		}
		return pbr.get();
	}

	public void setPbr(double d) {
		if (pbr == null) {
			return;
		}
		pbr.set(d);
	}

	public Double getEps() {
		if (eps == null) {
			return null;
		}
		return eps.get();
	}

	public void setEps(double d) {
		if (eps == null) {
			return;
		}
		eps.set(d);
	}

	public Double getBps() {
		if (bps == null) {
			return null;
		}
		return bps.get();
	}

	public void setBps(double d) {
		if (bps == null) {
			return;
		}
		bps.set(d);
	}
}
