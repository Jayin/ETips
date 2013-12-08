package com.meizhuo.etips.model;

/**
 * 一条借阅书本的记录
 * 
 * @author Jayin Ton
 * 
 */

public class BookBorrowRecord {
	/**
	 * 借阅状态。（正常、续满、超期）
	 */
	public String borrowStatus = "";
	/**
	 * 最迟应还期
	 */
	public String latestReturn = "";

	/**
	 * 题名／著者
	 */
	public String bookName = "";
	/**
	 * 图书类型
	 */
	public String category = "";
	/**
	 * 登录号
	 */
	public String loginNumber = "";
	/**
	 * 借书日期
	 */
	public String borrowTime = "";
	/**
	 * 书的唯一编号.可以用于构建URL来查询该书的信息
	 */
	public String bookId = "";

	/*
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BookBorrowRecord [borrowStatus=" + borrowStatus
				+ ", latestReturn=" + latestReturn + ", bookName=" + bookName
				+ ", category=" + category + ", loginNumber=" + loginNumber
				+ ", borrowTime=" + borrowTime + ", bookId=" + bookId + "]";
	}

}
