package com.meizhuo.etips.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 检索图书结果SearchBookResult List<BookInfo>的封装，想当于做个缓存 当你已经访问了第一页
 * 去了第二页在返回来，没有必要在重新访问载入第一页
 * 
 * @author Jayin Ton
 * 
 */
public class SearchBookResult {
	/**
	 * 搜索关键字
	 */
	public String keyword;
	/**
	 * 用于分页 第一层： 第几页 第二层： 对应的第 X 页的 检索列表
	 */
	private Map<Integer, List<BookInfo>> ResultMap;
	/**
	 * 检索结果数
	 */
	private int totalResult;
	/**
	 * 一共有多少页 根据结果数算出 ；一页显示20条数据
	 */
	private int totalPage;

	public SearchBookResult() {
		ResultMap = new HashMap<Integer, List<BookInfo>>();
		keyword = "";
		totalResult = 0;
		totalPage = 0;
	}

	/**
	 * 加入到当前 SearchBookResult Map
	 * 
	 * @param page
	 * @param list
	 */
	public void addList(int page, List<BookInfo> list) {
		ResultMap.put(page, list);
	}

	/**
	 * 获得第page也的检索列表
	 * 
	 * @param page
	 * @return
	 */
	public List<BookInfo> getResultList(int page) {
		return ResultMap.get(page);
	}

	/**
	 * 获得当前 SearchBookResult Map
	 * 
	 * @return
	 */
	public Map<Integer, List<BookInfo>> getResultMap() {
		return ResultMap;
	}

	/**
	 * 根据检索结果数求出页数
	 * 
	 * @return
	 */
	public int getTotalPage() {
		if (totalResult % 20 == 0)
			return totalResult / 20;
		else
			return totalResult / 20 + 1;
	}

	/**
	 * 获得检索结果总数
	 * 
	 * @return
	 */
	public int getTotalResult() {
		return totalResult;
	}

	/**
	 * 设置检索结果总数
	 * @param totalResult
	 */
	public void setTotalResult(int totalResult) {
		this.totalResult = totalResult;
	}

}
