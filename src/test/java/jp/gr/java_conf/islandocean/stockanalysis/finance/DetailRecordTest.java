package jp.gr.java_conf.islandocean.stockanalysis.finance;

import static org.junit.Assert.*;
import jp.gr.java_conf.islandocean.stockanalysis.common.InvalidDataException;
import jp.gr.java_conf.islandocean.stockanalysis.util.Util;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DetailRecordTest {

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
	public void test() {
		System.out.println(Util.getCurrentClassNameAndMethodName());
		DetailRecord record = new DetailRecord();
		String org = "20150717\t1491\t中外鉱業(株)\t非鉄金属\t32.0\t-1.0\t33.0\t32.0\t33.0\t31.0";
		org += "\t735700.0\t23634.0\t63.0\t3.0\t9272.0\t2.89747982E8\t0.0\t0.0\t58.18\t1.3";
		org += "\t0.55\t24.66\t3200.0\t100.0\t34.0\t27.0\t\t\t\t";
		org += "\t\t\t\t\t\t\t1837100.0\t-190700.0\t0.0\t0.0";
		org += "\t0.0";
		try {
			record.fromTsvString(org);
		} catch (InvalidDataException e) {
			e.printStackTrace();
			fail("Unexpected error.");
		}
		String actual = record.toTsvString();
		String expected = org;
		System.out.println("actual(converted)=" + actual);
		System.out.println("expect(original) =" + expected);
		assertEquals("failure - strings are not equal", expected, actual);

		// extra
		System.out.println("------------------------------");
		record.printAllNamesAndValues();
		System.out.println("------------------------------");
		System.out.println("record.header()=" + record.header());
	}
}
