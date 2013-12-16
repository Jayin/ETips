package com.meizhuo.etips.appwidget;

import java.util.Calendar;
import java.util.List;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.meizhuo.etips.activities.CourseMainActivity;
import com.meizhuo.etips.activities.R;
import com.meizhuo.etips.activities.SubSystemLoginActivity;
import com.meizhuo.etips.common.utils.CourseUtils;
import com.meizhuo.etips.common.utils.DataPool;
import com.meizhuo.etips.common.utils.ETipsContants;
import com.meizhuo.etips.common.utils.ETipsUtils;
import com.meizhuo.etips.common.utils.StringUtils;
import com.meizhuo.etips.model.Course;
import com.meizhuo.etips.model.Lesson;

/**
 * 课表桌面小部件
 * 
 * @author Jayin Ton
 * 
 */
public class CourseAppWidget extends AppWidgetProvider {

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {

		super.onDeleted(context, appWidgetIds);
	}

	@Override
	public void onDisabled(Context context) {

		super.onDisabled(context);
	}

	@Override
	public void onEnabled(Context context) {

		super.onEnabled(context);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		// 时间改变，课程改变，周数改变均需要更新
		if (action.equals(ETipsContants.Action_CourseChange)
				|| action.equals(ETipsContants.Action_CurrentWeekChange)
				|| action.equals("android.intent.action.TIME_SET")
				|| action.equals("android.intent.action.TIME_TICK")
				|| action.equals("android.intent.action.TIMEZONE_CHANGED")) {

			String time = StringUtils.getTimeFormat() + " 第"
					+ ETipsUtils.getCurrentWeek(context) + "周";
			String courseInfo = wrapData(context);
			PendingIntent startCourseMain = getPendingIntent(context);  //刷新点击事件
			
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
					R.layout.widget_course);
			remoteViews.setTextViewText(R.id.widget_course_time, time);
			remoteViews.setTextViewText(R.id.widget_course_content, courseInfo);
			remoteViews.setOnClickPendingIntent(R.id.widget_course_main,
					startCourseMain);

			AppWidgetManager appWidgetManager = AppWidgetManager
					.getInstance(context);
			ComponentName componentName = new ComponentName(context,
					CourseAppWidget.class);
			appWidgetManager.updateAppWidget(componentName, remoteViews);

		} else {

			super.onReceive(context, intent);
		}

	}

	public String wrapData(Context context) {
		String[] lessonName = new String[5];
		StringBuilder sb = new StringBuilder("全日无课！");
		boolean flag = true; // 用来判断是否全天无课,true 为是
		DataPool dp  = new DataPool(ETipsContants.SP_NAME_Course, context);
		Course course = (Course)dp.get("course");
		if(course ==null )return "尚未导入课程";
		List<List<List<Lesson>>> lessonList = course.getCourseList();
		int week = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 1 ? 7
				: Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
		int maxLength = 0;
		if (lessonList != null || lessonList.size() > 0) {
			List<List<Lesson>> todayLesson = lessonList.get(week - 1);
			for (int i = 0; i < 5; i++) {
				if (todayLesson.get(i).size() > 1) {
					List<Lesson> t = todayLesson.get(i);
					for (int j = 0; j < t.size(); j++) {
						if (CourseUtils.isLessonStart(context, t.get(j))) {
							flag = false;
							lessonName[i] = t.get(j).getLessonName();
							break;
						} else {
							lessonName[i] = "无课";
						}
					}
				} else {
					System.out.println(todayLesson.get(i)
					.get(0).toString());
					if (CourseUtils.isLessonStart(context, todayLesson.get(i)
							.get(0))) {
						flag = false;
						lessonName[i] = todayLesson.get(i).get(0)
								.getLessonName();
					} else {
						lessonName[i] = "无课";
					}
				}
				if (lessonName[i].length() > maxLength)
					maxLength = lessonName[i].length();
			}
			if (flag)
				return sb.toString();
			sb = new StringBuilder();
			for (int i = 0; i < 5; i++) {
				sb.append((i + 1) + ".");
				if (maxLength > lessonName[i].length()) {
					sb.append(scalaLength(lessonName[i], maxLength));
				} else {
					sb.append(lessonName[i]);
				}
				sb.append("\n");
			}
		}
		return sb.toString();
	}

	// 添加空格
	public String scalaLength(String string, int maxLength) {
		StringBuilder _sb = new StringBuilder(string);
		while (_sb.length() < maxLength) {
			_sb.append("  ");
		}
		return _sb.toString();
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		PendingIntent startCourseMain = getPendingIntent(context);
		for (int i = 0; i < appWidgetIds.length; i++) {
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
					R.layout.widget_course);
			remoteViews.setOnClickPendingIntent(R.id.widget_course_main,
					startCourseMain);
			appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
		}
		context.sendBroadcast(new Intent(ETipsContants.Action_CourseChange)); // 发送课表修改的广播！
		super.onUpdate(context, appWidgetManager, appWidgetIds);

	}
	
	private PendingIntent getPendingIntent(Context context){
		PendingIntent startCourseMain = PendingIntent.getActivity(context, 0,
				new Intent(context, CourseMainActivity.class), 0);
        DataPool dp = new DataPool(ETipsContants.SP_NAME_Course, context);
        Course c =(Course) dp.get("course");
        if(c==null || c.getCourseList().size()==0){
        	Intent intent = new Intent(context,
					SubSystemLoginActivity.class);
			intent.putExtra("toWhere", "CourseMainActivity");
			startCourseMain = PendingIntent.getActivity(context, 0,intent , 0);
        }
        return startCourseMain;
	}

}
