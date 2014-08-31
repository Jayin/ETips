package com.meizhuo.etips.fragment;

import butterknife.ButterKnife;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BaseFragment extends Fragment {
	public View contentView;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState, int layoutResId) {
		contentView = inflater.inflate(layoutResId, container,false);
		ButterKnife.inject(this,contentView);
		return contentView;
	}
}
