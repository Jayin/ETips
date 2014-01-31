package com.meizhuo.etips.activities;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.meizhuo.etips.common.utils.ETipsContants;
import com.meizhuo.etips.model.*;
import com.meizhuo.etips.net.utils.LibraryAPI;
import com.meizhuo.etips.ui.LibSearchResultListViewAdapter;
import com.meizhuo.etips.ui.WaittingDialog;

public class LibSearchResultActivity extends BaseUIActivity {
	private String keyword;
	private int totalPage = -1;  //-1 means having not finishing first download wrok
	private int page = 1;
	private TextView tv_title, tv_pageInfo;
	
	private View backBtn, prevBtn, nextBtn;
	private ListView lv;
	private List<BookInfo> list;
	private String error;
	private LibraryAPI api;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_library_searchresult);
		initData();
		initLayout();
		onWork();
	}

	private void onWork() {
		LibSRHandler handler = new LibSRHandler();
		new LibSRThread(handler).start();
	}

	@Override
	protected void initLayout() {
		tv_title = (TextView) this
				.findViewById(R.id.acty_library_searchresult_title_tv);
		tv_pageInfo = (TextView) this
				.findViewById(R.id.acty_library_searchresult_page);
		backBtn = this
				.findViewById(R.id.acty_library_searchresult_back);
		prevBtn =  this
				.findViewById(R.id.acty_library_searchresult_prev);
		nextBtn =  this
				.findViewById(R.id.acty_library_searchresult_next);
		lv = (ListView) this
				.findViewById(R.id.acty_library_searchresult_listview);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(LibSearchResultActivity.this,
						LibBookDetailActivity.class);
				intent.putExtra("from", "LibSearchResultActivity");
				intent.putExtra("BookInfo", list.get(position));
				openActivity(intent);
			}
		});
		tv_title.setText(keyword);
		setResult(RESULT_OK);
		backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(RESULT_OK);
				LibSearchResultActivity.this.finish();
			}
		});

		prevBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				 if(totalPage == -1){
					 onWork();
				 }else{
					 if(page-1>=1){
						 page--;
						 onWork();
					 }else{
						 Toast.makeText(LibSearchResultActivity.this, "囧~已经是首页了！", Toast.LENGTH_SHORT).show();
					 }
				 }
			}
		});

		nextBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				 if(totalPage != -1) page++;  //第一次进入的时候cancle了dialog
				 onWork();
			}
		});

	}

	@Override
	protected void initData() {
		keyword = getIntent().getStringExtra("keyword");
	}

	class LibSRHandler extends Handler {
		WaittingDialog dialog;

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ETipsContants.Start:
				dialog = new WaittingDialog(LibSearchResultActivity.this);
				dialog.show();
				break;
			case ETipsContants.Logining:
				break;
			case ETipsContants.Downloading:
				dialog.setText("ETips下载数据中...");
				break;
			case ETipsContants.Finish:
				if(!dialog.isShowing()){
					dialog = null;
					break;
				}
				list = (List<BookInfo>) msg.obj;
				totalPage = msg.arg1;
				tv_pageInfo .setText("第"+page+"/"+totalPage+"页");
				// update UI;
				lv.setAdapter(new LibSearchResultListViewAdapter(
						LibSearchResultActivity.this, list));
				dialog.dismiss();
				dialog = null;
				break;
			case ETipsContants.Fail:
				if(!dialog.isShowing()){
					dialog = null;
					break;
				}
				error = (String) msg.obj;
				Intent intent = getIntent();
				intent.putExtra("error", error);
				setResult(RESULT_FIRST_USER, intent);
				dialog.dismiss();
				dialog = null;
				LibSearchResultActivity.this.finish();
				break;
			}
		}
	}

	class LibSRThread extends Thread {
		Handler handler;

		public LibSRThread(Handler handler) {
			this.handler = handler;
		}

		@Override
		public void run() {
			if (api == null)
				api = new LibraryAPI();
			handler.sendEmptyMessage(ETipsContants.Start);
			try {
				handler.sendEmptyMessage(ETipsContants.Downloading);
				List<BookInfo> mlist = api.searchBook(keyword, page);
				if (mlist.size() > 0) {
					Message msg = handler.obtainMessage();
					msg.what = ETipsContants.Finish;
					msg.obj = mlist;
					msg.arg1 = api.getTotalPage(keyword);
					handler.sendMessage(msg);
				} else {
					Message msg = handler.obtainMessage();
					msg.what = ETipsContants.Fail;
					msg.obj = "囧~木有找到相关图书"; // error infomation
					handler.sendMessage(msg);
				}
			} catch (ClientProtocolException e) {
				Message msg = handler.obtainMessage();
				msg.what = ETipsContants.Fail;
				msg.obj = "囧~网络不给力啊！";
				handler.sendMessage(msg);
				e.printStackTrace();
			} catch (IOException e) {
				Message msg = handler.obtainMessage();
				msg.what = ETipsContants.Fail;
				msg.obj = "囧~网络不给力啊！";
				handler.sendMessage(msg);
				e.printStackTrace();	
			} 
		}

	}
}
