package com.jiongsoft.cocit.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";

	public static final String DEFAULT_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

	public static final String[] NLS_WEEKS = new String[] { "星期天", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };

	public static final String[] NLS_MONTHS = new String[] { "", "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月" };

	private Calendar calendar;

	private DateUtil(Date date) {
		this.calendar = getCalendar(date);
	}

	public static Calendar getCalendar() {
		return getCalendar(new Date());
	}

	public static Calendar getCalendar(Date date) {
		Calendar calendar = Calendar.getInstance();
		if (date == null)
			date = new Date();
		calendar.setTime(date);

		return calendar;
	}

	public static Date parse(String strDate) {
		if (strDate == null) {
			return null;
		}
		strDate = strDate.trim();
		int len = strDate.length();
		try {
			if (len >= 19) {
				return new SimpleDateFormat(DEFAULT_DATE_TIME_PATTERN).parse(strDate.trim());
			} else if (len >= 16) {
				return new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(strDate.trim());
			} else if (len >= 13) {
				return new SimpleDateFormat("yyyy-MM-dd HH").parse(strDate.trim());
			}
			return new SimpleDateFormat(DEFAULT_DATE_PATTERN).parse(strDate.trim());
		} catch (ParseException e) {
			throw new CocException(e);
		}
	}

	public static Date parse(String strDate, String format) {
		try {
			return new SimpleDateFormat(format).parse(strDate);
		} catch (ParseException e) {
			throw new CocException(e);
		}
	}

	public static String format(Date date) {
		if (date == null)
			return "";

		return (new SimpleDateFormat(DEFAULT_DATE_PATTERN)).format(date);
	}

	public static String format(Date date, String format) {
		if (date == null) {
			return "";
		}
		return (new SimpleDateFormat(format)).format(date);
	}

	public static String formatDateTime(Date date) {
		if (date == null) {
			return "";
		}
		return (new SimpleDateFormat(DEFAULT_DATE_TIME_PATTERN)).format(date);
	}

	public static String getNowDate() {
		Date date = new Date();
		return new SimpleDateFormat(DEFAULT_DATE_PATTERN).format(date);
	}

	public static String getNowDateTime() {
		Date date = new Date();
		return new SimpleDateFormat(DEFAULT_DATE_TIME_PATTERN).format(date);
	}

	public static Date getToday() {
		return parse(getNowDate());
	}

	public static Date getNext(int day) {
		return getNext(new Date(), day);
	}

	public static Date getNext(Date date, int day) {
		return new Date(date.getTime() + day * 24 * 60 * 60 * 1000);
	}

	public static DateUtil now() {
		return new DateUtil(new Date());
	}

	public static DateUtil make(Date date) {
		return new DateUtil(date);
	}

	public static Date makeDate(int yyyy, int MM, int dd) {
		return makeDate(yyyy, MM, dd, 0, 0, 0);
	}

	public static Date makeDate(int yyyy, int MM, int dd, int HH, int mm, int ss) {
		DateUtil date = now();

		date.setYear(yyyy);
		date.setMonth(MM);
		date.setDay(dd);
		date.setHour(HH);
		date.setMinite(mm);
		date.setSecond(ss);

		date.calendar.set(Calendar.MILLISECOND, 0);

		return date.calendar.getTime();
	}

	public Date get() {
		return calendar.getTime();
	}

	/**
	 * 哪年？
	 * 
	 * @return
	 */
	public int getYear() {
		return calendar.get(Calendar.YEAR);
	}

	/**
	 * 设置年份
	 * 
	 * @param value
	 */
	public DateUtil setYear(int value) {
		calendar.set(Calendar.YEAR, value);
		return this;
	}

	/**
	 * 几月？
	 * 
	 * @return 1-12
	 */
	public int getMonth() {
		return calendar.get(Calendar.MONTH) + 1;
	}

	/**
	 * 设置月份
	 * 
	 * @param value
	 *            1-12
	 */
	public DateUtil setMonth(int value) {
		calendar.set(Calendar.MONTH, value - 1);
		return this;
	}

	/**
	 * 几日？
	 * 
	 * @return
	 */
	public int getDay() {
		return calendar.get(Calendar.DATE);
	}

	/**
	 * 设置日
	 * 
	 * @param value
	 *            1-31
	 */
	public DateUtil setDay(int value) {
		calendar.set(Calendar.DATE, value);
		return this;
	}

	/**
	 * 几点？
	 * 
	 * @return 0-23
	 */
	public int getHour() {
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 设置小时
	 * 
	 * @param value
	 *            0-23
	 */
	public DateUtil setHour(int value) {
		calendar.set(Calendar.HOUR_OF_DAY, value);
		return this;
	}

	/**
	 * 几分？
	 * 
	 * @return
	 */
	public int getMinite() {
		return calendar.get(Calendar.MINUTE);
	}

	/**
	 * 设置分钟
	 * 
	 * @param value
	 *            0-59
	 */
	public DateUtil setMinite(int value) {
		calendar.set(Calendar.MINUTE, value);
		return this;
	}

	/**
	 * 几秒？
	 * 
	 * @return
	 */
	public int getSecond() {
		return calendar.get(Calendar.SECOND);
	}

	/**
	 * 设置秒钟
	 * 
	 * @param value
	 *            0-59
	 */
	public DateUtil setSecond(int value) {
		calendar.set(Calendar.SECOND, value);
		return this;
	}

	/**
	 * 星期几？0-星期天
	 * 
	 * @return 0-6
	 */
	public int getWeek() {
		return calendar.get(Calendar.DAY_OF_WEEK) - 1;
	}

	/**
	 * 设置毫秒数
	 * 
	 * @param value
	 */
	public DateUtil setMilliSecond(int value) {
		calendar.set(Calendar.MILLISECOND, value);
		return this;
	}

	public DateUtil setTime(int HH, int mm, int ss, int ms) {
		this.setHour(HH);
		this.setMinite(mm);
		this.setSecond(ss);
		this.setMilliSecond(ms);
		return this;
	}

	public DateUtil setDate(int yyyy, int MM, int dd) {
		this.setYear(yyyy);
		this.setMonth(MM);
		this.setDay(dd);
		return this;
	}

	public DateUtil setDateTime(int yyyy, int MM, int dd, int HH, int mm, int ss, int ms) {
		this.setDate(yyyy, MM, dd);
		this.setTime(HH, mm, ss, ms);
		return this;
	}
}
