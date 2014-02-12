package com.meizhuo.etips.common;

/**
 * 常量类
 * 
 * @author Jayin Ton
 * 
 */
public class ETipsContants {
	public final static String BaiduShareAppKey = "93NuSwnBajbvxYPNGGZsAuAH";
	/**
	 * name of Database、sharedPreferences、File
	 */
	public final static String DB_NAME = "ETips.db";
	public final static String DB_NAME_MsgCenter = "msgcenter.db";
	// public final static String SharedPreference_NAME =
	// "ETipsSharedPreference";
	public final static String SP_NAME_Preferences = "Preferences";// V
																	// 2.2开始用这个而不是上面一个
	public final static String SP_NAME_Notes = "Notes";
	public final static String SP_NAME_Book = "Book";
	// public final static String SP_NAME_User = "User";
	public final static String SP_NANE_ClientConfig = "ClientConfig";
	public final static String SP_NAME_Topic = "TopicList";
	// public final static String SP_NAME_Version = "Version_";
	public final static String SP_NAME_Course = "Course"; // 课程列表
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
	public final static int RequestCode_CourseMain2Setting = 9;
	public final static int RequestCode_BookCollection2Detail = 10;

	/**
	 * 消息类型
	 */
	public final static String TYPE_MsgCenter_Notify = "notify";
	public final static String TYPE_MsgCenter_System = "system";
	public final static String TYPE_MsgCenter_Push = "push";
	public final static String TYPE_MsgCenter_Feedback = "feedback";
	public final static String TYPE_MsgCenter_Image  = "image";
	/**
	 * SP类型
	 */
	public static final int TYPE_SP_Book = 0x0000001;
	public static final int TYPE_SP_Notes = 0x0000002;
	public static final int TYPE_SP_Topic = 0x0000003;
	public static final int TYPE_SP_Tweet = 0x0000004;
	// public static final int TYPE_SP_Course = 0x0000005;
	/**
	 * 定义一个每日提醒课程闹钟requestCode(ID)
	 */
	public final static int ID_Alarm_Course = 0x0000001;

	/**
	 * Notification 的id
	 */
	public final static int ID_Notification_MsgCenter = 0x0000011;
	public final static int ID_Notification_Alarm_Course = 0x0000012;
	public static final int ID_Notify = 10001;//暂时无用 since v2.2.但保留
	public static final int ID_System = 10002;
	public static final int ID_Push = 10003;
	public static final int ID_Send_Tweet = 10004;

	/**
	 * BrocastReceiver Action
	 */
	public final static String Action_Notes = "Action_Notes"; // Notes页面用于更新listview的Action
	public final static String Action_CurrentWeekChange = "Action_CurrentWeekChange";
	public final static String ACTION_Custom_Alarm = "ETips_Course_Alarm"; // 自定义闹钟的Action
	public final static String Action_CourseChange = "Action_CourseChange"; // 课表更新

	/**
	 * Service 的action
	 */
	public final static String Action_Service_Download_Pic = "download_pic";
	public final static String Action_Service_Check_Comment = "check_comment";
	/**
	 * TweetAPI 状态码
	 */
	public final static int SC_OK = 200;
	public final static int SC_Email_has_regist = 201;
	public final static int SC_Edit_timeout = 202;
	public final static int SC_Compose_faild = 203;
	public final static int SC_PSW_incorrect = 204;
	public final static int SC_Email_not_exist = 205;
	public final static int SC_Email_format_error = 206;
	public final static int SC_ID_HasRegist = 207;
    //debug tag
	public  final static String Debug = "debug";
}
