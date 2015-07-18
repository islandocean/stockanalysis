package jp.gr.java_conf.islandocean.stockanalysis.enumex;

public final class EnumUtil {

	private EnumUtil() {
	}

	public static <E extends HasDataValueClass> Class getDataValueClass(
			Class<E> enumClass, int idx) {
		E[] keys = enumClass.getEnumConstants();
		E key = keys[idx];
		return key.getDataValueClass();
	}
}
