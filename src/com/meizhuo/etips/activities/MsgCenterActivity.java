package com.meizhuo.etips.activities;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.meizhuo.etips.app.AppInfo;
import com.meizhuo.etips.app.Preferences;
import com.meizhuo.etips.common.ETipsContants;
import com.meizhuo.etips.common.ETipsUtils;
import com.meizhuo.etips.common.SP;
import com.meizhuo.etips.common.StringUtils;
import com.meizhuo.etips.model.MsgRecord;

/**
 * 消息中心
 * 
 * @author Jayin Ton
 * 
 */
public class MsgCenterActivity extends BaseUIActivity {
	private View backBtn, reflushBtn;
	private ListView lv;
	private ProgressBar pb;
	private ArrayList<MsgRecord> list;
	private boolean hasData = false;
	private MsgCenterLVAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_msgcenter);
		initData(); // 注意更新 用户查看消息的状态
		initLayout();
		onWork();
	}

	private void onWork() {
		MsgCenterHandler handler = new MsgCenterHandler();
		new MsgCenterThread(handler).start();
	}

	@Override
	protected void initLayout() {
		backBtn = this.findViewById(R.id.acty_msgcenter_back);
		reflushBtn = this.findViewById(R.id.acty_msgcenter_reflush);
		lv = (ListView) this.findViewById(R.id.acty_msgcenter_listview);
		pb = (ProgressBar) this.findViewById(R.id.acty_msgcenter_progressbar);
		backBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MsgCenterActivity.this.finish();
			}
		});
		reflushBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (adapter != null) {
					final Handler mHandler = new Handler() {
						@Override
						public void handleMessage(Message msg) {
							switch (msg.what) {
							case ETipsContants.Finish:
								adapter.notifyDataSetChanged();
								Toast.makeText(MsgCenterActivity.this, "清空完毕",
										Toast.LENGTH_SHORT).show();
								break;
							}
						}
					};
					new Thread(new Runnable() {
						@Override
						public void run() {
							// 清空
							if (AppInfo.setMessages(getContext(), list)) {
								list.removeAll(list);
								mHandler.sendEmptyMessage(ETipsContants.Finish);
							}
						}
					}).start();

				}
			}
		});

		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(ETipsContants.Action_Notes);
				String content = ((TextView)view.findViewById(R.id.item_dialog_msg_center_common_tv_content)).getText().toString();
				SP sp = new SP(ETipsContants.SP_NAME_Notes, getContext());
				sp.add(java.lang.System.currentTimeMillis() + "",content);
				sendBroadcast(intent);
				toast("已保存到个人便签");
				return false;
			}
		});
	}

	@Override
	protected void initData() {
		// 注意更新 用户查看消息的状态
		Preferences.setIsHasMsgToCheck(getContext(), false);
	}

	@SuppressLint("HandlerLeak")
	class MsgCenterHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ETipsContants.Start:
				break;
			case ETipsContants.Logining:
				break;
			case ETipsContants.Downloading:
				break;
			case ETipsContants.Finish:// update UI;
				pb.setVisibility(View.GONE);
				lv.setVisibility(View.VISIBLE);
				adapter = new MsgCenterLVAdapter();
				lv.setAdapter(adapter);
				hasData = true;
				break;
			case ETipsContants.Fail:
				break;
			}
		}
	}

	class MsgCenterThread extends Thread {
		Handler h;

		public MsgCenterThread(Handler h) {
			this.h = h;
		}

		@Override
		public void run() {
			// MsgCenterDAO dao = new MsgCenterDAO(MsgCenterActivity.this);
			// list = dao.queryAll();
			list = AppInfo.getMessages(getContext());
			if (list.size() > 0) {
				list = (ArrayList<MsgRecord>) ETipsUtils.reverse(list);
				h.sendEmptyMessage(ETipsContants.Finish);
			} else {
				// dao.addOne();
				// list = dao.queryAll();
				// 没有就添加一条默认的
				MsgRecord mr = new MsgRecord();
				mr.setId(0);
				mr.setContent(getContext().getString(R.string.MsgCenterTips));
				mr.setType(ETipsContants.TYPE_MsgCenter_System);
				mr.setAddTime(System.currentTimeMillis() + "");
				list.add(mr);
				AppInfo.setMessages(getContext(), list);
				h.sendEmptyMessage(ETipsContants.Finish);
			}
		}
	}

	class MsgCenterLVAdapter extends BaseAdapter {

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
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(MsgCenterActivity.this)
						.inflate(R.layout.item_dialog_msg_center_coomon, null);
				holder.tv_content = (TextView) convertView
						.findViewById(R.id.item_dialog_msg_center_common_tv_content);
				holder.tv_content.setMovementMethod(new LinkMovementMethod());
				holder.tv_time = (TextView) convertView
						.findViewById(R.id.item_dialog_msg_center_common_tv_time);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			MsgRecord mr = list.get(position);
			holder.tv_time.setText(ETipsUtils.getTimeForm(Long
					.parseLong(mr.addTime)));
			holder.tv_content.setText(StringUtils.wrapText(
					MsgCenterActivity.this, mr.content));

			return convertView;
		}

		class ViewHolder {
			TextView tv_time, tv_content;
		}

	}

}
