package com.meizhuo.etips.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 访问路径（URL）构建工具类
 * 
 * @author Jayin Ton 创建一个路径 include:
 *         1）搜索路径http://lib.wyu.edu.cn/opac/searchresult.aspx?anywords
 *         =XXXXX&submit
 *         =%bc%ec+%cb%f7&ecx_cb=1&ecx=1&efz=0&dt=ALL&cl=ALL&dp=20&sf=
 *         M_PUB_YEAR&ob=DESC&sm=table&dept=ALL XXXXX 代表搜索的keyword 2）换页路径
 *         NOTe:路径跟上式相似在末端添加上&page=XXXX
 *         3）评论路径http://lib.wyu.edu.cn/opac/common.aspx?ctrlno=AAAAA&id=49889
 *         AAAAA为书本编号 BookID /* id=49889 换成id=0 即为匿名评论
 */
public class PathBuilder {
	public static String search_1 = "http://lib.wyu.edu.cn/opac/searchresult.aspx?anywords=";
	public static String search_2 = "&submit=%bc%ec+%cb%f7&ecx_cb=1&ecx=1&efz=0&dt=ALL&cl=ALL&dp=20&sf=M_PUB_YEAR&ob=DESC&sm=table&dept=ALL";
	public static String changePage_1 = "&page=";
	public static String comment_1 = "http://lib.wyu.edu.cn/opac/common.aspx?ctrlno=";
	public static String comment_2 = "&id=";
	public static String BookInfoPath = "http://lib.wyu.edu.cn/opac/bookinfo.aspx?ctrlno=";
	public static final int PREVIOUS = 1;
	public static final int NEXT = 2;
	public static final int CURRENT = 3;
	/**
	 * 以下这两个是用于构建新闻URL
	 */
	public static String SchoolNews = "http://www.wyu.cn/news/default.asp";
	public static String SchoolNewsDetail = "http://www.wyu.cn/news/";

	/**
	 * 构建搜索的路径
	 * 
	 * @param keyword
	 *            要搜索的内容
	 * @return URL
	 */
	public static String getSearchPath(String keyword) {
		return getSearchPath(keyword, 1);
	}

	/**
	 * 构建搜索的路径
	 * 
	 * @param keyword
	 *            要搜索的内容
	 * @param page
	 *            第几页
	 * @return URL
	 */
	public static String getSearchPath(String keyword, int page) {
		String keywordEncode = "";
		try {
			keywordEncode = URLEncoder.encode(keyword, "gbk");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return search_1 + keywordEncode + search_2 + "&page=" + String.valueOf(page);
	}

	/**
	 * 
	 * 构建查看书本信息的路径
	 * 
	 * @param bookID
	 *            书的编号 唯一
	 * @return 查看书本信息的URL
	 */
	public static String getBookInfoPath(String bookID) {
		return BookInfoPath + bookID;
	}

	/**
	 * 构建评论图书的路径 这里是匿名的 ,默认id=0
	 * 
	 * @param bookID
	 *            书本的编号 唯一
	 * @return URL
	 */
	public static String getCommetPath(String bookID) {
		return getCommetPath(bookID, "0");
	}

	/**
	 * 构建评论图书的路径
	 * 
	 * @param bookID
	 *            书本的编号 唯一
	 * @param id
	 *            代表用户的ID
	 * @return URL
	 */
	public static String getCommetPath(String bookID, String id) {
		return comment_1 + bookID + comment_2 + id;
	}

	/**
	 * 构建学校主页新闻URL 第一页
	 * 
	 * @return 学校主页新闻URL
	 */
	public static String getSchoolNewsPath() {
		return getSchoolNewsPath(1);
	}

	/**
	 * 构建学校主页新闻URL
	 * 
	 * @param page
	 *            第几页学校主页新闻
	 * @return 第page页学校主页新闻URL
	 */
	public static String getSchoolNewsPath(int page) {
		if (page <= 0)
			page = 1;
		return SchoolNews + "?page=" + String.valueOf(page);
	}

	/**
	 * 构建新闻详细内容URL
	 * 
	 * @param link
	 *            该新闻的相对URL（在主页第一次获取）
	 * @return 完整的详细新闻URL
	 */
	public static String getSchoolNewsDetailPath(String link) {
		return SchoolNewsDetail + link;
	}
    /**
     * 根据title，构建单条学生手册内容的路径
     * @param title 
     * @return  单条学生手册内容的路径 用于WebView.loadURL(url)
     */
	public static String getManualContentPath(int position){
		return "file:///android_asset/manual/"+position+".htm";
	}
}