package com.meizhuo.etips.ui.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import com.meizhuo.etips.activities.R;
import com.meizhuo.etips.common.ETipsUtils;
import com.meizhuo.etips.ui.base.BaseDialog;
/**
 * 设置当前手指
 * @since version 2.2 从Activity中移除出来
 * @author Jayin Ton
 *
 */
public class SetCurrentWeekDialog extends BaseDialog {
	private Button okBtn, cancleBtn;
	private ImageButton decBtn, plusBtn;
	private TextView tv,tv_handler;
	private int mCurrentWeek;
	private Context context;

	public SetCurrentWeekDialog(Context context,TextView handlerTV) {
		super(context);
		this.context = context;
		mCurrentWeek = ETipsUtils.getCurrentWeek(context);
		tv_handler = handlerTV;
	}

	@Override
	protected void initLayout() {
		decBtn = (ImageButton) this
				.findViewById(R.id.dialog_setting_currentweek_decrease);
		decBtn.setOnClickListener(this);
		plusBtn = (ImageButton) this
				.findViewById(R.id.dialog_setting_currentweek_plus);
		plusBtn.setOnClickListener(this);
		okBtn = (Button) this.findViewById(R.id.dialog_setting_currentweek_ok);
		okBtn.setOnClickListener(this);
		cancleBtn = (Button) this
				.findViewById(R.id.dialog_setting_currentweek_cancle);
		cancleBtn.setOnClickListener(this);
		tv = (TextView) this.findViewById(R.id.dialog_setting_currentweek_week);
		tv.setText(mCurrentWeek + "");
	}

	@Override
	protected void setContentView() {
		this.setContentView(R.layout.dialog_setting_currentweek);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_setting_currentweek_decrease:
				mCurrentWeek--;
				tv.setText(mCurrentWeek + "");
			break;
		case R.id.dialog_setting_currentweek_plus:
			mCurrentWeek++;
			tv.setText(mCurrentWeek + "");
			break;
		case R.id.dialog_setting_currentweek_ok:
			this.dismiss();
			ETipsUtils.setCurrentWeek(context, mCurrentWeek);
			tv_handler.setText(ETipsUtils.getCurrentWeek(context)+"");
			break;
		case R.id.dialog_setting_currentweek_cancle:
			this.dismiss();
			break;
		}
	}
}
