package com.meizhuo.etips.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.meizhuo.etips.common.AndroidUtils;
import com.meizhuo.etips.common.ETipsContants;
import com.meizhuo.etips.common.SP;
import com.meizhuo.etips.model.Topic;
import com.meizhuo.etips.net.utils.TweetAPI;
import com.meizhuo.etips.ui.dialog.DeclarationDialog;
import com.meizhuo.etips.widget.PullToRefreshListView;
import com.meizhuo.etips.widget.PullToRefreshListView.OnRefreshListener;

/**
 * 话题列表
 * 
 * @author Jayin Ton
 * 
 */
public class TopicList extends BaseUIActivity implements OnClickListener {
	private View back, setting;
	private PullToRefreshListView lv;
	private View footView;
	private SP sp;
	private BroadcastReceiver receiver;
	private List<Topic> list,newList;
	private TopicListAdapter adapter;
 
//	private boolean shouldTopicListReflush = false; // 系统配置，是否要自动刷新列表
	//private boolean isRefreshing = false; // 设置一个状态，防止多次刷新 接着就
	// private boolean isGetingMore = false;
	// private boolean hasData = false;
	private String exception;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_topiclist);
		initData();
		initLayout();
		onWork();
		// 注册receiver
	}

	private void onWork() {
		if (list != null && list.size() > 0) {
			adapter = new TopicListAdapter(list);
			lv.setAdapter(adapter);
			footView.setVisibility(View.GONE);
//			if (shouldTopicListReflush) { // 系统控制是否刷新！！
//				lv.clickRefresh();
//			}
		} else {
			lv.clickRefresh();
		}
	}

	@Override
	protected void initLayout() {
		back = _getView(R.id.acty_topiclist_btn_back);
		setting = _getView(R.id.acty_topiclist_btn_setting);
		
		lv = (PullToRefreshListView) _getView(R.id.acty_topiclist_lv);

		back.setOnClickListener(this);

		setting.setOnClickListener(this);

		lv.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				new ReflushTask().execute();
				// 记得在Tast执行：lv.onRefreshComplete(); 隐藏ListView Head View
			}
		});
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			
				if( footView == view){
					lv.clickRefresh();
					return;
				}
				if(list.get(position-1).getName().equals( "邑大购购网")){
					openActivity(Browser.class);
					return;
				}
				Intent intent = new Intent(TopicList.this,TweetList.class);
			    intent.putExtra("topic_id", list.get(position-1).getId());
			    intent.putExtra("topic_name", list.get(position-1).getName());
			    intent.putExtra("enableIncognito", list.get(position-1).getEnableIncognito());
			    openActivity(intent);
			 
			}
		});
		footView = LayoutInflater.from(this).inflate(
				R.layout.pulltoreflush_head, null);
		lv.setAdapter(new TopicListAdapter(new ArrayList<Topic>()));
		lv.addFooterView(footView);
	}

	@Override
	protected void initData() {
		sp = new SP(ETipsContants.SP_NAME_Topic, this);
//		shouldTopicListReflush = (new SP(ETipsContants.SP_NAME_User, this)
//				.getValue("shouldTopicListUpdata").equals("YES")) ? true
//				: false;
		if (!sp.isEmpty()) {
			list = (List<Topic>) sp.toEntityAll(ETipsContants.TYPE_SP_Topic);
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		//unRegisterReceiver()
//		if (receiver != null) {
//			this.unregisterReceiver(receiver);
//		}
	}

	class ReflushTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
		//	isRefreshing = true;
			exception = null;
			TweetAPI api = new TweetAPI(getContext());
			if(AndroidUtils.isNetworkConnected(getContext()))newList = api.getTopicList();
			if (newList == null) {
				exception = "请检查你的网络";
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			lv.onRefreshComplete();// 隐藏ListView Head View
			if(newList==null){
				toast("请检查你的网络");
			}else if(newList.size() == 0){
				toast("没有数据返回");
			}else{
				if(list==null)list= new ArrayList<Topic>();
				list.clear();
				list.addAll(newList);
				onWork();
				toast("已刷新");
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.acty_topiclist_btn_back:
			closeActivity();
			break;

		case R.id.acty_topiclist_btn_setting:
			 openActivity(ETipsMainSettingActivity.class);
			// 说明

			break;
		case R.id.pulltoreflush_more:
			break;
		}
	}

	class TopicListAdapter extends BaseAdapter {
		List<Topic> list = null;

		public TopicListAdapter(List<Topic> _list) {
			this.list = _list;
		}

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
			ViewHodler h = null;
			if (convertView == null) {
				h = new ViewHodler();
				convertView = LayoutInflater.from(getContext()).inflate(
						R.layout.item_tweetlist, null);
				h.topicname = (TextView) convertView
						.findViewById(R.id.item_tweetlist_topic);
				h.description = (TextView)convertView.findViewById(R.id.item_tweetlist_description);
				convertView.setTag(h);
			} else {
				h = (ViewHodler) convertView.getTag();
			}
			h.topicname.setText(list.get(position).getName());
			h.description.setText(list.get(position).getDescription());
			return convertView;
		}

		class ViewHodler {
			TextView topicname,description;
		}

	}

}
