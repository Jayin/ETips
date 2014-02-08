package com.meizhuo.etips.service;

import java.io.File;

import org.apache.http.Header;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.meizhuo.etips.app.ImgSwitchInfo;
import com.meizhuo.etips.common.ETipsContants;
import com.meizhuo.etips.common.FileUtils;
import com.meizhuo.etips.common.StringUtils;
import com.meizhuo.etips.model.ImgInfo;

/**
 * 
 * @author Jayin Ton
 * 
 */
public class ETipsCoreService extends Service {
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i("debug", "onCreate");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("debug", "onStartCommand");
		String action = intent.getAction();
		if (ETipsContants.Action_Service_Download_Pic.equals(action)) {
			downloadPic(intent);
		}
		return Service.START_STICKY;
	}

	//
	private void downloadPic(final Intent intent) {
		// get info
		long displayTime = intent.getLongExtra("displayTime",
				System.currentTimeMillis());
		String description = intent.getStringExtra("description") == null ? ""
				: intent.getStringExtra("description");
		int continuance = intent.getIntExtra("continuance", 1);
		final String url = intent.getStringExtra("url");

		final ImgInfo info = ImgSwitchInfo.getImgInfo(getApplicationContext());
		info.setContinuance(continuance);
		info.setDescription(description);
		info.setDisplayTime(displayTime);
		info.setUrl(url);
		info.setName(StringUtils.getFileNameFromUrl(url));
		if (url == null) {
			Log.e(ETipsContants.Debug, "ETipsCoreService::url is null");
		} else {
			final AsyncHttpClient client = new AsyncHttpClient();
			client.get(url, new AsyncHttpResponseHandler() {
				public void onSuccess(int statusCode, Header[] headers,
						byte[] data) {
					FileUtils.writeFile(data, ImgSwitchInfo
							.getImgSavePath(getApplicationContext()),
							StringUtils.getFileNameFromUrl(url));
					info.setDownloaded(true);
					ImgSwitchInfo.setImgInfo(getApplicationContext(), info);
				 
					Log.i(ETipsContants.Debug,
							"ETipsCoreService::download finish");
				};

				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] data, Throwable err) {
					info.setDownloaded(false);
					ImgSwitchInfo.setImgInfo(getApplicationContext(), info);
					Log.e(ETipsContants.Debug,
							"ETipsCoreService::download faild ! error_code ="
									+ statusCode);
				}
			});
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
