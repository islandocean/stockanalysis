package jp.gr.java_conf.islandocean.stockanalysis.finance;

import jp.gr.java_conf.islandocean.stockanalysis.enumex.Record;

public class ProfileRecord extends Record implements IStockBasic {

	public ProfileRecord() {
		super(ProfileEnum.class);
	}

	@Override
	public String getStockCode() {
		return (String) this.get(ProfileEnum.STOCK_CODE);
	}

	@Override
	public String getStockName() {
		return (String) this.get(ProfileEnum.STOCK_NAME);
	}

	@Override
	public String getMarket() {
		return (String) this.get(ProfileEnum.MARKET);
	}

	@Override
	public String getSector() {
		return (String) this.get(ProfileEnum.SECTOR);
	}
}
