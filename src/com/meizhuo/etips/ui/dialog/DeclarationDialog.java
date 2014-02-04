package com.meizhuo.etips.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.meizhuo.etips.activities.R;
import com.meizhuo.etips.ui.base.BaseDialog;

public class DeclarationDialog extends BaseDialog {
	private View v;

	public DeclarationDialog(Context context) {
		super(context);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_topiclsit);
		v = findViewById(R.id.dialog_topiclist_ok);
		v.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				  DeclarationDialog.this.dismiss();
			}
		});
	}
}
