package com.meizhuo.etips.model;

import java.util.ArrayList;
import java.util.List;

public class ClassroomInfo {

	@Override
	public String toString() {
		return "ClassroomInfo [name=" + name + ", status=" + status + "]";
	}

	public ClassroomInfo() {
		status = new ArrayList<String>();
		this.status.add("free");
		this.status.add("free");
		this.status.add("free");
		this.status.add("free");
		this.status.add("free");
	}

	/**
	 * 今日课程信息</br> list.get(index)</br> 第一节，第二节、、、、
	 * 
	 */
	private List<String> status;
    /**
     * 名称
     */
    private String name ;
	/**
	 * 教室号
	 * @return
	 */
    private String RoomNumber;
    
    /**
	 * 根据地点获取教室号
	 * 
	 * @param address
	 * @return
	 */
	private int getRoomNumber(String address) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < address.length(); i++) {
			if (address.charAt(i) >= '0' && address.charAt(i) <= '9') {
				sb.append(address.charAt(i));
			}
		}
		return Integer.parseInt(sb.toString());
	}

 
	public String getRoomNumber() {
		return RoomNumber;
	}

	public void setRoomNumber(String roomNumber) {
		RoomNumber = roomNumber;
	}

	public String getName() {
		return name;
	}

	public ClassroomInfo setName(String name) {
		this.name = name;
		setRoomNumber(getRoomNumber(name)+"");
		return this;
	}

	public List<String> getStatus() {
		return status;
	}

	public void setStatus(List<String> status) {
		this.status = status;
	}
	
	

}
