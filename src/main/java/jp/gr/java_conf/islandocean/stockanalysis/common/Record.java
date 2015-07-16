package jp.gr.java_conf.islandocean.stockanalysis.common;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.EnumMap;
import java.util.List;

import jp.gr.java_conf.islandocean.stockanalysis.util.CalendarUtil;

public class Record extends EnumMap {

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

	public String toTsvString() {
		StringBuilder sb = new StringBuilder(100);
		Enum<?>[] allKeys = getAllKeys();
		for (int idx = 0; idx < allKeys.length; ++idx) {
			Enum<?> key = (Enum<?>) allKeys[idx];
			Object obj = this.get(key);
			if (idx != 0) {
				sb.append('\t');
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
