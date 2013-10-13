package com.meizhuo.etips.net.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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

import com.meizhuo.etips.model.ElectricityInfo;
/**
 * 生活服务API
 * @author Jayin Ton
 *
 */
public class LifeServiceAPI {
	public LifeServiceAPI() {

	}

	public ElectricityInfo getElectricityInfo(String apartID, String meterRoom)
			throws ClientProtocolException, IOException {
		HashMap<String, String> m = new HashMap<String, String>();
		m.put("34", "1");
		m.put("35", "9");
		m.put("36", "17");
		m.put("37", "25");
		m.put("1", "33");
		m.put("2", "40");
		m.put("3", "47");
		m.put("4", "55");
		m.put("5", "63");
		m.put("6", "68");
		m.put("19", "74");
		m.put("20", "75");
		m.put("21", "76");
		m.put("22", "77");
		m.put("14", "105");
		m.put("15", "114");
		m.put("38", "122");
		m.put("39", "123");
		m.put("40", "124");
		m.put("41", "214");
		m.put("42", "226");
		m.put("43", "227");
		ElectricityInfo elc = null;
		DefaultHttpClient client = new DefaultHttpClient();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("action", "search"));
		params.add(new BasicNameValuePair("apartID", m.get(apartID)));
		params.add(new BasicNameValuePair("meter_room", apartID + meterRoom));
		HttpPost post = new HttpPost("http://202.192.252.140/index.asp");
		HttpEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
		post.setEntity(entity);
		HttpResponse response = client.execute(post);
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			elc = new ElectricityInfo();
			String html = EntityUtils.toString(response.getEntity(), "gb2312");
			client.getConnectionManager().shutdown();
			if(html.indexOf("房间编号输入无效")!=-1)return null;
			return HtmlParser.parseHtmlForElectricityInfo(html, elc);
		}
		client.getConnectionManager().shutdown();
		return elc;

	}
}
