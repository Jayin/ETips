package com.meizhuo.etips.common.utils;

import java.util.Calendar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class SharedPreferenceHelper {
	public static boolean set(SharedPreferences sp, String key, String defvalue) {
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(key, defvalue);
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
	 * add in :
	 * 将会被移除
	 * version 2.2
	 * @param context
	 * @return 当前是第几周
	 */
//	public static int getCurrentWeek(Context context) {
//		SharedPreferences sp = getSharedPreferences(context);
//		int WEEK_OF_YEAR = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
//		int current_week = Integer.parseInt(sp.getString("Current_Week",
//				WEEK_OF_YEAR + ""));
//		current_week = WEEK_OF_YEAR - current_week + 1;
//		if (current_week <= 0 && current_week > 20) { // 非法数据，即记录的周数WEEK_OF_YEAR
//														// 比 目前的 还大
//			// （一般出现在用户自己调整了时间出现错误）
//			current_week = 1;
//			SharedPreferenceHelper.set(sp, "Current_Week", CalendarManager
//					.getCalendar().get(Calendar.WEEK_OF_YEAR));
//		}
//		return current_week;
//	}
    /**
     * 设置当前(本学期)的周数<br>
     * 配合weekOfYear使用
     * @since version 2.2
     * @param context 
     * @param currentWeek 一学期里的周数
     */
//	public static void setCurrentWeek(Context context, int currentWeek) {
//		if (currentWeek <= 0) {
//			currentWeek = 1;
//		}
//		SharedPreferences sp = context.getSharedPreferences(
//				ETipsContants.SharedPreference_NAME, Context.MODE_PRIVATE);
//		SharedPreferenceHelper.set(sp, "currentWeek", currentWeek + "");
//		SharedPreferenceHelper.set(sp, "weekOfYear",
//				CalendarManager.getWeekOfYeah() + "");
//		// 发广播通知Home页面要课表，时间切换
//		Intent intent = new Intent(ETipsContants.Action_CurrentWeekChange);
//		context.sendBroadcast(intent);
//	}
    /**
     * 补坑:1.之前的方法有bug,一旦年切换了，week Of Year 就会由新开始计算，导致最后的crrent week计算出来是负数<br>
     *    2. sp里面最终保存的不是真正的crrent week，而是week Of Year ,不容易处理<br>
     * 解决办法:currentWeek 保存真的是当前，本学期的周数。因为currentWeek只会递增(除非你自己修改了)<br>
     *       currentWeek 递增的依据，所保存的weekOfYear与当前CalendarManager.getWeekOfYeah()“不一致”就会增一<br>
     *       可能你会问：为啥不是weekOfYear递增才导致currentWeek递增？ 请理解补坑第一点<br>
     * @since version 2.2 移除到ETipsUtils
     * @param context
     * @return
     */
//	public static int _getCrrentWeek(Context context) {
//		SharedPreferences sp = context.getSharedPreferences(
//				ETipsContants.SharedPreference_NAME, Context.MODE_PRIVATE);
//		int currentWeek = Integer.parseInt(sp.getString("currentWeek", "1"));
//		int weekOfYear = Integer.parseInt(sp.getString("weekOfYear",
//				CalendarManager.getWeekOfYeah() + ""));
//		if(weekOfYear != CalendarManager.getWeekOfYeah()){
//			SharedPreferenceHelper.set(sp, "currentWeek", (++currentWeek) + "");
//			SharedPreferenceHelper.set(sp, "weekOfYear",
//					CalendarManager.getWeekOfYeah() + "");
//		} 
//		return currentWeek;
//		 
//	}
}
