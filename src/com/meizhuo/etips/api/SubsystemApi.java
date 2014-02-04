package com.meizhuo.etips.api;

import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.message.BasicHeader;

import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.meizhuo.etips.common.L;
import com.meizhuo.etips.model.ResponseHandler;

/**
 * 子系统API funcntion: 登录 获取课表 获取成绩
 * <li>登录
 * <li>获取课表
 * <li>获取成绩列表
 * <p>NOTE:</p>
 * 登录时： 注意：有时候使用API时，Android提供的Apache HttpClient 版本不同于my eclipse Lib导入的的版本
 * Android 的http client 会自动设置 重定向 ； 但my eclipse 中的lib 里的版本不会自动重定向！
 * 以302为判断标准，这是在my eclipse 中HttpClient 4.25版本 以200为判断标准，这是在Elipse（Android）原生提供的
 * ，android环境下应该用这个来判断，但有个问题就是 如果是密码错误，返回的StatusCode 也是200，因此必须用以302为判断标准
 * 
 * 
 * @author Jayin Ton
 * 
 */
public class SubsystemApi {
	private String userid;
	private String psw;

	/**
	 * 
	 * @param userid
	 *            学号
	 * @param psw
	 *            密码
	 */
	public SubsystemApi(String userid, String psw) {
		this.userid = userid;
		this.psw = psw;
	}

	/**
	 * login
	 * 
	 * @param handler
	 */
	public void login(final AsyncHttpResponseHandler handler) {
		final RequestParams params = new RequestParams();
		params.add("UserCode", userid);
		params.add("UserPwd", psw);
		params.add("Submit", "%CC%E1+%BD%BB");
		EtipsClient.get("http://jwc.wyu.edu.cn/student/rndnum.asp", null,
				new ResponseHandler() {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] data) {
						if (statusCode == HttpStatus.SC_OK) {
							String randomNumber = null;
							for (Header h : headers) {
								if ("Set-Cookie".equals(h.getName())) {
									randomNumber = h.getElements()[0]
											.getValue();
								}
							}
							params.add("Validate", randomNumber);
                            //NOTE Here!!
							final Header[] hs = new Header[] { new BasicHeader( "Referer", "http://jwc.wyu.edu.cn/student/body.htm") };
							EtipsClient.post( "http://jwc.wyu.edu.cn/student/logon.asp", params, hs, new ResponseHandler() {

										@Override
										public void onSuccess(int statusCode, Header[] headers, byte[] data) {
											if (statusCode == HttpStatus.SC_OK) {
												L.e("login successfully!");
												EtipsClient.removeHeaders(hs);
												handler.onSuccess(statusCode,
														headers, data);
											} else {
												L.e("login faild!");
												EtipsClient.removeHeaders(hs);
												handler.onFailure(statusCode,headers, data, null);
											}
										}

										@Override
										public void onFailure(int statusCode, Header[] headers, byte[] data, Throwable err) {
											L.e("login faild with error !");
											handler.onFailure(statusCode,
													headers, data, err);
										}
									});
						} else {
							handler.onFailure(statusCode, headers, data, null);
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] data, Throwable err) {
						handler.onFailure(statusCode, headers, data, err);
						L.e("get random number faild!");
					}
				});
	}

	/**
	 * 获得课程表
	 * 
	 * @param handler
	 */
	public void getCourse(AsyncHttpResponseHandler handler) {
		EtipsClient.get("http://jwc.wyu.edu.cn/student/f3.asp", null, handler);
	}
    /**
     * 获得成绩列表
     * @param handler
     */
	public void getScoreList(final AsyncHttpResponseHandler handler){
		EtipsClient.get("http://jwc.wyu.edu.cn/student/createsession_a.asp", null, new ResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] data) {
			  EtipsClient.get("http://jwc.wyu.edu.cn/student/createsession_b.asp", null, new ResponseHandler() {
				
				@Override
				public void onSuccess(int statusCode, Header[] headers, byte[] data) {
				    //NOTE Here!!
					final Header[] hs = new Header[]{new  BasicHeader("Referer","http://jwc.wyu.edu.cn/student/menu.asp")};
					EtipsClient.get("http://jwc.wyu.edu.cn/student/f4_myscore.asp", null, hs, new ResponseHandler() {
						
						@Override
						public void onSuccess(int statusCode, Header[] headers, byte[] data) {
							EtipsClient.removeHeaders(hs);
							handler.onSuccess(statusCode, headers, data);
						}
						
						@Override
						public void onFailure(int statusCode, Header[] headers, byte[] data,
								Throwable err) {
							EtipsClient.removeHeaders(hs);
							handler.onFailure(statusCode, headers, data, err);
						};
					});
				}
				
				@Override
				public void onFailure(int statusCode, Header[] headers, byte[] data,
						Throwable err) {
					handler.onFailure(statusCode, headers, data, err);
				}
			});
				
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] data,
					Throwable err) {
				handler.onFailure(statusCode, headers, data, err);
			}
		});
	}
}
