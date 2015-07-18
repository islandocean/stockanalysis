package jp.gr.java_conf.islandocean.stockanalysis.enumex;

import java.io.IOException;

import jp.gr.java_conf.islandocean.stockanalysis.finance.DetailRecord;

public class DetailEnumSample {

	public DetailEnumSample() {
	}

	public static void main(String[] args) throws IOException {
		DetailRecord record = new DetailRecord();
		String expect = "20150717\t1491\t中外鉱業(株)\t非鉄金属\t32.0\t-1.0\t33.0\t32.0\t33.0\t31.0\t735700.0\t23634.0\t63.0\t3.0\t9272.0\t2.89747982E8\t0.0\t0.0\t58.18\t1.3\t0.55\t24.66\t3200.0\t100.0\t34.0\t27.0\t1837100.0\t-190700.0\t0.0\t0.0\t0.0";
		record.fromTsvString(expect);
		String actual = record.toTsvString();
		System.out.println("actual(converted)=" + actual);
		System.out.println("expect(original) =" + expect);
		// record.printAllValues();

		if (expect.equals(actual)) {
			System.out.println("Success.");
		} else {
			System.out.println("Failure.");
		}
	}
}
