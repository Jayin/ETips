package com.meizhuo.etips.activities;

import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meizhuo.etips.common.utils.CalendarManager;
import com.meizhuo.etips.common.utils.ETipsContants;
import com.meizhuo.etips.common.utils.Elog;
import com.meizhuo.etips.common.utils.SharedPreferenceHelper;
import com.meizhuo.etips.ui.utils.BaseDialog;

public class CourseSettingActivity extends BaseUIActivity implements
		OnClickListener {
	private Button backBtn;
	private ETipsApplication App;
	private RelativeLayout reflshly, currentWeekly, openAlarmly;
	private String currentWeek; // 当前是第几周？
	private String isOpenCourseAlarm;
	private TextView tv_currentweek, tv_openAlarm;
	private Activity mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_course_setting);
		this.mContext = CourseSettingActivity.this;
		initData();
		initLayout();
	}

	@Override
	protected void initLayout() {
		backBtn = (Button) this.findViewById(R.id.acty_course_setting_back);
		backBtn.setOnClickListener(this);
		reflshly = (RelativeLayout) this
				.findViewById(R.id.acty_course_setting_rely_reflush);
		reflshly.setOnClickListener(this);
		tv_currentweek = (TextView) this
				.findViewById(R.id.acty_course_setting_tv_currentWeek);
		tv_openAlarm = (TextView) this
				.findViewById(R.id.acty_course_setting_tv_dailyAlarm);
		tv_currentweek.setText(currentWeek + "周");
		if (isOpenCourseAlarm.equals("YES")) {
			tv_openAlarm.setText("开启");
		} else {
			tv_openAlarm.setText("关闭");
		}

		currentWeekly = (RelativeLayout) this
				.findViewById(R.id.acty_course_setting_rely_currentWeek);
		currentWeekly.setOnClickListener(this);
		openAlarmly = (RelativeLayout) this
				.findViewById(R.id.acty_course_setting_rely_dailyAlarm);
		openAlarmly.setOnClickListener(this);
	}

	@Override
	protected void initData() {
		App = (ETipsApplication) getApplication();
		int recordWeek = Integer
				.parseInt(App.getProperty().get("Current_Week"));
		int WEEK_OF_YEAR =CalendarManager.getWeekOfYeah();
		int _currentWeek = WEEK_OF_YEAR - recordWeek + 1;
		if (_currentWeek <= 0) { // 非法数据，即记录的周数WEEK_OF_YEAR 比 目前的 还大
									// （一般出现在用户自己调整了时间出现错误）
			_currentWeek = 1;
			SharedPreferences sp = getSharedPreferences(
					ETipsContants.SharedPreference_NAME, Context.MODE_PRIVATE);
			SharedPreferenceHelper.set(sp, "Current_Week", CalendarManager
					.getCalendar().get(Calendar.WEEK_OF_YEAR));
			App.getProperty().put("Current_Week", String.valueOf(WEEK_OF_YEAR));
		}
		currentWeek = String.valueOf(_currentWeek);
		isOpenCourseAlarm = App.getProperty().get("Is_Open_Daily_Course_Alarm");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.acty_course_setting_back:
			App.setObject(null);
			CourseSettingActivity.this.finish();
			break;
		case R.id.acty_course_setting_rely_reflush:
			Intent intent = new Intent(CourseSettingActivity.this,
					SubSystemLoginActivity.class);
			intent.putExtra("toWhere", "CourseMainActivity");
			startActivity(intent);
			CourseSettingActivity.this.finish();
			break;
		case R.id.acty_course_setting_rely_currentWeek:
			// show dialog before it create
			SetCurrentWeekDialog dialog = new SetCurrentWeekDialog(mContext);
			dialog.show();
			break;
		case R.id.acty_course_setting_rely_dailyAlarm:
			// show dialog before it create
			SetDailyCourseAlarmDialog mdialog = new SetDailyCourseAlarmDialog(mContext);
			mdialog.show();
			break;
		}

	}

	class SetCurrentWeekDialog extends BaseDialog {
		private Button okBtn, cancleBtn;
		private ImageButton decBtn, plusBtn;
		private TextView tv;
		private int mCurrentWeek;

		public SetCurrentWeekDialog(Context context) {
			super(context);
		}

		@Override
		protected void initLayout() {
			decBtn = (ImageButton) this
					.findViewById(R.id.dialog_setting_currentweek_decrease);
			decBtn.setOnClickListener(this);
			plusBtn = (ImageButton) this
					.findViewById(R.id.dialog_setting_currentweek_plus);
			plusBtn.setOnClickListener(this);
			okBtn = (Button) this
					.findViewById(R.id.dialog_setting_currentweek_ok);
			okBtn.setOnClickListener(this);
			cancleBtn = (Button) this
					.findViewById(R.id.dialog_setting_currentweek_cancle);
			cancleBtn.setOnClickListener(this);
			tv = (TextView) this
					.findViewById(R.id.dialog_setting_currentweek_week);
			/* ..init */
			mCurrentWeek = Integer.parseInt(currentWeek);
			tv.setText(currentWeek);

		}

		@Override
		protected void setContentView() {
			this.setContentView(R.layout.dialog_setting_currentweek);
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.dialog_setting_currentweek_decrease:
				if (mCurrentWeek - 1 > 0) {
					mCurrentWeek--;
					tv.setText(mCurrentWeek + "");
				}
				break;
			case R.id.dialog_setting_currentweek_plus:
				if (mCurrentWeek + 1 < 25) {
					mCurrentWeek++;
					tv.setText(mCurrentWeek + "");
				}
				break;
			case R.id.dialog_setting_currentweek_ok:
				this.dismiss();
				currentWeek = String.valueOf(mCurrentWeek);
				tv_currentweek.setText(mCurrentWeek + "周");
				SharedPreferences sp = getSharedPreferences(
						ETipsContants.SharedPreference_NAME,
						Context.MODE_PRIVATE);
				int WEEK_OF_YEAR = CalendarManager.getWeekOfYeah();
				int recoredTime = Integer.parseInt(sp.getString("Current_Week",
						String.valueOf(WEEK_OF_YEAR)));
				SharedPreferenceHelper.set(sp, "Current_Week", WEEK_OF_YEAR
						- mCurrentWeek + 1);
				App.refreshProperty();
				break;
			case R.id.dialog_setting_currentweek_cancle:
				this.dismiss();
				break;
			}
		}
	}

	class SetDailyCourseAlarmDialog extends BaseDialog {
		private Button openBtn, closeBtn;

		public SetDailyCourseAlarmDialog(Context context) {
			super(context);
		}

		@Override
		protected void initLayout() {
			openBtn = (Button) this
					.findViewById(R.id.dialog_setting_dailyalarm_ok);
			closeBtn = (Button) this
					.findViewById(R.id.dialog_setting_dailyalarm_cancle);
			openBtn.setOnClickListener(this);
			closeBtn.setOnClickListener(this);
		}

		@Override
		protected void setContentView() {
			setContentView(R.layout.dialog_settting_dailyalarm);
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.dialog_setting_dailyalarm_ok:
				if (tv_openAlarm.getText().toString().equals("关闭")) {
					tv_openAlarm.setText("开启");
					SharedPreferenceHelper.set(SharedPreferenceHelper
							.getSharedPreferences(CourseSettingActivity.this),
							"Is_Open_Daily_Course_Alarm", "YES");
					App.refreshProperty();
				}
				this.dismiss();
				break;
			case R.id.dialog_setting_dailyalarm_cancle:
				if (tv_openAlarm.getText().toString().equals("开启")) {
					tv_openAlarm.setText("关闭");
					SharedPreferenceHelper.set(SharedPreferenceHelper
							.getSharedPreferences(CourseSettingActivity.this),
							"Is_Open_Daily_Course_Alarm", "NO");
					App.refreshProperty();
				}
				this.dismiss();
				break;
			}
		}

	}
}
