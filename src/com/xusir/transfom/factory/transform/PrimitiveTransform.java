package com.xusir.transfom.factory.transform;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.xusir.transfom.factory.TransformFactory;
import com.xusir.utils.Assert;
import com.xusir.utils.ReflationUtils;

public class PrimitiveTransform extends TransformHande {

	public boolean isCanTransform(java.lang.Class<?> valueClazz, java.lang.Class<?> targetClazz) {
		return TransformFactory.isPrimitive(targetClazz);
	}

	@Override
	public Object transform(Object value, Class<?> clazz) {
		if (Assert.isNull(value, clazz))
			return null;
		if (Object.class == clazz)
			return value;
		clazz = turnClass(clazz);
		if (String.class.isAssignableFrom(clazz))
			return toJson(value);
		if (!TransformFactory.isPrimitive(value.getClass()))
			return null;
		value = turnValue(value, clazz);
		if (Character.class.isAssignableFrom(clazz)) {
			Constructor<?> constructor = ReflationUtils.getConstructor(clazz, char.class);
			return ReflationUtils.newInstance(constructor, value);
		}
		return ReflationUtils.newInstance(clazz, value);
	}

	protected Class<?> turnClass(Class<?> clazz) {
		Matcher matcher = Pattern.compile("(int|long|float|short|double|byte|char|boolean)").matcher(clazz.getName());
		if (matcher.find()) {
			StringBuilder buffer = new StringBuilder(matcher.group());
			if (buffer.indexOf("int") > -1)
				buffer.append("eger");
			if (buffer.indexOf("char") > -1)
				buffer.append("acter");
			buffer.setCharAt(0, Character.toUpperCase(buffer.charAt(0)));
			buffer.insert(0, "java.lang.");
			clazz = ReflationUtils.findClass(buffer.toString());
		}
		return clazz;
	}

	protected Object turnValue(Object object, Class<?> clazz) {
		String clazzName = clazz.getName();
		String value = object.toString();
		if (Pattern.matches("(java\\.lang\\.)(Integer|Long)", clazzName)) {
			Matcher matcher = Pattern.compile("^([0-9]+)").matcher(value);
			if (matcher.find())
				return matcher.groupCount() > 0 ? matcher.group(0) : "0";
			return Pattern.matches("true", value) ? "1" : "0";
		} else if (Pattern.matches("(java\\.lang\\.)(Short|Float|Double)", clazzName)) {
			Matcher matcher = Pattern.compile("^([0-9\\.]+)").matcher(value);
			if (matcher.find())
				return matcher.groupCount() > 0 ? matcher.group(0) : "0";
			return Pattern.matches("true", value) ? "1" : "0";
		} else if (Pattern.matches("(java\\.lang\\.)(Byte|Character)", clazzName)) {
			if (object instanceof Boolean)
				value = Pattern.matches("true", value) ? "1" : "0";
			value = Character.toString(value.charAt(0));
			return clazz == Byte.class ? Pattern.matches("[0-9]", value) ? value : "0" : value;
		}
		return value;
	}

	protected String toJson(Object value) {
		if (null == value)
			return "null";
		Class<?> clazz = value.getClass();
		if (TransformFactory.isPrimitive(clazz)) {
			return (String.class.isAssignableFrom(clazz) || Character.class.isAssignableFrom(clazz)) ? String.format("\"%s\"", value) : value.toString();
		} else if (clazz.isArray())
			return arrayToJson(value);
		else if (Collection.class.isAssignableFrom(clazz))
			return collectionToJson(value);
		else if (Map.class.isAssignableFrom(clazz))
			return MapToJson(value);
		else
			return pojoToJson(value);
	}

	protected String pojoToJson(Object value) {
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		Method[] methods = ReflationUtils.getMethods(value.getClass());
		for (Method method : methods) {
			String name = method.getName();
			if (!name.startsWith("get") || name.equals("getClass"))
				continue;
			builder.append(String.format("\"%s%s\":", Character.toUpperCase(name.charAt(3)), name.substring(4)));
			Object temp = ReflationUtils.invoke(value, method);
			builder.append(toJson(temp));
			builder.append(",");
		}
		if (builder.charAt(builder.length() - 1) == ',')
			builder.delete(builder.length() - 1, builder.length());
		builder.append("}");
		return builder.toString();
	}

	protected String MapToJson(Object value) {
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		Map<?, ?> map = Map.class.cast(value);
		int len = map.size(), i = 0;
		for (Entry<?, ?> entry : map.entrySet()) {
			Object key = entry.getKey();
			if (!TransformFactory.isPrimitive(key.getClass()))
				continue;
			String temp = toJson(key);
			builder.append(String.format("%s%s%s:", temp.startsWith("\"") ? "" : "\"", temp, temp.startsWith("\"") ? "" : "\""));
			builder.append(toJson(entry.getValue()));
			builder.append(i++ < len - 1 ? "," : "");
		}
		builder.append("}");
		return builder.toString();
	}

	protected String collectionToJson(Object value) {
		Collection<?> list = Collection.class.cast(value);
		return arrayToJson(list.toArray());
	}

	protected String arrayToJson(Object value) {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		int len = Array.getLength(value);
		for (int i = 0; i < len; i++) {
			builder.append(toJson(Array.get(value, i)));
			builder.append(i < len - 1 ? "," : "");
		}
		builder.append("]");
		return builder.toString();
	}

}
