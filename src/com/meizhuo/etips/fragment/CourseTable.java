package com.meizhuo.etips.fragment;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import butterknife.InjectView;
import butterknife.OnClick;

import com.meizhuo.etips.activities.ETipsApplication;
import com.meizhuo.etips.activities.R;
import com.meizhuo.etips.activities.SubSystemLoginActivity;
import com.meizhuo.etips.adapter.CourseAllWeekAdapter;
import com.meizhuo.etips.model.Lesson;

/**
 * 课程表
 * @author Jayin
 *
 */
public class CourseTable extends BaseFragment {
	private List<List<List<Lesson>>> course; // 课程
	private ETipsApplication App;
	@InjectView(R.id.acty_course_main_allweek) View v_allweek;
	@InjectView(R.id.acty_course_main_no_lesson) View v_notlesson;
	@InjectView(R.id.acty_course_main_login) Button btn_login;
	@InjectView(R.id.acty_course_main_listview)ListView lv;
	
	

	@Override public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState,
				R.layout.fragment_coursetable);
		return contentView;
	}
	
	
	@Override public void onResume() {
		super.onResume();
		App = (ETipsApplication) getActivity().getApplication();
		course = App.getLessonList(); // course maybe null!!!!!
		if(course == null){
			v_allweek.setVisibility(View.GONE);
			v_notlesson.setVisibility(View.VISIBLE);
		}else{
			
			v_allweek.setVisibility(View.VISIBLE);
			v_notlesson.setVisibility(View.GONE);
			lv.setAdapter(new CourseAllWeekAdapter(getContext(), course));
		}
	}
	
	@OnClick(R.id.acty_course_main_login)
	public void login(){
		Intent intent =new Intent(getContext(),SubSystemLoginActivity.class);
		intent.putExtra("toWhere", "CourseMainActivity");
		openActivity(intent);
	}
	
 
}
