package jp.gr.java_conf.islandocean.stockanalysis.util;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class UtilTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSubstringBeforeLastOpeningRoundParentheses() {
		System.out.println(Util.getCurrentClassNameAndMethodName());
		String org = "(連)abc(07/20)";
		String actual = Util.substringBeforeLastOpeningRoundParentheses(org);
		String expected = "(連)abc";
		System.out.println("args=(\"" + org + "\")");
		System.out.println("actual  =\"" + actual + "\"");
		System.out.println("expected=\"" + expected + "\"");
		System.out.println();
		assertEquals("failure - strings are not equal", expected, actual);
	}

	@Test
	public void testSubstringChopStartIfMatch() {
		System.out.println(Util.getCurrentClassNameAndMethodName());
		String org = "(連)abc(07/20)";
		String chopStart = "(連)";
		String actual = Util.substringChopStartIfMatch(org, chopStart);
		String expected = "abc(07/20)";
		System.out.println("args=(\"" + org + "\",\"" + chopStart + "\")");
		System.out.println("actual  =\"" + actual + "\"");
		System.out.println("expected=\"" + expected + "\"");
		System.out.println();
		assertEquals("failure - strings are not equal", expected, actual);
	}

	@Test
	public void testSubstringChopEndIfMatch() {
		System.out.println(Util.getCurrentClassNameAndMethodName());
		String chopEnd = "万円";
		String org = "12345万円";
		String actual = Util.substringChopEndIfMatch(org, chopEnd);
		String expected = "12345";
		System.out.println("args(\"" + org + "\",\"" + chopEnd + "\")");
		System.out.println("actual  =\"" + actual + "\"");
		System.out.println("expected=\"" + expected + "\"");
		System.out.println();
		assertEquals("failure - strings are not equal", expected, actual);
	}

	@Test
	public void testEscapeAndUnescape() {
		System.out.println(Util.getCurrentClassNameAndMethodName());
		String org;
		String actual;
		String expected;
		String orgSave;

		org = null;
		actual = Util.escape(org);
		System.out.println("escape(" + org + ")=" + actual);
		expected = null;
		assertEquals("failure - strings are not equal", expected, actual);

		org = "abc日本\b\r\n\f世界xyz\t\"\'\\";
		actual = Util.escape(org);
		System.out.println("escape(" + org + ")=" + actual);
		expected = "abc日本\\b\\r\\n\\f世界xyz\\t\\\"\\\'\\\\";
		assertEquals("failure - strings are not equal", expected, actual);

		orgSave = org;
		org = expected;
		expected = orgSave;
		actual = Util.unescape(org);
		System.out.println("unescape(" + org + ")=" + actual);
		assertEquals("failure - strings are not equal", expected, actual);
	}
}
