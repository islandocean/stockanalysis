package jp.gr.java_conf.islandocean.stockanalysis.finance;

import java.util.Calendar;

public class StockDetailInfo {

	private String stockCode;
	private String stockName;
	private String sector;
	private Calendar date;
	private double realtimePrice;
	private double priceComparisonWithPreviousDay;
	private double previousClosingPrice;
	private double openingPrice;
	private double highPrice;
	private double lowPrice;
	private double tradingVolumeOfStocks;
	private double tradingValueOfMoney;
	private double highPriceLimit;
	private double lowPriceLimit;
	private double marketCapitalization;
	private double outstandingStockVolume;
	private double annualInterestRate;
	private double dividendsPerShare;
	private double per;
	private double pbr;
	private double eps;
	private double bps;
	private double minimumPurchaseAmount;
	private double shareUnitNumber;
	private double yearlyHigh;
	private double yearlyLow;
	private double marginDebtBalance;
	private double marginDebtBalanceRatioComparisonWithPreviousWeek;
	private double marginSellingBalance;
	private double marginSellingBalanceRatioComparisonWithPreviousWeek;
	private double ratioOfMarginBalance;

	public StockDetailInfo() {
		super();
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

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

	public double getRealtimePrice() {
		return realtimePrice;
	}

	public void setRealtimePrice(double realtimePrice) {
		this.realtimePrice = realtimePrice;
	}

	public double getPriceComparisonWithPreviousDay() {
		return priceComparisonWithPreviousDay;
	}

	public void setPriceComparisonWithPreviousDay(
			double priceComparisonWithPreviousDay) {
		this.priceComparisonWithPreviousDay = priceComparisonWithPreviousDay;
	}

	public double getLastDayClosingPrice() {
		return previousClosingPrice;
	}

	public void setLastDayClosingPrice(double lastDayClosingPrice) {
		this.previousClosingPrice = lastDayClosingPrice;
	}

	public double getOpeningPrice() {
		return openingPrice;
	}

	public void setOpeningPrice(double openingPrice) {
		this.openingPrice = openingPrice;
	}

	public double getHighPrice() {
		return highPrice;
	}

	public void setHighPrice(double highPrice) {
		this.highPrice = highPrice;
	}

	public double getLowPrice() {
		return lowPrice;
	}

	public void setLowPrice(double lowPrice) {
		this.lowPrice = lowPrice;
	}

	public double getTradingVolumeOfStocks() {
		return tradingVolumeOfStocks;
	}

	public void setTradingVolumeOfStocks(double tradingVolumeOfStocks) {
		this.tradingVolumeOfStocks = tradingVolumeOfStocks;
	}

	public double getTradingValueOfMoney() {
		return tradingValueOfMoney;
	}

	public void setTradingValueOfMoney(double tradingValueOfMoney) {
		this.tradingValueOfMoney = tradingValueOfMoney;
	}

	public double getHighPriceLimit() {
		return highPriceLimit;
	}

	public void setHighPriceLimit(double highPriceLimit) {
		this.highPriceLimit = highPriceLimit;
	}

	public double getLowPriceLimit() {
		return lowPriceLimit;
	}

	public void setLowPriceLimit(double lowPriceLimit) {
		this.lowPriceLimit = lowPriceLimit;
	}

	public double getMarketCapitalization() {
		return marketCapitalization;
	}

	public void setMarketCapitalization(double marketCapitalization) {
		this.marketCapitalization = marketCapitalization;
	}

	public double getOutstandingStockVolume() {
		return outstandingStockVolume;
	}

	public void setOutstandingStockVolume(double outstandingStockVolume) {
		this.outstandingStockVolume = outstandingStockVolume;
	}

	public double getAnnualInterestRate() {
		return annualInterestRate;
	}

	public void setAnnualInterestRate(double annualInterestRate) {
		this.annualInterestRate = annualInterestRate;
	}

	public double getDividendsPerShare() {
		return dividendsPerShare;
	}

	public void setDividendsPerShare(double dividendsPerShare) {
		this.dividendsPerShare = dividendsPerShare;
	}

	public double getPer() {
		return per;
	}

	public void setPer(double per) {
		this.per = per;
	}

	public double getPbr() {
		return pbr;
	}

	public void setPbr(double pbr) {
		this.pbr = pbr;
	}

	public double getEps() {
		return eps;
	}

	public void setEps(double eps) {
		this.eps = eps;
	}

	public double getBps() {
		return bps;
	}

	public void setBps(double bps) {
		this.bps = bps;
	}

	public double getMinimumPurchaseAmount() {
		return minimumPurchaseAmount;
	}

	public void setMinimumPurchaseAmount(double minimumPurchaseAmount) {
		this.minimumPurchaseAmount = minimumPurchaseAmount;
	}

	public double getShareUnitNumber() {
		return shareUnitNumber;
	}

	public void setShareUnitNumber(double shareUnitNumber) {
		this.shareUnitNumber = shareUnitNumber;
	}

	public double getYearlyHigh() {
		return yearlyHigh;
	}

	public void setYearlyHigh(double yearlyHigh) {
		this.yearlyHigh = yearlyHigh;
	}

	public double getYearlyLow() {
		return yearlyLow;
	}

	public void setYearlyLow(double yearlyLow) {
		this.yearlyLow = yearlyLow;
	}

	public double getMarginDebtBalance() {
		return marginDebtBalance;
	}

	public void setMarginDebtBalance(double marginDebtBalance) {
		this.marginDebtBalance = marginDebtBalance;
	}

	public double getMarginDebtBalanceRatioComparisonWithPreviousWeek() {
		return marginDebtBalanceRatioComparisonWithPreviousWeek;
	}

	public void setMarginDebtBalanceRatioComparisonWithPreviousWeek(
			double marginDebtBalanceRatioComparisonWithPreviousWeek) {
		this.marginDebtBalanceRatioComparisonWithPreviousWeek = marginDebtBalanceRatioComparisonWithPreviousWeek;
	}

	public double getMarginSellingBalance() {
		return marginSellingBalance;
	}

	public void setMarginSellingBalance(double marginSellingBalance) {
		this.marginSellingBalance = marginSellingBalance;
	}

	public double getMarginSellingBalanceRatioComparisonWithPreviousWeek() {
		return marginSellingBalanceRatioComparisonWithPreviousWeek;
	}

	public void setMarginSellingBalanceRatioComparisonWithPreviousWeek(
			double marginSellingBalanceRatioComparisonWithPreviousWeek) {
		this.marginSellingBalanceRatioComparisonWithPreviousWeek = marginSellingBalanceRatioComparisonWithPreviousWeek;
	}

	public double getRatioOfMarginBalance() {
		return ratioOfMarginBalance;
	}

	public void setRatioOfMarginBalance(double ratioOfMarginBalance) {
		this.ratioOfMarginBalance = ratioOfMarginBalance;
	}
}
