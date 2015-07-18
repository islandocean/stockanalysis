package jp.gr.java_conf.islandocean.stockanalysis.jreitportal;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class JreitRecordTest {

	private static final String HEAD = "foobar";

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
	public void testJreitRecord() {
		System.out.println("Test of " + JreitRecord.class.getCanonicalName()
				+ ".");
		JreitRecord record = new JreitRecord();
		assertTrue(record != null);
		if (record != null) {
			JreitEnum[] enums = JreitEnum.values();
			for (int idx = 0; idx < enums.length; ++idx) {
				record.put(enums[idx], HEAD + String.valueOf(idx));
			}

			Enum<?>[] allKeys = record.getEnumConstants();
			for (int idx = 0; idx < allKeys.length; ++idx) {
				JreitEnum key = (JreitEnum) allKeys[idx];
				String value = (String) record.get(key);
				System.out.println("key=" + key.name());
				System.out.println("value=" + value);
				assertEquals(value, HEAD + String.valueOf(idx));
			}
		}
	}
}
