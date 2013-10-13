package com.meizhuo.etips.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.meizhuo.etips.common.utils.ETipsContants;
import com.meizhuo.etips.common.utils.StringUtils;
import com.meizhuo.etips.db.CourseDAO;
import com.meizhuo.etips.model.Lesson;

public class CourseDetailActivity extends BaseUIActivity {
	private ETipsApplication App;
	private Lesson lesson;
	private EditText et_lesson, et_time, et_address, et_teacher;
	private TextView tv_lesson, tv_time, tv_address, tv_teacher, tv_title,
			tv_tips;
	private Button backBtn, settingBtn, okBtn, cancleBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_course_detail);
		initData();
		initLayout();
	}

	@Override
	protected void initLayout() {
		et_lesson = (EditText) this
				.findViewById(R.id.acty_course_detail_lesson_et);
		et_time = (EditText) this.findViewById(R.id.acty_course_detail_time_et);
		et_address = (EditText) this
				.findViewById(R.id.acty_course_detail_address_et);
		et_teacher = (EditText) this
				.findViewById(R.id.acty_course_detail_teacher_et);

		tv_lesson = (TextView) this
				.findViewById(R.id.acty_course_detail_lesson);
		tv_time = (TextView) this.findViewById(R.id.acty_course_detail_time);
		tv_address = (TextView) this
				.findViewById(R.id.acty_course_detail_address);
		tv_teacher = (TextView) this
				.findViewById(R.id.acty_course_detail_teacher);

		tv_title = (TextView) this.findViewById(R.id.acty_course_detail_tv);
		tv_tips = (TextView) this.findViewById(R.id.acty_course_detail_tv_tips);

		backBtn = (Button) this.findViewById(R.id.acty_course_detail_back);
		settingBtn = (Button) this
				.findViewById(R.id.acty_course_detail_setting);
		okBtn = (Button) this.findViewById(R.id.acty_course_detail_ok);
		cancleBtn = (Button) this.findViewById(R.id.acty_course_detail_cancle);

		et_lesson.setText(lesson.LessonName);
		et_time.setText(lesson.Time);
		et_address.setText(lesson.address);
		et_teacher.setText(lesson.Teacher);

		tv_lesson.setText(lesson.LessonName);
		tv_time.setText(lesson.Time);
		tv_address.setText(lesson.address);
		tv_teacher.setText(lesson.Teacher);

		backBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				CourseDetailActivity.this.finish();

			}
		});
		settingBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				change2edit();

			}
		});
		cancleBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				change2nomal();
				tv_lesson.setText(lesson.LessonName);
				tv_time.setText(lesson.Time);
				tv_address.setText(lesson.address);
				tv_teacher.setText(lesson.Teacher);
			}
		});
		okBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				change2nomal();

				final Handler handler = new Handler() {
					@Override
					public void handleMessage(Message msg) {
						if (msg.what == ETipsContants.Finish) {
							tv_lesson.setText(et_lesson.getText().toString()
									.trim());
							tv_address.setText(et_address.getText().toString()
									.trim());
							tv_teacher.setText(et_teacher.getText().toString()
									.trim());
							tv_time.setText(et_time.getText().toString().trim());
							Toast.makeText(CourseDetailActivity.this, "编辑成功！",
									Toast.LENGTH_SHORT).show();
							setResult(RESULT_OK, getIntent());
						}

						else {
							Toast.makeText(CourseDetailActivity.this,
									(String) msg.obj, Toast.LENGTH_SHORT)
									.show();
							setResult(RESULT_FIRST_USER, getIntent());
						}
					}
				};
				new Thread(new Runnable() {

					@Override
					public void run() {
						Message msg = handler.obtainMessage();
						if (!StringUtils.isEditVaild(et_time.getText()
								.toString())) {
							if (!(et_lesson.getText().toString().trim()
									.equals("")
									&& et_time.getText().toString().trim()
											.equals("")
									&& et_address.getText().toString().trim()
											.equals("") && et_teacher.getText()
									.toString().trim().equals(""))) {
								msg.obj = "编辑失败,输入上课周数错误！";
								msg.what = ETipsContants.Fail;
								handler.sendMessage(msg);
								return;
							}

						}
						CourseDAO dao = new CourseDAO(CourseDetailActivity.this);
						ContentValues cv = new ContentValues();
						cv.put("lessonName", et_lesson.getText().toString()
								.trim());
						cv.put("time", et_time.getText().toString().trim());
						cv.put("address", et_address.getText().toString()
								.trim());
						cv.put("teacher", et_teacher.getText().toString()
								.trim());
						cv.put("week", lesson.week);
						cv.put("classtime", lesson.classtime);
						initData();
						String where = "lessonName = ? and time = ? and address = ? and teacher = ? and week = ? and classtime = ? ";
						String[] whereArgs = new String[] { lesson.LessonName,
								lesson.Time, lesson.address, lesson.Teacher,
								String.valueOf(lesson.week),
								String.valueOf(lesson.classtime) };
						if (dao.update(cv, where, whereArgs)) {
							// Application 从新获取 lessonlist
							List<List<List<Lesson>>> course = new ArrayList<List<List<Lesson>>>();
							for (int i = 1; i <= 7; i++) {

								course.add(dao.getLessonList("week = ?",
										new String[] { String.valueOf(i) }));
							}
							App.setLessonList(course);
							msg.what = ETipsContants.Finish;

						} else {
							msg.obj = "编辑失败,请重试！";
							msg.what = ETipsContants.Fail;

						}
						handler.sendMessage(msg);
					}
				}).start();
			}
		});
	}

	@Override
	protected void initData() {
		int week = getIntent().getIntExtra("week", 1);
		int classtime = getIntent().getIntExtra("classtime", 1);
		App = (ETipsApplication) getApplication();
		lesson = App.getLessonList().get(week - 1).get(classtime - 1).get(0);
	}

	private void change2edit() {
		tv_title.setText("编辑课程");

		tv_lesson.setVisibility(View.GONE);
		tv_time.setVisibility(View.GONE);
		tv_address.setVisibility(View.GONE);
		tv_teacher.setVisibility(View.GONE);

		tv_tips.setVisibility(View.VISIBLE);
		et_lesson.setVisibility(View.VISIBLE);
		et_time.setVisibility(View.VISIBLE);
		et_address.setVisibility(View.VISIBLE);
		et_teacher.setVisibility(View.VISIBLE);

		settingBtn.setVisibility(View.INVISIBLE);
		backBtn.setVisibility(View.INVISIBLE);
		okBtn.setVisibility(View.VISIBLE);
		cancleBtn.setVisibility(View.VISIBLE);

		et_address.setText(tv_address.getText().toString());
		et_lesson.setText(tv_lesson.getText().toString());
		et_teacher.setText(tv_teacher.getText().toString());
		et_time.setText(tv_time.getText().toString());
	}

	private void change2nomal() {
		tv_title.setText("课程详情");
		tv_lesson.setVisibility(View.VISIBLE);
		tv_time.setVisibility(View.VISIBLE);
		tv_address.setVisibility(View.VISIBLE);
		tv_teacher.setVisibility(View.VISIBLE);

		tv_tips.setVisibility(View.GONE);
		et_lesson.setVisibility(View.GONE);
		et_time.setVisibility(View.GONE);
		et_address.setVisibility(View.GONE);
		et_teacher.setVisibility(View.GONE);

		settingBtn.setVisibility(View.VISIBLE);
		backBtn.setVisibility(View.VISIBLE);
		okBtn.setVisibility(View.INVISIBLE);
		cancleBtn.setVisibility(View.INVISIBLE);
	}

}
