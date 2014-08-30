package com.xusir.transfom.factory.transform;

import com.xusir.transfom.factory.TransformFactory;

public abstract class TransformHande {
	protected TransformFactory factory;

	public TransformHande setFactory(TransformFactory factory) {
		this.factory = factory;
		return this;
	}

	public abstract boolean isCanTransform(Class<?> valueClazz,Class<?> targetClazz);

	public abstract Object transform(Object value, Class<?> clazz);
}