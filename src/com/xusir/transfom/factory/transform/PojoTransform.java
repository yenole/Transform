package com.xusir.transfom.factory.transform;

import java.lang.reflect.Method;

import com.xusir.transfom.factory.TransformFactory;
import com.xusir.utils.Assert;
import com.xusir.utils.ReflationUtils;

public class PojoTransform extends TransformHande {

	public boolean isCanTransform(java.lang.Class<?> valueClazz, java.lang.Class<?> targetClazz) {
		return TransformFactory.isPojo(targetClazz);
	}

	@Override
	public Object transform(Object value, Class<?> clazz) {
		if (Assert.isNull(value, clazz) || clazz.isInterface())
			return null;
		if (Object.class == clazz)
			return value;
		else if (TransformFactory.isPojo(clazz)) {
			Object pojo = ReflationUtils.newInstance(clazz);
			Method[] methods = ReflationUtils.getMethods(clazz, "set.*+");
			if (Assert.isNull(methods, pojo) || methods.length == 0)
				return null;
			for (Method method : methods) {
				if (method.getParameterTypes().length != 1)
					continue;
				Object result = null;
				if (TransformFactory.isPojo(value.getClass())) {
					result = ReflationUtils.invoke(value, method.getName().replace("set", "get"));
				} else if (TransformFactory.isMap(value.getClass())) {
					String name = method.getName().replace("set", "");
					name = String.format("%s%s", Character.toLowerCase(name.charAt(0)), name.substring(1));
					result = ReflationUtils.invoke(value, "get", Object.class, name);
				}
				if (null == result)
					continue;
				result = this.factory.transform(result, method.getParameterTypes()[0]);
				Class<?> targetClazz = result.getClass();
				if (targetClazz == String.class || targetClazz == Character.class)
					result = ((String) result).substring(1, ((String) result).length() - 1);
				ReflationUtils.invoke(pojo, method, result);
			}
			return pojo;
		}
		return null;
	}
}
