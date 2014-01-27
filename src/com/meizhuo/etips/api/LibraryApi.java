package com.meizhuo.etips.api;

import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 图书馆API<br>
 * <li>查书
 * <li>查看数的状态
 * @author Jayin Ton
 * 
 */
public class LibraryApi {

	public static String search_1 = "http://lib.wyu.edu.cn/opac/searchresult.aspx?anywords=";
	public static String search_2 = "&submit=%bc%ec+%cb%f7&ecx_cb=1&ecx=1&efz=0&dt=ALL&cl=ALL&dp=20&sf=M_PUB_YEAR&ob=DESC&sm=table&dept=ALL";
	public static String BookInfoPath = "http://lib.wyu.edu.cn/opac/bookinfo.aspx?ctrlno=";

	/**
	 * 图书检索
	 * 
	 * @param page
	 *            页码
	 * @param keyword
	 *            书名
	 * @param handler
	 */
	public void searchBook(int page, String keyword,
			AsyncHttpResponseHandler handler) {
		if (page < 1)
			page = 1;
		EtipsClient.get(search_1 + keyword + search_2 + "&page=" + page, null,
				handler);

	}
    /**
     *  检索类表->该书详情 获得检索类表中的书的详情
     * @param bookID 该书的唯一ID
     * @param handler
     */
	public void getBookStatus(String bookID, AsyncHttpResponseHandler handler) {
		if (bookID == null)
			throw new IllegalArgumentException("book id can't be null");
		EtipsClient.get(BookInfoPath + bookID,null, handler);
	}
}
