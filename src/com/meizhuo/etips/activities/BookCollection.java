package com.meizhuo.etips.activities;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.meizhuo.etips.common.utils.ETipsContants;
import com.meizhuo.etips.common.utils.SP;
import com.meizhuo.etips.model.BookInfo;
import com.meizhuo.etips.ui.LibSearchResultListViewAdapter;

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
				SP sp = new SP(ETipsContants.SP_NAME_Book, getContext());
				if (sp.deleteAll()) {
					toast("清空完毕");
				} else {
					toast("清空失败");
				}
				closeActivity();
			}
		});
		adapter = new LibSearchResultListViewAdapter(this, list);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> paremt, View view, int position,
					long id) {
				Intent intent = new Intent(getContext(),
						LibBookDetailActivity.class);
				intent.putExtra("bookName", list.get(position).BookName);
				intent.putExtra("pressTime", list.get(position).PressTime);
				intent.putExtra("press", list.get(position).Press);
				intent.putExtra("bookID",list.get(position).BookID);
				Bundle b = new Bundle();
				b.putSerializable("BookInfo",list.get(position));
				intent.putExtras(b);
				intent.putExtra("from", "BookCollection");
				intent.putExtra("time", list.get(position).getCollectionTime() +""); //加入收藏的时间
				intent.putExtra("position", position); //用于确认是否要删除
				startActivityForResult(intent, ETipsContants.RequestCode_BookCollection2Detail);
			}
		});
	}

	@Override
	protected void initData() {
		SP sp = new SP(ETipsContants.SP_NAME_Book, getContext());
		list = (List<BookInfo>) sp.toEntityAll(ETipsContants.TYPE_SP_Book);

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if(requestCode ==ETipsContants.RequestCode_BookCollection2Detail && resultCode ==RESULT_OK){
	    	int positon = data.getIntExtra("position", -1);
	    	if(positon != -1){
	    		list.remove(positon);
	    		adapter.notifyDataSetChanged();
	    		count.setText("总数："+list.size());
	    	}
	    }
	}

}
