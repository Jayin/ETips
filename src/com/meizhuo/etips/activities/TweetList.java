package com.meizhuo.etips.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meizhuo.etips.app.ClientConfig;
import com.meizhuo.etips.common.AndroidUtils;
import com.meizhuo.etips.common.ETipsContants;
import com.meizhuo.etips.common.ETipsUtils;
import com.meizhuo.etips.common.SP;
import com.meizhuo.etips.common.ShareManager;
import com.meizhuo.etips.common.StringUtils;
import com.meizhuo.etips.model.Tweet;
import com.meizhuo.etips.net.utils.TweetAPI;
import com.meizhuo.etips.widget.PullToRefreshListView.OnRefreshListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.xview.XListView;
import com.xview.XListView.IXListViewListener;

/**
 * 推文列表 </br> 1.记得额外保存 topic_id!</br> 2.点击评论即可评论</br> 3.推文列表 sp命名规则
 * ：TweetList_XXXX xxxx为topic_id </br> funtion: compose/comment
 * 
 * @author Jayin Ton
 * 
 */
public class TweetList extends BaseUIActivity implements OnClickListener {
	private View back, compose;
	private TextView tv_title;
	private XListView lv;
	private String topic_id, title;
	private List<Tweet> list,// 一直存在
			newList; // 分页
	// NOTE : SP 不应该删除所有。。。。
	private TweetAdapter adapter; // 适配器，用来通知数据的改动
	private String exception;
	private SP sp;
	private int page = 0;
	private boolean isPullDonwUpdating = false; // 用来控制page，pulldown的时候就是下拉，应该是最新的一页page=1，点击更多的时候应该是下页
	private boolean isFootUpdating = false;
	private boolean enableIncognito = false; // 能否匿名发帖，转至评论，发帖时直接传递这个boolean值就ok了

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_tweet);
		initData();
		initLayout();
		onWork();
	}

	private void onWork() {

	}

	@Override
	protected void initLayout() {
		tv_title = (TextView) _getView(R.id.acty_tweet_title);
		back = _getView(R.id.acty_tweet_btn_back);
		compose = _getView(R.id.acty_tweet_btn_compose);
		lv = (XListView) _getView(R.id.acty_tweet_lv);

		tv_title.setText(title);
		back.setOnClickListener(this);
		compose.setOnClickListener(this);

		lv.setPullRefreshEnable(true);
		lv.setPullLoadEnable(true);
		
		lv.setXListViewListener(new IXListViewListener() {
			
			@Override
			public void onRefresh() {
				if (isFootUpdating) { // 底部更多正在刷新就不能下拉刷新
					lv.stopRefresh();
				} else  {
					new UpdateTask().execute(1); // 向下拉,page=1為最新页
				}
			}
			
			@Override
			public void onLoadMore() {
				if (!isPullDonwUpdating) { // 确保不是正在下拉刷新
					new FootUpdate().execute();
				}
			}
		});
		// list无数据就 先空
		if (list == null) {
			list = new ArrayList<Tweet>();
		}
		adapter = new TweetAdapter(list);
		lv.setAdapter(adapter);

        lv.startRefresh();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void initData() {
		topic_id = getIntent().getStringExtra("topic_id");
		title = getIntent().getStringExtra("topic_name");
		enableIncognito = getIntent().getIntExtra("enableIncognito", 1) == 1 ? true
				: false;
		SP sp = new SP("TweetList_" + topic_id, getContext());
		if (!sp.isEmpty()) {
			list = (List<Tweet>) sp.toEntityAll(ETipsContants.TYPE_SP_Tweet);
			page = 1;
		} else {
			page = 0;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.acty_tweet_btn_back:
			closeActivity();
			break;
		case R.id.acty_tweet_btn_compose:
			// 发布 要登录！
			if (ETipsUtils.isTweetLogin(getContext())) {
				if (ETipsUtils.isTweetLoginTimeOut(getContext())) {
					toast("ETips账户登录超时，请重新登录");
					openActivity(TweetLogin.class);
					return;
				}
			} else {
				toast("请先登录ETips账号");
				openActivity(TweetLogin.class);
				return;
			}
			Intent intent = wrapIntent(TweetCompose.class);
			intent.putExtra("function", "compose");
			intent.putExtra("topic_id", topic_id);
			intent.putExtra("enableIncognito", enableIncognito); // 是否可以匿名发布
			openActivity(intent);
			break;
		}
	}

	class FootUpdate extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			isFootUpdating = true;
			isPullDonwUpdating = false;
		}

		@Override
		protected Void doInBackground(Void... params) {
			isFootUpdating = true;
			TweetAPI api = new TweetAPI(getContext());
			newList = api.getTweetList(topic_id, (page + 1) + "");
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			lv.stopLoadMore();
			isFootUpdating = false;
			if (newList == null) {
				toast("网络异常");
				return;
			} else if (newList.size() == 0) {
				toast("沒有更多");
				lv.setPullLoadEnable(false);
				return;
			} else {
				toast("已刷新");
				page++;
				list.addAll(newList);
				adapter.notifyDataSetChanged();
			}

		}

	}

	/**
	 * 下拉刷新，获取第一页
	 * 
	 * @author Jayin Ton
	 */
	class UpdateTask extends AsyncTask<Integer, Void, Void> {
		@Override
		protected void onPreExecute() {
			isPullDonwUpdating = true;
		}
		@Override
		protected Void doInBackground(Integer... params) {
			TweetAPI api = new TweetAPI(getContext());
			newList = api.getTweetList(topic_id, params[0] + "");
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			isPullDonwUpdating = false;
			lv.stopRefresh();
			if (newList == null) {
				toast("网络异常");
				return;
			} else if (newList.size() == 0) {
				toast("尚无人发布");
				return;
			} else {
				toast("更新完毕");
				list.clear();
				list.addAll(newList); 
				adapter.notifyDataSetChanged();
			}
		}
	}

	class TweetAdapter extends BaseAdapter {
		private List<Tweet> list;

		public TweetAdapter(List<Tweet> _list) {
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
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder h = null;
			//final int _position = position;
			if (convertView == null) {
				h = new ViewHolder();
				convertView = LayoutInflater.from(getContext()).inflate(
						R.layout.item_tweet, null);
				h.author = (TextView) convertView
						.findViewById(R.id.item_tweet_author);
				h.sendTime = (TextView) convertView
						.findViewById(R.id.item_tweet_time);
				h.content = (TextView) convertView
						.findViewById(R.id.item_tweet_content);
				h.commentCount = (TextView) convertView
						.findViewById(R.id.item_tweet_commentCount);
				h.like = (Button) convertView
						.findViewById(R.id.item_tweet_like);
				h.item = (LinearLayout) convertView
						.findViewById(R.id.item_tweet_all);
				h.comment = convertView.findViewById(R.id.item_tweet_rely_comment);
				h.share = convertView.findViewById(R.id.item_tweet_share);
			
				convertView.setTag(h);
			} else {
				h = (ViewHolder) convertView.getTag();
			}
			
			h.item.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(TweetList.this,
							TweetDetail.class);
					intent.putExtra("Tweet", list.get(position));
					intent.putExtra("enableIncognito", enableIncognito); // 是否可以匿名发布
					// 因为加入了一个headview 占了第0个位置
					// Elog.i("" + (_position));
					intent.putExtra("topic_id", topic_id);
					intent.putExtra("topicName", title);
					// 其实每一个tweet已经包含一个topic_id 这里继续传过去 是为了保险起见
					openActivity(intent);
				}
			});

			h.like.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// 赞
					Tweet tweet = list.get(position);
					tweet.setLike(true);
					new LikeTask(tweet).start();
					Animation anim = AnimationUtils.loadAnimation(getContext(),R.anim.scale_zoom_big);
					v.setBackgroundResource(R.drawable.ic_item_tweet_like);
					v.startAnimation(anim);
				}
			});
			h.comment.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// 评论
					if (ETipsUtils.isTweetLogin(TweetList.this)) {
						Intent intent = new Intent(TweetList.this,
								TweetCompose.class);
						intent.putExtra("function", "comment");
						intent.putExtra("Tweet", list.get(position));
						intent.putExtra("enableIncognito", true);// 是否可以匿名发布
						openActivity(intent);
					} else {
						// 打开登录页面
						toast("请先登录ETips账号");
						openActivity(TweetLogin.class);
					}
				}
			});
			h.share.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// 分享
					Tweet tw = list.get(position);
					ShareManager sm  = new ShareManager("#"+title+"#"+tw.getContent());
					sm.shareToSina(getContext(), new SnsPostListener() {
						@Override
						public void onStart() {
						}
						
						@Override
						public void onComplete(SHARE_MEDIA arg0, int arg1, SocializeEntity arg2) {
						}
					});
				}
			});
			Tweet t = list.get(position);
			if (t.isIncognito()) {
				h.author.setText("@某同学");
			} else {
				h.author.setText("@" + t.getNickname());
			}

			h.sendTime.setText(StringUtils.getDateFormat(t.getSendTime(),
					"yy-mm-dd"));
			h.content.setText(t.getContent());
			h.commentCount.setText(t.getCommentCount() == 0 ? "评论" : ""
					+ t.getCommentCount());
			return convertView;
		}

		class ViewHolder {
			TextView author, sendTime, content, commentCount;
			Button like;
			View share, comment;
			LinearLayout item;
		}
	}
    /**
     * 赞，不需要确定在服务端是否赞的到</br>
     * 比较粗略，等待后期优化
     * @author Jayin Ton
     *
     */
	class LikeTask extends Thread {
		private Tweet tweet;

		public LikeTask(Tweet tweet) {
			this.tweet = tweet;
		}

		@Override
		public void run() {
			if (!AndroidUtils.isNetworkConnected(getContext())) {
				return;
			}
			if(!ETipsUtils.isTweetLogin(getContext())){
				return;
			}
			String content = "赞！";
			String time = System.currentTimeMillis() + "";
			TweetAPI api = new TweetAPI(getContext());
			String senderID = ClientConfig.getUserId(getContext());
			if(senderID==null || senderID.equals("null") || senderID.equals("")){
				return;
			}
			//赞是不匿名的！！
			api.comment(tweet.getTopicID(), tweet.getArticleID(), content,
					time, senderID, "0");
		}
	}
	
	class MyOnClickListener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			 
			
		}
		
	}
}
