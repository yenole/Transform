package com.junit;

import com.xusir.transfom.factory.TransformFactory;

public class A {
	private String name;
	private int age;

	public A() {
		// TODO Auto-generated constructor stub
	}

	public A(String name, int age) {
		this.name = name;
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return (String) TransformFactory.defaultFactory().transform(this, String.class);
	}

}