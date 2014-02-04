package com.meizhuo.etips.activities;

import java.util.List;
import java.util.Map;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.meizhuo.etips.app.ClientConfig;
import com.meizhuo.etips.common.ETipsContants;
import com.meizhuo.etips.model.Course;
import com.meizhuo.etips.model.Lesson;
import com.meizhuo.etips.net.utils.SubSystemAPI;
import com.meizhuo.etips.ui.dialog.WaittingDialog;

/**
 * 登录子系统 登录和登录后的操作应该分离！ 课表那里比较前搞的，所以没有遵循着原则。。。
 * 
 * @author Jayin Ton
 * 
 */
@SuppressLint("HandlerLeak")
public class SubSystemLoginActivity extends BaseUIActivity {
	private String toWhere = null;
	private String userID, userPSW;
	private Button loginBtn, cancleBtn;
	private EditText et_userID, et_userPSW;
	private ETipsApplication App;
	private final int TAG_ScoreRecord = 21, TAG_Lesson = 22;
	private int tag = 0; // design to figure out after Login , sovle what
	private SubSystemAPI api = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_login_subsystem1);
		initData();
		initLayout();

	}

	@Override
	protected void initLayout() {
		et_userID = (EditText) this
				.findViewById(R.id.acty_login_subsystem_userID);
		et_userPSW = (EditText) this
				.findViewById(R.id.acty_login_subsystem_userPSW);
		loginBtn = (Button) this.findViewById(R.id.acty_login_subsystem_login);
		cancleBtn = (Button) this
				.findViewById(R.id.acty_login_subsystem_cancle);

		et_userID.setText(ClientConfig.getUserId(getContext()));
		loginBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				userID = et_userID.getText().toString();
				if (userID == null || userID.equals("")) {
					Toast.makeText(SubSystemLoginActivity.this, "学号不能为空",
							Toast.LENGTH_SHORT).show();
					return;
				}
				userPSW = et_userPSW.getText().toString();
				if (userPSW == null || userPSW.equals("")) {
					Toast.makeText(SubSystemLoginActivity.this, "密码不能为空",
							Toast.LENGTH_SHORT).show();
					return;
				}

				if (toWhere.equals("CourseMainActivity")) {
					tag = TAG_Lesson;
					SSLoginHandler h = new SSLoginHandler();
					new SSLoginThread(h).start();
				} else if (toWhere.equals("ScoreRecordActivity")) {
					tag = TAG_ScoreRecord;
					SSLoginHandler h = new SSLoginHandler();
					new SSLoginThread(h).start();

				}
			}
		});

		cancleBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SubSystemLoginActivity.this.finish();
			}
		});

	}

	@Override
	protected void initData() {
		toWhere = getIntent().getStringExtra("toWhere").trim();
		App = (ETipsApplication) getApplication();
		api = App.getSubSystemAPI();
	}

	class SSLoginHandler extends Handler {
		WaittingDialog dialog;

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ETipsContants.Start:
				dialog = new WaittingDialog(SubSystemLoginActivity.this);
				dialog.show();
				break;
			case ETipsContants.Logining:
				dialog.setText("ETips登陆中...");
				break;
			case ETipsContants.Downloading:
				dialog.setText("ETips下载数据中...");
				break;
			case ETipsContants.Finish: // save data to DB
				if (!dialog.isShowing()) {
					dialog = null;
					break;
				}
				dialog.setText("ETips解析数据中...");
				if (tag == TAG_Lesson) {
					dialog.setText("ETips正在跳转...");
					// 转换
					Course course = Course
							.translateData((Map<Integer, Map<Integer, List<Lesson>>>) msg.obj);
					App.setLessonList(course.getCourseList());
					dialog.dismiss();
					dialog = null;
					startActivity(new Intent(SubSystemLoginActivity.this,
							CourseMainActivity.class));
					SubSystemLoginActivity.this.finish();
				} else if (tag == TAG_ScoreRecord) {
					dialog.setText("ETips正在跳转...");
					Intent intent = new Intent(SubSystemLoginActivity.this,
							ScoreRecordActivity.class);
					intent.putExtra("userID", userID);
					intent.putExtra("userPSW", userPSW);
					startActivity(intent);

					SubSystemLoginActivity.this.finish();
				}
				break;
			case ETipsContants.Fail:
				if (!dialog.isShowing()) {
					dialog = null;
					break;
				}
				dialog.dismiss();
				dialog = null;
				Toast.makeText(SubSystemLoginActivity.this, (String) msg.obj,
						Toast.LENGTH_SHORT).show();
				break;
			}
		}
	}

	class SSLoginThread extends Thread {
		Handler handler;

		public SSLoginThread(Handler h) {
			this.handler = h;
		}

		@Override
		public void run() {
			if (api == null) {
				api = new SubSystemAPI(userID, userPSW);
			}
			api.setUserData(userID, userPSW);
			App.setSubSystemAPI(api);
			try {
				handler.sendEmptyMessage(ETipsContants.Start);
				handler.sendEmptyMessage(ETipsContants.Logining);
				boolean flag = api.login();
				if (flag) {
					if (tag == TAG_Lesson) {
						// deal with lesson
						handler.sendEmptyMessage(ETipsContants.Downloading);
						Map<Integer, Map<Integer, List<Lesson>>> course = api
								.getLessons();
						if (course.isEmpty()) {
							Message msg = handler.obtainMessage();
							msg.what = ETipsContants.Fail;
							msg.obj = "子系统上没有你课程表哦~";
							handler.sendMessage(msg);
						} else {
							Message msg = handler.obtainMessage();
							msg.what = ETipsContants.Finish;
							msg.obj = course;
							handler.sendMessage(msg);
						}
					} else if (tag == TAG_ScoreRecord) {
						// deal with score record
						handler.sendEmptyMessage(ETipsContants.Finish);
					}
				} else {
					Message msg = handler.obtainMessage();
					msg.what = ETipsContants.Fail;
					msg.obj = "登陆信息不正确!";
					handler.sendMessage(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = handler.obtainMessage();
				msg.what = ETipsContants.Fail;
				msg.obj = "网络异常!";
				handler.sendMessage(msg);
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		//保存用户账号
		ClientConfig.setUserId(getContext(), et_userID.getText().toString()
				.trim());
	}
}
