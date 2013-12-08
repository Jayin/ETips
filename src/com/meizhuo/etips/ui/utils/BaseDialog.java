package com.meizhuo.etips.ui.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.*;

import com.meizhuo.etips.activities.R;
/**
 * 应该复写：setContentView()->initLayout() ->onWork()、选择：onClick()；
 * @author Jayin Ton
 */
public class BaseDialog extends Dialog implements View.OnClickListener {

	public BaseDialog(Context context) {
		super(context, R.style.BaseDialog);
		
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView();
		initLayout();
		onWork();
	}
	/**
	 * to do some work here
	 */
    private void onWork() {
	  
		
	}
	/**
     *init layout
     */
	protected void initLayout() {
		 
	}
    /**
     * set Content View to Dialog
     */
	protected void setContentView(){
		
	}
	@Override
	public void onClick(View v) {
			
	}
	 
	 
}
