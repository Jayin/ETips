package com.meizhuo.etips.model;

/**
 * 一条消息记录
 * 
 * @author Jayin Ton
 * 
 */
public class MsgRecord {
	public MsgRecord() {
		id = 0;
		type = "";
		content = "";
		addTime = "";
	}

	/**
	 * 消息id（数据库中的顺序）
	 */
	public int id;
	/**
	 * 消息类型
	 */
	public String type;
	/**
	 * 消息内容
	 */
	public String content;
	/**
	 * 接收并写入数据库时间
	 */
	public String addTime;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MsgRecord [id=" + id + ", type=" + type + ", content="
				+ content + ", addTime=" + addTime + "]";
	}

}
