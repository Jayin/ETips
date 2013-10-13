package com.meizhuo.etips.activities;

import java.util.HashMap;
import java.util.List;

import android.app.Application;

import com.meizhuo.etips.common.utils.SharedPreferenceHelper;
import com.meizhuo.etips.model.Lesson;
import com.meizhuo.etips.net.utils.LibraryAPI;
import com.meizhuo.etips.net.utils.SubSystemAPI;
import com.umeng.analytics.MobclickAgent;

/**
 * 1、application用于初始化，载入用户偏好设置 sharedpreference: 默认所有偏好设置为NO 后期自行修改~
 * 2、每当往sharedpreference中增加一个属性，Property也应该增加一个属性
 * 3、每次修改以后（写入到）SharedPreferences文件后，必须refreshProperty()
 * @author Jayin Ton
 * 
 */
public class ETipsApplication extends Application {
	private HashMap<String, String> property; // 用户偏好设置类
	private List<List<List<Lesson>>> lessonList;
	/***
	 * 用于任意Actvity间的数据传递 利用这个来传递数据时，必须有非常明白数据的生命周期 及时做好内存释放！！不然debug很难，有点破坏逻辑！
	 */
	private Object object = null;
	/**
	 * LibraryAPI 的缓存
	 */
	private LibraryAPI libraryAPI;
	/**
	 * SubSystemAPI 的缓存
	 */
	private SubSystemAPI subSystemAPI;

	// private ArrayList<Activity> activities;
   //开启调试模式
	@Override
	public void onCreate() {
		super.onCreate();
	 	//MobclickAgent.setDebugMode( true );
	}
	/**
	 * 每次修改以后（写入到）SharedPreferences文件后，必须refreshProperty()
	 * @return
	 */
    public HashMap<String, String> refreshProperty(){
    	return this.property = (HashMap<String, String>)SharedPreferenceHelper.getSharedPreferences(this).getAll();
    }

	public HashMap<String, String> getProperty() {
		if (property == null) {
			return new HashMap<String, String>();
		}
		return property;
	}

	public HashMap<String, String> setProperty(HashMap<String, String> property) {
		return this.property = property;
	}

	/**
	 * @return the lessonList
	 */
	public List<List<List<Lesson>>> getLessonList() {
		return lessonList;
	}

	/**
	 * @param lessonList
	 *            the lessonList to set
	 */
	public void setLessonList(List<List<List<Lesson>>> lessonList) {
		this.lessonList = lessonList;
	}

	/**
	 * @return the object
	 */
	public Object getObject() {
		return object;
	}

	/**
	 * @param object
	 *            the object to set
	 */
	public void setObject(Object object) {
		this.object = object;
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
