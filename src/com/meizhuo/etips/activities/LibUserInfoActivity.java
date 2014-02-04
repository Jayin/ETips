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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.meizhuo.etips.common.ETipsContants;
import com.meizhuo.etips.model.BookBorrowRecord;
import com.meizhuo.etips.net.utils.LibraryAPI;

public class LibUserInfoActivity extends BaseUIActivity {
	private String userID;
	private TextView tv_title, tv_record, tv_histroy;
	private Button backBtn, reflushBtn;
	private ListView lv_borrowRecord, lv_borrowHistroy;
	private ProgressBar pb_borrowRecord, pb_borrowHistroy;
	private List<BookBorrowRecord> recordlist, histroylist;
	private boolean hasGetrecordlist = false, hasGethistroylist = false;
	private int TAG_recordlist = 1, TAG_histroylist;
	private ETipsApplication App;
	private LibraryAPI api;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_library_userinfo);
		initData();
		initLayout();
		onWork();
	}

	private void onWork() {
		if (!hasGetrecordlist) {
			tv_record.setVisibility(View.GONE);
			pb_borrowRecord.setVisibility(View.VISIBLE);
			lv_borrowRecord.setVisibility(View.GONE);
			MHandler handler = new MHandler();
			new MThread(handler, TAG_recordlist).start();
		}
		if (!hasGethistroylist) {
			tv_histroy.setVisibility(View.GONE);
			pb_borrowHistroy.setVisibility(View.VISIBLE);
			lv_borrowHistroy.setVisibility(View.GONE);
			MHandler handler = new MHandler();
			new MThread(handler, TAG_histroylist).start();
		}
	}

	@Override
	protected void initLayout() {
		tv_title = (TextView) this
				.findViewById(R.id.acty_library_userinfo_title_tv);
		tv_record = (TextView) this
				.findViewById(R.id.acty_library_userinfo_tv_record);
		tv_histroy = (TextView) this
				.findViewById(R.id.acty_library_userinfo_tv_histroy);
		backBtn = (Button) this.findViewById(R.id.acty_library_userinfo_back);
		reflushBtn = (Button) this
				.findViewById(R.id.acty_library_userinfo_reflush);
		lv_borrowRecord = (ListView) this
				.findViewById(R.id.acty_library_userinfo_listview_borrowRecord);
		lv_borrowHistroy = (ListView) this
				.findViewById(R.id.acty_library_userinfo_listview_borrowHistroy);
		pb_borrowRecord = (ProgressBar) this
				.findViewById(R.id.acty_library_userinfo_progressBar_BorrowRecord);
		pb_borrowHistroy = (ProgressBar) this
				.findViewById(R.id.acty_library_userinfo_progressBar_borrowHistroy);

		tv_title.setText(userID);
		backBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				LibUserInfoActivity.this.finish();
			}
		});
		reflushBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (hasGethistroylist && hasGetrecordlist) {
					Toast.makeText(LibUserInfoActivity.this, "已经更新完毕！",
							Toast.LENGTH_SHORT).show();
				} else {
					onWork();
				}

			}
		});

		lv_borrowRecord.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(LibUserInfoActivity.this,
						LibBookDetailActivity.class);

			}
		});

	}

	@Override
	protected void initData() {
		userID = getIntent().getStringExtra("userID");
		App = (ETipsApplication) getApplication();
		api = App.getLibraryAPI();
	}

	class HistroyLVAdadpter extends BaseAdapter {

		@Override
		public int getCount() {// TODO Auto-generated method stub
			return histroylist.size();
		}

		@Override
		public Object getItem(int position) {
			return histroylist.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder hodler;
			if (convertView == null) {
				hodler = new ViewHolder();
				convertView = LayoutInflater.from(LibUserInfoActivity.this)
						.inflate(R.layout.item_library_bookborrwhistroy, null);
				hodler.tv_bookName = (TextView) convertView
						.findViewById(R.id.item_library_bookborrowhistroy_bookName);
				hodler.tv_returnTime = (TextView) convertView
						.findViewById(R.id.item_library_bookborrowhistroy_returnTime);
				hodler.tv_borrowTime = (TextView) convertView
						.findViewById(R.id.item_library_bookborrowhistroy_borrowTime);
				convertView.setTag(hodler);
			} else {
				hodler = (ViewHolder) convertView.getTag();
			}
			BookBorrowRecord bbr = histroylist.get(position);
			hodler.tv_bookName.setText(bbr.bookName);
			hodler.tv_returnTime.setText("还期:" + bbr.latestReturn);
			hodler.tv_borrowTime.setText("借期:" + bbr.borrowTime);
			return convertView;
		}

		class ViewHolder {
			TextView tv_returnTime, tv_borrowTime, tv_bookName;
		}

	}

	class RecordLVAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return recordlist.size();
		}

		@Override
		public Object getItem(int position) {
			return recordlist.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			MViewHolder hodler;
			if (convertView == null) {
				hodler = new MViewHolder();
				convertView = LayoutInflater.from(LibUserInfoActivity.this)
						.inflate(R.layout.item_library_bookborrowrecord, null);
				hodler.tv_bookName = (TextView) convertView
						.findViewById(R.id.item_library_bookborrowrecord_bookName);
				hodler.tv_returnTime = (TextView) convertView
						.findViewById(R.id.item_library_bookborrowrecord_returnTime);
				 
				hodler.tv_status = (TextView) convertView
						.findViewById(R.id.item_library_bookborrowrecord_status);
				convertView.setTag(hodler);
			} else {
				hodler = (MViewHolder) convertView.getTag();
			}
			BookBorrowRecord bbr = recordlist.get(position);
			hodler.tv_bookName.setText(bbr.bookName);
			hodler.tv_returnTime.setText("最迟应还期:" + bbr.latestReturn);
		 
			hodler.tv_status.setText(bbr.borrowStatus);
			return convertView;
		}

		class MViewHolder {
			TextView tv_returnTime, tv_status, tv_bookName;
		}
	}

	class MHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ETipsContants.Finish:
				if (msg.arg1 == TAG_histroylist) {
					if (hasGethistroylist) {
						return;
					}
					// update UI
					hasGethistroylist = true;
					tv_histroy.setVisibility(View.GONE);
					pb_borrowHistroy.setVisibility(View.GONE);
					lv_borrowHistroy.setAdapter(new HistroyLVAdadpter());
					lv_borrowHistroy.setVisibility(View.VISIBLE);
				} else {
					if (hasGetrecordlist) {
						return;
					}
					// update UI
					hasGetrecordlist = true;
					tv_record.setVisibility(View.GONE);
					pb_borrowRecord.setVisibility(View.GONE);
					lv_borrowRecord.setAdapter(new RecordLVAdapter());
					lv_borrowRecord.setVisibility(View.VISIBLE);
				}
				break;

			case ETipsContants.Fail:
				if (msg.arg1 == TAG_histroylist) {
					if (hasGethistroylist) {
						return;
					}
					pb_borrowHistroy.setVisibility(View.GONE);
					tv_histroy.setVisibility(View.VISIBLE);
					tv_histroy.setText((String) msg.obj);
				} else {
					if (hasGetrecordlist) {
						return;
					}
					pb_borrowRecord.setVisibility(View.GONE);
					tv_record.setVisibility(View.VISIBLE);
					tv_record.setText((String) msg.obj);
				}
				break;
			}
		}
	}

	class MThread extends Thread {
		Handler handler;
		int tag;

		public MThread(Handler handler, int tag) {
			this.handler = handler;
			this.tag = tag;
		}

		@Override
		public void run() {
			Message msg = new Message();
			msg.arg1 = tag;
			if (api == null) {
				msg.what = ETipsContants.Fail;
				msg.obj = "身份验证超时，请重新登录！";
				handler.sendMessage(msg);
				return;
			}
			try {
				if (tag == TAG_histroylist) {
					histroylist = new ArrayList<BookBorrowRecord>();
					api.getBorrowHistroy(histroylist);
					if (histroylist.size() > 0) {
						msg.what = ETipsContants.Finish;
					} else {
						msg.what = ETipsContants.Fail;
						msg.obj = "你居然一本书都木有借过....";
					}
				} else {
					recordlist = api.getBookBorrowRecord();
					if (recordlist.size() > 0) {
						msg.what = ETipsContants.Finish;
					} else {
						msg.what = ETipsContants.Fail;
						msg.obj = "你当前木有借阅任何图书....";
					}
				}
				handler.sendMessage(msg);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				msg.what = ETipsContants.Fail;
				msg.obj = "网络错误，请刷新...";
				handler.sendMessage(msg);
			} catch (IOException e) {
				e.printStackTrace();
				msg.what = ETipsContants.Fail;
				msg.obj = "网络错误，请刷新...";
				handler.sendMessage(msg);
			}

		}
	}
}
