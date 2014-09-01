package com.junit;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.xusir.transfom.factory.TransformFactory;

public class PojoJunit {

	@Test
	public void j1() {
		// map to pojo
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("name", "ITspas");
		map.put("age", "10.8kll");
		System.out.println(TransformFactory.defaultFactory().transform(map, A.class));

		// pojo to pojo
		A a = new A();
		a.setAge(10);
		a.setName("ITspas");
		System.out.println(TransformFactory.defaultFactory().transform(a, B.class));

		// json to pojo
		String text = "{\"name\":\"ITspas\",\"age\":\"10.7ab\",\"a\":{\"name\":\"ITspas2\",\"age\":true}}";
		System.out.println(TransformFactory.defaultFactory().transform(text, B.class));
	}
}
