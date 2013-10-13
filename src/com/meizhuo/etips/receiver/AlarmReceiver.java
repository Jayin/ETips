package com.meizhuo.etips.receiver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import com.meizhuo.etips.activities.ETipsMainActivity;
import com.meizhuo.etips.activities.ETipsStartActivity;
import com.meizhuo.etips.common.utils.CalendarManager;
import com.meizhuo.etips.common.utils.ETipsContants;
import com.meizhuo.etips.common.utils.StringUtils;
import com.meizhuo.etips.db.CourseDAO;
import com.meizhuo.etips.model.Lesson;
import com.meizhuo.etips.ui.utils.BaseNotificationCompat;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class AlarmReceiver extends BroadcastReceiver {
	private Context context;

	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
		System.out.println("收到！！！！");
		// 通知用户改天有多少节课需要上（弹出Dialog样式的Activity）。。。然后继续设置闹钟。。。
		SharedPreferences sp = context.getSharedPreferences(
				ETipsContants.SharedPreference_NAME, Context.MODE_APPEND);
		if (sp.getString("Is_Open_Daily_Course_Alarm", "YES").equals("YES")) {
			PendingIntent operation = PendingIntent.getBroadcast(context,
					ETipsContants.ID_Alarm_Course, new Intent(
							ETipsContants.ACTION_Custom_Alarm),
					PendingIntent.FLAG_UPDATE_CURRENT);
			AlarmManager am = CalendarManager.getAlarmManager(context);
			Calendar c = CalendarManager.getCourseAlarmDaily();
			am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis() + 24 * 60 * 60
					* 1000, operation);
			showNotification();
		}
		
	}

	private void showNotification() {
		CourseDAO dao = new CourseDAO(context);
		Calendar c = CalendarManager.getCalendar();
		String nextDay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
		List<List<Lesson>> course = dao.getLessonList("week = ?",
				new String[] { nextDay });// 明日课程表
		List<Lesson> Lessons = new ArrayList<Lesson>(); // 明日要上的课程
		SharedPreferences sp = context.getSharedPreferences(
				ETipsContants.SharedPreference_NAME, Context.MODE_PRIVATE);
		int startWeek = Integer.parseInt(sp.getString("Current_Week", String.valueOf(c.get(Calendar.WEEK_OF_YEAR))));
		int currentWeek = c.get(Calendar.WEEK_OF_YEAR) - startWeek + 1;
		for (List<Lesson> l : course) {
			for (Lesson ml : l) {
				Set<Integer> set = null;
				if (ml.Time != null && !ml.Time.equals(""))
					set = StringUtils.parseTimeOfLesson(ml.Time);
				if (set != null && set.contains(currentWeek))
					Lessons.add(ml);
			}
		}
		BaseNotificationCompat build = (BaseNotificationCompat) BaseNotificationCompat
				.getInstance(context);
		build.setContentTitle("课程预告");
		build.setContentText("明日你有" + Lessons.size() + "节课要上");
		build.setContentIntent(BaseNotificationCompat.getContentIntent(context,
				ETipsStartActivity.class));
		build.setID(ETipsContants.ID_Alarm_Course);
		build.show();
	}
}
