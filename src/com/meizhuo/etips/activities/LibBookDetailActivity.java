package com.meizhuo.etips.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.meizhuo.etips.common.utils.ETipsContants;
import com.meizhuo.etips.common.utils.Elog;
import com.meizhuo.etips.common.utils.SP;
import com.meizhuo.etips.model.BookInfo;
import com.meizhuo.etips.model.BookStatus;
import com.meizhuo.etips.net.utils.LibraryAPI;
/**
 * 图书详情页面
 * @author Jayin Ton
 *@version 2.2
 */
public class LibBookDetailActivity extends BaseUIActivity {
	private Button backBtn, collect;
	private ListView lv;
	private TextView tv_title, tv_press, tv_pressTime, tv_words;
	private ProgressBar progressBar;
	private List<BookStatus> list;
	private BookInfo bookInfo;
	private String from;// 从哪个Activity跳转过来
	private BDListViewAdapter adapter;

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
		if (bookInfo != null && bookInfo.getStatus() != null
				&& bookInfo.getStatus().size() > 0) {
			list = bookInfo.getStatus();
			handler.sendEmptyMessage(ETipsContants.Finish);
		} else {
			new LibBDThread(handler).start();
		}

	}

	@Override
	protected void initLayout() {
		collect = (Button) this
				.findViewById(R.id.acty_library_bookdetail_collect);
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
		if (from.equals("LibSearchResultActivity")) {
			collect.setText("收藏");
		} else if (from.equals("BookCollection")) {
			collect.setText("取消收藏");
		}
		tv_title.setText(bookInfo.getBookName());
		tv_press.setText(bookInfo.getPress());
		tv_pressTime.setText(bookInfo.getPressTime());
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				LibBookDetailActivity.this.finish();
			}
		});
		collect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SP sp = new SP(ETipsContants.SP_NAME_Book, getContext());
				ArrayList<BookInfo> bookInfos = AppInfo
						.getFavouriteBook(getContext());
				if (from.equals("LibSearchResultActivity")) {
					// 加入收藏信息
					bookInfo.setStatus(list);
					// bookInfo.setCollectionTime(System.currentTimeMillis());
					// if (sp.add(System.currentTimeMillis() + "",
					// sp.toJSON(ETipsContants.TYPE_SP_Book, bookInfo))) {
					// toast("收藏成功");
					// } else {
					// toast("收藏失败");
					// }

					if (bookInfos.contains(bookInfo)) {
						toast("已收藏");
					} else {
						bookInfos.add(bookInfo);
						if (AppInfo.setFavouriteBook(getContext(), bookInfos)) {
							toast("收藏成功");
						} else {
							toast("收藏失败");
						}
					}

				} else if (from.equals("BookCollection")) {
					for (int i = 0; i < bookInfos.size(); i++) {
						if (bookInfos.get(i).getBookID()
								.equals(bookInfo.getBookID())) {
							bookInfos.remove(i);
							break;
						}
					}
					if (AppInfo.setFavouriteBook(getContext(), bookInfos)) {
						toast("删除成功");
					} else {
						toast("删除失败");
					}
				}
				closeActivity();

			}
		});

	}

	@Override
	protected void initData() {
		bookInfo = (BookInfo) getIntent().getSerializableExtra("BookInfo");
		from = getIntent().getStringExtra("from");
	}

	class LibBDHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == ETipsContants.Finish) {
				progressBar.setVisibility(View.GONE);
				tv_words.setVisibility(View.GONE);
				lv.setVisibility(View.VISIBLE);
				adapter = new BDListViewAdapter();
				lv.setAdapter(adapter);
				// e.g:用户可能收藏时还没接受到收藏馆信息,但在这里更新了以后逻辑上是要保存下下来的
				if (from.equals("BookCollection")) {
					ArrayList<BookInfo> bookInfos = AppInfo
							.getFavouriteBook(getContext());
					if (bookInfos.contains(bookInfo)) {
						bookInfos.remove(bookInfo);
						bookInfo.setStatus(list);
						bookInfos.add(bookInfo);
						AppInfo.setFavouriteBook(getContext(), bookInfos);
					}
				}
			} else {
				progressBar.setVisibility(View.GONE);
				tv_words.setText("囧~网络好像有点问题哦..");
			}
		}
	}

	class LibBDThread extends Thread {
		private Handler handler;

		public LibBDThread(Handler handler) {
			this.handler = handler;
		}

		@Override
		public void run() {
			LibraryAPI api = new LibraryAPI();
			try {
				list = api.getBookStatus(bookInfo.getBookID());
				if (list.size() > 0)
					handler.sendEmptyMessage(ETipsContants.Finish);
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
			// Elog.i(bs.toString());
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
