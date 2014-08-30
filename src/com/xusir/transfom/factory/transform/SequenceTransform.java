package com.xusir.transfom.factory.transform;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.xusir.transfom.factory.TransformFactory;
import com.xusir.utils.Assert;
import com.xusir.utils.ReflationUtils;

public class SequenceTransform extends TransformHande {

	@Override
	public boolean isCanTransform(Class<?> valueClazz, Class<?> targetClazz) {
		return TransformFactory.isSequence(targetClazz);
	}

	@Override
	public Object transform(Object value, Class<?> clazz) {
		if (Assert.isNull(value, clazz, factory))
			return null;
		if (Object.class == clazz)
			return value;
		else if (clazz.isArray())
			return toArray(value, clazz);
		else if (Collection.class.isAssignableFrom(clazz))
			return toCollection(value, clazz);
		return null;
	}

	protected Object toCollection(Object value, Class<?> clazz) {
		Object seq = ReflationUtils.newInstance(clazz);
		if (TransformFactory.isPrimitive(value.getClass())) {
			ReflationUtils.invoke(seq, "add", Object.class, value);
			return seq;
		}
		if (clazz.isAssignableFrom(value.getClass()))
			return value;
		else if (value instanceof Collection)
			value = Collection.class.cast(value).toArray();
		int len = Array.getLength(value);
		for (int i = 0; i < len; i++) {
			ReflationUtils.invoke(seq, "add", Object.class, Array.get(value, i));
		}
		return seq;
	}

	protected Object toArray(Object value, Class<?> clazz) {
		Class<?> primitiveClazz = getPrimitiveClass(clazz);
		Object seq = null;
		if (TransformFactory.isPrimitive(value.getClass())) {
			seq = Array.newInstance(primitiveClazz, 1);
			Array.set(seq, 0, this.factory.transform(value, primitiveClazz));
		} else if (TransformFactory.isSequence(value.getClass())) {
			if (value instanceof Collection)
				value = Collection.class.cast(value).toArray();
			int len = Array.getLength(value);
			seq = Array.newInstance(primitiveClazz, len);
			for (int i = 0; i < len; i++) {
				Object temp = Array.get(value, i);
				Array.set(seq, i, this.factory.transform(temp, primitiveClazz));
			}
		}
		return seq;
	}

	protected Class<?> getPrimitiveClass(Class<?> clazz) {
		String className = clazz.getCanonicalName().replace("[]", "");
		Matcher matcher = Pattern.compile("(int|long|float|short|double|byte|char|boolean)").matcher(className);
		if (matcher.find()) {
			StringBuilder buffer = new StringBuilder(matcher.group());
			if (buffer.indexOf("int") > -1)
				buffer.append("eger");
			if (buffer.indexOf("char") > -1)
				buffer.append("acter");
			buffer.setCharAt(0, Character.toUpperCase(buffer.charAt(0)));
			buffer.insert(0, "java.lang.");
			className = buffer.toString();
		}
		return ReflationUtils.findClass(className);
	}

}
