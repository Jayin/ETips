package com.meizhuo.etips.activities;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.meizhuo.etips.common.utils.ETipsContants;
import com.meizhuo.etips.common.utils.Elog;
import com.meizhuo.etips.common.utils.PathBuilder;
import com.meizhuo.etips.common.utils.ShareManager;
import com.meizhuo.etips.net.utils.WYUNewsAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.UMImage;

public class SchoolNewsDetailActivity extends BaseUIActivity {
	private String linkPath = null, title = null, content = null;
	private View backBtn, shareBtn;
	private ProgressBar pb;
	private WebView webview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_schoolnew_detail);
		initData();
		initLayout();
		onWork();
	}

	private void initWebView(WebView webview) {
		WebSettings webSettings = webview.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportZoom(true);
		webSettings.setAllowFileAccess(true);
		webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		webSettings.setDefaultTextEncodingName("utf-8");
		webSettings.setSupportMultipleWindows(true);
		webSettings.setLoadsImagesAutomatically(true); // 自动加载图片
		webview.setBackgroundResource(R.color.lightblue);
		webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN); // 自动适配图片大小
		webview.setWebViewClient(new WebViewClient() {
			@Override
			public void onLoadResource(WebView view, String url) {

			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				Uri uri = Uri.parse(url);
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
				return true;
			}

		});
	}

	private void onWork() {
		if (webview != null && (webview.getVisibility() == View.VISIBLE)) {

			Toast.makeText(SchoolNewsDetailActivity.this, "已更新",
					Toast.LENGTH_SHORT).show();
		} else {
			SNDHandler handler = new SNDHandler();
			new SNDThread(handler).start();
		}
	}

	@Override
	protected void initLayout() {
		backBtn = this.findViewById(R.id.acty_schoolnews_detail_back);
		shareBtn = this.findViewById(R.id.acty_schoolnews_detail_share);

		pb = (ProgressBar) this
				.findViewById(R.id.acty_schoolnews_detail_progressBar);
		webview = (WebView) this
				.findViewById(R.id.acty_schoolnews_detail_webview);
		initWebView(webview);
		backBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				SchoolNewsDetailActivity.this.finish();
			}
		});
		shareBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String content = "#邑大新闻#" + title + " 详情："
						+ PathBuilder.getSchoolNewsDetailPath(linkPath)
						+ " (分享自ETips客户端)";
				ShareManager sm = new ShareManager(content);
				sm.shareToSina(getContext(), new SnsPostListener() {

					@Override
					public void onStart() {

					}

					@Override
					public void onComplete(SHARE_MEDIA arg0, int arg1,
							SocializeEntity arg2) {

					}
				});
			}
		});

	}

	@Override
	protected void initData() {
		linkPath = getIntent().getStringExtra("linkPath");
		title = getIntent().getStringExtra("title");
		content = getIntent().getStringExtra("content");
	}

	class SNDHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ETipsContants.Start:
				pb.setVisibility(View.VISIBLE);
				webview.setVisibility(View.GONE);
				break;
			case ETipsContants.Logining:
				break;
			case ETipsContants.Downloading:
				break;
			case ETipsContants.Finish:// update UI;
				String content = (String) msg.obj;
				Intent intent = getIntent();
				intent.putExtra("content", content);
				setResult(RESULT_OK, intent);
				if (content.equals("403")) {
					content = "<html><body>该页面只能在邑大内网访问，建议在校园wifi环境下访问该页面!</html></body>";
				}
				// tv_content.setText(content);
				webview.loadDataWithBaseURL(null, content, "text/html", null,
						null);
				// webview.loadData(content, "text/html", "gb2312");
				pb.setVisibility(View.GONE);
				webview.setVisibility(View.VISIBLE);
				break;
			case ETipsContants.Fail:
				setResult(RESULT_CANCELED, getIntent());
				pb.setVisibility(View.GONE);
				webview.setVisibility(View.GONE);
				Toast.makeText(SchoolNewsDetailActivity.this, (String) msg.obj,
						Toast.LENGTH_SHORT).show();
				break;
			}
		}
	}

	class SNDThread extends Thread {
		Handler handler;

		public SNDThread(Handler handler) {
			this.handler = handler;
		}

		@Override
		public void run() {
			handler.sendEmptyMessage(ETipsContants.Start);
			Message msg = handler.obtainMessage();
			WYUNewsAPI api = new WYUNewsAPI();
			try {
				content = api.getSchoolNewsDetails(linkPath);
				if (content == null) {
					msg.what = ETipsContants.Fail;
					msg.obj = "网络好像不太给力哦！";
				} else if (content.equals("403")) {
					msg.what = ETipsContants.Finish;
					msg.obj = content;
				} else {
					msg.what = ETipsContants.Finish;
					msg.obj = content;
				}
				handler.sendMessage(msg);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				msg.what = ETipsContants.Fail;
				msg.obj = "网络错误！";
				handler.sendMessage(msg);
			} catch (IOException e) {
				e.printStackTrace();
				msg.what = ETipsContants.Fail;
				msg.obj = "网络错误！";
				handler.sendMessage(msg);
			}
		}
	}
}
