package com.meizhuo.etips.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meizhuo.etips.common.AndroidUtils;
import com.meizhuo.etips.common.ETipsContants;
import com.meizhuo.etips.common.SP;
import com.meizhuo.etips.common.ShareManager;
import com.meizhuo.etips.common.StringUtils;
import com.meizhuo.etips.ui.base.BaseDialog;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.UMImage;

/**
 * 根据Intent 的数据来判断这是一条新建的标签还是以前的标签 NOTE: 编辑以前的标签，需要先载入就的时间，点击保存时，先删去以前时间点的标签
 * 再加入一个以当前时间为点的标签
 * 
 * @author Jayin Ton
 * 
 */
public class NotesEdit extends BaseUIActivity implements OnClickListener {
	private EditText et_content;
	private View back, save, delete, share;
	private TextView tv_time;
	private String time, content;
	private SP sp;
	private boolean isNewNote = true// 判断是否一条新建的标签
			,
			isSave = false; // 是否已经保存了？ //偷懒 。。还要写个dialog 出来 =。。
	private LinearLayout font;
	private BaseDialog dialog;
	private View ok, no; // dialog上的OK and NO

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_notesedit);
		initData();
		initLayout();
	}

	@Override
	protected void initLayout() {
		et_content = (EditText) _getView(R.id.acty_notesedit_et_content);
		tv_time = (TextView) _getView(R.id.acty_notesedit_time);
		back = _getView(R.id.btn_back);
		save = _getView(R.id.acty_notesedit_save);
		delete = _getView(R.id.acty_notesedit_delete);
		share = _getView(R.id.acty_notesedit_share);

		font = (LinearLayout) _getView(R.id.acty_notesedit_font);

		back.setOnClickListener(this);
		save.setOnClickListener(this);
		delete.setOnClickListener(this);
		share.setOnClickListener(this);
		initDialog();

		if (!isNewNote) {
			tv_time.setText(StringUtils.getDateFormat(Long.parseLong(time),
					"yy-mm-dd"));
			et_content.setText(content);
			et_content.setSelection(content.length()); // 设置光标在文字末端
		} else {
			tv_time.setText(StringUtils.getDateFormat(
					java.lang.System.currentTimeMillis(), "yy-mm-dd"));
		}
	}

	private void initDialog() {
		dialog = new BaseDialog(getContext());
		dialog.setContentView(R.layout.dlg_editsave);
		ok = dialog.findViewById(R.id.dlg_editsave_ok);
		no = dialog.findViewById(R.id.dlg_editsave_no);
		ok.setOnClickListener(this);
		no.setOnClickListener(this);
	}

	@Override
	protected void initData() {
		sp = new SP(ETipsContants.SP_NAME_Notes, this);
		content = getIntent().getStringExtra("content");
		if (content != null && !content.equals("")) {
			time = getIntent().getStringExtra("time");
			if (time != null && !time.equals("")) {
				isNewNote = false;
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			isExit();
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

	public void isExit(){
    	// 1.新建了，编辑了 没有保存就 离开 2.已有的打开，内容编辑了,但是没有按保存就离开
		if (( (content ==null && !"".equals(et_content.getText().toString()))     || (content != null
				&& !content.equals(et_content.getText().toString()) ) )
				&& !isSave) {
			if (!dialog.isShowing()) {
				dialog.show();
			}
		}else{
			closeActivity();
		}
    }

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(ETipsContants.Action_Notes);
		switch (v.getId()) {
		case R.id.dlg_editsave_ok:
			// 把之前的先删了再添加
			if (!isNewNote) {
				sp.delete(time);
			}
			sp.add(java.lang.System.currentTimeMillis() + "", et_content
					.getText().toString());
			this.sendBroadcast(intent);
			dialog.dismiss();
			toast("已保存");
			closeActivity();
			break;
		case R.id.dlg_editsave_no:
			dialog.dismiss();
			closeActivity();
			break;
		case R.id.btn_back:
			// 内容编辑了,但是没有按保存就离开
			isExit();
			break;
		case R.id.acty_notesedit_save:
			if (et_content.getText().toString().trim().equals("")) {
				toast("亲,一片空白你让我保存啥？");
				return;
			}
			// 把之前的先删了再添加
			if (!isNewNote) {
				sp.delete(time);
			}
			sp.add(java.lang.System.currentTimeMillis() + "", et_content
					.getText().toString());
			this.sendBroadcast(intent);
			toast("已保存");
			closeActivity();
			break;
		case R.id.acty_notesedit_delete:
			// delete
			if (sp.delete(time)) {
				toast("已删除");
				this.sendBroadcast(intent);// 发送广播
				closeActivity();
			} else {
				toast("删除失败");
				closeActivity();
			}

			break;
		case R.id.acty_notesedit_share:
			if (et_content.getText().toString().equals("")) {
				toast("写点东西吧！");
				return;
			}
			share.setVisibility(View.INVISIBLE);
			font.setVisibility(View.GONE);
			Bitmap screenPic = AndroidUtils.screenShot(NotesEdit.this);
			Rect frame = new Rect();
			NotesEdit.this.getWindow().getDecorView()
					.getWindowVisibleDisplayFrame(frame);
			share.setVisibility(View.VISIBLE);
			font.setVisibility(View.VISIBLE);
			String content = et_content.getText().toString() + " (分享自ETips客户端)";
			ShareManager sm = new ShareManager(content, new UMImage(
					getContext(), screenPic));
			sm.shareToSina(getContext(), new SnsPostListener() {

				@Override
				public void onStart() {
				}

				@Override
				public void onComplete(SHARE_MEDIA arg0, int arg1,
						SocializeEntity arg2) {
				}
			});
			break;
		}
	}

}
