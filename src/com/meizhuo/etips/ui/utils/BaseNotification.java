package com.meizhuo.etips.ui.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;

import com.meizhuo.etips.activities.R;

 

/**
 * <strong>2.0后推荐使用这个</strong></br>
 * Notification轻量封装,常用的都在这里了</br>
 * How to use ?</br> 1.先创建对象，默认是ETips icon，可以自动取消(点击后显示Intent然后消失),震动 and
 * 铃声，setSmallIcon(null);setVibrate(new long[0])</br>
 * 2.call : setContenText() / setContentIntent() </br>
 * 3.call : 必须的 setNotificationID()  (虽然有默认值)</br>
 * 4.call : show();</br>
 * 
 * <strong>since 2.0</strong>
 * @author Jayin Ton
 * 
 */
public class BaseNotification extends NotificationCompat.Builder {
	private Context context;
	private int NotificationID = 0x0000f4;

	public BaseNotification(Context context) {
		super(context);
		this.context = context;

		this.setContentTitle("ETips")
				.setSmallIcon(
						R.drawable.icon_small)
				.setAutoCancel(true)
				.setVibrate(new long[] { 100, 300, 500, 300 })
				.setSound(
						Uri.parse("android.resource://"
								+ context.getPackageName() + "/"
								+ R.raw.msg_notification));
	}

	/**
	 * 显示Notification
	 */
	public void show() {
		NotificationManager manager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(NotificationID, this.build());
	}
    /**
     * 取消显示当前的Notification
     */
	public void cancle() {
		((NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE))
				.cancel(NotificationID);
	}

	/**
	 * 设置Notification ID
	 * 
	 * @param id
	 */
	public void setNotificationID(int id) {
		this.NotificationID = id;
	}
    /**
     * 设置点击时的Intent
     */
	@Override
	public Builder setContentIntent(PendingIntent intent) {
		return super.setContentIntent(intent);
	}
   /**
    * 设置通知消失时的Intent
    */
	@Override
	public Builder setDeleteIntent(PendingIntent intent) {
		return super.setDeleteIntent(intent);
	}

	@Override
	public Builder setAutoCancel(boolean autoCancel) {
		return super.setAutoCancel(autoCancel);
	}

	@Override
	public Builder setContentText(CharSequence text) {
		return super.setContentText(text);
	}

	@Override
	public Builder setContentTitle(CharSequence title) {
		return super.setContentTitle(title);
	}

	@Override
	public Builder setSmallIcon(int icon) {
		return super.setSmallIcon(icon);
	}

	@Override
	public Builder setTicker(CharSequence tickerText) {

		return super.setTicker(tickerText);
	}

	@Override
	public Builder setVibrate(long[] pattern) {
		return super.setVibrate(pattern);
	}

	@Override
	public Builder setWhen(long when) {
		return super.setWhen(when);
	}

}
