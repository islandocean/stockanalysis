package jp.gr.java_conf.islandocean.stockanalysis.ui;

public class MarketItemValue {

	private String market;

	public MarketItemValue() {
		super();
	}

	public MarketItemValue(String market) {
		super();
		this.market = market;
	}

	@Override
	public String toString() {
		return market;
	}

	public String getMarket() {
		return market;
	}

	public void setMarket(String market) {
		this.market = market;
	}
}
