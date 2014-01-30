package com.meizhuo.etips.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.meizhuo.etips.model.BookInfo;
import com.meizhuo.etips.ui.LibSearchResultListViewAdapter;
/**
 * 图书收藏页面
 * @author Jayin Ton
 * @version 2.2
 */
public class BookCollection extends BaseUIActivity {
	private ListView lv;
	private TextView count;
	private View back, deleteAll;
	private List<BookInfo> list;
	private LibSearchResultListViewAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_bookcollection);
		initData();
		initLayout();
	}

	@Override
	protected void initLayout() {
		lv = (ListView) _getView(R.id.acty_bookcollection_listview);
		count = (TextView) _getView(R.id.acty_bookcollection_count);
		back = _getView(R.id.acty_bookcollection_back);
		deleteAll = _getView(R.id.acty_bookcollection_deleteAll);

		count.setText("总数：" + list.size());
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				closeActivity();
			}
		});

		deleteAll.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ArrayList<BookInfo> bookInfos = AppInfo.getFavouriteBook(getContext());
				bookInfos.clear();
				AppInfo.setFavouriteBook(getContext(), bookInfos);
				closeActivity();
			}
		});
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

	@Override
	protected void onResume() {
		super.onResume();

		List<BookInfo> newList = AppInfo.getFavouriteBook(getContext());
		list.clear();
		list.addAll(newList);
		adapter.notifyDataSetChanged();
		count.setText("总数:" + list.size());

	}

	@Override
	protected void initData() {
		list = AppInfo.getFavouriteBook(getContext());
	}
 
}
