package com.junit;

import java.util.HashMap;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.xusir.transfom.factory.TransformFactory;

public class Junit {
	@Test
	public void j1() {

		System.out.println(TransformFactory.isPrimitive(int.class));

		System.out.println(TransformFactory.defaultFactory().transform(new String[] { "Tom", "fsfsd" }, HashMap.class));

		// String text1 =
		// "{\"array\":[1,2,3],\"boolean\":true,\"null\":null,\"number\":123,\"object\":{\"a\":\"b\",\"c\":\"d\",\"e\":\"f\"},\"string\":\"Hello World\"}";
		// String text2 =
		// "{\"array\":[1,2,3],\"boolean\":true,\"null\":null,\"object\":{\"a\":\"b\",\"c\":\"d\",\"e\":\"f\"},\"string\":\"Hello World\"}";
		//
		// long time = System.currentTimeMillis();
		// System.out.println(TransformFactory.parseJSON(text1));
		// System.out.println(System.currentTimeMillis() - time);
		// time = System.currentTimeMillis();
		// System.out.println(JSON.parse(text1));
		// System.out.println(System.currentTimeMillis() - time);
		//
		//
		//
		// time = System.currentTimeMillis();
		// System.out.println(TransformFactory.parseJSON(text2));
		// System.out.println(System.currentTimeMillis() - time);
		// time = System.currentTimeMillis();
		// System.out.println(JSON.parse(text2));
		// System.out.println(System.currentTimeMillis() - time);

	}
}
