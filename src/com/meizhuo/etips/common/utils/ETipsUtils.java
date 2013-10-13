package com.meizhuo.etips.common.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
     * @param type 类型
     * @return content 内容
     */
	public static String parseJSON(String JSONstring,String type){
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
}
