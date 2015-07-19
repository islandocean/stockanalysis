package jp.gr.java_conf.islandocean.stockanalysis.price;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

import jp.gr.java_conf.islandocean.stockanalysis.finance.StockSplit;
import jp.gr.java_conf.islandocean.stockanalysis.finance.StockSplitInfo;
import jp.gr.java_conf.islandocean.stockanalysis.util.CalendarRange;
import jp.gr.java_conf.islandocean.stockanalysis.util.IOUtil;

public class StockManager {

	private DataStore store;
	private String storeDbFolder;
	private List<List<StockRecord>> allCorpDataListInDailyList;
	private List<Calendar> dayList;
	private List<LinkedHashMap<String, StockRecord>> allCorpDataMapInDailyList;
	private LinkedHashMap<String, List<StockRecord>> corpDataListInCodeMap;

	private StockManager(DataStore store) {
		super();
		this.store = store;
		this.storeDbFolder = store.getStoreDbFolder();
	}

	public static StockManager getInstance(DataStore store) {
		return new StockManager(store);
	}

	public int load(CalendarRange calendarRange,
			boolean returnOnNewestDataLoaded) {
		Calendar begin = calendarRange.getBegin();
		Calendar end = calendarRange.getEnd();
		System.out.println("storeDbFolder=" + storeDbFolder);
		String encoding = store.getEncoding();
		int count = 0;

		allCorpDataListInDailyList = new ArrayList<List<StockRecord>>();
		dayList = new ArrayList<Calendar>();

		outer_loop: for (Calendar day = (Calendar) end.clone(); day
				.compareTo(begin) >= 0; day.add(Calendar.DAY_OF_MONTH, -1)) {
			String[] csvCandidates = this.store
					.getRelativeCsvFilePathCandidates(day);
			for (String csv : csvCandidates) {
				String fullpath = storeDbFolder + csv;

				// Read csv file and add data into list of StockRecord.
				List<String> lines = null;
				try {
					lines = IOUtil.readLocalText(fullpath, encoding);
					System.out.println("Success: read file. fullpath="
							+ fullpath);
				} catch (IOException e) {
					System.out.println("Error: File not found. fullpath="
							+ fullpath);
					continue;
				}

				// Calendar object is shared among many records created by the
				// following method, so need to clone. Because original day
				// object is changed in this loop.
				Calendar dayCopy = (Calendar) day.clone();

				List<StockRecord> recordList = this.store.createStockRecords(
						dayCopy, lines);
				if (recordList != null) {
					dayList.add(0, dayCopy);
					allCorpDataListInDailyList.add(0, recordList);
					++count;
					if (returnOnNewestDataLoaded) {
						break outer_loop;
					}
					break;
				}
			}
		}
		return count;
	}

	public void generateAllCorpDataMapInDailyList() {
		this.allCorpDataMapInDailyList = new ArrayList<LinkedHashMap<String, StockRecord>>();
		for (List<StockRecord> dataList : allCorpDataListInDailyList) {
			LinkedHashMap<String, StockRecord> map = new LinkedHashMap<String, StockRecord>();
			for (StockRecord record : dataList) {
				String code = (String) record.get(StockEnum.STOCK_CODE);
				map.put(code, record);
			}
			allCorpDataMapInDailyList.add(map);
		}
	}

	public List<StockRecord> retrieve(String stockCode) {
		List<StockRecord> records = new ArrayList<StockRecord>();
		for (LinkedHashMap<String, StockRecord> map : this.allCorpDataMapInDailyList) {
			StockRecord record = map.get(stockCode);
			if (record != null) {
				records.add(record);
			}
		}
		return records;
	}

	public void generateCorpDataListInCodeMap() throws Exception {
		this.corpDataListInCodeMap = new LinkedHashMap<String, List<StockRecord>>();
		List<StockRecord> lastData = allCorpDataListInDailyList
				.get(allCorpDataListInDailyList.size() - 1);
		// Calendar lastDay = dayList.get(dayList.size() - 1);
		for (int i = 0; i < lastData.size(); ++i) {
			String code = (String) ((StockRecord) lastData.get(i))
					.get(StockEnum.STOCK_CODE);
			List<StockRecord> records = retrieve(code);
			this.corpDataListInCodeMap.put(code, records);
		}
	}

	public void resetAdjustedPricesToOriginalPrices(List<StockRecord> records) {
		for (StockRecord record : records) {
			record.resetAdjustedPrices();
		}
	}

	/**
	 * 株式分割を考慮した株価を計算する。
	 * レコードのリストは、すべてのレコードが１つの企業の株価から構成され、日付で昇順（古いものから新しいものの順）に並んでいなければならない。
	 * 
	 * @param oneCorpRecords
	 * @param code2SplitInfoMap
	 * @param currentDay
	 */
	public void calcAdjustedPricesForOneCorp(List<StockRecord> oneCorpRecords,
			StockSplitInfo splitInfo, Calendar currentDay) {

		resetAdjustedPricesToOriginalPrices(oneCorpRecords);
		if (oneCorpRecords.size() < 1) {
			System.out.println("Warning: No record.");
			return;
		}

		String code = (String) (oneCorpRecords.get(0).get(StockEnum.STOCK_CODE));

		if (splitInfo == null) {
			return;
		}
		if (splitInfo.isExplicitlyNoSplit()) {
			return; // Completely no problem !!!
		}

		List<StockSplit> stockSplitList = splitInfo.getStockSplitList();
		if (stockSplitList == null || stockSplitList.size() <= 0) {
			// TODO: suspicious situation
			//
			// System.out
			// .println("Warning: No split info. Split info file may be old. Continue process assuming that there was no split for this stock. Stock Code="
			// + code);
			return;
		}

		for (StockSplit stockSplit : stockSplitList) {
			Calendar splitDay = stockSplit.getSplitDay();
			double pre = stockSplit.getPreSplitShares();
			double post = stockSplit.getPostSplitShares();
			double multiplier = pre / post;

			// 分割年月日が currentDay 以前の場合にその分割による株価調整を行う。
			if (currentDay.compareTo(splitDay) >= 0) {
				for (StockRecord record : oneCorpRecords) {
					Calendar priceDate = (Calendar) (record.get(StockEnum.DATE));

					if (priceDate.before(splitDay)) {
						record.multiplyAdjustedPrices(multiplier);
					} else {
						// レコードが日付順並びなので、分割日以降のレコードが現れたら残りのレコードも分割日以降。
						break;
					}
				}
			}
		}
	}

	public List<Calendar> getDayList() {
		return dayList;
	}

	public List<List<StockRecord>> getAllCorpDataListInDailyList() {
		return allCorpDataListInDailyList;
	}

	public LinkedHashMap<String, List<StockRecord>> getCorpDataListInCodeMap() {
		return corpDataListInCodeMap;
	}

	public String[] toStockCodeArray(List<StockRecord> list) {
		String[] ar = new String[list.size()];
		for (int idx = 0; idx < list.size(); ++idx) {
			StockRecord record = list.get(idx);
			ar[idx] = (String) record.get(StockEnum.STOCK_CODE);
		}
		return ar;
	}

	public String getStockCodeSuffixOfDefaultMarket() {
		return this.store.getStockCodeSuffixOfDefaultMarket();
	}
}
