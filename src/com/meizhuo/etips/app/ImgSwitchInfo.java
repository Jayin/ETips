package com.meizhuo.etips.app;

import java.io.File;

import android.content.Context;

import com.meizhuo.etips.common.CalendarUtils;
import com.meizhuo.etips.common.DataPool;
import com.meizhuo.etips.common.FileUtils;
import com.meizhuo.etips.common.L;
import com.meizhuo.etips.model.ImgInfo;

/**
 * 处理图片切换的管理类
 * 
 * @author Jayin Ton
 * 
 */
public class ImgSwitchInfo {
	public static final String DP_Name_ImgSwitchInfo = "ImgSwitchInfo";
	public static final String DP_key_ImgInfo = "imgInfo";

	/** 获得图片保存位置:files/img/ */
	public static String getImgSavePath(Context context) {
		return context.getFilesDir().getAbsolutePath() + File.separator + "img"
				+ File.separator;
	}

	/** 获得图片信息 */
	public static ImgInfo getImgInfo(Context context) {
		DataPool dp = new DataPool(DP_Name_ImgSwitchInfo, context);
		ImgInfo res = (ImgInfo) dp.get(DP_key_ImgInfo);
		return res == null ? new ImgInfo() : res;
	}

	/** 保存图片信息 */
	public static boolean setImgInfo(Context context, ImgInfo info) {
		return new DataPool(DP_Name_ImgSwitchInfo, context).put(DP_key_ImgInfo,
				info);
	}

	/**
	 * 判断是否应该展示图片，如果图片已过期 则删除。
	 * 
	 * @param context
	 * @return true if should display
	 */
	public static boolean shouldDisplayImg(Context context) {
		ImgInfo img = getImgInfo(context);
		if (!img.isDownloaded())
			return false;
		int res = CalendarUtils.isInSameDay(img.getDisplayTime(),
				System.currentTimeMillis())
				+ CalendarUtils.isInSameDay(
						img.getDisplayTime() + (img.getContinuance() - 1) * 24
								* 60 * 60 * 1000, System.currentTimeMillis());
		if (res > -2 && res < 2) { // 处于显示期
			return true;
		} else {
			if (res == -2) {// 已过期 ,应该删除
				if (FileUtils.deleteDirectory(getImgSavePath(context))){
					setImgInfo(context, new ImgInfo());
					L.i("delete ok");
				}else{
					L.i("delete faild");
				}
				
			} else { // 还没有到
						// nothing to do
			}
		}
		return false;
	}

}
