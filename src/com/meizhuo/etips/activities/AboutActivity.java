package com.meizhuo.etips.activities;

import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.widget.TextView;

import com.meizhuo.etips.common.AndroidUtils;
/**
 * 关于页面
 * @author Jayin Ton
 *
 */
public class AboutActivity extends BaseUIActivity {
	private TextView tv_version;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_about);
		initData();
		initLayout();
		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	protected void initLayout() {

		tv_version = (TextView) _getView(R.id.tv_version);
		try {
			tv_version.setText( AndroidUtils.getAppVersionName(getContext()));
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void initData() {
	}

}
