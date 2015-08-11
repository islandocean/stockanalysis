package jp.gr.java_conf.islandocean.stockanalysis.app.ui;

public class ItemValue {

	private String name;
	private String caption;
	private int numChildren = 0;

	public ItemValue() {
		super();
	}

	public ItemValue(String name) {
		super();
		setName(name);
	}

	public void addNumChildren(int add) {
		numChildren += add;
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

	public int getNumChildren() {
		return numChildren;
	}

	public void setNumChildren(int numChildren) {
		this.numChildren = numChildren;
	}
}
