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
	 * 
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
	 * 获得当前是第几周 Note 由于版本原因 这里不会修改Application 中property里面的参数 property 会逐渐被弃用！
	 * </br> 挖了个小坑：在SharedPreference里面，里面的SharedPreference的Current_week</br>
	 * 实际上是保存当前week of year 而不是真的是一学期的当前周数 这样做的原因就是难以方便计算 当然 这里获取当前周数是封装好的 since
	 * version 1.2
	 * 
	 * @param context
	 * @return 当前是第几周
	 */
	public static int getCurrentWeek(Context context) {
		SharedPreferences sp = getSharedPreferences(context);
		int WEEK_OF_YEAR = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
		int current_week = Integer.parseInt(sp.getString("Current_Week",
				WEEK_OF_YEAR + ""));
		current_week = WEEK_OF_YEAR - current_week + 1;
		if (current_week <= 0 && current_week > 20) { // 非法数据，即记录的周数WEEK_OF_YEAR
														// 比 目前的 还大
			// （一般出现在用户自己调整了时间出现错误）
			current_week = 1;
			SharedPreferenceHelper.set(sp, "Current_Week", CalendarManager
					.getCalendar().get(Calendar.WEEK_OF_YEAR));
		}
		return current_week;
	}

}
