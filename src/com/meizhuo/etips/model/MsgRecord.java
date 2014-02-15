package com.meizhuo.etips.model;

import java.io.Serializable;

/**
 * 一条消息记录
 * 
 * @author Jayin Ton
 * 
 */
@SuppressWarnings("serial")
public class MsgRecord implements Serializable {
	public MsgRecord() {
		id = 0;
		type = "";
		content = "";
		addTime = "";
		author = "";
		to_comment_id = "";
		topic_id = "";
		article_id = "";
		incognito = 0;
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
	// 下面的都是为了支持新版帖子回复提醒
	/** 消息的作者id */
	private String author;
	/** 评论idid */
	private String to_comment_id;
	/** 话题id */
	private String topic_id;
	/** 推文id */
	private String article_id;
	/** 推文作者名称 */
	private String nickname;
	/** 是否匿名 */
	private int incognito;

	public void setIncognito(int incognito) {
		this.incognito = incognito;
	}

	public boolean isIncognito() {
		return incognito==1;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getTopic_id() {
		return topic_id;
	}

	public void setTopic_id(String topic_id) {
		this.topic_id = topic_id;
	}

	public String getArticle_id() {
		return article_id;
	}

	public void setArticle_id(String article_id) {
		this.article_id = article_id;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTo_comment_id() {
		return to_comment_id;
	}

	public void setTo_comment_id(String to_comment_id) {
		this.to_comment_id = to_comment_id;
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
