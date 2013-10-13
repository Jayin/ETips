package com.meizhuo.etips.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AboutActivity extends BaseUIActivity {
	private Button backBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_about);
		backBtn=(Button)this.findViewById(R.id.acty_about_back);
		backBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AboutActivity.this.finish();
			}
		});
	}

	@Override
	protected void initLayout() {
	 

	}

	@Override
	protected void initData() {  
	}

}
