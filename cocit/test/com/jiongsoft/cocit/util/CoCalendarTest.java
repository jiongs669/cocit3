package com.jiongsoft.cocit.util;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CoCalendarTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDateStr() {
		Date date = DateUtil.now().get();
		Integer time = new Long(date.getTime()).intValue();
		System.out.println(DateUtil.formatDateTime(date));
		System.out.println(date.getTime() + ":" + time + ":" + Integer.toHexString(time).toUpperCase());
	}

	@Test
	public void makeDate() {
		Date date = DateUtil.makeDate(2013, 1, 1);

		System.out.println(DateUtil.formatDateTime(date));

		DateUtil util = DateUtil.make(date);
		assertEquals(2013, util.getYear());
		assertEquals(1, util.getMonth());
		assertEquals(1, util.getDay());
		assertEquals(0, util.getHour());
		assertEquals(0, util.getMinite());
		assertEquals(0, util.getSecond());

		util.setMonth(9);
		util.setDay(15);
		System.out.println(DateUtil.formatDateTime(util.get()));
		assertEquals(2013, util.getYear());
		assertEquals(9, util.getMonth());
		assertEquals(15, util.getDay());
		assertEquals(0, util.getHour());
		assertEquals(0, util.getMinite());
		assertEquals(0, util.getSecond());
		assertEquals(0, util.getWeek());

		util.setMonth(9);
		util.setDay(16);
		System.out.println(DateUtil.formatDateTime(util.get()));
		assertEquals(2013, util.getYear());
		assertEquals(9, util.getMonth());
		assertEquals(16, util.getDay());
		assertEquals(0, util.getHour());
		assertEquals(0, util.getMinite());
		assertEquals(0, util.getSecond());
		assertEquals(1, util.getWeek());

		util.setMonth(12);
		util.setDay(1);
		System.out.println(DateUtil.formatDateTime(util.get()));
		assertEquals(2013, util.getYear());
		assertEquals(12, util.getMonth());
		assertEquals(1, util.getDay());
		assertEquals(0, util.getHour());
		assertEquals(0, util.getMinite());
		assertEquals(0, util.getSecond());

		util.setMonth(12);
		util.setDay(31);
		System.out.println(DateUtil.formatDateTime(util.get()));
		assertEquals(2013, util.getYear());
		assertEquals(12, util.getMonth());
		assertEquals(31, util.getDay());
		assertEquals(0, util.getHour());
		assertEquals(0, util.getMinite());
		assertEquals(0, util.getSecond());

		util.setMonth(12);
		util.setDay(32);
		System.out.println(DateUtil.formatDateTime(util.get()));

		util.setYear(2013);
		util.setMonth(12);
		util.setDay(0);
		System.out.println(DateUtil.formatDateTime(util.get()));

		util.setYear(2013);
		util.setMonth(1);
		util.setDay(0);
		System.out.println(DateUtil.formatDateTime(util.get()));
	}

	@Test
	public void test1() {

		//
		DateUtil cal = DateUtil.now();
		cal.setTime(0, 0, 0, 0);
		int year = cal.getYear();
		int month = cal.getMonth();
		// 添加12个月的计划
		for (int i = 1; i <= 12; i++) {

			// 设置月份
			cal.setYear(year);
			cal.setMonth(month + i);

			// 从月末的一周中找出周四和周五
			cal.setDay(-7);
			int day = cal.getDay();
			for (int j = 1; j <= 7; j++) {
				cal.setDay(day + j);

				if (cal.getWeek() == 4 || cal.getWeek() == 5) {
					String str = DateUtil.format(cal.get(), "yyyy-MM-dd HH:mm:ss E");
					System.out.println("i=" + i + ", day=" + day + ", date=" + str);
				}
			}
		}

	}
}