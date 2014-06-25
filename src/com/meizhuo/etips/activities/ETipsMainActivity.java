package com.meizhuo.etips.activities;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import com.meizhuo.etips.app.AppInfo;
import com.meizhuo.etips.app.Preferences;
import com.meizhuo.etips.common.CalendarManager;
import com.meizhuo.etips.common.CourseUtils;
import com.meizhuo.etips.common.ETipsContants;
import com.meizhuo.etips.common.ETipsUtils;
import com.meizhuo.etips.common.StringUtils;
import com.meizhuo.etips.model.Lesson;
import com.meizhuo.etips.service.ETipsCoreService;

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
	// private boolean hasLoadingCourse = false; // 是否已经刷新了课表
	private RelativeLayout Btn_Library, Btn_wyuNews, Btn_notes,
			Btn_checkElectricity, Btn_msgCenter, Btn_Setting, Btn_Course,
			queryClassroom;
	private TextView tv_time, tv_weekNo, tv_TimePart;// tv_weekNo 第几周
														// tv_TimePart =AM PM
														// Night
														// //tv_courseStatus
	private int current_week;// 第几周
	private BroadcastReceiver receiver;
	private View Tweets;// 学校资讯
	private String timePart = "AM";

	private TextView tv_course_up1, tv_course_up2, tv_course_down1,
			tv_course_down2;
	private String up1, up2, down1, down2;

	private ViewFlipper flipper_image;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_etips_main_3);
		initData();
		initLayout();
		initReceivers(); // 初始化recevier.. 可以不断添加intent的Action
		toCheck();
	}

	private void toCheck() {
		Intent service = new Intent(getContext(),ETipsCoreService.class);
		service.setAction(ETipsContants.Action_Service_Check_Comment);
		startService(service);
	}

	private void initReceivers() {
		receiver = new MainReceiver();
		IntentFilter filter = new IntentFilter(ETipsContants.Action_Notes);
		filter.addAction(ETipsContants.Action_MsgReceive);
		filter.addAction(ETipsContants.Action_CurrentWeekChange);
		filter.addAction(ETipsContants.Action_CourseChange);
		this.registerReceiver(receiver, filter);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (receiver != null) {
			this.unregisterReceiver(receiver);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (flipper_image != null)
			flipper_image.stopFlipping();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 如果没有载入课表信息，则刷新一次
		flipper_image.startFlipping();// 启动flipper 在onPause停止
		flipper_image.postDelayed(new Runnable() {

			@Override
			public void run() {
				flipper_image.showNext();
			}
		}, 1000);
		startAsyncWork();
	}

	// 切换时间课表
	private void startAsyncWork() {
		CourseUpdate cu = new CourseUpdate();
		current_week = Preferences.getCurrentWeek(this);
		if (current_week > 0 && current_week < 21)
			tv_weekNo.setText("第" + current_week + "周");
		else
			tv_weekNo.setText("放假ing");
		cu.execute("");
	}

	@Override
	protected void initLayout() {
		initTweets();
		Btn_Library = (RelativeLayout) _getView(R.id.acty_etips_main_library);
		Btn_wyuNews = (RelativeLayout) _getView(R.id.acty_etips_main_wyunews);
		Btn_notes = (RelativeLayout) _getView(R.id.acty_etips_main_notes);
		Btn_checkElectricity = (RelativeLayout) _getView(R.id.acty_etips_main_checkElcecity);
		queryClassroom = (RelativeLayout) _getView(R.id.acty_etips_main_rely_qureyclassroom);
		Btn_msgCenter = (RelativeLayout) _getView(R.id.acty_etips_main_msgcenter);
		Btn_Setting = (RelativeLayout) _getView(R.id.acty_etips_main_setting);
		Btn_Course = (RelativeLayout) _getView(R.id.acty_etips_main_course);
		tv_time = (TextView) _getView(R.id.acty_etips_main_course_time);
		tv_weekNo = (TextView) _getView(R.id.acty_etips_main_course_weekNo);
		tv_TimePart = (TextView) _getView(R.id.acty_etips_main_course_time_part);
		flipper_image = (ViewFlipper) _getView(R.id.acty_etips_main_flipper_teweet);// 图片flipper

		tv_weekNo.setText("第" + current_week + "周");
		// / tv_courseStatus.setText("");
		tv_time.setText(StringUtils.getTimeFormat());
		// 是否有未读消息
		if (Preferences.isHasMsgToCheck(getContext())) {
			((ImageView) _getView(R.id.acty_etips_main_msgcenter_notifyNew))
					.setVisibility(View.VISIBLE);
		}
		Btn_Library.setOnClickListener(this);
		Btn_wyuNews.setOnClickListener(this);
		Btn_notes.setOnClickListener(this);
		Btn_checkElectricity.setOnClickListener(this);
		queryClassroom.setOnClickListener(this);
		Btn_msgCenter.setOnClickListener(this);
		Btn_Setting.setOnClickListener(this);
		Btn_Course.setOnClickListener(this);

		initCourseShow();
		initFlipper();
	}

	private void initCourseShow() {

		tv_course_up1 = (TextView) _getView(R.id.acty_etips_main_course_two_up_1);
		tv_course_up2 = (TextView) _getView(R.id.acty_etips_main_course_two_up_2);
		tv_course_down1 = (TextView) _getView(R.id.acty_etips_main_course_two_down_1);
		tv_course_down2 = (TextView) _getView(R.id.acty_etips_main_course_two_down_2);

	}

	private void initTweets() {
		Tweets = _getView(R.id.acty_etips_main_flipper_teweet);
		Tweets.setOnClickListener(this);

	}

	// 初始化Flipper
	private void initFlipper() {
		flipper_image.addView(_inflat(R.drawable.pic1));
		flipper_image.addView(_inflat(R.drawable.pic2));
		flipper_image.addView(_inflat(R.drawable.pic3));
		flipper_image.addView(_inflat(R.drawable.pic4));
		flipper_image.addView(_inflat(R.drawable.pic5));
		flipper_image.addView(_inflat(R.drawable.pic6));
		flipper_image.setInAnimation(this, R.anim.main_tweet_in);
		flipper_image.setOutAnimation(this, R.anim.main_tweet_out);
	}

	public View _inflat(int picID) {
		View v = LayoutInflater.from(this).inflate(R.layout.item_image, null);
		v.setBackgroundResource(picID);
		// ((LinearLayout)v.findViewById(R.id.item_image_iv)).setImageResource(picID);
		return v;
	}

	@Override
	protected void initData() {
		App = (ETipsApplication) getApplication();
		current_week = ETipsUtils.getCurrentWeek(this);
		// 获取Notes
//		SP sp = new SP(ETipsContants.SP_NAME_Notes, this);
		// notes = (List<MNotes>) sp.toEntityAll(ETipsContants.TYPE_SP_Notes);

		up1 = up2 = down1 = down2 = null;
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
			Log.d("debug",list.toString());
			if (list == null || list.size() == 0) {
				intent = new Intent(ETipsMainActivity.this,
						SubSystemLoginActivity.class);
				intent.putExtra("toWhere", "CourseMainActivity");
			} else {
				intent = new Intent(ETipsMainActivity.this,
						CourseMainActivity.class);
			}
			openActivity(intent);
			break;
		case R.id.acty_etips_main_msgcenter: // 消息中心
			((ImageView) v
					.findViewById(R.id.acty_etips_main_msgcenter_notifyNew))
					.setVisibility(View.INVISIBLE);
			openActivity(MsgCenterActivity.class);
			break;
		case R.id.acty_etips_main_rely_qureyclassroom: // 查空课室
			openActivity(QueryEmptyClassroom.class);
			break;
		case R.id.acty_etips_main_checkElcecity: // 查电费
			openActivity(LifeElectricityActivity.class);
			break;
		case R.id.acty_etips_main_notes: // 个人便签
			openActivity(Notes.class);
			break;
		case R.id.acty_etips_main_setting: // 设置
			openActivity(ETipsMainSettingActivity.class);

			break;
		case R.id.acty_etips_main_flipper_teweet:
			openActivity(TopicList.class);
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
			if (!Preferences.isCourseExist(getContext())) {
				return up1 = "同学\n还没有课表信息哦！\n赶紧点击这里更新课表吧!";
				//没有课表可以做些清理操作。。
			}
			up1 = up2 = down1 = down2 = null;
			wrapperCourseInfo(CalendarManager.getDayPart());
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// tv_courseStatus.setText(result);
			tv_course_up1.setText(up1);
			if (!up1.equals("本时段没有课程!")
					&& !up1.equals("同学\n还没有课表信息哦！\n赶紧点击这里更新课表吧!")) {
				tv_course_down1.setText(down1);
				tv_course_down1.setVisibility(View.VISIBLE);
				if (up2 != null) {
					tv_course_up2.setText(up2);
					tv_course_down2.setText(down2);
					tv_course_up2.setVisibility(View.VISIBLE);
					tv_course_down2.setVisibility(View.VISIBLE);
				} else {
					tv_course_up2.setVisibility(View.GONE);
					tv_course_down2.setVisibility(View.GONE);
				}
			} else {
				tv_course_down1.setVisibility(View.GONE);
				tv_course_up2.setVisibility(View.GONE);
				tv_course_down2.setVisibility(View.GONE);

			}
			tv_TimePart.setText(timePart);
		}
	}

	class MainReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(
					ETipsContants.Action_CurrentWeekChange)) {
				new CourseUpdate().execute("");
			}else if(intent.getAction().equals(ETipsContants.Action_MsgReceive)){//有新消息
				((ImageView) _getView(R.id.acty_etips_main_msgcenter_notifyNew))
				.setVisibility(View.VISIBLE);
			}
		}
	}

	public void wrapperCourseInfo(String part) {
		List<Lesson> _coursList1 = null, _coursList2 = null;
		boolean flag = false;
		if ("AM".equals(part)) {
			_coursList1 = AppInfo.getCourse(getContext()).getDailyLesson(
					CalendarManager.getWeekDay() - 1, 0);

			_coursList2 = AppInfo.getCourse(getContext()).getDailyLesson(
					CalendarManager.getWeekDay() - 1, 1);

		} else if ("PM".equals(part)) {
			_coursList1 = AppInfo.getCourse(getContext()).getDailyLesson(
					CalendarManager.getWeekDay() - 1, 2);

			_coursList2 = AppInfo.getCourse(getContext()).getDailyLesson(
					CalendarManager.getWeekDay() - 1, 3);
		} else {
			// Night
			_coursList1 = AppInfo.getCourse(getContext()).getDailyLesson(
					CalendarManager.getWeekDay() - 1, 4);
		}
		for (Lesson l : _coursList1) {
			if (CourseUtils.isLessonStart(getContext(), l)) {
				up1 = l.LessonName;
				down1 = l.address;
				flag = true;
				break;
			}
		}
		if (_coursList2 != null) {
			for (Lesson l : _coursList2) {
				if (CourseUtils.isLessonStart(getContext(), l)) {
					if (up1 == null) { // 没有第一节课
						up1 = l.LessonName;
						down1 = l.address;
					} else {
						up2 = l.LessonName;
						down2 = l.address;
					}
					flag = true;
					break;
				}
			}
		}
		if (!flag) {
			up1 = "本时段没有课程!";
		}
	}
}
