package com.xusir.utils;


import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 断言判断
 * @author ITspas
 *
 */
public class Assert {

	/**
	 * 是否包含Null
	 * @param objects
	 * @return 
	 */
	public static boolean isNull(Object... objects) {
		if (objects == null)
			return true;
		for (Object object : objects) {
			if (object == null)
				return true;
		}
		return false;
	}

	/**
	 * 是否包含
	 * @param source
	 * @param target
	 * @return
	 */
	public static boolean isContain(String source, String content) {
		if (isNull(source, content))
			return false;
		return source.indexOf(content) > -1;
	}

	/**
	 * 是否是子类
	 * @param clazz
	 * @param object
	 * @return
	 */
	public static boolean isInstanceOf(Class<?> clazz, Object object) {
		if (isNull(clazz, object))
			return false;
		return clazz.isInstance(object);
	}

	/**
	 * 是否包含Key
	 * @param source
	 * @param keys
	 * @return
	 */
	public static <K,M extends Map<K, ?>> boolean isContainKey(M source, K... keys) {
		if (isNull(source, keys))
			return false;
		for (K key : keys) {
			if (!source.containsKey(key))
				return false;
		}
		return true;
	}
	
	/**
	 * 是否包含Value
	 * @param source
	 * @param values
	 * @return
	 */
	public static <V,M extends Map<?, V>> boolean isContainValue(M source, V... values) {
		if (isNull(source, values))
			return false;
		for (V value : values) {
			if (!source.containsValue(value))
				return false;
		}
		return true;
	}
	
	
	/**
	 * 是否包含
	 * @param source
	 * @param args
	 * @return
	 */
	public static <L extends List<?>> boolean isContain(L source,Object...args) {
		if (isNull(source, args))
			return false;
		for (Object object : args) {
			if(!source.contains(object))
				return false;
		}
		return true;
	}
	
	/**
	 * 是否包含
	 * @param source
	 * @param args
	 * @return
	 */
	public static <T> boolean isContain(T[] source,T...args) {
		if (isNull(source, args))
			return false;
		for (Object object : args) {
			if(Arrays.binarySearch(source, object) < 0)
				return false;
		}
		return true;
	}


	/**
	 * 是不是空
	 * @param object
	 * @return
	 */
	public static boolean isEmpty(Object object) {
		return !Assert.notEmpty(object);
	}

	

	/**
	 * 不是空
	 * @param object
	 * @return
	 */
	public static boolean notEmpty(Object object) {
		if (Assert.isNull(object))
			return false;
		else if (String.class.isInstance(object))
			return !((String) object).isEmpty();
		else if (object.getClass().isArray())
			return ((Object[]) object).length > 0;
		else if (object instanceof Collection<?>)
			return ((Collection<?>) object).size() > 0;
		else if (object instanceof Map<?, ?>)
			return ((Map<?, ?>) object).size() > 0;
		else
			return true;
	}

}
