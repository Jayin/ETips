package com.meizhuo.etips.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.meizhuo.etips.activities.R;
import com.meizhuo.etips.model.Lesson;
import com.meizhuo.etips.ui.CourseSwitchDialog.CourseSwitchDialogCallBack;
/**
 * 单页课程适配器
 * @author Jayin Ton
 *
 */
public class CourseListViwAdapter extends BaseAdapter {
	private List<List<Lesson>> course;
	private List<String> ltime;
	private Context context;
	public CourseListViwAdapter(List<List<Lesson>> course,Context context){
		 this.course  =course;
		 this.context = context;
		 ltime = new ArrayList<String>();
		 String[] _s = context.getResources().getStringArray(R.array.course_time_start);
		 for(String s : _s)
			 ltime.add(s);
		 /**
		  * 08:15-09:50
		  * 10:10-11:45
		  * 14:45-16:20
		  * 16:30-18:05 一大节耗时95分钟
		  * 19:30-21:05
		  */
	}
	@Override
	public int getCount() {
		 
		return course.size();
	}

	@Override
	public Object getItem(int position) {
		 
		return course.get(position);  //这里可能会出错
	}

	@Override
	public long getItemId(int position) {
		 
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    final ViewHodler holder  ;
	    final int _position = position;
	    if(convertView == null){
	    	holder  = new ViewHodler();
	    	convertView = LayoutInflater.from(context).inflate(R.layout.item_listview_lesson,null);
	    	holder.tv_itemId = (TextView)convertView.findViewById(R.id.item_listview_lesson_textview_num);
	    	holder.tv_ltime = (TextView)convertView.findViewById(R.id.item_listview_lesson_textview_ltime);
	    	holder.tv_lesson = (TextView)convertView.findViewById(R.id.item_listview_lesson_textView_lesson);
	    	holder.tv_time = (TextView)convertView.findViewById(R.id.item_listview_lesson_textView_lesson_time);
	    	holder.tv_teacher = (TextView)convertView.findViewById(R.id.item_listview_lesson_textView_lesson_teacher);
	    	holder.tv_address = (TextView)convertView.findViewById(R.id.item_listview_lesson_textView_lesson_address);
	    	holder.imageBtn = (ImageButton)convertView.findViewById(R.id.item_listview_lesson_imageButton1);
	    	
	    	convertView.setTag(holder);
	    	
	    }else{
	    	holder = (ViewHodler)convertView.getTag();
	    }
	    
	    Lesson l = course.get(position).get(0); 
	    holder.tv_itemId.setText(String.valueOf(position+1));
	    holder.tv_ltime.setText(ltime.get(position));
	    holder.tv_lesson.setText(l.LessonName);
	    holder.tv_time.setText(l.Time);
	    holder.tv_teacher.setText(l.Teacher);
	    holder.tv_address.setText(l.address);
	    if(course.get(position).size()>1){
	    	holder.imageBtn.setVisibility(View.VISIBLE);
	    	holder.imageBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//改变 课程的顺序
					CourseSwitchDialog dialog = new CourseSwitchDialog(context, course.get(_position), new CourseSwitchDialogCallBack() {
						@Override
						public void SetText(Lesson lesson) {
							    CourseListViwAdapter.this.notifyDataSetChanged();
							    holder.tv_lesson.setText(lesson.LessonName);
							    holder.tv_time.setText(lesson.Time);
							    holder.tv_teacher.setText(lesson.Teacher);
							    holder.tv_address.setText(lesson.address);
							
						}
					});
					dialog.show();
				}
			});
	    }else{
	    	holder.imageBtn.setVisibility(View.INVISIBLE);
	    }
	    
		return convertView;
	}
	class  ViewHodler{
		TextView tv_itemId,tv_ltime,tv_lesson,tv_time,tv_teacher,tv_address;
		ImageButton imageBtn;
	}

}
