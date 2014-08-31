package com.meizhuo.etips.activities;

import java.io.Serializable;

import butterknife.ButterKnife;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;
/**
 * 所有Activity的基类
 * @author Jayin Ton
 * @version 2.2
 * @since 2014.1.31
 * 
 * change a lot
 * @author Jayin 
 * @version 3.0.0
 */
public abstract class BaseActivity extends FragmentActivity {
	 
	protected abstract void initData();

	protected abstract void initLayout();
	
	protected void onCreate(Bundle savedInstanceState,int layoutResId) {
		super.onCreate(savedInstanceState);
		setContentView(layoutResId);
		ButterKnife.inject(this);
		initData();
		initLayout();
	}

	/**
	 * 
	 * 以一个Class<?> cls 启动一个Activity
	 * @param cls
	 */
	public void openActivity(Class<?> cls) {
		this.startActivity(new Intent(this,cls));
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
	 * 
	 * @return
	 */
	public Context getContext() {
		return this;
	}
	/**
	 * 调试
	 * @param content
	 */
	public void debug(String content){
		 Log.i("debug",this.getClass().getName()+":"+content);
	}
	
    /**
     * Get intent extra
     *
     * @param name
     * @return serializable
     */
    @SuppressWarnings("unchecked")
    protected <V extends Serializable> V getSerializableExtra(final String name) {
        return (V) getIntent().getSerializableExtra(name);
    }

    /**
     * Get intent extra
     *
     * @param name
     * @return int -1 if not exist!
     */
    protected int getIntExtra(final String name) {
        return getIntent().getIntExtra(name, -1);
    }

    /**
     * Get intent extra
     *
     * @param name
     * @return string
     */
    protected String getStringExtra(final String name) {
        return getIntent().getStringExtra(name);
    }

    /**
     * Get intent extra
     *
     * @param name
     * @return string array
     */
    protected String[] getStringArrayExtra(final String name) {
        return getIntent().getStringArrayExtra(name);
    }

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
	@Override
	protected void onResume() {
		super.onResume();
	}
}
