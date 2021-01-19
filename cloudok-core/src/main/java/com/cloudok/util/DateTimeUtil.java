package com.cloudok.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author xiazhijian
 * @date May 29, 2019 10:37:45 AM
 * 
 */
public class DateTimeUtil {

	/**
	 * 本地时间转utc
	 * 
	 * @param localtime
	 * @return
	 */
	public static long local2UTC(long localtime) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(localtime);
		int zoneOffset = calendar.get(java.util.Calendar.ZONE_OFFSET);
		int dstOffset = calendar.get(java.util.Calendar.DST_OFFSET);
		calendar.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
		return calendar.getTimeInMillis();
	}

	/**
	 * 本地时间转utc
	 * 
	 * @param localtime
	 * @return
	 */
	public static Date local2UTC(Date localtime) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(localtime);
		int zoneOffset = calendar.get(java.util.Calendar.ZONE_OFFSET);
		int dstOffset = calendar.get(java.util.Calendar.DST_OFFSET);
		calendar.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
		return calendar.getTime();
	}

	public static final String PATTERN_RFC1123 = "EEE, dd MMM yyyy HH:mm:ss zzz";

	/**
	 * Date format pattern used to parse HTTP date headers in RFC 1036 format.
	 */
	public static final String PATTERN_RFC1036 = "EEEE, dd-MMM-yy HH:mm:ss zzz";

	/**
	 * Date format pattern used to parse HTTP date headers in ANSI C
	 * <code>asctime()</code> format.
	 */
	public static final String PATTERN_ASCTIME = "EEE MMM d HH:mm:ss yyyy";

	static {
		Calendar calendar = Calendar.getInstance();
		calendar.set(2000, Calendar.JANUARY, 1, 0, 0);
	}

	/**
	 * 获取当前时间的秒数
	 * 
	 * @return
	 */
	public static Long getCurrentSecond() {

		return System.currentTimeMillis() / 1000;
	}

	/**
	 * 格式化
	 * 
	 * @param format
	 * @return
	 */
	public static Date parse(String date, String format) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			throw e;
		}
	}

	/**
	 * 格式化
	 * 
	 * @param time
	 * @return
	 */
	public static String formatTimestamp(Timestamp time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (time == null) {
			return null;
		}

		return sdf.format(time);
	}

	/**
	 * 格式化
	 *
	 * @param date
	 * @return
	 */
	public static String formatDefaultDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (date == null) {
			return null;
		}

		return sdf.format(date);
	}

	/**
	 * 格式化当前时间 返回格式yyMMdd
	 * 
	 * @return
	 */
	public static String getCurrentDateYYMMDD() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyMMdd");
		Date currentTime = new Date();
		return formatter.format(currentTime);
	}

	/**
	 * 格式化当前时间 返回格式yyyyMMdd
	 * 
	 * @return
	 */
	public static String getCurrentDateYYYYMMDD() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		Date currentTime = new Date();
		return formatter.format(currentTime);
	}

	/**
	 * 格式化
	 * 
	 * @param second
	 * @return
	 */
	public static String formatSecond(Long second) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (second == null) {
			return null;
		}
		return sdf.format(new Date(second * 1000));
	}

	/**
	 * 格式化
	 * 
	 * @param second
	 * @return
	 */
	public static String formatSecondOne(Long second) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (second == null) {
			return null;
		}
		return sdf.format(new Date(second * 1000));
	}

	/**
	 * 格式化
	 * 
	 * @return
	 */
	public static String formatCurrentDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}

	/**
	 * 格式化
	 * 
	 * @return
	 */
	public static String format(String format, Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	/**
	 * 格式化 yyyy-MM-dd
	 * 
	 * @param date
	 * @return
	 */
	public static String formatyyyyMMdd(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}

	/****
	 * 
	 * 格式化 yyyy-MM-dd. <br/>
	 * 
	 * @param date
	 * @return
	 * @author Coollf
	 * @date 2016年3月30日 下午4:21:46
	 */
	public static String formatSimpleyyyyMMdd(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(date);
	}

	/**
	 * 取当前日期的年月日部分
	 * 
	 * @return
	 */
	public static Date getDatePart(Date date) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
			String str = formatter.format(date);

			return formatter.parse(str);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * String 转化成 Long
	 * 
	 * @param format
	 * @return
	 * @throws ParseException
	 */
	public static Long dateFormat(String format) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = sdf.parse(format);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date.getTime() / 1000;
	}

	/**
	 * String 转化成 Long
	 * 
	 * @param format
	 * @return
	 * @throws ParseException
	 */
	public static Long timeFormat(String format) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = sdf.parse(format);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date.getTime() / 1000;
	}

	/**
	 * Formats the given date according to the RFC 1123 pattern.
	 * 
	 * @param date
	 *            The date to format.
	 * @return An RFC 1123 formatted date string.
	 * 
	 * @see #PATTERN_RFC1123
	 */
	public static String formatDate(Date date) {
		return formatDate(date, PATTERN_RFC1123);
	}

	/**
	 * Formats the given date according to the specified pattern. The pattern must
	 * conform to that used by the {@link SimpleDateFormat simple date format}
	 * class.
	 * 
	 * @param date
	 *            The date to format.
	 * @param pattern
	 *            The pattern to use for formatting the date.
	 * @return A formatted date string.
	 * 
	 * @throws IllegalArgumentException
	 *             If the given date pattern is invalid.
	 * 
	 * @see SimpleDateFormat
	 */
	public static String formatDate(Date date, String pattern) {
		if (date == null)
			throw new IllegalArgumentException("date is null");
		if (pattern == null)
			throw new IllegalArgumentException("pattern is null");

		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		return formatter.format(date);
	}

	/**
	 * 格式化
	 * 
	 * @return
	 */
	public static int getCurrentYear() {
		Calendar ca = new GregorianCalendar();
		GregorianCalendar.getInstance().setTime(new Date());
		return ca.get(Calendar.YEAR);
	}

	/**
	 * 格式化
	 * 
	 * @return
	 */
	public static int getCurrentMonth() {
		Calendar ca = new GregorianCalendar();
		GregorianCalendar.getInstance().setTime(new Date());
		return ca.get(Calendar.MONTH);
	}

	/**
	 * 格式化
	 * 
	 * @return
	 */
	public static int getCurrentDayOfMonth() {
		Calendar ca = new GregorianCalendar();
		GregorianCalendar.getInstance().setTime(new Date());
		return ca.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 格式化
	 * 
	 * @return
	 */
	public static int getCurrentDayOfWeek() {
		Calendar ca = new GregorianCalendar();
		GregorianCalendar.getInstance().setTime(new Date());
		return ca.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 时间添加天数
	 * 
	 * @param date
	 * @param day
	 * @return
	 */
	public static Date getDateAddDay(Date date, int day) {
		Calendar ca = new GregorianCalendar();
		ca.setTime(date);
		ca.add(Calendar.DAY_OF_YEAR, day);
		return ca.getTime();
	}

	/**
	 * 时间添加月份
	 * 
	 * @param date
	 * @param month
	 * @return
	 */
	public static Date getDateAddMonth(Date date, int month) {
		Calendar ca = new GregorianCalendar();
		ca.setTime(date);
		ca.add(Calendar.MONTH, month);
		return ca.getTime();
	}

	/**
	 * 时间操作
	 * 
	 * @param date
	 * @param type
	 * @param step
	 * @return
	 */
	public static Date addDateByType(Date date, int type, int step) {
		Calendar ca = new GregorianCalendar();
		ca.setTime(date);
		ca.add(type, step);
		return ca.getTime();
	}

	/**
	 * 计算日期间隔 -- 天
	 * 
	 * @param date
	 * @param date2
	 * @return
	 */
	public static int computeDateInterval(Date date, Date date2) {
		Calendar aCalendar = Calendar.getInstance();

		aCalendar.setTime(date);

		int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);

		aCalendar.setTime(date2);

		int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);

		return day2 - day1;
	}

	/**
	 * 比较时间大小
	 * 
	 * @param date
	 * @param date2
	 * @return
	 */
	public static int compareDate(Date date, Date date2) {
		if (date.getTime() > date2.getTime()) {
			return 1;
		} else if (date.getTime() < date2.getTime()) {
			return -1;
		} else {
			return 0;
		}
	}

	/**
	 * 设置当前月份为某一天
	 * 
	 * @param date
	 * @param day
	 * @return
	 */
	public static Date getDateThisMonthOfDay(Date date, int day) {
		Calendar ca = new GregorianCalendar();
		ca.setTime(date);
		int year = ca.get(Calendar.YEAR);
		int month = ca.get(Calendar.MONTH);
		ca.set(year, month, day);// 设置日期，此时的日期是2013年11月30号

		return ca.getTime();
	}

	/**
	 * 计算两个日期之间相差的天数
	 * 
	 * @param smdate
	 *            较小的时间
	 * @param bdate
	 *            较大的时间
	 * @return 相差天数
	 * @throws ParseException
	 */
	public static int daysBetween(Date smdate, Date bdate) throws ParseException {
		Calendar cal = Calendar.getInstance();
		cal.setTime(smdate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(bdate);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * 计算两个日期之间相差的月份
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public static int getMonthBetween(Date start, Date end) {
		if (start.after(end)) {
			Date t = start;
			start = end;
			end = t;
		}
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(start);
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(end);
		Calendar temp = Calendar.getInstance();
		temp.setTime(end);
		temp.add(Calendar.DATE, 1);

		int year = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
		int month = endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);

		if ((startCalendar.get(Calendar.DATE) == 1) && (temp.get(Calendar.DATE) == 1)) {
			return year * 12 + month + 1;
		} else if ((startCalendar.get(Calendar.DATE) != 1) && (temp.get(Calendar.DATE) == 1)) {
			return year * 12 + month;
		} else if ((startCalendar.get(Calendar.DATE) == 1) && (temp.get(Calendar.DATE) != 1)) {
			return year * 12 + month;
		} else {
			return (year * 12 + month - 1) < 0 ? 0 : (year * 12 + month);
		}
	}
}
