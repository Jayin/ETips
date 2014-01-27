package com.meizhuo.etips.api;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 查询课室接口<br>
 * <li>查询课室
 * @author Jayin Ton
 * 
 */
public class ClassroomApi {
	public ClassroomApi() {

	}

	/**
	 * 查询课室结果
	 * 
	 * @param week
	 *            第几周
	 * @param weekTime
	 *            星期几
	 * @param address
	 *            地点
	 */
	public void queryClassroom(String week, String weekTime, String address,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.add("cc9", address);
		params.add("wk", week);
		params.add("ew", weekTime);
		// pt为上课节次
		params.add("pt", "1");
		params.add("pt", "2");
		params.add("pt", "3");
		params.add("pt", "4");
		params.add("pt", "5");
		// 1-3为勾选星期，节次，周次
		params.add("cnt", "1");
		params.add("cnt", "2");
		params.add("cnt", "3");
		// 9为勾选上课地点
		params.add("cnt", "9");
		params.add("Submit", "提交");
	 
        EtipsClient.post("http://jwc.wyu.cn/everyday/query/indeft/query.asp", params, handler);
	}
}
