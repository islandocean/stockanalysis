package jp.gr.java_conf.islandocean.stockanalysis.finance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainDumpLocalSplitInfo {

	public MainDumpLocalSplitInfo() {
		super();
	}

	public static void main(String[] args) throws IOException {
		FinanceManager financeManager = FinanceManager.getInstance();
		List<String> lines = financeManager.readLocalSplitInfoText();
		SplitInfoTextAnalyzer analyzer = new SplitInfoTextAnalyzer();
		analyzer.analyze(lines);
		List<StockSplitInfo> stockSplitInfoList = analyzer
				.getStockSplitInfoList();

		List<StockSplitInfo> focusList = new ArrayList<StockSplitInfo>();
		int maxSplit = 0;
		for (StockSplitInfo stockSplitInfo : stockSplitInfoList) {
			List<StockSplit> stockSplitList = stockSplitInfo
					.getStockSplitList();
			if (stockSplitList != null) {
				int size = stockSplitList.size();
				if (maxSplit < size) {
					maxSplit = size;
				}
				if (size >= 10) {
					focusList.add(stockSplitInfo);
				}

			}
		}

		int[] numCorp = new int[maxSplit + 1];
		for (int i = 0; i < numCorp.length; ++i) {
			numCorp[i] = 0;
		}

		int countValid = 0;
		int countAll = 0;
		int countExplicitlyNoSplit = 0;
		for (StockSplitInfo stockSplitInfo : stockSplitInfoList) {
			++countAll;
			System.out.println("code="
					+ stockSplitInfo.getSplitSearchStockCode());
			List<StockSplit> stockSplitList = stockSplitInfo
					.getStockSplitList();

			if (stockSplitList != null) {
				int size = stockSplitList.size();
				++numCorp[size];
			} else {
				if (stockSplitInfo.isExplicitlyNoSplit()) {
					++numCorp[0];
				} else {
					// abnormal data
				}
			}

			if (stockSplitInfo.isExplicitlyNoSplit()) {
				++countValid;
				++countExplicitlyNoSplit;
				continue;
			}
			if (stockSplitList == null) {
				System.out.println("Warning: stockSplitList is null.");
				continue;
			}
			if (stockSplitList.size() == 0) {
				System.out.println("Warning: stockSplitList is empty.");
				continue;
			}
			for (StockSplit stockSplit : stockSplitList) {
				System.out.println(stockSplit);
			}
			++countValid;
		}
		System.out.println("----- summary -----");
		System.out.println("countAll=" + countAll);
		System.out.println("countValid=" + countValid);
		System.out.println("countExplicitlyNoSplit=" + countExplicitlyNoSplit);
		System.out.println("countAll - countValid =" + (countAll - countValid));
		System.out.println();

		for (int i = 0; i < maxSplit + 1; ++i) {
			System.out.println("SplitCount=" + i + " numCorp=" + numCorp[i]);
		}
		System.out.println("maxSplit=" + maxSplit);
		System.out.println();

		System.out.println("manytimes stock splitted corps");
		for (StockSplitInfo focusSplitInfo : focusList) {
			System.out.println(focusSplitInfo.getStockSplitList().size() + " "
					+ focusSplitInfo.getSplitSearchStockCode());
		}
	}
}
