package com.meizhuo.etips.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

/**
 * 文件操作工具类
 * 
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
	 * 
	 * @param context
	 *            上下文
	 * @param fileName
	 *            文件名
	 * @return 序列化对象
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
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
				}
			}
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * 向手机写图片
	 * 
	 * @param buffer
	 * @param folderPath
	 *            like -> mnt/sdcar/img/
	 * @param fileName
	 *            pic.png
	 * @return
	 */
	public static boolean writeFile(byte[] buffer, String folderPath,
			String fileName) {
		boolean writeSucc = false;
		File fileDir = new File(folderPath);
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		}

		File file = new File(folderPath + fileName);
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			out.write(buffer);
			writeSucc = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return writeSucc;
	}

	/**
	 * 获取文件大小
	 * 
	 * @param filePath
	 * @return
	 */
	public static long getFileSize(String filePath) {
		long size = 0;

		File file = new File(filePath);
		if (file != null && file.exists()) {
			size = file.length();
		}
		return size;
	}

	/**
	 * 获取目录文件大小
	 * 
	 * @param dir
	 * @return size 字节
	 */
	public static long getDirSize(File dir) {
		if (dir == null) {
			return 0;
		}
		if (!dir.isDirectory()) {
			return 0;
		}
		long dirSize = 0;
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				dirSize += file.length();
			} else if (file.isDirectory()) {
				dirSize += file.length();
				dirSize += getDirSize(file); // 递归调用继续统计
			}
		}
		return dirSize;
	}

	/**
	 * 新建目录
	 * 
	 * @param directoryName
	 * @return
	 */
	public static boolean createDirectory(String directoryName) {
		boolean status;
		if (!directoryName.equals("")) {
			File path = Environment.getExternalStorageDirectory();
			File newPath = new File(path.toString() + directoryName);
			status = newPath.mkdir();
			status = true;
		} else
			status = false;
		return status;
	}

	/**
	 * 删除目录(包括：目录里的所有文件)
	 * 
	 * @param directoryName  dir 被删除目录的文件路径 
	 * @return 目录删除成功返回true,否则返回false  
	 */
	public static boolean deleteDirectory(String directoryPath) {
		boolean flag = true;
		if (directoryPath.equals(""))
			return false;
		// 如果dir不以文件分隔符结尾，自动添加文件分隔符
		if (!directoryPath.endsWith(File.separator))
			directoryPath += File.separator;
		File dirFile = new File(directoryPath);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		File[] files = dirFile.listFiles();
		for (File f : files) {
			if (f.isFile()) { // 删除子文件
				flag = deleteFile(f.getAbsolutePath());
				if (!flag)
					break;
			} else { // 删除子目录
				flag = deleteDirectory(f.getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag)
			return false;
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param fileName
	 * @return
	 */
	public static boolean deleteFile(String fileName) {
		File file = new File(fileName);
		if (file.isFile() && file.exists()) {
			file.delete();
			return true;
		} else {
			return false;
		}
	}
}
