package jp.gr.java_conf.islandocean.stockanalysis.finance;

import java.util.Arrays;
import java.util.Comparator;

import javafx.scene.control.TreeItem;
import jp.gr.java_conf.islandocean.stockanalysis.app.ui.SectorItemValue;

public class SectorUtil {

	private SectorUtil() {
	}

	private static final String[] ALL_SECTORS = {

			//
			"水産・農林業",

			//
			"鉱業",

			//
			"建設業",

			//
			"食料品",

			//
			"繊維製品",

			//
			"パルプ・紙",

			//
			"化学",

			//
			"医薬品",

			//
			"石油石炭製品",

			//
			"ゴム製品",

			//
			"ガラス土石製品",

			//
			"鉄鋼",

			//
			"非鉄金属",

			//
			"金属製品",

			//
			"機械",

			//
			"電気機器",

			//
			"輸送用機器",

			//
			"精密機器",

			//
			"その他製品",

			//
			"電気・ガス業",

			//
			"陸運業",

			//
			"海運業",

			//
			"空運業",

			//
			"倉庫運輸関連",

			//
			"情報・通信業",

			//
			"卸売業",

			//
			"小売業",

			//
			"銀行業",

			//
			"証券商品先物",

			//
			"保険業",

			//
			"その他金融業",

			//
			"不動産業",

			//
			"サービス業",

			//
			"その他",

	//
	};

	public static Comparator<TreeItem> sectorTreeComparator() {
		return new Comparator<TreeItem>() {
			@Override
			public int compare(TreeItem t0, TreeItem t1) {
				String s0 = ((SectorItemValue) t0.getValue()).getName();
				String s1 = ((SectorItemValue) t1.getValue()).getName();
				int idx0 = idxOfSector(s0);
				int idx1 = idxOfSector(s1);
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

	public static Comparator<String> sectorComparator() {
		return new Comparator<String>() {
			@Override
			public int compare(String s0, String s1) {
				int idx0 = idxOfSector(s0);
				int idx1 = idxOfSector(s1);
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

	private static int idxOfSector(String sector) {
		for (int idx = 0; idx < ALL_SECTORS.length; ++idx) {
			if (ALL_SECTORS[idx].equals(sector)) {
				return idx;
			}
		}
		return -1;
	}

	public static void main(String[] args) {
		String[] array = { "b", "鉱業", "4", "保険業", "a", "その他", "3", "水産・農林業" };
		Arrays.sort(array, sectorComparator());
		for (String string : array) {
			System.out.println(string);
		}
	}
}
