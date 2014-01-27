package com.meizhuo.etips.api;

import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 邑大新闻API 
 * <li> 获取新闻列表
 * <li> 获取新闻详情
 * @author Jayin Ton
 * 
 */
public class WyuNewsApi {
	/**
	 * 以下这两个是用于构建新闻URL
	 */
	public static String SchoolNews = "http://www.wyu.cn/news/default.asp";
	public static String SchoolNewsDetail = "http://www.wyu.cn/news/";
	

	public WyuNewsApi() {

	}

	/**
	 * 获得学校新闻列表
	 * 
	 * @param page
	 * @param handler
	 */
	public void getSchoolNews(int page, AsyncHttpResponseHandler handler) {
		if (page <= 0)
			page = 1;
		EtipsClient.get(SchoolNews + "?page=" + page, null, handler);
	}
    /**
     * 通过SchoolNews。linkPath来获取该新闻的详细内容
     * @param linkPath
     * @param handler
     */
	public void getNewsDetail(String linkPath, AsyncHttpResponseHandler handler) {
		if (linkPath == null)
			throw new IllegalArgumentException("link path can't not be null");
		EtipsClient.get(SchoolNewsDetail + linkPath, null, handler);
	}

}
