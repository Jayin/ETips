package com.meizhuo.etips.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.meizhuo.etips.common.utils.StringUtils;

/**
 * 用户注册 注册时用户验证 这个Activity 的启动模式应该是singleTast
 * 
 * @author Jayin Ton
 * 
 */
public class Regist extends BaseUIActivity {
	private EditText account, nickname, psw, comfirmPsw;
	private View regist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_regist);
		initData();
		initLayout();
	}

	@Override
	protected void initLayout() {
		account = (EditText) _getView(R.id.acty_regist_account);
		nickname = (EditText) _getView(R.id.acty_regist_nickname);
		psw = (EditText) _getView(R.id.acty_regist_psw);
		comfirmPsw = (EditText) _getView(R.id.acty_regist_psw_confirm);
		regist = _getView(R.id.acty_regist_ok);

		regist.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isFormatCorrect()) { // finish this method
					Intent intent = new Intent(Regist.this,
							RegistIdentity.class);
					intent.putExtra("account", account.getText().toString()
							.trim());
					intent.putExtra("nickname", nickname.getText().toString()
							.trim());
					intent.putExtra("psw", psw.getText().toString().trim());
					openActivity(intent);
				}
			}
		});
	}
     /**
      * 输入格式
      * @return
      */
	protected boolean isFormatCorrect() {
		if (!StringUtils.isEmail(account.getText().toString())) {
			toast("邮箱格式有误！");
			return false;
		}
	
		if (!StringUtils.isNickname(nickname.getText().toString())) {
			toast("用户昵称只能包含中/英文、数字、下划线、横线");
			return false;
		}
		if (psw.getText().toString().length() > 16
				|| psw.getText().toString().length() < 8) {
			toast("密码长度有误");
			return false;
		}
		if (comfirmPsw.getText().toString().length() > 16
				|| comfirmPsw.getText().toString().length() < 8) {
			toast("密码长度有误");
			return false;
		}
		if (!StringUtils.isPswVaild(psw.getText().toString())) {
			toast("密码格式有误");
			return false;
		}
		
		if (!StringUtils.isPswVaild(comfirmPsw.getText().toString())) {
			toast("密码格式有误");
			return false;
		}
		if(!psw.getText().toString().equals(comfirmPsw.getText().toString())){
			toast("输入密码不一致");
			return false;
		}
 		return true;
	}

	@Override
	protected void initData() {

	}

}
