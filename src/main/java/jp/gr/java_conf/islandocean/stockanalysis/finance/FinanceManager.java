package jp.gr.java_conf.islandocean.stockanalysis.finance;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.gr.java_conf.islandocean.stockanalysis.common.InvalidDataException;
import jp.gr.java_conf.islandocean.stockanalysis.price.StockEnum;
import jp.gr.java_conf.islandocean.stockanalysis.price.StockRecord;
import jp.gr.java_conf.islandocean.stockanalysis.util.CalendarRange;
import jp.gr.java_conf.islandocean.stockanalysis.util.HtmlUtil;

import org.jsoup.nodes.Document;

public class FinanceManager {

	private Map<String, StockSplitInfo> stockCodeToSplitInfoMap;
	private Map<String, DetailRecord> stockCodeToDetailRecordMap;
	private Map<String, ProfileRecord> stockCodeToProfileRecordMap;

	private FinanceManager() {
		super();
		File folder = new File(Config.getBaseFolder());
		if (!folder.exists()) {
			if (!folder.mkdir()) {
			}
		}
	}

	public static FinanceManager getInstance() {
		return new FinanceManager();
	}

	//
	// Read remote HTML page.
	//

	public String getHtmlChartPageSpec(String code) {
		String spec = Config.getRemoteLocationStocksChart() + code;
		return spec;
	}

	public String getHtmlDetailPageSpec(String code) {
		String spec = Config.getRemoteLocationStocksDetail() + code;
		return spec;
	}

	public String getHtmlProfilePageSpec(String code) {
		String spec = Config.getRemoteLocationStocksProfile() + code;
		return spec;
	}

	public Document readRemoteHtmlChartPage(String code) throws IOException {
		String spec = getHtmlChartPageSpec(code);
		Document doc = HtmlUtil.readRemoteHtml(spec);
		return doc;
	}

	public Document readRemoteHtmlDetailPage(String code) throws IOException {
		String spec = getHtmlDetailPageSpec(code);
		Document doc = HtmlUtil.readRemoteHtml(spec);
		return doc;
	}

	public Document readRemoteHtmlProfilePage(String code) throws IOException {
		String spec = getHtmlProfilePageSpec(code);
		Document doc = HtmlUtil.readRemoteHtml(spec);
		return doc;
	}

	//
	// Read locally saved text.
	//

	public List<String> readLocalSplitInfoText() throws IOException {
		String filename = Config.getSplitInformationFilename()
				+ Config.getSplitInformationExt();
		FileSystem fs = FileSystems.getDefault();
		Path src = fs.getPath(filename);
		List<String> lines = Files.readAllLines(src, StandardCharsets.UTF_8);
		return lines;
	}

	public List<String> readLocalDetailInfoText() throws IOException {
		String filename = Config.getDetailInformationFilename()
				+ Config.getDetailInformationExt();
		FileSystem fs = FileSystems.getDefault();
		Path src = fs.getPath(filename);
		List<String> lines = Files.readAllLines(src, StandardCharsets.UTF_8);
		return lines;
	}

	public List<String> readLocalProfileInfoText() throws IOException {
		String filename = Config.getProfileInformationFilename()
				+ Config.getProfileInformationExt();
		FileSystem fs = FileSystems.getDefault();
		Path src = fs.getPath(filename);
		List<String> lines = Files.readAllLines(src, StandardCharsets.UTF_8);
		return lines;
	}

	//
	// Stock code transform.
	//

	public String toSplitSearchStockCode(String stockCode) {
		int index = stockCode.indexOf('-');
		if (index >= 0) {
			stockCode = stockCode.substring(0, index);
		}
		return stockCode;
	}

	public String toDetailSearchStockCode(String stockCode) {
		return stockCode.replace('-', '.');
	}

	//
	// Split Info
	//

	private Map<String, StockSplitInfo> listToMapForStockSplitInfo(
			List<StockSplitInfo> list) {
		Map<String, StockSplitInfo> map = new HashMap<String, StockSplitInfo>();
		for (StockSplitInfo info : list) {
			String stockCode = info.getSplitSearchStockCode();
			if (stockCode != null) {
				String splitSearchStockCode = toSplitSearchStockCode(stockCode);
				if (map.get(splitSearchStockCode) == null) {
					map.put(splitSearchStockCode, info);
				}
			}
		}
		return map;
	}

	public void generateStockCodeToSplitInfoMap() throws IOException {
		List<String> lines = readLocalSplitInfoText();
		SplitInfoTextAnalyzer analyzer = new SplitInfoTextAnalyzer();
		analyzer.analyze(lines);
		List<StockSplitInfo> list = analyzer.getStockSplitInfoList();
		Map<String, StockSplitInfo> map = listToMapForStockSplitInfo(list);
		this.stockCodeToSplitInfoMap = map;
	}

	public void checkAndWarnSplitInfo(List<StockRecord> list,
			Map<String, StockSplitInfo> code2SplitInfoMap) {
		for (int idxCorp = 0; idxCorp < list.size(); ++idxCorp) {
			String stockCode = (String) ((StockRecord) list.get(idxCorp))
					.get(StockEnum.STOCK_CODE);
			String splitSerachStockCode = toSplitSearchStockCode(stockCode);
			StockSplitInfo stockSplitInfo = (StockSplitInfo) code2SplitInfoMap
					.get(splitSerachStockCode);
			if (stockSplitInfo == null) {
				System.out
						.println("Warning: No split info. Continue process assuming that there was no split for this stock, so be careful. Stock Code="
								+ stockCode);
			}
		}
	}

	public int countSplitsInCalendarRange(StockSplitInfo splitInfo,
			CalendarRange calendarRange) {

		if (splitInfo == null) {
			return 0;
		}
		if (splitInfo.isExplicitlyNoSplit()) {
			return 0; // Completely no problem !!!
		}

		List<StockSplit> stockSplitList = splitInfo.getStockSplitList();
		if (stockSplitList == null || stockSplitList.size() <= 0) {
			// TODO: suspicious situation
			//
			// System.out
			// .println("Warning: No split info. Split info file may be old. Continue process assuming that there was no split for this stock. Stock Code="
			// + code);
			return 0;
		}

		int splitCount = 0;
		Calendar begin = calendarRange.getBegin();
		Calendar end = calendarRange.getEnd();
		for (StockSplit stockSplit : stockSplitList) {
			Calendar splitDay = stockSplit.getSplitDay();
			if (splitDay.compareTo(begin) >= 0 && splitDay.compareTo(end) <= 0) {
				++splitCount;
			}
		}
		return splitCount;
	}

	//
	// Detail Info
	//

	private Map<String, DetailRecord> listToMapForDetailRecord(
			List<DetailRecord> list) {
		Map<String, DetailRecord> map = new HashMap<String, DetailRecord>();
		for (DetailRecord record : list) {
			String stockCode = (String) record.get(DetailEnum.STOCK_CODE);
			if (stockCode != null) {
				// String splitSearchStockCode =
				// toSplitSearchStockCode(stockCode);
				if (map.get(stockCode) == null) {
					map.put(stockCode, record);
				}
			}
		}
		return map;
	}

	public void generateStockCodeToDetailRecordMap() throws IOException,
			InvalidDataException {
		List<String> lines = readLocalDetailInfoText();
		DetailInfoTextAnalyzer analyzer = new DetailInfoTextAnalyzer();
		analyzer.analyze(lines);
		List<DetailRecord> list = analyzer.getDetailRecordList();
		Map<String, DetailRecord> map = listToMapForDetailRecord(list);
		this.stockCodeToDetailRecordMap = map;
	}

	//
	// Profile Info
	//

	private Map<String, ProfileRecord> listToMapForProfileRecord(
			List<ProfileRecord> recordList) {
		Map<String, ProfileRecord> map = new HashMap<String, ProfileRecord>();
		for (ProfileRecord record : recordList) {
			String stockCode = (String) record.get(ProfileEnum.STOCK_CODE);
			if (stockCode != null) {
				// String splitSearchStockCode =
				// toSplitSearchStockCode(stockCode);
				if (map.get(stockCode) == null) {
					map.put(stockCode, record);
				}
			}
		}
		return map;
	}

	public void generateStockCodeToProfileRecordMap() throws IOException,
			InvalidDataException {
		List<String> lines = readLocalProfileInfoText();
		ProfileInfoTextAnalyzer analyzer = new ProfileInfoTextAnalyzer();
		analyzer.analyze(lines);
		List<ProfileRecord> list = analyzer.getProfileRecordList();
		Map<String, ProfileRecord> map = listToMapForProfileRecord(list);
		this.stockCodeToProfileRecordMap = map;
	}

	//
	// Getters
	//

	public Map<String, StockSplitInfo> getStockCodeToSplitInfoMap() {
		return this.stockCodeToSplitInfoMap;
	}

	public Map<String, DetailRecord> getStockCodeToDetailRecordMap() {
		return this.stockCodeToDetailRecordMap;
	}

	public Map<String, ProfileRecord> getStockCodeToProfileRecordMap() {
		return this.stockCodeToProfileRecordMap;
	}
}
