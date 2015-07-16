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
	 * Returns substring before last '(' (opening round parentheses). If
	 * "(連)abc(07/14)" is specified, returns "(連)abc". If there is no opening
	 * round parentheses in the string, returns the original string.
	 * 
	 * @param org
	 * @return substring before opening round parentheses.
	 */
	public static String substringBeforeLastOpeningRoundParentheses(String org) {
		int len;
		if (org == null || (len = org.length()) == 0) {
			return org;
		}
		int index = org.lastIndexOf('(');
		if (index < 0) {
			return org;
		}
		return org.substring(0, index);
	}

	public static String substringChopStartIfMatch(String org, String chop) {
		if (org == null || org.length() == 0) {
			return org;
		}
		if (org.startsWith(chop)) {
			return org.substring(chop.length());
		}
		return org;
	}

	public static String substringChopEndIfMatch(String org, String chop) {
		if (org == null || org.length() == 0) {
			return org;
		}
		if (org.endsWith(chop)) {
			return org.substring(0, org.length() - chop.length());
		}
		return org;
	}

	/**
	 * Remove comma from String.
	 * 
	 * @param org
	 * @return
	 */
	public static String removeComma(String org) {
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
			sb.append(c);
		}
		return sb.toString();
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
