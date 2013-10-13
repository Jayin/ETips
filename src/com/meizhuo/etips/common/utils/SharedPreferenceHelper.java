package com.meizhuo.etips.common.utils;

import java.util.Calendar;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceHelper {
	public static boolean set(SharedPreferences sp, String key, String value) {
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(key, value);
		return editor.commit();
	}
    /**
     * 设置
     * @param sp
     * @param key
     * @param value
     * @return
     */
	public static boolean set(SharedPreferences sp, String key, int value) {
		return set(sp, key, String.valueOf(value));
	}

	/**
	 * 获得SharedPreference_NAME = "ETipsSharedPreference"
	 * 
	 * @return
	 */
	public static SharedPreferences getSharedPreferences(Context context) {
		return context.getSharedPreferences(
				ETipsContants.SharedPreference_NAME, Context.MODE_PRIVATE);
	}
	/**
	 * 获得当前是第几周
	 * Note 由于版本原因 这里不会修改Application 中property里面的参数
	 * property 会逐渐被弃用！
	 * since version 1.2 
	 * @param context
	 * @return 当前是第几周
	 */
	public static int getCurrentWeek(Context context){
		SharedPreferences sp = getSharedPreferences(context);
		int WEEK_OF_YEAR = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
		int current_week = Integer.parseInt(sp.getString("Current_Week", WEEK_OF_YEAR+""));
		current_week = WEEK_OF_YEAR - current_week + 1;
		if (current_week <= 0) { // 非法数据，即记录的周数WEEK_OF_YEAR 比 目前的 还大
			// （一般出现在用户自己调整了时间出现错误）
			current_week = 1;
			SharedPreferenceHelper.set(sp, "Current_Week", CalendarManager
					.getCalendar().get(Calendar.WEEK_OF_YEAR));
		}
		return current_week;
	}
	
	 
}
