package com.meizhuo.etips.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询课程实体类
 * 
 * @author Jayin Ton
 * 
 */
public class CourseQueryData {

	public CourseQueryData() {

	}

	/**
	 * 课程代号
	 */
	private String id;

	/**
	 * 课程名
	 */
	private String name;
	/**
	 * 课类
	 */
	private String kind;
	/**
	 * 学分
	 */
	private String xuefen;

	/**
	 * 上课班级
	 */
	private String classes;
	/**
	 * 上课时间
	 */
	private String time;
	/**
	 * 上课地点
	 */
	private String address;
	/**
	 *任教教师
	 */
	private String teacher;

	@Override
	public String toString() {
		return "CourseQueryData [address=" + address + ", classes=" + classes
				+ ", id=" + id + ", kind=" + kind + ", name=" + name
				+ ", teacher=" + teacher + ", time=" + time + ", xuefen="
				+ xuefen + "]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getXuefen() {
		return xuefen;
	}

	public void setXuefen(String xuefen) {
		this.xuefen = xuefen;
	}

	public String getClasses() {
		return classes;
	}

	public void setClasses(String classes) {
		this.classes = classes;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTeacher() {
		return teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}

}
