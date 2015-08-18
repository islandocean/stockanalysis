package jp.gr.java_conf.islandocean.stockanalysis.finance;

import java.util.Arrays;
import java.util.Comparator;

import javafx.scene.control.TreeItem;
import jp.gr.java_conf.islandocean.stockanalysis.app.ui.MarketItemValue;

public class MarketUtil {

	private MarketUtil() {
	}

	private static String[] ALL_MARKETS = {

			//
			"東証1部",

			//
			"東証1部外国",

			//
			"東証",

			//
			"東証2部",

			//
			"東証2部外国",

			//
			"東証マザーズ",

			//
			"東証マザーズ外国",

			//
			"東証TPM",

			//
			"JQスタンダード",

			//
			"JQグロース",

			//
			"JQスタンダード外国",

			//
			"JQ",

			//
			"札証",

			//
			"札証アンビシャス",

			//
			"福証",

			//
			"福証Q-Board"

	//
	};

	public static Comparator<TreeItem> marketTreeComparator() {
		return new Comparator<TreeItem>() {
			@Override
			public int compare(TreeItem t0, TreeItem t1) {
				String s0 = ((MarketItemValue) t0.getValue()).getName();
				String s1 = ((MarketItemValue) t1.getValue()).getName();
				int idx0 = idxOfMarket(s0);
				int idx1 = idxOfMarket(s1);
				if (idx0 >= 0) {
					if (idx1 >= 0) {
						return idx0 - idx1;
					} else {
						return -1;
					}
				} else {
					if (idx1 >= 0) {
						return 1;
					} else {
						return s0.compareTo(s1);
					}
				}
			}
		};
	}

	public static Comparator<String> marketComparator() {
		return new Comparator<String>() {
			@Override
			public int compare(String s0, String s1) {
				int idx0 = idxOfMarket(s0);
				int idx1 = idxOfMarket(s1);
				if (idx0 >= 0) {
					if (idx1 >= 0) {
						return idx0 - idx1;
					} else {
						return -1;
					}
				} else {
					if (idx1 >= 0) {
						return 1;
					} else {
						return s0.compareTo(s1);
					}
				}
			}
		};
	}

	private static int idxOfMarket(String market) {
		for (int idx = 0; idx < ALL_MARKETS.length; ++idx) {
			if (ALL_MARKETS[idx].equals(market)) {
				return idx;
			}
		}
		return -1;
	}

	public static void main(String[] args) {
		String[] array = { "b", "JQスタンダード", "4", "福証", "a", "東証1部", "3", "東証" };
		Arrays.sort(array, marketComparator());
		for (String string : array) {
			System.out.println(string);
		}
	}
}
