package com.meizhuo.etips.net.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import com.meizhuo.etips.model.BookBorrowRecord;
import com.meizhuo.etips.model.BookInfo;
import com.meizhuo.etips.model.BookStatus;
import com.meizhuo.etips.model.CourseQueryData;
import com.meizhuo.etips.model.ElectricityInfo;
import com.meizhuo.etips.model.Lesson;
import com.meizhuo.etips.model.SchoolNews;
import com.meizhuo.etips.model.ScoreRecord;
import com.meizhuo.etips.model.StudentInfo;

/**
 * HTML 解析工具类
 * 
 * @author Jayin Ton
 * 
 */
public class HtmlParser {

	/**
	 * 解析课程html Note: 同一节课，会有>=1个课程 为此用List<Lesson>来存贮
	 * 
	 * @param html
	 *            课程表html
	 * @param map
	 *            周1-7，课时1-5
	 * @return map[周数，对应当日的课程列表]
	 */
	public static Map<Integer, Map<Integer, List<Lesson>>> parseHtmlForLesson(
			String html, Map<Integer, Map<Integer, List<Lesson>>> map) {
		for (int i = 1; i <= 7; i++) {
			HashMap<Integer, List<Lesson>> hashmap = new HashMap<Integer, List<Lesson>>();
			for (int j = 1; j <= 7; j++)
				hashmap.put(j, new ArrayList<Lesson>());
			map.put(i, hashmap);
		}
		Pattern p = Pattern.compile("<td valign=top align=center>(.*)</td>");
		Matcher m = p.matcher(html);
		int day = 1, lessonTime = 1;
		while (m.find()) {
			List<Lesson> mlist = new ArrayList<Lesson>();
			String s = m.group(1).trim();
			s = s.replaceAll("<br>", "&nbsp;");
			String[] ms = s.split("&nbsp;");
			int j = 0;
			int count = 0;
			Lesson l = null;
			while (j < ms.length) {
				while (ms[j].equals(""))
					j++;
				switch (count) {
				case 0:
					l = new Lesson();
					l.LessonName = ms[j++].trim();
					count++;
					break;

				case 1:
					l.Time = ms[j++].trim();
					count++;
					break;
				case 2:
					l.address = ms[j++].trim();
					count++;
					break;

				case 3:
					l.Teacher = ms[j++].trim();
					mlist.add(l);
					count = 0;
					System.out.println(l.toString());
					break;
				}
			}
			if (ms.length == 0) {
				l = new Lesson();
				mlist.add(l);
			}
			(map.get(day)).put(lessonTime, mlist);
			if (day + 1 > 7) {
				day = 1;
				lessonTime++;
			} else {
				day++;
			}
		}
		return map;
	}

	/**
	 * 解析成绩列表
	 * 
	 * @param html
	 *            成绩页页代码
	 * @param list
	 *            成绩列表
	 * @return 成绩列表
	 */
	public static List<ScoreRecord> parseHtmlForScore(String html,
			List<ScoreRecord> list) {

		String regex = "<p class=MsoNormal align=center style='text-align:center'>([0-9a-zA-Z]+)</p>"; // 课程代码
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(html);
		while (m.find()) {
			int count = m.groupCount();
			for (int i = 1; i <= count; i++) {
				ScoreRecord sr = new ScoreRecord();
				sr.lessonCode = m.group(i);
				list.add(sr);
			}
		}

		regex = "<p class=MsoNormal><span style='font-family:宋体'>(.*)</span></p>"; // 课程名称
		p = Pattern.compile(regex);
		m = p.matcher(html);
		int i = 0;
		while (m.find()) {

			list.get(i++).lessonName = m.group(1);
			if (i == list.size())
				break;
		}
		regex  ="<p class=MsoNormal align=center style='text-align:center'><span\\s*\\r*style='font-family:宋体'>(.*)</span></p>";// 课程类别（选修or必修or通识课or公选课）
		p = Pattern.compile(regex);
		m = p.matcher(html);
		i = 0;
		while (m.find()) {
		    if(m.group(1).equals("必修")||m.group(1).equals("选修")||m.group(1).equals("通识课")||m.group(1).equals("公选课")||m.group(1).equals("必修")){
		    	list.get(i++).category = m.group(1);
				if (i == list.size())
					break;
		    }
		}
		regex = "<p class=MsoNormal align=center style='text-align:center'><span lang=EN-US>(.*)</span></p>";// 第一个为学分，第二个为成绩
		p = Pattern.compile(regex);
		m = p.matcher(html);
		i = 0;
		boolean sy = true;
		while (m.find()) {
			if (sy) {
				list.get(i).lessonScore = m.group(1);
			} else {
				list.get(i).score = m.group(1);
				i++;
			}
			sy = !sy;
			if (i == list.size())
				break;
		}

		return list;
	}

	/**
	 * 解析学生个人信息
	 * 
	 * @param html
	 *            html code
	 * @param stu
	 *            a student
	 * @return the information of the student
	 */
	public static StudentInfo parseHtmlForStudentInfo(String html,
			StudentInfo stu) {
		String regex = "<td height=\"26\" width=\"100\" align=\"center\">(.*)</td>";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(html);
		List<String> list = new ArrayList<String>();
		while (m.find()) {
			int count = m.groupCount();
			for (int i = 1; i <= count; i++) {
				if (m.group(i).trim().equals("&nbsp;"))
					list.add("null");
				else
					list.add(m.group(i).trim());
			}
		}

		regex = "<td height=\"26\" colspan=\"3\" align=\"center\">(.*)</td>";
		p = Pattern.compile(regex);
		m = p.matcher(html);
		while (m.find()) {
			int count = m.groupCount();
			for (int i = 1; i <= count; i++) {
				if (m.group(i).trim().equals("&nbsp;"))
					list.add("null");
				else
					list.add(m.group(i).trim());
			}
		}
		stu.id = list.get(0);
		stu.neme = list.get(1);
		stu.nickName = list.get(2);
		stu.sex = list.get(3);
		stu.birthday = list.get(4);
		stu.nation = list.get(5);
		stu.political = list.get(6);
		stu.classNumber = list.get(7);
		stu.department = list.get(8);
		stu.edu_system = list.get(9);
		stu.enter_time = list.get(10);
		stu.edu_status = list.get(11);
		stu.contact = list.get(14);
		stu.homeTown = list.get(17);
		stu.major = list.get(18);
		stu.system = list.get(19);
		stu.identify = list.get(20);
		stu.address = list.get(21);
		return stu;
	}

	/**
	 * 解析借阅图书列表html
	 * 
	 * @param html
	 *            html code
	 * @param list
	 *            a list of BookBorrowRecord
	 * @return 借阅信息列表
	 */
	public static List<BookBorrowRecord> parseHtmlForBookBorrowRecord(
			String html, List<BookBorrowRecord> list) {
		String regex = "<td width=\"5%\">(\\s+.*\\s+)</td>";

		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(html);
		while (m.find()) {
			BookBorrowRecord b = new BookBorrowRecord();
			if (m.group(1).indexOf("超期") != -1) {
				b.borrowStatus = "超期";
			} else if (m.group(1).indexOf("续满") != -1)
				b.borrowStatus = "续满";
			else
				b.borrowStatus = "可续借";

			list.add(b);
		}

		// 解析日期 第一个是应还日期 第二个是借阅日期
		regex = "<td width=\"10%\">([0-9]+.*)</td>";
		p = Pattern.compile(regex);
		m = p.matcher(html);
		boolean sy = true;
		int i = 0;
		while (m.find()) {
			if (sy) {
				list.get(i).latestReturn = m.group(1);
			} else {
				list.get(i).borrowTime = m.group(1);
				i++;
			}
			sy = !sy;
			if (i > 5)
				break;
		}
		// 解析书的id unique ID
		regex = "<td width=\"35%\".*ctrlno=(.*)\" target=\"_blank\">(.*)</a></td>";
		p = Pattern.compile(regex);
		m = p.matcher(html);
		i = 0;
		while (m.find()) {
			list.get(i).bookId = m.group(1);
			list.get(i++).bookName = m.group(2);
			if (i > 5)
				break;
		}
		// 解析图书的类型
		regex = " <td width=\"8%\">(.*[^类型])</td>";
		p = Pattern.compile(regex);
		m = p.matcher(html);
		i = 0;
		while (m.find()) {
			list.get(i++).category = m.group(1);
			if (i > 5)
				break;
		}
		// 解析图书的登录号
		regex = "<td width=\"7%\">(.*[0-9]+)</td>";
		p = Pattern.compile(regex);
		m = p.matcher(html);
		i = 0;
		while (m.find()) {
			list.get(i++).loginNumber = m.group(1);
			if (i > 5)
				break;
		}
		// for(i=0;i<list.size();i++){
		// System.out.println(list.get(i).toString());
		// }
		return list;
	}

	/**
	 * 解析检索图书的html
	 * 
	 * @param html
	 *            html code
	 * @param list
	 *            a list of bookInfo that not parse yet
	 * @return  检索结果数
	 * PS:这里可能会出现BUG
	 * 在解析检索结果数时，在电脑浏览器的查看源码是： <span id="ctl00_ContentPlaceHolder1_countlbl" style="color:Red;">2397</span>
	 * java模拟访问输出到控制台    ：                                              <span id="ctl00_ContentPlaceHolder1_countlbl"><font color="Red">2397</font></span>
	 * 有那么一点坑~；  
	 */
	public static int parseHtmlForSearch(String html,
			List<BookInfo> list) {
		int totalResult = 0;
		String regex = "<span class=\"title\"><a href=.*ctrlno=(.*)\" target=\"_blank\">(.*)</a></span>"
				+ ".*\\s*<td>(.*)</td>"
				+ ".*\\s*<td>(.*)</td>"
				+ ".*\\s*<td>(.*)</td>"
				+ ".*\\s*<td class=\"tbr\">(.*)</td>"
				+ ".*\\s*<td class=\"tbr\">(.*)</td>"
				+ ".*\\s*<td class=\"tbr\">(.*)</td>";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(html);
		while (m.find()) {
			BookInfo b = new BookInfo();
			b.BookID = m.group(1);
			b.BookName = m.group(2);
			b.Authors = m.group(3);
			b.Press = m.group(4);
			b.PressTime = m.group(5);
			b.ExactNumber = m.group(6);
			b.Totle = m.group(7);
			b.Left = m.group(8);
			list.add(b);
		}
		regex = "<font color=\"Red\">(.*)</font>";
		p = Pattern.compile(regex);
		m = p.matcher(html);
		if(m.find())totalResult = Integer.parseInt(m.group(1).trim());
		return totalResult;
	}

	/**
	 * 解析借书历史html
	 * 
	 * @param html
	 *            借书历史页面
	 * @param list
	 *            解析借书历史list
	 * @return 解析借书历史list
	 */
	public static List<BookBorrowRecord> parseHtmlForBorrowHistroy(String html,
			List<BookBorrowRecord> list) {
		String regex = "<td>(.*)</td>";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(html);
		int i = 0;
		BookBorrowRecord bbr = null;
		while (m.find()) {
			i++;
			switch (i) {
			case 1:
				bbr = new BookBorrowRecord();
				bbr.borrowTime = m.group(1);
				break;

			case 2:
				bbr.latestReturn = m.group(1);
				break;
			case 3:
				String _regex = "<a href=.*?ctrlno=([0-9]*)\".*>(.*)</a>";
				Pattern _p = Pattern.compile(_regex);
				Matcher _m = _p.matcher(m.group(1));
				while (_m.find()) {
					bbr.bookId = _m.group(1);
					bbr.bookName = _m.group(2);
				}
				break;
			case 5:
				bbr.borrowStatus = "已借";
				bbr.loginNumber = m.group(1);
				list.add(bbr);
				i = 0;
				break;
			}
		}
		return list;
	}

	/**
	 * 解析查询电量页面
	 * 
	 * @param html
	 *            查询电量页面
	 * @param elc
	 *            用电情况
	 * @return 用电情况
	 */
	public static ElectricityInfo parseHtmlForElectricityInfo(String html,
			ElectricityInfo elc) {
		String regex = "<td height=\"26\" align=\"left\" valign=\"middle\"><span class=\"STYLE7\">(.*)</span></td>"
				+ "\\s*\\r*<td align=\"left\" valign=\"middle\"><span class=\"STYLE7\">(.*)</span></td>"
				+ "\\s*\\r*<td width=\"15%\" align=\"left\" valign=\"middle\"><span class=\"STYLE7\">(.*)</span></td>"
				+ "\\s*\\r*<td width=\"15%\" align=\"left\" valign=\"middle\"><span class=\"STYLE7\">(.*)</span></td>"
				+ "\\s*\\r*<td width=\"30%\" align=\"left\" valign=\"middle\"><span class=\"STYLE7\">(.*)</span></td>";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(html);
		while (m.find()) {
			elc.apartID = m.group(1).trim();
			elc.meterRoom = m.group(2).trim();
			elc.hasUseElc = m.group(3).trim();
			elc.ElcLeft = m.group(4).trim();
			elc.RecordTime = m.group(5).trim();
		}
		return elc;
	}

	/**
	 * 解析新闻首页http://www.wyu.cn/news/default.asp
	 * 
	 * @param html
	 *            页面代码
	 * @param list
	 *            新闻列表
	 * @return 新闻总数（总篇数）
	 */
	public static int parseHtmlForSchoolNews(String html,
			List<SchoolNews> list) {
		Pattern p;
		Matcher m;
		String regex = "<td height=\"22\" valign=\"bottom\"> <a href=\"(.*)\" target=\"_blank\">(.*)</a>";

		p = Pattern.compile(regex);
		m = p.matcher(html);
		SchoolNews sn = null;
		while (m.find()) {
			sn = new SchoolNews();
			Document doc = Jsoup.parse(m.group(0));
			Element e = doc.select("a").first();
			sn.linkPath = e.attr("href");
			sn.title = e.text();
//			sn.linkPath = m.group(1);
//			sn.title = m.group(2);
			list.add(sn);

		}
		regex = " <td height=\"18\">.*&nbsp;&nbsp;(.*)&nbsp;&nbsp;(.*)</td>";
		p = Pattern.compile(regex);
		m = p.matcher(html);
		int i = 0;
		while (m.find()) {
			sn = list.get(i++);
			sn.from = m.group(1);
			sn.time = m.group(2);
			if (i > 10)
				break;
		}
		regex = "共<b>([0-9]*)</b>篇";
		p = Pattern.compile(regex);
		m = p.matcher(html);
		if(m.find())return Integer.parseInt(m.group(1));
		else return 0;
	}

	/**
	 * 解析一个新闻的详细内容页面 如：http://www.wyu.cn/news/news_xnxw/201371613514788772.htm
	 * 此处不再使用，改用WebView 直接获取网页代码处理后用WebView显示-> 改用:wrapHtmlForSchoolNewsDetails()
	 * @param html
	 *            该页面代码
	 * @return 新闻内容
	 * @throws UnsupportedEncodingException
	 * 
	 */
	public static String parseHtmlForSchoolNewsDetails(String html)
			throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		Document doc = Jsoup.parse(html);
		Elements elements = doc.getElementsByAttributeValue("id", "zoom");
		Element e = elements.get(0);
		elements = e.select("P");
		for (int i = 0; i < elements.size(); i++) {
		///	String s = new String(elements.get(i).text().getBytes(), "gbk");
			String s = elements.get(i).text();
			s = s.trim().replaceAll("\\?", "");
			sb.append(s).append("\n");
		}
		return sb.toString();
	}
	/**
	 * 包装 新闻的详细内容页面
	 * 易于WebView 显示
	 * @param html   该页面代码
	 * @return 包装好的页面代码
	 */
	public static String wrapHtmlForSchoolNewsDetails(String html){
		StringBuilder sb = new StringBuilder();
		Document doc = Jsoup.parse(html); 
		Elements elements = doc.getElementsByTag("table");
	    html = elements.get(2).html();	
	    sb.append("<html><body>").append(Jsoup.clean(html, new Whitelist().relaxed())).append("</body></html>");
		return  sb.toString();
	}
    /**
     * 解析检索页面 中的一本书的详情 
     * @see http://lib.wyu.edu.cn/opac/bookinfo.aspx?ctrlno=524565
     * @param html 
     * @param list  多条状态
     * @return
     */
	public static List<BookStatus> parseHtmlForBookStatus(String html,
			List<BookStatus> list) {
		BookStatus bs = new BookStatus();
		Document doc = Jsoup.parse(html);
		Elements elements = doc.getElementsByTag("tbody");
		Element element = elements.get(0);
		elements = element.getElementsByTag("td");
		if (html.indexOf("特藏码/排架码") != -1) {
			int i = 0;
			for (Element e : elements) {
				i++;
				String text = e.text().trim();
				switch (i) {
				case 1:
					bs.location = text;
					break;
				case 2:
					bs.collectionCode = text;
					break;
				case 3:
					bs.askNum = text;
					break;
				case 4:
					bs.loginNum = text;
					break;
				case 5:
					bs.version = text;
					break;
				case 6:
					bs.age = text;
					break;
				case 7:
					bs.status = text;
					break;
				case 8:
					bs.borrowKind = text;
					break;
				}
				if (i == 8) {
					list.add(bs);
					bs = new BookStatus();
					i = 0;
				}

			}
		} else {
			int i = 0;
			for (Element e : elements) {
				i++;
				String text = e.text().trim();
				switch (i) {
				case 1:
					bs.location = text;
					break;
				case 2:
					bs.askNum = text;
					break;
				case 3:
					bs.loginNum = text;
					break;
				case 4:
					bs.version = text;
					break;
				case 5:
					bs.age = text;
					break;
				case 6:
					bs.status = text;
					break;
				case 7:
					bs.borrowKind = text;
					break;

				}
				if (i == 7) {
					list.add(bs);
					bs = new BookStatus();
					i = 0;
				}
			}
		}
		return list;
	}
	
	/**
	 * 解析查询空课室页面
	 * 
	 * @param html
	 * @return List of CourseQueryData
	 */
	public static List<CourseQueryData> parseHtmlForQueryEmptyClassroom(
			String html) {
		// System.out.println(html);
		Document doc = Jsoup.parse(html);
		Elements elements1 = doc.getElementsByAttributeValue("bgcolor",
				"#F1F1F1");
		Elements elements2 = doc.getElementsByAttributeValue("bgcolor",
				"#FFFFEE");
		// System.out.println(elements2.toString());
		List<CourseQueryData> list = parseElements(elements1);
		list.addAll(parseElements(elements2));
		return list;
	}
    /**
     * 接上，分开解析
     * @param elements
     * @return
     */
	private static List<CourseQueryData> parseElements(Elements elements) {
		List<CourseQueryData> list = new ArrayList<CourseQueryData>();
		for (Element e : elements) {
			Elements mElements = e.getElementsByTag("td");
			CourseQueryData mCourseQueryData = new CourseQueryData();
			mCourseQueryData.setId(mElements.get(0).text());
			mCourseQueryData.setName(mElements.get(1).text());
			mCourseQueryData.setKind(mElements.get(3).text());
			mCourseQueryData.setXuefen(mElements.get(4).text());
			mCourseQueryData.setClasses(mElements.get(6).text());
			mCourseQueryData.setTime(mElements.get(7).text());
			mCourseQueryData.setAddress(mElements.get(8).text());
			mCourseQueryData.setTeacher(mElements.get(9).text());
		//	System.out.println(mCourseQueryData.toString());
			list.add(mCourseQueryData);
		}
		return list;
	}

}
