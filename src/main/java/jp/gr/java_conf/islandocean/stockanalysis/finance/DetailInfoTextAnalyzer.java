package jp.gr.java_conf.islandocean.stockanalysis.finance;

import java.util.ArrayList;
import java.util.List;

import jp.gr.java_conf.islandocean.stockanalysis.common.InvalidDataException;
import jp.gr.java_conf.islandocean.stockanalysis.util.CalendarUtil;
import jp.gr.java_conf.islandocean.stockanalysis.util.Util;

public class DetailInfoTextAnalyzer {

	public static final String DELIM = "\t";

	List<DetailRecord> detailRecordList;

	public DetailInfoTextAnalyzer() {
		super();
	}

	public void analyze(List<String> lines) throws InvalidDataException {
		List<DetailRecord> recordList = new ArrayList<DetailRecord>();
		for (int idxLine = 0; idxLine < lines.size(); ++idxLine) {
			String line = lines.get(idxLine);
			if (idxLine == 0) {
				continue; // skip header line
			}
			if (line == null || line.length() < 1) {
				System.out.println("Warning: Invalid line. line number="
						+ idxLine + " line=" + line);
				continue;
			}

			String[] fields = line.split(DELIM);
			DetailRecord record = new DetailRecord();
			for (DetailEnum detailEnum : DetailEnum.values()) {
				Class<?> dataValueClass = detailEnum.getDataValueClass();
				int tsvPosition = detailEnum.ordinal();
				String field = null;
				if (tsvPosition < fields.length) {
					field = fields[tsvPosition];
				}
				Object obj = null;
				switch (detailEnum) {
				case DATA_GET_DATE:
					if (!isNoDataField(field)) {
						obj = (Object) CalendarUtil
								.createCalendarByStringyyyyMMdd(field);
					}
					break;
				case LISTED_DATE:
					if (!isNoDataField(field)) {
						obj = (Object) CalendarUtil
								.createCalendarByStringyyyyMMdd(field);
					}
					break;
				default:
					if (!isNoDataField(field)) {
						obj = Util.convClass(field, dataValueClass);
					}
					break;
				}

				if (obj != null) {
					record.put(detailEnum, obj);
				}
			}
			recordList.add(record);
		}
		this.detailRecordList = recordList;
	}

	public boolean isNoDataField(String field) {
		if (field == null || field.length() == 0) {
			return true;
		}
		return false;
	}

	public List<DetailRecord> getDetailRecordList() {
		return detailRecordList;
	}
}
