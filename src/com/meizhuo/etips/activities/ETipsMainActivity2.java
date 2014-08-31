package com.meizhuo.etips.activities;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.meizhuo.etips.fragment.CourseTable;
import com.meizhuo.etips.fragment.Explore;
import com.meizhuo.etips.fragment.NewFeeds;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

/**
 * share :只支持单用户分享~！
 * 
 * @author Jayin Ton
 * 
 */
public class ETipsMainActivity2 extends BaseActivity {
	@InjectView(R.id.tabs) com.astuetz.PagerSlidingTabStrip mPagerSlidingTabStrip;
	@InjectView(R.id.viewpager) ViewPager mViewPager;
	
	List<Fragment> fragments = new ArrayList<Fragment>();

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.acty_etips_main_4);
		
		fragments.add(new NewFeeds());
		fragments.add(new CourseTable());
		fragments.add(new Explore());
		
		mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), fragments));
		mPagerSlidingTabStrip.setViewPager(mViewPager);
	}

	@Override protected void initLayout() {

	}

	@Override protected void initData() {

	}

	public class MyPagerAdapter extends FragmentPagerAdapter {

		List<Fragment> fragments;

		private final String[] TITLES = { "新鲜事", "课程表", "发现" };

		public MyPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
			super(fm);
			this.fragments = fragments;
		}

		@Override public CharSequence getPageTitle(int position) {
			return TITLES[position];
		}

		@Override public int getCount() {
			return TITLES.length;
		}

		@Override public Fragment getItem(int position) {
			return fragments.get(position);
		}

	}

}
