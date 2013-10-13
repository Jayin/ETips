package com.meizhuo.etips.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;

import android.content.Context;
/**
 * 文件操作工具类
 * @author Jayin Ton
 *
 */
public class FileUtils {
	/**
	 * 保存对象 到文件
	 * 
	 * @param context
	 *            上下文
	 * @param targetObj
	 *            要保存的对象
	 * @param fileName
	 *            保存到指定的文件的文件名
	 * @return true if operate successfully
	 */
	public static boolean saveObject(Context context, Serializable targetObj,
			String fileName) {
		boolean flag = false;
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(targetObj);
			oos.flush();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
				}
			}
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
				}
			}
		}

	}
    /**
     * 读取对象
     * @param context 上下文
     * @param fileName 文件名
     * @return  序列化对象
     */
	public static Serializable readObject(Context context, String fileName) {
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = context.openFileInput(fileName);
			ois = new ObjectInputStream(fis);
			return (Serializable) ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
			// 反序列化失败 - 删除缓存文件
			if (e instanceof InvalidClassException) {
				File data = context.getFileStreamPath(fileName);
				data.delete();
			}
			return null;
		}finally{
			if(fis!=null){
				try {
					fis.close();
				} catch (IOException e) { }
			}
			if(ois !=null){
				try {
					ois.close();
				} catch (IOException e) { }
			}
		}

	}
}
