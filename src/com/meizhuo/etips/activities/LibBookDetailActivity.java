package com.meizhuo.etips.activities;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.meizhuo.etips.common.utils.ETipsContants;
import com.meizhuo.etips.common.utils.Elog;
import com.meizhuo.etips.model.BookStatus;
import com.meizhuo.etips.net.utils.LibraryAPI;

public class LibBookDetailActivity extends BaseUIActivity {
	private String bookName,bookID,pressTime, press;
	private Button backBtn;
	private ListView lv;
	private TextView tv_title, tv_press, tv_pressTime, tv_words;
	private ProgressBar progressBar;
	private List<BookStatus> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_library_bookdetail);
		initData();
		initLayout();
		onWork();

	}

	private void onWork() {
         LibBDHandler handler = new LibBDHandler();
         new LibBDThread(handler).start();
	}

	@Override
	protected void initLayout() {
		backBtn = (Button) this.findViewById(R.id.acty_library_bookdetail_back);
		tv_title = (TextView) this
				.findViewById(R.id.acty_library_bookdetail_title_tv);
		tv_press = (TextView) this
				.findViewById(R.id.acty_library_bookdetail_press);
		tv_pressTime = (TextView) this
				.findViewById(R.id.acty_library_bookdetail_presstime);
		tv_words = (TextView) this
				.findViewById(R.id.acty_library_bookdetail_tv_words);
		progressBar = (ProgressBar) this
				.findViewById(R.id.acty_library_bookdetail_progressBar);
		lv = (ListView) this
				.findViewById(R.id.acty_library_bookdetail_listview);

		tv_title.setText(bookName);
		tv_press.setText(press);
		tv_pressTime.setText(pressTime);
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				LibBookDetailActivity.this.finish();
			}
		});

	}

	@Override
	protected void initData() {
		bookName = getIntent().getStringExtra("bookName");
		pressTime = getIntent().getStringExtra("pressTime");
		press = getIntent().getStringExtra("press");
		bookID = getIntent().getStringExtra("bookID");
	}

	class LibBDHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == ETipsContants.Finish) {
                progressBar.setVisibility(View.GONE);
                tv_words.setVisibility(View.GONE);
                lv.setVisibility(View.VISIBLE);
                lv.setAdapter(new BDListViewAdapter());
			}else{
				progressBar.setVisibility(View.GONE);
				tv_words.setText("囧~网络好像有点问题哦..");
			}
		}
	}

	class LibBDThread extends Thread {
	private	Handler handler;
		public LibBDThread(Handler handler){
			this.handler = handler;
		}
		@Override
		public void run() {
            LibraryAPI api = new LibraryAPI();
            try {
				list = api.getBookStatus(bookID);
				if(list.size()>0)handler.sendEmptyMessage(ETipsContants.Finish);
				else {
					handler.sendEmptyMessage(ETipsContants.Fail);
				}
			} catch (ClientProtocolException e) {
				handler.sendEmptyMessage(ETipsContants.Fail);
				e.printStackTrace();
			} catch (IOException e) {
				handler.sendEmptyMessage(ETipsContants.Fail);
				e.printStackTrace();
			}
		}
	}

	class BDListViewAdapter extends BaseAdapter {

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
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(LibBookDetailActivity.this)
						.inflate(R.layout.item_library_bookdetail, null);
				holder.tv_location = (TextView) convertView
						.findViewById(R.id.item_library_bookdetail_location);
				holder.tv_kind = (TextView) convertView
						.findViewById(R.id.item_library_bookdetail_kind);
				holder.tv_status = (TextView) convertView
						.findViewById(R.id.item_library_bookdetail_status);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			BookStatus bs = list.get(position);
			Elog.i(bs.toString());
			holder.tv_location.setText(bs.location);
			holder.tv_kind.setText(bs.borrowKind);
			holder.tv_status.setText(bs.status);
			return convertView;
		}

		class ViewHolder {
			TextView tv_location, tv_status, tv_kind;
		}
	}

}
