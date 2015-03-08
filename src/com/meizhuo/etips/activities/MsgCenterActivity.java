package com.meizhuo.etips.activities;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.meizhuo.etips.app.AppInfo;
import com.meizhuo.etips.app.Preferences;
import com.meizhuo.etips.common.CalendarUtils;
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
	private ListView lv;
	private ProgressBar pb;
	private ArrayList<MsgRecord> list;
	private MsgCenterLVAdapter adapter;

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_msgcenter);
		initData(); // 注意更新 用户查看消息的状态
		initLayout();
		onWork();
	}

	@Override public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.acty_msgcenter, menu);
		return true;
	}

	@Override public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.cleanup){
			clean_up();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void onWork() {
		MsgCenterHandler handler = new MsgCenterHandler();
		new MsgCenterThread(handler).start();
	}

	@Override protected void initLayout() {
		lv = (ListView) this.findViewById(R.id.acty_msgcenter_listview);
		pb = (ProgressBar) this.findViewById(R.id.acty_msgcenter_progressbar);
	}
	
	@SuppressLint("HandlerLeak") private void clean_up(){
		if (adapter != null) {
			final Handler mHandler = new Handler() {
				@Override public void handleMessage(Message msg) {
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
				@Override public void run() {
					// 清空
					list.removeAll(list);
					if (AppInfo.setMessages(getContext(), list)) {
						mHandler.sendEmptyMessage(ETipsContants.Finish);
					}
				}
			}).start();

		}
	}

	@Override protected void initData() {
		// 注意更新 用户查看消息的状态
		Preferences.setIsHasMsgToCheck(getContext(), false);
		// 清除notification
		NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		manager.cancel(ETipsContants.ID_System);
		manager.cancel(ETipsContants.ID_Notify);
		manager.cancel(ETipsContants.ID_Push);
	}

	@SuppressLint("HandlerLeak") class MsgCenterHandler extends Handler {
		@Override public void handleMessage(Message msg) {
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

		@Override public void run() {
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

		@Override public int getCount() {
			return list.size();
		}

		@Override public Object getItem(int position) {
			return list.get(position);
		}

		@Override public long getItemId(int position) {
			return position;
		}

		@Override public View getView(final int position, View convertView,
				ViewGroup parent) {
			final ViewHolder holder;
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
			// holder.tv_time.setText(ETipsUtils.getTimeForm(Long
			// .parseLong(mr.addTime)));
			holder.tv_time.setText(CalendarUtils.getTimeFromat(
					Long.parseLong(mr.getAddTime()), CalendarUtils.TYPE_ONE));
			holder.tv_content.setText(StringUtils.wrapText(
					MsgCenterActivity.this, mr.content));
		
			// 长按保存
			holder.tv_content.setOnLongClickListener(new OnLongClickListener() {

				@Override public boolean onLongClick(View v) {
					Intent intent = new Intent(ETipsContants.Action_Notes);
					// String content = ((TextView) convertView
					// .findViewById(R.id.item_dialog_msg_center_common_tv_content))
					// .getText().toString();
					String content = holder.tv_content.getText().toString();
					SP sp = new SP(ETipsContants.SP_NAME_Notes, getContext());
					sp.add(java.lang.System.currentTimeMillis() + "", content);
					sendBroadcast(intent);
					toast("已保存到个人便签");
					return true;
				}
			});
			return convertView;
		}

		class ViewHolder {
			TextView tv_time, tv_content;
		}
	}

}
