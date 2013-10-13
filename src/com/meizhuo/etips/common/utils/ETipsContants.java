package com.meizhuo.etips.common.utils;

/**
 * 常量类
 * 
 * @author Lenovo
 * 
 */
public class ETipsContants {
	public final static String BaiduShareAppKey = "93NuSwnBajbvxYPNGGZsAuAH";
	/**
	 * name of Database、sharedPreferences、File
	 */
	public final static String DB_NAME = "ETips.db";
	public final static String DB_NAME_MsgCenter ="msgcenter.db";
	public final static String SharedPreference_NAME = "ETipsSharedPreference";
	public final static String File_NewsCache = "NewsCache";
	public final static String SharedPreference_NAME_Saying = "Saying";
    
	/**
	 * http请求状态
	 */
	public final static int Start = 0;
	public final static int Logining = 1;
	public final static int Downloading = 2;
	public final static int Finish = 3;
	public final static int Fail = 4;
    
    /**
     * Activity 请求码
     */
	public final static int RequestCode_CourseMain2Detail = 5;
	public final static int RequestCode_LibraryMain2SearchResult = 6;
	public final static int RequestCode_SchoolNewMain2Detail = 7;
	public final static int RequestCode_EtipsMain2MsgCenter = 8;
	
	
	/**
	 * 消息类型
	 */
	public final static String TYPE_MsgCenter_Notify ="notify";
	public final static String TYPE_MsgCenter_System ="system";
	public final static String TYPE_MsgCenter_Push ="push";
	/**
	 * 定义一个每日提醒课程闹钟requestCode(ID)
	 */
    public final static int ID_Alarm_Course = 0x0000001;  
    /**
     * 自定义闹钟的Action
     */
    public final static String ACTION_Custom_Alarm = "ETips_Course_Alarm";
    /**
     * Notification 的id
     */
    public final static int ID_Notification_MsgCenter = 0x0000011;
    public final static int ID_Notification_Alarm_Course = 0x0000012;
    public static final int ID_Notify = 10001;
    public static final int ID_System = 10002;
    public static final int ID_Push = 10003;
    
}
