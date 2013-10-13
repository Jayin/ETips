package com.meizhuo.etips.receiver;

import java.util.Calendar;

import com.meizhuo.etips.common.utils.CalendarManager;
import com.meizhuo.etips.common.utils.ETipsContants;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
 

/**
 * 时间发生改变、手机开机 均需要重新启动闹钟计时
 * 
 * @author Jayin Ton
 * 
 */
public class StartAlarmReceiver extends BroadcastReceiver {
    private Context context;
	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
		startCourseAlarmReceiver();
		
	}
	private void startCourseAlarmReceiver(){
		AlarmManager am = CalendarManager.getAlarmManager(context);
		Calendar c =  CalendarManager.getCourseAlarmDaily();
		PendingIntent operation = PendingIntent.getBroadcast(context, ETipsContants.ID_Alarm_Course,
				new Intent(ETipsContants.ACTION_Custom_Alarm), PendingIntent.FLAG_UPDATE_CURRENT);
		//如果当前时间比闹钟时间迟（大），说明前该闹钟失效应该推到下一天
		if(c.getTimeInMillis() < System.currentTimeMillis()){
			am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis()+24*60*60*1000, operation);
			System.out.println("StartAlarmReceiver:推移到第二天！！");
		}else{ //当日闹钟依然有效
			am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), operation);
			System.out.println("StartAlarmReceiver:今天！！！");
		}		
	}
}
