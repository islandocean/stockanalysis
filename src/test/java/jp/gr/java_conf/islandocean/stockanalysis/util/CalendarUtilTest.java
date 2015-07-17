package jp.gr.java_conf.islandocean.stockanalysis.util;

import static org.junit.Assert.*;

import java.util.Calendar;

import jp.gr.java_conf.islandocean.stockanalysis.common.InvalidDataException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class CalendarUtilTest {

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
	public void testCreateDay() {
		System.out.println(Util.getCurrentClassNameAndMethodName());
		int year = 2015;
		int month = Calendar.JANUARY;
		int day = 3;
		Calendar cal = CalendarUtil.createDay(year, month, day);
		String yyyyMMdd = CalendarUtil.format_yyyyMMdd(cal);
		String actual = yyyyMMdd;
		String expected = "20150103";
		System.out.println("actual=" + actual);
		System.out.println("expected=" + expected);
		assertEquals("failure - strings are not equal", expected, actual);
	}

	@Test
	public void testCreateCalendarByStringyyyyMMdd() {
		System.out.println(Util.getCurrentClassNameAndMethodName());
		{
			String yyyyMMdd = "20150131";
			Calendar cal = null;
			try {
				cal = CalendarUtil.createCalendarByStringyyyyMMdd(yyyyMMdd);
			} catch (InvalidDataException e) {
				fail();
			}
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH);
			int day = cal.get(Calendar.DAY_OF_MONTH);
			System.out.println("Created Calendar= year=" + year + " month="
					+ month + " day=" + day);
			System.out.println("org String=" + yyyyMMdd);
			assertTrue("failure - Calendar is not created as expected.",
					year == 2015 && month == Calendar.JANUARY && day == 31);
		}

		{
			String yyyyMMdd = "20151201";
			Calendar cal = null;
			try {
				cal = CalendarUtil.createCalendarByStringyyyyMMdd("20151201");
			} catch (InvalidDataException e) {
				fail();
			}
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH);
			int day = cal.get(Calendar.DAY_OF_MONTH);
			System.out.println("Created Calendar= year=" + year + " month="
					+ month + " day=" + day);
			System.out.println("org String=" + yyyyMMdd);
			assertTrue("failure - Calendar is not created as expected.",
					year == 2015 && month == Calendar.DECEMBER && day == 1);
		}
	}
}
