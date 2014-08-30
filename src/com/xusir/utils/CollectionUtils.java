package com.xusir.utils;

import java.util.Map;

public class CollectionUtils {
	public static int getLength(Object object) {
		if (null != object) {
			return (Integer) ReflationUtils.invoke(object, "size");
		}
		return 0;
	}

	public Class<?>[] getMap(Class<?> clazz) {
		return null;
	}

	public static void put(Object object, Object key, Object value) {
		if (null != object && object instanceof Map) {
			ReflationUtils.invoke(object, "put", Object.class, Object.class, key, value);
		}
	}

	public static Object get(Object object, Object key) {
		return null;
	}

	public static Object get(Object object, int index) {
		return null;
	}

	public static void add(Object object, Object value) {

	}

}
