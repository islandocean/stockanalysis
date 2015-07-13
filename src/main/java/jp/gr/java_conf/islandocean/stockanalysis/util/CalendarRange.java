package jp.gr.java_conf.islandocean.stockanalysis.util;

import java.util.Calendar;

public class CalendarRange {

	private Calendar begin;
	private Calendar end;

	public CalendarRange(Calendar begin, Calendar end) {
		super();
		this.begin = begin;
		this.end = end;
	}

	public Calendar getBegin() {
		return begin;
	}

	public void setBegin(Calendar begin) {
		this.begin = begin;
	}

	public Calendar getEnd() {
		return end;
	}

	public void setEnd(Calendar end) {
		this.end = end;
	}
}
