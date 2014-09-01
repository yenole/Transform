package com.junit;

import org.junit.Test;

import com.xusir.transfom.factory.TransformFactory;

public class PrimitiveJunit {

	@Test
	public void t1() {
		System.out.println(TransformFactory.defaultFactory().transform(10, int.class));
		System.out.println(TransformFactory.defaultFactory().transform(10.78, int.class));
		System.out.println(TransformFactory.defaultFactory().transform("string", int.class));
		System.out.println(TransformFactory.defaultFactory().transform("12.577jdfs", int.class));
		System.out.println(TransformFactory.defaultFactory().transform(true, int.class));
		System.out.println();
		System.out.println(TransformFactory.defaultFactory().transform(10, float.class));
		System.out.println(TransformFactory.defaultFactory().transform(10.78, float.class));
		System.out.println(TransformFactory.defaultFactory().transform("string", float.class));
		System.out.println(TransformFactory.defaultFactory().transform("12.577jdfs", float.class));
		System.out.println(TransformFactory.defaultFactory().transform(true, float.class));
	}
}
