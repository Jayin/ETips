package com.meizhuo.etips.common;

import android.util.Log;

public class Elog {
	public static void i(String content) {
		Log.i("debug", "******");
		Log.i("debug", content);
		Log.i("debug", "******");
	}
}
