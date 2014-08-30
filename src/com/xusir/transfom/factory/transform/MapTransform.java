package com.xusir.transfom.factory.transform;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;

import com.xusir.transfom.factory.TransformFactory;
import com.xusir.utils.Assert;
import com.xusir.utils.ReflationUtils;

public class MapTransform extends TransformHande {

	@Override
	public boolean isCanTransform(Class<?> valueClazz, Class<?> targetClazz) {
		return TransformFactory.isMap(targetClazz);
	}

	@Override
	public Object transform(Object value, Class<?> clazz) {
		if (Assert.isNull(value, clazz))
			return null;
		if (Object.class == clazz)
			return value;
		else if (TransformFactory.isMap(clazz)) {
			if (TransformFactory.isMap(value.getClass())) {
				if (clazz.isAssignableFrom(value.getClass()))
					return value;
				Map<?, ?> valueMap = Map.class.cast(value);
				if (valueMap.isEmpty())
					return null;
				Map<?, ?> map = Map.class.cast(ReflationUtils.newInstance(clazz));
				if (null == map)
					return null;
				for (Entry<?, ?> entry : valueMap.entrySet()) {
					ReflationUtils.invoke(map, "put", Object.class, Object.class, entry.getKey(), entry.getValue());
				}
				return map;
			} else if (TransformFactory.isPojo(value.getClass())) {
				Object map = ReflationUtils.newInstance(clazz);
				if (map == null)
					return null;
				Method[] methods = ReflationUtils.getMethods(value.getClass());
				for (Method method : methods) {
					String name = method.getName();
					if (!name.startsWith("get") || name.equals("getClass"))
						continue;
					String key = String.format("%s%s", Character.toUpperCase(name.charAt(3)), name.substring(4));
					Object temp = ReflationUtils.invoke(value, method);
					ReflationUtils.invoke(map, "put", Object.class, Object.class, key, temp);
				}
				return map;
			}
		}
		return null;
	}
}
