package jp.gr.java_conf.islandocean.stockanalysis.common;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.EnumMap;
import java.util.List;

import jp.gr.java_conf.islandocean.stockanalysis.util.CalendarUtil;

public class Record extends EnumMap {

	private static String DELIM = "\t";

	private Class enumClass;

	public Record(Class enumClass) {
		super(enumClass);
		this.enumClass = enumClass;

		if (!enumClass.isEnum()) {
			// TODO: error.
		}
	}

	public Enum<?>[] getAllKeys() {
		return (Enum<?>[]) enumClass.getEnumConstants();
	}

	/**
	 * Get all values retrieved by all Enum keys. Value null is converted to
	 * empty String.
	 * 
	 * @return List of value String.
	 */
	public List<String> getAllValues() {
		ArrayList<String> list = new ArrayList<String>();
		Enum<?>[] allKeys = getAllKeys();
		for (int idx = 0; idx < allKeys.length; ++idx) {
			Enum<?> key = (Enum<?>) allKeys[idx];
			String val = (String) this.get(key);
			if (val == null) {
				val = "";
			}
			list.add(val);
		}
		return list;
	}

	// public void printAllValues() {
	// List list = getAllValues();
	// for (int idx = 0; idx < list.size(); ++idx) {
	// System.out.println("idx=" + idx + " value=" + list.get(idx).toString());
	// }
	// }

	public String toTsvString() {
		StringBuilder sb = new StringBuilder(100);
		Enum<?>[] allKeys = getAllKeys();
		for (int idx = 0; idx < allKeys.length; ++idx) {
			Enum<?> key = (Enum<?>) allKeys[idx];
			Object obj = this.get(key);
			if (idx != 0) {
				sb.append(DELIM);
			}
			if (obj != null) {
				if (obj instanceof Calendar) {
					String s = CalendarUtil.format_yyyyMMdd((Calendar) obj);
					sb.append(s);
				} else {
					sb.append(obj.toString());
				}
			}
		}
		return sb.toString();
	}

	public void fromTsvString(String line) {
		ValueClassHolder v = ValueClassHolder.getInstance();
		Class[] valueClasses = v.referValueClass(enumClass);
		Enum<?>[] allKeys = getAllKeys();

		String[] a = line.split(DELIM);
		if (a.length != allKeys.length) {
			// TODO:
			throw new RuntimeException(
					"Invalid csv line data. Number of items is different from number of fields of record.");
		}
		for (int idx = 0; idx < allKeys.length; ++idx) {
			Enum<?> key = (Enum<?>) allKeys[idx];
			String s = a[idx];
			if (s == null) {
				put(key, null);
			} else {
				Class clazz = valueClasses[idx];
				if (clazz.equals(Double.class)) {
					put(key, Double.parseDouble(s));
				} else if (clazz.equals(String.class)) {
					put(key, s);
				} else if (clazz.equals(Long.class)) {
					put(key, Long.parseLong(s));
				} else if (clazz.equals(Integer.class)) {
					put(key, Integer.parseInt(s));
				} else if (clazz.equals(Calendar.class)) {
					Calendar cal = null;
					try {
						cal = CalendarUtil.createCalendarByStringyyyyMMdd(s);
					} catch (InvalidDataException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					put(key, cal);
				}
			}
		}
	}

	// output CSV
	@Deprecated
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(100);
		Enum<?>[] allKeys = getAllKeys();
		for (int idx = 0; idx < allKeys.length; ++idx) {
			Enum<?> key = (Enum<?>) allKeys[idx];
			Object obj = this.get(key);
			if (idx != 0) {
				sb.append(',');
			}
			if (obj != null) {
				if (obj instanceof Calendar) {
					String s = CalendarUtil.format_yyyyMMdd((Calendar) obj);
					sb.append(s);
				} else {
					sb.append(obj.toString());
				}
			}
		}
		return sb.toString();
	}
}
