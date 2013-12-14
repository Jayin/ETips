package com.meizhuo.etips.activities;

import java.util.Calendar;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

 
 
import com.meizhuo.etips.common.utils.CalendarManager;
import com.meizhuo.etips.common.utils.CourseUtils;
import com.meizhuo.etips.common.utils.ETipsContants;
import com.meizhuo.etips.common.utils.Elog;

import com.meizhuo.etips.common.utils.SP;
import com.meizhuo.etips.common.utils.SharedPreferenceHelper;
import com.meizhuo.etips.common.utils.StringUtils;
import com.meizhuo.etips.db.CourseDAO;
import com.meizhuo.etips.model.Lesson;
import com.meizhuo.etips.model.MNotes;

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
			queryClassroom; // Btn_checkElectricity,
	// private Button Btn_addNote; // 增加Notes
	private ViewFlipper flipper; // 控制Notes的不断切换的flipper
	private TextView tv_time, tv_weekNo, tv_TimePart;// tv_weekNo 第几周
														// tv_TimePart =AM PM
														// Night
														// //tv_courseStatus
	private int current_week;// 第几周
	private BroadcastReceiver receiver;
	// private List<MNotes> notes;
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
	}

	private void initReceivers() {
		receiver = new MainReceiver();
		IntentFilter filter = new IntentFilter(ETipsContants.Action_Notes);
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
    	if(flipper_image!=null )flipper_image.stopFlipping();
    }
	@Override
	protected void onResume() {
		super.onResume();
		// 如果没有载入课表信息，则刷新一次
	    flipper_image.startFlipping();//启动flipper   在onPause停止
		flipper_image.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				flipper_image.showNext();
			}
		}, 1000);
		startAsyncWork();
		// 如果更新了周数，应该刷新页面
		// if (current_week != SharedPreferenceHelper.getCurrentWeek(this)) {
		// current_week = SharedPreferenceHelper.getCurrentWeek(this);
		// startAsyncWork();
		// tv_weekNo.setText("第" + current_week + "周");
		// Elog.i("ETipsMainActy::current week---->" + current_week);
		// }

		// 启动flipper;
		// if (flipper != null && !flipper.isFlipping()) {
		// flipper.startFlipping();
		// }
	}
	
	

	// 切换时间课表
	private void startAsyncWork() {
		CourseUpdate cu = new CourseUpdate();
		current_week = SharedPreferenceHelper.getCurrentWeek(this);
		tv_weekNo.setText("第" + current_week + "周");
		cu.execute("");
	}

	@Override
	protected void initLayout() {
		initTweets();
		Btn_Library = (RelativeLayout) _getView(R.id.acty_etips_main_library);
		Btn_wyuNews = (RelativeLayout) _getView(R.id.acty_etips_main_wyunews);
		Btn_notes = (RelativeLayout) _getView(R.id.acty_etips_main_notes);
		// Btn_checkScore = (RelativeLayout)
		// _getView(R.id.acty_etips_main_checkSource);
		Btn_checkElectricity = (RelativeLayout) _getView(R.id.acty_etips_main_checkElcecity);
		queryClassroom = (RelativeLayout) _getView(R.id.acty_etips_main_rely_qureyclassroom);
		Btn_msgCenter = (RelativeLayout) _getView(R.id.acty_etips_main_msgcenter);
		Btn_Setting = (RelativeLayout) _getView(R.id.acty_etips_main_setting);
		Btn_Course = (RelativeLayout) _getView(R.id.acty_etips_main_course);
		tv_time = (TextView) _getView(R.id.acty_etips_main_course_time);
		// tv_courseStatus = (TextView)
		// _getView(R.id.acty_etips_main_course_courseStatus);
		tv_weekNo = (TextView) _getView(R.id.acty_etips_main_course_weekNo);
		tv_TimePart = (TextView) _getView(R.id.acty_etips_main_course_time_part);
		// Btn_addNote = (Button) _getView(R.id.frame_main_note_btn_add);
		// flipper = (ViewFlipper) _getView(R.id.frame_main_note_flipper); // //note 的flipper
        flipper_image = (ViewFlipper) _getView(R.id.acty_etips_main_flipper_teweet);//图片flipper
        
        
		tv_weekNo.setText("第" + current_week + "周");
		// / tv_courseStatus.setText("");
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
		Btn_notes.setOnClickListener(this);
		Btn_checkElectricity.setOnClickListener(this);
		queryClassroom.setOnClickListener(this);
		Btn_msgCenter.setOnClickListener(this);
		Btn_Setting.setOnClickListener(this);
		Btn_Course.setOnClickListener(this);
		// Btn_addNote.setOnClickListener(this);

		
		// flipper.startFlipping();
		 
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
		LayoutInflater flater = getLayoutInflater();
//		flipper.setInAnimation(this, R.anim.push_down_in);
//		flipper.setOutAnimation(this, R.anim.push_down_out);
         
		//flipper_image.removeAllViews();
		flipper_image.addView(_inflat(R.drawable.pic1));
		flipper_image.addView(_inflat(R.drawable.pic2));
		flipper_image.addView(_inflat(R.drawable.pic3));
		flipper_image.addView(_inflat(R.drawable.pic4));
		flipper_image.addView(_inflat(R.drawable.pic5));
		flipper_image.addView(_inflat(R.drawable.pic6));
		flipper_image.setInAnimation(this,R.anim.main_tweet_in);
		flipper_image.setOutAnimation(this,R.anim.main_tweet_out);
	
		
	
		// if (notes == null || notes.size() == 0) {
		// View v = flater.inflate(R.layout.item_frame_note, null);
		// ((TextView) v.findViewById(R.id.item_frame_note_time))
		// .setText(StringUtils.getDateFormat(
		// System.currentTimeMillis(), "yy-mm-dd"));
		// ((TextView) v.findViewById(R.id.item_frame_note_content))
		// .setText("总有些事情容易淡忘，不妨写下来的吧！");
		// flipper.addView(v);
		// } else {
		// for (int position = 0; position < notes.size(); position++) {
		// View v = flater.inflate(R.layout.item_frame_note, null);
		// ((TextView) v.findViewById(R.id.item_frame_note_time))
		// .setText(StringUtils.getDateFormat(notes.get(position)
		// .getTime(), "yy-mm-dd"));
		// ((TextView) v.findViewById(R.id.item_frame_note_content))
		// .setText(notes.get(position).getContent());
		// flipper.addView(v);
		// }
		// }
	}
	 public View _inflat(int picID){
		 View v=  LayoutInflater.from(this).inflate(R.layout.item_image, null);
		 v.setBackgroundResource(picID);
		// ((LinearLayout)v.findViewById(R.id.item_image_iv)).setImageResource(picID);
		 return v;
	 }

	@Override
	protected void initData() {
		App = (ETipsApplication) getApplication();
		current_week = SharedPreferenceHelper.getCurrentWeek(this);
		// 获取Notes
		SP sp = new SP(ETipsContants.SP_NAME_Notes, this);
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
			if (list == null || list.size() == 0) {
				intent = new Intent(ETipsMainActivity.this,
						SubSystemLoginActivity.class);
				intent.putExtra("toWhere", "CourseMainActivity");
			} else {
				intent = new Intent(ETipsMainActivity.this,
						CourseMainActivity.class);
				// intent.putExtra("toWhere", "CourseMainActivity");
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
		    up1 = up2 =  down1 = down2 = null;
			List<Lesson> _coursList = null;
			boolean flag = false;
			SharedPreferences sp = ETipsMainActivity.this.getSharedPreferences(
					ETipsContants.SharedPreference_NAME, Context.MODE_PRIVATE);
			String status = sp.getString("LessonDB_Has_Data", "NO");
			if (!status.equals("YES")) {
				return up1 = "同学\n还没有课表信息哦！\n赶紧点击这里更新课表吧!";
			}

			int[] courseStatus = CourseUtils.getCourseStatus(
					ETipsMainActivity.this, CalendarManager.getTimePart()
							.getTimeInMillis());
			if (courseStatus[1] == 1) {
				timePart = "AM";
			} else if (courseStatus[1] == 3) {
				timePart = "PM";
			} else if (courseStatus[1] == 5) {
				timePart = "Night";
			}
			if (courseStatus[0] == 0) {
				// sb.append("无课\n");
			} else { //
				_coursList = getLesson(courseStatus);

				for (Lesson l : _coursList) {
					if (CourseUtils.isLessonStart(ETipsMainActivity.this, l)) {
						up1 = l.LessonName;
						down1 = l.address;
						flag = true;
						break;
					}
				}

			}

			courseStatus[0] = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
			if (courseStatus[0] == 0)
				courseStatus[0] = 7;

			if (courseStatus[1] + 1 > 5) {

			} else {
				courseStatus[1]++;
				_coursList = getLesson(courseStatus);

				for (Lesson l : _coursList) {
					if (CourseUtils.isLessonStart(ETipsMainActivity.this, l)) {
						if (up1 == null) { //没有第一节课
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

	class MainReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(
					ETipsContants.Action_CurrentWeekChange)) {
				new CourseUpdate().execute("");
			}
		}
	}
}
