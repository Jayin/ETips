package com.meizhuo.etips.common.utils;

import java.util.Calendar;

import android.app.AlarmManager;
import android.content.Context;

/**
 * 时间日历、闹钟管理器
 * 
 * @author Jayin Ton
 * 
 */
public class CalendarManager {
	/**
	 * 获得一个AlarmManager对象
	 * 
	 * @param context
	 * @return
	 */
	public static AlarmManager getAlarmManager(Context context) {
		return (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
	}

	/**
	 * 获得一个当前时间的Calendar实例
	 * 
	 * @return
	 */
	public static Calendar getCalendar() {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
		return c;
	}

	/**
	 * 获得一个当日并指定的时、分、秒、毫秒的Calendar实例
	 * 
	 * @param HOUR_OF_DAY
	 * @param MINUTE
	 * @param SECOND
	 * @param MILLISECOND
	 * @return
	 */
	public static Calendar getCalendar(int HOUR_OF_DAY, int MINUTE, int SECOND,
			int MILLISECOND) {
		Calendar c = getCalendar();
		c.set(Calendar.HOUR_OF_DAY, HOUR_OF_DAY);
		c.set(Calendar.MINUTE, MINUTE);
		c.set(Calendar.SECOND, SECOND);
		c.set(Calendar.MILLISECOND, MILLISECOND);
		return c;
	}

	/**
	 * 一天又三个时间 早上；下午；晚上 区分：时间段 每日凌晨0:00； 每日中午12：00
	 * 
	 * @return
	 */
	public static Calendar getTimePart() {
		Calendar c = getCalendar();
		if (c.get(Calendar.HOUR_OF_DAY) >= 0
				&& c.get(Calendar.HOUR_OF_DAY) < 12) {
			// AM
			return getCalendar(8, 16, 0, 0);
		} else if (c.get(Calendar.HOUR_OF_DAY) >= 12
				&& c.get(Calendar.HOUR_OF_DAY) < 18) {
			// PM
			return getCalendar(14, 46, 0, 0);
		} else {
			// Night
			return getCalendar(19, 31, 0, 0);
		}
	}

	/**
	 * 一天又三个时间 早上；下午；晚上 区分：时间段 每日凌晨0:00； 每日中午12：00 Like
	 * {@linkplain#getTimePart()}
	 * 
	 * @return "AM"。"PM"。"Night"
	 */
	public static String getDayPart() {
		Calendar c = getCalendar();
		if (c.get(Calendar.HOUR_OF_DAY) >= 0
				&& c.get(Calendar.HOUR_OF_DAY) < 12) {
			return "AM";
		} else if (c.get(Calendar.HOUR_OF_DAY) >= 12
				&& c.get(Calendar.HOUR_OF_DAY) < 18) {
			return "PM";
		} else {
			return "Night";
		}
	}

	/**
	 * 获得今日星期几<br>
	 * 若星期一则返回1
	 * 
	 * @return 星期几，若星期一则返回1
	 */
	public static int getWeekDay() {
		int day_of_week = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
		return day_of_week - 1 == 0 ? 7 : day_of_week - 1;
	}

	/**
	 * 获得一个每日提醒要上多少节课的Calendar实例 时间默认为每日 20:01:00:00
	 * 
	 * @return
	 */
	public static Calendar getCourseAlarmDaily() {
		return getCalendar(20, 1, 0, 0);
	}

	/**
	 * 获得当日的 Week Of Yeah
	 * 挖了个小坑：在SharedPreference里面，里面的SharedPreference的Current_week 实际上是保存当前week
	 * of year 而不是真的是一学期的当前周数 这样做的原因就是难以方便计算
	 * 
	 * @return
	 */
	public static int getWeekOfYeah() {
		return Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * 判断是否时。分是否相等
	 * 
	 * @param f
	 * @param c
	 * @return true if HOUR_OF_DAY and MINUTE is equal
	 */
	public static boolean isInSameTime(Calendar f, Calendar c) {
		if (f == null && c == null)
			return true;
		if ((f == null && c != null) || (f != null && c == null))
			return false;
		if (f.get(Calendar.HOUR_OF_DAY) == c.get(Calendar.HOUR_OF_DAY)
				&& f.get(Calendar.MINUTE) == c.get(Calendar.MINUTE)) {
			return true;
		}
		return false;
	}

	/**
	 * 获得本学期的起始的周数
	 * 
	 * @return
	 */
	public static int getStartTermWeek() {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		if (month > Calendar.FEBRUARY && month < Calendar.SEPTEMBER) {
			// 下学期,本来打算每年的都弄一个日期，发现都是在2月24日附近开学 所以为了方便这里就。。这么设置了
			c.set(year, Calendar.FEBRUARY, 24);
			return c.get(Calendar.WEEK_OF_YEAR);
		} else {
			// 上学期,本来打算每年的都弄一个日期，发现都是在9月2日附近开学 所以为了方便这里就。。这么设置了
			c.set(year, Calendar.SEPTEMBER, 2); // 2013年9月2日 开学
			return c.get(Calendar.WEEK_OF_YEAR);
		}
	}

}
