package com.xusir.utils;


import java.lang.reflect.Method;

/**
 * Log4j日记工具
 * @author 零零�?
 *
 */
public class Log4jUtils {

	/**
	 * 核心工具
	 * @param level
	 * @param messager
	 */
	private static void _log(String level, Object messager) {
		if (!Assert.notEmpty(level) || Assert.isNull(messager))
			return;
		Class<?> logClass = null;
		try {
			logClass = Class.forName("org.apache.log4j.Logger");
		} catch (ClassNotFoundException e) {
			return;
		}
		if (Assert.isNull(logClass))
			return;
		Method method = ReflationUtils.getMethod(logClass, "getLogger",
				String.class);
		if (Assert.isNull(method))
			return;
		Object instance = ReflationUtils.invoke(null, method,
				new Throwable().getStackTrace()[2].getClassName());
		if (Assert.isNull(instance))
			return;
		method = ReflationUtils.getMethod(logClass, level, Object.class);
		if (Assert.isNull(method))
			return;
		ReflationUtils.invoke(instance, method, messager);
	}

	/**
	 * Debug日记
	 * @param messager
	 */
	public static void debug(Object messager) {
		_log("debug", messager);
	}

	/**
	 * info日记
	 * @param messager
	 */
	public static void info(Object messager) {
		_log("info", messager);
	}

	/**
	 * warn日记
	 * @param messager
	 */
	public static void warn(Object messager) {
		_log("warn", messager);
	}

	/**
	 * error日记
	 * @param messager
	 */
	public static void error(Object messager) {
		_log("error", messager);
	}

	/**
	 * fatal日记
	 * @param messager
	 */
	public static void fatal(Object messager) {
		_log("fatal", messager);
	}
}
