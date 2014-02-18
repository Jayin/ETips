package com.meizhuo.etips.activities;

import org.apache.http.Header;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.meizhuo.etips.app.ClientConfig;
import com.meizhuo.etips.common.AndroidUtils;
import com.meizhuo.etips.common.ETipsContants;
import com.meizhuo.etips.common.ETipsUtils;
import com.meizhuo.etips.common.JSONParser;
import com.meizhuo.etips.net.utils.TweetAPI;
import com.meizhuo.etips.ui.base.BaseNotification;

/**
 * function : 发布(compose)+评论(comment)
 * 
 * @author Jayin Ton
 * 
 */
public class TweetCompose extends BaseUIActivity implements OnClickListener {
	private View back, send, incognito, check;
	private TextView tv_count,tv_title;
	private EditText et_comment;
	private boolean isCognito = false;
	private String function = "compose"; // compose 发帖 comment 评论
	private boolean enableIncognito = false; // 是否可以匿名发布
	private String id; // 发布人学号
	private String topic_id,article_id;
	private String to_comment_id;
	private String author;//原评论的id
	private String nickname;//原评论名称

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
		tv_title = (TextView)_getView(R.id.tv_title);
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
		if (enableIncognito) {
			isCognito = true;
			check.setBackgroundResource(R.drawable.ic_check_press);
		}
		// 如果是回复评论:
		if (author != null) {
			tv_title.setText("回复");
			isCognito = false;
			check.setBackgroundResource(R.drawable.ic_check_nomal);
			et_comment.setHint("回复@"+nickname+":");
		}
	}

	@Override
	protected void initData() {
		function = getIntent().getStringExtra("function");
		topic_id = getIntent().getStringExtra("topic_id"); 
		article_id = getIntent().getStringExtra("article_id");
		if (function.equals("reply")) {  //回复
			author = getIntent().getStringExtra("author"); //原评论者的id
			to_comment_id = getIntent().getStringExtra("to_comment_id");
			nickname = getIntent().getStringExtra("nickname");
		}
		enableIncognito = getIntent().getBooleanExtra("enableIncognito", true);
		//
		id = ClientConfig.getUserId(getContext());
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
			if (et_comment.getText().toString().length() > 140 || et_comment.getText().toString().length()==0) {
				toast("字数为0或超出范围！");
				return;
			}
			if (!ETipsUtils.isTweetLogin(getContext())) {
				toast("请先登录ETips账号");
				return;
			} else if (ETipsUtils.isTweetLoginTimeOut(getContext())) {
				toast("登录已失效，请重新登录ETips账号");
				return;
			}
			if (!AndroidUtils.isNetworkConnected(getContext())) {
				toast("请检查你的网络");
				return;
			}
			if (function.equals("reply")) {
				final BaseNotification notification = new BaseNotification(
						TweetCompose.this);
				String content = et_comment.getText().toString();
				String sendTime = System.currentTimeMillis() + "";
				AsyncHttpClient client = new AsyncHttpClient();
				RequestParams params = new RequestParams();
				params.add("to_comment_id", to_comment_id);
				params.add("to_author", author);
                params.add("content",content);
                params.add("sendTime", sendTime);
                params.add("incognito", isCognito ? "1" : "0");
                params.add("article_id", article_id);
                params.add("topic_id", topic_id);
                params.add("author", ClientConfig.getUserId(getContext()));
				client.get(TweetAPI.BaseUrl + "comment.php", params,
						new AsyncHttpResponseHandler() {
							@Override
							public void onStart() {
								notification.setContentTitle("校园资讯");
								notification.setContentText("发送中....");
								notification.setVibrate(new long[0]);
								notification.setSound(null);
								notification
										.setNotificationID(ETipsContants.ID_Send_Tweet);
								notification.setTicker("发送中....");
								notification.show();
							}

							@Override
							public void onSuccess(int arg0, Header[] arg1,
									byte[] data) {
								notification.cancle();
								if (JSONParser.isOK(new String(data))) {
									notification.setContentText("回复成功");
									notification.setTicker("回复成功");
									notification.show();
									try {
										Thread.sleep(1000);
									} catch (InterruptedException e) {
										e.printStackTrace();
									} finally {
										notification.cancle();
									}
								} else {
									// can't send!
									notification.cancle();
									notification.setTicker("回复失败  错误码:"+JSONParser.getStatusCode(new String(data)));
									notification
											.setContentText("回复失败  错误码:"+JSONParser.getStatusCode(new String(data)));
									notification.show();
								}
							}

							@Override
							public void onFailure(int arg0, Header[] arg1,
									byte[] arg2, Throwable arg3) {
								// can't send!
								notification.cancle();
								notification.setTicker("回复失败，网络异常");
								notification.setContentText("回复失败，网络异常");
								notification.show();
							}
						});
			} else { // comment or compose
						// 开启线程去 server 去发送 注意检查字数
				new PostTask().start();
			}

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
					debug("response");
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
				// 评论
			} else if (function.equals("comment")) {
				// your code
				TweetAPI api = new TweetAPI(getContext());
				String content = et_comment.getText().toString();
				String sendTime = System.currentTimeMillis() + "";
				String author = id;
				String response = api.comment(topic_id, article_id, content,
						sendTime, author, isCognito ? "1" : "0");
				notification.cancle();
				if (JSONParser.isOK(response)) {
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
				} else {
					notification.setTicker("评论失败,"
							+ JSONParser.getStatus(response));
					notification.setContentText("评论失败,"
							+ JSONParser.getStatus(response));
					notification.show();
				}
			}

		}
	}

}
