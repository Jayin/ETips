package com.meizhuo.etips.activities;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.meizhuo.etips.common.utils.CalendarManager;
import com.meizhuo.etips.common.utils.ETipsContants;
import com.meizhuo.etips.common.utils.JPushManager;
import com.meizhuo.etips.common.utils.SharedPreferenceHelper;
import com.meizhuo.etips.db.CourseDAO;
import com.meizhuo.etips.model.Lesson;

public class ETipsStartActivity extends BaseUIActivity {
	private HashMap<String, String> property; // 用户偏好设置类
	private ETipsApplication App;
	private String saying = "";
	private TextView tv_saying;

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
				default:
					JPushManager.init(getApplicationContext());
					// init
					if (tv_saying != null && tv_saying.equals(""))
						tv_saying.setText(saying);
					startActivity(new Intent(ETipsStartActivity.this,
							ETipsMainActivity.class));
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
							App.setLessonList(course);
						}
						Message msg = handler.obtainMessage();
						if (sp.getString("Has_Saying_load", "NO").equals("NO")) {
							handler.sendEmptyMessage(ETipsContants.Start);
							loadSaying();
							SharedPreferenceHelper.set(sp, "Has_Saying_load",
									"YES");
							msg.what = ETipsContants.Finish;
							handler.sendMessage(msg);
							return;
						}
						msg.what = ETipsContants.Finish;
						handler.sendMessageDelayed(msg, 1000);

					}
				}).start();

			}
		});

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

	}

	private void loadSaying() {
		BufferedReader br = new BufferedReader(new InputStreamReader(
				getResources().openRawResource(R.raw.saying)));
		String data;
		try {
			int count = 0;
			SharedPreferences sp = getSharedPreferences(
					ETipsContants.SharedPreference_NAME_Saying,
					Context.MODE_PRIVATE);
			while ((data = br.readLine()) != null) {
				count++;
				SharedPreferenceHelper.set(sp, String.valueOf(count), data);
			}
			SharedPreferenceHelper.set(sp, "SIZE", count);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void initLayout() {
		tv_saying = (TextView) _getView(R.id.acty_etips_start_tv_saying);
		tv_saying.setText(saying);
	}

	@Override
	protected void initData() {
		getSaying();
	}

	private String getSaying() {
		SharedPreferences sp = getSharedPreferences(
				ETipsContants.SharedPreference_NAME_Saying,
				Context.MODE_PRIVATE);
		if (sp.getString("SIZE", "0").equals("0")) {
			// 不存在
			// System.out.println("SIZE =0");
		} else {
			// 存在
			int size = Integer.parseInt(sp.getString("SIZE", "1"));
			// System.out.println("Size= " + size);
			Random r = new Random(System.currentTimeMillis());
			int randomNumber = r.nextInt(size) + 1;
			while (!(randomNumber >= 1 && randomNumber <= size)) {
				randomNumber = r.nextInt(size) + 1;
			}
			// System.out.println("randomNumber::" + randomNumber);
			saying = sp.getString(String.valueOf(randomNumber), "");
			// System.out.println("saying is:" + saying);
		}
		return saying;
	}

}
