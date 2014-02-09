package com.meizhuo.etips.common;



import java.util.LinkedHashSet;
import java.util.Set;

import android.app.Notification;
import android.content.Context;
import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

import com.meizhuo.etips.activities.R;
/**
 * context 必须是application context
 * 调试模式 ：在getTags() 不注释set.add("ETipsTestCase");
 * @author Jayin Ton
 *
 */
public class JPushManager {
	public static void init(Context context) {
//		JPushInterface.setDebugMode(true); // 设置开启日志,发布时请关闭日志
		JPushInterface.init(context); // 初始化 JPush
		BasicPushNotificationBuilder b = new BasicPushNotificationBuilder(context);
        b.statusBarDrawable = R.drawable.icon;
        b.notificationFlags = Notification.FLAG_AUTO_CANCEL;  //设置为自动消失
        b.notificationDefaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;  // 设置为铃声与震动都要
        JPushInterface.setDefaultPushNotificationBuilder( b);
        JPushInterface.setAliasAndTags(context, null,getTags());
        
	}
	
	public static void stopPush(Context context){
		JPushInterface.stopPush(context.getApplicationContext());
	}
	
	public static void resumePush(Context context){
		JPushInterface.resumePush(context.getApplicationContext());
	}
	
	/**
	 * 设置别名和标签
	 * @param context
	 * @param alias 别名
	 * @param tags 标签
	 */ 
	public void setAliasAndTags(Context context,String alias,Set tags){
		JPushInterface.setAliasAndTags(context, alias, tags);
	}
	/**
	 * 设置别名 
	 * @param context
	 * @param alias 别名
	 */
	public void setAlias(Context context,String alias){
		this.setAliasAndTags(context,alias,null);
	}
	/**
	 * 设置标签
	 * @param context
	 * @param tags  标签
	 */ 
	public void setTags(Context context,Set tags){
		this.setAliasAndTags(context, null, tags);
	}
	/**
	 * 获得标签
	 * 后期 将从文件中获取tags
	 * @return set of tags
	 */
	public  static Set getTags(){
		Set<String> set = new LinkedHashSet<String>();
		set.add("邑大");
//	  	set.add("ETipsTestCase");
		return set;
	}
	
}
