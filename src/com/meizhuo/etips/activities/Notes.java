package com.meizhuo.etips.activities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.ListView;
import android.widget.TextView;

import com.meizhuo.etips.common.ETipsContants;
import com.meizhuo.etips.common.SP;
import com.meizhuo.etips.common.StringUtils;
import com.meizhuo.etips.model.MNotes;

public class Notes extends BaseUIActivity implements OnClickListener {
	private View add;
	private ListView lv;
	private SP sp;
	private boolean isEmpty = false;
	private List<MNotes> list = null;

	private BroadcastReceiver receiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_notes);
		initData();
		initLayout();
		final Handler h = new Handler() {
			@Override
			public void handleMessage(Message msg) {

			}
		};
		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				// h.sendEmptyMessage(1);
				initData();
				lv.setAdapter(new MyAdapter());
			}
		};
		IntentFilter filter = new IntentFilter(ETipsContants.Action_Notes);
		this.registerReceiver(receiver, filter);
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (receiver != null) {
			this.unregisterReceiver(receiver);
		}
	}

	@Override
	protected void initLayout() {
		add = _getView(R.id.acty_notes_btn_add);
		lv = (ListView) _getView(R.id.acty_notes_lv);
		add.setOnClickListener(this);

		lv.setAdapter(new MyAdapter());
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent intent = wrapIntent(NotesEdit.class);
				intent.putExtra("time", list.get(position).getTime() + "");
				intent.putExtra("content", list.get(position).getContent());
				openActivity(intent);
			}
		});

	}

	@Override
	protected void initData() {
		sp = new SP(ETipsContants.SP_NAME_Notes, this);
		list = null;
		list = new ArrayList<MNotes>();
		if (sp.isEmpty()) {
			isEmpty = true;
		} else {
			list = (List<MNotes>) sp.toEntityAll(ETipsContants.TYPE_SP_Notes);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.acty_notes_btn_add:
			openActivity(NotesEdit.class);
			break;
		}
	}

	class MyAdapter extends BaseAdapter {

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
			convertView = LayoutInflater.from(Notes.this).inflate(
					R.layout.item_note, null);
			((TextView) convertView.findViewById(R.id.item_note_content))
					.setText(list.get(position).getContent());
			return convertView;
		}

	}
}
