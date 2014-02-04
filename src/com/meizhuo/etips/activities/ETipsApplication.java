package com.meizhuo.etips.activities;

import java.util.List;

import android.app.Application;

import com.meizhuo.etips.app.AppInfo;
import com.meizhuo.etips.common.JPushManager;
import com.meizhuo.etips.model.Course;
import com.meizhuo.etips.model.Lesson;
import com.meizhuo.etips.net.utils.LibraryAPI;
import com.meizhuo.etips.net.utils.SubSystemAPI;
import com.umeng.analytics.MobclickAgent;

/**
 *  
 * 移除property
 * @since version 2.2
 * @version 2.2
 * @author Jayin Ton
 * 
 */
public class ETipsApplication extends Application {
	/** LibraryAPI 的缓存 */
	private LibraryAPI libraryAPI;
	/**  SubSystemAPI 的缓存 */
	private SubSystemAPI subSystemAPI;
	// 开启调试模式
	@Override
	public void onCreate() {
		super.onCreate();
		JPushManager.init(getApplicationContext());
		MobclickAgent.setDebugMode(true);
	}
	/**
	 * @return the lessonList
	 */
	public List<List<List<Lesson>>> getLessonList() {
		Course course = AppInfo.getCourse(this);
		if (course == null)
			return null;
		return course.getCourseList();

	}

	/**
	 * @param lessonList
	 *            把课程添加到sharedpreference;
	 */
	public void setLessonList(List<List<List<Lesson>>> lessonList) {
		AppInfo.setCourse(this,  new Course(lessonList));
	}


	/**
	 * @return the libraryAPI
	 */
	public LibraryAPI getLibraryAPI() {
		return libraryAPI;
	}

	/**
	 * @param libraryAPI
	 *            the libraryAPI to set
	 */
	public void setLibraryAPI(LibraryAPI libraryAPI) {
		this.libraryAPI = libraryAPI;
	}

	/**
	 * @return the subSystemAPI
	 */
	public SubSystemAPI getSubSystemAPI() {
		return subSystemAPI;
	}

	/**
	 * @param subSystemAPI
	 *            the subSystemAPI to set
	 */
	public void setSubSystemAPI(SubSystemAPI subSystemAPI) {
		this.subSystemAPI = subSystemAPI;
	}

}
