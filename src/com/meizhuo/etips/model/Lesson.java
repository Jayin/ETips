package com.meizhuo.etips.model;

import java.io.Serializable;
import java.util.List;

/**
 * Lesson 一课程对象 如果要增加更强大的功能 请继承这类 ，建议命名为BasicLesson
 * 
 * @author Jayin Ton
 * 
 */
@SuppressWarnings("serial")
public class Lesson implements Serializable{
	public String LessonName = null;
	public String Teacher = null;
	public String Time = null;
	public String address = null;
	public int classtime = 0; // 第几节
	public int week = 0; // 星期几

	/**
	 * 默认的构造方法
	 */
	public Lesson() {
		LessonName = "";
		Teacher = "";
		Time = "";
		address = "";
		classtime = 0;
		week = 0;
	}

	@Override
	public String toString() {
		return "Lesson [LessonName=" + LessonName + ", Teacher=" + Teacher
				+ ", Time=" + Time + ", address=" + address + ", classtime="
				+ classtime + ", week=" + week + "]";
	}



	public String getLessonName() {
		return LessonName;
	}

	public void setLessonName(String lessonName) {
		LessonName = lessonName;
	}

	public String getTeacher() {
		return Teacher;
	}

	public void setTeacher(String teacher) {
		Teacher = teacher;
	}

	public String getTime() {
		return Time;
	}

	public void setTime(String time) {
		Time = time;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getClasstime() {
		return classtime;
	}

	public void setClasstime(int classtime) {
		this.classtime = classtime;
	}

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}

}
