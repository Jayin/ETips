package com.meizhuo.etips.common;

import java.util.Calendar;

/**
 * 日期工具类
 * 
 * @author Jayin Ton
 * 
 */
public class CalendarUtils {
	/**
	 * 时间轴，类似于新浪微博/微信的时间 :前xx分钟，今日hh:mm 昨日hh:mm
	 */
	public static String TYPE_timeline = "timeline";
	/** "yy-mm-dd-hh-mm"*/
	public static String TYPE_ONE = "yy-mm-dd-hh-mm";
	/** "yy-mm-dd"*/
	public static String TYPE_TWO = "yy-mm-dd";
	/**  "yy-mm-dd hh:mm:ss"*/
	public static String TYPE_THIRD = "yy-mm-dd hh:mm:ss";

	/**
	 * 根据给定的时间和格式生成一时间字符串
	 * 
	 * @param milliseconds
	 *            时间戳
	 * @param fromat
	 *            格式 ，请查看本工具类的常量TYPE_
	 * @return 时间字符串
	 */
	public static String getTimeFromat(long milliseconds, String fromat) {
		StringBuilder sb = new StringBuilder();
		Calendar cur = getCurrent();
		Calendar pre = getCurrent();
		pre.setTimeInMillis(milliseconds);
		if ("timeline".equals(fromat)) {
			int tmp = cur.get(Calendar.YEAR) - pre.get(Calendar.YEAR);
			if(tmp>0)return getTimeFromat(milliseconds, TYPE_ONE);
			tmp = cur.get(Calendar.DAY_OF_YEAR)
					- pre.get(Calendar.DAY_OF_YEAR);
			switch (tmp) {
			case 0:
				tmp = cur.get(Calendar.HOUR_OF_DAY)
						- pre.get(Calendar.HOUR_OF_DAY);
				if (tmp <= 1) {
					tmp = cur.get(Calendar.MINUTE) - pre.get(Calendar.MINUTE);
					sb.append(tmp).append("分钟").append("前");
				} else {
					sb.append("今日 ")
							.append(_formatNmber(pre.get(Calendar.HOUR_OF_DAY)))
							.append(":")
							.append(_formatNmber(pre.get(Calendar.MINUTE)));
				}
				break;
			case 1:
				sb.append("昨日 ").append(_formatNmber(pre.get(Calendar.HOUR_OF_DAY)))
						.append(":").append(_formatNmber(pre.get(Calendar.MINUTE)));
				break;
			default:
				sb.append(pre.get(Calendar.YEAR)).append("-").append(pre.get(Calendar.MONTH) + 1).append("-")
						.append(pre.get(Calendar.DAY_OF_MONTH)).append("- ")
						.append(_formatNmber(pre.get(Calendar.HOUR_OF_DAY)))
						.append(":")
						.append(_formatNmber(pre.get(Calendar.MINUTE)));
				break;
			}
		} else if ("yy-mm-dd-hh-mm".equals(fromat)) {
			sb.append(getTimeFromat(milliseconds, TYPE_TWO)).append(" ")
					.append(_formatNmber(pre.get(Calendar.HOUR_OF_DAY)))
					.append(":")
					.append(_formatNmber(pre.get(Calendar.MINUTE)));
		}else if("yy-mm-dd".equals(fromat)){
			sb.append(pre.get(Calendar.YEAR)).append("-")
			.append((pre.get(Calendar.MONTH) + 1) + "").append("-")
			.append(pre.get(Calendar.DAY_OF_MONTH));
		}else if("yy-mm-dd hh:mm:ss".equals(fromat)){
			sb.append(getTimeFromat(milliseconds,TYPE_TWO)).append(" ").append(_formatNmber(pre.get(Calendar.HOUR_OF_DAY)))
			.append(":").append(_formatNmber(pre.get(Calendar.MINUTE))).append(":").append(_formatNmber(pre.get(Calendar.SECOND)));
		}
		return sb.toString();
	}

	/**
	 * 获得当前时间的Calendar实例
	 * 
	 * @return
	 */
	public static Calendar getCurrent() {
		return Calendar.getInstance();
	}

	/**
	 * 格式化时间 早上9:9 -> 09:09
	 * 
	 * @param number
	 * @return
	 */
	private static String _formatNmber(int number) {
		if (number < 10)
			return "0" + number;
		else
			return number + "";
	}
	
	/**
	 * 判断是否在同一天<br>
	 * return 0,if day1==day2; return 1 ,if day1 > day2;return -1,if day1<day2;
	 * 
	 * @param day1
	 * @param day2
	 * @return 0,if day1==day2; 1 ,if day1 > day2; -1,if day1<day2;
	 */
	public static int isInSameDay(long day1, long day2) {
		Calendar c1 = Calendar.getInstance();
		c1.setTimeInMillis(day1);
		Calendar c2 = Calendar.getInstance();
		c2.setTimeInMillis(day2);
		if (c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR))
			return 0;
		else {
			return c1.get(Calendar.DAY_OF_YEAR) > c2.get(Calendar.DAY_OF_YEAR) ? 1
					: -1;
		}

	}
}
