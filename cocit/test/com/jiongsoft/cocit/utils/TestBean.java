package com.jiongsoft.cocit.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestBean {
	String prop1;
	String prop2;
	String prop3;

	public TestBean() {

	}

	public TestBean(String prop1) {
		this.prop1 = prop1;
	}

	public TestBean(String prop1, String prop2) {
		this.prop1 = prop1;
		this.prop2 = prop2;
	}

	public TestBean(String prop1, String prop2, String prop3) {
		this.prop1 = prop1;
		this.prop2 = prop2;
		this.prop3 = prop3;
	}

	public TestBean(HttpServletRequest req, HttpServletResponse res) {
	}

	public static class InnerClass {

	}

	public String getProp1() {
		return prop1;
	}

	public String getProp2() {
		return prop2;
	}

	public String getProp3() {
		return prop3;
	}
}
