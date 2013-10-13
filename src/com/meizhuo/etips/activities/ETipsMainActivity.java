package com.meizhuo.etips.activities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.meizhuo.etips.common.utils.CalendarManager;
import com.meizhuo.etips.common.utils.CourseUtils;
import com.meizhuo.etips.common.utils.ETipsContants;
import com.meizhuo.etips.common.utils.SharedPreferenceHelper;
import com.meizhuo.etips.common.utils.StringUtils;
import com.meizhuo.etips.db.CourseDAO;
import com.meizhuo.etips.model.Lesson;

/**
 * share :只支持单用户分享~！
 * 
 * @author Jayin Ton
 * 
 */
public class ETipsMainActivity extends BaseUIActivity implements
		View.OnClickListener {
	private boolean isGongingToClose = false;
	private long waittingTime = 0;
	private ETipsApplication App;
	private boolean hasLoadingCourse = false; // 是否已经刷新了课表
	private RelativeLayout Btn_Library, Btn_wyuNews, Btn_Manual,
			Btn_checkScore, Btn_checkElectricity, Btn_msgCenter, Btn_Setting,
			Btn_Course;
	private TextView tv_time, tv_courseStatus, tv_weekNo;// tv_weekNo 第几周
	private int current_week;// 第几周

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_etips_main);
		initData();
		initLayout();

	}

	@Override
	protected void onResume() {
		super.onResume();
		//如果没有载入课表信息，则刷新一次
		if (!hasLoadingCourse) {
			startAsyncWork();
		}
		//如果更新了周数，应该刷新页面
		if(current_week != SharedPreferenceHelper.getCurrentWeek(this)){
			current_week = SharedPreferenceHelper.getCurrentWeek(this);
			startAsyncWork();
		    tv_weekNo.setText("第" + current_week + "周");
		}
	}

	private void startAsyncWork() {
		CourseUpdate cu = new CourseUpdate();
		cu.execute("");
	}

	@Override
	protected void initLayout() {
		Btn_Library = (RelativeLayout) _getView(R.id.acty_etips_main_library);
		Btn_wyuNews = (RelativeLayout) _getView(R.id.acty_etips_main_wyunews);
		Btn_Manual = (RelativeLayout) _getView(R.id.acty_etips_main_manual);
		Btn_checkScore = (RelativeLayout) _getView(R.id.acty_etips_main_checkSource);
		Btn_checkElectricity = (RelativeLayout) _getView(R.id.acty_etips_main_checkElcecity);
		Btn_msgCenter = (RelativeLayout) _getView(R.id.acty_etips_main_msgcenter);
		Btn_Setting = (RelativeLayout) _getView(R.id.acty_etips_main_setting);
		Btn_Course = (RelativeLayout) _getView(R.id.acty_etips_main_course);
		tv_time = (TextView) _getView(R.id.acty_etips_main_course_time);
		tv_courseStatus = (TextView) _getView(R.id.acty_etips_main_course_courseStatus);
		tv_weekNo = (TextView) _getView(R.id.acty_etips_main_course_weekNo);

		tv_weekNo.setText("第" + current_week + "周");
		tv_courseStatus.setText("");
		tv_time.setText(StringUtils.getTimeFormat());
		SharedPreferences sp = getSharedPreferences(
				ETipsContants.SharedPreference_NAME, Context.MODE_PRIVATE);
		String value = sp.getString("Has_Msg_To_Check", "NO");
		if (value.equals("YES")) {
			((ImageView) _getView(R.id.acty_etips_main_msgcenter_notifyNew))
					.setVisibility(View.VISIBLE);
		}
		Btn_Library.setOnClickListener(this);
		Btn_wyuNews.setOnClickListener(this);
		Btn_Manual.setOnClickListener(this);
		Btn_checkScore.setOnClickListener(this);
		Btn_checkElectricity.setOnClickListener(this);
		Btn_msgCenter.setOnClickListener(this);
		Btn_Setting.setOnClickListener(this);
		Btn_Course.setOnClickListener(this);

	}

	@Override
	protected void initData() {
		App = (ETipsApplication) getApplication();
		current_week = SharedPreferenceHelper.getCurrentWeek(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isGongingToClose) {
				if (System.currentTimeMillis() - waittingTime <= 1500) {
					this.finish();
				} else {
					isGongingToClose = false;
				}
			} else {
				waittingTime = System.currentTimeMillis();
				isGongingToClose = true;
				Toast.makeText(this, "再按一次返回键退出", Toast.LENGTH_SHORT).show();
				return true;
			}
		}
		return super.onKeyUp(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.acty_etips_main_library: // 图书馆
			openActivity(LibraryMainActivity.class);
			break;
		case R.id.acty_etips_main_wyunews: // 邑大新闻
			openActivity(SchoolNewsMainActivity.class);
			break;
		case R.id.acty_etips_main_course: // 课表
			Intent intent;
			List<List<List<Lesson>>> list = App.getLessonList();
			if (list == null || list.size() == 0) {
				intent = new Intent(ETipsMainActivity.this,
						SubSystemLoginActivity.class);
				intent.putExtra("toWhere", "CourseMainActivity");
			} else {
				intent = new Intent(ETipsMainActivity.this,
						CourseMainActivity.class);
				// intent.putExtra("toWhere", "CourseMainActivity");
			}
			openActicity(intent);
			break;
		case R.id.acty_etips_main_msgcenter: // 消息中心
			((ImageView) v
					.findViewById(R.id.acty_etips_main_msgcenter_notifyNew))
					.setVisibility(View.INVISIBLE);
			openActivity(MsgCenterActivity.class);
			break;
		case R.id.acty_etips_main_checkSource: // 查成绩
			intent = new Intent(ETipsMainActivity.this,
					SubSystemLoginActivity.class);
			intent.putExtra("toWhere", "ScoreRecordActivity");
			openActicity(intent);
			break;
		case R.id.acty_etips_main_checkElcecity: // 查电费
			openActivity(LifeElectricityActivity.class);
			break;
		case R.id.acty_etips_main_manual: // 学生手册
			openActivity(ManualMainActivity.class);
			break;
		case R.id.acty_etips_main_setting: // 设置
			openActivity(ETipsMainSettingActivity.class);

			break;
		}
	}

	/**
	 * 课表Button 的状态更新 异步
	 * 
	 * @author Jayin Ton
	 */
	class CourseUpdate extends AsyncTask<String, Void, String> {
		// 判断当前那节课要上？ 也可以无课的哦~
		// 还有下一节课
		@Override
		protected String doInBackground(String... params) {
			List<Lesson> _coursList = null;
			boolean flag = false;
			SharedPreferences sp = ETipsMainActivity.this.getSharedPreferences(
					ETipsContants.SharedPreference_NAME, Context.MODE_PRIVATE);
			String status = sp.getString("LessonDB_Has_Data", "NO");
			if (!status.equals("YES")) {
				return "同学\n还没有课表信息哦！\n赶紧更新课表吧！";
			}
			int[] courseStatus = CourseUtils.getCourseStatus(
					ETipsMainActivity.this, System.currentTimeMillis());
			StringBuilder sb = new StringBuilder("Current\n  ");
			System.out.println("status:" + courseStatus[0] + " perid:"
					+ courseStatus[1]);
			if (courseStatus[0] == 0) {
				sb.append("无课\n");
			} else { //
				_coursList = getLesson(courseStatus);
				flag = false;
				for (Lesson l : _coursList) {
					if (CourseUtils.isLessonStart(ETipsMainActivity.this, l)) {
						sb.append(l.LessonName).append("\n  ")
								.append(l.address).append("\n");
						flag = true;
						break;
					}
				}
				if (!flag) {
					sb.append("无课\n");
				}

			}
			sb.append("\nNext\n  ");
			courseStatus[0] = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
			if (courseStatus[0] == 0)
				courseStatus[0] = 7;
			flag = false;
			if (courseStatus[1] + 1 > 5) {
				sb.append("无课\n");
			} else {
				courseStatus[1]++;
				_coursList = getLesson(courseStatus);
				flag = false;
				for (Lesson l : _coursList) {
					if (CourseUtils.isLessonStart(ETipsMainActivity.this, l)) {
						sb.append(l.LessonName).append("\n  ")
								.append(l.address).append("\n");
						flag = true;
						break;
					}
				}
				if (!flag) {
					sb.append("无课\n");
				}
			}
			hasLoadingCourse = true;// 已经刷新了课表
			return sb.toString();
		}

		@Override
		protected void onPostExecute(String result) {
			tv_courseStatus.setText(result);

		}

		private List<Lesson> getLesson(int[] courseStatus) {
			List<Lesson> _coursList = null;
			if (App.getLessonList() != null && App.getLessonList().size() > 0) {
				_coursList = App.getLessonList().get(courseStatus[0] - 1)
						.get(courseStatus[1] - 1);
			} else {
				CourseDAO dao = new CourseDAO(ETipsMainActivity.this);
				// dao.getCourse()
				_coursList = dao.getLessonByClassTime(
						"week = ? and classtime = ?",
						new String[] { String.valueOf(courseStatus[0]),
								String.valueOf(courseStatus[1]) });
			}
			return _coursList;
		}

	}
}
