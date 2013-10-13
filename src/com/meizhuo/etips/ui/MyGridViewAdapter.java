package com.meizhuo.etips.ui;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.meizhuo.etips.activities.R;
import com.meizhuo.etips.common.utils.ETipsContants;
import com.meizhuo.etips.common.utils.Elog;
import com.meizhuo.etips.common.utils.SharedPreferenceHelper;

public class MyGridViewAdapter extends BaseAdapter {
	private List<String> nameList;
	private Context context;
	private AbsListView.LayoutParams layoutParams;

	// private List<Bitmap> bitmap;
	// private List<Integer> resourceId;
	public MyGridViewAdapter(Context context, List<String> nameList) {
		this.context = context;
		this.nameList = nameList;

	}

	@Override
	public int getCount() {

		return nameList.size();
	}

	@Override
	public Object getItem(int positon) {

		return nameList.get(positon);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * toMeasureItem(ETipsMainActivity.this) 动态设置Item的大小！！！
	 * 
	 * @param a
	 * @return
	 */
	public MyGridViewAdapter toMeasureItem(Activity a) {
		WindowManager wm = a.getWindowManager();
		DisplayMetrics dm = new DisplayMetrics();
		int padingOfItem = 20;
		wm.getDefaultDisplay().getMetrics(dm);
		layoutParams = new AbsListView.LayoutParams(dm.widthPixels / 2
				- padingOfItem - 10, dm.heightPixels / 4);
		return this;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.i("debug", "get "+position+":"+nameList.get(position));
		ViewHolder h =  null;
	 
		if (convertView == null) {
			h = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_gridview_one, null);
			h.tv_name = (TextView)convertView.findViewById(R.id.item_gridview_tv_text);
			h.iv = (ImageView)convertView.findViewById(R.id.item_gridview_image_new);
			convertView.setTag(h);
		}else{
			h = (ViewHolder)convertView.getTag();
		}
		h.tv_name.setText(nameList.get(position));
		if(position ==  5 ){
			Log.i("debug", "NEW! "+position+":"+nameList.get(position));
			SharedPreferences sp = context.getSharedPreferences(ETipsContants.SharedPreference_NAME, Context.MODE_PRIVATE);
			String value = sp.getString("Has_Msg_To_Check", "NO");
			if(value.equals("YES")){
				h.iv.setVisibility(View.VISIBLE);
			}
		}
		convertView.setLayoutParams(layoutParams);
		return convertView;
	}

	class ViewHolder {
		TextView tv_name;
		ImageView iv;
	}

}
