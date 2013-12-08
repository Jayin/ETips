package com.meizhuo.etips.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.meizhuo.etips.model.Lesson;

public class CourseDAO {
	private CourseDBHelper dbhelper;

	public CourseDAO(Context context) {
		dbhelper = new CourseDBHelper(context);
	}

	/**
	 * ContentValues a wrapper of key-value(columu-row)
	 * 
	 * @param cv
	 * @return true if operarte successfully
	 */
	public boolean add(ContentValues cv) {
		boolean flag = false;
		SQLiteDatabase db = null;
		long id = -1;
		try {
			db = dbhelper.getWritableDatabase();
			id = db.insert("course", null, cv);
			flag = (id != -1 ? true : false);
		} catch (Exception e) {

		} finally {
			if (db != null)
				db.close();
		}
		return flag;
	}

	/**
	 * it looks like a split SQL string delete from XXXX where age = 18 and id =
	 * 4 xxxx means table so:
	 * 
	 * @param whereClause
	 *            age = ? and id = ?
	 * @param whereArgs
	 *            String[]{"18","4"}
	 * @return true if operarte successfully
	 */
	public boolean delete(String whereClause, String[] whereArgs) {
		boolean flag = false;
		SQLiteDatabase db = null;
		try {
			db = dbhelper.getWritableDatabase();
			int count = db.delete("course", whereClause, whereArgs); // count
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
	 * clean up the table
	 * 
	 * @return true if execute successfully
	 */
	public boolean deleteAll() {
		boolean flag = false;
		SQLiteDatabase db = null;
		try {
			db = dbhelper.getWritableDatabase();
			db.execSQL("delete from course");
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (db != null)
				db.close();
		}
		return flag;
	}

	/**
	 * see delete()
	 * 
	 * @param cv
	 *            a map of key-value you want to set
	 * @param whereClause
	 * @param whereArgs
	 * @return
	 */
	public boolean update(ContentValues cv, String whereClause,
			String[] whereArgs) {
		boolean flag = false;
		SQLiteDatabase db = null;
		try {
			db = dbhelper.getWritableDatabase();
			int count = db.update("course", cv, whereClause, whereArgs); // count
																			// //
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
	 * 获得某日的课程 like whereClause and whereArgs
	 * 
	 * @param selection
	 * @param selectionArgs
	 * @return
	 */
	public List<List<Lesson>> query(String selection, String[] selectionArgs) {
		List<List<Lesson>> list = new ArrayList<List<Lesson>>();
		SQLiteDatabase db = null;

		int count = 0;
		try {
			db = dbhelper.getWritableDatabase();
			Cursor c = db.query("course", null, selection, selectionArgs, null,
					null, null);
			List<Lesson> mlist = null;
			Lesson l;
			int c_len = c.getColumnCount();
			String lastString = null;
			while (c.moveToNext()) {
				count++;
				l = new Lesson();
				String classtime = null;
				for (int i = 0; i < c_len; i++) {
					String key = c.getColumnName(i);
					String value = c.getString(c.getColumnIndex(key));
					if (value == null) {
						value = "";
					}
					if (key.equals("lessonName"))
						l.LessonName = value;
					else if (key.equals("time"))
						l.Time = value;
					else if (key.equals("address"))
						l.address = value;
					else if (key.equals("teacher"))
						l.Teacher = value;
					else if (key.equals("classtime")) {
						classtime = value;
						l.classtime = Integer.parseInt(value);
					} else if (key.equals("week"))
						l.week = Integer.parseInt(value);

				}
				if (lastString == null) {
					lastString = classtime;
					mlist = new ArrayList<Lesson>();
					mlist.add(l);
				} else if (lastString != null && !lastString.equals(classtime)) {
					lastString = classtime;
					list.add(mlist);
					mlist = new ArrayList<Lesson>();
					mlist.add(l);
				} else if (lastString != null && lastString.equals(classtime)) {
					mlist.add(l);
				}
			}
			list.add(mlist);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (db != null)
				db.close();
		}
		System.out.println(count);
		return list;
	}

	/**
	 * 获得某日的课程
	 * 
	 * @param selection
	 *            week = ?
	 * @param selectionArgs
	 *            i = 1 to 7
	 * @return 某日的课程
	 */
	public List<List<Lesson>> getLessonList(String selection,
			String[] selectionArgs) {
		return query(selection, selectionArgs);
	}

	/**
	 * 获得一周的课程
	 * 
	 * @return List<List<List<Lesson>>> course
	 */
	public List<List<List<Lesson>>> getCourse() {
		List<List<List<Lesson>>> course = new ArrayList<List<List<Lesson>>>();
		for (int i = 1; i <= 7; i++) {
			course.add(this.getLessonList("week = ?",
					new String[] { String.valueOf(i) }));
		}
		return course;
	}
    /**
     * 查询某一课时的课程
     * eg:
     * selection: "week = ? and classtime = ?"
     * selectionArgs: new String[]{"5","1"}; 周五第一节
     * @param selection
     * @param selectionArgs
     * @return List of lesson
     */
	public List<Lesson> getLessonByClassTime(String selection,
			String[] selectionArgs) {
		List<Lesson> list = new ArrayList<Lesson>();
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		try {
			Cursor c = db.query("course", null, selection, selectionArgs, null,
					null, null);
			while (c.moveToNext()) {
				int ColumnCount = c.getColumnCount();
				Lesson l = new Lesson();
				for (int i = 0; i < ColumnCount; i++) {
					String key = c.getColumnName(i);
					String value = c.getString(c.getColumnIndex(key));
					if (value == null) {
						value = "";
					}
					if (key.equals("lessonName"))
						l.LessonName = value;
					else if (key.equals("time"))
						l.Time = value;
					else if (key.equals("address"))
						l.address = value;
					else if (key.equals("teacher"))
						l.Teacher = value;
					else if (key.equals("classtime")) {
						l.classtime = Integer.parseInt(value);
					} else if (key.equals("week"))
						l.week = Integer.parseInt(value);
				}
				list.add(l);
			}
		} catch (Exception e) {
			return list;
		}finally{
			if(db!=null){
				db.close();
			}
		}
		return list;
	}

	/**
	 * 通过一个Lesson对象构造一个CV
	 * 
	 * @param lesson
	 * @return
	 */
	public static ContentValues getContentValuesByLesson(Lesson lesson) {
		ContentValues cv = new ContentValues();
		cv.put("lessonName", lesson.LessonName);
		cv.put("time", lesson.Time);
		cv.put("address", lesson.address);
		cv.put("teacher", lesson.Teacher);
		cv.put("week", lesson.week);
		cv.put("classtime", lesson.classtime);
		return cv;
	}

	/**
	 * 构造一个唯一的ContentValues 用于表中的行的交换 确保这条ContentValues 在现实中不可能存在
	 * 
	 * @return 唯一的ContentValues
	 */
	public static ContentValues getUniqueContentValues() {
		ContentValues cv = new ContentValues();
		cv.put("lessonName", "EtipsClass");
		cv.put("time", "EtipsTime");
		cv.put("address", "ETipsAddress");
		cv.put("teacher", "ETipsTeacher");
		cv.put("week", 9);
		cv.put("classtime", 9);
		return cv;
	}

	/**
	 * 删除getUniqueContentValues()构造的唯一ContentValues
	 * 
	 * @return true if it operates successfully
	 */
	public boolean deleteUniqueContentValues() {
		return delete(getWhere(), new String[] { "EtipsClass", "EtipsTime",
				"ETipsAddress", "ETipsTeacher", "9", "9" });
	}

	/**
	 * 添加getUniqueContentValues()构造的唯一ContentValues
	 * 
	 * @return true if it operates successfully
	 */
	public boolean addUniqueContentValues() {
		return add(getUniqueContentValues());
	}

	/**
	 * 表中的所有属性 构造一个where 注意：是所有 ！
	 * 
	 * @return where
	 */
	public static String getWhere() {
		return "lessonName = ? and time = ? and address = ? and teacher = ? and week = ? and classtime = ?";

	}

	/**
	 * 
	 * @return 唯一ContentValues 的whereArgs
	 */
	public static String[] getwhereArgs() {
		return new String[] { "EtipsClass", "EtipsTime", "ETipsAddress",
				"ETipsTeacher", "9", "9" };
	}

	/**
	 * 用Lesson的所有属性值 的构造一个whereArgs 注意： 是所有！
	 * 
	 * @return whereArgs
	 */
	public static String[] getwhereArgsByLesson(Lesson l) {
		return new String[] { l.LessonName, l.Time, l.address, l.Teacher,
				String.valueOf(l.week), String.valueOf(l.classtime) };
	}

}
