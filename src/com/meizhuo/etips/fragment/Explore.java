package com.meizhuo.etips.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.OnClick;

import com.meizhuo.etips.activities.LibraryMainActivity;
import com.meizhuo.etips.activities.LifeElectricityActivity;
import com.meizhuo.etips.activities.ManualMainActivity;
import com.meizhuo.etips.activities.MsgCenterActivity;
import com.meizhuo.etips.activities.Notes;
import com.meizhuo.etips.activities.QueryEmptyClassroom;
import com.meizhuo.etips.activities.R;
import com.meizhuo.etips.activities.SchoolNewsMainActivity;
import com.meizhuo.etips.activities.SubSystemLoginActivity;

/**
 * 发现
 * 
 * @author Jayin
 * 
 */
public class Explore extends BaseFragment {

	@Override public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState,
				R.layout.fragment_explore);
	 
		return contentView;
	}
	
	@OnClick(R.id.btn_message_center)
	public void msg_center(){
		openActivity(MsgCenterActivity.class);
	}
	
	@OnClick(R.id.btn_wyunews)
	public void wyunews(){
		openActivity(SchoolNewsMainActivity.class);
	}
	
	@OnClick(R.id.btn_library)
	public void library(){
		openActivity(LibraryMainActivity.class);
	}
	
	
	@OnClick(R.id.btn_query_classroom)
	public void query_classroom(){
		openActivity(QueryEmptyClassroom.class);
	}
	
	@OnClick(R.id.btn_check_elcecity)
	public void check_elcecity(){
		openActivity(LifeElectricityActivity.class);
	}
	
	@OnClick(R.id.btn_notes)
	public void notes(){
		openActivity(Notes.class);
	}
	
	@OnClick(R.id.btn_check_source)
	public void check_source(){
		Intent intent =new Intent(getContext(),SubSystemLoginActivity.class);
		intent.putExtra("toWhere", "ScoreRecordActivity");
		openActivity(intent);
	}
	
	@OnClick(R.id.btn_manual)
	public void manual(){
		openActivity(ManualMainActivity.class);
	}
	
	
}
