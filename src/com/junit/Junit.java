package com.junit;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.xusir.transfom.factory.TransformFactory;

public class Junit {
	@Test
	public void j1() {
		String text1 = "{\"array\":[1,2,3],\"boolean\":true,\"null\":null,\"number\":123,\"object\":{\"a\":\"b\",\"c\":\"d\",\"e\":\"f\"},\"string\":\"Hello World\"}";
		String text2 = "{\"array\":[1,2,3],\"boolean\":true,\"null\":null,\"object\":{\"a\":\"b\",\"c\":\"d\",\"e\":\"f\"},\"string\":\"Hello World\"}";

		long time = System.currentTimeMillis();
		System.out.println(TransformFactory.parseJSON(text1));
		System.out.println(System.currentTimeMillis() - time);
		time = System.currentTimeMillis();
		System.out.println(JSON.parse(text1));
		System.out.println(System.currentTimeMillis() - time);

		
		
		time = System.currentTimeMillis();
		System.out.println(TransformFactory.parseJSON(text2));
		System.out.println(System.currentTimeMillis() - time);
		time = System.currentTimeMillis();
		System.out.println(JSON.parse(text2));
		System.out.println(System.currentTimeMillis() - time);

	}
}
