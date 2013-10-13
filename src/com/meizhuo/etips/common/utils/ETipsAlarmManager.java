package com.meizhuo.etips.common.utils;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;

/**
 * 闹钟提醒管理<br>
 * 都是基于AlarmManager上的轻量级封装
 * @author Jayin Ton
 * 
 */
public class ETipsAlarmManager {
	private Context context;
	private AlarmManager alarmManager = null;

	public ETipsAlarmManager(Context context) {
		this.context = context;
		alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
	}
    /**
     * 设置重复的闹钟
     * 闹钟的识别是根据padding的属性，要一模一样
     * @param firstTime 第一次开启的时间
     * @param intervalMillis 闹钟(下次)启动的间隔时间
     * @param pendingIntent  闹钟响应时发出的PaendingIntent
     */
	public void setRepeatAlarm(long firstTime, long intervalMillis,
			PendingIntent pendingIntent) {
		// AlarmManager.RTC_WAKEUP :sleep时也发广播
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, firstTime,
				intervalMillis, pendingIntent);
	}
    /**
     * 取消一个闹钟
     * 闹钟的识别是根据padding的属性，要一模一样
     * requestcode 等等
     * @param pendingIntent 
     */
	public void cancleAlarm(PendingIntent pendingIntent) {
		alarmManager.cancel(pendingIntent);
	}
	/**
	 * 小时 +  分钟 构建一个firstTime第一次启动的时间
	 * @param hourOfDay  24小时制
	 * @param minute      0-60s
	 */
	public long getFirstTime(int hourOfDay,int minute){
		Calendar c =  Calendar.getInstance();
		System.out.println(c.getTimeInMillis());
		c.set(Calendar.HOUR_OF_DAY, hourOfDay);
		c.set(Calendar.MINUTE, minute);
	 
		System.out.println(c.get(Calendar.DAY_OF_WEEK));
		System.out.println(c.get(Calendar.HOUR_OF_DAY));
		System.out.println(c.get(Calendar.MINUTE));
		return c.getTimeInMillis();
	}
	
	 

}
