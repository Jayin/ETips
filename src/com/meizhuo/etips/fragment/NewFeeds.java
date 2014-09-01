package com.meizhuo.etips.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import butterknife.InjectView;

import com.meizhuo.etips.activities.R;
import com.meizhuo.etips.adapter.NewFeedsAdapter;

public class NewFeeds extends BaseFragment {
	@InjectView(R.id.lv_explore) ListView lv;

	@Override public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState,
				R.layout.fragment_newfeeds);
		lv.setAdapter(new NewFeedsAdapter(getActivity()));
		return contentView;
	}
	
	
	
}
