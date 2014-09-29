package com.jiongsoft.cocit.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;

import mockit.Expectations;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.jiongsoft.cocit.util.ClassUtil;
import com.jiongsoft.cocit.util.CocException;
import com.jiongsoft.cocit.util.StringUtil;

public class ClassUtilTest {

	boolean boolVal = true;

	Boolean boolObj = new Boolean(true);

	byte byteVal = 9;

	Byte byteObj = Byte.valueOf("9");

	short shortVal = 9;

	Short shortObj = Short.valueOf("9");

	int intVal = 9;

	Integer intObj = new Integer(9);

	long longVal = 9;

	Long longObj = new Long(9);

	float floatVal = 1.0f;

	Float floatObj = new Float(1.0);

	double doubleVal = 1.0;

	Double doubleObj = new Double(1.0);

	Number numObj = new BigDecimal("12.3E+7");

	Class cls = this.getClass();

	Object obj = new Object();

	String str = "str";

	Date date = new Date();

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testIsBoolean() throws Exception {
		boolean trueValue = ClassUtil.isBoolean(false) && ClassUtil.isBoolean(boolVal) && ClassUtil.isBoolean(boolObj)//
		;
		boolean falseValue = ClassUtil.isBoolean(null)//
				|| ClassUtil.isBoolean(byteVal) || ClassUtil.isBoolean(byteObj)//
				|| ClassUtil.isBoolean(shortVal) || ClassUtil.isBoolean(shortObj)//
				|| ClassUtil.isBoolean(intVal) || ClassUtil.isBoolean(intObj)//
				|| ClassUtil.isBoolean(longVal) || ClassUtil.isBoolean(longObj)//
				|| ClassUtil.isBoolean(floatVal) || ClassUtil.isBoolean(floatObj)//
				|| ClassUtil.isBoolean(doubleVal) || ClassUtil.isBoolean(doubleObj)//
				|| ClassUtil.isBoolean(numObj)//
				|| ClassUtil.isBoolean(cls)//
				|| ClassUtil.isBoolean(obj)//
				|| ClassUtil.isBoolean(str)//
				|| ClassUtil.isBoolean(date)//
		;
		assertTrue(trueValue);
		assertFalse(falseValue);
	}

	@Test
	public void testIsByte() throws Exception {
		boolean trueValue = ClassUtil.isByte(byteVal) && ClassUtil.isByte(byteObj)//
		;
		boolean falseValue = ClassUtil.isByte(null)//
				|| ClassUtil.isByte(boolVal) || ClassUtil.isByte(boolObj)//
				|| ClassUtil.isByte(shortVal) || ClassUtil.isByte(shortObj)//
				|| ClassUtil.isByte(intVal) || ClassUtil.isByte(intObj)//
				|| ClassUtil.isByte(longVal) || ClassUtil.isByte(longObj)//
				|| ClassUtil.isByte(floatVal) || ClassUtil.isByte(floatObj)//
				|| ClassUtil.isByte(doubleVal) || ClassUtil.isByte(doubleObj)//
				|| ClassUtil.isByte(numObj)//
				|| ClassUtil.isByte(cls)//
				|| ClassUtil.isByte(obj)//
				|| ClassUtil.isByte(str)//
				|| ClassUtil.isByte(date)//
		;
		assertTrue(trueValue);
		assertFalse(falseValue);
	}

	@Test
	public void testIsShort() throws Exception {
		boolean trueValue = ClassUtil.isShort(shortVal) && ClassUtil.isShort(shortObj)//
		;
		boolean falseValue = ClassUtil.isShort(null)//
				|| ClassUtil.isShort(boolVal) || ClassUtil.isShort(boolObj)//
				|| ClassUtil.isShort(byteVal) || ClassUtil.isShort(byteObj)//
				|| ClassUtil.isShort(intVal) || ClassUtil.isShort(intObj)//
				|| ClassUtil.isShort(longVal) || ClassUtil.isShort(longObj)//
				|| ClassUtil.isShort(floatVal) || ClassUtil.isShort(floatObj)//
				|| ClassUtil.isShort(doubleVal) || ClassUtil.isShort(doubleObj)//
				|| ClassUtil.isShort(numObj)//
				|| ClassUtil.isShort(cls)//
				|| ClassUtil.isShort(obj)//
				|| ClassUtil.isShort(str)//
				|| ClassUtil.isShort(date)//
		;
		assertTrue(trueValue);
		assertFalse(falseValue);
	}

	@Test
	public void testIsInteger() throws Exception {
		boolean trueValue = ClassUtil.isInteger(intVal) && ClassUtil.isInteger(intObj)//
		;
		boolean falseValue = ClassUtil.isInteger(null)//
				|| ClassUtil.isInteger(boolVal) || ClassUtil.isInteger(boolObj)//
				|| ClassUtil.isInteger(byteVal) || ClassUtil.isInteger(byteObj)//
				|| ClassUtil.isInteger(shortVal) || ClassUtil.isInteger(shortObj) //
				|| ClassUtil.isInteger(longVal) || ClassUtil.isInteger(longObj)//
				|| ClassUtil.isInteger(floatVal) || ClassUtil.isInteger(floatObj)//
				|| ClassUtil.isInteger(doubleVal) || ClassUtil.isInteger(doubleObj)//
				|| ClassUtil.isInteger(numObj)//
				|| ClassUtil.isInteger(cls)//
				|| ClassUtil.isInteger(obj)//
				|| ClassUtil.isInteger(str)//
				|| ClassUtil.isInteger(date)//
		;
		assertTrue(trueValue);
		assertFalse(falseValue);
	}

	@Test
	public void testIsLong() throws Exception {
		boolean trueValue = ClassUtil.isLong(longVal) && ClassUtil.isLong(longObj)//
		;
		boolean falseValue = ClassUtil.isLong(null)//
				|| ClassUtil.isLong(boolVal) || ClassUtil.isLong(boolObj)//
				|| ClassUtil.isLong(byteVal) || ClassUtil.isLong(byteObj)//
				|| ClassUtil.isLong(shortVal) || ClassUtil.isLong(shortObj) //
				|| ClassUtil.isLong(intVal) || ClassUtil.isLong(intObj)//
				|| ClassUtil.isLong(floatVal) || ClassUtil.isLong(floatObj)//
				|| ClassUtil.isLong(doubleVal) || ClassUtil.isLong(doubleObj)//
				|| ClassUtil.isLong(numObj)//
				|| ClassUtil.isLong(cls)//
				|| ClassUtil.isLong(obj)//
				|| ClassUtil.isLong(str)//
				|| ClassUtil.isLong(date)//
		;
		assertTrue(trueValue);
		assertFalse(falseValue);
	}

	@Test
	public void testIsFloat() throws Exception {
		boolean trueValue = ClassUtil.isFloat(floatVal) && ClassUtil.isFloat(floatObj)//
		;
		boolean falseValue = ClassUtil.isFloat(null)//
				|| ClassUtil.isFloat(boolVal) || ClassUtil.isFloat(boolObj)//
				|| ClassUtil.isFloat(byteVal) || ClassUtil.isFloat(byteObj)//
				|| ClassUtil.isFloat(shortVal) || ClassUtil.isFloat(shortObj) //
				|| ClassUtil.isFloat(intVal) || ClassUtil.isFloat(intObj)//
				|| ClassUtil.isFloat(longVal) || ClassUtil.isFloat(longObj)//
				|| ClassUtil.isFloat(doubleVal) || ClassUtil.isFloat(doubleObj)//
				|| ClassUtil.isFloat(numObj)//
				|| ClassUtil.isFloat(cls)//
				|| ClassUtil.isFloat(obj)//
				|| ClassUtil.isFloat(str)//
				|| ClassUtil.isFloat(date)//
		;
		assertTrue(trueValue);
		assertFalse(falseValue);
	}

	@Test
	public void testIsDouble() throws Exception {
		boolean trueValue = ClassUtil.isDouble(doubleVal) && ClassUtil.isDouble(doubleObj)
		//
		;
		boolean falseValue = ClassUtil.isDouble(null)//
				|| ClassUtil.isDouble(boolVal) || ClassUtil.isDouble(boolObj)//
				|| ClassUtil.isDouble(byteVal) || ClassUtil.isDouble(byteObj)//
				|| ClassUtil.isDouble(shortVal) || ClassUtil.isDouble(shortObj) //
				|| ClassUtil.isDouble(intVal) || ClassUtil.isDouble(intObj)//
				|| ClassUtil.isDouble(longVal) || ClassUtil.isDouble(longObj)//
				|| ClassUtil.isDouble(floatVal) || ClassUtil.isDouble(floatObj)//
				|| ClassUtil.isDouble(numObj)//
				|| ClassUtil.isDouble(cls)//
				|| ClassUtil.isDouble(obj)//
				|| ClassUtil.isDouble(str)//
				|| ClassUtil.isDouble(date)//
		;
		assertTrue(trueValue);
		assertFalse(falseValue);
	}

	@Test
	public void testIsNumber() throws Exception {
		boolean trueValue = ClassUtil.isNumber(byteVal) && ClassUtil.isNumber(byteObj)//
				&& ClassUtil.isNumber(shortVal) && ClassUtil.isNumber(shortObj) //
				&& ClassUtil.isNumber(intVal) && ClassUtil.isNumber(intObj)//
				&& ClassUtil.isNumber(longVal) && ClassUtil.isNumber(longObj)//
				&& ClassUtil.isNumber(floatVal) && ClassUtil.isNumber(floatObj)//
				&& ClassUtil.isNumber(doubleVal) && ClassUtil.isNumber(doubleObj)//
				&& ClassUtil.isNumber(numObj)//
		//
		;
		boolean falseValue = ClassUtil.isNumber(null)//
				|| ClassUtil.isNumber(boolVal) || ClassUtil.isNumber(boolObj)//
				|| ClassUtil.isNumber(cls)//
				|| ClassUtil.isNumber(obj)//
				|| ClassUtil.isNumber(str)//
				|| ClassUtil.isNumber(date)//
		;
		assertTrue(trueValue);
		assertFalse(falseValue);
	}

	@Test
	public void testIsString() throws Exception {
		boolean trueValue = ClassUtil.isString(str)//
		//
		;
		boolean falseValue = ClassUtil.isString(null)//
				|| ClassUtil.isString(boolVal) || ClassUtil.isString(boolObj)//
				|| ClassUtil.isString(byteVal) || ClassUtil.isString(byteObj)//
				|| ClassUtil.isString(shortVal) || ClassUtil.isString(shortObj) //
				|| ClassUtil.isString(intVal) || ClassUtil.isString(intObj)//
				|| ClassUtil.isString(longVal) || ClassUtil.isString(longObj)//
				|| ClassUtil.isString(floatVal) || ClassUtil.isString(floatObj)//
				|| ClassUtil.isString(doubleVal) || ClassUtil.isString(doubleObj)//
				|| ClassUtil.isString(numObj)//
				|| ClassUtil.isString(cls)//
				|| ClassUtil.isString(obj)//
				|| ClassUtil.isString(date)//
		;
		assertTrue(trueValue);
		assertFalse(falseValue);
	}

	@Test
	public void testIsDate() throws Exception {
		boolean trueValue = ClassUtil.isDate(date)//
		//
		//
		;
		boolean falseValue = ClassUtil.isDate(null)//
				|| ClassUtil.isDate(boolVal) || ClassUtil.isDate(boolObj)//
				|| ClassUtil.isDate(byteVal) || ClassUtil.isDate(byteObj)//
				|| ClassUtil.isDate(shortVal) || ClassUtil.isDate(shortObj) //
				|| ClassUtil.isDate(intVal) || ClassUtil.isDate(intObj)//
				|| ClassUtil.isDate(longVal) || ClassUtil.isDate(longObj)//
				|| ClassUtil.isDate(floatVal) || ClassUtil.isDate(floatObj)//
				|| ClassUtil.isDate(doubleVal) || ClassUtil.isDate(doubleObj)//
				|| ClassUtil.isDate(numObj)//
				|| ClassUtil.isDate(cls)//
				|| ClassUtil.isDate(obj)//
				|| ClassUtil.isDate(str)//
		;
		assertTrue(trueValue);
		assertFalse(falseValue);
	}

	@Test
	public void testIsPrimitive() throws Exception {
		boolean trueValue = ClassUtil.isPrimitive(boolVal) && ClassUtil.isPrimitive(boolObj)//
				&& ClassUtil.isPrimitive(byteVal) && ClassUtil.isPrimitive(byteObj)//
				&& ClassUtil.isPrimitive(shortVal) && ClassUtil.isPrimitive(shortObj) //
				&& ClassUtil.isPrimitive(intVal) && ClassUtil.isPrimitive(intObj)//
				&& ClassUtil.isPrimitive(longVal) && ClassUtil.isPrimitive(longObj)//
				&& ClassUtil.isPrimitive(floatVal) && ClassUtil.isPrimitive(floatObj)//
				&& ClassUtil.isPrimitive(doubleVal) && ClassUtil.isPrimitive(doubleObj)//
				&& ClassUtil.isPrimitive(str)//
		//
		;
		boolean falseValue = ClassUtil.isPrimitive(null)//
				|| ClassUtil.isPrimitive(numObj)//
				|| ClassUtil.isPrimitive(cls)//
				|| ClassUtil.isPrimitive(obj)//
				|| ClassUtil.isPrimitive(date)//
		;
		assertTrue(trueValue);
		assertFalse(falseValue);
	}

	@Test
	public void testNewInstance() throws Throwable {
		TestBean obj = ClassUtil.newInstance(TestBean.class.getName());
		assertNotNull(obj);

		obj = ClassUtil.newInstance(TestBean.class.getName(), "value1");
		assertNotNull(obj);
		assertEquals("value1", obj.prop1);

		obj = ClassUtil.newInstance(TestBean.class.getName(), "value1", "value2");
		assertNotNull(obj);
		assertEquals("value1", obj.prop1);
		assertEquals("value2", obj.prop2);

		obj = ClassUtil.newInstance(TestBean.class.getName(), "value1", "value2", "value3");
		assertNotNull(obj);
		assertEquals("value1", obj.prop1);
		assertEquals("value2", obj.prop2);
		assertEquals("value3", obj.prop3);

		obj = ClassUtil.newInstance(TestBean.class.getName(), new MockHttpServletRequest(), new MockHttpServletResponse());
		assertNotNull(obj);

		obj = ClassUtil.newInstance(TestBean.class.getName(), new MockHttpServletRequest(), null);
		assertNotNull(obj);

		obj = ClassUtil.newInstance(TestBean.class.getName(), null, new MockHttpServletResponse());
		assertNotNull(obj);

		obj = ClassUtil.newInstance(TestBean.class.getName(), null, null);
		assertNotNull(obj);

		obj = ClassUtil.newInstance(TestBean.class.getName(), null, null, null);
		assertNotNull(obj);

		obj = ClassUtil.newInstance(TestBean.class.getName(), null, null, null, null);
		assertNull(obj);

		TestBean[] array = ClassUtil.newInstance(TestBean[].class.getName());
		assertNotNull(array);
		array = ClassUtil.newInstance(TestBean.class.getName() + "[]");
		assertNotNull(array);

		TestBean.InnerClass innerObj = ClassUtil.newInstance(TestBean.InnerClass.class.getName());
		assertNotNull(innerObj);

		String[] arr1 = ClassUtil.newInstance("[Ljava.lang.String;");
		assertNotNull(arr1);

		try {
			ClassUtil.newInstance("[Ljava.lang.String");
			assertTrue(false);
		} catch (CocException e) {
			assertTrue(true);
		}

		try {
			ClassUtil.newInstance("java.lang.String;");
			assertTrue(false);
		} catch (CocException e) {
			assertTrue(true);
		}

		Object nullObj = ClassUtil.newInstance((String) null);
		assertNull(nullObj);
		nullObj = ClassUtil.newInstance("");
		assertNull(nullObj);

		new Expectations(Thread.class) {
			{
				Thread.currentThread().getContextClassLoader();
				result = null;
			}
		};

		obj = ClassUtil.newInstance(TestBean.class.getName());
		assertNotNull(obj);
	}

	@Test
	public void testBase64() throws IOException {
		String str = null;
		String str2 = null;
		System.out.println(str + " : " + StringUtil.encodeHex(str));

		str = "";
		str2 = StringUtil.encodeHex(str);
		System.out.println(str + " ==> " + str2);
		assertEquals(str, StringUtil.decodeHex(str2));

		str = "" + new Random().nextInt(100);
		str2 = StringUtil.encodeHex(str);
		System.out.println(str + " ==> " + str2);
		assertEquals(str, StringUtil.decodeHex(str2));

		str = "123456";
		str2 = StringUtil.encodeHex(str);
		System.out.println(str + " ==> " + str2);
		assertEquals(str, StringUtil.decodeHex(str2));

		str = " 对中文进行加密 ";
		str2 = StringUtil.encodeHex(str);
		System.out.println(str + " ==> " + str2);
		assertEquals(str, StringUtil.decodeHex(str2));

		str = new Random().nextInt() + ":" + new Random().nextInt();
		str2 = StringUtil.encodeHex(str);
		System.out.println(str + " ==> " + str2);
		assertEquals(str, StringUtil.decodeHex(str2));

		str = "模块ID/系统ID:/）（）*&……%￥#@！~+——";
		str2 = StringUtil.encodeHex(str);
		System.out.println(str + " ==> " + str2);
		assertEquals(str, StringUtil.decodeHex(str2));

		System.out.println(StringUtil.decodeHex("1"));
	}

}