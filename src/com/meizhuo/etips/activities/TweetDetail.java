package com.meizhuo.etips.activities;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.meizhuo.etips.app.ClientConfig;
import com.meizhuo.etips.common.AndroidUtils;
import com.meizhuo.etips.common.ETipsUtils;
import com.meizhuo.etips.common.ShareManager;
import com.meizhuo.etips.common.StringUtils;
import com.meizhuo.etips.model.Comment;
import com.meizhuo.etips.model.Tweet;
import com.meizhuo.etips.net.utils.TweetAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;

/**
 * 推文详情 更新评论的2中交互方式：</br> 1.按了一下 “评论” </br> 2.一开始就刷新</br>
 * 
 * @author Jayin Ton
 * 
 */
public class TweetDetail extends BaseUIActivity implements OnClickListener {
	private ListView lv;
	private TextView tv_author, tv_sendTime, tv_content, tv_commentCount;
	private View share, like, comment, back;
	private String nickname, content; // commentCount为0的时候显示为tv_commentCount评论
	private long sendTime;
	private int commentCount;
	private ProgressBar progress;
	private boolean isLike = false, isIncognito = true;
	private Tweet tweet;
	private List<Comment> list;
	private String topic_id, topicName;
	private boolean enableIncognito = false;// 是否可以匿名发布

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_tweet_detail);
		initData();
		initLayout();
		onWork();
	}

	private void onWork() {
		new GetCommentTask().execute();
	}

	@Override
	protected void initLayout() {
		tv_author = (TextView) _getView(R.id.item_tweet_detail_author);
		tv_sendTime = (TextView) _getView(R.id.item_tweet_detail_time);
		tv_content = (TextView) _getView(R.id.item_tweet_detail_content);
		tv_commentCount = (TextView) _getView(R.id.item_tweet_detail_tv_commentCount);
		share = _getView(R.id.item_tweet_detail_share);
		like = _getView(R.id.item_tweet_detail_like);
		comment = _getView(R.id.item_tweet_detail_rely_comment);
		lv = (ListView) _getView(R.id.acty_tweet_detail_lv);
		back = _getView(R.id.item_tweet_detail_btn_back);
		progress = (ProgressBar) _getView(R.id.acty_tweet_detail_progress);

		like.setOnClickListener(this);
		share.setOnClickListener(this);
		comment.setOnClickListener(this);

		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				closeActivity();
			}
		});
		if (isIncognito) {
			tv_author.setText("@某同学");
		} else {
			tv_author.setText("@" + nickname);
		}
		tv_sendTime.setText(StringUtils.getDateFormat(sendTime, "yy-mm-dd"));
		tv_content.setText(content);
		tv_commentCount.setText(commentCount == 0 ? "评论" : "" + commentCount);
		if (isLike) {
			like.setBackgroundResource(R.drawable.ic_item_tweet_like);
		}

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getContext(), TweetCompose.class);
				intent.putExtra("function", "reply");
				intent.putExtra("author", list.get(position).getAuthor());
				intent.putExtra("enableIncognito", true);
				intent.putExtra("topic_id", list.get(position).getTopicID());
				intent.putExtra("article_id", list.get(position).getArticleID());
				intent.putExtra("author", list.get(position).getAuthor());
				intent.putExtra("to_comment_id", list.get(position).getComment_id());
				openActivity(intent);
			}
		});
	}

	@Override
	protected void initData() {
		enableIncognito = getIntent().getBooleanExtra("enableIncognito", true);
		topic_id = getIntent().getStringExtra("topic_id");
		topicName = getIntent().getStringExtra("topicName");
		// Elog.i(topic_id);
		tweet = (Tweet) getIntent().getSerializableExtra("Tweet");
		// if (tweet != null) {
		nickname = tweet.getNickname();
		sendTime = tweet.getSendTime();
		content = tweet.getContent();
		commentCount = tweet.getCommentCount();
		isLike = tweet.isLike();
		isIncognito = tweet.isIncognito();
		// }
		// else{
		// toast("tweet is null");
		// }

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.item_tweet_detail_share:
			share();
			break;
		case R.id.item_tweet_detail_like:
			// Tweet tweet = list.get(_position);
			if (!tweet.isLike()) {
				tweet.setLike(true);
				new LikeTask(tweet).start();
			}
			Animation anim = AnimationUtils.loadAnimation(getContext(),
					R.anim.scale_zoom_big);
			v.setBackgroundResource(R.drawable.ic_item_tweet_like);
			v.startAnimation(anim);
			break;
		case R.id.item_tweet_detail_rely_comment:
			if (ETipsUtils.isTweetLogin(TweetDetail.this)) {
				Intent intent = new Intent(TweetDetail.this, TweetCompose.class);
				intent.putExtra("function", "comment");
				// intent.putExtra("Tweet", tweet);
				intent.putExtra("topic_id", tweet.getTopicID());
				intent.putExtra("article_id", tweet.getArticleID());
				// intent.putExtra("enableIncognito", true);// 是否可以匿名发布
				openActivity(intent);
			} else {
				// 打开登录页面
				toast("请先登录ETips账号");
				openActivity(TweetLogin.class);
			}
			break;
		}
	}

	// 分享
	private void share() {
		ShareManager sm = new ShareManager("#" + topicName + "#"
				+ tweet.getContent());
		sm.shareToSina(getContext(), new SnsPostListener() {
			@Override
			public void onStart() {
			}

			@Override
			public void onComplete(SHARE_MEDIA arg0, int arg1,
					SocializeEntity arg2) {
			}
		});

	}

	/**
	 * 获取评论
	 * 
	 * @author Jayin Ton
	 * 
	 */
	class GetCommentTask extends AsyncTask<Void, Void, Integer> {
		@Override
		protected void onPreExecute() {
			progress.setVisibility(View.VISIBLE);
			lv.setVisibility(View.GONE);
		}

		@Override
		protected Integer doInBackground(Void... params) {
			if (!AndroidUtils.isNetworkConnected(getContext())) {
				return 0;
			}
			TweetAPI api = new TweetAPI(getContext());
			if (tweet != null)
				list = api.getCommentList(topic_id, tweet.getArticleID());
			return 1;
		}

		@Override
		protected void onPostExecute(Integer result) {
			// updata UI
			progress.setVisibility(View.GONE);
			lv.setVisibility(View.VISIBLE);
			switch (result) {
			case 0:
				toast("请检查你的网络!");
				break;
			case 1:
				if (list == null || list.size() == 0) {
					Toast.makeText(getContext(), "这条信息还木有评论,快来抢沙发！", 500)
							.show();
					// toast("这条信息还木有评论,快来抢沙发！");
					return;
				}
				lv.setAdapter(new CommentAdapter(list));
				break;
			}

		}

	}

	class CommentAdapter extends BaseAdapter {
		private List<Comment> list;

		public CommentAdapter(List<Comment> _list) {
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
			ViewHoloder h = null;
			if (convertView == null) {
				h = new ViewHoloder();
				convertView = LayoutInflater.from(getContext()).inflate(
						R.layout.item_tweet_comment, null);

				h.tv_name = (TextView) convertView
						.findViewById(R.id.item_tweet_comment_author);
				h.tv_content = (TextView) convertView
						.findViewById(R.id.item_tweet_comment_content);
				h.tv_time = (TextView) convertView
						.findViewById(R.id.item_tweet_comment_time);
				convertView.setTag(h);
			} else {
				h = (ViewHoloder) convertView.getTag();
			}
			Comment c = list.get(position);
			if (c.isIncognito()) {
				h.tv_name.setText("@某同学");
			} else {
				h.tv_name.setText("@" + c.getNickname());
			}
			h.tv_time.setText(StringUtils.getDateFormat(c.getSendTime(),
					"yy-mm-dd"));
			h.tv_content.setText(c.getContent());
			return convertView;
		}

		class ViewHoloder {
			TextView tv_name, tv_content, tv_time;
		}
	}

	/**
	 * 赞，不需要确定在服务端是否赞的到</br> 比较粗略，等待后期优化
	 * 
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
			if (!ETipsUtils.isTweetLogin(getContext())) {
				return;
			}
			// SP sp = new SP(ETipsContants.SP_NAME_User, getContext());
			// String senderID = sp.getValue("id");
			String senderID = ClientConfig.getUserId(getContext());
			if (senderID == null || senderID.equals("null")
					|| senderID.equals("")) {
				return;
			}

			String content = "赞！";
			String time = System.currentTimeMillis() + "";
			TweetAPI api = new TweetAPI(getContext());
			// 赞是不匿名的！！
			String json = api.comment(tweet.getTopicID(), tweet.getArticleID(),
					content, time, senderID, "0");
		}
	}
}
