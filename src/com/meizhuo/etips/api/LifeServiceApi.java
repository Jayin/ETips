package com.meizhuo.etips.api;

import java.util.HashMap;
import java.util.Map;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 生活服务api
 * <li>获取宿舍用电
 * 
 * @author Jayin Ton
 * 
 */
public class LifeServiceApi {
	private HashMap<String, String> apartList = new HashMap<String, String>();

	public LifeServiceApi() {
		apartList.put("34", "1");
		apartList.put("35", "9");
		apartList.put("36", "17");
		apartList.put("37", "25");
		apartList.put("1", "33");
		apartList.put("2", "40");
		apartList.put("3", "47");
		apartList.put("4", "55");
		apartList.put("5", "63");
		apartList.put("6", "68");
		apartList.put("19", "74");
		apartList.put("20", "75");
		apartList.put("21", "76");
		apartList.put("22", "77");
		apartList.put("14", "105");
		apartList.put("15", "114");
		apartList.put("38", "122");
		apartList.put("39", "123");
		apartList.put("40", "124");
		apartList.put("41", "214");
		apartList.put("42", "226");
		apartList.put("43", "227");
	}

	public Map<String, String> getApartList() {
		return this.apartList;
	}
    /**
     * 获取宿舍用电
     * @param apartID
     * @param meterRoom
     * @param handler
     */
	public void getElectricityInfo(String apartID, String meterRoom,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.add("action", "search");
		params.add("apartID", apartID);
		params.add("meter_room", meterRoom);
		EtipsClient.post("http://202.192.252.140/index.asp", params, handler);

	}

}
