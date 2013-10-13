package com.meizhuo.etips.db;

import android.content.ContentValues;

public interface BaseDAO {
	public boolean add(ContentValues cv);

	public boolean delete(String whereClause, String[] whereArgs);

	public boolean deleteAll();

	public boolean update(ContentValues cv, String whereClause,
			String[] whereArgs);
 

}
