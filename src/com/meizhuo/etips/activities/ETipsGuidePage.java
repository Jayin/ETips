package com.meizhuo.etips.activities;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class ETipsGuidePage extends BaseUIActivity {
	private ViewPager viewPager;
	private List<View> list;
	private View ok;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_guidepage);
		viewPager = (ViewPager) findViewById(R.id.acty_guidepage_viewpage);
		list = new ArrayList<View>();
		list.add(inflate(R.drawable.guide_schoolinfo));
		list.add(inflate(R.drawable.guide_library));
		list.add(inflate(R.drawable.guide_classroom));
		list.add(inflate(R.drawable.guide_notepaper));
		ok = list.get(3).findViewById(R.id.item_guidepage_ok);
		ok.setVisibility(View.VISIBLE);
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				openActivity(ETipsMainActivity.class);
				closeActivity();
			}
		});

		viewPager.setAdapter(new MyPageAdapter());
		viewPager.setDrawingCacheEnabled(true);

	}

	private View inflate(int resID) {
		View v = LayoutInflater.from(this).inflate(R.layout.item_guidepage,
				null);
		v.setBackgroundResource(resID);
		return v;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			closeActivity();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void initLayout() {

	}

	@Override
	protected void initData() {

	}

	class MyPageAdapter extends PagerAdapter {

		@Override
		public int getCount() {

			return list.size();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView(list.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			((ViewPager) container).addView(list.get(position));
			return list.get(position);
		}

		@Override
		public boolean isViewFromObject(View v, Object obj) {
			return v == obj;
		}

	}

}
