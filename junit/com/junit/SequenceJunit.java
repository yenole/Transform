package com.junit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.xusir.transfom.factory.TransformFactory;

public class SequenceJunit {

	@Test
	public void t1() {
		// collection to array
		List<Object> list = new ArrayList<Object>();
		list.add(0);
		list.add("1");
		list.add("2.3");
		list.add(4L);
		list.add(true);
		System.out.println(Arrays.toString((Integer[]) TransformFactory.defaultFactory().transform(list, int[].class)));

		// collection to collection
		System.out.println(TransformFactory.defaultFactory().transform(list, LinkedList.class));

		// array to collection
		System.out.println(TransformFactory.defaultFactory().transform(new Object[] { 0, "1", "2.3", 4L, true }, ArrayList.class));

		// json to collection
		String text = "[0,\"1\",\"2.3\",4.5,true,[1,2,3,4],{\"Name\":\"ITpsas\"}]";
		System.out.println(TransformFactory.defaultFactory().transform(text, ArrayList.class));
		
		// array to array
		System.out.println(Arrays.toString((Integer[]) TransformFactory.defaultFactory().transform(new Object[] { 0, "1", "2.3", 4L, true }, int[].class)));

		// json to array
		text = "[0,\"1\",\"2.3\",4.5,true]";
		System.out.println(Arrays.toString((Integer[]) TransformFactory.defaultFactory().transform(text, int[].class)));
	}
}
