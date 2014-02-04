package com.meizhuo.etips.receiver;

import java.util.ArrayList;

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
import com.meizhuo.etips.common.ETipsUtils;
import com.meizhuo.etips.model.MsgRecord;
import com.meizhuo.etips.ui.base.BaseNotificationCompat;

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
			String content = bundle.getString(JPushInterface.EXTRA_MESSAGE);
			String JSONstring = bundle.getString(JPushInterface.EXTRA_EXTRA);
			// Log.d(TAG, "接收到推送下来的自定义消息: " + content);
			// Log.d(TAG, "接收到推送下来的自定义json: " + JSONstring);
			// System.out.println("content:" + content);
			// System.out.println("接收到推送下来的自定义json: " + JSONstring);
			// Elog.i("接收到推送下来的自定义json: " + JSONstring);
			if (JSONstring != null) {
				// get content of notify/push/system
				String notify = ETipsUtils.parseJSON(JSONstring,
						ETipsContants.TYPE_MsgCenter_Notify);
				String push = ETipsUtils.parseJSON(JSONstring,
						ETipsContants.TYPE_MsgCenter_Push);
				String system = ETipsUtils.parseJSON(JSONstring,
						ETipsContants.TYPE_MsgCenter_System);
				BaseNotificationCompat notification = (BaseNotificationCompat) BaseNotificationCompat
						.getInstance(context);
				notification.setContentIntent(BaseNotificationCompat
						.getContentIntent(context, MsgCenterActivity.class));
				notification.setContentTitle("ETips消息中心");
				if (saveMessage(notify, ETipsContants.TYPE_MsgCenter_Notify)) {
					saveToSharedPerference(context); // 通知用户有新消息
					notification.setID(ETipsContants.ID_Notify);
					notification.setContentText(notify);
					notification.show();
				}
				if (saveMessage(push, ETipsContants.TYPE_MsgCenter_Push)) {
					saveToSharedPerference(context);
					// not to create a notification
				}
				if (saveMessage(system, ETipsContants.TYPE_MsgCenter_System)) {
					saveToSharedPerference(context);
					notification.setID(ETipsContants.ID_System);
					notification.setContentText(system);
					notification.show();
				}
			}

		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
				.getAction())) {
			// Log.d(TAG, "接收到推送下来的通知");
			int notifactionId = bundle
					.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
			// Log.d(TAG, "接收到推送下来的通知的ID: " + notifactionId);

		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
				.getAction())) {
			// Log.d(TAG, "用户点击打开了通知");

			// 打开自定义的Activity
			Intent i = new Intent(context, MsgCenterActivity.class);
			i.putExtras(bundle);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);

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
	 * 保存到数据库
	 * 分离 since v2.2
	 * @param content
	 * @param type
	 * @return
	 */
//	public boolean saveToDB(String content, String type) {
//		if (content != null) {
//			final MsgCenterDAO dao = new MsgCenterDAO(context);
//			if (size == -1)
//				size = dao.getRowCount();
//			long currentTime = System.currentTimeMillis();
//			final ContentValues cv = new ContentValues();
//			cv.put("id", size++);
//			cv.put("type", type);
//			cv.put("content", content);
//			cv.put("addTime", currentTime);
//			new Thread(new Runnable() {
//				@Override
//				public void run() {
//					int i = 1;
//					while (!dao.add(cv) && i < 4)
//						i++;
//				}
//			}).start();
//			return true;
//		}
//		return false;
//	}
    /**
     * 保存消息
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

	public void saveToSharedPerference(Context context) {
//		SharedPreferences sp = context.getSharedPreferences(
//				ETipsContants.SharedPreference_NAME, Context.MODE_PRIVATE);
//		SharedPreferenceHelper.set(sp, "Has_Msg_To_Check", "YES");
		Preferences.setIsHasMsgToCheck(context, true);
	}

}
