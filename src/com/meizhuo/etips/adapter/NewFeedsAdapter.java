package com.meizhuo.etips.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.meizhuo.etips.activities.R;

public class NewFeedsAdapter extends BaseAdapter {
	private Context mContext;

	public NewFeedsAdapter(Context context) {
		this.mContext = context;
	}

	@Override public int getCount() {

		return 20;
	}

	@Override public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override public View getView(int position, View convertView,
			ViewGroup parent) {
		ViewHolder h;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_newfeeds, null);
			h = new ViewHolder(convertView);
			convertView.setTag(h);
		} else {
			h = (ViewHolder) convertView.getTag();
		}
		h.tv_title.setText("工业4.0: 颠覆全球制造业的新思维");

		h.tv_from.setText("商业评论网");
		h.tv_time.setText("2014-08-21");
	
		return convertView;
	}

	static class ViewHolder {
		@InjectView(R.id.tv_title) TextView tv_title;
		@InjectView(R.id.tv_from) TextView tv_from;
		@InjectView(R.id.tv_time) TextView tv_time;

		public ViewHolder(View view) {
			ButterKnife.inject(this, view);
		}
	}

}
