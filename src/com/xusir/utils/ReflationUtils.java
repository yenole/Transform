package com.xusir.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ReflationUtils {

	private static boolean isDecalred = false;

	/**
	 * 通过类名获取�?
	 * 
	 * @param 类名
	 * @return �?
	 */
	public static Class<?> findClass(String clazz) {
		if (!Assert.notEmpty(clazz))
			return null;
		try {
			return Class.forName(clazz);
		} catch (ClassNotFoundException e) {
			Log4jUtils.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 设计声明模式
	 * 
	 * @param isEnabled
	 */
	public static void decalred(boolean isEnabled) {
		isDecalred = isEnabled;
	}

	/**
	 * 获取构�?方法
	 * 
	 * @param clazz
	 * @return
	 */
	public static Constructor<?>[] getConstructors(Class<?> clazz) {
		if (Assert.isNull(clazz))
			return null;
		return isDecalred ? clazz.getDeclaredConstructors() : clazz.getConstructors();
	}

	/**
	 * 获取构�?方法
	 * 
	 * @param clazz
	 * @param parameterTypes
	 * @return
	 */
	public static <T> Constructor<T> getConstructor(Class<T> clazz, Class<?>... parameterTypes) {
		if (Assert.isNull(clazz))
			return null;
		try {
			return isDecalred ? clazz.getDeclaredConstructor(parameterTypes) : clazz.getConstructor(parameterTypes);
		} catch (Exception e) {
			Log4jUtils.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 获取实例
	 * 
	 * @param constructor
	 * @param initargs
	 * @return
	 */
	public static <T> T newInstance(Class<T> clazz, Object... args) {
		if (null == clazz)
			return null;
		if (null == args || args.length == 0)
			return newInstance(clazz);
		List<Class<?>> list = new LinkedList<Class<?>>();
		for (Object object : args) {
			list.add(object.getClass());
		}
		Class<?>[] parameterTypes = new Class<?>[list.size()];
		list.toArray(parameterTypes);
		Constructor<T> constructor = ReflationUtils.getConstructor(clazz, parameterTypes);
		return ReflationUtils.newInstance(constructor, args);
	}

	public static <T> T newInstance(Constructor<T> constructor, Object... args) {
		if (null == constructor || constructor.getParameterTypes().length != args.length)
			return null;
		try {
			return constructor.newInstance(args);
		} catch (Exception e) {
			Log4jUtils.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 获取实例
	 * 
	 * @param clazz
	 * @return
	 */
	public static <T> T newInstance(Class<T> clazz) {
		if (Assert.isNull(clazz))
			return null;
		try {
			return clazz.newInstance();
		} catch (Exception e) {
			Log4jUtils.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 获取�?��属�?
	 * 
	 * @param �
	 *            ?
	 * @return 属�?集合
	 */
	public static Field[] getFields(Class<?> clazz) {
		if (Assert.isNull(clazz))
			return null;
		return isDecalred ? clazz.getDeclaredFields() : clazz.getFields();
	}

	/**
	 * 获取属�?
	 * 
	 * @param �
	 *            ?
	 * @param 属�
	 *            ?�?
	 * @return 属�?
	 */
	public static Field getField(Class<?> clazz, String name) {
		if (Assert.isNull(clazz) || !Assert.notEmpty(name))
			return null;
		try {
			return isDecalred ? clazz.getDeclaredField(name) : clazz.getField(name);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取�?��方法
	 * 
	 * @param �
	 *            ?
	 * @return 方法集合
	 */
	public static Method[] getMethods(Class<?> clazz) {
		if (Assert.isNull(clazz))
			return null;
		return isDecalred ? clazz.getDeclaredMethods() : clazz.getMethods();
	}

	/**
	 * 获取制定名的方法
	 * 
	 * @param clazz
	 * @param name
	 * @return
	 */
	public static Method[] getMethods(Class<?> clazz, String regexName) {
		if (Assert.isNull(clazz, regexName) || Assert.isEmpty(regexName))
			return null;
		Method[] methods = getMethods(clazz);
		if (null == methods || methods.length == 0)
			return null;
		List<Method> list = new ArrayList<Method>();
		for (Method method : methods) {
			if (RegexUtils.isMatch(regexName, method.getName()))
				list.add(method);
		}
		methods = new Method[list.size()];
		list.toArray(methods);
		return methods;
	}

	/**
	 * 获取方法
	 * 
	 * @param �
	 *            ?
	 * @param 方法�
	 *            ?
	 * @param 方法参数集合
	 * @return 方法
	 */
	public static Method getMethod(Class<?> clazz, String name, Class<?>... clazzs) {
		if (Assert.isNull(clazz) || !Assert.notEmpty(name))
			return null;
		try {
			return isDecalred ? clazz.getDeclaredMethod(name, clazzs) : clazz.getMethod(name, clazzs);
		} catch (Exception e) {
			Log4jUtils.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 调用方法
	 * 
	 * @param 实例
	 *            静�?方法:null
	 * @param 方法
	 * @param 参数
	 * @return 结果
	 */
	public static Object invoke(Object target, Method method, Object... args) {
		if (Assert.isNull(method))
			return null;
		try {
			return method.invoke(target, args);
		} catch (Exception e) {
			Log4jUtils.error(e.getMessage());
		}
		return null;
	}

	public static Object invoke(Object target, String name, Object... args) {
		if (Assert.isNull(target, name) || Assert.isEmpty(name) || args.length % 2 == 1)
			return null;
		int len = args.length / 2;
		Class<?>[] parameterTypes = new Class<?>[len];
		Object[] parameterObject = new Object[len];
		for (int i = 0; i < args.length; i++) {
			if (i < len)
				parameterTypes[i] = Class.class.cast(args[i]);
			else
				parameterObject[i - len] = args[i];
		}
		Method method = getMethod(target.getClass(), name, parameterTypes);
		if (null != method)
			return ReflationUtils.invoke(target, method, parameterObject);
		return null;
	}

	/**
	 * 获取泛型
	 * 
	 * @param clazz
	 * @param idx
	 * @return
	 */
	public static Class<?> getGenericType(Class<?> clazz, int idx) {
		if (null == clazz)
			return null;
		Type genericType = clazz.getGenericSuperclass();
		if (genericType instanceof ParameterizedType) {
			ParameterizedType type = (ParameterizedType) genericType;
			int count = type.getActualTypeArguments().length;
			if (idx < count)
				return type.getActualTypeArguments()[idx].getClass();
		}
		return null;

	}
}
