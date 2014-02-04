package com.meizhuo.etips.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.meizhuo.etips.common.CalendarManager;
import com.meizhuo.etips.common.ETipsContants;
import com.meizhuo.etips.common.SharedPreferenceHelper;

/**
 * 用户配置偏好信息,管理版本等信息<br>
 * ps:一般有默认值<brs>
 * 一般都是isXXX() / getXXX() /setxxx()的方法
 * (准备分离，暂时保留)1.获取/设置是否第一次打开App<br>
 * 2.获取是否有课表<br>
 * 3.获取/设置当前周数<br>
 * 4.获取/设置是否有未读消息<br>
 * 5.获取/设置子系统账号、密码<br>
 * 6.获取/设置当前版本代号<br>
 * @author Jayin Ton
 * @version 2.2
 */
public class Preferences {

	/** to get SharedPreferences */
	public static SharedPreferences getSP(Context context) {
		return context.getSharedPreferences(ETipsContants.SP_NAME_Preferences,
				Context.MODE_PRIVATE);
	}

	/** helper method like the set() in class SharedPreferenceHelper1 */
	public static boolean set(SharedPreferences sp, String key, String value) {
		return SharedPreferenceHelper.set(sp, key, value);
	}

	/** 是否第一次打开App see{@linkplain#setIsFirstOpenApp} */
	public static boolean isFirstOpenApp(Context context) {
		return getSP(context).getString("First_Open_APP", "YES").equals("YES");
	}

	/** 设置是否第一次打开App */
	public static boolean setIsFirstOpenApp(Context context,
			boolean isFirstOpenApp) {
		return set(getSP(context), "First_Open_APP", isFirstOpenApp ? "YES"
				: "NO");
	}

	/** 是否有课表,这是触发式的，故省略设置是否存在 */
	public static boolean isCourseExist(Context context) {
		return AppInfo.getCourse(context) == null ? false : true;
	}

	public static int getCurrentWeek(Context context) {
		SharedPreferences sp = getSP(context);
		int currentWeek = Integer.parseInt(sp.getString("currentWeek", "1"));
		int weekOfYear = Integer.parseInt(sp.getString("weekOfYear",
				CalendarManager.getWeekOfYeah() + ""));
		if (weekOfYear != CalendarManager.getWeekOfYeah()) {
			currentWeek++;
			setCurrentWeek(context, currentWeek);
		}
		return currentWeek;
	}

	/** 设置当前周数,并且发周数改变的广播 */
	public static void setCurrentWeek(Context context, int currentWeek) {
		if (currentWeek <= 0) {
			currentWeek = 1;
		}
		set(getSP(context), "currentWeek", currentWeek + "");
		set(getSP(context), "weekOfYear", CalendarManager.getWeekOfYeah() + "");
		// 发广播通知Home页面要课表，时间切换
		Intent intent = new Intent(ETipsContants.Action_CurrentWeekChange);
		context.sendBroadcast(intent);
	}

	/** 是否有未读消息 */
	public static boolean isHasMsgToCheck(Context context) {
		return getSP(context).getString("Has_Msg_To_Check", "NO").contains(
				"YES");
	}

	/** 设置是否有未读消息 */
	public static void setIsHasMsgToCheck(Context context,
			boolean isHasMsgToCheck) {
		set(getSP(context), "Has_Msg_To_Check", isHasMsgToCheck ? "YES" : "NO");
	}

	/** 获得用户子系统账号 */
	public static String getSubSystemID(Context context) {
		return getSP(context).getString("User_ID_Subsystem", "");
	}

	/** 设置用户子系统账号 */
	public static void setSubSystemID(Context context, String userId) {
		set(getSP(context), "User_ID_Subsystem", userId);
	}

	/** 获得子系统密码 */
	public static String getetSubSystemPsw(Context context) {
		return getSP(context).getString("User_PSW_Subsystem", "");
	}

	/** 设置子系统密码 */
	public static void setetSubSystemPsw(Context context, String userPsw) {
		set(getSP(context), "User_PSW_Subsystem", userPsw);
	}
	/** 设置版本代号获取 用equal()来判断版本*/
	public static String getAppVersion(Context context){
		return  getSP(context).getString("version_name", "");
	}
	/** 设置版本代号*/
	public static void setAppVersion(Context context,String versionName){
		set(getSP(context),"version_name",versionName);
	}
}
