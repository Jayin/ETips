package com.meizhuo.etips.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.google.gson.Gson;
import com.meizhuo.etips.model.BookInfo;
import com.meizhuo.etips.model.Course;
import com.meizhuo.etips.model.MNotes;
import com.meizhuo.etips.model.Topic;
import com.meizhuo.etips.model.Tweet;

/**
 * 小量数据SharedPreference增删查改框架 每增加一个SP
 * <p>
 * 1. 一般以时间（long）为key,value保存一个对象的JSON </br> 2. 使用google GSON 更快捷地 Object ->JSON
 * || JSON -> Object
 * </p>
 * 一般要改写的方法：
 * 
 * @see #toJSON(int, Object)
 * @see #toEntityAll(int)
 * @see #toEntityAll(int)
 * 
 * @author Jayin Ton
 * 
 */
public class SP {

	private SharedPreferences sp = null;
	private Context context;
	private String SPname;
	private int mode = Context.MODE_PRIVATE;

	public SP(String SPname, Context context) {
		this.context = context;
		this.SPname = SPname;
		sp = getSharedPreferences();
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public SharedPreferences getSharedPreferences() {
		if (sp != null)
			return sp;
		return context.getSharedPreferences(SPname, mode);
	}

	public boolean add(String key, String value) {
		return sp.edit().putString(key, value).commit();
	}

	public boolean delete(String key) {
		return sp.edit().remove(key).commit();
	}

	public boolean updata(String key, String value) {
		return add(key, value);
	}

	/**
	 * 这是一个非常神奇的函数，获取以后没有找到自动加入一个null 的默认值！
	 * 
	 * @param key
	 * @return
	 */
	public String getValue(String key) {
		if (sp.getString(key, "null").equals("null")) {
			add(key, "null");
			return "null";
		}
		return sp.getString(key, "null");
	}

	public Map<String, ?> getAll() {
		return sp.getAll();
	}

	public boolean deleteAll() {
		Map<String, ?> mMap = sp.getAll();
		try {
			for (String key : mMap.keySet()) {
				delete(key);
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 把一实体类转化为json(字符串)
	 * 
	 * @param what
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("finally")
	public String toJSON(int what, Object obj) {
		Gson gson = null;
		switch (what) {
		case ETipsContants.TYPE_SP_Book:
			gson = new Gson();
			return gson.toJson((BookInfo) obj);
		case ETipsContants.TYPE_SP_Topic:
			gson = new Gson();
			return gson.toJson((Topic) obj);
		case ETipsContants.TYPE_SP_Tweet:
			gson = new Gson();
			return gson.toJson((Tweet) obj);
	 
		}
		return null;
	}

	/**
	 * 从一个json(字符串)构造一个对应的对象
	 * 
	 * @param what
	 * @param json
	 * @return
	 */
	@SuppressWarnings("finally")
	public Object toEntity(int what, String json) {
		Gson gson = null;
		switch (what) {
		case ETipsContants.TYPE_SP_Book:
			gson = new Gson();
			return gson.fromJson(json, BookInfo.class);

		case ETipsContants.TYPE_SP_Notes:
			gson = new Gson();
			return gson.fromJson(json, MNotes.class);
		case ETipsContants.TYPE_SP_Topic:
			gson = new Gson();
			return gson.fromJson(json, Topic.class);

		case ETipsContants.TYPE_SP_Tweet:
			gson = new Gson();
			return gson.fromJson(json, Tweet.class);

	 
		}
		return null;
	}

	/**
	 * 推荐这里用法，返回的列表是按一定规则排序的了<br>
	 * NOTE: ETipsContants.TYPE_SP_Course  只有1对 key-value,所以 toEntity() = toEntityAll()
	 * 
	 * @param what
	 * @return
	 */
	public Object toEntityAll(int what) {
		Gson gson = null;
		Map<String, ?> map = null;
		switch (what) {
		case ETipsContants.TYPE_SP_Book:
			// your code ..
			gson = new Gson();
			List<BookInfo> bookInfos = new ArrayList<BookInfo>();
			map = sp.getAll();
			for (String key : map.keySet()) {
				BookInfo bookInfo = (BookInfo) toEntity(
						ETipsContants.TYPE_SP_Book, (String) map.get(key));
				bookInfos.add(bookInfo);
			}
			// 排序 (按时间排序，时间越大，越新)
			Collections.sort(bookInfos, new Comparator<BookInfo>() {
				@Override
				public int compare(BookInfo l, BookInfo r) {
					return (l.getCollectionTime() > r.getCollectionTime()) ? -1
							: 1;
				}
			});
			return bookInfos;

		case ETipsContants.TYPE_SP_Notes:
			gson = new Gson();
			List<MNotes> notes = new ArrayList<MNotes>();
			map = sp.getAll();
			for (String key : map.keySet()) {
				MNotes mNotes = new MNotes(Long.parseLong(key),
						(String) map.get(key));
				notes.add(mNotes);
			}
			// 排序 (按id排序 小的在前)
			Collections.sort(notes, new Comparator<MNotes>() {
				@Override
				public int compare(MNotes l, MNotes r) {
					return (l.getTime() > r.getTime()) ? -1 : 1;
				}
			});
			return notes;

		case ETipsContants.TYPE_SP_Topic:
			gson = new Gson();
			List<Topic> topicList = new ArrayList<Topic>();
			map = sp.getAll();
			for (String key : map.keySet()) {
				Topic topic = (Topic) toEntity(ETipsContants.TYPE_SP_Topic,
						(String) map.get(key));
				topicList.add(topic);
			}
			// 按时间排序，时间越小，越新；因为读入来的时候已经按时间顺序排好)
			Collections.sort(topicList, new Comparator<Topic>() {

				@Override
				public int compare(Topic lhs, Topic rhs) {
					return Integer.parseInt(lhs.getId()) > Integer.parseInt(rhs
							.getId()) ? 1 : -1;
				}
			});
			return topicList;

		case ETipsContants.TYPE_SP_Tweet:
			gson = new Gson();
			List<Tweet> tweetList = new ArrayList<Tweet>();
			map = sp.getAll();
			for (String key : map.keySet()) {
				Tweet t = (Tweet) toEntity(ETipsContants.TYPE_SP_Tweet,
						(String) map.get(key));
				tweetList.add(t);
			}
			// 同样地以Tweet发布时间排序 ，时间越大 越新
			Collections.sort(tweetList, new Comparator<Tweet>() {
				@Override
				public int compare(Tweet lhs, Tweet rhs) {
					return lhs.getSendTime() > rhs.getSendTime() ? -1 : 1;
				}
			});

			return tweetList;
			
		}

		return null;
	}

	/**
	 * 是否为空
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return sp.getAll().isEmpty();
	}

}
