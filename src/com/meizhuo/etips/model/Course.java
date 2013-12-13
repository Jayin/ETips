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

	public Course(List<List<List<Lesson>>> course) {
		this.course = course;
	}

	private List<List<List<Lesson>>> course;

	public List<List<List<Lesson>>> getCourse() {
		return course;
	}

	public void setCourse(List<List<List<Lesson>>> course) {
		this.course = course;
	}

	@Override
	public String toString() {
		return "Course [course=" + course.toString() + "]";
	}
}
