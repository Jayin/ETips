package com.meizhuo.etips.ui.utils;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.meizhuo.etips.activities.R;

public class BottomPopupWindow  {
	protected View anchor, root;
	protected int ResourceId;
	protected Context context;
	protected int[] location = new int[2];
	protected PopupWindow window;

	public BottomPopupWindow(View anchor, int ResourceId, Context context) {
		this.anchor = anchor;
		this.context = context;
		this.root = LayoutInflater.from(context).inflate(ResourceId, null);
		init();
		onCreate();
	}

	public BottomPopupWindow(View anchor, View root, Context context) {
		this.anchor = anchor;
		this.context = context;
		this.root = root;
		init();
		onCreate();
	}

	private void init() {
		this.window = new PopupWindow(context);
		anchor.getLocationOnScreen(location);
		this.window.setContentView(root);
		this.window.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        this.window.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
		this.window.setFocusable(true);
		this.window.setOutsideTouchable(true);
		this.window.setBackgroundDrawable(new ShapeDrawable());
		this.window.setAnimationStyle(R.style.Animations_popup_right);
	}

	/**
	 * 在这里初始化layout 绑定监听器。。
	 */
	protected void onCreate() {

	}

	/**
	 * 从底部展出
	 */
	public void show() {
		this.window.showAtLocation(anchor, Gravity.NO_GRAVITY, location[0],
				location[1] - this.window.getHeight());
	}

	/**
	 * 设置动画
	 */
	public void setAnimationStyle(int ResourceId) {
		this.window.setAnimationStyle(ResourceId);
	}

	/**
	 * 获得contentView
	 */
	public View getContentView() {
		return this.root;
	}

	public void dismiss() {
		this.window.dismiss();
	}
}
