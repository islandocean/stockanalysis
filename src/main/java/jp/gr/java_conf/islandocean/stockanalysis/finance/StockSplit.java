package jp.gr.java_conf.islandocean.stockanalysis.finance;

import java.util.Calendar;

public class StockSplit {

	// If stock is splitted 1:3, preSplitShares is 1.0, and postSplitShares is
	// 3.0.
	private double preSplitShares;
	private double postSplitShares;
	private Calendar splitDay;

	public StockSplit() {
		super();
	}

	public double getPreSplitShares() {
		return preSplitShares;
	}

	public void setPreSplitShares(double preSplitShares) {
		this.preSplitShares = preSplitShares;
	}

	public double getPostSplitShares() {
		return postSplitShares;
	}

	public void setPostSplitShares(double postSplitShares) {
		this.postSplitShares = postSplitShares;
	}

	public Calendar getSplitDay() {
		return splitDay;
	}

	public void setSplitDay(Calendar splitDay) {
		this.splitDay = splitDay;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("preSplitShares=" + preSplitShares);
		sb.append(",postSplitShares=" + postSplitShares);
		sb.append(",year=" + splitDay.get(Calendar.YEAR));
		sb.append(",month=" + splitDay.get(Calendar.MONTH));
		sb.append(",day=" + splitDay.get(Calendar.DAY_OF_MONTH));
		return sb.toString();
	}
}
