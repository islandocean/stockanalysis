package jp.gr.java_conf.islandocean.stockanalysis.price;

import java.util.Calendar;

import jp.gr.java_conf.islandocean.stockanalysis.util.CalendarRange;
import jp.gr.java_conf.islandocean.stockanalysis.util.CalendarUtil;

public class MainDownloadKdb {

	public MainDownloadKdb() {
		super();
	}

	@SuppressWarnings("unused")
	private static CalendarRange selectCalendarRange() {
		CalendarRange calendarRange;

		calendarRange = CalendarUtil.createCalendarRangeRecentDays(14);
		if (false) {
			calendarRange = CalendarUtil.createCalendarRangeRecentDays(7);
			calendarRange = CalendarUtil.createCalendarRangeRecentDays(14);
			calendarRange = CalendarUtil.createCalendarRangeRecentDays(30);
			calendarRange = CalendarUtil.createCalendarRangeRecentDays(90);
			calendarRange = CalendarUtil.createCalendarRangeRecentDays(180);
			calendarRange = CalendarUtil.createCalendarRangeRecentDays(365);
			calendarRange = CalendarUtil.createCalendarRangeYearMonth(2013,
					Calendar.MAY);
			calendarRange = CalendarUtil.createCalendarRangeYear(2007);
			calendarRange = CalendarUtil.createCalendarRangeYear(2008);
			calendarRange = CalendarUtil.createCalendarRangeYear(2009);
			calendarRange = CalendarUtil.createCalendarRangeYear(2010);
			calendarRange = CalendarUtil.createCalendarRangeYear(2011);
			calendarRange = CalendarUtil.createCalendarRangeYear(2012);
			calendarRange = CalendarUtil.createCalendarRangeYear(2013);
			calendarRange = CalendarUtil.createCalendarRangeYear(2014);
			calendarRange = CalendarUtil.createCalendarRangeYear(2015);
			calendarRange = new CalendarRange(CalendarUtil.createDay(2007,
					Calendar.JANUARY, 1), CalendarUtil.createToday());
		}
		return calendarRange;
	}

	public static void main(String[] args) {
		DataStore store = new DataStoreKdb();
		CalendarRange calendarRange = selectCalendarRange();
		int count = store.download(calendarRange, null);
		System.out.println("count=" + count);
	}
}
