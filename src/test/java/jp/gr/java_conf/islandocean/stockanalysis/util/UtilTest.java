package jp.gr.java_conf.islandocean.stockanalysis.util;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class UtilTest {
	private String classname = Util.class.getCanonicalName();

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
		String funcname = "substringBeforeLastOpeningRoundParentheses";
		System.out.println(title(funcname));
		String org = "(連)abc(07/20)";
		String actual = Util.substringBeforeLastOpeningRoundParentheses(org);
		String expected = "(連)abc";
		System.out.println(funcname + "(\"" + org + "\")=\"" + actual + "\"");
		System.out.println("actual  =\"" + actual + "\"");
		System.out.println("expected=\"" + expected + "\"");
		System.out.println();
		assertEquals("failure - strings are not equal", expected, actual);
	}

	@Test
	public void testSubstringChopStartIfMatch() {
		String funcname = "substringChopStartIfMatch";
		System.out.println(title(funcname));
		String org = "(連)abc(07/20)";
		String chopStart = "(連)";
		String actual = Util.substringChopStartIfMatch(org, chopStart);
		String expected = "abc(07/20)";
		System.out.println(funcname + "(\"" + org + "\",\"" + chopStart
				+ "\")=\"" + actual + "\"");
		System.out.println("actual  =\"" + actual + "\"");
		System.out.println("expected=\"" + expected + "\"");
		System.out.println();
		assertEquals("failure - strings are not equal", expected, actual);
	}

	@Test
	public void testSubstringChopEndIfMatch() {
		String funcname = "substringChopEndIfMatch";
		System.out.println(title(funcname));
		String chopEnd = "万円";
		String org = "12345万円";
		String actual = Util.substringChopEndIfMatch(org, chopEnd);
		String expected = "12345";
		System.out.println(funcname + "(\"" + org + "\",\"" + chopEnd
				+ "\")=\"" + actual + "\"");
		System.out.println("actual  =\"" + actual + "\"");
		System.out.println("expected=\"" + expected + "\"");
		System.out.println();
		assertEquals("failure - strings are not equal", expected, actual);
	}

	private String title(String funcname) {
		return "Test of " + classname + "#" + funcname + "()";
	}
}
