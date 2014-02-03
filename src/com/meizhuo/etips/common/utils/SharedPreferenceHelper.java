package com.meizhuo.etips.common.utils;

import java.util.Calendar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class SharedPreferenceHelper {
	public static boolean set(SharedPreferences sp, String key, String value) {
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(key, value);
		return editor.commit();
	}

	/**
	 * 设置
	 * 
	 * @param sp
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean set(SharedPreferences sp, String key, int value) {
		return set(sp, key, String.valueOf(value));
	}

	/**
	 * 获得SharedPreference_NAME = "ETipsSharedPreference"
	 * 
	 * @return
	 */
	public static SharedPreferences getSharedPreferences(Context context) {
		return context.getSharedPreferences(
				ETipsContants.SharedPreference_NAME, Context.MODE_PRIVATE);
	}
}
