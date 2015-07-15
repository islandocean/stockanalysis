package jp.gr.java_conf.islandocean.stockanalysis.util;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

public final class Util {

	private Util() {
	}

	public static final Object convClass(String s, Class clazz) {
		if (clazz.equals(Double.class)) {
			return Double.parseDouble(s);
		}
		if (clazz.equals(String.class)) {
			return s;
		}
		if (clazz.equals(Long.class)) {
			return Long.parseLong(s);
		}
		if (clazz.equals(Integer.class)) {
			return Integer.parseInt(s);
		}
		throw new java.lang.RuntimeException(
				"Error. Internal error. Cannot convert String to specified class. class="
						+ clazz);
	}

	public static final List<String> toList(String[] array) {
		List<String> list = new LinkedList<String>();
		for (String s : array) {
			list.add(s);
		}
		return list;
	}

	/**
	 * Remove comma and nbsp(\u00a0) from String.
	 * 
	 * @param org
	 * @return
	 */
	public static String removeCommaAndNbsp(String org) {
		int len;
		if (org == null || (len = org.length()) == 0) {
			return org;
		}
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; ++i) {
			char c = org.charAt(i);
			if (c == ',') {
				continue;
			}
			if (c == '\u00a0') {
				continue;
			}
			sb.append(c);
		}
		return sb.toString();
	}

	private static final DecimalFormat percentFormat = new DecimalFormat(
			"##0.00%");

	public static final String formatPercent(double d) {
		return percentFormat.format(d);
	}
}
