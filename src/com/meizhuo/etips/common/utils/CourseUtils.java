package com.meizhuo.etips.common.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;

import com.meizhuo.etips.activities.R;
import com.meizhuo.etips.model.Lesson;

/**
 * 课程管理/工具类
 * 
 * @author Jayin Ton
 * 
 */
public class CourseUtils {
	/**
	 * 根据给出的时间，获取当前在那一节课时 00:00-8:14 int[0] = 0 ;int [1]=0 08:15-09:50 int[0] =
	 * 0 ;int [1]=1 -> Note That 09:50-10:10 int[0] = 0 ;int [1]=1 -> Note That
	 * int[1] with 10:10-11:45 int[0] = 0 ;int [1]=2 11:45-14:45 int[0] = 0 ;int
	 * [1]=2
	 * 
	 * @param context
	 * @param milliseconds
	 *            当前时间 System.currentTimeMillis()
	 * @return int[0] -> value = 0 :处于非上课时间段 ... value > 0 :处于上课时间段,意义:星期x
	 *         int[1] -> value:处于第几节课时; 开始时间段为00:00-8:14(第0节课时);
	 * 
	 */
	public static int[] getCourseStatus(Context context, long milliseconds) {
		int[] result = new int[] { 0, 0 };
		Calendar current = Calendar.getInstance();
		current.setTimeInMillis(milliseconds);
		
	//	System.out.println("getCourseStatus-->"+milliseconds);
		List<TimeLine> timeList = new ArrayList<TimeLine>();
		String[] __start = context.getResources().getStringArray(
				R.array.course_time_start);
		String[] __end = context.getResources().getStringArray(
				R.array.course_time_end);
		timeList.add(new TimeLine(creatCalendar("00:00"),
				creatCalendar("08:14")));
		for (int i = 0; i < __start.length; i++) {
			Calendar start = CourseUtils.creatCalendar(__start[i]);
			Calendar end = CourseUtils.creatCalendar(__end[i]);
			TimeLine tl = new TimeLine(start, end);
			timeList.add(tl);
			if (i + 1 < __start.length)
				timeList.add(new TimeLine(creatCalendar(__end[i]),
						creatCalendar(__start[i + 1])));
		}
		timeList.add(new TimeLine(creatCalendar("21:06"),
				creatCalendar("23:59")));
 
		//Calendar current = Calendar.getInstance();
		for (int i = 0; i < timeList.size(); i++) {
			Calendar start = timeList.get(i).getStart();
			Calendar end = timeList.get(i).getEnd();
			if ((CalendarManager.isInSameTime(current, start) || current
					.after(start))
					&& (CalendarManager.isInSameTime(current, end) || current
							.before(end))) {
				if (i % 2 == 0) { // 处于非上课时间段
					result[0] = 0; // value = 0 :处于非上课时间段 .value > 0 :处于上课时间段
									// ,意义星期x
					result[1] = i / 2; // value:处于第几节课时;
										// 开始时间段为00:00-8:14(第0节课时);
				} else { // 处于上课时间段
					result[0] = current.get(Calendar.DAY_OF_WEEK) - 1;
					if (result[0] == 0)
						result[0] = 7;
					result[1] = (i + 1) / 2;
				}
				return result;
			}
		}
		return result;

	}

	/**
	 * 根据时间 创建Calendar
	 * 
	 * @param time
	 *            格式为"hh:mm" hh为24小时格式
	 * @return
	 */
	public static Calendar creatCalendar(String time) {
		String[] ms = time.split(":");
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(ms[0]));
		c.set(Calendar.MINUTE, Integer.parseInt(ms[1]));
		return c;
	}
    /**
     * 判断当前这节课程是否要上！ 本周
     * @param context
     * @param l
     * @return
     */
	public static boolean isLessonStart(Context context, Lesson l) {
		SharedPreferences sp = context.getSharedPreferences(
				ETipsContants.SharedPreference_NAME, Context.MODE_PRIVATE);
		Calendar c = CalendarManager.getCalendar();
		int currentWeek = ETipsUtils.getCurrentWeek(context);
		Set<Integer> set = null;
		if (l.Time != null && !l.Time.equals(""))
			set = StringUtils.parseTimeOfLesson(l.Time);
		if (set != null && set.contains(currentWeek))
			return true;
		return false;
	}

}

/**
 * 时间段
 * 
 * @author Jayin Ton
 * 
 */
class TimeLine {
	private Calendar start, end;

	public Calendar getStart() {
		return start;
	}

	public void setStart(Calendar start) {
		this.start = start;
	}

	public Calendar getEnd() {
		return end;
	}

	public void setEnd(Calendar end) {
		this.end = end;
	}

	public TimeLine(Calendar start, Calendar end) {
		this.start = start;
		this.end = end;
	}

	@Override
	public String toString() {
		return "TimeLine [start=" + start.get(Calendar.HOUR_OF_DAY) + ":"
				+ start.get(Calendar.MINUTE) + ", end="
				+ end.get(Calendar.HOUR_OF_DAY) + ":"
				+ end.get(Calendar.MINUTE) + "]";
	}
}
