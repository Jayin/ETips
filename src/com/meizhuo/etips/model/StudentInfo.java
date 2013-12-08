package com.meizhuo.etips.model;
/**
 * 学生基本信息 
 * @author Jayin Ton
 *  
 */
public class StudentInfo {
	/**
	 *  学号
	 */
    public String id = "";            //学号
    /**
	 *  姓名
	 */
    public String neme = "";          //姓名
    /**
	 *  曾用名
	 */
    public String nickName = "";      //曾用名
    /**
	 *  性别
	 */
    public String sex="";             //性别
    /**
	 *  生日期
	 */
    public String birthday="";        //出生日期
    /**
	 *   民族
	 */
    public String nation="";          //民族
    /**
	 *  籍贯
	 */
    public String homeTown="";        //籍贯
    /**
	 * 政治面貌
	 */
    public String political="";       //政治面貌
    /**
	 * 专业
	 */
    public String major = "";         //专业
    /**
	 *  所在班级
	 */
    public String classNumber="";     //所在班级
    /**
	 *  所在学院
	 */
    public String department="";      //所在学院
    /**
	 * 所在的系
	 */
    public String system="";          //所在的系
    /**
	 * 学制
	 */
    public String edu_system="";      //学制
    /**
	 *  入学时间
	 */
    public String enter_time="";      //入学时间
    /**
	 *  学籍状态
	 */
    public String edu_status="";      //学籍状态
    /**
	 * 身份证
	 */
    public String identify="";        //身份证
    /**
	 * 家庭住址
	 */
    public String address="";         //家庭住址
    /**
	 *  邮政编码
	 */
    public String postCode="";        //邮政编码
    /**
	 *  联系电话
	 */
    public String contact="";         //联系电话
	 
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("id:").append(id).append(" ");
		sb.append("neme:").append(neme).append(" ");
		sb.append("nickName:").append(nickName).append(" ");
		sb.append("sex:").append(sex).append(" ");
		sb.append("birthday:").append(birthday).append(" ");
		sb.append("nation:").append(nation).append(" ");
		sb.append("political:").append(political).append(" ");
		sb.append("major:").append(major).append(" ");
		sb.append("classNumber:").append(classNumber).append(" ");
		sb.append("department:").append(department).append(" ");
		sb.append("system:").append(system).append(" ");
		sb.append("edu_system:").append(edu_system).append(" ");
		sb.append("enter_time:").append(enter_time).append(" ");
		sb.append("edu_status:").append(edu_status).append(" ");
		sb.append("identify:").append(identify).append(" ");
		sb.append("address:").append(address).append(" ");
		sb.append("postCode:").append(postCode).append(" ");
		sb.append("contact:").append(contact).append(" ");
		return  sb.toString();
	}
 
}
 