package com.meizhuo.etips.app;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;

import com.meizhuo.etips.common.utils.DataPool;
import com.meizhuo.etips.common.utils.ETipsContants;
import com.meizhuo.etips.model.BookInfo;
import com.meizhuo.etips.model.Course;
import com.meizhuo.etips.model.MsgRecord;

/**
 * 应用信息管理<br>
 * 各种非主sharedPreferred的管理<br>
 * see class : {@linkplain#Preference}
 * @author Jayin Ton
 * @since v2.2 2014.1.31
 * 
 */
public class AppInfo {
	// 图书收藏
	public static final String DP_Name_FavouriteBook = "FavouriteBook";
	public static final String DP_Key_FavouriteBook = "FavouriteBook";
	// 课程表
	public static final String DP_Name_Course = "Course";
	public static final String DP_Key_Course = "course";
	// 消息
	public static final String DP_Name_Message = "Message";
	public static final String DP_Key_Message = "message";

	public AppInfo() {

	}

	public static DataPool getDataPool(String dataPoolName, Context context) {
		return new DataPool(dataPoolName, context);
	}

	/**
	 * 获得图书收藏表
	 * 
	 * @param context
	 * @return ---never return null ;
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<BookInfo> getFavouriteBook(Context context) {
		DataPool dp = getDataPool(DP_Name_FavouriteBook, context);
		ArrayList<BookInfo> res = (ArrayList<BookInfo>) dp
				.get(DP_Key_FavouriteBook);
		return res == null ? new ArrayList<BookInfo>() : res;
	}

	/**
	 * 保存图书收藏表
	 * 
	 * @param context
	 * @param bookInfos
	 * @return
	 */
	public static boolean setFavouriteBook(Context context,
			ArrayList<BookInfo> bookInfos) {
		DataPool dp = getDataPool(DP_Name_FavouriteBook, context);
		return dp.put(DP_Key_FavouriteBook, bookInfos);
	}

	/**
	 * 获得课程表
	 * 
	 * @param context
	 * @return null if not exist
	 */
	public static Course getCourse(Context context) {
		DataPool dp = getDataPool(DP_Name_Course, context);
		return (Course) dp.get(DP_Key_Course);
	}

	/**
	 * 保存课程表
	 * 
	 * @param context
	 * @param course
	 * @return
	 */
	public static boolean setCourse(Context context, Course course) {
		DataPool dp = getDataPool(DP_Name_Course, context);
		if (dp.put(DP_Key_Course, course)) {
			context.sendBroadcast(new Intent(ETipsContants.Action_CourseChange)); // 发送课表修改的广播！
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获得消息列表
	 * 
	 * @param context
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<MsgRecord> getMessages(Context context) {
		DataPool dp = getDataPool(DP_Name_Message, context);
		ArrayList<MsgRecord> res = (ArrayList<MsgRecord>) dp
				.get(DP_Key_Message);
		return res == null ? new ArrayList<MsgRecord>() : res;
	}

	/**
	 * 保存消息
	 * 
	 * @param context
	 * @param messages
	 * @return
	 */
	public static boolean setMessages(Context context,
			ArrayList<MsgRecord> messages) {
		DataPool dp = getDataPool(DP_Name_Message, context);
		return dp.put(DP_Key_Message, messages);
	}

}
