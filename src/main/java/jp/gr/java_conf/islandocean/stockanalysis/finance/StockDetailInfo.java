package jp.gr.java_conf.islandocean.stockanalysis.finance;

import java.util.Calendar;

public class StockDetailInfo {

	private Calendar dataGetDate;

	private String stockCode;
	private String stockName;
	private String sector;

	private Double realtimePrice;
	private Double priceComparisonWithPreviousDay;
	private Double previousClosingPrice;
	private Double openingPrice;
	private Double highPrice;
	private Double lowPrice;
	private Double tradingVolumeOfStocks;
	private Double tradingValueOfMoney;
	private Double highPriceLimit;
	private Double lowPriceLimit;

	private Double marketCapitalization;
	private Double outstandingStockVolume;
	private Double annualInterestRate;
	private Double dividendsPerShare;
	private Double per;
	private Double pbr;
	private Double eps;
	private Double bps;
	private Double minimumPurchaseAmount;
	private Double shareUnitNumber;
	private Double yearlyHigh;
	private Double yearlyLow;

	private Double marginDebtBalance;
	private Double marginDebtBalanceRatioComparisonWithPreviousWeek;
	private Double marginSellingBalance;
	private Double marginSellingBalanceRatioComparisonWithPreviousWeek;
	private Double ratioOfMarginBalance;

	public StockDetailInfo() {
		super();
	}

	public Calendar getDataGetDate() {
		return dataGetDate;
	}

	public void setDataGetDate(Calendar dataGetDate) {
		this.dataGetDate = dataGetDate;
	}

	public String getStockCode() {
		return stockCode;
	}

	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}

	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}

	public Double getRealtimePrice() {
		return realtimePrice;
	}

	public void setRealtimePrice(Double realtimePrice) {
		this.realtimePrice = realtimePrice;
	}

	public Double getPriceComparisonWithPreviousDay() {
		return priceComparisonWithPreviousDay;
	}

	public void setPriceComparisonWithPreviousDay(
			Double priceComparisonWithPreviousDay) {
		this.priceComparisonWithPreviousDay = priceComparisonWithPreviousDay;
	}

	public Double getPreviousClosingPrice() {
		return previousClosingPrice;
	}

	public void setPreviousClosingPrice(Double previousClosingPrice) {
		this.previousClosingPrice = previousClosingPrice;
	}

	public Double getOpeningPrice() {
		return openingPrice;
	}

	public void setOpeningPrice(Double openingPrice) {
		this.openingPrice = openingPrice;
	}

	public Double getHighPrice() {
		return highPrice;
	}

	public void setHighPrice(Double highPrice) {
		this.highPrice = highPrice;
	}

	public Double getLowPrice() {
		return lowPrice;
	}

	public void setLowPrice(Double lowPrice) {
		this.lowPrice = lowPrice;
	}

	public Double getTradingVolumeOfStocks() {
		return tradingVolumeOfStocks;
	}

	public void setTradingVolumeOfStocks(Double tradingVolumeOfStocks) {
		this.tradingVolumeOfStocks = tradingVolumeOfStocks;
	}

	public Double getTradingValueOfMoney() {
		return tradingValueOfMoney;
	}

	public void setTradingValueOfMoney(Double tradingValueOfMoney) {
		this.tradingValueOfMoney = tradingValueOfMoney;
	}

	public Double getHighPriceLimit() {
		return highPriceLimit;
	}

	public void setHighPriceLimit(Double highPriceLimit) {
		this.highPriceLimit = highPriceLimit;
	}

	public Double getLowPriceLimit() {
		return lowPriceLimit;
	}

	public void setLowPriceLimit(Double lowPriceLimit) {
		this.lowPriceLimit = lowPriceLimit;
	}

	public Double getMarketCapitalization() {
		return marketCapitalization;
	}

	public void setMarketCapitalization(Double marketCapitalization) {
		this.marketCapitalization = marketCapitalization;
	}

	public Double getOutstandingStockVolume() {
		return outstandingStockVolume;
	}

	public void setOutstandingStockVolume(Double outstandingStockVolume) {
		this.outstandingStockVolume = outstandingStockVolume;
	}

	public Double getAnnualInterestRate() {
		return annualInterestRate;
	}

	public void setAnnualInterestRate(Double annualInterestRate) {
		this.annualInterestRate = annualInterestRate;
	}

	public Double getDividendsPerShare() {
		return dividendsPerShare;
	}

	public void setDividendsPerShare(Double dividendsPerShare) {
		this.dividendsPerShare = dividendsPerShare;
	}

	public Double getPer() {
		return per;
	}

	public void setPer(Double per) {
		this.per = per;
	}

	public Double getPbr() {
		return pbr;
	}

	public void setPbr(Double pbr) {
		this.pbr = pbr;
	}

	public Double getEps() {
		return eps;
	}

	public void setEps(Double eps) {
		this.eps = eps;
	}

	public Double getBps() {
		return bps;
	}

	public void setBps(Double bps) {
		this.bps = bps;
	}

	public Double getMinimumPurchaseAmount() {
		return minimumPurchaseAmount;
	}

	public void setMinimumPurchaseAmount(Double minimumPurchaseAmount) {
		this.minimumPurchaseAmount = minimumPurchaseAmount;
	}

	public Double getShareUnitNumber() {
		return shareUnitNumber;
	}

	public void setShareUnitNumber(Double shareUnitNumber) {
		this.shareUnitNumber = shareUnitNumber;
	}

	public Double getYearlyHigh() {
		return yearlyHigh;
	}

	public void setYearlyHigh(Double yearlyHigh) {
		this.yearlyHigh = yearlyHigh;
	}

	public Double getYearlyLow() {
		return yearlyLow;
	}

	public void setYearlyLow(Double yearlyLow) {
		this.yearlyLow = yearlyLow;
	}

	public Double getMarginDebtBalance() {
		return marginDebtBalance;
	}

	public void setMarginDebtBalance(Double marginDebtBalance) {
		this.marginDebtBalance = marginDebtBalance;
	}

	public Double getMarginDebtBalanceRatioComparisonWithPreviousWeek() {
		return marginDebtBalanceRatioComparisonWithPreviousWeek;
	}

	public void setMarginDebtBalanceRatioComparisonWithPreviousWeek(
			Double marginDebtBalanceRatioComparisonWithPreviousWeek) {
		this.marginDebtBalanceRatioComparisonWithPreviousWeek = marginDebtBalanceRatioComparisonWithPreviousWeek;
	}

	public Double getMarginSellingBalance() {
		return marginSellingBalance;
	}

	public void setMarginSellingBalance(Double marginSellingBalance) {
		this.marginSellingBalance = marginSellingBalance;
	}

	public Double getMarginSellingBalanceRatioComparisonWithPreviousWeek() {
		return marginSellingBalanceRatioComparisonWithPreviousWeek;
	}

	public void setMarginSellingBalanceRatioComparisonWithPreviousWeek(
			Double marginSellingBalanceRatioComparisonWithPreviousWeek) {
		this.marginSellingBalanceRatioComparisonWithPreviousWeek = marginSellingBalanceRatioComparisonWithPreviousWeek;
	}

	public Double getRatioOfMarginBalance() {
		return ratioOfMarginBalance;
	}

	public void setRatioOfMarginBalance(Double ratioOfMarginBalance) {
		this.ratioOfMarginBalance = ratioOfMarginBalance;
	}
}
