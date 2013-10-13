package com.meizhuo.etips.activities;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.baidu.sharesdk.BaiduShareException;
import com.baidu.sharesdk.BaiduSocialShare;
import com.baidu.sharesdk.ShareContent;
import com.baidu.sharesdk.ShareListener;
import com.baidu.sharesdk.Utility;
import com.baidu.sharesdk.ui.BaiduSocialShareUserInterface;
import com.meizhuo.etips.common.utils.ETipsContants;

public class ETipsMainSettingActivity extends BaseUIActivity implements
		OnClickListener {
	private RelativeLayout shareBtn, aboutBtn;
	private Button backBtn;
	private BaiduSocialShare bss = null;
	private BaiduSocialShareUserInterface bssUI = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_etips_main_setting);
		initData();
		initLayout();
	}

	@Override
	protected void initLayout() {
		shareBtn = (RelativeLayout) _getView(R.id.acty_etips_main_setting_rely_share);
		backBtn = (Button) _getView(R.id.acty_etips_main_setting_back);
		aboutBtn = (RelativeLayout) _getView(R.id.acty_etips_main_setting_rely_aboutus);

		shareBtn.setOnClickListener(this);
		backBtn.setOnClickListener(this);
		aboutBtn.setOnClickListener(this);
	}

	@Override
	protected void initData() {

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
		}

	}

	private void share() {
		final Handler handler = new Handler();
		bss = BaiduSocialShare.getInstance(ETipsMainSettingActivity.this,
				ETipsContants.BaiduShareAppKey);
		bssUI = bss.getSocialShareUserInterfaceInstance();
		toast("感谢你的分享！");
		ShareContent sc = new ShareContent();
		sc.setContent("我正在使用邑大校园生活助手#ETips#！一键录入课程表、与学校图书馆对接，查个人借阅信息，图书检索、集成《学生手册》、与学校新闻资讯同步、对接子系统，一键查询成绩，计算当前绩点、还可以查询宿舍用电信息！");
		sc.setImageUrl(null);
		sc.setUrl("http://t.cn/zQKiqgC");
		sc.setTitle("ETips");
		if (bss.isAccessTokenValid(Utility.SHARE_TYPE_SINA_WEIBO)) {
			bss.share(ETipsMainSettingActivity.this,
					Utility.SHARE_TYPE_SINA_WEIBO, sc, new ShareListener() {

						@Override
						public void onError(BaiduShareException arg0) {
							handler.post(new Runnable() {
								@Override
								public void run() {
									toast("分享失败！");
								}
							});

						}

						@Override
						public void onAuthComplete(Bundle arg0) {

						}

						@Override
						public void onApiComplete(String arg0) {
							handler.post(new Runnable() {
								@Override
								public void run() {
									toast("分享成功！");
								}
							});

						}
					});
		} else {
			bss.authorize(ETipsMainSettingActivity.this,
					Utility.SHARE_TYPE_SINA_WEIBO, new ShareListener() {
						@Override
						public void onError(BaiduShareException arg0) {
							handler.post(new Runnable() {
								@Override
								public void run() {
									toast("授权失败！");
								}
							});
						}

						@Override
						public void onAuthComplete(Bundle arg0) {
							share();
						}

						@Override
						public void onApiComplete(String arg0) {

						}
					});
		}

	}

}
