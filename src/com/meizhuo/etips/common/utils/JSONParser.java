package com.meizhuo.etips.common.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.meizhuo.etips.model.Comment;
import com.meizhuo.etips.model.Topic;
import com.meizhuo.etips.model.Tweet;

/**
 * JSON解析器
 * <p>
 * How to use?
 * </p>
 * <p>
 * 1.先判断状态 is() or getStatus()
 * </p>
 * <p>
 * 2.使用parseXXX()
 * </p>
 * 当然，自行判断resposen是否为空也行ss
 * 
 * @author Jayin Ton
 * 
 */
public class JSONParser {
	/**
	 * 状态是否为OK= 200
	 * 
	 * @param json
	 * @return
	 */
	public static boolean isOK(String json) {
		JSONObject job;
		try {
			job = new JSONObject(json);
			return job.getInt("status") == ETipsContants.SC_OK;
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		} catch (NullPointerException e) {
			e.printStackTrace();
			//内部服务器问题
			return false;
		}
	}

	/**
	 * 获取状态码
	 * 
	 * @param json
	 * @return 状态码
	 */
	public static int getStatusCode(String json) {
		JSONObject job;
		try {
			job = new JSONObject(json);
			return job.getInt("status");
		} catch (JSONException e) {
			e.printStackTrace();
			return 201;
		}

	}

	/**
	 * 根据json 获得返回状态(String)
	 * 
	 * @param json
	 * @return
	 */
	public static String getStatus(String json) {
		JSONObject job;
		try {
			job = new JSONObject(json);
			int status = job.getInt("status");
			switch (status) {
			case ETipsContants.SC_Compose_faild:
				return "发布失败";
			case ETipsContants.SC_Edit_timeout:
				return "修改超時";
			case ETipsContants.SC_Email_format_error:
				return "邮箱格式错误";
			case ETipsContants.SC_Email_has_regist:
				return "邮箱已注册";
			case ETipsContants.SC_Email_not_exist:
				return "邮箱不存在";
			case ETipsContants.SC_OK:
				return "成功";
			case ETipsContants.SC_PSW_incorrect:
				return "密码错误";
			case ETipsContants.SC_ID_HasRegist:
				return "学号已被注册";
			default:
				return "未知错误";
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return "未知错误";
		}
	}

	/**
	 * 获得response
	 * 
	 * @param json
	 * @return null if it's in Exception
	 */
	public static JSONArray getResponse(String json) {
		JSONObject job;
		try {
			String _json =json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1);
//			System.out.println(_json);
//			System.out.println("json:");
//			 System.out.println(json);
			job = new JSONObject(_json);
		   	return job.getJSONArray("response");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解析获取话题列表
	 * 
	 * @param json
	 * @return List<Topic>
	 */
	public static List<Topic> parseTopicList(String json) {
		List<Topic> list = new ArrayList<Topic>();
		JSONArray jsonArray = getResponse(json);
		if (jsonArray == null)
			return list;
		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				JSONObject obj = jsonArray.getJSONObject(i);
				String id = obj.getString("topic_id");
				String name = obj.getString("topic_name");
				String description = obj.getString("description");
				int enableIncognito = obj.getString("enableIncognito").equals(
						"1") ? 1 : 0;
				int click_amount = obj.getInt("click_amount");
				Elog.i("i =" + i + "--->d" + description + "--->inco"
						+ enableIncognito);
				list.add(new Topic(name, id, description, enableIncognito,
						click_amount));
			} catch (JSONException e) {
				e.printStackTrace();
				continue;
			}
		}
		return list;
	}

	/**
	 * 解析获取帖子Tweet列表 Note: 这里的解析的时候并没有确定Tweet所在的话题id 手动加入方法 吐槽：智峣
	 * 
	 * @see #addTweetTopicID(List)
	 * @param json
	 * @return List<Tweet>
	 */
	public static List<Tweet> parseTweetList(String json) {
		List<Tweet> list = new ArrayList<Tweet>();
		JSONArray jsonArray = getResponse(json);
		if (jsonArray == null)
			Elog.i("jsonArray---is null" + jsonArray);
		if (jsonArray == null)
			return null; // jsonArray response null!!
		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				JSONObject obj = jsonArray.getJSONObject(i);
				String article_id = obj.getString("article_id");
				// Elog.i("article_id---"+article_id);
				long sendTime = Long.parseLong(obj.getString("sendTime"));
				// Elog.i("sendTime---"+sendTime);
				String author = obj.getString("author");
				// Elog.i("author---"+author);
				int incognito = Integer.parseInt(obj.getString("incognito"));
				// Elog.i("incognito---"+incognito);
				int comments = Integer.parseInt(obj.getString("comments"));
				// Elog.i("comments---"+comments);
				String content = obj.getString("content");
				// Elog.i("content---"+content);
				String nickname = obj.getString("nickname");
				list.add(new Tweet(article_id, sendTime, author, incognito,
						content, comments,nickname));
			} catch (JSONException e) {
				e.printStackTrace();
				continue;
			}
		}
		return list;
	}

	/**
	 * 整体添加topic id
	 * 
	 * @param list
	 * @param topicID
	 * @return
	 */
	public static List<Tweet> addTweetTopicID(List<Tweet> list, String topicID) {
		for (Tweet t : list) {
			t.setTopicID(topicID);
		}
		return list;
	}

	/**
	 * 解析获取评论列表 ——TweetAPI.getComment();
	 * 
	 * @param json
	 * @return List<Comment>
	 */
	public static List<Comment> parseCommentList(String json) {
		List<Comment> list = new ArrayList<Comment>();
		JSONArray jsonArray = getResponse(json);
		if (jsonArray == null)
			return null;
		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				JSONObject obj = jsonArray.getJSONObject(i);
				String topic_id = obj.getString("topic_id");
				String article_id = obj.getString("article_id");
				long sendTime = obj.getLong("sendTime");
				String author = obj.getString("author");
				int incognito = obj.getInt("incognito");
				String content = obj.getString("content");
				String nickname = obj.getString("nickname");
				list.add(new Comment(topic_id, article_id, sendTime, author,
						incognito, content,nickname));
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
		}
		return list;
	}

}
