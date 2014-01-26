package com.meizhuo.etips.api;

import org.apache.http.Header;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

public class EtipsClient {
	private static int HTTP_Timeout = 10 * 1000;
	private static AsyncHttpClient client = new AsyncHttpClient();
	public static Context context;

	/**
	 * 初始化:如果需要调用登录验证记录session的函数前，必须调用这个方法，否则请求失败
	 * 
	 * @param context
	 *            Activity or Application context
	 */
	public static void init(Context context) {
		EtipsClient.context = context;
	}

	private static void initClient() {
		if (context != null)
			client.setCookieStore(new PersistentCookieStore(context));
		client.setTimeout(HTTP_Timeout);
		client.setEnableRedirects(false);
	}

	/**
	 * get method
	 * 
	 * @param url
	 *            url
	 * @param params
	 * @param responseHandler
	 */
	public static void get(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		get(url, params, null, responseHandler);
	}
    /**
     * get method with added Headers
     * @param url
     * @param params
     * @param headers
     * @param responseHandler
     */
	public static void get(String url, RequestParams params, Header[] headers,
			AsyncHttpResponseHandler responseHandler) {
		initClient();
		addHeaders(headers);
		client.get(url, params, responseHandler);

	}

	/**
	 * post method
	 * 
	 * @param url
	 * @param params
	 * @param responseHandler
	 */
	public static void post(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		post(url, params, null, responseHandler);
	}

	/**
	 * post method with added Headers
	 * 
	 * @param url
	 * @param params
	 * @param headers
	 * @param responseHandler
	 */
	public static void post(String url, RequestParams params, Header[] headers,
			AsyncHttpResponseHandler responseHandler) {
		initClient();
		addHeaders(headers);
		client.post(url, params, responseHandler);
	}

	/**
	 * add heanders!<br>
	 * after use this ,you should called removeHeaders();
	 * 
	 * @param headers
	 */
	public static void addHeaders(Header[] headers) {
		if (headers != null) {
			for (Header h : headers) {
				client.addHeader(h.getName(), h.getValue());
			}
		}
	}

	/**
	 * remove headers
	 * 
	 * @param headers
	 */
	public static void removeHeaders(Header[] headers) {
		if (headers != null) {
			for (Header h : headers) {
				client.removeHeader(h.getName());
			}
		}
	}

	/**
	 * 清除cookie<br>
	 * 建议用完一次(整个系统而言)后就抛弃cookie
	 */
	public static void removeCookie() {
		if (context != null) {
			new PersistentCookieStore(context).clear();
		}
	}
}
