package com.meizhuo.etips.db;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

import android.content.ContentValues;
import android.content.Context;
import android.test.AndroidTestCase;

import com.meizhuo.etips.model.ETipsException;
import com.meizhuo.etips.model.Lesson;
import com.meizhuo.etips.model.SchoolNews;
import com.meizhuo.etips.net.utils.SubSystemAPI;
import com.meizhuo.etips.net.utils.WYUNewsAPI;

public class MyTeszt extends AndroidTestCase {
	public MyTeszt() {

	}

	// course(courseName,time ,address ,teacher,week integer,classtime
	// integer)";
	public void DBadd() {
		CourseDAO dao = new CourseDAO(getContext());
		ContentValues cv = null;
		SubSystemAPI api = new SubSystemAPI("3112002722", "931127");
		List<List<List<Lesson>>> mmmlist = new ArrayList<List<List<Lesson>>>();
		List<List<Lesson>> mmlist = new ArrayList<List<Lesson>>();
		List<Lesson> mlist;
		try {
			api.login();
			Map<Integer, Map<Integer, List<Lesson>>> course = api.getLessons();
			for (int i = 1; i <= 7; i++) {
				Map<Integer, List<Lesson>> week = course.get(i);
				mmlist = new ArrayList<List<Lesson>>();
				for (int j = 1; j <= 5; j++) {
					List<Lesson> list = week.get(j);
					mlist = new ArrayList<Lesson>();
					for (Lesson l : list) {
						l.week = i;
						l.classtime = j;
						mlist.add(l);
						String lessonName = l.LessonName;
						String time = l.Time;
						String address = l.address;
						String teacher = l.Teacher;
						cv = new ContentValues();
						cv.put("lessonName", lessonName);
						cv.put("time", time);
						cv.put("address", address);
						cv.put("teacher", teacher);
						cv.put("week", i);
						cv.put("classtime", j);
						System.out.println(i * j + "--->" + dao.add(cv));

					}

					mmlist.add(mlist);
				}
				mmmlist.add(mmlist);

			}

		} catch (ClientProtocolException e) {

			e.printStackTrace();
		} catch (ETipsException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// cv.put("lessonName", "Math");
		// cv.put("time", "week1-9");
		// cv.put("address", "malanfnag");
		// cv.put("teacher", "Mr.T12");
		// cv.put("week", 1);
		// cv.put("classtime", 4);
		// System.out.println("__>>>>" + dao.add(cv));
		System.out.println("*******");
		System.out.println(((mmmlist.get(1)).get(1)).toString());
		System.out.println(((mmmlist.get(1)).get(2)).toString());
		System.out.println("*******");

	}

	public void DBupdate() {
		CourseDAO dao = new CourseDAO(getContext());
		// ContentValues cv = CourseDAO.getUniqueContentValues();
		ContentValues cv = new ContentValues();
		cv.put("lessonName", "高等英语3");
		System.out
				.println("__?>>>?>>>>"
						+ dao.update(
								cv,
								"lessonName = ? and time = ? and address = ? and teacher = ? and week = ? and classtime = ?",
								new String[] { "高等英语2", "第3-18周", "马兰芳教学楼401",
										"陈莉", "1", "1" }));
		// "lessonName = ? and time = ? and address = ? and teacher = ？ and week = ? and classtime = ?",
		// new String[] {"高等数学一（2）","第3-10周","马兰芳教学楼401","陈莉","5","1"}));
	}

	// "lessonName = ? and time = ? and address = ? and teacher = ？ and week = ? and classtime = ?"
	public void DBdelte() {
		CourseDAO dao = new CourseDAO(getContext());
		System.out.println("------>>>>"
				+ dao.delete("courseName = ?", new String[] { "Chinese" }));
	}

	public void DBquery() {
		CourseDAO dao = new CourseDAO(getContext());
		List<List<Lesson>> list = dao.query("week = ?", new String[] { "4" });
		System.out.println("size of list:" + list.size());
		for (List<Lesson> mlist : list) {
			System.out.println("*************");
			System.out.println("size of mlist =" + mlist.size());
			System.out.println(mlist.toString());
			System.out.println("*************");
		}

	}

	public void updata2() {
		CourseDAO dao = new CourseDAO(getContext());
		Context context = getContext();
		// A -> B 交换A版位置 C是temp
		List<List<Lesson>> course = dao.getLessonList("week = ?",
				new String[] { "5" });
		List<Lesson> list = course.get(0);
		ContentValues a = CourseDAO.getContentValuesByLesson(list.get(0));
		ContentValues b = CourseDAO.getContentValuesByLesson(list.get(1));
		System.out.println("++++++++");

		// String f1 = CourseDAO.getUniqueContentValues().toString();
		ContentValues cv = CourseDAO.getUniqueContentValues();
		System.out.println(cv.get("lessonName"));
		System.out.println(cv.get("time"));
		System.out.println(cv.get("address"));
		System.out.println(cv.get("teacher"));
		System.out.println(cv.get("week"));
		System.out.println(cv.get("classtime"));
		System.out.println("++++++++");
		String f2 = CourseDAO.getWhere();
		for (String f3 : CourseDAO.getwhereArgsByLesson(list.get(0)))
			System.out.println(f3);
		System.out.println("++++++++");
		if (dao.update(CourseDAO.getUniqueContentValues(),
				CourseDAO.getWhere(),
				CourseDAO.getwhereArgsByLesson(list.get(0)))) {// C覆盖A的位置

			System.out.println("success!!");
		} else {
			System.out.println("fuck!!!!!!!!!!!");
		}

	}

	public void htmlwrite() {
		Context c = getContext();
		File file = c.getFileStreamPath("test.html");
		FileOutputStream fou = null;
		try {
			fou = new FileOutputStream(file);
			String s = "lol";
			fou.write(s.getBytes());

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (fou != null)
				try {
					fou.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	public void fileOutByByte() {
		Context c = getContext();
		File file = c.getFileStreamPath("test.html");
		FileInputStream fin = null;
		ByteArrayOutputStream bout = null;
	
		StringBuffer sb = null;
		try {
			sb = new StringBuffer();
			byte[] buffer = new byte[1024];
			fin = new FileInputStream(file);
	      
			bout = new ByteArrayOutputStream();
			int len = 0;
			while ((len = fin.read(buffer)) != -1) {
				bout.write(buffer, 0, len);
			//	sb.append(new String(buffer, "utf-8"));
			}
			System.out.println(bout.toString("utf-8"));
			
		 

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			 
			e.printStackTrace();
		} finally {
			try {
				if (fin != null)
					fin.close();
				if (bout != null)
					bout.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void   fileOutByChar() {
		Context c = getContext();
		File file = c.getFileStreamPath("test.html");
		BufferedReader br = null;
		try {
			br= new BufferedReader(new FileReader(file));
			String s  = null;
			StringBuffer sb = new StringBuffer();
			while ((s=br.readLine())!=null){
				sb.append(s);
				System.out.println(s);
			}
			System.out.println(sb.toString());
			 
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	 
	public void wyunew(){
		WYUNewsAPI api = new WYUNewsAPI();
		try {
			List<SchoolNews> list = api.getSchoolNews(1);
			System.out.println(api.getSCtotalPage());
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
