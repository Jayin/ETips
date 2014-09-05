package com.meizhuo.etips.activities;

import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.meizhuo.etips.common.AndroidUtils;
import com.meizhuo.etips.model.ClassroomInfo;
import com.meizhuo.etips.model.CourseQueryData;
import com.meizhuo.etips.net.utils.CourseQueryAPI;

public class QueryEmptyClassroom extends BaseUIActivity {
	private ViewFlipper flipper;
	private ListView lv;
	private Spinner sp_week, sp_weektime, sp_address;
	private TextView tv_loadInfo;
	// , tv_switch, tv_title;
	private int status = 0;// 0为输入信息界面 1为结果界面
	private List<ClassroomInfo> list = null;

	@Override protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_queryemptyclassroom);
		initData();
		initLayout();
	}

	@Override public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.acty_queryemptyclassroom, menu);
		return true;
	}

	@Override public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.work) {
			if (item.getTitle().equals("查询")) {
				if (!AndroidUtils.isNetworkConnected(getContext())) {
					toast("请检查你的网络！");
					return super.onOptionsItemSelected(item);
				}
				new QueryTask().execute();
				item.setTitle("重查");

			} else {
				item.setTitle("查询");
				status = 0;
				switchView();
				flipper.showPrevious();
			}
		}
		return super.onOptionsItemSelected(item);
	}

	@Override protected void initLayout() {
		flipper = (ViewFlipper) _getView(R.id.acty_query_flipper);
		sp_week = (Spinner) _getView(R.id.acty_query_week);
		sp_weektime = (Spinner) _getView(R.id.acty_query_weektime);
		sp_address = (Spinner) _getView(R.id.acty_query_address);
		tv_loadInfo = (TextView) _getView(R.id.acty_query_loadinfo);
		lv = (ListView) _getView(R.id.acty_query_listview);

		ArrayAdapter<CharSequence> weekAdapter = ArrayAdapter
				.createFromResource(this, R.array.week,
						R.layout.item_simple_spinner);
		ArrayAdapter<CharSequence> weekTimeAdapter = ArrayAdapter
				.createFromResource(this, R.array.weekTime,
						R.layout.item_simple_spinner);
		ArrayAdapter<CharSequence> addressAdapter = ArrayAdapter
				.createFromResource(this, R.array.address,
						R.layout.item_simple_spinner);
		sp_week.setAdapter(weekAdapter);
		sp_weektime.setAdapter(weekTimeAdapter);
		sp_address.setAdapter(addressAdapter);

		status = 0;
		switchView();
		flipper.setInAnimation(this, R.anim.push_up_in);
		flipper.setOutAnimation(this, R.anim.push_up_out);

	}

	@Override protected void initData() {

	}

	// 先改变status再switchView()
	private void switchView() {
		if (status == 0) {
			setActionBarTitle("课室查询");

		} else {
			String word = (String) sp_address.getSelectedItem()
					+ (String) sp_week.getSelectedItem()
					+ (String) sp_weektime.getSelectedItem();
			setActionBarTitle(word);

		}
	}

	class QueryTask extends AsyncTask<Void, Integer, Void> {

		@Override protected void onPreExecute() {
			tv_loadInfo.setVisibility(View.VISIBLE);
			tv_loadInfo.setText("查询中....");
		}

		@Override protected void onProgressUpdate(Integer... values) {
			switch (values[0]) {
			case 1:
				tv_loadInfo.setText("正在分析....");
				break;

			case 2:
				toast("无相关信息");
				break;
			case 3:
				toast("网络异常");
				break;
			case 4:
				toast("分析时出现异常，这个一个BUG!");
				break;
			}

		}

		@Override protected Void doInBackground(Void... params) {
			try {
				CourseQueryAPI api = new CourseQueryAPI();
				String week = (sp_week.getSelectedItemPosition() + 1) + "";
				String weekTime = (sp_weektime.getSelectedItemPosition() + 1)
						+ "";
				String address = (String) sp_address.getSelectedItem();
				List<CourseQueryData> mList = api.queryClassroom(week,
						weekTime, address);
				publishProgress(1);
				if (mList != null) {
					list = api.getClassroomInfo(mList);
					if (list == null) {
						publishProgress(4);
					}

				}
			} catch (Exception e) {
				publishProgress(3);
				return null;
			}
			return null;
		}

		@Override protected void onPostExecute(Void result) {
			tv_loadInfo.setVisibility(View.GONE);
			if (list != null) {
				if (list.size() == 0) {
					toast("无相关信息");
				} else {
					// 更新listview。。。。
					lv.setAdapter(new MyAdapter());
					status = 1;
					switchView();
					flipper.showNext();
				}
			}
		}
	}

	class MyAdapter extends BaseAdapter {

		@Override public int getCount() {

			return list.size();
		}

		@Override public Object getItem(int position) {

			return list.get(position);
		}

		@Override public long getItemId(int position) {
			return position;
		}

		@Override public View getView(int position, View convertView,
				ViewGroup parent) {
			ViewHolder h = null;
			if (convertView == null) {
				h = new ViewHolder();
				convertView = LayoutInflater.from(getContext()).inflate(
						R.layout.item_lv_emptyclassroom, null);
				h.tv_roomNumber = (TextView) convertView
						.findViewById(R.id.item_lv_room);
				h.iv1 = (ImageView) convertView
						.findViewById(R.id.item_lv_emptyclassroom_1);
				h.iv2 = (ImageView) convertView
						.findViewById(R.id.item_lv_emptyclassroom_2);
				h.iv3 = (ImageView) convertView
						.findViewById(R.id.item_lv_emptyclassroom_3);
				h.iv4 = (ImageView) convertView
						.findViewById(R.id.item_lv_emptyclassroom_4);
				h.iv5 = (ImageView) convertView
						.findViewById(R.id.item_lv_emptyclassroom_5);
				convertView.setTag(h);
			} else {
				h = (ViewHolder) convertView.getTag();
			}
			h.tv_roomNumber.setText(list.get(position).getRoomNumber());
			List<String> mStatus = list.get(position).getStatus();
			change(mStatus.get(0), h.iv1);
			change(mStatus.get(1), h.iv2);
			change(mStatus.get(2), h.iv3);
			change(mStatus.get(3), h.iv4);
			change(mStatus.get(4), h.iv5);
			return convertView;
		}

		private void change(String _status, ImageView iv) {
			if (_status.equals("free")) {
				iv.setImageResource(R.drawable.ic_check_free);
			} else {
				iv.setImageResource(R.drawable.ic_check_busy);
			}
		}

		class ViewHolder {
			TextView tv_roomNumber;
			ImageView iv1, iv2, iv3, iv4, iv5;
		}

	}
}
