package jp.gr.java_conf.islandocean.stockanalysis.common;

import java.util.HashMap;

public class ValueClassHolder {

	private static ValueClassHolder instance = new ValueClassHolder();
	private HashMap map = new HashMap();

	private ValueClassHolder() {
		super();
	}

	public static ValueClassHolder getInstance() {
		return instance;
	}

	public void registValueClass(Class enumClass, Class[] valueClasses) {
		map.put(enumClass, valueClasses);
	}

	public Class[] referValueClass(Class enumClass) {
		return (Class[]) map.get(enumClass);
	}
}
