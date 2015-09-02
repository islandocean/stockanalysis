package jp.gr.java_conf.islandocean.stockanalysis.app.gui;

import javafx.scene.Node;

public class History {

	private HistoryType historyType;
	private Node view;
	private String name;
	private String nameInEnglish;
	private String caption;
	private Object content;

	public History() {
		super();
	}

	public HistoryType getHistoryType() {
		return historyType;
	}

	public void setHistoryType(HistoryType historyType) {
		this.historyType = historyType;
	}

	public Node getView() {
		return view;
	}

	public void setView(Node view) {
		this.view = view;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameInEnglish() {
		return nameInEnglish;
	}

	public void setNameInEnglish(String nameInEnglish) {
		this.nameInEnglish = nameInEnglish;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}
}
