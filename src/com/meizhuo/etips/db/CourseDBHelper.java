package com.meizhuo.etips.db;

import com.meizhuo.etips.common.utils.ETipsContants;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CourseDBHelper extends SQLiteOpenHelper {

	public CourseDBHelper(Context context) {
		super(context, ETipsContants.DB_NAME, null, 1);
	}

	// getdb()时,数据库才会创建 。。。这里
	// 当数据库创建的时候，这里才是第一次执行
	// 在这里创建数据库
	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table course(lessonName varchar(64),time varchar(64),address varchar(64),teacher varchar(64),week integer,classtime integer)";
		db.execSQL(sql);

	}

	// version 增加时会调用
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		 
	}

}
