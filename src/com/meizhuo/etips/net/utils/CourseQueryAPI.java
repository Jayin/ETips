package com.meizhuo.etips.net.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.meizhuo.etips.model.ClassroomInfo;
import com.meizhuo.etips.model.CourseQueryData;
 
/**
 * 查询课室接口<br>
 * 
 * 1.queryClassroom()     <br>
 * 2.getClassroomInfo(list)  <br>
 * @author Jayin Ton
 *
 */
public class CourseQueryAPI {
	private DefaultHttpClient client = null;

	public CourseQueryAPI() {

	}

	private String post(List<NameValuePair> params)
			throws ClientProtocolException, IOException {
		client = new DefaultHttpClient();
		HttpPost post = new HttpPost(
				"http://jwc.wyu.cn/everyday/query/indeft/query.asp");
		HttpEntity entity = new UrlEncodedFormEntity(params, "gb2312"); // encode
		post.setEntity(entity);
		HttpResponse response = client.execute(post);
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

			return EntityUtils.toString(response.getEntity(), "gb2312");
		} else {
			return null;
		}
	}

	/**
	 * 
	 * 查询课室结果
	 * @param week
	 *            第几周
	 * @param weekTime
	 *            星期几
	 * @param address
	 *            地点
	 * @return List of CourseQueryData
	 */
	public List<CourseQueryData> queryClassroom(String week,
			String weekTime, String address) {
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("cc9", address));
		// we 为上课周次
		list.add(new BasicNameValuePair("ew", week));
		// pt为上课节次
		list.add(new BasicNameValuePair("pt", "1"));
		list.add(new BasicNameValuePair("pt", "2"));
		list.add(new BasicNameValuePair("pt", "3"));
		list.add(new BasicNameValuePair("pt", "4"));
		list.add(new BasicNameValuePair("pt", "5"));
		// wk 星期几
		list.add(new BasicNameValuePair("wk", weekTime));
		// 1-3为勾选星期，节次，周次
		list.add(new BasicNameValuePair("cnt", "1"));
		list.add(new BasicNameValuePair("cnt", "2"));
		list.add(new BasicNameValuePair("cnt", "3"));
		// 9为勾选上课地点
		list.add(new BasicNameValuePair("cnt", "9"));
		list.add(new BasicNameValuePair("Submit", "提交"));
		try {
			List<CourseQueryData> result = HtmlParser
					.parseHtmlForQueryEmptyClassroom(post(list));
			Collections.sort(result,
					new java.util.Comparator<CourseQueryData>() {

						@Override
						public int compare(CourseQueryData l, CourseQueryData r) {
							int a = getRoomNumber(l.getAddress());
							int b = getRoomNumber(r.getAddress());
							return a < b ? -1 : 1;
						}

					});
			return result;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (client != null) {
				client.getConnectionManager().shutdown();
			}
		}
	}

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
    /**
     * 获取课室状态列表
     * @param list
     * @return list of ClassroomInfo
     */
	public List<ClassroomInfo> getClassroomInfo(List<CourseQueryData> list) {
		if (list == null || list.size() == 0)
			return null;
		List<ClassroomInfo> result = new ArrayList<ClassroomInfo>();
		ClassroomInfo info = new ClassroomInfo();
		info.setName(list.get(0).getAddress());
		changeStatus(list.get(0).getTime(), info);
		result.add(info);
		for (int i = 1; i < list.size(); i++) {
			int l = getRoomNumber(result.get(result.size() - 1).getName());
			int r = getRoomNumber(list.get(i).getAddress());
			if (l == r) { // 可以合并
				changeStatus(list.get(i).getTime(), result
						.get(result.size() - 1));
			} else { // 不可以合并
			    info = new ClassroomInfo();
			    info.setName(list.get(i).getAddress());
			    changeStatus(list.get(i).getTime(),info);
			    result.add(info);
			}
		} 
		return result;
	}
    /**
     * 根据时间刷新课室的状态
     * @param time
     * @param info
     * @return
     */
	private ClassroomInfo changeStatus(String time, ClassroomInfo info) {
		StringBuilder sb = new StringBuilder(time.split("星期")[1]);
		sb.deleteCharAt(0);
		if (sb.charAt(0) == '第') {
			sb.deleteCharAt(0);
			sb.deleteCharAt(sb.length() - 1); 
			if (sb.charAt(0) == '1') {
				info.getStatus().set(0,"busy");
			} else if (sb.charAt(0) == '3') {
				info.getStatus().set(1,"busy");
			} else if (sb.charAt(0) == '5') {
				info.getStatus().set(2,"busy");
			} else if (sb.charAt(0) == '7') {
				info.getStatus().set(3,"busy");
			}
		} else {
			info.getStatus().set(4,"busy");
		}
		return info;
	}

	 
}
