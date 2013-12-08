package com.meizhuo.etips.model;
/**
 * 用电量情况对象
 * @author Jayin Ton
 *
 */
public class ElectricityInfo {
	/**
	 * 栋数 e.g: 3 栋
	 */
	public String apartID = "";
	/**
	 * 房间号 e.g: 3栋706 显示为706 而不是3706
	 */ 
	public String meterRoom = "";
	/**
	 * 已使用电量
	 */
	public String hasUseElc = "";
	/**
	 * 剩余电量
	 */
	public String ElcLeft = "";
	/**
	 * 记录时间
	 */
	public String RecordTime = "";

	@Override
	public String toString() {
		return "ElectricityInfo [ElcLeft=" + ElcLeft + ", RecordTime="
				+ RecordTime + ", apartID=" + apartID + ", hasUseElc="
				+ hasUseElc + ", meterRoom=" + meterRoom + "]";
	}

}
