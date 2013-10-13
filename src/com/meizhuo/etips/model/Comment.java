package com.meizhuo.etips.model;

import java.io.Serializable;

/**
 * 一条推文的评论
 * 
 * @author Jayin Ton
 * 
 */
public class Comment implements Serializable {

	public Comment(String topicID, String articleID, long sendTime,
			String author, int incognito, String content,String nickname) {
		super();
		this.topicID = topicID;
		this.articleID = articleID;
		this.sendTime = sendTime;
		this.author = author;
		this.incognito = incognito;
		this.content = content;
		this.nickname = nickname;
	}

	/**
	 * 话题的唯一标识id
	 */
	private String topicID;
	/**
	 * 帖子的唯一标识id
	 */
	private String articleID;
	/**
	 * 发 布(评论) 时间
	 */
	private long sendTime;
	/**
	 * 发布人id（评论者id）
	 */
	private String author;
	/**
	 * 是否匿名
	 */
	private int incognito;
	/**
	 * 评论内容
	 */
	private String content;
	/**
	 * 评论人的昵称
	 */
	private String nickname;

	/**
	 * 是否匿名 是否匿名:是为1，其他为否，但否一般约定为0
	 */
	public boolean isIncognito() {
		return incognito == 1;
	}

	public String getTopicID() {
		return topicID;
	}

	public void setTopicID(String topicID) {
		this.topicID = topicID;
	}

	public String getArticleID() {
		return articleID;
	}

	public void setArticleID(String articleID) {
		this.articleID = articleID;
	}

	public long getSendTime() {
		return sendTime;
	}

	public void setSendTime(long sendTime) {
		this.sendTime = sendTime;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getIncognito() {
		return incognito;
	}

	public void setIncognito(int incognito) {
		this.incognito = incognito;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
}
