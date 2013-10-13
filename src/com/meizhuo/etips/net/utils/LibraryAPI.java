package com.meizhuo.etips.net.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import com.meizhuo.etips.common.utils.Elog;
import com.meizhuo.etips.common.utils.PathBuilder;
import com.meizhuo.etips.model.BookBorrowRecord;
import com.meizhuo.etips.model.BookInfo;
import com.meizhuo.etips.model.BookStatus;
import com.meizhuo.etips.model.SearchBookResult;

/**
 * 图书馆API 1)登录、查阅当前借阅状态 2)搜索图书 后续 ： 增加续借功能 修改用户密码
 * 
 * 登录时：
 * 注意：有时候使用API时，Android提供的Apache HttpClient 版本不同于my eclipse Lib导入的的版本 Android
 * 的http client 会自动设置 重定向 ； 但my eclipse 中的lib 里的版本不会自动重定向！ 以302为判断标准，这是在my
 * eclipse 中HttpClient 4.25版本 以200为判断标准，这是在Elipse（Android）原生提供的
 * ，android环境下应该用这个来判断，但有个问题就是 如果是密码错误，返回的StatusCode 也是200，因此必须用以302为判断标准
 * 
 * 
 * @author Jayin Ton
 * 
 */
public class LibraryAPI {
	private String userid = null;
	private String userpsw = null;
	/**
	 * CookieStore 保存着cookie comment()评论接口非常依赖者cookie，实际上
	 * 他不认URL上的userid，只认cookie~
	 */
	private CookieStore mCookieStore = null;
	private SearchBookResult searchBookResult;

	/**
	 * 不能使用这个构造方法！！否则不能成功登陆
	 */
	public LibraryAPI() {
		this("0", "0");
	}

	/**
	 * 必须使用这个构造方法来初始化
	 * 
	 * @param _userid
	 *            账号
	 * @param _userpsw
	 *            密码
	 */
	public LibraryAPI(String _userid, String _userpsw) {
		userid = _userid;
		userpsw = _userpsw;
		searchBookResult = new SearchBookResult();
	}
	/**
	 * 设置账号 密码
	 * @param _userID
	 * @param _userPSW
	 */
	public void setUserData(String _userID, String _userPSW) {
		userid = _userID;
		userpsw = _userPSW;

	}
	/**
	 * 
	 * @return true if login successfully
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public boolean Login() throws ClientProtocolException, IOException {
		// 设置httpClient参数，不自动重定向
		HttpParams httpParams = new BasicHttpParams();
		HttpClientParams.setRedirecting(httpParams, false);
		DefaultHttpClient client = new DefaultHttpClient(httpParams);
		HttpPost post = new HttpPost("http://lib.wyu.edu.cn/opac/login.aspx");
		HttpResponse response = null;
		String __EVENTTARGET = "";
		String __EVENTVALIDATION = "/wEWBQL99Iu/DwKOmK5RApX9wcYGAsP9wL8JAqW86pcI4PPe9nq/n81bkQRHtZ4j0i2bqXg=";
		String __VIEWSTATE = "/wEPDwULLTE0MjY3MDAxNzcPZBYCZg9kFgoCAQ8PFgIeCEltYWdlVXJsBRt+XGltYWdlc1xoZWFkZXJvcGFjNGdpZi5naWZkZAICDw8WAh4EVGV4dAUn5LqU6YKR5aSn5a2m5Zu+5Lmm6aaG5Lmm55uu5qOA57Si57O757ufZGQCAw8PFgIfAQUcMjAxM+W5tDA35pyIMDbml6UgIOaYn+acn+WFrWRkAgQPZBYEZg9kFgQCAQ8WAh4LXyFJdGVtQ291bnQCCBYSAgEPZBYCZg8VAwtzZWFyY2guYXNweAAM55uu5b2V5qOA57SiZAICD2QWAmYPFQMOY2xzYnJvd3NlLmFzcHgADOWIhuexu+WvvOiIqmQCAw9kFgJmDxUDDmJvb2tfcmFuay5hc3B4AAzor7vkuabmjIflvJVkAgQPZBYCZg8VAwl4c3RiLmFzcHgADOaWsOS5pumAmuaKpWQCBQ9kFgJmDxUDFHJlYWRlcnJlY29tbWVuZC5hc3B4AAzor7vogIXojZDotK1kAgYPZBYCZg8VAxNvdmVyZHVlYm9va3NfZi5hc3B4AAzmj5DphpLmnI3liqFkAgcPZBYCZg8VAxJ1c2VyL3VzZXJpbmZvLmFzcHgAD+aIkeeahOWbvuS5pummhmQCCA9kFgJmDxUDFWh0dHA6Ly9saWIud3l1LmVkdS5jbgAP5Zu+5Lmm6aaG6aaW6aG1ZAIJD2QWAgIBDxYCHgdWaXNpYmxlaGQCAw8WAh8CZmQCAQ9kFgQCAw9kFgQCAQ8PZBYCHgxhdXRvY29tcGxldGUFA29mZmQCBw8PFgIfAWVkZAIFD2QWBgIBDxBkZBYBZmQCAw8QZGQWAWZkAgUPD2QWAh8EBQNvZmZkAgUPDxYCHwEF/gFDb3B5cmlnaHQgJmNvcHk7MjAwOC0yMDA5LiBTVUxDTUlTIE9QQUMgNC4wMS4gIEFsbCByaWdodHMgcmVzZXJ2ZWQuPGJyIC8+PHNjcmlwdCBsYW5ndWFnZT0iamF2YXNjcmlwdCIgdHlwZT0idGV4dC9qYXZhc2NyaXB0IiBzcmM9Imh0dHA6Ly9qcy51c2Vycy41MS5sYS8zMjI2ODYwLmpzIj48L3NjcmlwdD7niYjmnYPmiYDmnInvvJrkupTpgpHlpKflrablm77kuabppoYgRS1tYWlsOmhsaEB3eXUuZWR1LmNu77yM6IGU57O755S16K+d77yaNjYxMWRkZMkvhDEg7qh+n+8HSV6Wc9VY9MHo";
		String ctl00$ContentPlaceHolder1$btnLogin_Lib = "登录";
		String ctl00$ContentPlaceHolder1$txtlogintype = "0";
		String ctl00$ContentPlaceHolder1$txtPas_Lib = userpsw; // psw
		String ctl00$ContentPlaceHolder1$txtUsername_Lib = userid;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("__EVENTTARGET", __EVENTTARGET));
		params.add(new BasicNameValuePair("__EVENTVALIDATION",
				__EVENTVALIDATION));
		params.add(new BasicNameValuePair("__VIEWSTATE", __VIEWSTATE));
		params.add(new BasicNameValuePair(
				"ctl00$ContentPlaceHolder1$btnLogin_Lib",
				ctl00$ContentPlaceHolder1$btnLogin_Lib));
		params.add(new BasicNameValuePair(
				"ctl00$ContentPlaceHolder1$txtlogintype",
				ctl00$ContentPlaceHolder1$txtlogintype));
		params.add(new BasicNameValuePair(
				"ctl00$ContentPlaceHolder1$txtPas_Lib",
				ctl00$ContentPlaceHolder1$txtPas_Lib));
		params.add(new BasicNameValuePair(
				"ctl00$ContentPlaceHolder1$txtUsername_Lib",
				ctl00$ContentPlaceHolder1$txtUsername_Lib));
		HttpEntity entity = new UrlEncodedFormEntity(params, "UTF-8");// may not
																		// to
																		// decode?
		post.setEntity(entity);
		response = client.execute(post);
		if (response.getStatusLine().getStatusCode() == 302) { // 重定向,跳转成功说明登录成功
			String s = EntityUtils.toString(response.getEntity(), "utf-8");
			mCookieStore = client.getCookieStore();
			System.out.println("Library Login successfully");
			client.getConnectionManager().shutdown();
			return true;
		} else {
			mCookieStore = client.getCookieStore();
			System.out.println("Library login faild!");
		}
		client.getConnectionManager().shutdown();
		return false;
	}

	/**
	 * 查询个人借阅状态
	 * 
	 * @return 借书记录列表
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public List<BookBorrowRecord> getBookBorrowRecord()
			throws ClientProtocolException, IOException {
		List<BookBorrowRecord> list = new ArrayList<BookBorrowRecord>();
		DefaultHttpClient client = new DefaultHttpClient();
		client.setCookieStore(mCookieStore);
		HttpResponse response = client.execute(new HttpGet(
				"http://lib.wyu.edu.cn/opac/user/bookborrowed.aspx"));
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			String html = EntityUtils.toString(response.getEntity(), "utf-8");
			client.getConnectionManager().shutdown();
			return HtmlParser.parseHtmlForBookBorrowRecord(html, list);
		}
		client.getConnectionManager().shutdown();
		return list;
	}

	/**
	 * 图书检索
	 * 
	 * @param keyword
	 *            要检索的图书名
	 * @param page
	 *            第几页（第一页为1）
	 * @return 检索结果（匹配的书本列表）
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public List<BookInfo> searchBook(String keyword, int page)
			throws ClientProtocolException, IOException {
		int flag = 0;
		if (searchBookResult.keyword.equals(keyword)) {
			if (searchBookResult.getResultList(page) != null) {
				return searchBookResult.getResultList(page);
			} else
				flag = 1;
		} else {
			flag = 2;
		}
		if (flag == 2)
			searchBookResult = new SearchBookResult();
		searchBookResult.keyword = keyword;
		List<BookInfo> list = new ArrayList<BookInfo>();
		String url = PathBuilder.getSearchPath(keyword, page);
		DefaultHttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(new HttpGet(url));
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			String html = EntityUtils.toString(response.getEntity(), "utf-8");
			client.getConnectionManager().shutdown();
			int totalResult = HtmlParser.parseHtmlForSearch(html, list);
			searchBookResult.addList(page, list);
			searchBookResult.setTotalResult(totalResult);
			return list;
		} else {
			client.getConnectionManager().shutdown();
			// searchBookResult.addList(page, list); 无法访问就是没有加载过！
			return list;
		}

	}

	/**
	 * 获得结果列表中的 总共有多少页
	 * 
	 * @param keyword
	 *            要查询的关键字
	 * @return
	 */
	public int getTotalPage(String keyword) {
		if (searchBookResult.keyword.equals(keyword))
			return searchBookResult.getTotalPage();
		return 0;
	}

	/**
	 * 检索类表->该书详情 获得检索类表中的书的详情
	 * 
	 * @param bookID
	 *            该书的唯一ID
	 * @return 该书的详细情况
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public List<BookStatus> getBookStatus(String bookID)
			throws ClientProtocolException, IOException {
		List<BookStatus> list = new ArrayList<BookStatus>();
		String url = PathBuilder.getBookInfoPath(bookID);
		DefaultHttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(new HttpGet(url));
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			String html = EntityUtils.toString(response.getEntity(), "utf-8");
			client.getConnectionManager().shutdown();
			return HtmlParser.parseHtmlForBookStatus(html, list);
		} else {
			client.getConnectionManager().shutdown();
			return list;
		}
	}

	/**
	 * 评论图书接口 CookieStore 保存着cookie comment()评论接口非常依赖者cookie，实际上
	 * 他不认URL上的userid，只认cookie~ 所以必须要登录再评论！！ 该接口可以评论任何书，但是评论没有借阅过or没有阅读过的书是毫无意义的
	 * 所以开发者自行维护评论的图书是 1)正借阅的 2）曾借阅的
	 * 
	 * @param bookID
	 *            评论图书的ID
	 * @param userID
	 *            评论人的ID
	 * @param comment
	 *            comment_words
	 * @return true if comment successfully
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public boolean comment(String bookID, String userID, String comment)
			throws ClientProtocolException, IOException {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(PathBuilder.getCommetPath(bookID, userID));
		System.out.println(PathBuilder.getCommetPath(bookID, userID));
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String __EVENTVALIDATION = "/wEWAwLeh4bkDwK21ooMAoznisYG0dvuv3jZRwNjVQ6uPzp8XTGJWGk=";
		String __VIEWSTATE = "/wEPDwUKLTQ5MDA4NzU5MmRksMQZ9kEaRpoquE02WFkOgeYwiBg=";
		String Button1 = "提交";
		String txtreasons = comment;
		params.add(new BasicNameValuePair("__EVENTVALIDATION",
				__EVENTVALIDATION));
		params.add(new BasicNameValuePair("__VIEWSTATE", __VIEWSTATE));
		params.add(new BasicNameValuePair("Button1", Button1));
		params.add(new BasicNameValuePair("txtreasons", txtreasons));
		HttpEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
		post.setEntity(entity);
		client.setCookieStore(mCookieStore); // key step
		HttpResponse response = client.execute(post);
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			mCookieStore = client.getCookieStore();
			System.out.println("comment successfully!");
			client.getConnectionManager().shutdown();
			return true;
		} else {
			System.out.println("comment faild!");
			client.getConnectionManager().shutdown();
			return false;
		}
	}

	/**
	 * 匿名评论，userID=0 也是一个用户! CookieStore 保存着cookie comment()评论接口非常依赖者cookie，实际上
	 * 他不认URL上的userid，只认cookie~ 所以必须要登录再评论！！
	 * 
	 * @param bookID
	 *            评论图书的ID
	 * @param comment
	 *            comment_words
	 * @return true if comment successfully
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public boolean comment(String bookID, String comment)
			throws ClientProtocolException, IOException {
		return comment(bookID, "0", comment);
	}

	/**
	 * 获取借书历史列表 这里一般用于评论一本书时，判断该书是否借过~
	 * 
	 * @param list
	 *            要返回的借书列表
	 * @return 借书列表
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public List<BookBorrowRecord> getBorrowHistroy(List<BookBorrowRecord> list)
			throws ClientProtocolException, IOException {
		DefaultHttpClient client = new DefaultHttpClient();
		if (mCookieStore != null)
			client.setCookieStore(mCookieStore);
		HttpResponse response = client.execute(new HttpGet(
				"http://lib.wyu.edu.cn/opac/user/bookborrowedhistory.aspx"));
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			String html = EntityUtils.toString(response.getEntity());
			client.getConnectionManager().shutdown();
			return HtmlParser.parseHtmlForBorrowHistroy(html, list);
		}
		client.getConnectionManager().shutdown();
		return list;
	}
}
