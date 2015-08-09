package jp.gr.java_conf.islandocean.stockanalysis.ui;

public class SectorItemValue {

	private String sector;

	public SectorItemValue() {
		super();
	}

	public SectorItemValue(String sector) {
		super();
		this.sector = sector;
	}

	@Override
	public String toString() {
		return sector;
	}

	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}
}
