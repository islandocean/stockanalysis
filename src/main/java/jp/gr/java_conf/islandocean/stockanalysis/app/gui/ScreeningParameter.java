package jp.gr.java_conf.islandocean.stockanalysis.app.gui;

public class ScreeningParameter {

	private Double minPer;
	private Double maxPer;
	private Double minPbr;
	private Double maxPbr;
	private Double minAnnualInterestRate;
	private Double maxAnnualInterestRate;

	public ScreeningParameter() {
		super();
	}

	public Double getMinPer() {
		return minPer;
	}

	public void setMinPer(Double minPer) {
		this.minPer = minPer;
	}

	public Double getMaxPer() {
		return maxPer;
	}

	public void setMaxPer(Double maxPer) {
		this.maxPer = maxPer;
	}

	public Double getMinPbr() {
		return minPbr;
	}

	public void setMinPbr(Double minPbr) {
		this.minPbr = minPbr;
	}

	public Double getMaxPbr() {
		return maxPbr;
	}

	public void setMaxPbr(Double maxPbr) {
		this.maxPbr = maxPbr;
	}

	public Double getMinAnnualInterestRate() {
		return minAnnualInterestRate;
	}

	public void setMinAnnualInterestRate(Double minAnnualInterestRate) {
		this.minAnnualInterestRate = minAnnualInterestRate;
	}

	public Double getMaxAnnualInterestRate() {
		return maxAnnualInterestRate;
	}

	public void setMaxAnnualInterestRate(Double maxAnnualInterestRate) {
		this.maxAnnualInterestRate = maxAnnualInterestRate;
	}

}
