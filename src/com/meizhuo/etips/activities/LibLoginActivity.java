package com.meizhuo.etips.activities;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.meizhuo.etips.common.ETipsContants;
import com.meizhuo.etips.net.utils.LibraryAPI;
import com.meizhuo.etips.ui.dialog.WaittingDialog;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LibLoginActivity extends BaseUIActivity {
	private EditText et_userID, et_userPSW;
	private Button okBtn, cancleBtn;
	private String userID, userPSW;
	private ETipsApplication App;
	private LibraryAPI api;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_login_library);
		initData();
		initLayout();
	}

	@Override
	protected void initLayout() {
		et_userID = (EditText) this
				.findViewById(R.id.acty_login_library_userID);
		et_userPSW = (EditText) this
				.findViewById(R.id.acty_login_library_userPSW);
		okBtn = (Button) this.findViewById(R.id.acty_login_library_login);
		cancleBtn = (Button) this.findViewById(R.id.acty_login_library_cancle);

		cancleBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LibLoginActivity.this.finish();

			}
		});
		okBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				userID = et_userID.getText().toString().trim();
				if (userID == null || userID.equals("")) {
					Toast.makeText(LibLoginActivity.this, "学号不能为空",
							Toast.LENGTH_SHORT).show();
					return;
				}
				userPSW = et_userPSW.getText().toString().trim();
				if (userPSW == null || userPSW.equals("")) {
					Toast.makeText(LibLoginActivity.this, "密码不能为空",
							Toast.LENGTH_SHORT).show();
					return;
				}
				LibLoginHanlder handler = new LibLoginHanlder();
				new LibLoginThread(handler).start();
			}
		});

	}

	@Override
	protected void initData() {
		App = (ETipsApplication) this.getApplication();
		api = App.getLibraryAPI();
	}

	class LibLoginHanlder extends Handler {
		WaittingDialog dialog;

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ETipsContants.Start:
				dialog = new WaittingDialog(LibLoginActivity.this);
				dialog.show();
				break;
			case ETipsContants.Logining:
				dialog.setText("ETips登陆中...");
				break;
			case ETipsContants.Finish: // save data to DB
				if (!dialog.isShowing()) {
					dialog = null;
					break;
				}
				// ...do something
				dialog.dismiss();
				dialog = null;
				Intent intent = new Intent(LibLoginActivity.this,
						LibUserInfoActivity.class);
				intent.putExtra("userID", userID);
				startActivity(intent);

				LibLoginActivity.this.finish();
				break;
			case ETipsContants.Fail:
				if (!dialog.isShowing()) {
					dialog = null;
					break;
				}
				dialog.dismiss();
				dialog = null;
				Toast.makeText(LibLoginActivity.this, (String) msg.obj,
						Toast.LENGTH_SHORT).show();
				break;

			}
		}
	}

	class LibLoginThread extends Thread {
		Handler handler;

		public LibLoginThread(Handler handler) {
			this.handler = handler;
		}

		@Override
		public void run() {
			handler.sendEmptyMessage(ETipsContants.Start);
			if (api == null) {
				api = new LibraryAPI();	
			}
			api.setUserData(userID, userPSW);
			App.setLibraryAPI(api);
			try {
				if (api.Login()) {
					handler.sendEmptyMessage(ETipsContants.Finish);
				} else {
					Message msg = new Message();
					msg.what = ETipsContants.Fail;
					msg.obj = "登陆失败!";
					handler.sendMessage(msg);
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = ETipsContants.Fail;
				msg.obj = "登陆失败!";
				handler.sendMessage(msg);
			} catch (IOException e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = ETipsContants.Fail;
				msg.obj = "登陆失败!";
				handler.sendMessage(msg);
			}
		}
	}
}
