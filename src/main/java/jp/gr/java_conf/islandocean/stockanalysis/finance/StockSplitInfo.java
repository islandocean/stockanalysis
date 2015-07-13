package jp.gr.java_conf.islandocean.stockanalysis.finance;

import java.util.List;

public class StockSplitInfo {

	private boolean explicitlyNoSplit = false; // 分割情報が「なし」と明示されていた場合にtrue。
	private String splitSearchStockCode;
	private List<StockSplit> stockSplitList;

	public StockSplitInfo() {
		super();
	}

	public boolean isExplicitlyNoSplit() {
		return explicitlyNoSplit;
	}

	public void setExplicitlyNoSplit(boolean explicitlyNoSplit) {
		this.explicitlyNoSplit = explicitlyNoSplit;
	}

	public String getSplitSearchStockCode() {
		return splitSearchStockCode;
	}

	public void setSplitSearchStockCode(String splitSearchStockCode) {
		this.splitSearchStockCode = splitSearchStockCode;
	}

	public List<StockSplit> getStockSplitList() {
		return stockSplitList;
	}

	public void setStockSplitList(List<StockSplit> stockSplitList) {
		this.stockSplitList = stockSplitList;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("explicitlyNoSplit=" + explicitlyNoSplit);
		sb.append(",splitSearchStockCode=" + splitSearchStockCode);
		sb.append(",stockSplitList=");
		if (stockSplitList != null) {
			boolean first = true;
			sb.append("{");
			for (StockSplit stockSplit : stockSplitList) {
				if (!first) {
					sb.append(",");
				}
				if (stockSplit != null) {
					sb.append("[" + stockSplit.toString() + "]");
				} else {
					sb.append("null");
				}
				first = false;
			}
			sb.append("}");
		} else {
			sb.append("null");
		}
		return sb.toString();
	}
}
