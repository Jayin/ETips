package com.meizhuo.etips.activities;

import com.meizhuo.etips.common.utils.AndroidUtils;
import com.meizhuo.etips.common.utils.ETipsContants;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class LibraryMainActivity extends BaseUIActivity {
	private EditText et_input;
	private View loginBtn;
	private RelativeLayout searchBtn, collectBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_library_main);
		initData();
		initLayout();
	}

	@Override
	protected void initLayout() {
		et_input = (EditText) this
				.findViewById(R.id.acty_library_main_search_edit);
		// backBtn = (Button) this.findViewById(R.id.acty_library_main_back);
		loginBtn = this.findViewById(R.id.acty_library_main_login);

		searchBtn = (RelativeLayout) this
				.findViewById(R.id.acty_library_main_commonSearch);
		collectBtn = (RelativeLayout) this
				.findViewById(R.id.acty_library_main_collections);

		loginBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// login .//
				Intent intent = new Intent(LibraryMainActivity.this,
						LibLoginActivity.class);
				startActivity(intent);
			}
		});

		searchBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String keyword = et_input.getText().toString().trim();
				if (keyword == null || keyword.equals("")) {
					Toast.makeText(LibraryMainActivity.this, "亲,你要搜哪本书？",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (!AndroidUtils.isNetworkConnected(getContext())) {
					toast("请检查你的网络!");
					return;
				}
				Intent intent = new Intent(LibraryMainActivity.this,
						LibSearchResultActivity.class);
				intent.putExtra("keyword", keyword);
				startActivityForResult(intent,
						ETipsContants.RequestCode_LibraryMain2SearchResult);

			}
		});

		collectBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				openActivity(BookCollection.class);

			}
		});
	}

	@Override
	protected void initData() {

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == ETipsContants.RequestCode_LibraryMain2SearchResult
				&& resultCode != RESULT_OK) {
			String error = data.getStringExtra("error");
			Toast.makeText(LibraryMainActivity.this, error, Toast.LENGTH_SHORT)
					.show();
		}
	}
}
