package com.meizhuo.etips.model;

/**
 * 成绩对象
 * 
 * @author Jayin Ton
 * 
 */
public class ScoreRecord {
	public String lessonCode = ""; // 课程代码
	public String lessonName = ""; // 课程名称
	public String category = ""; // 类别
	public String lessonScore = ""; // 学分
	public String score = ""; // 成绩
	public String remark = ""; // 备注

	@Override
	public String toString() {
		return "ScoreRecord [category=" + category + ", lessonCode="
				+ lessonCode + ", lessonName=" + lessonName + ", lessonScore="
				+ lessonScore + ", remark=" + remark + ", score=" + score + "]";
	}
}
