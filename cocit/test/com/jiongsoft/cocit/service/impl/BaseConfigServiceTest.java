package com.jiongsoft.cocit.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import mockit.Expectations;
import mockit.Mocked;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jiongsoft.cocit.service.impl.BaseConfigService;
import com.jiongsoft.cocit.util.DateUtil;
import com.jiongsoft.cocit.util.StringUtil;
import com.jiongsoft.cocit.utils.TestBean;

public class BaseConfigServiceTest {

	@Mocked
	BaseConfigService assist;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void get() {

		new Expectations(assist) {
			{
				assist.getStr("str");
				result = "str";
				assist.getStr("int");
				result = "1";
				assist.getStr("double");
				result = "1.0";
				assist.getStr("boolean");
				result = "true";
				assist.getStr("null");
				result = null;
				assist.getStr("defaultKey");
				result = null;
				assist.getStr("long");
				result = "2";
				assist.getStr("long");
				result = "2";
				assist.getStr("byte");
				result = "3";
				assist.getStr("short");
				result = "3";
				assist.getStr("float");
				result = "3.5";
				assist.getStr("date");
				result = "2013-08-28";
				assist.getStr("class");
				result = StringUtil.class.getName();
				assist.getStr("defaultReturnIsNull");
				result = "string";
				assist.getStr("testcase");
				result = "prop1";
				assist.getStr("errorInt");
				result = "2.3l";
			}
		};

		String str = assist.get("str", "");
		assertEquals("str", str);
		assertEquals(1, (int) assist.get("int", 0));
		Double d = assist.get("double", 0.0);
		assertEquals(new Double("1.0"), d);
		assertTrue(assist.get("boolean", false));
		assertNull(assist.get("null", null));
		assertEquals("defaultValue", assist.get("defaultKey", "defaultValue"));
		assertEquals(2l, (long) assist.get("long", 0l));
		assertEquals(Long.valueOf(2), (Long) assist.get("long", 0l));
		assertEquals((byte) 3, (byte) assist.get("byte", (byte) 0));
		assertEquals((short) 3, (short) assist.get("short", (short) 0));
		float f = assist.get("float", 0.0f);
		assertTrue(3.5f == f);
		Date date = assist.get("date", new Date());
		assertEquals("2013/08/28", DateUtil.format(date, "yyyy/MM/dd"));
		Object obj = assist.get("defaultReturnIsNull", null);
		assertEquals("string", obj);
		TestBean case1 = (TestBean) assist.get("testcase", new TestBean());
		assertNotNull(case1);
		assertEquals(9999, (int) assist.get("errorInt", 9999));
	}
}