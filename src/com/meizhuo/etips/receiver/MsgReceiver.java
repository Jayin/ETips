package com.meizhuo.etips.receiver;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

import com.meizhuo.etips.activities.MsgCenterActivity;
import com.meizhuo.etips.app.AppInfo;
import com.meizhuo.etips.app.Preferences;
import com.meizhuo.etips.common.ETipsContants;
import com.meizhuo.etips.model.MsgRecord;
import com.meizhuo.etips.service.ETipsCoreService;
import com.meizhuo.etips.ui.base.BaseNotification;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 * 约定服务端：1）单条消息中可以同时存在notify+system的类型
 * ,但是都会被以通知栏的方式显示出来，不推荐同时推送，而是推荐逐个推送，而且有点一定时间间隔 一面影响用户体验 2）push 类型
 * 将不会被以通知栏的方式显示出来，仅在小心中心查看，这里可以是
 * 
 */
public class MsgReceiver extends BroadcastReceiver {
	private static final String TAG = "MsgReceiver";

	private Context context;
	private int size = -1;

	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
		Bundle bundle = intent.getExtras();
		Log.d(TAG, "onReceive - " + intent.getAction() + ", extras: "
				+ printBundle(bundle));

		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			String regId = bundle
					.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			// Log.d(TAG, "接收Registration Id : " + regId);
			// send the Registration Id to your server...
		} else if (JPushInterface.ACTION_UNREGISTER.equals(intent.getAction())) {
			String regId = bundle
					.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			// Log.d(TAG, "接收UnRegistration Id : " + regId);
			// send the UnRegistration Id to your server...
		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
				.getAction())) {
			String content = bundle.getString(JPushInterface.EXTRA_MESSAGE);// 接收到推送下来的自定义消息:
			String JSONstring = bundle.getString(JPushInterface.EXTRA_EXTRA);// 接收到推送下来的自定义json
			try {
				JSONObject obj = new JSONObject(JSONstring);
				String type = obj.getString("type");
				if (type != null) {
					if (ETipsContants.TYPE_MsgCenter_System.equals(type)) {
						toSystem(obj);
					} else if (ETipsContants.TYPE_MsgCenter_Push.equals(type)) {
						toPush(obj);
					} else if (ETipsContants.TYPE_MsgCenter_Image.equals(type)) {
						toImage(obj);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
				.getAction())) {
			// Log.d(TAG, "接收到推送下来的通知");
			// int notifactionId = bundle
			// .getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
			// Log.d(TAG, "接收到推送下来的通知的ID: " + notifactionId);

		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
				.getAction())) {
			// Log.d(TAG, "用户点击打开了通知");
		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent
				.getAction())) {
			// Log.d(TAG,
			// "用户收到到RICH PUSH CALLBACK: "
			// + bundle.getString(JPushInterface.EXTRA_EXTRA));
			// 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
			// 打开一个网页等..

		} else {
			// Log.d(TAG, "Unhandled intent - " + intent.getAction());
		}

	}

	// notify to download
	private void toImage(JSONObject obj) {
		try {
			String url = obj.getString("url");
			long displayTime = Long.parseLong(obj.getString("displaytime"));
			String description = obj.getString("description");
			int continuance = Integer.parseInt(obj.getString("continuance"));
			Intent service = new Intent(context, ETipsCoreService.class);
			service.putExtra("url", url);
			service.putExtra("displayTime", displayTime);
			service.putExtra("description", description);
			service.putExtra("continuance", continuance);
			service.setAction(ETipsContants.Action_Service_Download_Pic);
			context.startService(service);
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private void toPush(JSONObject obj) {
		try {
			if (saveMessage(obj.getString("content"), obj.getString("type"))) {
				showNotification(ETipsContants.ID_Push,
						obj.getString("content"));
				Preferences.setIsHasMsgToCheck(context, true);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void toSystem(JSONObject obj) {
		try {
			if (saveMessage(obj.getString("content"), obj.getString("type"))) {
				showNotification(ETipsContants.ID_System,
						obj.getString("content"));
				Preferences.setIsHasMsgToCheck(context, true);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	// 显示通知
	private void showNotification(int id, String content) {
		BaseNotification notify = new BaseNotification(context);
		notify.setNotificationID(id);
		notify.setContentText(content);
		notify.setTicker(content);
		notify.setContentIntent(PendingIntent.getActivity(context, 0,
				new Intent(context, MsgCenterActivity.class), 0));
		notify.show();
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}

	/**
	 * 保存消息
	 * 
	 * @param content
	 * @param type
	 * @return
	 */
	public boolean saveMessage(String content, String type) {
		if (content != null) {
			ArrayList<MsgRecord> messages = AppInfo.getMessages(context);
			size = messages.size();
			MsgRecord mr = new MsgRecord();
			mr.setId(size);
			mr.setType(type);
			mr.setContent(content);
			mr.setAddTime(System.currentTimeMillis() + "");
			messages.add(mr);
			return AppInfo.setMessages(context, messages);
		}
		return false;
	}
}
