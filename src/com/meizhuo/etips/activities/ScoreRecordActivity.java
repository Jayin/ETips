package com.meizhuo.etips.activities;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.meizhuo.etips.common.utils.ETipsContants;
import com.meizhuo.etips.model.ScoreRecord;
import com.meizhuo.etips.net.utils.SubSystemAPI;

public class ScoreRecordActivity extends BaseUIActivity {
	private String userID, userPSW;
	private ProgressBar pb;
	private TextView tv_title, tv_error, tv_jidian;
	private ListView lv;
	private List<ScoreRecord> list;
	private Button backbtn, reflushBtn;
	private boolean hasData = false; // to define wheather listview's adapter
										// has date or not
	private SubSystemAPI api;
	private ETipsApplication App;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_score_record);
		initData();
		initLayout();
		onWork();
	}

	private void onWork() {

		// create handler and thead to get data & update UI
		
		SRHandler handler = new SRHandler();
		new SRThread(handler).start();
	}

	@Override
	protected void initLayout() {
		pb = (ProgressBar) this
				.findViewById(R.id.acty_score_record_progressBar);
		tv_title = (TextView) this
				.findViewById(R.id.acty_score_record_title_tv);
		tv_error = (TextView) this
				.findViewById(R.id.acty_score_record_tv_error_scoreRecord);
		tv_jidian = (TextView) this
				.findViewById(R.id.acty_score_record_jidian);
		lv = (ListView) this.findViewById(R.id.acty_score_record_listview);
		backbtn = (Button) this.findViewById(R.id.acty_score_record_back);
		reflushBtn = (Button) this.findViewById(R.id.acty_score_record_reflush);

		tv_title.setText(userID);
		backbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ScoreRecordActivity.this.finish();
			}
		});

		reflushBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (hasData) {
					Toast.makeText(ScoreRecordActivity.this, "成绩已经更新！",
							Toast.LENGTH_SHORT).show();
				} else {
					onWork();
				}
			}
		});
	}

	@Override
	protected void initData() {
		userID = getIntent().getStringExtra("userID");
		userPSW = getIntent().getStringExtra("userPSW");
		App = (ETipsApplication)getApplication();
		api = App.getSubSystemAPI();
	}

	class SRHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ETipsContants.Start:
				pb.setVisibility(View.VISIBLE);
				tv_error.setVisibility(View.GONE);
				lv.setVisibility(View.GONE);
				tv_jidian.setText("ETips认真计算中....");
				break;
			case ETipsContants.Finish:
				if (hasData)
					return;
				hasData = true;
				pb.setVisibility(View.GONE);
				tv_error.setVisibility(View.GONE);
				lv.setVisibility(View.VISIBLE);
				//
				lv.setAdapter(new SRAdapter());
				String jidian = calculate(list);
				tv_jidian.setText(jidian);
				break;
			case ETipsContants.Fail:
				if (hasData)
					return;
				pb.setVisibility(View.GONE);
				tv_error.setVisibility(View.VISIBLE);
				lv.setVisibility(View.GONE);
				tv_jidian.setText("null");
				if(msg.obj != null && msg.obj instanceof String){
					Toast.makeText(ScoreRecordActivity.this, (String)msg.obj, Toast.LENGTH_SHORT).show();
				}
				break;

			}
		}

		private String calculate(List<ScoreRecord> list) {
			double totalScore = 0.0; // 总绩点
			double totalLessonScore = 0.0; // 总学分
			for (ScoreRecord sr : list) {
				if (sr.category.equals("必修")) {
					double mscore = 0;
					 
				    if(sr.score.equals("优秀")){
				    	mscore = 4.5;
				    } else if(sr.score.equals("良好")){
				    	mscore = 3.5;
				    } else if(sr.score.equals("中等")){
				    	mscore = 2.5;
				    } else if(sr.score.equals("及格")){
				    	mscore = 1.5;
				    } else if(sr.score.equals("不及格")){
				    	mscore = 0;
				    } else if(sr.score.equals("旷考")){
				    	mscore = 0;
				    }else{
				    	mscore = (Double.parseDouble(sr.score) - 50)/ 10; 
				    }
					totalScore +=mscore * Double.parseDouble(sr.lessonScore);
					totalLessonScore += Double.parseDouble(sr.lessonScore);
				}
			}
			return String.valueOf(totalScore / totalLessonScore);
		}
	}

	class SRThread extends Thread {
		Handler handler;

		public SRThread(Handler handler) {
			this.handler = handler;
		}

		@Override
		public void run() {
			handler.sendEmptyMessage(ETipsContants.Start);
//			SubSystemAPI api = new SubSystemAPI(userID, userPSW);
            if(api  == null ){
            	api = new SubSystemAPI(userID, userPSW);
            	try {
					if(!api.login()){
					    Message msg = handler.obtainMessage();
					    msg.obj = "未知错误，请重新登录";
					    msg.what  =ETipsContants.Fail;
					    handler.sendMessage(msg);
					    return;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
            }
			try {
				list = api.getScoreList();
				if (list.size() > 0)
					handler.sendEmptyMessage(ETipsContants.Finish);
				else
					handler.sendEmptyMessage(ETipsContants.Fail);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				handler.sendEmptyMessage(ETipsContants.Fail);
			} catch (IOException e) {
				e.printStackTrace();
				handler.sendEmptyMessage(ETipsContants.Fail);
			}
		}

	}

	class SRAdapter extends BaseAdapter {

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
			SRViewHolder holder;
			if (convertView == null) {
				holder = new SRViewHolder();
				convertView = LayoutInflater.from(ScoreRecordActivity.this)
						.inflate(R.layout.item_score_record, null);
				holder.tv_lessonName = (TextView) convertView
						.findViewById(R.id.item_score_record_lessonName);
				holder.tv_lessonScore = (TextView) convertView
						.findViewById(R.id.item_score_record_lessonScore);
				holder.tv_score = (TextView) convertView
						.findViewById(R.id.item_score_record_score1);
				holder.tv_category = (TextView) convertView
						.findViewById(R.id.item_score_record_category);
				convertView.setTag(holder);
			} else {
				holder = (SRViewHolder) convertView.getTag();
			}
			ScoreRecord sr = list.get(position);
			holder.tv_lessonName.setText(sr.lessonName);
			holder.tv_lessonScore.setText("学分:"+sr.lessonScore);
			holder.tv_score.setText(sr.score);
			holder.tv_category.setText(sr.category);
			return convertView;
		}

		class SRViewHolder {
			TextView tv_lessonName, tv_lessonScore, tv_score, tv_category;
		}

	}
}
