package com.meizhuo.etips.activities;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.meizhuo.etips.common.ETipsContants;
import com.meizhuo.etips.common.SP;
import com.meizhuo.etips.model.MNotes;

public class Notes extends BaseUIActivity implements OnClickListener {
	private View add, back;
	private ListView lv;
	private SP sp;
	private boolean isEmpty = false;
	private List<MNotes> list = null;
	private MyAdapter adapter;
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
				// lv.setAdapter(new MyAdapter());
				if (adapter != null)
					adapter.notifyDataSetChanged();
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
		back = _getView(R.id.btn_back);
		lv = (ListView) _getView(R.id.acty_notes_lv);
		add.setOnClickListener(this);
		back.setOnClickListener(this);

		adapter = new MyAdapter(list);
		lv.setAdapter(adapter);
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

		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				SP sp = new SP(ETipsContants.SP_NAME_Notes, getContext());
				sp.delete(list.get(position).getTime() + "");
				list.remove(position);
				adapter.notifyDataSetChanged();
				toast("已删除");
				return false;
			}
		});

	}

	@SuppressWarnings("unchecked")
	@Override
	protected void initData() {
		sp = new SP(ETipsContants.SP_NAME_Notes, this);
		if (list == null)
			list = new ArrayList<MNotes>();
		if (sp.isEmpty()) {
			isEmpty = true;
		} else {
			list.clear();
			list.addAll((List<MNotes>) sp
					.toEntityAll(ETipsContants.TYPE_SP_Notes));
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.acty_notes_btn_add:
			openActivity(NotesEdit.class);
			break;
		case R.id.btn_back:
			closeActivity();
			break;
		}
	}

	class MyAdapter extends BaseAdapter {
		List<MNotes> data;

		public MyAdapter(List<MNotes> list) {
			this.data = list;
		}

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			return data.get(position);
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
					.setText(data.get(position).getContent());
			return convertView;
		}

	}
}
