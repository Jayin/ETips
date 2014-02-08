package com.meizhuo.etips.ui.dialog;

import com.meizhuo.etips.activities.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * 新版的loading dialog
 * @author Jayin Ton
 *@version 2.2
 */
public class LoadingDialog extends Dialog {
	private String loadingText = "ETips正努力加载中...";
	private boolean cancelable = false;
	private Context context;

	public LoadingDialog(Context context) {
		super(context, R.style.BaseDialog);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.dialog_loading, null);// 得到加载view
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
		// main.xml中的ImageView
		ImageView outerImg = (ImageView) v.findViewById(R.id.loading_img_outer);
		ImageView innerImg = (ImageView) v.findViewById(R.id.loading_img_inner);
		// 加载动画
		Animation outerRotateAnimation = new RotateAnimation(0, 360,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		outerRotateAnimation.setRepeatCount(-1);
		outerRotateAnimation.setDuration(1000);
		outerRotateAnimation.setInterpolator(new LinearInterpolator());
		Animation innerRotateAnimation = new RotateAnimation(360, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		innerRotateAnimation.setRepeatCount(-1);
		innerRotateAnimation.setDuration(1300);
		innerRotateAnimation.setInterpolator(new LinearInterpolator());
		// 使用ImageView显示动画
		outerImg.startAnimation(outerRotateAnimation);
		innerImg.startAnimation(innerRotateAnimation);

		TextView loadingTextView = (TextView) v.findViewById(R.id.loading_text);
		loadingTextView.setText("" + loadingText);

		 
        this.setContentView(v);
		this.setCancelable(cancelable);// 不可以用“返回键”取消
		this.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
	}

	/** 设置提醒文字 */
	public void setLodingText(String text) {
		this.loadingText = text;
	}

	@Override
	public void setCancelable(boolean cancelable) {
		this.cancelable =cancelable;
		super.setCancelable(cancelable);
	}
}
