package com.meizhuo.etips.activities;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.meizhuo.etips.common.utils.AndroidUtils;
import com.meizhuo.etips.common.utils.CalendarManager;
import com.meizhuo.etips.common.utils.DataPool;
import com.meizhuo.etips.common.utils.ETipsAlarmManager;
import com.meizhuo.etips.common.utils.ETipsContants;
import com.meizhuo.etips.common.utils.Elog;
import com.meizhuo.etips.common.utils.JPushManager;
import com.meizhuo.etips.common.utils.SP;
import com.meizhuo.etips.common.utils.SharedPreferenceHelper;
import com.meizhuo.etips.db.CourseDAO;
import com.meizhuo.etips.model.Course;
import com.meizhuo.etips.model.Lesson;
/**
 * Change Log:
 * ETips 2.0 
 *  1.移除每日一句saying, Has_Saying_load 将会在再后一个版本移除
 *  2.根据版本来识别
 * since 11.28 <br>
 * 12.13 changelog<br>
 * 除去Application中的courseList ,专用到保存在SharedPreference<br>
 * since 2.1
 * @author Jayin Ton
 *
 */
public class ETipsStartActivity extends BaseUIActivity {
	private HashMap<String, String> property; // 用户偏好设置类
	private ETipsApplication App;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 必须在在setContentView()前设置
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		final View view = View.inflate(this, R.layout.acty_etips_start, null);
		setContentView(view);
		initData();
		initLayout();
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case ETipsContants.Start:
					toast("首次加载需要稍等片刻");
					break;
				case ETipsContants.Finish:
					JPushManager.init(getApplicationContext());
					// init
 
					SP sp  = null;
				    try {
				    	sp =new  SP(ETipsContants.SP_NAME_Version+AndroidUtils.getAppVersionName(getContext()), getContext());
					} catch (NameNotFoundException e) {
						e.printStackTrace();
					}
				    if(sp!=null && sp.getValue("First_Install").equals("YES")){
				    	sp.add("First_Install", "NO");
				    	//跳转到 导航
				    	openActivity(ETipsGuidePage.class);
				    }else{
				    	startActivity(new Intent(ETipsStartActivity.this,
								ETipsMainActivity.class));
					    }
					
					ETipsStartActivity.this.finish();
					break;
				}

			}

		};
		App = (ETipsApplication) getApplication();
		Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
		view.startAnimation(animation);
		animation.setDuration(1000);
		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						loadPreference();
						SharedPreferences sp = getSharedPreferences(
								ETipsContants.SharedPreference_NAME,
								Context.MODE_PRIVATE);
						if (sp.getString("LessonDB_Has_Data", "NO").equals(
								"YES")) {
							CourseDAO dao = new CourseDAO(
									ETipsStartActivity.this);
							List<List<List<Lesson>>> course = new ArrayList<List<List<Lesson>>>();
							for (int i = 1; i <= 7; i++) {
								course.add(dao.getLessonList("week = ?",
										new String[] { String.valueOf(i) }));
							}
						//	App.setLessonList(course);
							//把课程表保存到SharedPreference 更高效可靠
							DataPool dp  = new DataPool(ETipsContants.SP_NAME_Course, getContext());
						    Course c = new Course(course);
						    if(dp.add("course", c)){
						    	Elog.i("ETipsStartActivity -Course add  successfully");
						    }else{
						    	Elog.i("ETipsStartActivity -Course add  faild");
						    }
 
						}
						Message msg = handler.obtainMessage();
				//		initAlarm();
						msg.what = ETipsContants.Finish;
						handler.sendMessageDelayed(msg, 200);
					}
				}).start();

			}
		});
	}
	private void initAlarm(){
		PendingIntent operation = PendingIntent.getBroadcast(getContext(), ETipsContants.ID_Alarm_Course,
				new Intent(ETipsContants.ACTION_Custom_Alarm), PendingIntent.FLAG_UPDATE_CURRENT);
		ETipsAlarmManager eam = new ETipsAlarmManager(getContext());
		long firstTime = eam.getFirstTime(20, 1);
		eam.setRepeatAlarm(firstTime, 1000*60*60*24, operation);
	}
	
	private void loadPreference() {
		// 用户偏好设置加载
		SharedPreferences sp = this.getSharedPreferences(
				ETipsContants.SharedPreference_NAME, Context.MODE_PRIVATE);
		property = App.getProperty();

		if (!sp.contains("First_Open_APP")) {
			String[] properties = getResources().getStringArray(
					R.array.SharedPreference);
			for (String key : properties) {
				SharedPreferenceHelper.set(sp, key, "NO");
			}
			SharedPreferenceHelper.set(sp, "First_Open_APP", "YES");
			SharedPreferenceHelper.set(sp, "First_Open_APP_Time",
					String.valueOf(System.currentTimeMillis()));
			SharedPreferenceHelper.set(sp, "Current_Week", CalendarManager
					.getCalendar().get(Calendar.WEEK_OF_YEAR));
			SharedPreferenceHelper.set(sp, "Is_Open_Daily_Course_Alarm", "YES");
			property = (HashMap<String, String>) sp.getAll();

		} else {
			property = (HashMap<String, String>) sp.getAll();
			SharedPreferenceHelper.set(sp, "First_Open_APP", "NO");
			property.put("First_Open_APP", "NO");
		}
		if (sp.contains("Has_Saying_load")) { // 包含，说明用户基于新版本安装   

		} else { // 不包含，说明用户基于就旧本升级
			SharedPreferenceHelper.set(sp, "Has_Saying_load", "NO");
			property.put("Has_Saying_load", "NO");
		}
		App.setProperty(property);

		// 设置校园资讯模块
		SP msp = new SP(ETipsContants.SP_NAME_User, getContext());
		if (msp.getSharedPreferences().getString("nickname", "").equals("")) {
			msp.add("nickname", "null");
			msp.add("account", "null");
			msp.add("psw", "null");
			msp.add("session", "null");
			msp.add("id", "null");
			msp.add("ReigstTimeout", 1000 * 60 * 5 + "");
			msp.add("loginTimeout", 1000 * 60 * 60 * 24 * 7 + "");
			msp.add("loginTime","null");
			msp.add("shouldTopicListUpdata", "null");
			msp.add("shouldStartBgUpdata", "null");
			msp.add("shouldCurrentUpdata", "null");
			msp.add("descrpiton", "null");
			msp.add("SendCount", 0+"");  //sendCount就是控制每日可以发多少帖子
			msp.add("Day_of_Year", CalendarManager.getCalendar().get(Calendar.DAY_OF_YEAR)+"");
			msp.add("MaxSend", 3+"");// 一天最多能发的帖子数
		}
		//每日清0
		if(CalendarManager.getCalendar().get(Calendar.DAY_OF_YEAR) != Integer.parseInt(msp.getValue("Day_of_Year"))){
			msp.add("SendCount", 0+"");  //sendCount就是控制每日可以发多少帖子
			msp.add("Day_of_Year", CalendarManager.getCalendar().get(Calendar.DAY_OF_YEAR)+"");
		}
		//版本控制；跳转时才判断是否应该跳转到导航 or MainActivity;
		try {
			SP vsp = new SP(ETipsContants.SP_NAME_Version+AndroidUtils.getAppVersionName(getContext()), getContext());
			if(!vsp.getSharedPreferences().contains("First_Install")){
				vsp.add("First_Install","YES");
				SharedPreferenceHelper.set(sp, "Current_Week", CalendarManager
						.getStartTermWeek());
				//不断加入属性。。。
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

	}
	@Override
	protected void initLayout() {
 
	}

	@Override
	protected void initData() {
	 
	}

 
}
