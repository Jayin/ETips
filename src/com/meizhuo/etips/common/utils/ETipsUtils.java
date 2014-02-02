package com.meizhuo.etips.common.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * 工具类
 * 
 * @author Jayin Ton
 * 
 */
public class ETipsUtils {
	/**
	 * 逆转一个队列
	 * 
	 * @param list
	 *            要逆转的队列
	 * @return 逆转后的队列
	 */
	public static <T> List<T> reverse(List<T> list) {
		List<T> newList = new ArrayList<T>();
		for (int i = list.size() - 1; i >= 0; i--) {
			newList.add(list.get(i));
		}
		return newList;
	}

	/**
	 * 根据给定的时间构造一个时间格式
	 * 
	 * @param milliseconds
	 * @return
	 */
	public static String getTimeForm(long milliseconds) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(milliseconds);
		StringBuffer sb = new StringBuffer();
		sb.append(c.get(c.YEAR)).append("-").append(c.get(c.MONTH) + 1)
				.append("-").append(c.get(c.DAY_OF_MONTH)).append(" ")
				.append(c.get(c.HOUR_OF_DAY)).append(":")
				.append(c.get(c.MINUTE));
		return sb.toString();
	}

	/**
	 * 解析接受到的通知
	 * 
	 * @param type
	 *            类型
	 * @return content 内容
	 */
	public static String parseJSON(String JSONstring, String type) {
		String value = null;
		JSONObject o;
		try {
			o = new JSONObject(JSONstring);
			value = o.getString(type);
			return value;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取当前周数 </br> 
	 * 挖了个小坑：在SharedPreference里面，里面的SharedPreference的Current_week</br>
	 * 实际上是保存当前week of year 而不是真的是一学期的当前周数 这样做的原因就是难以方便计算 当然 这里获取当前周数是封装好的
	 * @deprecated
	 * @param c
	 * @return 当前周数
	 */
//	public static int getCurrentWeek1(Context c) {
//		return SharedPreferenceHelper.getCurrentWeek(c);
//	}
	/**
	 * 获取当前周数<br>
	 * currentWeek 保存真的是当前，本学期的周数。因为currentWeek只会递增(除非你自己修改了)<br>
     * currentWeek 递增的依据，所保存的weekOfYear与当前CalendarManager.getWeekOfYeah()“不一致”就会增一(考虑到一旦跨年了，getWeekOfYeah又会从1开始)<br>
	 * @since version 2.2
	 * @param context
	 * @return 
	 */
	public static int getCurrentWeek(Context context){
		SharedPreferences sp = context.getSharedPreferences(
				ETipsContants.SharedPreference_NAME, Context.MODE_PRIVATE);
		int currentWeek = Integer.parseInt(sp.getString("currentWeek", "1"));
		int weekOfYear = Integer.parseInt(sp.getString("weekOfYear",
				CalendarManager.getWeekOfYeah() + ""));
		if(weekOfYear != CalendarManager.getWeekOfYeah()){
			currentWeek++;
			setCurrentWeek(context,currentWeek);
		} 
		return currentWeek;
	}
	/**
	 * 设置当前周数,并且发布周数切换广播
	 * @param context
	 * @param currentWeek
	 */
	public static void setCurrentWeek(Context context,int currentWeek){
		if (currentWeek <= 0) {
			currentWeek = 1;
		}
		SharedPreferences sp = context.getSharedPreferences(
				ETipsContants.SharedPreference_NAME, Context.MODE_PRIVATE);
		SharedPreferenceHelper.set(sp, "currentWeek", currentWeek + "");
		SharedPreferenceHelper.set(sp, "weekOfYear",
				CalendarManager.getWeekOfYeah() + "");
		// 发广播通知Home页面要课表，时间切换
		Intent intent = new Intent(ETipsContants.Action_CurrentWeekChange);
		context.sendBroadcast(intent);
	}

	/**
	 * 判断用户是否登录了校园资讯 or 是否登录失效 
	 * @param context
	 * @return
	 */
	public static boolean isTweetLogin(Context context) {
		SP sp = new SP(ETipsContants.SP_NAME_User, context);
		if (sp.getValue("nickname").equals("null")
				|| sp.getValue("id").equals("null"))
			return false;
		return true;
	}

	/**
	 * 是否登录超时(ETips校园资讯) 
	 * @param context
	 * @return
	 */
	public static boolean isTweetLoginTimeOut(Context context) {
		SP sp = new SP(ETipsContants.SP_NAME_User, context);
		long loginTime = Long.parseLong(sp.getValue("loginTime"));
		long loginTimeout = Long.parseLong(sp.getValue("loginTimeout"));
		if (System.currentTimeMillis() < loginTime + loginTimeout)
			return false;
		else
			return true;
	}
	/**
	 * 判断是否能够发帖（有发帖数量限制）
	 * @param context
	 * @return true if it's endable to send
	 */
	public static boolean enableSend(Context context){
		SP sp  = new SP(ETipsContants.SP_NAME_User,context);
		return Integer.parseInt(sp.getValue("MaxSend")) - Integer.parseInt(sp.getValue("SendCount")) > 0 ;
	}
	/**
	 * 发帖子的时候添加发送数量
	 * @param context
	 * @return true  if add successfully
	 */
	public static boolean addSendCount(Context context){
		if(enableSend(context)){
			SP sp  = new SP(ETipsContants.SP_NAME_User,context);
			sp.add("SendCount", (Integer.parseInt(sp.getValue("SendCount"))+1)+"");
			return true;
		}else{
			return false;
		}	
	}
}
