package com.meizhuo.etips.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.meizhuo.etips.activities.R;
import com.meizhuo.etips.activities.R.string;
import com.meizhuo.etips.common.ETipsContants;
import com.meizhuo.etips.common.ETipsUtils;
import com.meizhuo.etips.model.MsgRecord;

public class MsgCenterDAO implements BaseDAO {
	private MsgCenterDBHelper dbHelper = null;
	private Context context;

	public MsgCenterDAO(Context context) {
		dbHelper = new MsgCenterDBHelper(context);
		this.context = context;
	}

	public static ContentValues getContentValues(MsgRecord m) {
		ContentValues cv = new ContentValues();
		cv.put("id", m.id);
		cv.put("content", m.content);
		cv.put("type", m.type);
		cv.put("addTime", m.addTime);
		return cv;
	}

	public List<MsgRecord> query(String selection, String[] selectionArgs) {
		SQLiteDatabase db = null;
		List<MsgRecord> list = new ArrayList<MsgRecord>();
		try {
			db = dbHelper.getReadableDatabase();
			Cursor c = db.query("msg", null, selection, selectionArgs, null,
					null, null);
			int len = c.getColumnCount();
			while (c.moveToNext()) {
				MsgRecord m = new MsgRecord();
				for (int i = 0; i < len; i++) {
					String key = c.getColumnName(i);
					String value = c.getString(c.getColumnIndex(key));
					if (value == null)
						value = "";
					if (key.equals("id"))
						m.id = Integer.parseInt(value);
					else if (key.equals("content"))
						m.content = value;
					else if (key.equals("type"))
						m.type = value;
					else if (key.equals("addTime"))
						m.addTime = value;
				}
				list.add(m);
			}
		} catch (Exception e) {

		} finally {
			if (db != null) {
				db.close();
			}
		}
		return list;
	}
	/**
	 * 全部查询
	 * @return  List<MsgRecord>
	 */
	public List<MsgRecord> queryAll(){
		return this.query(null, null);
	}

	@Override
	public boolean add(ContentValues cv) {
		SQLiteDatabase db = null;
		boolean flag = false;
		try {
			db = dbHelper.getWritableDatabase();
			long id = db.insert("msg", null, cv);
			flag = (id != -1 ? true : false);
		} catch (Exception e) {

		} finally {
			if (db != null) {
				db.close();
			}
		}
		return flag;
	}
	/**
	 * 必须添加这一条，就算清空了也要重新加入这条
	 * @return true if operate successfully
	 */
	public boolean addOne(){
		ContentValues cv =new ContentValues();
		//SharedPreferences sp  = context.getSharedPreferences(ETipsContants.SharedPreference_NAME, Context.MODE_PRIVATE);
		cv.put("id", 0);
		cv.put("content",context.getString(R.string.MsgCenterTips));
		cv.put("type", ETipsContants.TYPE_MsgCenter_System);
		cv.put("addTime", System.currentTimeMillis()+"");
		System.out.println((String)cv.get("addTime"));
		return add(cv);
	}
    /**
     * 删除时请注意，不能删除第0条，系统默认的
     * @param
     */
	@Override
	public boolean delete(String whereClause, String[] whereArgs) {
		boolean flag = false;
		SQLiteDatabase db = null;
		try {
			db = dbHelper.getWritableDatabase();
			int count = db.delete("msg", whereClause, whereArgs);
			flag = (count > 0 ? true : false);
		} catch (Exception e) {

		} finally {
			if (db != null) {
				db.close();
			}
		}
		return flag;
	}
    /**
     * 全部删除时请注意，不能删除第0条，系统默认的
     * 全部删除后必须调用addOne()
     */
	@Override
	public boolean deleteAll() {
		boolean flag = false;
		SQLiteDatabase db = null;
		try {
			db = dbHelper.getWritableDatabase();
			db.execSQL("delete from msg");
		    flag = true;			 
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (db != null)
				db.close();
		}
		return flag;
	}

	@Override
	public boolean update(ContentValues cv, String whereClause,
			String[] whereArgs) {
		boolean flag = false;
		SQLiteDatabase db = null;
		try {
			db = dbHelper.getWritableDatabase();
			int count = db.update("msg", cv, whereClause, whereArgs); // count
																		// 受影响的条数
			flag = (count > 0 ? true : false);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (db != null)
				db.close();
		}
		return flag;
	}
	
	/**
	 * 有多少行数据
	 * @return RowCount
	 */
    public synchronized int getRowCount(){
    	return queryAll().size();
    }
}
