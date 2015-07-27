package jp.gr.java_conf.islandocean.stockanalysis.finance;

import java.util.ArrayList;
import java.util.List;

import jp.gr.java_conf.islandocean.stockanalysis.common.InvalidDataException;
import jp.gr.java_conf.islandocean.stockanalysis.util.Util;

public class ProfileInfoTextAnalyzer {

	public static final String DELIM = "\t";

	List<ProfileRecord> profileRecordList;

	public ProfileInfoTextAnalyzer() {
		super();
	}

	public void analyze(List<String> lines) throws InvalidDataException {
		List<ProfileRecord> recordList = new ArrayList<ProfileRecord>();
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
			ProfileRecord record = new ProfileRecord();
			for (ProfileEnum profileEnum : ProfileEnum.values()) {
				Class<?> dataValueClass = profileEnum.getDataValueClass();
				int tsvPosition = profileEnum.ordinal();
				String field = null;
				if (tsvPosition < fields.length) {
					field = fields[tsvPosition];
				}
				Object obj = null;
				switch (profileEnum) {
				default:
					if (!isNoDataField(field)) {
						obj = Util.convClass(field, dataValueClass);
					}
					break;
				}

				if (obj != null) {
					record.put(profileEnum, obj);
				}
			}
			recordList.add(record);
		}
		this.profileRecordList = recordList;
	}

	public boolean isNoDataField(String field) {
		if (field == null || field.length() == 0) {
			return true;
		}
		return false;
	}

	public List<ProfileRecord> getProfileRecordList() {
		return profileRecordList;
	}
}
