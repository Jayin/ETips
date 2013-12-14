package com.meizhuo.etips.model;

import java.io.Serializable;
import java.util.List;

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
