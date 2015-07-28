package jp.gr.java_conf.islandocean.stockanalysis.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jp.gr.java_conf.islandocean.stockanalysis.common.InvalidDataException;

public final class CalendarUtil {

	private static final SimpleDateFormat DATE_FORMAT_yyyy = new SimpleDateFormat(
			"yyyy");
	private static final SimpleDateFormat DATE_FORMAT_MM = new SimpleDateFormat(
			"MM");
	private static final SimpleDateFormat DATE_FORMAT_dd = new SimpleDateFormat(
			"dd");
	private static final SimpleDateFormat DATE_FORMAT_yyyyMMdd = new SimpleDateFormat(
			"yyyyMMdd");
	private static final SimpleDateFormat DATE_FORMAT_yyyy_MM_dd_SeparatedByHyphen = new SimpleDateFormat(
			"yyyy-MM-dd");
	private static final SimpleDateFormat DATE_FORMAT_yyMMdd = new SimpleDateFormat(
			"yyMMdd");

	private CalendarUtil() {
	}

	/**
	 * Clear hour, minute, second, millisecond of Calendar.
	 */
	public static void clearHourAndAllBelow(Calendar cal) {
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		cal.clear(Calendar.MILLISECOND);
	}

	/**
	 * Create today as Calendar. Fields hour, minute, second, and millisecond
	 * are cleared.
	 * 
	 * @return
	 */
	public static Calendar createToday() {
		Calendar cal = Calendar.getInstance();
		clearHourAndAllBelow(cal);
		return cal;
	}

	/**
	 * Create first day of this year as Calendar. Fields hour, minute, second,
	 * and millisecond are cleared.
	 * 
	 * @return
	 */
	public static Calendar createFirstDayOfThisYear() {
		return createFirstDayOfThisYear(Calendar.getInstance());
	}

	/**
	 * Create first day of this year as Calendar. Fields hour, minute, second,
	 * and millisecond are cleared.
	 * 
	 * @param now
	 * @return
	 */
	public static Calendar createFirstDayOfThisYear(Calendar now) {
		Calendar cal = (Calendar) now.clone();
		cal.set(cal.get(Calendar.YEAR), Calendar.JANUARY, 1);
		clearHourAndAllBelow(cal);
		return cal;
	}

	/**
	 * Create list of String from begin day to end day formatted as yyyyMMdd.
	 * 
	 * @param begin
	 * @param end
	 * @return
	 */
	public static List<String> createStringyyyyMMddList(Calendar begin,
			Calendar end) {
		ArrayList<String> list = new ArrayList<String>();
		for (Calendar day = (Calendar) begin.clone(); day.compareTo(end) <= 0; day
				.add(Calendar.DAY_OF_MONTH, 1)) {
			String s = DATE_FORMAT_yyyyMMdd.format(day.getTime());
			list.add(s);
		}
		return list;
	}

	public static Calendar createFirstDayOfMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, 1, 0, 0, 0);
		cal.clear(Calendar.MILLISECOND);
		return cal;
	}

	public static Calendar createLastDayOfMonth(int year, int month) {
		Calendar cal = createFirstDayOfMonth(year, month);
		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		return cal;
	}

	public static Calendar createDay(int year, int month, int day) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, day, 0, 0, 0);
		cal.clear(Calendar.MILLISECOND);
		return cal;
	}

	public static CalendarRange createCalendarRangeYearMonth(int year, int month) {
		Calendar begin = CalendarUtil.createFirstDayOfMonth(year, month);
		Calendar end = CalendarUtil.createLastDayOfMonth(year, month);
		return new CalendarRange(begin, end);
	}

	public static CalendarRange createCalendarRangeYear(int year) {
		Calendar begin = CalendarUtil.createFirstDayOfMonth(year,
				Calendar.JANUARY);
		Calendar end = CalendarUtil.createLastDayOfMonth(year,
				Calendar.DECEMBER);
		return new CalendarRange(begin, end);
	}

	public static CalendarRange createCalendarRangeRecentDays(int backdays) {
		Calendar end = CalendarUtil.createToday();
		Calendar begin = (Calendar) end.clone();
		begin.add(Calendar.DAY_OF_MONTH, -backdays);
		return new CalendarRange(begin, end);
	}

	public static Calendar createCalendarByStringyyyyMMdd(String yyyyMMdd)
			throws InvalidDataException {
		if (yyyyMMdd.length() != 8) {
			throw new InvalidDataException(
					"Error. Length of yyyyMMdd String is not 8. String="
							+ yyyyMMdd);
		}
		String yearStr = yyyyMMdd.substring(0, 4);
		String monthStr = yyyyMMdd.substring(4, 6);
		String dayStr = yyyyMMdd.substring(6, 8);
		return createDay(Integer.parseInt(yearStr),
				Integer.parseInt(monthStr) - 1, Integer.parseInt(dayStr));
	}

	/**
	 * "2015年7月25日"　-> Calendar で返す。
	 * 
	 * @param dateStr
	 * @return null if dateStr is null. Otherwize, return Calendar.
	 * @throws InvalidDataException
	 */
	public static Calendar createCalendarByJapaneseString(String dateStr)
			throws InvalidDataException {
		if (dateStr == null) {
			return null;
		}
		String s = dateStr;
		s = s.trim();
		s = Util.substringChopEndIfMatch(s, "日");
		if (s.length() < 8) {
			throw new InvalidDataException(
					"Error: dateStr is invalid. dateStr=" + dateStr);
		}

		int year;
		int month;
		int day;
		String[] p = s.split("年");
		if (p.length != 2) {
			throw new InvalidDataException(
					"Error: dateStr is invalid. dateStr=" + dateStr);
		}
		year = Integer.parseInt(p[0].trim());

		String[] q = p[1].split("月");
		if (q.length != 2) {
			throw new InvalidDataException(
					"Error: dateStr is invalid. dateStr=" + dateStr);
		}
		month = Integer.parseInt(q[0].trim()) - 1; /* because January==0 */
		day = Integer.parseInt(q[1].trim());
		Calendar cal = CalendarUtil.createDay(year, month, day);
		return cal;
	}

	public static String format_yyyy(Calendar cal) {
		return DATE_FORMAT_yyyy.format(cal.getTime());
	}

	public static String format_MM(Calendar cal) {
		return DATE_FORMAT_MM.format(cal.getTime());
	}

	public static String format_dd(Calendar cal) {
		return DATE_FORMAT_dd.format(cal.getTime());
	}

	public static String format_yyyyMMdd(Calendar cal) {
		return DATE_FORMAT_yyyyMMdd.format(cal.getTime());
	}

	public static String format_yyyy_MM_dd_SeparatedByHyphen(Calendar cal) {
		return DATE_FORMAT_yyyy_MM_dd_SeparatedByHyphen.format(cal.getTime());
	}

	public static String format_yyMMdd(Calendar cal) {
		return DATE_FORMAT_yyMMdd.format(cal.getTime());
	}
}
