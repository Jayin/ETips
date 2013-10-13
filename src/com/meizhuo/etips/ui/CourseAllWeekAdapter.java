package com.meizhuo.etips.ui;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.meizhuo.etips.activities.CourseDetailActivity;
import com.meizhuo.etips.activities.R;
import com.meizhuo.etips.common.utils.CourseUtils;
import com.meizhuo.etips.model.Lesson;

//适配数据：同一课时，不同星期的课程 list.get(i).get(x).get(0);其中i为0-6....x为固定值0-6 ...// 0代表同一课时的第一个课程
//课表为5行7列
public class CourseAllWeekAdapter extends BaseAdapter {
	private List<List<Lesson>> courseList;
	private Context context;
	final private List<List<List<Lesson>>> _course; // 为了算出position

	// 为了进入CourseDetailActivity
	// ;这样麻烦，但是不会改变数据库内数据的顺序

	public CourseAllWeekAdapter(Context context, List<List<List<Lesson>>> course) {
		this.context = context;
		this._course = course;
		courseList = translate(course); // 转换
	}

	private List<List<Lesson>> translate(List<List<List<Lesson>>> course) {

		List<List<Lesson>> mcourse = new ArrayList<List<Lesson>>();
		List<Lesson> mmcouuser = null;
		for (int classtime = 0; classtime <= 4; classtime++) {
			mmcouuser = new ArrayList<Lesson>();
			for (int week = 0; week <= 6; week++) {
				Lesson l = course.get(week).get(classtime).get(0);
				int size = course.get(week).get(classtime).size();
				// 判断是否符合上课周数
				if (size > 1) {
					for (int i = 1; i < size; i++) {
						if (CourseUtils.isLessonStart(context, course.get(week)
								.get(classtime).get(i))) {
							l = course.get(week).get(classtime).get(i);
						}
					}
				}
				mmcouuser.add(l);
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
			h.tv_classtime = (TextView) convertView
					.findViewById(R.id.item_course_allweek_classtime);
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
			h.tv_first.setOnClickListener(new MyOnClickListener(courseList.get(
					position).get(0)));
			h.tv_second.setOnClickListener(new MyOnClickListener(courseList
					.get(position).get(1)));
			h.tv_third.setOnClickListener(new MyOnClickListener(courseList.get(
					position).get(2)));
			h.tv_forth.setOnClickListener(new MyOnClickListener(courseList.get(
					position).get(3)));
			h.tv_fifth.setOnClickListener(new MyOnClickListener(courseList.get(
					position).get(4)));
			convertView.setTag(h);
		} else {
			h = (CAWViewHolder) convertView.getTag();
		}
		h.tv_classtime.setText("" + (position + 1));
		h.tv_first.setText(courseList.get(position).get(0).LessonName);
		h.tv_second.setText(courseList.get(position).get(1).LessonName);
		h.tv_third.setText(courseList.get(position).get(2).LessonName);
		h.tv_forth.setText(courseList.get(position).get(3).LessonName);
		h.tv_fifth.setText(courseList.get(position).get(4).LessonName);

		isGoing(courseList.get(position).get(0), h.tv_first);
		isGoing(courseList.get(position).get(1), h.tv_second);
		isGoing(courseList.get(position).get(2), h.tv_third);
		isGoing(courseList.get(position).get(3), h.tv_forth);
		isGoing(courseList.get(position).get(4), h.tv_fifth);
		return convertView;
	}

	private void isGoing(Lesson l, TextView tv) {
		if (!CourseUtils.isLessonStart(context, l)) {
			tv.setTextColor(Color.GRAY);
			tv.setBackgroundResource(R.drawable.item_course_grey);
			if (l.getLessonName() == null || l.getLessonName().equals("")) {
				//tv.setVisibility(View.INVISIBLE);
				tv.setBackgroundColor(context.getResources().getColor(R.color.lucency_white));
			}
		} else {
			tv.setBackgroundResource(R.drawable.item_course_green);
		}
	}

	class CAWViewHolder {
		TextView tv_classtime, tv_first, tv_second, tv_third, tv_forth,
				tv_fifth, tv_sixth, tv_seventh;
	}

	class MyOnClickListener implements View.OnClickListener {
		private Lesson l;

		public MyOnClickListener(Lesson l) {
			this.l = l;
		}

		@Override
		public void onClick(View v) {

			Intent intent = new Intent(context, CourseDetailActivity.class);
			intent.putExtra("week", l.getWeek());
			intent.putExtra("classtime", l.getClasstime());
			int week = l.week - 1;
			int classtime = l.classtime - 1;
			List<Lesson> list = _course.get(week).get(classtime);
			int positon = 0;
			if (list.size() > 1) {
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getLessonName().equals(l.getLessonName())) {
						positon = i;
						break;
					}
				}
			}
			intent.putExtra("position", positon);
			context.startActivity(intent);
		}

	}
}
