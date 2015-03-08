package com.meizhuo.etips.service;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.meizhuo.etips.activities.MsgCenterActivity;
import com.meizhuo.etips.app.AppInfo;
import com.meizhuo.etips.app.ClientConfig;
import com.meizhuo.etips.app.ImgSwitchInfo;
import com.meizhuo.etips.app.Preferences;
import com.meizhuo.etips.common.ETipsContants;
import com.meizhuo.etips.common.ETipsUtils;
import com.meizhuo.etips.common.FileUtils;
import com.meizhuo.etips.common.JSONParser;
import com.meizhuo.etips.common.StringUtils;
import com.meizhuo.etips.model.ImgInfo;
import com.meizhuo.etips.model.MsgRecord;
import com.meizhuo.etips.net.utils.TweetAPI;
import com.meizhuo.etips.ui.base.BaseNotification;

/**
 * ETips后台服务
 * @author Jayin Ton
 * 
 */
public class ETipsCoreService extends Service {
	private BroadcastReceiver mReceiver;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		startCountdown();
		registReceiver();//时间改变也去刷新
		Log.i("debug", "onCreate-->startCountdown");
		
	}
	
	

	private void registReceiver() {
		mReceiver = new ServiceBroadcastRecevier();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.TIME_SET");
		filter.addAction("android.intent.action.TIME_TICK");
		filter.addAction("android.intent.action.TIMEZONE_CHANGED");
		registerReceiver(mReceiver, filter);
	}
    //定时刷新
	private void startCountdown(){
		AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
		Intent service = new Intent(getApplicationContext(),ETipsCoreService.class);
		service.setAction(ETipsContants.Action_Service_Check_Comment);
		am.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 60*60*1000, PendingIntent.getService(getApplicationContext(), 0, service, Intent.FLAG_FROM_BACKGROUND));
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("debug", "onStartCommand-->start main work");
		if(intent != null){
			String action = intent.getAction();
			if (ETipsContants.Action_Service_Download_Pic.equals(action)) {
				downloadPic(intent);
			} else if (ETipsContants.Action_Service_Check_Comment.equals(action)) {
				checkComments();
			}
		}
		return Service.START_STICKY;
	}

	// dirty code!
	private void checkComments() {
		final AsyncHttpClient client = new AsyncHttpClient();
		if (ClientConfig.isUserLogin(getApplicationContext())) {
			RequestParams params = new RequestParams();
			params.put("author",
					ClientConfig.getUserId(getApplicationContext()));
			params.put("op", "check");
			client.get(TweetAPI.BaseUrl + "checkcomment.php", params,
					new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								byte[] data) {
							if (JSONParser.isOK(new String(data))) {
								JSONArray arr = JSONParser
										.getResponse(new String(data));
								if (arr.length() > 0) {
									int count = 0;
									try {
										count = Integer.parseInt(arr
												.getString(0));
									} catch (NumberFormatException e) {
										e.printStackTrace();
									} catch (JSONException e) {
										e.printStackTrace();
									}
									if (count > 0) {// 有消息
										client.get(TweetAPI.BaseUrl+ "getcomment.php",new RequestParams("author",ClientConfig.getUserId(getApplicationContext())),
												new AsyncHttpResponseHandler() {
													public void onSuccess(int statusCode,Header[] headers,byte[] data) {
													
														if(JSONParser.isOK(new String(data))){
															//get info
															JSONArray array = JSONParser.getResponse(new String(data));
															ArrayList<MsgRecord> msgs = AppInfo.getMessages(getApplicationContext());
															int size = msgs.size();
															msgs.clear();
															for(int i=0;i<array.length();i++){
																try {
																	JSONObject obj = array.getJSONObject(i);
																	String content = obj.getString("content");
																	String content_id = obj.getString("content_id");
																	String comment = obj.getString("comment");
																	String sendTime = obj.getString("sendTime");
																	String author = obj.getString("author");
																	String incognito = obj.getString("incognito");
																	String topic_id = obj.getString("topic_id");
																	String article_id = obj.getString("article_id");
																	String nickname = obj.getString("nickname");
																	StringBuilder sb = new StringBuilder();
																	MsgRecord mr = new MsgRecord();

																	if(!incognito.equals("0")){//1为匿名
																		sb.append("@某同学");
																		mr.setIncognito(1);
																	}else{
																		sb.append("@"+nickname);
																		mr.setIncognito(0);
																	}
																	sb.append(" 回复你:");
																	sb.append(comment+"(点击可回复)");
																	mr.setId(size++);
																	mr.setType(ETipsContants.TYPE_MsgCenter_Tweet);
																	mr.setContent(sb.toString());
																	mr.setAddTime(sendTime);
																	mr.setAuthor(author);
																	mr.setTo_comment_id(content_id);
																	mr.setTopic_id(topic_id);
																	mr.setArticle_id(article_id);
																	mr.setNickname(nickname);
																	msgs.add(mr);
																} catch (JSONException e) {
																	e.printStackTrace();
																}
															}
															ArrayList<MsgRecord> messages = AppInfo.getMessages(getApplicationContext());
															ETipsUtils.reverse(msgs);
															messages.addAll(msgs);
															AppInfo.setMessages(getApplicationContext(), messages);
															Preferences.setIsHasMsgToCheck(getApplicationContext(), true);
															String content = "你收到"+msgs.size()+"个回复";
															BaseNotification notification = new BaseNotification(getApplicationContext());
															notification.setTicker(content);
															notification.setContentText(content);
							                                notification.setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, 
									                           new Intent(getApplicationContext(),MsgCenterActivity.class), Intent.FLAG_ACTIVITY_NEW_TASK));
							                                notification.setNotificationID(ETipsContants.ID_Notify);
							                                notification.setVibrate(null);
							                                notification.show();
															//notify :message receive!
															getApplicationContext().sendBroadcast(new Intent(ETipsContants.Action_MsgReceive));
															//clean the comment in server!
															RequestParams params = new RequestParams();
															params.add("author", ClientConfig.getUserId(getApplicationContext()));
															params.add("op","clear");
													        client.get(TweetAPI.BaseUrl+ "checkcomment.php", params, new AsyncHttpResponseHandler(){
													        	public void onSuccess(int arg0, Header[] arg1, byte[] data) {
													        		if(JSONParser.isOK(new String(data))){
													        			Log.i("debug", "ETipsCoreService--->clean Comment successfully!");
													        		}else{
													        			Log.i("debug", "ETipsCoreService--->clean Comment faild!");
													        		}
													        	}

																@Override public void onFailure(
																		int arg0,
																		Header[] arg1,
																		byte[] arg2,
																		Throwable arg3) {
																	//  Auto-generated method stub
																	
																};
													        });
															 
														}
                                                      
													}

													@Override public void onFailure(
															int arg0,
															Header[] arg1,
															byte[] arg2,
															Throwable arg3) {
														// TODO Auto-generated method stub
														
													};
												});
									}
								}
							}
						}

						@Override public void onFailure(int arg0,
								Header[] arg1, byte[] arg2, Throwable arg3) {
							// Auto-generated method stub
						}

					});
		}

	}

	// 下载图片
	private void downloadPic(final Intent intent) {
		// get info
		long displayTime = intent.getLongExtra("displayTime",
				System.currentTimeMillis());
		String description = intent.getStringExtra("description") == null ? ""
				: intent.getStringExtra("description");
		int continuance = intent.getIntExtra("continuance", 1);
		final String url = intent.getStringExtra("url");

		final ImgInfo info = ImgSwitchInfo.getImgInfo(getApplicationContext());
		info.setContinuance(continuance);
		info.setDescription(description);
		info.setDisplayTime(displayTime);
		info.setUrl(url);
		info.setName(StringUtils.getFileNameFromUrl(url));
		if (url == null) {
			Log.e(ETipsContants.Debug, "ETipsCoreService::url is null");
		} else {
			final AsyncHttpClient client = new AsyncHttpClient();
			client.get(url, new AsyncHttpResponseHandler() {
				public void onSuccess(int statusCode, Header[] headers,
						byte[] data) {
					FileUtils.writeFile(data, ImgSwitchInfo
							.getImgSavePath(getApplicationContext()),
							StringUtils.getFileNameFromUrl(url));
					info.setDownloaded(true);
					ImgSwitchInfo.setImgInfo(getApplicationContext(), info);

					Log.i(ETipsContants.Debug,
							"ETipsCoreService::download finish");
				};

				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] data, Throwable err) {
					info.setDownloaded(false);
					ImgSwitchInfo.setImgInfo(getApplicationContext(), info);
					Log.e(ETipsContants.Debug,
							"ETipsCoreService::download faild ! error_code ="
									+ statusCode);
				}
			});
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
		Intent service = new Intent(getApplicationContext(),ETipsCoreService.class);
		service.setAction(ETipsContants.Action_Service_Check_Comment);
		am.cancel( PendingIntent.getService(getApplicationContext(), 0, service, Intent.FLAG_FROM_BACKGROUND));
	}

	
	class ServiceBroadcastRecevier extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			startCountdown();
		}
	}
}
