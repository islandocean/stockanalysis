package jp.gr.java_conf.islandocean.stockanalysis.app.gui;

import java.io.Serializable;

public class ScreeningParameter implements Serializable {

	private Double minAnnualInterestRate;
	private Double maxAnnualInterestRate;
	private Double minPer;
	private Double maxPer;
	private Double minPbr;
	private Double maxPbr;
	private Double minEps;
	private Double maxEps;
	private Double minBps;
	private Double maxBps;
	private Double minRoe;
	private Double maxRoe;
	private Double minMarketCapitalization;
	private Double maxMarketCapitalization;
	private Double minAverageAnnualSalary;
	private Double maxAverageAnnualSalary;
	private Double minAverageAge;
	private Double maxAverageAge;

	public ScreeningParameter() {
		super();
	}

	public boolean isEmpty() {
		if (minAnnualInterestRate == null && maxAnnualInterestRate == null
				&& minPer == null && maxPer == null && minPbr == null
				&& maxPbr == null && minEps == null && maxEps == null
				&& minBps == null && maxBps == null && minRoe == null
				&& maxRoe == null && minMarketCapitalization == null
				&& maxMarketCapitalization == null
				&& minAverageAnnualSalary == null
				&& maxAverageAnnualSalary == null && minAverageAge == null
				&& maxAverageAge == null) {
			return true;
		}
		return false;
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

	public Double getMinEps() {
		return minEps;
	}

	public void setMinEps(Double minEps) {
		this.minEps = minEps;
	}

	public Double getMaxEps() {
		return maxEps;
	}

	public void setMaxEps(Double maxEps) {
		this.maxEps = maxEps;
	}

	public Double getMinBps() {
		return minBps;
	}

	public void setMinBps(Double minBps) {
		this.minBps = minBps;
	}

	public Double getMaxBps() {
		return maxBps;
	}

	public void setMaxBps(Double maxBps) {
		this.maxBps = maxBps;
	}

	public Double getMinRoe() {
		return minRoe;
	}

	public void setMinRoe(Double minRoe) {
		this.minRoe = minRoe;
	}

	public Double getMaxRoe() {
		return maxRoe;
	}

	public void setMaxRoe(Double maxRoe) {
		this.maxRoe = maxRoe;
	}

	public Double getMinMarketCapitalization() {
		return minMarketCapitalization;
	}

	public void setMinMarketCapitalization(Double minMarketCapitalization) {
		this.minMarketCapitalization = minMarketCapitalization;
	}

	public Double getMaxMarketCapitalization() {
		return maxMarketCapitalization;
	}

	public void setMaxMarketCapitalization(Double maxMarketCapitalization) {
		this.maxMarketCapitalization = maxMarketCapitalization;
	}

	public Double getMinAverageAnnualSalary() {
		return minAverageAnnualSalary;
	}

	public void setMinAverageAnnualSalary(Double minAverageAnnualSalary) {
		this.minAverageAnnualSalary = minAverageAnnualSalary;
	}

	public Double getMaxAverageAnnualSalary() {
		return maxAverageAnnualSalary;
	}

	public void setMaxAverageAnnualSalary(Double maxAverageAnnualSalary) {
		this.maxAverageAnnualSalary = maxAverageAnnualSalary;
	}

	public Double getMinAverageAge() {
		return minAverageAge;
	}

	public void setMinAverageAge(Double minAverageAge) {
		this.minAverageAge = minAverageAge;
	}

	public Double getMaxAverageAge() {
		return maxAverageAge;
	}

	public void setMaxAverageAge(Double maxAverageAge) {
		this.maxAverageAge = maxAverageAge;
	}
}
