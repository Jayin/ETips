package com.meizhuo.etips.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 帖子实体类 也叫推文
 * 注意的地方都在下面两个方法
 * @see #Tweet(String, String, long, String, int, String, int)
 * @see #Tweet(String, long, String, int, String, int)   
 * @author Jayin Ton
 * 
 */
public class Tweet implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 常用的构造方法：
	 * 默认：
	 * 1.评论数0
	 * 2.无评论列表 但已经new ArrayList<T>()
	 * 3.不赞
	 * @param topicID
	 * @param articleID
	 * @param sendTime
	 * @param author
	 * @param incognito
	 * @param content
	 *  
	 */
	public Tweet(String topicID, String articleID, long sendTime,
			String author, int incognito, String content,int commentCount,String nickname) {
		this(topicID, articleID, sendTime, author, incognito, content, commentCount,
				new ArrayList<Comment>(), false,nickname);
	}
    /**
     * 在JSONParse.parseTweetList()构造时使用这方法
     * 在解释json数据的时候服务端并不返回该tweet所在的话题id(topic),
     * 因此JSONParse.parseTweetList()提供了一个方法来手动添加！
     * @param articleID
     * @param sendTime
     * @param author
     * @param incognito
     * @param content
     * @param commentCount
     */
	public Tweet(String articleID, long sendTime, String author, int incognito,
			String content, int commentCount,String nickname) {
		this(null, articleID, sendTime, author, incognito, content, commentCount,
				new ArrayList<Comment>(), false,nickname);
	}
 
	@Override
	public String toString() {
		return "Tweet [topicID=" + topicID + ", articleID=" + articleID
				+ ", sendTime=" + sendTime + ", author=" + author
				+ ", incognito=" + incognito + ", content=" + content
				+ ", commentCount=" + commentCount + ", commentList="
				+ commentList + ", like=" + like + ", nickname=" + nickname
				+ "]";
	}

	public Tweet(String topicID, String articleID, long sendTime,
			String author, int incognito, String content, int commentCount,
			List<Comment> commentList, boolean like,String nickname) {
		super();
		this.topicID = topicID;
		this.articleID = articleID;
		this.sendTime = sendTime;
		this.author = author;
		this.incognito = incognito;
		this.content = content;
		this.commentCount = commentCount;
		this.commentList = commentList;
		this.like = like;
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
	 * 评论数
	 */
	private int commentCount;
	/*
	 * 评论列表
	 */
	private List<Comment> commentList = null;
	/**
	 * 赞
	 */
	private boolean like;
	/**
	 * 发布人昵称
	 */
	private String nickname ;
	/**
	 * 是否匿名 是否匿名:是为1，其他为否，但否一般约定为0
	 */
	public boolean isIncognito() {
		return incognito == 1;
	}

	public boolean isLike() {
		return like;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public List<Comment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<Comment> commentList) {
		this.commentList = commentList;
	}

	public void setLike(boolean like) {
		this.like = like;
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

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public long getSendTime() {
		return sendTime;
	}

	public void setSendTime(long sendTime) {
		this.sendTime = sendTime;
	}

	public int getIncognito() {
		return incognito;
	}

	public void setIncognito(int incognito) {
		this.incognito = incognito;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

}
