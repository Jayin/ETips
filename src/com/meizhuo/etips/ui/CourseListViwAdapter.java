package com.meizhuo.etips.ui;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.helper.StringUtil;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.meizhuo.etips.activities.R;
import com.meizhuo.etips.activities.R.color;
import com.meizhuo.etips.common.utils.CourseUtils;
import com.meizhuo.etips.common.utils.ETipsUtils;
import com.meizhuo.etips.common.utils.Elog;
import com.meizhuo.etips.common.utils.StringUtils;
import com.meizhuo.etips.model.Lesson;
import com.meizhuo.etips.ui.CourseSwitchDialog.CourseSwitchDialogCallBack;

/**
 * 单页课程适配器
 * 
 * @author Jayin Ton
 * 
 */
public class CourseListViwAdapter extends BaseAdapter {
	private List<List<Lesson>> course;
	private List<String> ltime;
	private Context context;

	public CourseListViwAdapter(List<List<Lesson>> course, Context context) {
		this.course = course;
		this.context = context;
		ltime = new ArrayList<String>();
		String[] _s = context.getResources().getStringArray(
				R.array.course_time_start);
		for (String s : _s)
			ltime.add(s);
		/**
		 * 08:15-09:50 10:10-11:45 14:45-16:20 16:30-18:05 一大节耗时95分钟 19:30-21:05
		 */
	}

	@Override
	public int getCount() {

		return course.size();
	}

	@Override
	public Object getItem(int position) {

		return course.get(position); // 这里可能会出错
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHodler holder;
		final int _position = position;
		if (convertView == null) {
			holder = new ViewHodler();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_listview_lesson, null);
			holder.tv_itemId = (TextView) convertView
					.findViewById(R.id.item_listview_lesson_textview_num);
			holder.tv_ltime = (TextView) convertView
					.findViewById(R.id.item_listview_lesson_textview_ltime);
			holder.tv_lesson = (TextView) convertView
					.findViewById(R.id.item_listview_lesson_textView_lesson);
			holder.tv_time = (TextView) convertView
					.findViewById(R.id.item_listview_lesson_textView_lesson_time);
			holder.tv_teacher = (TextView) convertView
					.findViewById(R.id.item_listview_lesson_textView_lesson_teacher);
			holder.tv_address = (TextView) convertView
					.findViewById(R.id.item_listview_lesson_textView_lesson_address);
			holder.imageBtn = (ImageButton) convertView
					.findViewById(R.id.item_listview_lesson_imageButton1);
			holder.iv_one = (ImageView) convertView.findViewById(R.id.item_listview_lesson_imageView2);
			holder.iv_two = (ImageView) convertView.findViewById(R.id.item_listview_lesson_imageView3);
			holder.iv_third = (ImageView) convertView.findViewById(R.id.item_listview_lesson_imageView4);
			convertView.setTag(holder);

		} else {
			holder = (ViewHodler) convertView.getTag();
		}

		Lesson l = course.get(position).get(0);
		// 自动切换本周，本课时是否要上的课程，如果都不用上，默认是第一个
		for (Lesson _lesson : course.get(position)) {
			if(CourseUtils.isLessonStart(context, _lesson)){
				l = _lesson;
			}
		}
		holder.tv_itemId.setText(String.valueOf(position + 1));
		holder.tv_ltime.setText(ltime.get(position));
		holder.tv_lesson.setText(l.LessonName);
		holder.tv_time.setText(l.Time);
		holder.tv_teacher.setText(l.Teacher);
		holder.tv_address.setText(l.address);
		isOnGoing(holder, l);
		if (course.get(position).size() > 1) {
			holder.imageBtn.setVisibility(View.VISIBLE);
			holder.imageBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// 改变 课程的顺序 已废弃，since 2.0
					CourseSwitchDialog dialog = new CourseSwitchDialog(context,
							course.get(_position),
							new CourseSwitchDialogCallBack() {
								@Override
								public void SetText(Lesson lesson) {
								}
							});
					dialog.show();
				}
			});
		} else {
			holder.imageBtn.setVisibility(View.INVISIBLE);
		}

		return convertView;
	}

	private void isOnGoing(ViewHodler holder, Lesson lesson) {
		if(lesson==null || lesson.Time ==null || lesson.Time.equals("")){
			holder.iv_one.setVisibility(View.INVISIBLE);
			holder.iv_two.setVisibility(View.INVISIBLE);
			holder.iv_third.setVisibility(View.INVISIBLE);
		}
		if (!CourseUtils.isLessonStart(context, lesson)) {
			holder.tv_itemId.setTextColor(color.grey);
			holder.tv_ltime.setTextColor(color.grey);
			holder.tv_time.setTextColor(color.grey);
			holder.tv_address.setTextColor(color.grey);
			holder.tv_teacher.setTextColor(color.grey);
			holder.tv_address.setTextColor(color.grey);
			holder.tv_lesson.setTextColor(color.grey);
			
		}
	}

	class ViewHodler {
		TextView tv_itemId, tv_ltime, tv_lesson, tv_time, tv_teacher,
				tv_address;
		ImageButton imageBtn;
		ImageView iv_one,iv_two,iv_third;
	}

}
