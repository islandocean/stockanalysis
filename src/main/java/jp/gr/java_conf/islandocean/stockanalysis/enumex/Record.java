package jp.gr.java_conf.islandocean.stockanalysis.enumex;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.EnumMap;
import java.util.List;

import jp.gr.java_conf.islandocean.stockanalysis.common.InvalidDataException;
import jp.gr.java_conf.islandocean.stockanalysis.util.CalendarUtil;
import jp.gr.java_conf.islandocean.stockanalysis.util.Util;

public class Record extends EnumMap {

	private static final String DELIM = "\t";
	private Class enumClass;

	public Record(Class enumClass) {
		super(enumClass);
		this.enumClass = enumClass;
	}

	public Enum<?>[] getEnumConstants() {
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
		Enum<?>[] allKeys = getEnumConstants();
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
		Enum<?>[] allKeys = getEnumConstants();
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

	public void fromTsvString(String line) throws InvalidDataException {
		Enum<?>[] allKeys = getEnumConstants();
		String[] a = line.split(DELIM);
		if (a.length != allKeys.length && a.length != allKeys.length - 1) {
			throw new InvalidDataException(
					"Invalid tsv line data. Number of actual items is different from number of fields of record definition."
							+ " splitted data length="
							+ a.length
							+ " allKeys.length=" + allKeys.length);
		}
		for (int idx = 0; idx < allKeys.length; ++idx) {
			Enum<?> key = (Enum<?>) allKeys[idx];
			String s = null;
			if (idx <= a.length - 1) {
				s = a[idx];
			}

			if (s == null) {
				put(key, null);
			} else {
				Class clazz = EnumUtil.getDataValueClass(enumClass, idx);
				if (clazz.equals(String.class)) {
					put(key, s);
				} else {
					if (s.length() == 0) {
						put(key, null);
					} else {
						if (clazz.equals(Double.class)) {
							put(key, Double.parseDouble(s));
						} else if (clazz.equals(Long.class)) {
							put(key, Long.parseLong(s));
						} else if (clazz.equals(Integer.class)) {
							put(key, Integer.parseInt(s));
						} else if (clazz.equals(Calendar.class)) {
							Calendar cal = null;
							try {
								cal = CalendarUtil
										.createCalendarByStringyyyyMMdd(s);
							} catch (InvalidDataException e) {
								throw e;
							}
							put(key, cal);
						}
					}
				}
			}
		}
	}

	public String toTabSeparatedNameEqualsValueString() {
		StringBuilder sb = new StringBuilder(100);
		Enum<?>[] allKeys = getEnumConstants();
		for (int idx = 0; idx < allKeys.length; ++idx) {
			Enum<?> key = (Enum<?>) allKeys[idx];
			Object obj = this.get(key);
			if (idx != 0) {
				sb.append(DELIM);
			}
			sb.append(key.name());
			sb.append('=');
			if (obj != null) {
				if (obj instanceof Calendar) {
					String s = CalendarUtil.format_yyyyMMdd((Calendar) obj);
					sb.append(s);
				} else if (obj instanceof Double || obj instanceof Long
						|| obj instanceof Integer) {
					sb.append(obj.toString());
				} else {
					sb.append(Util.escape((String) obj));
				}
			}
		}
		return sb.toString();
	}

	public void fromTabSeparatedNameEqualsValueString(String line)
			throws InvalidDataException {
		Enum<?>[] allKeys = getEnumConstants();
		String[] a = line.split(DELIM);
		for (int idx = 0; idx < a.length; ++idx) {
			String s = a[idx];
			String keyStr;
			String valueStr;
			int idxEqual = s.indexOf('=');
			if (idxEqual >= 0) {
				keyStr = s.substring(0, idxEqual);
				valueStr = s.substring(idxEqual + 1);
			} else {
				keyStr = s;
				valueStr = null;
			}

			Enum<?> key = null;
			int idxEnum = -1;
			for (Enum<?> k : allKeys) {
				if (k.name().equals(keyStr)) {
					key = k;
					idxEnum = k.ordinal();
					break;
				}
			}
			if (key == null) {
				throw new InvalidDataException(
						"Cannot find correspondent enum key. keyStr=" + keyStr
								+ " valueStr=" + valueStr);
			}

			Class clazz = EnumUtil.getDataValueClass(enumClass, idxEnum);
			if (clazz.equals(String.class)) {
				put(key, Util.unescape(valueStr));
			} else {
				if (valueStr.length() == 0) {
					put(key, null);
				} else {
					if (clazz.equals(Double.class)) {
						put(key, Double.parseDouble(valueStr));
					} else if (clazz.equals(Long.class)) {
						put(key, Long.parseLong(valueStr));
					} else if (clazz.equals(Integer.class)) {
						put(key, Integer.parseInt(valueStr));
					} else if (clazz.equals(Calendar.class)) {
						Calendar cal = null;
						try {
							cal = CalendarUtil
									.createCalendarByStringyyyyMMdd(valueStr);
						} catch (InvalidDataException e) {
							throw e;
						}
						put(key, cal);
					}
				}
			}
		}
	}

	public String header() {
		StringBuilder sb = new StringBuilder(100);
		Enum<?>[] allKeys = getEnumConstants();
		for (int idx = 0; idx < allKeys.length; ++idx) {
			Enum<?> key = (Enum<?>) allKeys[idx];
			String name = key.toString();
			if (idx != 0) {
				sb.append(DELIM);
			}
			sb.append(name);
		}
		return sb.toString();
	}

	public void printAllNamesAndValues() {
		Enum<?>[] allKeys = getEnumConstants();
		for (int idx = 0; idx < allKeys.length; ++idx) {
			Enum<?> key = (Enum<?>) allKeys[idx];
			String name = key.toString();
			Object obj = this.get(key);
			if (obj instanceof Calendar) {
				String s = CalendarUtil.format_yyyyMMdd((Calendar) obj);
				System.out.println(name + "=" + s);
			} else {
				System.out.print(name + "=");
				if (obj == null) {
					System.out.println("null");

				} else {
					System.out.println(obj.toString());
				}
			}
		}
	}
}
