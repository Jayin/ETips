package com.meizhuo.etips.model;

import java.util.List;

/**
 * Lesson 一课程对象 如果要增加更强大的功能 请继承这类 ，建议命名为BasicLesson
 * 
 * @author Jayin Ton
 * 
 */
public class Lesson {

	public String LessonName = null;
	public String Teacher = null;
	public String Time = null;
	public String address = null;
    public int classtime = 0;
    public int week = 0;

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

	 

}
