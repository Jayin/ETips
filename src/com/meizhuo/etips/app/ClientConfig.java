package com.meizhuo.etips.app;

import java.util.Calendar;

import com.meizhuo.etips.common.CalendarManager;
import com.meizhuo.etips.common.ETipsContants;
import com.meizhuo.etips.common.SharedPreferenceHelper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 客户端配置管理<br>
 * account,nickname,psw,id,session,loginTime,descrpiton,loginTimeout,MaxSend
 * ,SendCount(get的时候支持自动切换日期来统计，不需要自己判断本日是否够了)
 * 
 * @author Jayin Ton
 * 
 */
public class ClientConfig {
	public static final long Login_Timeout = 1000 * 60 * 60 * 24 * 60; // 2个月
	public static final long Max_Send_Count = 3;// 一天最大发送数

	/** to get SharedPreferences */
	public static SharedPreferences getSP(Context context) {
		return context.getSharedPreferences(ETipsContants.SP_NANE_ClientConfig,
				Context.MODE_PRIVATE);
	}

	/** helper method like the set() in class SharedPreferenceHelper1 */
	public static boolean set(SharedPreferences sp, String key, String value) {
		return SharedPreferenceHelper.set(sp, key, value);
	}

	// 初始化常量
	public static void init(Context context) {
		setMaxSend(context, Max_Send_Count + "");
		setLoginTimeout(context, Login_Timeout + "");
		int day_of_year = Integer.parseInt(getSP(context).getString(
				"Day_Of_Year", "0"));
		// 发送次数每日清零
		if (CalendarManager.getCalendar().get(Calendar.DAY_OF_YEAR) != day_of_year) {
			setSendCount(context, 0 + "");
			set(getSP(context), "Day_Of_Year", CalendarManager.getCalendar()
					.get(Calendar.DAY_OF_YEAR) + "");
		}
	}

	// 登出删除数据
	public static void cleanAll(Context context) {
		setAccount(context, "");
		setDescrpiton(context, "");
		setLoginTime(context, "");
		setNickName(context, "");
		setSendCount(context, "");
		setSession(context, "");
		setUserId(context, "");
		setUserPsw(context, "");
	}
    //用户是否登录了
	public static boolean isUserLogin(Context context) {
		if (getAccount(context).equals("") || getUserId(context).equals("")
				|| getUserId(context).equals("")) {
			cleanAll(context);
			return false;
		}
		return true;
	}

	/** 获取账号 */
	public static String getAccount(Context context) {
		return getSP(context).getString("account", "");
	}

	/** 设置账号 */
	public static void setAccount(Context context, String account) {
		set(getSP(context), "account", account);
	}

	/** 获取昵称 */
	public static String getNickname(Context context) {
		return getSP(context).getString("nickname", "");
	}

	/** 设置昵称 */
	public static void setNickName(Context context, String nickname) {
		set(getSP(context), "nickname", nickname);
	}

	/** 获取用户密码 */
	public static String getUserPsw(Context context) {
		return getSP(context).getString("psw", "");
	}

	/** 设置用户密码 */
	public static void setUserPsw(Context context, String psw) {
		set(getSP(context), "psw", psw);
	}

	/** 获取用户id 学号*/
	public static String getUserId(Context context) {
		return getSP(context).getString("id", "");
	}

	/** 设置用户id */
	public static void setUserId(Context context, String id) {
		set(getSP(context), "id", id);
	}

	/** 获取用户session */
	public static String getSession(Context context) {
		return getSP(context).getString("session", "");
	}

	/** 设置用户session */
	public static void setSession(Context context, String session) {
		set(getSP(context), "session", session);
	}

	/** 获取用户loginTime */
	public static String getLoginTime(Context context) {
		return getSP(context).getString("loginTime",
				System.currentTimeMillis() + "");
	}

	/** 设置用户loginTime */
	public static void setLoginTime(Context context, String loginTime) {
		set(getSP(context), "loginTime", loginTime);
	}

	/** 获取用户descrpiton */
	public static String getDescrpiton(Context context) {
		return getSP(context).getString("descrpiton", "");
	}

	/** 设置用户descrpiton */
	public static void setDescrpiton(Context context, String descrpiton) {
		set(getSP(context), "descrpiton", descrpiton);
	}

	/** 获取用户登录超时时间loginTimeout */
	public static String getLoginTimeout(Context context) {
		return getSP(context).getString("loginTimeout", Login_Timeout + "");
	}

	/** 设置用户登录超时时间LoginTimeout */
	public static void setLoginTimeout(Context context, String loginTimeout) {
		set(getSP(context), "loginTimeout", loginTimeout);
	}

	/** 获取用户一天最多可以信息发布的数目MaxSend */
	public static String getMaxSend(Context context) {
		return getSP(context).getString("MaxSend", "0");
	}

	/** 设置用户一天最多信息可以发布的数目MaxSend */
	public static void setMaxSend(Context context, String MaxSend) {
		set(getSP(context), "MaxSend", MaxSend);
	}

	/** 获取用户一天的发布信息的数目SendCount */
	public static String getSendCount(Context context) {
		return getSP(context).getString("SendCount", "0");
	}

	/** 设置用户一天发布信息的数目SendCount */
	public static void setSendCount(Context context, String SendCount) {
		set(getSP(context), "SendCount", SendCount);
	}

}
