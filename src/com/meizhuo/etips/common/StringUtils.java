package com.meizhuo.etips.common;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.view.View;

public class StringUtils {

	/**
	 * 判断给定字符串是否空白串。 空白串是指由空格(' ')、制表符('\t')、回车符'\r'、换行符'\n'组成的字符串
	 * 若输入字符串为null或空字符串，返回true
	 * 
	 * @param input
	 * @return boolean
	 */
	public static boolean isEmpty(String input) {
		if (input == null || "".equals(input.trim())) {
			return true;
		}
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c != ' ' && c != '\t' && c != '\n' && c != '\r') {
				return false;
			}
		}
		return true;
	}

	/**
	 * 装饰text,如果有链接，均弹出浏览器选项
	 * 
	 * @param text
	 * @return SpannableString
	 */
	public static SpannableString wrapText(final Context context,
			final String text) {
		final int index = text.indexOf("http");
		if (index == -1) {
			return new SpannableString(text);
		}
		final SpannableString ss = new SpannableString(text);
		ss.setSpan(new ClickableSpan() {
			@Override
			public void onClick(View widget) {
				Uri uri = Uri.parse(text.substring(index, text.length()));
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				context.startActivity(intent);
			}
		}, index, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return ss;
	}

	/**
	 * 解析课程的时间，用于判断什么时候上课
	 * 
	 * @param time
	 *            课程时间
	 * @return 要上课的周数集合set
	 */
	public static Set<Integer> parseTimeOfLesson(String time) {

		Set<Integer> set = new HashSet<Integer>();
		String[] afterSplit = time.replace("第", "").replace("周", "").trim()
				.split(",");
		for (String s : afterSplit) {

			if (s.indexOf("-") == -1) {
				set.add(Integer.parseInt(s));
			} else {
				String[] _s = s.split("-");
				int start = Integer.parseInt(_s[0]);
				int end = Integer.parseInt(_s[1]);
				for (int i = start; i <= end; i++) {
					set.add(i);
				}
			}
		}

		return set;
	}

	/**
	 * 判断修改的时间是否有效
	 * 
	 * @param s
	 *            上课周数
	 * @return true if input String is vaild
	 */
	public static boolean isEditVaild(String s) {
		if (s == null)
			return false;
		s = s.replace("第", "").replace("周", "").trim();
		if (s.equals(""))
			return false;
		Pattern p = Pattern.compile("[^0-9,-]");
		Matcher m = p.matcher(s);
		if (m.find())
			return false;
		String[] ms = s.split(",");
		for (String os : ms) {
			if (os.contains("-")) {
				String[] _s = os.split("-");
				try {
					if (Integer.parseInt(_s[0]) > Integer.parseInt(_s[1])) {

						return false;
					}
				} catch (Exception e) { // index out of bounds
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 生成时间字符串 格式： yy-mm-dd 星期X
	 * 
	 * @return
	 */
	public static String getTimeFormat() {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
		StringBuilder sb = new StringBuilder();
		sb.append(c.get(Calendar.YEAR)).append("-")
				.append(c.get(Calendar.MONTH) + 1).append("-")
				.append(c.get(Calendar.DAY_OF_MONTH)).append(" 星期");
		int day_of_week = c.get(Calendar.DAY_OF_WEEK) == 1 ? 7 : c
				.get(Calendar.DAY_OF_WEEK) - 1;
		sb.append(getChinese(day_of_week));
		return sb.toString();
	}

	/**
	 * 根据星期x获取中文<br>
	 * 星期1 ->星期一
	 * 
	 * @param day_of_week
	 *            星期1 ->星期一
	 * @return
	 */
	public static String getChinese(int day_of_week) {
		switch (day_of_week) {
		case 1:
			return "一";
		case 2:
			return "二";
		case 3:
			return "三";
		case 4:
			return "四";
		case 5:
			return "五";
		case 6:
			return "六";
		default:
			return "日";
		}
	}

	/**
	 * 根据给出的时间 很格式 获取对应时间各个时期
	 * 
	 * @param milliseconds
	 * @param format
	 *            e.g.(yy-mm-dd) 有需要就在这里添加代码
	 * @return
	 */
	public static String getDateFormat(long milliseconds, String format) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(milliseconds);
		StringBuilder sb = new StringBuilder();
		if (format.equals("yy-mm-dd")) {
			sb.append(c.get(Calendar.YEAR)).append("-")
					.append(c.get(Calendar.MONTH) + 1).append("-")
					.append(c.get(Calendar.DAY_OF_MONTH));
		} else if (format.equals("mm-dd")) {
			sb.append(c.get(Calendar.MONTH)).append("-")
					.append(c.get(Calendar.DAY_OF_MONTH));
		}

		return sb.toString();
	}

	/**
	 * 检测字符串中只能包含：中文、数字、下划线、横线,英文a-z A-Z
	 * 
	 * @param sequence
	 * @return true if it's ok
	 */
	public static boolean isNickname(String sequence) {
		final String format = "[^\\u4E00-\\u9FA5\\uF900-\\uFA2D\\w-_a-zA-Z]";
		Pattern pattern = Pattern.compile(format);
		Matcher matcher = pattern.matcher(sequence);
		return !matcher.find();
	}

	/**
	 * 判断是否是邮箱
	 * 
	 * @param sequence
	 * @return
	 */
	public static boolean isEmail(String sequence) {
		Pattern pattern = Pattern
				.compile("^([a-z0-9A-Z]+[-_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
		Matcher matcher = pattern.matcher(sequence);
		return matcher.find();
	}

	/**
	 * 判断密码格式是否ok 长度8-16 , 英文+中文
	 * 
	 * @param sequence
	 * @return
	 */
	public static boolean isPswVaild(String sequence) {
		if (!(sequence.length() >= 8 && sequence.length() <= 16)) {
			return false;
		}
		Pattern pattern = Pattern.compile("^([a-zA-Z0-9]){8,16}$");
		Matcher matcher = pattern.matcher(sequence);
		return matcher.find();
	}

	/**
	 * 从url中获取文件名+后缀名
	 * 
	 * @param url
	 * @return
	 */
	public static String getFileNameFromUrl(String url) {
		int p = url.lastIndexOf("/");
		return url.substring(p + 1, url.length());
	}
}
