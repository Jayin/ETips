package com.meizhuo.etips.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.meizhuo.etips.common.ETipsContants;
import com.meizhuo.etips.common.FileUtils;
import com.meizhuo.etips.model.SchoolNewList;
import com.meizhuo.etips.model.SchoolNews;
import com.meizhuo.etips.net.utils.WYUNewsAPI;
import com.meizhuo.etips.ui.dialog.LoadingDialog;
import com.meizhuo.etips.ui.dialog.WaittingDialog;

public class SchoolNewsMainActivity extends BaseUIActivity {
	private int page = 1, totalPage = -1;
	private View backBtn, nextBtn, prevBtn;
	private TextView tv_pageInfo;
	private ListView lv;
	private List<SchoolNews> list;
	private WYUNewsAPI api;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_schoolnews_main);
		initData();
		initLayout();
		onWork();

	}


	private void onWork() {
		SNMHandler handler = new SNMHandler();
		new SNMThread(handler).start();
	}

	@Override
	protected void initLayout() {
		backBtn = this.findViewById(R.id.acty_schoolnews_main_back);
		nextBtn = this.findViewById(R.id.acty_schoolnews_main_next);
		prevBtn = this.findViewById(R.id.acty_schoolnews_main_prev);
		tv_pageInfo = (TextView) this
				.findViewById(R.id.acty_schoolnews_main_page);
		lv = (ListView) this.findViewById(R.id.acty_schoolnews_main_listview);
		final Handler h = new Handler() {
			public void handleMessage(Message msg) {
				if (totalPage == -1 && (List<SchoolNews>) msg.obj != null) {
					list = (List<SchoolNews>) msg.obj;
					lv.setAdapter(new SNAdapter());
				}
			}
		};
		new LoadFileThread(h).start();
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(SchoolNewsMainActivity.this,
						SchoolNewsDetailActivity.class);
				intent.putExtra("linkPath", list.get(position).linkPath);
				intent.putExtra("position", position);
				intent.putExtra("title", list.get(position).title);
				intent.putExtra("content", list.get(position).content);
				startActivityForResult(intent,
						ETipsContants.RequestCode_SchoolNewMain2Detail);
			}
		});

		backBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				SchoolNewsMainActivity.this.finish();
			}
		});

		nextBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (totalPage != -1)
					page++; // 第一次进入的时候cancle了dialog
				onWork();
			}
		});

		prevBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (totalPage == -1) {
					onWork();
				} else {
					if (page - 1 >= 1) {
						page--;
						onWork();
					} else {
						Toast.makeText(SchoolNewsMainActivity.this,
								"囧~已经是首页了！", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});

	}

	@Override
	protected void initData() {

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == ETipsContants.RequestCode_SchoolNewMain2Detail
				&& resultCode == RESULT_OK) {
			int position = data.getIntExtra("position", 0);
			list.get(position).content = data.getStringExtra("content");
		}
	}

	class SNAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			SNViewHolder h = null;
			if (convertView == null) {
				h = new SNViewHolder();
				convertView = LayoutInflater.from(SchoolNewsMainActivity.this)
						.inflate(R.layout.item_schoolnew_main_listview, null);
				h.tv_from = (TextView) convertView
						.findViewById(R.id.item_schoolnew_main_listview_from);
				h.tv_time = (TextView) convertView
						.findViewById(R.id.item_schoolnew_main_listview_time);
				h.tv_title = (TextView) convertView
						.findViewById(R.id.item_schoolnew_main_listview_title);
				convertView.setTag(h);
			} else {
				h = (SNViewHolder) convertView.getTag();
			}
			SchoolNews sn = list.get(position);
			h.tv_from.setText(sn.from);
			h.tv_time.setText(sn.time);
			h.tv_title.setText(sn.title);
			return convertView;
		}

		class SNViewHolder {
			TextView tv_title, tv_time, tv_from;
		}

	}

	class SNMHandler extends Handler {
       LoadingDialog dialog;
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ETipsContants.Start:
				dialog = new LoadingDialog(getContext());
				dialog.show();
				break;
			case ETipsContants.Logining:
				break;
			case ETipsContants.Downloading:
				if (!dialog.isShowing()) {
					dialog = null;
					break;
				}
			dialog.setLodingText("ETips获取数据中...");
				break;
			case ETipsContants.Finish:
				if (!dialog.isShowing()) {
					dialog = null;
					break;
				}
				// 保存首页
				if (totalPage == -1 && page == 1 && list != null) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							FileUtils.saveObject(SchoolNewsMainActivity.this,
									SchoolNewList.getInstance(list),
									ETipsContants.File_NewsCache);
						}
					}).start();
				}
				totalPage = msg.arg1;
				tv_pageInfo.setText("第" + page + "/" + totalPage + "页");
				// update UI;
				lv.setAdapter(new SNAdapter());
				dialog.dismiss();
				dialog = null;
				break;
			case ETipsContants.Fail:
				if (!dialog.isShowing()) {
					dialog = null;
					break;
				}
				Toast.makeText(SchoolNewsMainActivity.this, (String) msg.obj,
						Toast.LENGTH_SHORT).show();
				dialog.dismiss();
				dialog = null;
				break;
			}
		}
	}

	class SNMThread extends Thread {
		Handler handler;

		public SNMThread(Handler h) {
			this.handler = h;
		}

		@Override
		public void run() {
			handler.sendEmptyMessage(ETipsContants.Start);
			if (api == null)
				api = new WYUNewsAPI();
			Message msg = handler.obtainMessage();
			try {
				list = api.getSchoolNews(page);
				if (list.size() > 0) {
					msg.what = ETipsContants.Finish;
					msg.arg1 = api.getSCtotalPage();
				} else {
					msg.what = ETipsContants.Fail;
					msg.obj = "获取数据失败！";
				}
				handler.sendMessage(msg);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				msg.what = ETipsContants.Fail;
				msg.obj = "囧~网络不给力啊！";
				handler.sendMessage(msg);
			} catch (IOException e) {
				e.printStackTrace();
				msg.what = ETipsContants.Fail;
				msg.obj = "囧~网络不给力啊！";
				handler.sendMessage(msg);
			}
		}
	}

	class LoadFileThread extends Thread {
		Handler h;

		public LoadFileThread(Handler h) {
			this.h = h;
		}

		@Override
		public void run() {
			Message msg = new Message();
			msg.obj = (ArrayList<SchoolNews>) FileUtils.readObject(
					SchoolNewsMainActivity.this, ETipsContants.File_NewsCache);
			h.sendMessage(msg);
		}
	}

}
