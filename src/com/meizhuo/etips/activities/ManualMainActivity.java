package com.meizhuo.etips.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.meizhuo.etips.common.utils.PathBuilder;
/**
 * 学生手册
 * @author Jayin Ton
 *
 */
public class ManualMainActivity extends BaseUIActivity {
	private ListView lv;
	private View backBtn;
	private List<String> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_manual_main);
		initData();
		initLayout();
	}

	@Override
	protected void initLayout() {
		lv = (ListView) this.findViewById(R.id.acty_manual_main_listview);
		backBtn = this.findViewById(R.id.acty_manual_main_back);
		lv.setAdapter(new ManualContentAdapter());
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String title = list.get(position);
				if (title.equals("校园秩序管理") || title.equals("奖励与处分")
						|| title.equals("奖学与助学") || title.equals("附录")){
					return;
				}
				Intent intent = new Intent(ManualMainActivity.this,
						ManualDetailActivity.class);
				intent.putExtra("URL",
						PathBuilder.getManualContentPath(position));
				intent.putExtra("title", list.get(position));
				startActivity(intent);
			}
		});

		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ManualMainActivity.this.finish();

			}
		});
	}

	@Override
	protected void initData() {
		list = new ArrayList<String>();
		String[] strings = getResources().getStringArray(
				R.array.manual_contnet_list);
		for (String s : strings)
			list.add(s);
	}

	class ManualContentAdapter extends BaseAdapter {

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
			TextView tv;
			String title = list.get(position);
			if (title.equals("校园秩序管理") || title.equals("奖励与处分")
					|| title.equals("奖学与助学") || title.equals("附录")) {
				convertView = LayoutInflater.from(ManualMainActivity.this)
						.inflate(R.layout.item_manual_listview_kind, null);
				tv = (TextView) convertView
						.findViewById(R.id.item_manual_listview_kind_tv);
				tv.setTextColor(Color.WHITE);
			} else {
				convertView = LayoutInflater.from(ManualMainActivity.this)
						.inflate(R.layout.item_manual_listview_title, null);
				tv = (TextView) convertView
						.findViewById(R.id.item_manual_listview_title_tv);
			}
			tv.setText(title);
			return convertView;
		}
	}
}
