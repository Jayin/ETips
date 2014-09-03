package com.meizhuo.etips.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.meizhuo.etips.adapter.LibSearchResultListViewAdapter;
import com.meizhuo.etips.app.AppInfo;
import com.meizhuo.etips.model.BookInfo;

/**
 * 图书收藏页面
 * 
 * @author Jayin Ton
 * @version 2.2
 */
public class BookCollection extends BaseUIActivity {
	private ListView lv;
	private List<BookInfo> list;
	private LibSearchResultListViewAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_bookcollection);
		initData();
		initLayout();
	}

	
	@Override public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.acty_bookcollection, menu);
		return true;
	}

	@Override public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.cleanup) {
			cleanup();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	protected void initLayout() {
		lv = (ListView) _getView(R.id.acty_bookcollection_listview);
		adapter = new LibSearchResultListViewAdapter(this, list);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> paremt, View view,
					int position, long id) {
				Intent intent = new Intent(getContext(),
						LibBookDetailActivity.class);
				BookInfo bookInfo = list.get(position);
				intent.putExtra("from", "BookCollection");
				intent.putExtra("BookInfo", bookInfo);
				openActivity(intent);
			}
		});
	}
	
	private void cleanup(){
		ArrayList<BookInfo> bookInfos = AppInfo
				.getFavouriteBook(getContext());
		bookInfos.clear();
		AppInfo.setFavouriteBook(getContext(), bookInfos);
		closeActivity();
		toast("已清空");
	}

	@Override
	protected void onResume() {
		super.onResume();

		List<BookInfo> newList = AppInfo.getFavouriteBook(getContext());
		list.clear();
		list.addAll(newList);
		adapter.notifyDataSetChanged();

	}

	@Override
	protected void initData() {
		list = AppInfo.getFavouriteBook(getContext());
	}

}
