package jp.gr.java_conf.islandocean.stockanalysis.finance;

import java.util.Calendar;

import jp.gr.java_conf.islandocean.stockanalysis.common.InvalidDataException;
import jp.gr.java_conf.islandocean.stockanalysis.util.CalendarUtil;

public class StockDetailInfo {

	private static Character DELIM = '\t';

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

	public StockDetailInfo(String tsvLine) throws InvalidDataException {
		super();

		String[] a = tsvLine.split("" + DELIM);
		if (a.length != 31) {
			// TODO:
			throw new RuntimeException(
					"Invalid csv line data. Items of csv line should be 31.");
		}

		String s;
		int idx = 0;

		//

		s = a[idx++];
		Calendar cal = CalendarUtil.createCalendarByStringyyyyMMdd(s);
		setDataGetDate(cal);

		s = a[idx++];
		if (s != null && s.length() != 0) {
			setStockCode(s);
		}

		s = a[idx++];
		if (s != null && s.length() != 0) {
			setStockName(s);
		}

		s = a[idx++];
		if (s != null && s.length() != 0) {
			setSector(s);
		}

		//

		s = a[idx++];
		if (s != null && s.length() != 0) {
			setRealtimePrice(Double.parseDouble(s));
		}

		s = a[idx++];
		if (s != null && s.length() != 0) {
			setPriceComparisonWithPreviousDay(Double.parseDouble(s));
		}

		s = a[idx++];
		if (s != null && s.length() != 0) {
			setPreviousClosingPrice(Double.parseDouble(s));
		}

		s = a[idx++];
		if (s != null && s.length() != 0) {
			setOpeningPrice(Double.parseDouble(s));
		}

		s = a[idx++];
		if (s != null && s.length() != 0) {
			setHighPrice(Double.parseDouble(s));
		}

		s = a[idx++];
		if (s != null && s.length() != 0) {
			setLowPrice(Double.parseDouble(s));
		}

		s = a[idx++];
		if (s != null && s.length() != 0) {
			setTradingVolumeOfStocks(Double.parseDouble(s));
		}

		s = a[idx++];
		if (s != null && s.length() != 0) {
			setTradingValueOfMoney(Double.parseDouble(s));
		}

		s = a[idx++];
		if (s != null && s.length() != 0) {
			setHighPriceLimit(Double.parseDouble(s));
		}

		s = a[idx++];
		if (s != null && s.length() != 0) {
			setLowPriceLimit(Double.parseDouble(s));
		}

		//

		s = a[idx++];
		if (s != null && s.length() != 0) {
			setMarketCapitalization(Double.parseDouble(s));
		}

		s = a[idx++];
		if (s != null && s.length() != 0) {
			setOutstandingStockVolume(Double.parseDouble(s));
		}

		s = a[idx++];
		if (s != null && s.length() != 0) {
			setAnnualInterestRate(Double.parseDouble(s));
		}

		s = a[idx++];
		if (s != null && s.length() != 0) {
			setDividendsPerShare(Double.parseDouble(s));
		}

		s = a[idx++];
		if (s != null && s.length() != 0) {
			setPer(Double.parseDouble(s));
		}

		s = a[idx++];
		if (s != null && s.length() != 0) {
			setPbr(Double.parseDouble(s));
		}

		s = a[idx++];
		if (s != null && s.length() != 0) {
			setEps(Double.parseDouble(s));
		}

		s = a[idx++];
		if (s != null && s.length() != 0) {
			setBps(Double.parseDouble(s));
		}

		s = a[idx++];
		if (s != null && s.length() != 0) {
			setMinimumPurchaseAmount(Double.parseDouble(s));
		}

		s = a[idx++];
		if (s != null && s.length() != 0) {
			setShareUnitNumber(Double.parseDouble(s));
		}

		s = a[idx++];
		if (s != null && s.length() != 0) {
			setYearlyHigh(Double.parseDouble(s));
		}

		s = a[idx++];
		if (s != null && s.length() != 0) {
			setYearlyLow(Double.parseDouble(s));
		}

		//

		s = a[idx++];
		if (s != null && s.length() != 0) {
			setMarginDebtBalance(Double.parseDouble(s));
		}

		s = a[idx++];
		if (s != null && s.length() != 0) {
			setMarginDebtBalanceRatioComparisonWithPreviousWeek(Double
					.parseDouble(s));
		}

		s = a[idx++];
		if (s != null && s.length() != 0) {
			setMarginSellingBalance(Double.parseDouble(s));
		}

		s = a[idx++];
		if (s != null && s.length() != 0) {
			setMarginSellingBalanceRatioComparisonWithPreviousWeek(Double
					.parseDouble(s));
		}

		s = a[idx++];
		if (s != null && s.length() != 0) {
			setRatioOfMarginBalance(Double.parseDouble(s));
		}
	}

	public String toTsvString() {
		StringBuilder sb = new StringBuilder(200);

		//

		String s = "";
		if (dataGetDate != null) {
			s = CalendarUtil.format_yyyyMMdd(dataGetDate);
		}
		sb.append(s);

		sb.append(DELIM);
		if (stockCode != null) {
			sb.append(stockCode);
		}

		sb.append(DELIM);
		if (stockName != null) {
			sb.append(stockName);
		}

		sb.append(DELIM);
		if (sector != null) {
			sb.append(sector);
		}

		//

		sb.append(DELIM);
		if (realtimePrice != null) {
			sb.append(realtimePrice);
		}

		sb.append(DELIM);
		if (priceComparisonWithPreviousDay != null) {
			sb.append(priceComparisonWithPreviousDay);
		}

		sb.append(DELIM);
		if (previousClosingPrice != null) {
			sb.append(previousClosingPrice);
		}

		sb.append(DELIM);
		if (openingPrice != null) {
			sb.append(openingPrice);
		}

		sb.append(DELIM);
		if (highPrice != null) {
			sb.append(highPrice);
		}

		sb.append(DELIM);
		if (lowPrice != null) {
			sb.append(lowPrice);
		}

		sb.append(DELIM);
		if (tradingVolumeOfStocks != null) {
			sb.append(tradingVolumeOfStocks);
		}

		sb.append(DELIM);
		if (tradingValueOfMoney != null) {
			sb.append(tradingValueOfMoney);
		}

		sb.append(DELIM);
		if (highPriceLimit != null) {
			sb.append(highPriceLimit);
		}

		sb.append(DELIM);
		if (lowPriceLimit != null) {
			sb.append(lowPriceLimit);
		}

		//

		sb.append(DELIM);
		if (marketCapitalization != null) {
			sb.append(marketCapitalization);
		}

		sb.append(DELIM);
		if (outstandingStockVolume != null) {
			sb.append(outstandingStockVolume);
		}

		sb.append(DELIM);
		if (annualInterestRate != null) {
			sb.append(annualInterestRate);
		}

		sb.append(DELIM);
		if (dividendsPerShare != null) {
			sb.append(dividendsPerShare);
		}

		sb.append(DELIM);
		if (per != null) {
			sb.append(per);
		}

		sb.append(DELIM);
		if (pbr != null) {
			sb.append(pbr);
		}

		sb.append(DELIM);
		if (eps != null) {
			sb.append(eps);
		}

		sb.append(DELIM);
		if (bps != null) {
			sb.append(bps);
		}

		sb.append(DELIM);
		if (minimumPurchaseAmount != null) {
			sb.append(minimumPurchaseAmount);
		}

		sb.append(DELIM);
		if (shareUnitNumber != null) {
			sb.append(shareUnitNumber);
		}

		sb.append(DELIM);
		if (yearlyHigh != null) {
			sb.append(yearlyHigh);
		}

		sb.append(DELIM);
		if (yearlyLow != null) {
			sb.append(yearlyLow);
		}

		//

		sb.append(DELIM);
		if (marginDebtBalance != null) {
			sb.append(marginDebtBalance);
		}

		sb.append(DELIM);
		if (marginDebtBalanceRatioComparisonWithPreviousWeek != null) {
			sb.append(marginDebtBalanceRatioComparisonWithPreviousWeek);
		}

		sb.append(DELIM);
		if (marginSellingBalance != null) {
			sb.append(marginSellingBalance);
		}

		sb.append(DELIM);
		if (marginSellingBalanceRatioComparisonWithPreviousWeek != null) {
			sb.append(marginSellingBalanceRatioComparisonWithPreviousWeek);
		}

		sb.append(DELIM);
		if (ratioOfMarginBalance != null) {
			sb.append(ratioOfMarginBalance);
		}

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
