package jp.gr.java_conf.islandocean.stockanalysis.app.gui;

import java.math.BigDecimal;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import jp.gr.java_conf.islandocean.stockanalysis.finance.DetailEnum;
import jp.gr.java_conf.islandocean.stockanalysis.finance.DetailRecord;
import jp.gr.java_conf.islandocean.stockanalysis.finance.ProfileEnum;
import jp.gr.java_conf.islandocean.stockanalysis.finance.ProfileRecord;
import jp.gr.java_conf.islandocean.stockanalysis.price.StockEnum;
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
	private final SimpleDoubleProperty roe;
	private final SimpleDoubleProperty averageAnnualSalary;
	private final SimpleDoubleProperty averageAge;

	public TableStockData(StockRecord record, DetailRecord detail,
			ProfileRecord profile) {
		this.stockCode = new SimpleStringProperty(
				(String) record.get(StockEnum.STOCK_CODE));
		this.stockName = new SimpleStringProperty(
				(String) record.get(StockEnum.STOCK_NAME));
		this.market = new SimpleStringProperty(
				(String) record.get(StockEnum.MARKET));
		this.sector = new SimpleStringProperty(
				(String) record.get(StockEnum.SECTOR));

		Double d;
		if (detail == null) {
			this.marketCapitalization = null;
			this.annualInterestRate = null;
			this.per = null;
			this.pbr = null;
			this.eps = null;
			this.bps = null;
			this.roe = null;
		} else {
			d = (Double) detail.get(DetailEnum.MARKET_CAPITALIZATION);
			this.marketCapitalization = createSimpleDoublePropertyOrNull(d);

			d = (Double) detail.get(DetailEnum.ANNUAL_INTEREST_RATE);
			this.annualInterestRate = createSimpleDoublePropertyOrNull(d);

			d = (Double) detail.get(DetailEnum.PER);
			this.per = createSimpleDoublePropertyOrNull(d);

			d = (Double) detail.get(DetailEnum.PBR);
			this.pbr = createSimpleDoublePropertyOrNull(d);

			d = (Double) detail.get(DetailEnum.EPS);
			this.eps = createSimpleDoublePropertyOrNull(d);

			d = (Double) detail.get(DetailEnum.BPS);
			this.bps = createSimpleDoublePropertyOrNull(d);

			d = (Double) detail.get(DetailEnum.ROE);
			if (d != null) {
				BigDecimal b = new BigDecimal(d);
				b = b.setScale(3, BigDecimal.ROUND_HALF_UP);
				d = b.doubleValue();
			}
			this.roe = createSimpleDoublePropertyOrNull(d);
		}

		if (profile == null) {
			this.averageAnnualSalary = null;
			this.averageAge = null;
		} else {
			d = (Double) profile.get(ProfileEnum.AVERAGE_ANNUAL_SALARY);
			this.averageAnnualSalary = createSimpleDoublePropertyOrNull(d);

			d = (Double) profile.get(ProfileEnum.AVERAGE_AGE);
			this.averageAge = createSimpleDoublePropertyOrNull(d);
		}
	}

	protected SimpleDoubleProperty createSimpleDoublePropertyOrNull(Double d) {
		if (d == null) {
			return null;
		}
		return new SimpleDoubleProperty(d);
	}

	//
	// Getters
	//

	public String getStockCode() {
		return stockCode.get();
	}

	public String getStockName() {
		return stockName.get();
	}

	public String getMarket() {
		return market.get();
	}

	public String getSector() {
		return sector.get();
	}

	public Double getMarketCapitalization() {
		if (marketCapitalization == null) {
			return null;
		}
		return marketCapitalization.get();
	}

	public Double getAnnualInterestRate() {
		if (annualInterestRate == null) {
			return null;
		}
		return annualInterestRate.get();
	}

	public Double getPer() {
		if (per == null) {
			return null;
		}
		return per.get();
	}

	public Double getPbr() {
		if (pbr == null) {
			return null;
		}
		return pbr.get();
	}

	public Double getEps() {
		if (eps == null) {
			return null;
		}
		return eps.get();
	}

	public Double getBps() {
		if (bps == null) {
			return null;
		}
		return bps.get();
	}

	public Double getRoe() {
		if (roe == null) {
			return null;
		}
		return roe.get();
	}

	public Double getAverageAnnualSalary() {
		if (averageAnnualSalary == null) {
			return null;
		}
		return averageAnnualSalary.get();
	}

	public Double getAverageAge() {
		if (averageAge == null) {
			return null;
		}
		return averageAge.get();
	}
}
