package com.meizhuo.etips.net.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.meizhuo.etips.common.utils.Elog;
import com.meizhuo.etips.common.utils.PathBuilder;
import com.meizhuo.etips.model.SchoolNews;

/**
 * 邑大新闻API
 * 
 * @author Jayin Ton
 * 
 */
public class WYUNewsAPI {
	private SCResultCache  resultCache = null;
	public WYUNewsAPI(){
		resultCache = new SCResultCache();
	}
	/**
	 * 获取新闻列表
	 * 
	 * @return 新闻列表
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public  List<SchoolNews> getSchoolNews(int page)
			throws ClientProtocolException, IOException {
		if(resultCache.getMap().get(page) !=null)return resultCache.getMap().get(page);
		List<SchoolNews> list = new ArrayList<SchoolNews>();
		DefaultHttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(new HttpGet(PathBuilder
				.getSchoolNewsPath(page)));
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			String html = EntityUtils.toString(response.getEntity(), "gb2312");
			client.getConnectionManager().shutdown();
			int totalNews = HtmlParser.parseHtmlForSchoolNews(html, list);
			resultCache.setTotalNews(totalNews);
			resultCache.addList(page, list);
			return list;
		}
		client.getConnectionManager().shutdown();
		return list;
	}
	/**
	 * 获得学校首页的新闻的总页数
	 * @return
	 */
	public int getSCtotalPage(){
		return resultCache.getToatalPage();
	}

	/**
	 * 通过SchoolNews。linkPath来获取该新闻的详细内容
	 * 
	 * @param linkPath
	 *            该新闻的相对路径
	 * @return 新闻的详细内容
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public   String getSchoolNewsDetails(String linkPath)
			throws ClientProtocolException, IOException {
		DefaultHttpClient client = new DefaultHttpClient();
	//	Elog.i("linke path: "+linkPath);
		HttpResponse response = client.execute(new HttpGet(PathBuilder
				.getSchoolNewsDetailPath(linkPath)));
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			String html = EntityUtils.toString(response.getEntity(), "gb2312");
			client.getConnectionManager().shutdown();
		//	return HtmlParser.parseHtmlForSchoolNewsDetails(html);
			return HtmlParser.wrapHtmlForSchoolNewsDetails(html);
		} else {
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_FORBIDDEN) {
				return "403";
			}
		}
		return null;
	}
	
}

/**
 * 假设邑大首页新闻页最大页数为900
 * @author Lenovo
 *
 */
class SCResultCache {
	private Map<Integer, List<SchoolNews>> map;
	private int totalNews;
    private int totalPage ;
	public SCResultCache() {
		map = new HashMap<Integer, List<SchoolNews>>();
		totalPage = 0;
		totalNews = 0;
	}

	public List<SchoolNews> getList(int page) {
		return map.get(page);
	}
	public void addList(int page,List<SchoolNews> mlist){
		map.put(page, mlist);
	}
	
	public Map<Integer, List<SchoolNews>> getMap(){
		return map;
	}
	
	public void setTotalNews(int totalNews){
		this.totalNews = totalNews;
	}
	
	public int getToatalPage(){
		if(totalNews % 10 ==0)return totalNews / 10;
		else return totalNews / 10 +1;
	}
}