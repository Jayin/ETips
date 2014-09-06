package com.meizhuo.etips.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import butterknife.InjectView;
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
import com.meizhuo.etips.app.Preferences;

/**
 * 发现
 * 
 * @author Jayin
 * 
 */
public class Explore extends BaseFragment {
	@InjectView(R.id.iv_new_msg) ImageView iv_new_msg;

	@Override public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState,
				R.layout.fragment_explore);
		
		return contentView;
	}
	
	@Override public void onResume() {
		super.onResume();
		if (Preferences.isHasMsgToCheck(getContext())) {
			iv_new_msg.setVisibility(View.VISIBLE);
		}else{
			iv_new_msg.setVisibility(View.INVISIBLE);
		}
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
