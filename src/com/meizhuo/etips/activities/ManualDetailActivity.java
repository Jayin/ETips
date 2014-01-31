package com.meizhuo.etips.activities;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
/**
 * 学生手册-详情
 * @author Jayin Ton
 *
 */
public class ManualDetailActivity extends BaseUIActivity {
	private String URL = null, title = null;
	private WebView webview;
	private View backbtn;
	private ProgressBar pb ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_manual_detail);
		initData();
		initLayout();
		onWork();

	}

	private void onWork() {
		WebSettings webSettings = webview.getSettings();
		// 设置支持放大
		webSettings.setBuiltInZoomControls(true);
		webSettings.setSupportZoom(true);
		webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		webSettings.setDefaultTextEncodingName("utf-8");
		webSettings.setSupportMultipleWindows(true);
		webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if(newProgress == 100){
                  	webview.setVisibility(View.VISIBLE);
                  	pb.setVisibility(View.GONE);
                }
            }
		});
		webview.loadUrl(URL);
	}

	@Override
	protected void initLayout() {
		pb= (ProgressBar) this.findViewById(R.id.acty_manual_detail_progressbar1);
		webview = (WebView) this.findViewById(R.id.acty_manual_detail_webview);
		backbtn =  this.findViewById(R.id.acty_manual_detail_back);
		backbtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ManualDetailActivity.this.finish();
			}
		});
	}

	@Override
	protected void initData() {
		URL = getIntent().getStringExtra("URL");
		title = getIntent().getStringExtra("title");
	}

}
