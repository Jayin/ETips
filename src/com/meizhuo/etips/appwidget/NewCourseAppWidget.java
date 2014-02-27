package com.meizhuo.etips.appwidget;

import java.util.Calendar;
import java.util.List;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import com.meizhuo.etips.activities.CourseMainActivity;
import com.meizhuo.etips.activities.R;
import com.meizhuo.etips.activities.SubSystemLoginActivity;
import com.meizhuo.etips.app.AppInfo;
import com.meizhuo.etips.common.ETipsContants;
import com.meizhuo.etips.common.L;
import com.meizhuo.etips.common.StringUtils;
import com.meizhuo.etips.model.Course;
import com.meizhuo.etips.model.Lesson;
/**
 * 新的课表插件<br>
 * @version 2.2
 * @author Jayin Ton
 *
 */
public class NewCourseAppWidget extends AppWidgetProvider {

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		// 时间改变，课程改变，周数改变均需要更新
		if (action.equals(ETipsContants.Action_CourseChange)
				|| action.equals(ETipsContants.Action_CurrentWeekChange)
				|| action.equals("android.intent.action.TIME_SET")
				|| action.equals("android.intent.action.TIME_TICK")
				|| action.equals("android.intent.action.TIMEZONE_CHANGED")
				|| action.equals("android.appwidget.action.APPWIDGET_UPDATE")) {

			// 计算对应的数据。Calendar的API不想吐槽了 ++！
			int cur_weekday = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 2 == -1 ? 6
					: Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 2;
			// 星期几?
			int weekday = intent.getIntExtra("weekday", cur_weekday);
			String time = "星期 "
					+ StringUtils.getChinese((weekday + 1 == 8 ? 1
							: weekday + 1));
			// 获得当日的要上的课程表
			Course course = AppInfo.getCourse(context);
			List<Lesson> lessons = course == null ? null : course
					.getDailyOnGoingLessons(context, weekday);
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
					R.layout.widget_course_new);
			// 更新UI
			remoteViews.setTextViewText(R.id.tv_weekday, time);
			if (lessons == null) {
				remoteViews.setViewVisibility(R.id.linearlayout_timetable,
						View.GONE);
				remoteViews.setViewVisibility(R.id.tv_notify, View.VISIBLE);
			} else {
				remoteViews.setViewVisibility(R.id.tv_notify, View.GONE);
				remoteViews.setViewVisibility(R.id.linearlayout_timetable,
						View.VISIBLE);
				setText(remoteViews, lessons.get(0), R.id.tv_lessonname_1,
						R.id.tv_address_1);
				setText(remoteViews, lessons.get(1), R.id.tv_lessonname_2,
						R.id.tv_address_2);
				setText(remoteViews, lessons.get(2), R.id.tv_lessonname_3,
						R.id.tv_address_3);
				setText(remoteViews, lessons.get(3), R.id.tv_lessonname_4,
						R.id.tv_address_4);
				setText(remoteViews, lessons.get(4), R.id.tv_lessonname_5,
						R.id.tv_address_5);
				L.i("current intent"+intent.getIntExtra("weekday", 99));
				PendingIntent pre = getPrePendingIntent(context, weekday);
				PendingIntent next = getNextPendingintent(context, weekday);
				remoteViews.setOnClickPendingIntent(R.id.iv_previous, pre);// 前一天
				remoteViews.setOnClickPendingIntent(R.id.iv_next, next);// 下一天
			}
			// 设置点击响应事件
			PendingIntent startCourseMain = getPendingIntent(context);
			remoteViews.setOnClickPendingIntent(R.id.rely_container,
					startCourseMain);

			AppWidgetManager appWidgetManager = AppWidgetManager
					.getInstance(context);
			ComponentName componentName = new ComponentName(context,
					NewCourseAppWidget.class);
			appWidgetManager.updateAppWidget(componentName, remoteViews);
		} else {
			super.onReceive(context, intent);
		}
	}

	private void setText(RemoteViews remoteViews, Lesson l, int nameId,
			int adressId) {
		if (l == null) {
			remoteViews.setViewVisibility(nameId, View.INVISIBLE);
			remoteViews.setViewVisibility(adressId, View.INVISIBLE);
		} else {
			remoteViews.setViewVisibility(nameId, View.VISIBLE);
			remoteViews.setViewVisibility(adressId, View.VISIBLE);
			remoteViews.setTextViewText(nameId, l.getLessonName());
			remoteViews.setTextViewText(adressId, l.getAddress());
		}
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// PendingIntent startCourseMain = getPendingIntent(context);
		for (int i = 0; i < appWidgetIds.length; i++) {
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
					R.layout.widget_course_new);
			appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
		}
		context.sendBroadcast(new Intent(ETipsContants.Action_CourseChange)); // 发送课表修改的广播！
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}
    /** 点击响应事件,打开课表or登录获取课表*/
	private PendingIntent getPendingIntent(Context context) {
		PendingIntent startCourseMain = PendingIntent.getActivity(context, 0,
				new Intent(context, CourseMainActivity.class), 0);
		Course c = AppInfo.getCourse(context);
		if (c == null || c.getCourseList().size() == 0) {
			Intent intent = new Intent(context, SubSystemLoginActivity.class);
			intent.putExtra("toWhere", "CourseMainActivity");
			startCourseMain = PendingIntent.getActivity(context, 0, intent, 0);
		}
		return startCourseMain;
	}

	private PendingIntent getNextPendingintent(Context context, int weekday) {
		Intent intent = new Intent(ETipsContants.Action_CourseChange);
		if (weekday + 1 > 6) {
			intent.putExtra("weekday", 0);
		} else {
			intent.putExtra("weekday", weekday + 1);
		}
		return PendingIntent.getBroadcast(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
	}

	private PendingIntent getPrePendingIntent(Context context, int weekday) {
		Intent intent = new Intent(ETipsContants.Action_CourseChange);
		if (weekday - 1 < 0) {
			intent.putExtra("weekday", 6);
		} else {
			intent.putExtra("weekday", weekday - 1);
		}
		return PendingIntent.getBroadcast(context, 1, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
	}
}
