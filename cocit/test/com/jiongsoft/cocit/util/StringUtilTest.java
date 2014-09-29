package com.jiongsoft.cocit.util;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class StringUtilTest {

	@Test
	public void testNID() {
		assertTrue(StringUtil.isNID("53212819791206631X"));
		assertTrue(StringUtil.isNID("53212819791206631x"));
		assertTrue(StringUtil.isNID("532128197912042924"));
		assertTrue(StringUtil.isNID("530112198808080316"));

		assertFalse(StringUtil.isNID("53212819791206634"));
		assertFalse(StringUtil.isNID("53212819791206631"));

		assertFalse(StringUtil.isNID("08714562314"));
		assertFalse(StringUtil.isNID("159117318301"));
		assertFalse(StringUtil.isNID("1591173183a"));
	}

	@Test
	public void testMobile() {
		assertTrue(StringUtil.isMobile("15911731833"));

		assertTrue(StringUtil.isMobile("08714562314"));
		assertFalse(StringUtil.isMobile("159117318301"));
		assertFalse(StringUtil.isMobile("1591173183a"));
	}

	@Test
	public void testEncodeHex_1() throws Exception {
		String str = "1";
		String result = StringUtil.encodeHex(str);
		System.out.println(result);

		str = "2";
		result = StringUtil.encodeHex(str);
		System.out.println(result);
		str = "3";
		result = StringUtil.encodeHex(str);
		System.out.println(result);
		str = "11";
		result = StringUtil.encodeHex(str);
		System.out.println(result);
		str = "111";
		result = StringUtil.encodeHex(str);
		System.out.println(result);
		str = "1111";
		result = StringUtil.encodeHex(str);
		System.out.println(result);
		str = "11111";
		result = StringUtil.encodeHex(str);
		System.out.println(result);
		str = "111111";
		result = StringUtil.encodeHex(str);
		System.out.println(result);
		str = "1111111";
		result = StringUtil.encodeHex(str);
		System.out.println(result);
		str = "11111111";
		result = StringUtil.encodeHex(str);
		System.out.println(result);
		str = "111111111";
		result = StringUtil.encodeHex(str);
		System.out.println(result);
		str = "1111111111";
		result = StringUtil.encodeHex(str);
		System.out.println(result);
		
		result = StringUtil.encodeHex("D:\\Winmail");
		System.out.println(result);
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	public static void main(String[] args) {
		new org.junit.runner.JUnitCore().run(StringUtilTest.class);
	}
}