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
	 * 获得一个每日提醒要上多少节课的Calendar实例 时间默认为每日 20:01:00:00
	 * 
	 * @return
	 */
	public static Calendar getCourseAlarmDaily() {
		return getCalendar(20, 1, 0, 0);
	}

	/**
	 * 获得当日的 Week Of Yeah
	 * 
	 * @return
	 */
	public static int getWeekOfYeah() {
		return Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
	}
    /**
     * 判断是否时。分是否相等
     * @param f  
     * @param c
     * @return true if HOUR_OF_DAY and MINUTE is equal
     */
	public static boolean isInSameTime(Calendar f,Calendar c){
		if(f==null && c == null) return true;
		if((f==null && c!=null) || (f!=null && c==null))return false;
		if(f.get(Calendar.HOUR_OF_DAY)==c.get(Calendar.HOUR_OF_DAY)&& f.get(Calendar.MINUTE)==c.get(Calendar.MINUTE)){
			return true;
		}
		return false;
	}
}
