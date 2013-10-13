package com.meizhuo.etips.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

public abstract class BaseUIActivity extends Activity {
	/**
	 * 初始化Layout UI 等等
	 */
	protected abstract void initLayout();

	/**
	 * 初始化数据
	 */
	protected abstract void initData();

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	/**
	 * 以一个Class<?> cls启动一个Activity
	 * 
	 * @param cls
	 */
	public void openActivity(Class<?> cls) {
		this.startActivity(new Intent(this, cls));
	}

	/**
	 * 以一个intent来启动一个Activity
	 * 
	 * @param intent
	 */
	public void openActivity(Intent intent) {
		this.startActivity(intent);
	}

	/**
	 * 封装了Toast，直接toast（String content）
	 * 
	 * @param content
	 *            content of your want to Toast
	 */
	public void toast(String content) {
		Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 轻量封装findViewById(int id)
	 * 
	 * @param id
	 * @return
	 */
	public View _getView(int id) {
		return this.findViewById(id);
	}

	/**
	 * 用class 包装一个Intent;
	 * 
	 * @param cls
	 * @return
	 */
	public Intent wrapIntent(Class<?> cls) {
		return new Intent(this, cls);
	}

	/**
	 * 关闭Activity
	 */
	public void closeActivity() {
		this.finish();
	}
	/**
	 * 获得这个Activity的Context
	 * @return
	 */
	public Context getContext(){
		return this;
	}
}
