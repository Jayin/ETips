package com.meizhuo.etips.db;

import com.meizhuo.etips.common.ETipsContants;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MsgCenterDBHelper extends SQLiteOpenHelper {
	public MsgCenterDBHelper(Context context) {
		super(context, ETipsContants.DB_NAME_MsgCenter, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table msg(id integer primary key,type varchar(16),content varchar(256),addTime varchar(32))";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
