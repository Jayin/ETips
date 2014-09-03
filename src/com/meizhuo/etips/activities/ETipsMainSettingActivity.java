package com.meizhuo.etips.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
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
 * 主设置页面
 * 
 * @author Jayin Ton
 * 
 */
public class ETipsMainSettingActivity extends BaseUIActivity implements
		OnClickListener {
	private View shareBtn, aboutBtn, declaration, cleanAuthorization,
			currentWeekBtn;// queryClassroom;

	private TextView tv_crrentWeek;
	private DeclarationDialog dialog;
	private SetCurrentWeekDialog setCurrentWeekDialog;

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_etips_main_setting);
		initData();
		initLayout();
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			closeActivity();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override protected void initLayout() {
		shareBtn = _getView(R.id.acty_etips_main_setting_rely_share);
		aboutBtn = _getView(R.id.acty_etips_main_setting_rely_aboutus);
		declaration = _getView(R.id.acty_etips_main_setting_rely_declaration);
		cleanAuthorization = _getView(R.id.acty_etips_main_setting_rely_cleanAuthorization);
		currentWeekBtn = _getView(R.id.rely_currentWeek);

		tv_crrentWeek = (TextView) _getView(R.id.tv_currentWeek);

		shareBtn.setOnClickListener(this);
		aboutBtn.setOnClickListener(this);
		declaration.setOnClickListener(this);
		cleanAuthorization.setOnClickListener(this);
		currentWeekBtn.setOnClickListener(this);

		setCurrentWeekDialog = new SetCurrentWeekDialog(getContext(),
				tv_crrentWeek);
	}

	@Override protected void initData() {

	}

	@Override protected void onResume() {
		super.onResume();
		initData();

		tv_crrentWeek.setText(ETipsUtils.getCurrentWeek(getContext()) + "");
	}

	@Override protected void onActivityResult(int requestCode, int resultCode,
			Intent data) {
		initData();
	}

	@Override public void onClick(View v) {
		switch (v.getId()) {
		case R.id.acty_etips_main_setting_rely_aboutus:
			openActivity(AboutActivity.class);
			break;
		case R.id.acty_etips_main_setting_rely_share:
			share();
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
			// 修改周数
			setCurrentWeekDialog.show();
			break;
		default:
			break;
		}

	}

	private void cleanAuth() {

		ShareManager sm = new ShareManager();
		sm.cleanSinaOAuth(getContext(), new SocializeClientListener() {

			@Override public void onStart() {

			}

			@Override public void onComplete(int arg0, SocializeEntity arg1) {
				toast("已取消新浪微博授权");
			}
		});
	}

	private void share() {
		ShareManager sm = new ShareManager(getString(R.string.share_content));
		sm.shareToSina(ETipsMainSettingActivity.this, new SnsPostListener() {

			@Override public void onStart() {

			}

			@Override public void onComplete(SHARE_MEDIA arg0, int arg1,
					SocializeEntity arg2) {

			}
		});

	}

}
