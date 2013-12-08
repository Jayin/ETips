package com.meizhuo.etips.ui;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.meizhuo.etips.activities.R;
import com.meizhuo.etips.model.BookInfo;

public class LibSearchResultListViewAdapter extends BaseAdapter {
	private List<BookInfo> list;
	private Context context;

	public LibSearchResultListViewAdapter(Context context ,List<BookInfo> list) {
		this.context  = context;
		this.list = list;

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
		ViewHolder hodler;
		if(convertView == null){
			hodler = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_library_searchresult, null);
			hodler.bookName =(TextView) convertView.findViewById(R.id.item_library_searchresult_bookname);
			hodler.author =(TextView) convertView.findViewById(R.id.item_library_searchresult_author);
			hodler.exactNumber =(TextView) convertView.findViewById(R.id.item_library_searchresult_askid_tv);
			hodler.total =(TextView) convertView.findViewById(R.id.item_library_searchresult_total);
			hodler.left =(TextView) convertView.findViewById(R.id.item_library_searchresult_left);
			convertView.setTag(hodler);
			
		}else{
			hodler = (ViewHolder)convertView.getTag();
		}
		BookInfo b= list.get(position);
		hodler.bookName.setText(b.BookName);
		hodler.author.setText(b.Authors);
		hodler.exactNumber.setText(b.ExactNumber);
		hodler.total.setText("馆藏:"+b.Totle);
		hodler.left.setText("可借:"+b.Left);
		return convertView;
	}
    class ViewHolder{
    	TextView bookName,author,exactNumber,total,left;
    }
}
