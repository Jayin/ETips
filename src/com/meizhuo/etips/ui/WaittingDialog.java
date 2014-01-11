package com.meizhuo.etips.ui;

import com.meizhuo.etips.activities.R;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;

public class WaittingDialog extends Dialog  {
	private TextView tv;

	public WaittingDialog(Context context) {
		super(context,R.style.BaseDialog);
		this.setCancelable(true);
	 
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_progress_waitting);
		tv = (TextView) this.findViewById(R.id.dialog_progress_waitting_tv);
	}

	public void setText(String text) {
		if (tv != null)
			tv.setText(text);
	}

}
