package com.meizhuo.etips.model;

/**
 * 书的基本信息
 * 检索的时候用到
 * @author Jayin Ton
 * 
 */
public class BookInfo {
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

	/*
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BookInfo [BookName=" + BookName + ", Authors=" + Authors
				+ ", Press=" + Press + ", PressTime=" + PressTime
				+ ", ExactNumber=" + ExactNumber + ", Totle=" + Totle
				+ ", Left=" + Left + ", BookID=" + BookID + "]";
	}

}
