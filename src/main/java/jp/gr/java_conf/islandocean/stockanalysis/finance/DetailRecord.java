package jp.gr.java_conf.islandocean.stockanalysis.finance;

import jp.gr.java_conf.islandocean.stockanalysis.enumex.Record;

public class DetailRecord extends Record implements IStockBasic {

	public DetailRecord() {
		super(DetailEnum.class);
	}

	@Override
	public String getStockCode() {
		return (String) this.get(DetailEnum.STOCK_CODE);
	}

	@Override
	public String getStockName() {
		return (String) this.get(DetailEnum.STOCK_NAME);
	}

	@Override
	public String getMarket() {
		return (String) this.get(DetailEnum.MARKET);
	}

	@Override
	public String getSector() {
		return (String) this.get(DetailEnum.SECTOR);
	}
}
