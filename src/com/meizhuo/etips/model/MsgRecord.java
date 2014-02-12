package com.meizhuo.etips.model;

import java.io.Serializable;

/**
 * 一条消息记录
 * 
 * @author Jayin Ton
 * 
 */
@SuppressWarnings("serial")
public class MsgRecord implements Serializable{
	public MsgRecord() {
		id = 0;
		type = "";
		content = "";
		addTime = "";
		from = "";
		to = "";
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
	/** 来自*/
	private String from;
	/** to id*/
	private String to;

 
	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
	
	

}
