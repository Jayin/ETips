package com.meizhuo.etips.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.meizhuo.etips.common.utils.CourseUtils;

/**
 * 一周课程的实体类<br>
 * add in version 2.1
 * 
 * @author Jayin Ton
 * 
 */
public class Course implements Serializable {
	private List<List<List<Lesson>>> courseList;

	public Course(List<List<List<Lesson>>> course) {
		this.courseList = course;
	}

	/**
	 * 获得星期whichDay(从0开始)的课程列表
	 * 
	 * @param whichDay
	 *            星期几(从0开始)
	 * @return
	 */
	public List<List<Lesson>> getDailyLesson(int whichDay) {
		return this.courseList.get(whichDay);
	}

	/**
	 * 获得星期whichDay(从0开始),第time(从0开始)课室上的课程列表
	 * 
	 * @param whichDay
	 *            星期几 
	 * @param time
	 *            第几节课时 (从0开始)
	 * @return
	 */
	public List<Lesson> getDailyLesson(int whichDay, int time) {
		return getDailyLesson(whichDay).get(time);
	}
    /**
     * 获得一天要上的课程<br>
     * Note That,若某一课室没有课程要上，那一课时为null
     * @param context 
     * @param whichDay 星期几 (start from 0)
     * @return List<Lesson>
     */
	public List<Lesson> getDailyOnGoingLessons(Context context, int whichDay) {
		List<Lesson> result = new ArrayList<Lesson>();
		List<List<Lesson>> cur_lessons = getDailyLesson(whichDay);
		for (int i = 0; i < 5; i++) {
			if (getDailyLesson(whichDay, i).size() > 1) {
				boolean flag = false;
				for (Lesson l : getDailyLesson(whichDay, i)) {
					if (CourseUtils.isLessonStart(context, l)) {
						result.add(l);
						flag = true;
						break;
					}
				}
				if (!flag)
					result.add(null);
			} else {
				if (CourseUtils.isLessonStart(context,
						getDailyLesson(whichDay, i).get(0))) {
					result.add(getDailyLesson(whichDay, i).get(0));
				} else {
					result.add(null);
				}
			}
		}
		return result;
	}

	public List<List<List<Lesson>>> getCourseList() {
		return courseList;
	}

	public void setCourseList(List<List<List<Lesson>>> courseList) {
		this.courseList = courseList;
	}

	@Override
	public String toString() {
		return "Course [courseList=" + courseList.toString() + "]";
	}

}
