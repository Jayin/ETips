package com.meizhuo.etips.activities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.meizhuo.etips.app.ClientConfig;
import com.meizhuo.etips.common.AndroidUtils;
import com.meizhuo.etips.common.ETipsContants;
import com.meizhuo.etips.common.ETipsUtils;
import com.meizhuo.etips.common.Elog;
import com.meizhuo.etips.common.JSONParser;
import com.meizhuo.etips.common.SP;
import com.meizhuo.etips.common.StringUtils;
import com.meizhuo.etips.net.utils.TweetAPI;
import com.meizhuo.etips.service.ETipsCoreService;
import com.meizhuo.etips.ui.dialog.LoadingDialog;

import android.content.Intent;
import android.drm.DrmStore.RightsStatus;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 登录界面 11.20 : 服务端返回数据有改动，登录成功时返回用户的nickname & id
 * 
 * @author Jayin Ton
 * 
 */
public class TweetLogin extends BaseUIActivity implements OnClickListener {
	private final int _toLogin = 0, _toUser = 1;
	private LinearLayout toLogin, toUser;
	private View login, loginOut, regist;
	private TextView tv_nickName, tv_loginTime,  tv_account; 
	private EditText et_account, et_psw;
	private boolean isLogin = false;
	private String nickName = "", account = "", loginTime = "";
	private LoadingDialog dialog;

	private boolean isLogining = false; // 正在登录，防止点击登录，在继续登录

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_tweetlogin);
		initData();
		initLayout();
	}

	@Override
	protected void initLayout() {
		toUser = (LinearLayout) _getView(R.id.acty_tweetlogin_frame_toUser);
		toLogin = (LinearLayout) _getView(R.id.acty_tweetlogin_frame_tologin);
		login = _getView(R.id.acty_tweetlogin_ok);
		loginOut = _getView(R.id.acty_tweetlogin_loginOut);
		regist = _getView(R.id.acty_tweetlogin_regist);
		tv_nickName = (TextView) _getView(R.id.acty_tweetlogin_frame_tv_nickname);
		tv_loginTime = (TextView) _getView(R.id.acty_tweetlogin_frame_tv_loginTime);
		et_account = (EditText) _getView(R.id.acty_tweetlogin_account);
		et_psw = (EditText) _getView(R.id.acty_tweetlogin_psw);
		tv_account = (TextView) _getView(R.id.acty_tweetlogin_frame_tv_account);

		login.setOnClickListener(this);
		loginOut.setOnClickListener(this);
		regist.setOnClickListener(this);

		if (isLogin) {
			switchView(_toUser);
			tv_loginTime.setText(StringUtils.getDateFormat(
					Long.parseLong(loginTime), "yy-mm-dd"));
			tv_nickName.setText(nickName);
			tv_account.setText(account);

		} else {
			switchView(_toLogin);
		}

		dialog = new LoadingDialog(getContext());
		dialog.setLodingText("登录中..");
		dialog.setCancelable(true);

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!"".equals(ClientConfig.getAccount(getContext()))) {
			et_account.setText(ClientConfig.getAccount(getContext()));
		}
	}

	@Override
	protected void initData() {
		isLogin = ETipsUtils.isTweetLogin(getContext());
		if (isLogin) {
			loginTime = ClientConfig.getLoginTime(getContext());
			nickName = ClientConfig.getNickname(getContext());
			account = ClientConfig.getAccount(getContext());
		}
	}

	private void switchView(int toWhich) {
		switch (toWhich) {
		case _toLogin:
			toUser.setVisibility(View.GONE);
			toLogin.setVisibility(View.VISIBLE);
			break;
		case _toUser:
			toLogin.setVisibility(View.GONE);
			toUser.setVisibility(View.VISIBLE);
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.acty_tweetlogin_ok:
			// check network.. login
			if (et_account.getText().toString().equals("")) {
				toast("登录账号不能为空");
				return;
			}
			if (et_psw.getText().toString().equals("")) {
				toast("密码不能为空");
				return;
			}
			if (!StringUtils.isPswVaild(et_psw.getText().toString())) {
				toast("密码格式不正确");
				return;
			}
			if (!AndroidUtils.isNetworkConnected(getContext())) {
				toast("请检查你的网络");
				return;
			}
			// go
			new LoginTask().execute();
			break;

		case R.id.acty_tweetlogin_loginOut:
			// login out..
			ClientConfig.cleanAll(getContext());
			isLogin = false;
			toast("已注销");
			switchView(_toLogin);
			break;
		case R.id.acty_tweetlogin_regist:
			openActivity(Regist.class);
			break;
		}

	}

	class LoginTask extends AsyncTask<Void, Void, Integer> {
		@Override
		protected void onPreExecute() {
			isLogining = true;
			// progress.setVisibility(View.VISIBLE);
			dialog.show();
		}

		@Override
		protected Integer doInBackground(Void... params) {
			try {
				TweetAPI api = new TweetAPI(getContext());
				String result = api.login(et_account.getText().toString(),
						et_psw.getText().toString());
				Elog.i("login ::");
				Elog.i(et_account.getText().toString());
				Elog.i(et_psw.getText().toString());
				Elog.i("result-->" + result);
				// 登录成功后保存账号密码 用户昵称 + 学号,登录时间
				if (JSONParser.isOK(result)) {
					JSONArray jsonArray = JSONParser.getResponse(result);
					try {
						JSONObject jobj = jsonArray.getJSONObject(0);
						ClientConfig.setAccount(getContext(), et_account
								.getText().toString());
						ClientConfig.setLoginTime(getContext(),
								System.currentTimeMillis() + "");
						ClientConfig.setNickName(getContext(),
								jobj.getString("nickname"));
						ClientConfig.setUserId(getContext(),
								jobj.getString("id"));
						ClientConfig.setUserPsw(getContext(), et_psw.getText()
								.toString());

					} catch (Exception e) {
						e.printStackTrace();
						return 0; // 服务器返回数据有误
					}
					return 1; // 登录成功
				} else {
					if (result == null)
						return 3;
					if (JSONParser.getStatusCode(result) == 205)
						return 4;
					return 2; // 密码错误 204
				}
			} catch (Exception e) {
				return 5;
			}
		}

		@Override
		protected void onPostExecute(Integer result) {
			// progress.setVisibility(View.GONE);
			dialog.dismiss();
			isLogining = false;
			switch (result) {
			case 0:
				toast("服务器返回数据有误");
				break;
			case 1:
				toast("登录成功");
				// 去获取消息
				Intent service = new Intent(getContext(),
						ETipsCoreService.class);
				service.setAction(ETipsContants.Action_Service_Check_Comment);
				startService(service);
				closeActivity();
				break;
			case 2:
				toast("密码错误");
				break;
			case 3:
				toast("服务器错误");
				break;
			case 4:
				toast("邮箱不存在");
				break;

			case 5:
				toast("网络异常");
				break;
			}

		}

	}
}
