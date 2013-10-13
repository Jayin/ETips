package com.meizhuo.etips.activities;

import java.security.PublicKey;

import org.jsoup.Jsoup;

import com.meizhuo.etips.common.utils.AndroidUtils;
import com.meizhuo.etips.common.utils.ETipsContants;
import com.meizhuo.etips.common.utils.Elog;
import com.meizhuo.etips.common.utils.JSONParser;
import com.meizhuo.etips.net.utils.SubSystemAPI;
import com.meizhuo.etips.net.utils.TweetAPI;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 
 * 注册验证
 * 
 * @author Jayin Ton
 * 
 */
public class RegistIdentity extends BaseUIActivity {
	private EditText id, subPsw;
	private View ok;
	private String account, psw, nickname;
	private TextView tv_status;
	private boolean isRegisting = false; // 正在注册...

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_regist_identify);
		initData();
		initLayout();
	}

	@Override
	protected void initLayout() {
		id = (EditText) _getView(R.id.acty_regist_identity_id);
		subPsw = (EditText) _getView(R.id.acty_regist_identity_subSystemPsw);
		ok = _getView(R.id.acty_regist_identity_ok);
		tv_status = (TextView) _getView(R.id.acty_regist_identify_status);
		ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isRegisting) {
					new IdentifyTast().execute();
				}

			}
		});
	}

	class IdentifyTast extends AsyncTask<Void, Integer, Boolean> {
		@Override
		protected void onPreExecute() {
			tv_status.setText("正在验证......");
			isRegisting = true;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			switch (values[0]) {
			case 1:
				tv_status.setText("正在验证......");
				break;
			case 2:
				tv_status.setText("验证通过，正在注册......");
				break;
			case 3:
				tv_status.setText("注册成功!");
				break;
			case 4:
				tv_status.setText("子系统验证失败，请确保输入无误！");
				break;
			case 5:
				tv_status.setText("注册失败,邮箱已被占用");
				break;
			case 6:
				tv_status.setText("网络异常，请检查你的网络");
				break;
			case 7:
				tv_status.setText("注册失败,该学号已经注册");
				break;
			case 8:
				tv_status.setText("注册失败");
				break;
			}
		}

		@Override
		protected Boolean doInBackground(Void... params) {

			publishProgress(1);
			if(!AndroidUtils.isNetworkConnected(getContext())){
				publishProgress(6);
				return false;
			}
			String studentID = id.getText().toString().trim();
			SubSystemAPI api = new SubSystemAPI(studentID, subPsw
					.getText().toString());
			try {
				if (api.login()) {
					publishProgress(2);
					TweetAPI tweetAPI = new TweetAPI(getContext());
					String result = tweetAPI.regist(nickname, account, studentID, psw);
					Elog.i(result);
					Elog.i("status-->"+JSONParser.getStatus(result));
					Elog.i("status code--->"+JSONParser.getStatusCode(result));
					if (JSONParser.isOK(result)) {
						publishProgress(3);
						return true;
					} else {
						if (JSONParser.getStatusCode(result) == ETipsContants.SC_Email_has_regist) {
							publishProgress(5);
							return false;
						} else if (JSONParser.getStatusCode(result) == ETipsContants.SC_ID_HasRegist) {
							publishProgress(7);
							return false;
						}
						publishProgress(8);
						return false;
					}

				} else {
					publishProgress(4);
					return false;
				}

			} catch (Exception e) {
				e.printStackTrace();
				publishProgress(6);
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			isRegisting = false;
			if (result) {
				toast("注册成功");
				openActivity(TweetLogin.class);
				closeActivity();
			}
		}
	}

	@Override
	protected void initData() {
		account = getIntent().getStringExtra("account");
		nickname = getIntent().getStringExtra("nickname");
		psw = getIntent().getStringExtra("psw");
	}

}
