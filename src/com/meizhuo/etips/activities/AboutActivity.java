package com.meizhuo.etips.activities;

import com.meizhuo.etips.common.utils.AndroidUtils;

import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AboutActivity extends BaseUIActivity {
	private Button backBtn;
	private TextView tv_version;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_about);
		backBtn = (Button) this.findViewById(R.id.acty_about_back);
		backBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AboutActivity.this.finish();
			}
		});
	}

	@Override
	protected void initLayout() {
		tv_version = (TextView) _getView(R.id.textView8);
		try {
			tv_version
					.setText("版本:" + AndroidUtils.getAppVersionName(getContext()));
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void initData() {
	}

}
