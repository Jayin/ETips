package com.meizhuo.etips.model;

public class MNotes {
	private long time;
	private String content;

	public MNotes() {
		time = System.currentTimeMillis();
		content = "总有些事情容易忘记，不妨写下来的吧！";
	}
	public MNotes(long time,String content){
		this.time = time;
		this.content = content;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
