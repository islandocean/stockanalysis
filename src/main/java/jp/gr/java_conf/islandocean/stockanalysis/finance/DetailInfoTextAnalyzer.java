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

			// TODO: debug
//			System.out.println("idxLine=" + idxLine + " Line=" + line);
			if (line == null || line.length() < 1) {
				System.out.println("Warning: Invalid line. line number="
						+ idxLine + " line=" + line);
				continue;
			}
			String[] fields = line.split(DELIM);

			DetailRecord record = new DetailRecord();
			for (DetailEnum detailEnum : DetailEnum.values()) {
				Class<?> dataValueClass = detailEnum.getDataValueClass();
				int ordinal = -1;
				String field = null;
				Object obj = null;

				switch (detailEnum) {
				case DATA_GET_DATE:
					String dataGetDateStr = fields[DetailEnum.DATA_GET_DATE
							.ordinal()];
					obj = (Object) CalendarUtil
							.createCalendarByStringyyyyMMdd(dataGetDateStr);
					break;
				case STOCK_CODE:
					ordinal = DetailEnum.STOCK_CODE.ordinal();
					break;
				case STOCK_NAME:
					ordinal = DetailEnum.STOCK_NAME.ordinal();
					break;
				case SECTOR:
					ordinal = DetailEnum.SECTOR.ordinal();
					break;
				case REALTIME_PRICE:
					ordinal = DetailEnum.REALTIME_PRICE.ordinal();
					break;
				case PRICE_COMPARISON_WITH_PREVIOUS_DAY:
					ordinal = DetailEnum.PRICE_COMPARISON_WITH_PREVIOUS_DAY
							.ordinal();
					break;
				case PREVIOUS_CLOSING_PRICE:
					ordinal = DetailEnum.PREVIOUS_CLOSING_PRICE.ordinal();
					break;
				case OPENING_PRICE:
					ordinal = DetailEnum.OPENING_PRICE.ordinal();
					break;
				case HIGH_PRICE:
					ordinal = DetailEnum.HIGH_PRICE.ordinal();
					break;
				case LOW_PRICE:
					ordinal = DetailEnum.LOW_PRICE.ordinal();
					break;
				case TRADING_VOLUME_OF_STOCKS:
					ordinal = DetailEnum.TRADING_VOLUME_OF_STOCKS.ordinal();
					break;
				case TRADING_VALUE_OF_MONEY:
					ordinal = DetailEnum.TRADING_VALUE_OF_MONEY.ordinal();
					break;
				case HIGH_PRICE_LIMIT:
					ordinal = DetailEnum.HIGH_PRICE_LIMIT.ordinal();
					break;
				case LOW_PRICE_LIMIT:
					ordinal = DetailEnum.LOW_PRICE_LIMIT.ordinal();
					break;
				case MARKET_CAPITALIZATION:
					ordinal = DetailEnum.MARKET_CAPITALIZATION.ordinal();
					break;
				case OUTSTANDING_STOCK_VOLUME:
					ordinal = DetailEnum.OUTSTANDING_STOCK_VOLUME.ordinal();
					break;
				case ANNUAL_INTEREST_RATE:
					ordinal = DetailEnum.ANNUAL_INTEREST_RATE.ordinal();
					break;
				case DIVIDENDS_PER_SHARE:
					ordinal = DetailEnum.DIVIDENDS_PER_SHARE.ordinal();
					break;
				case PER:
					ordinal = DetailEnum.PER.ordinal();
					break;
				case PBR:
					ordinal = DetailEnum.PBR.ordinal();
					break;
				case EPS:
					ordinal = DetailEnum.EPS.ordinal();
					break;
				case BPS:
					ordinal = DetailEnum.BPS.ordinal();
					break;
				case MINIMUM_PURCHASE_AMOUNT:
					ordinal = DetailEnum.MINIMUM_PURCHASE_AMOUNT.ordinal();
					break;
				case SHARE_UNIT_NUMBER:
					ordinal = DetailEnum.SHARE_UNIT_NUMBER.ordinal();
					break;
				case YEARLY_HIGH:
					ordinal = DetailEnum.YEARLY_HIGH.ordinal();
					break;
				case YEARLY_LOW:
					ordinal = DetailEnum.YEARLY_LOW.ordinal();
					break;
				case MARGIN_DEBT_BALANCE:
					ordinal = DetailEnum.MARGIN_DEBT_BALANCE.ordinal();
					break;
				case MARGIN_DEBT_BALANCE_RATIO_COMPARISON_WITH_PREVIOUS_WEEK:
					ordinal = DetailEnum.MARGIN_DEBT_BALANCE_RATIO_COMPARISON_WITH_PREVIOUS_WEEK
							.ordinal();
					break;
				case MARGIN_SELLING_BALANCE:
					ordinal = DetailEnum.MARGIN_SELLING_BALANCE.ordinal();
					break;
				case MARGIN_SELLING_BALANCE_RATIO_COMPARISON_WITH_PREVIOUS_WEEK:
					ordinal = DetailEnum.MARGIN_SELLING_BALANCE_RATIO_COMPARISON_WITH_PREVIOUS_WEEK
							.ordinal();
					break;
				case RATIO_OF_MARGIN_BALANCE:
					ordinal = DetailEnum.RATIO_OF_MARGIN_BALANCE.ordinal();
					break;
				}

				if (obj == null && ordinal >= 0 && ordinal < fields.length) {
					field = fields[ordinal];
					if (!isNoDataField(field)) {
						obj = Util.convClass(field, dataValueClass);
					}
				}
				record.put(detailEnum, obj);
			}
			recordList.add(record);

			// TODO: debug
			// System.out.print("anlyzer record=" + record.toTsvString());
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
