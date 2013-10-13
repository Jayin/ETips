package com.meizhuo.etips.model;

/**
 * 查阅一本书的详情
 * 
 * @see http://lib.wyu.edu.cn/opac/bookinfo.aspx?ctrlno=524565
 * @author Jayin Ton
 * 
 */
public class BookStatus {
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
