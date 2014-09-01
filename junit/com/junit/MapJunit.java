package com.junit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.junit.Test;

import com.xusir.transfom.factory.TransformFactory;

public class MapJunit {
	@Test
	public void j1() {
		// array to map
		System.out.println(TransformFactory.defaultFactory().transform(new int[] { 0, 2, 3, 4, 4, 5, 6 }, HashMap.class));
		// collection to map
		List<Object> list = new ArrayList<Object>();
		list.add(0);
		list.add("1");
		list.add(2L);
		list.add(true);
		list.add(new ArrayList<String>());
		list.add(new HashMap<String, String>());
		System.out.println(TransformFactory.defaultFactory().transform(list, HashMap.class));
		// pojo to map
		A a = new A();
		a.setAge(10);
		a.setName("ITspas");
		System.out.println(TransformFactory.defaultFactory().transform(a, LinkedHashMap.class));
		// json to map
		String json = "{\"name\":\"ITspas\",\"age\":10,\"is_admin\":true,\"addr\":[\"addr1\",\"addr2\",10]}";
		System.out.println(TransformFactory.defaultFactory().transform(json, HashMap.class));
	}
}
