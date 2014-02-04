package com.meizhuo.etips.ui.base;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;

import com.meizhuo.etips.activities.R;

/**
 * 先实例化一个对象，然后才setContentText() .setContextIntent().setID(); 最后才show()；
 * 一般情况下不用自己设置其他的东西 这里提供getContentIntent（）方法 ，方便获得一个PendingIntent
 * 
 * @author Jayin Ton
 * 
 */
public class BaseNotificationCompat extends NotificationCompat.Builder {
	private static NotificationCompat.Builder mBuilder = null;
	private int id;
	private Context context;

	/**
	 * 默认构造方式
	 * 
	 * @param activity
	 */
	protected BaseNotificationCompat(Context context) {
		super(context);
		this.context = context;
		mBuilder = new NotificationCompat.Builder(context);
		mBuilder.setContentTitle("ETips")
				.setSmallIcon(R.drawable.icon_small)
				.setAutoCancel(true)
				.setVibrate(new long[] { 100, 300, 500, 300 })
				.setSound(
						Uri.parse("android.resource://"
								+ context.getPackageName() + "/"
								+ R.raw.msg_notification));

	}

	/**
	 * 设置震动
	 * 
	 * @param pattern
	 */
	public NotificationCompat.Builder setVibrate(long[] pattern) {
		return this.mBuilder.setVibrate(pattern);
	}

	/**
	 * 设置声音
	 * 
	 * @param uri
	 * @return NotificationCompat.Builder
	 */
	public NotificationCompat.Builder setVibrate(Uri uri) {
		return this.mBuilder.setSound(uri);
	}

	/**
	 * 设置消息内容
	 * 
	 * @param content
	 * @return
	 */
	public NotificationCompat.Builder setContentText(CharSequence content) {
		return mBuilder.setContentText(content);
	}

	/**
	 * 设置内容标题
	 * 
	 * @param title
	 */
	public NotificationCompat.Builder setContentTitle(CharSequence title) {
		return mBuilder.setContentTitle(title);
	}

	/**
	 * 100*100最佳
	 * 
	 * @param resourceID
	 *            资源id
	 */
	public NotificationCompat.Builder setSmallIcon(int resourceID) {
		return mBuilder.setSmallIcon(resourceID);
	}

	/**
	 * 设置ContentIntent
	 */
	public NotificationCompat.Builder setContentIntent(
			PendingIntent pendingIntent) {
		return this.mBuilder.setContentIntent(pendingIntent);
	}

	/**
	 * 获得实例
	 * 
	 * @return
	 */
	public static NotificationCompat.Builder getInstance(Context context) {
		return new BaseNotificationCompat(context);
	}

	/**
	 * 构造ContentIntent
	 * 
	 * @param packageContext
	 *            from 在哪个上下文对象跳转
	 * @param cls
	 *            target启动那个activity
	 * @return PendingIntent
	 */
	public static PendingIntent getContentIntent(Context packageContext,
			Class<?> cls) {
		return PendingIntent.getActivity(packageContext, 0, new Intent(
				packageContext, cls), 0);
	}

	/**
	 * 设置ID
	 * 
	 * @param id
	 */
	public void setID(int id) {
		this.id = id;
	}

	public void show() {
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(id, mBuilder.build());
	}
}
