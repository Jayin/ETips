package com.meizhuo.etips.model;

import java.io.Serializable;

/**
 * 图片信息
 * @author Jayin Ton
 * 
 */
@SuppressWarnings("serial")
public class ImgInfo implements Serializable {
	/** 下载图片的url */
	private String url;
	/** 展示的开始的日期 */
	private long displayTime;
	/** 图片描述 */
	private String description;
	/** 是否已经下载完毕 */
	private boolean isDownloaded = false;
	/** 图片名称(包含后缀名) */
	private String name;
	/** 持续时间（天） */
	private int continuance;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getContinuance() {
		return continuance;
	}

	public void setContinuance(int continuance) {
		this.continuance = continuance;
	}

 
	public ImgInfo() {
		isDownloaded = false;
		displayTime = System.currentTimeMillis();
	}

	public boolean isDownloaded() {
		return isDownloaded;
	}

	public void setDownloaded(boolean isDownloaded) {
		this.isDownloaded = isDownloaded;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public long getDisplayTime() {
		return displayTime;
	}

	public void setDisplayTime(long displayTime) {
		this.displayTime = displayTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "ImgInfo [url=" + url + ", displayTime=" + displayTime
				+ ", description=" + description + ", isDownloaded="
				+ isDownloaded + ", name=" + name + ", continuance="
				+ continuance + "]";
	}
	
}
