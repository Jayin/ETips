package com.meizhuo.etips.activities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;

import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meizhuo.etips.animations.Rotate3dAnimation;
import com.meizhuo.etips.common.utils.CalendarManager;
import com.meizhuo.etips.common.utils.ETipsContants;

import com.meizhuo.etips.model.Lesson;
import com.meizhuo.etips.ui.CourseAllWeekAdapter;
import com.meizhuo.etips.ui.CourseListViwAdapter;
import com.meizhuo.etips.ui.CourseViewPagerAdapter;

public class CourseMainActivity extends BaseUIActivity {
	private List<ListView> lvList;
	private Button backBtn, settingBtn, switichBtn;
	private TextView tv;
	private ViewPager viewpager;
	private List<View> views;
	private ETipsApplication App;
	private HashMap<String, String> property;
	private List<List<List<Lesson>>> course;
	private List<CourseListViwAdapter> lvAdapterList;
	private int DAY_OF_WEEK = 1; // 1 - 7 means Sunday,Monday.....Saturday
	private ListView lv;
	private RelativeLayout rely;// rely显示allweek 的view // container.包含viewpager
								// and rely
	private ViewGroup container;
	private int FLAG_POSITIVE = 1; // 页面旋转标志:正面旋转至反面，
	private int FLAG_NEGATIVE = -1;// 页面旋转标志:反面旋转至正面
	private String Title_CurrentWeek = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_course_main);
		initData();
		// App.setObject(this); // 这里为了方便跳转！CourseMainActivity-》settingActivity
		// ->subloginActivity-》CourseMainActivity
		initLayout();

	}

	@Override
	protected void onResume() {
		super.onResume();
		reflushWeekTime();
		Log.i("debug", "I am onResume!");
	}

	@Override
	protected void initLayout() {
		lvList = new ArrayList<ListView>();
		views = new ArrayList<View>();
		tv = (TextView) this.findViewById(R.id.acty_course_main_tv_weekday);
		setDate(DAY_OF_WEEK - 1);

		lv = (ListView) this.findViewById(R.id.acty_course_main_listview);
		rely = (RelativeLayout) this
				.findViewById(R.id.acty_course_main_allweek);
		container = (ViewGroup) this.findViewById(R.id.acty_course_main_middle);
		// Since we are caching large views, we want to keep their cache
		// between each animation
		container
				.setPersistentDrawingCache(ViewGroup.PERSISTENT_ANIMATION_CACHE);

		backBtn = (Button) this.findViewById(R.id.acty_course_main_back);
		backBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CourseMainActivity.this.finish();
			}
		});
		settingBtn = (Button) this.findViewById(R.id.acty_course_main_setting);
		settingBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(CourseMainActivity.this,
						CourseSettingActivity.class));
			}
		});
		switichBtn = (Button) this.findViewById(R.id.acty_course_main_switcher);
		switichBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (switichBtn.getText().toString().equals("整 周")) {
					switichBtn.setText("每 日");
					tv.setText(Title_CurrentWeek);
					applyRotation(FLAG_POSITIVE, 0, 90);
				} else {
					switichBtn.setText("整 周");
					setDate(DAY_OF_WEEK - 1);
					applyRotation(FLAG_NEGATIVE, 180, 90);
				
				}
			}
		});

		// 初始化viewpager
		viewpager = (ViewPager) this
				.findViewById(R.id.acty_course_main_viewpager);
		LayoutInflater inflater = getLayoutInflater();
		for (int i = 0; i < 7; i++) {
			views.add(getView(i));
		}
		viewpager.setAdapter(new CourseViewPagerAdapter(views));
		viewpager.setDrawingCacheEnabled(true);
		viewpager.setOnPageChangeListener(new mOnPageChangeListener());
		viewpager.setCurrentItem(DAY_OF_WEEK - 1);
		// 初始化gone 的 view (allweek)
		lv.setAdapter(new CourseAllWeekAdapter(CourseMainActivity.this, course));

	}

	private View getView(int position) {
		LayoutInflater inflater = getLayoutInflater();
		View v = inflater.inflate(R.layout.item_viewpager_listview, null);
		ListView lv = (ListView) v.findViewById(R.id.acty_course_main_listview);
		lvList.add(lv);
		lvAdapterList.add(new CourseListViwAdapter(course.get(position),
				CourseMainActivity.this));
		lv.setAdapter(lvAdapterList.get(lvAdapterList.size() - 1));
		final int week = position + 1;
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// to do someThing..

				Intent intent = new Intent(CourseMainActivity.this,
						CourseDetailActivity.class);
				intent.putExtra("week", week);
				intent.putExtra("classtime", position + 1); // classtime;
				startActivityForResult(intent,
						ETipsContants.RequestCode_CourseMain2Detail);
			}
		});
		return v;
	}

	@Override
	protected void initData() {
		App = (ETipsApplication) this.getApplication();
		property = App.getProperty();
		course = App.getLessonList(); // course maybe null!!!!!
		lvAdapterList = new ArrayList<CourseListViwAdapter>();
		Calendar calendar = Calendar.getInstance();
		DAY_OF_WEEK = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		if (DAY_OF_WEEK == 0) {
			DAY_OF_WEEK = 7;
		}
		reflushWeekTime();
	}

	/**
	 * 首次进入、设置完毕，需要reflushWeekTime 更新Title !!
	 */
	public void reflushWeekTime() {
		property = App.getProperty();
		int recordWeek = Integer.parseInt(property.get("Current_Week"));
		int currentWeek = CalendarManager.getWeekOfYeah() - recordWeek + 1;
		Title_CurrentWeek = "第" + currentWeek + "周";
		if (switichBtn != null
				&& switichBtn.getText().toString().trim().equals("每 日")) {
			tv.setText(Title_CurrentWeek);
		}

	}

	/**
	 * listener
	 * 
	 * @author Jayin Ton
	 * 
	 */
	class mOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int position) {
			setDate(position);

		}

	}

	/**
	 * 这只当前星期几
	 * 
	 * @param position
	 */
	public void setDate(int position) {
		switch (position) {
		case 0:
			tv.setText("星期一");
			break;
		case 1:
			tv.setText("星期二");
			break;
		case 2:
			tv.setText("星期三");
			break;
		case 3:
			tv.setText("星期四");
			break;
		case 4:
			tv.setText("星期五");
			break;
		case 5:
			tv.setText("星期六");
			break;
		case 6:
			tv.setText("星期日");
			break;

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.i("debug", "I am onActivityResult!");
		if (ETipsContants.RequestCode_CourseMain2Detail == requestCode
				&& resultCode == RESULT_OK) {
			int week = data.getIntExtra("week", 1);
			course = App.getLessonList();
			lvList.get(week - 1).setAdapter(
					new CourseListViwAdapter(course.get(week - 1),
							CourseMainActivity.this));
			lvAdapterList.get(week - 1).notifyDataSetChanged();
			lv.setAdapter(new CourseAllWeekAdapter(CourseMainActivity.this,
					course));

		}

	}

	@Override
	protected void onRestart() {

		super.onRestart();
		Log.i("debug", "I am onRestart!");
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.i("debug", "I am onStart!");
	}
	

	/**
	 * Setup a new 3D rotation on the container view.
	 * 
	 * @param position
	 *            the item that was clicked to show a picture, or -1 to show the
	 *            list
	 * @param start
	 *            the start angle at which the rotation must begin
	 * @param end
	 *            the end angle of the rotation
	 */
	private void applyRotation(int position, float start, float end) {
		// Find the center of the container
		final float centerX = container.getWidth() / 2.0f;
		final float centerY = container.getHeight() / 2.0f;

		// Create a new 3D rotation with the supplied parameter
		// The animation listener is used to trigger the next animation
		final Rotate3dAnimation rotation;
		if (position == FLAG_POSITIVE) {
			rotation = new Rotate3dAnimation(start, end, centerX, centerY,
					Rotate3dAnimation.FLAG_POSITIVE);
		} else {
			rotation = new Rotate3dAnimation(start, end, centerX, centerY,
					Rotate3dAnimation.FLAG_NEGATIVE);
		}

		rotation.setDuration(00);
		rotation.setFillAfter(true);
		rotation.setInterpolator(new AccelerateInterpolator());
		rotation.setAnimationListener(new DisplayNextView(position));
		container.startAnimation(rotation);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		initData();
		initLayout();
	}

	/**
	 * This class listens for the end of the first half of the animation. It
	 * then posts a new action that effectively swaps the views when the
	 * container is rotated 90 degrees and thus invisible.
	 */
	private final class DisplayNextView implements Animation.AnimationListener {
		private final int mPosition;

		private DisplayNextView(int position) {
			mPosition = position;
		}

		public void onAnimationStart(Animation animation) {
		}

		public void onAnimationEnd(Animation animation) {
			container.post(new SwapViews(mPosition));
		}

		public void onAnimationRepeat(Animation animation) {
		}
	}

	/**
	 * This class is responsible for swapping the views and start the second
	 * half of the animation.
	 */
	private final class SwapViews implements Runnable {
		private final int mPosition;

		public SwapViews(int position) {
			mPosition = position;
		}

		public void run() {
			final float centerX = container.getWidth() / 2.0f;
			final float centerY = container.getHeight() / 2.0f;
			Rotate3dAnimation rotation;

			if (mPosition > 0) {
				viewpager.setVisibility(View.GONE);
				rely.setVisibility(View.VISIBLE);
				rely.requestFocus();
				rotation = new Rotate3dAnimation(90, 180, centerX, centerY,
						Rotate3dAnimation.FLAG_POSITIVE);
			} else {
				rely.setVisibility(View.GONE);
				viewpager.setVisibility(View.VISIBLE);
				viewpager.requestFocus();
				rotation = new Rotate3dAnimation(90, 0, centerX, centerY,
						Rotate3dAnimation.FLAG_NEGATIVE);
			}
			rotation.setDuration(200);
			rotation.setFillAfter(true);
			rotation.setInterpolator(new AccelerateInterpolator());
			container.startAnimation(rotation);
		}
	}

}
