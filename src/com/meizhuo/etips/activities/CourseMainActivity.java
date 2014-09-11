package com.meizhuo.etips.activities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ViewFlipper;

import com.meizhuo.etips.adapter.CourseAllWeekAdapter;
import com.meizhuo.etips.adapter.CourseListViwAdapter;
import com.meizhuo.etips.adapter.CourseViewPagerAdapter;
import com.meizhuo.etips.common.ETipsContants;
import com.meizhuo.etips.common.ETipsUtils;
import com.meizhuo.etips.common.StringUtils;

import com.meizhuo.etips.model.Lesson;

/**
 * 
 * 全课表
 * 
 * @author Jayin Ton
 * 
 */
public class CourseMainActivity extends BaseUIActivity {
	private List<ListView> lvList;
	private ViewPager viewpager;
	private List<View> views;
	private ETipsApplication App;

	private List<List<List<Lesson>>> course; // 课程
	private List<CourseListViwAdapter> lvAdapterList;
	private int DAY_OF_WEEK = 1; // 1 - 7 means Sunday,Monday.....Saturday
	private ListView lv;
	private RelativeLayout relyAllWeek;// rely显示allweek 的view //
										// container.包含viewpager
	// and rely
	private ViewFlipper flipper;
	private MenuItem mMenuItem;

	@Override protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_course_main);
		try {
			initData();
			// App.setObject(this); //
			// 这里为了方便跳转！CourseMainActivity-》settingActivity
			// ->subloginActivity-》CourseMainActivity
			initLayout();

			setActionBarTitle( "第"+ETipsUtils.getCurrentWeek(getContext())+"周");
		} catch (Exception e) {
			e.printStackTrace();
			// 课表异常了
			this.finish();
			Intent intent = new Intent(this, SubSystemLoginActivity.class);
			intent.putExtra("toWhere", "CourseMainActivity");
			openActivity(intent);
		}
	}

	@Override public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.acty_coursemain, menu);
		mMenuItem = menu.findItem(R.id.switch_model);
		return true;
	}

	@Override public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.switch_model){
			switch_model();
		}else if(item.getItemId() == R.id.update){
			update();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override protected void onResume() {
		super.onResume();

	}

	@Override protected void initLayout() {
		lvList = new ArrayList<ListView>();
		views = new ArrayList<View>();
		lv = (ListView) _getView(R.id.acty_course_main_listview1);
		relyAllWeek = (RelativeLayout) _getView(R.id.acty_course_main_allweek);


		// 初始化viewpager
		viewpager = (ViewPager) this
				.findViewById(R.id.acty_course_main_viewpager);
		for (int i = 0; i < 7; i++) {
			views.add(getView(i));
		}
		viewpager.setAdapter(new CourseViewPagerAdapter(views));
		viewpager.setDrawingCacheEnabled(true);
		viewpager.setOnPageChangeListener(new mOnPageChangeListener());
		viewpager.setCurrentItem(DAY_OF_WEEK - 1);
		// 初始化gone 的 view (allweek)
		lv.setAdapter(new CourseAllWeekAdapter(this, course));

		flipper = (ViewFlipper) this.findViewById(R.id.acty_course_main_middle);
		flipper.removeAllViews();
		flipper.addView(relyAllWeek);
		flipper.addView(viewpager);
		flipper.setInAnimation(getContext(), R.anim.push_up_in);
		flipper.setOutAnimation(getContext(), R.anim.push_up_out);

	}
	
	private void update(){
		Intent intent = new Intent(CourseMainActivity.this,
				SubSystemLoginActivity.class);
		intent.putExtra("toWhere", "CourseMainActivity");
		openActivity(intent);
	}
	
	private void switch_model(){
		String title =(String)mMenuItem.getTitle();
		if (title.equals("整 周")) {
			setActionBarTitle( "第"+ETipsUtils.getCurrentWeek(getContext())+"周");
			mMenuItem.setTitle("每 日");
			viewpager.setCurrentItem(DAY_OF_WEEK - 1); // 改变显示为当日的日期
			flipper.setInAnimation(getContext(), R.anim.push_up_in);
			flipper.setOutAnimation(getContext(), R.anim.push_up_out);
			flipper.showNext();
		} else {
			mMenuItem.setTitle("整 周");
			setDate(DAY_OF_WEEK - 1);
			flipper.setInAnimation(getContext(), R.anim.push_down_in);
			flipper.setOutAnimation(getContext(), R.anim.push_down_out);
			flipper.showPrevious();
		}
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

			@Override public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// to do someThing..
				// 该课程一般为可以上（高亮） or 均不可上 默认为第1个（灰暗）
				Intent intent = new Intent(CourseMainActivity.this,
						CourseDetailActivity.class);
				intent.putExtra("week", week);
				intent.putExtra("classtime", position + 1); // classtime;

				List<Lesson> _lesList = course.get(week - 1).get(position);
				int posOfLessonList = 0;
				if (_lesList.size() > 1) {
					for (int i = 0; i < _lesList.size(); i++) {
						Lesson mLesson = _lesList.get(i);
						if (mLesson == null || mLesson.Time == null
								|| mLesson.Time.equals(""))
							return;
						if (StringUtils
								.parseTimeOfLesson(mLesson.Time)
								.contains(
										ETipsUtils
												.getCurrentWeek(CourseMainActivity.this))) {
							posOfLessonList = i;
						}
					}
				}
				intent.putExtra("position", posOfLessonList);
				startActivityForResult(intent,
						ETipsContants.RequestCode_CourseMain2Detail);
			}
		});
		return v;
	}

	@Override protected void initData() {
		App = (ETipsApplication) this.getApplication();
		course = App.getLessonList(); // course maybe null!!!!!
		lvAdapterList = new ArrayList<CourseListViwAdapter>();
		Calendar calendar = Calendar.getInstance();
		DAY_OF_WEEK = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		if (DAY_OF_WEEK == 0) {
			DAY_OF_WEEK = 7;
		}
	}

	/**
	 * listener
	 * 
	 * @author Jayin Ton
	 * 
	 */
	class mOnPageChangeListener implements OnPageChangeListener {

		@Override public void onPageScrollStateChanged(int arg0) {

		}

		@Override public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override public void onPageSelected(int position) {
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
			setActionBarTitle("星期一");
			break;
		case 1:
			setActionBarTitle("星期二");
			break;
		case 2:
			setActionBarTitle("星期三");
			break;
		case 3:
			setActionBarTitle("星期四");
			break;
		case 4:
			setActionBarTitle("星期五");
			break;
		case 5:
			setActionBarTitle("星期六");
			break;
		case 6:
			setActionBarTitle("星期日");
			break;

		}
	}

	@Override protected void onActivityResult(int requestCode, int resultCode,
			Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

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
		if (ETipsContants.RequestCode_CourseMain2Setting == requestCode
				&& resultCode == RESULT_OK) {
			initData();
			initLayout();
		}

	}

	@Override protected void onRestart() {

		super.onRestart();

	}

	@Override protected void onStart() {

		super.onStart();

	}

	@Override protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		initData();
		initLayout();
		// System.out.println("onNewIntent!!!!");
	}

}
