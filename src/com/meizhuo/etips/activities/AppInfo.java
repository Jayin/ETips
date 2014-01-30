package com.meizhuo.etips.activities;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;

import com.meizhuo.etips.common.utils.DataPool;
import com.meizhuo.etips.model.BookInfo;

/**
 * 应用程序信息管理
 * 
 * @author Jayin Ton
 * @since v2.2 2014.1.31
 * 
 */
public class AppInfo {
	// 图书收藏
	public static final String DP_Name_FavouriteBook = "FavouriteBook";
	public static final String DP_Key_FavouriteBook = "FavouriteBook";

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
		return res == null ?  new ArrayList< BookInfo>():res ;
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
}
