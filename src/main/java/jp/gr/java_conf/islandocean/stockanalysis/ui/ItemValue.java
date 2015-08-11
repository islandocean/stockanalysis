package jp.gr.java_conf.islandocean.stockanalysis.ui;

public class ItemValue {

	private String name;
	private String caption;

	public ItemValue() {
		super();
	}

	public ItemValue(String name) {
		super();
		setName(name);
	}

	@Override
	public String toString() {
		return caption;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		if (caption == null) {
			setCaption(name);
		}
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}
}
