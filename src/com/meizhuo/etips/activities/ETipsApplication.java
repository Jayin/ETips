package com.meizhuo.etips.activities;

import java.util.List;

import android.app.Application;
import android.content.Intent;

import com.meizhuo.etips.common.utils.DataPool;
import com.meizhuo.etips.common.utils.ETipsContants;
import com.meizhuo.etips.common.utils.Elog;
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
		MobclickAgent.setDebugMode(true);
	}
	/**
	 * @return the lessonList
	 */
	public List<List<List<Lesson>>> getLessonList() {
		Course course = null;
		DataPool dp = new DataPool(ETipsContants.SP_NAME_Course,
				getApplicationContext());
		course = (Course) dp.get("course");
		if (course == null)
			return null;
		return course.getCourseList();

	}

	/**
	 * @param lessonList
	 *            把课程添加到sharedpreference;
	 */
	public void setLessonList(List<List<List<Lesson>>> lessonList) {
		Course course = new Course(lessonList);
		DataPool dp = new DataPool(ETipsContants.SP_NAME_Course,
				getApplicationContext());
		if (dp.put("course", course)) {
			Elog.i("App:setLessonList  successfully");
			getApplicationContext().sendBroadcast(
					new Intent(ETipsContants.Action_CourseChange)); // 发送课表修改的广播！
		} else {
			Elog.i("App:setLessonList  faild");
		}
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
