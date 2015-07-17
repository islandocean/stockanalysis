package jp.gr.java_conf.islandocean.stockanalysis.finance;

import java.util.Calendar;

import jp.gr.java_conf.islandocean.stockanalysis.util.CalendarUtil;

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

	public String toTsvString() {
		Character delim = '\t';
		StringBuilder sb = new StringBuilder(100);

		//

		String s = "";
		if (dataGetDate != null) {
			s = CalendarUtil.format_yyyyMMdd(dataGetDate);
		}
		sb.append(s);

		sb.append(delim);
		sb.append(stockCode);

		sb.append(delim);
		sb.append(stockName);

		sb.append(delim);
		sb.append(sector);

		//

		sb.append(delim);
		sb.append(realtimePrice);

		sb.append(delim);
		sb.append(priceComparisonWithPreviousDay);

		sb.append(delim);
		sb.append(previousClosingPrice);

		sb.append(delim);
		sb.append(openingPrice);

		sb.append(delim);
		sb.append(highPrice);

		sb.append(delim);
		sb.append(lowPrice);

		sb.append(delim);
		sb.append(tradingVolumeOfStocks);

		sb.append(delim);
		sb.append(tradingValueOfMoney);

		sb.append(delim);
		sb.append(highPriceLimit);

		sb.append(delim);
		sb.append(lowPriceLimit);

		//

		sb.append(delim);
		sb.append(marketCapitalization);

		sb.append(delim);
		sb.append(outstandingStockVolume);

		sb.append(delim);
		sb.append(annualInterestRate);

		sb.append(delim);
		sb.append(dividendsPerShare);

		sb.append(delim);
		sb.append(per);

		sb.append(delim);
		sb.append(pbr);

		sb.append(delim);
		sb.append(eps);

		sb.append(delim);
		sb.append(bps);

		sb.append(delim);
		sb.append(minimumPurchaseAmount);

		sb.append(delim);
		sb.append(shareUnitNumber);

		sb.append(delim);
		sb.append(yearlyHigh);

		sb.append(delim);
		sb.append(yearlyLow);

		//

		sb.append(delim);
		sb.append(marginDebtBalance);

		sb.append(delim);
		sb.append(marginDebtBalanceRatioComparisonWithPreviousWeek);

		sb.append(delim);
		sb.append(marginSellingBalance);

		sb.append(delim);
		sb.append(marginSellingBalanceRatioComparisonWithPreviousWeek);

		sb.append(delim);
		sb.append(ratioOfMarginBalance);

		return sb.toString();
	}

	public void printAll() {
		System.out.println("------------------------------");
		System.out.println("dataGetDate="
				+ CalendarUtil.format_yyyyMMdd(dataGetDate));

		System.out.println("stockCode=" + stockCode);
		System.out.println("stockName=" + stockName);
		System.out.println("sector=" + sector);

		System.out.println("realtimePrice=" + realtimePrice);
		System.out.println("priceComparisonWithPreviousDay="
				+ priceComparisonWithPreviousDay);
		System.out.println("previousClosingPrice=" + previousClosingPrice);
		System.out.println("openingPrice=" + openingPrice);
		System.out.println("highPrice=" + highPrice);
		System.out.println("lowPrice=" + lowPrice);
		System.out.println("tradingVolumeOfStocks=" + tradingVolumeOfStocks);
		System.out.println("tradingValueOfMoney=" + tradingValueOfMoney);
		System.out.println("highPriceLimit=" + highPriceLimit);
		System.out.println("lowPriceLimit=" + lowPriceLimit);

		System.out.println("marketCapitalization=" + marketCapitalization);
		System.out.println("outstandingStockVolume=" + outstandingStockVolume);
		System.out.println("annualInterestRate=" + annualInterestRate);
		System.out.println("dividendsPerShare=" + dividendsPerShare);
		System.out.println("per=" + per);
		System.out.println("pbr=" + pbr);
		System.out.println("eps=" + eps);
		System.out.println("bps=" + bps);
		System.out.println("minimumPurchaseAmount=" + minimumPurchaseAmount);
		System.out.println("shareUnitNumber=" + shareUnitNumber);
		System.out.println("yearlyHigh=" + yearlyHigh);
		System.out.println("yearlyLow=" + yearlyLow);

		System.out.println("marginDebtBalance=" + marginDebtBalance);
		System.out.println("marginDebtBalanceRatioComparisonWithPreviousWeek="
				+ marginDebtBalanceRatioComparisonWithPreviousWeek);
		System.out.println("marginSellingBalance=" + marginSellingBalance);
		System.out
				.println("marginSellingBalanceRatioComparisonWithPreviousWeek="
						+ marginSellingBalanceRatioComparisonWithPreviousWeek);
		System.out.println("ratioOfMarginBalance=" + ratioOfMarginBalance);
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
