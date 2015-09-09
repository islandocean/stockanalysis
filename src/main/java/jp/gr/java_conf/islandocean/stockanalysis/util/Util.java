package jp.gr.java_conf.islandocean.stockanalysis.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

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

	public static String toDigitOnly(String org) {
		int len;
		if (org == null || (len = org.length()) == 0) {
			return org;
		}
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; ++i) {
			char c = org.charAt(i);
			if (!Character.isDigit(c)) {
				continue;
			}
			sb.append(c);
		}
		return sb.toString();
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
	 * Remove comma from String, and trim.
	 * 
	 * @param org
	 * @return
	 */
	public static String removeCommaAndTrim(String org) {
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
		return sb.toString().trim();
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

	/**
	 * Normalize round parentheses.
	 * 
	 * @param org
	 * @return
	 */
	public static String normalizeRoundParentheses(String org) {
		int len;
		if (org == null || (len = org.length()) == 0) {
			return org;
		}
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; ++i) {
			char c = org.charAt(i);
			if (c == '（') {
				sb.append('(');
				continue;
			}
			if (c == '）') {
				sb.append(')');
				continue;
			}
			sb.append(c);
		}
		return sb.toString();
	}

	public static void dumpStringCodes(String org) {
		if (org == null || org.length() == 0) {
			return;
		}
		for (int idx = 0; idx < org.length(); ++idx) {
			int code = org.charAt(idx);
			System.out.print("idx=" + idx);
			System.out.print(" ");
			System.out.print("char=" + org.charAt(idx));
			System.out.print(" ");
			System.out.print("code(decimal)=" + code);
			System.out.print(" ");
			System.out.print("code(hex)=" + Integer.toHexString(code));
			System.out.println();
		}
	}

	public static String escape(String org) {
		int len;
		if (org == null || (len = org.length()) == 0) {
			return org;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len; ++i) {
			char c = org.charAt(i);
			switch (c) {
			case '\b':
				sb.append("\\b");
				break;
			case '\t':
				sb.append("\\t");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\f':
				sb.append("\\f");
				break;
			case '\r':
				sb.append("\\r");
				break;
			case '\"':
				sb.append("\\\"");
				break;
			case '\'':
				sb.append("\\\'");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			default:
				sb.append(c);
				break;
			}
		}
		return sb.toString();
	}

	public static String unescape(String org) {
		int len;
		if (org == null || (len = org.length()) == 0) {
			return org;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len; ++i) {
			char c = org.charAt(i);
			if (c == '\\') {
				if (i == len - 1) {
					sb.append('\\');
					break;
				} else {
					++i;
					char c2 = org.charAt(i);
					switch (c2) {
					case 'b':
						sb.append("\b");
						break;
					case 't':
						sb.append("\t");
						break;
					case 'n':
						sb.append("\n");
						break;
					case 'f':
						sb.append("\f");
						break;
					case 'r':
						sb.append("\r");
						break;
					case '\"':
						sb.append("\"");
						break;
					case '\'':
						sb.append("\'");
						break;
					case '\\':
						sb.append("\\");
						break;
					default:
						sb.append(c);
						break;
					}
				}
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	private static final DecimalFormat percentFormat = new DecimalFormat(
			"##0.00%");

	public static final String formatPercent(double d) {
		return percentFormat.format(d);
	}

	/**
	 * Get current methodName.
	 * 
	 * @return String methodName
	 */
	public static String getCurrentMethodName() {
		StackTraceElement stackTraceElement = Thread.currentThread()
				.getStackTrace()[2];
		String methodName = stackTraceElement.getMethodName();
		return methodName;
	}

	/**
	 * Get current className and methodName.
	 * 
	 * @return String Returns String formatted "className#methodName()".
	 */
	public static String getCurrentClassNameAndMethodName() {
		StackTraceElement stackTraceElement = Thread.currentThread()
				.getStackTrace()[2];
		String className = stackTraceElement.getClassName();
		String methodName = stackTraceElement.getMethodName();
		return className + "#" + methodName + "()";
	}

	public static String serializeObjToHex(Serializable obj) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(obj);
		oos.close();
		baos.close();
		return DatatypeConverter.printHexBinary(baos.toByteArray());
	}

	public static Serializable deserializeHexToObj(String hexStr)
			throws IOException, ClassNotFoundException {
		byte[] bytes = DatatypeConverter.parseHexBinary(hexStr);
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		ObjectInputStream ois = new ObjectInputStream(bais);
		Serializable obj = (Serializable) ois.readObject();
		ois.close();
		bais.close();
		return obj;
	}
}
