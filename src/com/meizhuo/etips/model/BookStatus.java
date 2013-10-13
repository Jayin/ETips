package com.meizhuo.etips.model;

import java.io.Serializable;

/**
 * 查阅一本书的详情
 * 
 * @see http://lib.wyu.edu.cn/opac/bookinfo.aspx?ctrlno=524565
 * @author Jayin Ton
 * 
 */
public class BookStatus implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 馆藏地
	 */
	public String location;
	/**
	 * 特藏码/排架码
	 */
	public String collectionCode;
	/**
	 * 索取号
	 */
	public String askNum;
	/**
	 * 登录号
	 */
	public String loginNum;
	/**
	 * 卷期
	 */
	public String version;
	/**
	 * 年代
	 */
	public String age;
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCollectionCode() {
		return collectionCode;
	}

	public void setCollectionCode(String collectionCode) {
		this.collectionCode = collectionCode;
	}

	public String getAskNum() {
		return askNum;
	}

	public void setAskNum(String askNum) {
		this.askNum = askNum;
	}

	public String getLoginNum() {
		return loginNum;
	}

	public void setLoginNum(String loginNum) {
		this.loginNum = loginNum;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBorrowKind() {
		return borrowKind;
	}

	public void setBorrowKind(String borrowKind) {
		this.borrowKind = borrowKind;
	}

	/**
	 * 状态
	 */
	public String status;
	/**
	 * 借阅类型
	 */
	public String borrowKind;

	@Override
	public String toString() {
		return "BookStatus [age=" + age + ", askNum=" + askNum
				+ ", borrowkind=" + borrowKind + ", collectionCode="
				+ collectionCode + ", location=" + location + ", loginNum="
				+ loginNum + ", status=" + status + ", version=" + version
				+ "]";
	}

}
