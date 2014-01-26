package com.meizhuo.etips.model;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * useful sub-class to remind you what should work
 * 
 * @author Jayin Ton
 * 
 */
public abstract class ResponseHandler extends AsyncHttpResponseHandler {

	@Override
	public abstract void onFailure(int statusCode, Header[] headers,
			byte[] data, Throwable err);

	@Override
	public abstract void onSuccess(int statusCode, Header[] headers, byte[] data);
}
