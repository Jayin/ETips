package com.meizhuo.etips.activities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.meizhuo.etips.common.utils.AndroidUtils;
import com.meizhuo.etips.common.utils.ETipsContants;
import com.meizhuo.etips.common.utils.ETipsUtils;
import com.meizhuo.etips.common.utils.Elog;
import com.meizhuo.etips.common.utils.JSONParser;
import com.meizhuo.etips.common.utils.SP;
import com.meizhuo.etips.common.utils.StringUtils;
import com.meizhuo.etips.net.utils.TweetAPI;

import android.drm.DrmStore.RightsStatus;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * 登录界面
 * 11.20 : 服务端返回数据有改动，登录成功时返回用户的nickname & id
 * @author Jayin Ton
 *
 */
public class TweetLogin extends BaseUIActivity implements OnClickListener {
	private final int _toLogin = 0, _toUser = 1;
	private LinearLayout toLogin, toUser;
	private View login, loginOut,regist;
	private TextView tv_nickName, tv_loginTime, tv_status,tv_account;
	private EditText et_account, et_psw;
	private boolean isLogin = false;
	private String longTime = "", nickName = "",account = "";
	private View progress;
	
	private boolean isLogining = false; //正在登录，防止点击登录，在继续登录

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
		progress = _getView(R.id.acty_tweetlogin_rely_progressBar);
		tv_status = (TextView) _getView(R.id.acty_tweetlogin_tv_loginStatus);
		tv_account = (TextView)_getView(R.id.acty_tweetlogin_frame_tv_account);

		login.setOnClickListener(this);
		loginOut.setOnClickListener(this);
		regist.setOnClickListener(this);
		
		if(isLogin){
			switchView(_toUser);
			tv_loginTime.setText(StringUtils.getDateFormat(Long.parseLong(longTime), "yy-mm-dd"));
			tv_nickName.setText(nickName);
			tv_account.setText(account);
			
		}else{
			switchView(_toLogin);
		}

	}
    @Override
    protected void onResume() {
    	super.onResume();
    	SP msp = new SP(ETipsContants.SP_NAME_User, getContext());
    	if(!msp.getValue("account").equals("null")){
    		et_account.setText(msp.getValue("account"));
    	} 	
    }
	@Override
	protected void initData() {
		isLogin = ETipsUtils.isTweetLogin(getContext());
		if (isLogin) {
			SP msp = new SP(ETipsContants.SP_NAME_User, getContext());
			longTime = msp.getValue("loginTime");
			nickName = msp.getValue("nickname");
			account = msp.getValue("account");	
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
			if(!StringUtils.isPswVaild(et_psw.getText().toString())){
				toast("密码格式不正确");
				return ;
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
			SP msp = new SP(ETipsContants.SP_NAME_User, getContext());
			msp.add("nickname", "null");
			msp.add("psw", "null");
			msp.add("session", "null");
			msp.add("id", "null");
			msp.add("loginTime", "null");
			msp.add("descrpiton", "null");
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
			progress.setVisibility(View.VISIBLE);
		}
  
		@Override
		protected Integer doInBackground(Void... params) {
			TweetAPI api = new TweetAPI(getContext());
			String result = api.login(et_account.getText().toString(), et_psw.getText()
					.toString());
			Elog.i("login ::");
			Elog.i(et_account.getText().toString());
			Elog.i(et_psw.getText().toString());
			Elog.i("result-->"+result);
			//登录成功后保存账号密码 用户昵称 + 学号,登录时间
			if(JSONParser.isOK(result)){
				SP msp = new SP(ETipsContants.SP_NAME_User, getContext());
			    JSONArray jsonArray = JSONParser.getResponse(result);
			    try {
				JSONObject jobj =jsonArray.getJSONObject(0);
					msp.add("account", et_account.getText().toString());
 					msp.add("psw", et_psw.getText()
 							.toString());     
					msp.add("id", "3112002722");
					msp.add("loginTime", System.currentTimeMillis()+"");
					msp.add("nickname", jobj.getString("nickname"));
					msp.add("id", jobj.getString("id"));
				} catch (JSONException e) {
					e.printStackTrace();
					return 0;  //服务器返回数据有误
				}
			    return 1; //登录成功
			}else{
				if(result == null)return 3;
				if(JSONParser.getStatusCode(result)==205)return 4;
                return 2; //密码错误 204
			}
			
		}

		@Override
		protected void onPostExecute(Integer result) {
			progress.setVisibility(View.GONE);
			isLogining = false;
			switch (result) {
			case 0:
				toast("服务器返回数据有误");
				break;
			case 1:
				toast("登录成功");
				closeActivity();
				break;
			case 2 :
				toast("密码错误");
				break;
			case 3: 
				toast("服务器错误");
				break;
			case 4:
				toast("邮箱不存在");
				break;
			}
			
		}

	}
}
