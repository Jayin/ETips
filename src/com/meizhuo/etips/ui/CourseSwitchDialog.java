package com.meizhuo.etips.ui;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.meizhuo.etips.activities.CourseDetailActivity;
import com.meizhuo.etips.activities.CourseMainActivity;
import com.meizhuo.etips.activities.ETipsApplication;
import com.meizhuo.etips.activities.R;
import com.meizhuo.etips.common.utils.Elog;
import com.meizhuo.etips.db.CourseDAO;
import com.meizhuo.etips.model.Lesson;
import com.meizhuo.etips.ui.utils.BaseDialog;

public class CourseSwitchDialog extends BaseDialog {
	private ListView lv;
	private List<Lesson> list;
	private Context context;
	private ETipsApplication App;
	private CourseSwitchDialogCallBack callback;

	public CourseSwitchDialog(Context context, List<Lesson> list,
			CourseSwitchDialogCallBack callback) {
		super(context);
		this.context = context;
		this.callback = callback;
		this.list = list;
		this.setCancelable(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_course_switch);
		lv = (ListView) this.findViewById(R.id.dialog_course_switch_listview);
		lv.setAdapter(new CourseSwitchAdapter());
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				if (position == 0) {
//					CourseSwitchDialog.this.dismiss();
//					return;
//				}
//				CourseDAO dao = new CourseDAO(context); // A -> B 交换A版位置 C是temp
//				ContentValues a = CourseDAO.getContentValuesByLesson(list
//						.get(0));
//				ContentValues b = CourseDAO.getContentValuesByLesson(list
//						.get(position));
//				if (dao.update(CourseDAO.getUniqueContentValues(),
//						CourseDAO.getWhere(),
//						CourseDAO.getwhereArgsByLesson(list.get(0)))) {// C覆盖A的位置
//					if (dao.update(a, CourseDAO.getWhere(),
//							CourseDAO.getwhereArgsByLesson(list.get(position)))) {
//						// A覆盖B的位置
//						if (dao.update(b, CourseDAO.getWhere(),
//								CourseDAO.getwhereArgs())) {
//							Lesson l = list.get(0);
//							list.set(0, list.get(position));
//							list.set(position, l);
//							CourseSwitchDialog.this.dismiss();
//							callback.SetText(list.get(0));
//						}
//					}
//				}
				Intent intent = new Intent(context,
						CourseDetailActivity.class);
				intent.putExtra("week", list.get(position).week);
				intent.putExtra("classtime", list.get(position).classtime); // classtime;
				intent.putExtra("position", position);
				context.startActivity(intent);
				CourseSwitchDialog.this.dismiss();
				callback.SetText(list.get(position));
			}
		});
	}

	class CourseSwitchAdapter extends BaseAdapter {

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
			ViewHodler holder;
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
				holder.imageView = (ImageView) convertView
						.findViewById(R.id.item_listview_lesson_imageview_right);
				convertView.setTag(holder);
			} else {
				holder = (ViewHodler) convertView.getTag();
			}
			holder.tv_itemId.setText("");
			holder.tv_ltime.setText("");
			Lesson l = list.get(position);
			holder.tv_lesson.setText(l.LessonName);
			holder.tv_time.setText(l.Time);
			holder.tv_teacher.setText(l.Teacher);
			holder.tv_address.setText(l.address);
			if (position == 0) {

				holder.imageView.setImageResource(R.drawable.ic_dialog_check);
			} else {
				holder.imageView.setVisibility(View.INVISIBLE);
			}
			return convertView;
		}

	}

	class ViewHodler {
		TextView tv_itemId, tv_ltime, tv_lesson, tv_time, tv_teacher,
				tv_address;
		ImageButton imageBtn;
		ImageView imageView;
	}

	public interface CourseSwitchDialogCallBack {
		public void SetText(Lesson lesson);
	}
}
