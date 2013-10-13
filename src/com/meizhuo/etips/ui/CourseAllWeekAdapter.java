package com.meizhuo.etips.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.meizhuo.etips.activities.R;
import com.meizhuo.etips.model.Lesson;

//适配数据：同一课时，不同星期的课程 list.get(i).get(x).get(0);其中i为0-6....x为固定值0-6 ...// 0代表同一课时的第一个课程
//课表为5行7列
public class CourseAllWeekAdapter extends BaseAdapter {
	private List<List<Lesson>> courseList;
	private Context context;

	public CourseAllWeekAdapter(Context context, List<List<List<Lesson>>> course) {
		this.context = context;
		courseList = translate(course); // 转换
	}

	private List<List<Lesson>> translate(List<List<List<Lesson>>> course) {
		List<List<Lesson>> mcourse = new ArrayList<List<Lesson>>();
		List<Lesson> mmcouuser = null;
		for (int classtime = 0; classtime <= 4; classtime++) {
			mmcouuser = new ArrayList<Lesson>();
			for (int week = 0; week <= 6; week++) {
				mmcouuser.add(course.get(week).get(classtime).get(0));
			}
			mcourse.add(mmcouuser);
		}
		return mcourse;
	}

	@Override
	public int getCount() {
		return courseList.size();
	}

	@Override
	public Object getItem(int position) {
		return courseList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CAWViewHolder h;
		if (convertView == null) {
			h = new CAWViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_course_allweek, null);
			h.tv_classtime = (TextView)convertView.findViewById(R.id.item_course_allweek_classtime);
			h.tv_first = (TextView) convertView
					.findViewById(R.id.item_course_allweek_first);
			h.tv_second = (TextView) convertView
					.findViewById(R.id.item_course_allweek_second);
			h.tv_third = (TextView) convertView
					.findViewById(R.id.item_course_allweek_third);
			h.tv_forth = (TextView) convertView
					.findViewById(R.id.item_course_allweek_forth);
			h.tv_fifth = (TextView) convertView
					.findViewById(R.id.item_course_allweek_fifth);
			h.tv_sixth = (TextView) convertView
					.findViewById(R.id.item_course_allweek_sixth);
			h.tv_seventh = (TextView) convertView
					.findViewById(R.id.item_course_allweek_seventh);
			convertView.setTag(h);
		} else {
			h = (CAWViewHolder) convertView.getTag();
		} 
		h.tv_classtime.setText(""+(position+1));
		h.tv_first.setText(courseList.get(position).get(0).LessonName);
		h.tv_second.setText(courseList.get(position).get(1).LessonName);
		h.tv_third.setText(courseList.get(position).get(2).LessonName);
		h.tv_forth.setText(courseList.get(position).get(3).LessonName);
		h.tv_fifth.setText(courseList.get(position).get(4).LessonName);
		h.tv_sixth.setText(courseList.get(position).get(5).LessonName);
		h.tv_seventh.setText(courseList.get(position).get(6).LessonName);
		return convertView;
	}

	class CAWViewHolder {
		TextView tv_classtime,tv_first, tv_second, tv_third, tv_forth, tv_fifth, tv_sixth,
				tv_seventh;
	}

}
