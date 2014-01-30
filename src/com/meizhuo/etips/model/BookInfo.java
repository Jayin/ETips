package com.meizhuo.etips.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 书的基本信息 检索的时候用到
 * 
 * @author Jayin Ton
 * 
 */
public class BookInfo implements Serializable {
	/**
	 * default version
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * 书名
	 */
	public String BookName;
	/**
	 * 作者
	 */
	public String Authors; // 作者
	/**
	 * 出版社
	 */
	public String Press; // 出版社
	/**
	 * 出版时间
	 */
	public String PressTime; // 出版时间
	/**
	 * 索取编号
	 */
	public String ExactNumber;// 索取编号
	/**
	 * 馆藏总数
	 */
	public String Totle; // 馆藏总数

	/**
	 * 可借数目
	 */
	public String Left; // 可借数目
	/**
	 * 书本在图书馆的编号 唯一
	 */
	public String BookID; // 书本在图书馆的编号
	/**
	 * 收藏情况
	 */
	public List<BookStatus> status = new ArrayList<BookStatus>();

	@Override
	public String toString() {
		return "BookInfo [BookName=" + BookName + ", Authors=" + Authors
				+ ", Press=" + Press + ", PressTime=" + PressTime
				+ ", ExactNumber=" + ExactNumber + ", Totle=" + Totle
				+ ", Left=" + Left + ", BookID=" + BookID + ", status="
				+ status + "]";
	}

	public String getBookName() {
		return BookName;
	}

	public void setBookName(String bookName) {
		BookName = bookName;
	}

	public String getAuthors() {
		return Authors;
	}

	public void setAuthors(String authors) {
		Authors = authors;
	}

	public String getPress() {
		return Press;
	}

	public void setPress(String press) {
		Press = press;
	}

	public String getPressTime() {
		return PressTime;
	}

	public void setPressTime(String pressTime) {
		PressTime = pressTime;
	}

	public String getExactNumber() {
		return ExactNumber;
	}

	public void setExactNumber(String exactNumber) {
		ExactNumber = exactNumber;
	}

	public String getTotle() {
		return Totle;
	}

	public void setTotle(String totle) {
		Totle = totle;
	}

	public String getLeft() {
		return Left;
	}

	public void setLeft(String left) {
		Left = left;
	}

	public String getBookID() {
		return BookID;
	}

	public void setBookID(String bookID) {
		BookID = bookID;
	}

	public List<BookStatus> getStatus() {
		return status;
	}

	public void setStatus(List<BookStatus> status) {
		this.status = status;
	}
}
