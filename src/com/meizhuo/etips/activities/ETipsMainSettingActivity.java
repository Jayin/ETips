package com.meizhuo.etips.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.meizhuo.etips.common.ETipsUtils;
import com.meizhuo.etips.common.ShareManager;
import com.meizhuo.etips.ui.dialog.DeclarationDialog;
import com.meizhuo.etips.ui.dialog.SetCurrentWeekDialog;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.controller.listener.SocializeListeners.SocializeClientListener;

/**
 * 主设置页面 写得很粗糙 = =
 * 
 * @author Jayin Ton
 * 
 */
public class ETipsMainSettingActivity extends BaseUIActivity implements
		OnClickListener {
	private View backBtn, shareBtn, aboutBtn, manualBtn, account, checkSource,
			declaration, cleanAuthorization, currentWeekBtn;// queryClassroom;

	private boolean isETipsAccountLogin = false, isloginTimeOut = false;
	private TextView tv_AccountInfo, tv_crrentWeek;
	private DeclarationDialog dialog;
private SetCurrentWeekDialog setCurrentWeekDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_etips_main_setting);
		initData();
		initLayout();
	}

	@Override
	protected void initLayout() {
		shareBtn = _getView(R.id.acty_etips_main_setting_rely_share);
		backBtn = _getView(R.id.acty_etips_main_setting_back);
		aboutBtn = _getView(R.id.acty_etips_main_setting_rely_aboutus);
		manualBtn = _getView(R.id.acty_etips_main_setting_rely_manual);
		account = _getView(R.id.acty_etips_main_setting_rely_account);
		checkSource = _getView(R.id.acty_etips_main_setting_rely_checkSource);
		declaration = _getView(R.id.acty_etips_main_setting_rely_declaration);
		cleanAuthorization = _getView(R.id.acty_etips_main_setting_rely_cleanAuthorization);
		currentWeekBtn = _getView(R.id.rely_currentWeek);

		tv_crrentWeek = (TextView) _getView(R.id.tv_currentWeek);
		tv_AccountInfo = (TextView) _getView(R.id.acty_etips_main_setting_tv_accountInfo);
		
		if (isETipsAccountLogin) {
			if (isloginTimeOut) {
				tv_AccountInfo.setText("ETips账户登录失效，请重新登录");
			} else {
				tv_AccountInfo.setText("已登录ETips账户");
			}
		}

		shareBtn.setOnClickListener(this);
		backBtn.setOnClickListener(this);
		aboutBtn.setOnClickListener(this);
		manualBtn.setOnClickListener(this);
		account.setOnClickListener(this);
		checkSource.setOnClickListener(this);
		declaration.setOnClickListener(this);
		cleanAuthorization.setOnClickListener(this);
		currentWeekBtn.setOnClickListener(this);
		
		setCurrentWeekDialog = new SetCurrentWeekDialog(getContext(),tv_crrentWeek);
	}

	@Override
	protected void initData() {
//		SharedPreferences sp = this.getSharedPreferences(
//				ETipsContants.SP_NAME_User, Context.MODE_PRIVATE);
		isETipsAccountLogin = ETipsUtils.isTweetLogin(this);
		if (isETipsAccountLogin) {
			isloginTimeOut = ETipsUtils.isTweetLoginTimeOut(this);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		initData();
		if (isETipsAccountLogin) {
			if (isloginTimeOut) {
				tv_AccountInfo.setText("ETips账户登录失效，请重新登录");
			} else {
				tv_AccountInfo.setText("已登录ETips账户");
			}
		} else {
			tv_AccountInfo.setText("ETips账户登录");
		}
		tv_crrentWeek.setText(ETipsUtils.getCurrentWeek(getContext())+"");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		initData();
		if (isETipsAccountLogin) {
			if (isloginTimeOut) {
				tv_AccountInfo.setText("ETips账户登录失效，请重新登录");
			} else {
				tv_AccountInfo.setText("已登录ETips账户");
			}
		} else {
			tv_AccountInfo.setText("ETips账户登录");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.acty_etips_main_setting_back:
			this.finish();
			break;
		case R.id.acty_etips_main_setting_rely_aboutus:
			openActivity(AboutActivity.class);
			break;
		case R.id.acty_etips_main_setting_rely_share:
			share();
			break;
		case R.id.acty_etips_main_setting_rely_manual:
			openActivity(ManualMainActivity.class);
			break;
		case R.id.acty_etips_main_setting_rely_account:
			startActivityForResult(wrapIntent(TweetLogin.class), 1);
			// openActivity(TweetLogin.class);
			// closeActivity();
			break;
		case R.id.acty_etips_main_setting_rely_checkSource:
			Intent intent = new Intent(getContext(),
					SubSystemLoginActivity.class);
			intent.putExtra("toWhere", "ScoreRecordActivity");
			openActivity(intent);
			break;
		case R.id.acty_etips_main_setting_rely_declaration:
			if (dialog == null) {
				dialog = new DeclarationDialog(getContext());
				dialog.show();
			} else if (!dialog.isShowing()) {
				dialog.show();
			}
			break;
		case R.id.acty_etips_main_setting_rely_cleanAuthorization:
			cleanAuth();
			break;
		case R.id.rely_currentWeek:
			//修改周数
			setCurrentWeekDialog.show();
			break;
		default:
			break;
		}

	}

	private void cleanAuth() {

		ShareManager sm = new ShareManager();
		sm.cleanSinaOAuth(getContext(), new SocializeClientListener() {

			@Override
			public void onStart() {

			}

			@Override
			public void onComplete(int arg0, SocializeEntity arg1) {
				toast("已取消新浪微博授权");
			}
		});
	}

	private void share() {
		ShareManager sm = new ShareManager(
				getString(R.string.share_content));
		sm.shareToSina(ETipsMainSettingActivity.this, new SnsPostListener() {

			@Override
			public void onStart() {

			}

			@Override
			public void onComplete(SHARE_MEDIA arg0, int arg1,
					SocializeEntity arg2) {

			}
		});

	}

}
