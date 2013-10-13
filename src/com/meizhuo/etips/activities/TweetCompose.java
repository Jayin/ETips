package com.meizhuo.etips.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.meizhuo.etips.common.utils.AndroidUtils;
import com.meizhuo.etips.common.utils.ETipsContants;
import com.meizhuo.etips.common.utils.ETipsUtils;
import com.meizhuo.etips.common.utils.Elog;
import com.meizhuo.etips.common.utils.JSONParser;
import com.meizhuo.etips.common.utils.SP;
import com.meizhuo.etips.model.Tweet;
import com.meizhuo.etips.net.utils.TweetAPI;
import com.meizhuo.etips.ui.utils.BaseNotification;

/**
 * function : 发布(compose)+评论(comment)
 * 
 * @author Jayin Ton
 * 
 */
public class TweetCompose extends BaseUIActivity implements OnClickListener {
	private View back, send, incognito, check;
	private TextView tv_count;
	private EditText et_comment;
	private boolean isCognito = false;
	private String function = "compose"; //compose 发帖 comment 评论
	private Tweet tweet;
	private boolean enableIncognito = false; // 是否可以匿名发布
	private String id; // 发布人学号
	private String topic_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_tweet_compose);
		initData();
		initLayout();
	}

	@Override
	protected void initLayout() {
		back = _getView(R.id.acty_tweet_comment_btn_back);
		incognito = _getView(R.id.acty_tweet_comment_btn_check);
		send = _getView(R.id.acty_tweet_comment_btn_send);
		et_comment = (EditText) _getView(R.id.acty_tweet_comment_content);
		tv_count = (TextView) _getView(R.id.acty_tweet_comment_tv_count);
        check = _getView(R.id.acty_tweet_comment_btn_check);
		tv_count.setText("0/140");
		back.setOnClickListener(this);
		incognito.setOnClickListener(this);
		send.setOnClickListener(this);
		et_comment.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				tv_count.setText(s.length() + "/140");
				if (tv_count.getText().toString().length() > 140)
					tv_count.setTextColor(Color.RED);
				else {
					tv_count.setTextColor(Color.WHITE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	@Override
	protected void initData() {
		function = getIntent().getStringExtra("function");
		topic_id = getIntent().getStringExtra("topic_id");
		if (function.equals("comment")) {
			tweet = (Tweet) getIntent().getSerializableExtra("Tweet");
		}
		enableIncognito = getIntent().getBooleanExtra("enableIncognito", true);
		SP sp = new SP(ETipsContants.SP_NAME_User, getContext());
		id = sp.getValue("id");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.acty_tweet_comment_btn_back:
			closeActivity();
			break;

		case R.id.acty_tweet_comment_btn_check:
			// 发帖子的时候才检查是否要求匿名
			if (function.equals("compose") && !enableIncognito) {
				toast("这里不允许匿名发布哦！");
				return;
			}
			if (isCognito) {
				check.setBackgroundResource(R.drawable.ic_check_nomal);
				isCognito = false;
			} else {
				check.setBackgroundResource(R.drawable.ic_check_press);
				isCognito = true;
			}

			break;
		case R.id.acty_tweet_comment_btn_send:
			if (et_comment.getText().toString().length() > 140) {
				toast("字数超出范围！");
				return;
			}
			if (!ETipsUtils.isTweetLogin(getContext())) {
				toast("请先登录ETips账号");
				return;
			} else if (ETipsUtils.isTweetLoginTimeOut(getContext())) {
				toast("登录已失效，请重新登录ETips账号");
				return;
			}
			if(!AndroidUtils.isNetworkConnected(getContext())){
				toast("请检查你的网络");
				return ;
			}
			// 开启线程去 server 去发送 注意检查字数
			new PostTask().start();
			closeActivity();
			break;
		}
	}

	class PostTask extends Thread {
		@Override
		public void run() {
			BaseNotification notification = new BaseNotification(
					TweetCompose.this);
			notification.setContentTitle("校园资讯");
			notification.setContentText("发送中....");
			notification.setVibrate(new long[0]);
			notification.setSound(null);
			notification.setNotificationID(ETipsContants.ID_Send_Tweet);
			notification.setTicker("发送中....");
			notification.show();
			if (function.equals("compose")) {
				// 判断是否发帖数量达到上限
				if (ETipsUtils.enableSend(getContext())) {
					// your code,虽然下面不美观，但是为了方便调试 = =
					TweetAPI api = new TweetAPI(getContext());
				 
					String content = et_comment.getText().toString();
					String sendTime = System.currentTimeMillis() + "";

					String response = api.compose(topic_id, content, id,
							sendTime, isCognito ? "1" : "0");
					notification.cancle();
					if (JSONParser.isOK(response)) {
						ETipsUtils.addSendCount(getContext());
						notification.setContentText("发布成功");
						notification.setTicker("发布成功");
						notification.show();
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						} finally {
							notification.cancle();
						}
					} else {	 
						notification.setTicker("发布失败");
						notification.setContentText("发布失败");
						notification.show();
					}
				} else {
					// can't send!
					notification.cancle();
					notification.setTicker("发布失败，今日发布次数达到上限");
					notification.setContentText("发布失败，今日发布次数达到上限");
					notification.show();
				}
               //评论
			} else if (function.equals("comment")) {
				// your code
				TweetAPI api  =new TweetAPI(getContext()); 
				String topic_id = tweet.getTopicID();
				String  article_id = tweet.getArticleID();
				String content =  et_comment.getText().toString();
				String sendTime = System.currentTimeMillis()+"";
				String author = id;
				String response =  api.comment(topic_id, article_id, content, sendTime, author, isCognito?"1":"0");
				notification.cancle();
				if(JSONParser.isOK(response)){
					notification.setTicker("评论成功");
					notification.setContentText("评论成功");
					notification.show();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} finally {
						notification.cancle();
					}
				}else{
					notification.setTicker("评论失败,"+JSONParser.getStatus(response));
					notification.setContentText("评论失败,"+JSONParser.getStatus(response));
					notification.show();
				}
			}

		}
	}

}
