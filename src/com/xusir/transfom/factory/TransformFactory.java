package com.xusir.transfom.factory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Pattern;

import com.xusir.transfom.factory.transform.MapTransform;
import com.xusir.transfom.factory.transform.PojoTransform;
import com.xusir.transfom.factory.transform.PrimitiveTransform;
import com.xusir.transfom.factory.transform.SequenceTransform;
import com.xusir.transfom.factory.transform.TransformHande;
import com.xusir.utils.Assert;
import com.xusir.utils.ReflationUtils;

public class TransformFactory {

	private static TransformFactory defaultFactory;
	private LinkedList<TransformHande> transformList;

	private TransformFactory() {
		transformList = new LinkedList<TransformHande>();
		this.transformList.addLast(new PojoTransform().setFactory(this));
		this.transformList.addLast(new MapTransform().setFactory(this));
		this.transformList.addLast(new SequenceTransform().setFactory(this));
		this.transformList.addLast(new PrimitiveTransform().setFactory(this));
	}

	public static TransformFactory defaultFactory() {
		return null == defaultFactory ? (defaultFactory = new TransformFactory()) : defaultFactory;
	}

	public static TransformFactory newFactory(TransformHande... handes) {
		TransformFactory factory = new TransformFactory();
		for (TransformHande hande : handes) {
			factory.transformList.addFirst(hande.setFactory(factory));
		}
		return factory;
	}

	public Object transform(Object value, Class<?> clazz) {
		if (null == value || clazz == null || clazz.isInterface())
			return null;
		if (value instanceof String && (isJsonMap((String) value) || isJsonSequence((String) value))) {
			value = parseJSON((String) value);
		}
		for (TransformHande transformHande : transformList) {
			if (transformHande.isCanTransform(value.getClass(), clazz))
				return transformHande.transform(value, clazz);
		}
		return null;
	}

	public static boolean isPrimitive(Class<?> clazz) {
		return null != clazz && (Number.class.isAssignableFrom(clazz) || Pattern.matches("^(java\\.lang\\.)?([ilfsSdbBcC](nt|ong|loat|hort|ouble|oolean|yte|har(acter)?|tring))$", clazz.getName()));
	}

	public static boolean isSequence(Class<?> clazz) {
		return null != clazz && !clazz.isInterface() && (clazz.isArray() || Collection.class.isAssignableFrom(clazz));
	}

	public static boolean isMap(Class<?> clazz) {
		return null != clazz && !clazz.isInterface() && Map.class.isAssignableFrom(clazz);
	}

	public static boolean isPojo(Class<?> clazz) {
		return !isPrimitive(clazz) && !isSequence(clazz) && !isMap(clazz);
	}

	public static Object parseJSON(String text) {
		text.trim();
		Stack<String> stack = new Stack<String>();
		Stack<Object> objects = new Stack<Object>();
		Stack<String> keys = new Stack<String>();
		Stack<Boolean> types = new Stack<Boolean>();
		int idx = 0;
		int pos = 0;
		Object obj = null;
		do {
			switch (text.charAt(idx++)) {
			case '[':
			case '{':
				boolean isMap = text.charAt(idx - 1) == '{';
				if (stack.size() != 0 && stack.lastElement().equals("\"")) {
					break;
				}
				stack.push("}");
				types.push(isMap);
				if (null != obj)
					objects.push(obj);
				obj = ReflationUtils.newInstance(isMap ? HashMap.class : ArrayList.class);
				pos = idx;
				break;
			case ']':
			case '}':
				if (pos != idx) {
					String value = text.substring(pos, idx - (text.charAt(idx - 2) == '"' ? 2 : 1)).trim();
					if (obj instanceof HashMap) {
						ReflationUtils.invoke(obj, "put", Object.class, Object.class, keys.pop(), value);
					} else {
						ReflationUtils.invoke(obj, "add", Object.class, value);
					}
				}
				types.pop();
				stack.pop();
				if (objects.size() > 0) {
					if (objects.lastElement() instanceof HashMap) {
						ReflationUtils.invoke(objects.lastElement(), "put", Object.class, Object.class, keys.pop(), obj);
					} else {
						ReflationUtils.invoke(objects.lastElement(), "add", Object.class, obj);
					}
					obj = objects.pop();
				}
				pos = idx + 1;
				break;
			case '"':
				if (stack.lastElement().equals("\"")) {
					stack.pop();
				} else {
					stack.push("\"");
					pos = idx;
				}
				break;
			case ',':
				if (pos != idx) {
					String value = text.substring(pos, idx - (text.charAt(idx - 2) == '"' ? 2 : 1)).trim();
					if (types.lastElement()) {
						ReflationUtils.invoke(obj, "put", Object.class, Object.class, keys.pop(), value);
					} else {
						ReflationUtils.invoke(obj, "add", Object.class, value);
					}
				}
				pos = idx;
				break;
			case ':':
				keys.push(text.substring(pos, (pos = idx) - 2));
				break;
			}
		} while (idx < text.length());
		return obj;
	}

	public static boolean isJsonSequence(String value) {
		if (Assert.notEmpty(value)) {
			return Pattern.matches("^\\[(.*?)\\]$", value.trim());
		}
		return false;
	}

	public static boolean isJsonMap(String value) {
		if (Assert.notEmpty(value)) {
			return Pattern.matches("^\\{(.*?)\\}$", value.trim());
		}
		return false;
	}
}
