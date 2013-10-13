package com.meizhuo.etips.model;

import java.io.Serializable;

/**
 * 话题实体类
 * 
 * @author Jayin Ton
 * 
 */
public class Topic implements Serializable {

 

	@Override
	public String toString() {
		return "Topic [name=" + name + ", updataTime=" + updataTime
				+ ", description=" + description + ", id=" + id
				+ ", enableIncognito=" + enableIncognito + "]";
	}

	public Topic(String name, String id,String description,int enableIncognito,int click_amount) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.updataTime = System.currentTimeMillis();
		this.enableIncognito  = enableIncognito;
		this.click_amount = click_amount;
		
	}

	/*
	 * 话题名称
	 */
	private String name;
	/*
	 * 更新时间
	 */
	private long updataTime;
	/*
	 * 描述
	 */
	private String description;
	/*
	 * 话题的唯一id
	 */
	private String id;
	/**
	 * 是否可以匿名
	 */
	private int enableIncognito  = 0;
	/**
	 * 点击率
	 */
	 private int click_amount   = 0;
	public int getClick_amount() {
		return click_amount;
	}
	public void setClick_amount(int click_amount) {
		this.click_amount = click_amount;
	}
	public int getEnableIncognito() {
		return enableIncognito;
	}

	public void setEnableIncognito(int enableIncognito) {
		this.enableIncognito = enableIncognito;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getUpdataTime() {
		return updataTime;
	}

	public void setUpdataTime(long updataTime) {
		this.updataTime = updataTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
