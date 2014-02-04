package com.meizhuo.etips.common;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import android.content.Context;
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
