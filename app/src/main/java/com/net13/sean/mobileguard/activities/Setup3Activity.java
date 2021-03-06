package com.net13.sean.mobileguard.activities;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.net13.sean.mobileguard.R;
import com.net13.sean.mobileguard.utils.EncryptTools;
import com.net13.sean.mobileguard.utils.MyConstants;
import com.net13.sean.mobileguard.utils.SpTools;


/**
 * Created by SEAN on 2017/4/21.
 */


/**
 * 第三个设置向导界面
 */
public class Setup3Activity extends BaseSetupActivity {
	private EditText et_safeNumber;//安全号码的编辑框
	private String safeNumber;//安全号码

	/*
	 * 子类覆盖此方法来完成组件数据的初始化
	 */
	@Override
	public void initData() {
		safeNumber = SpTools.getString(getApplicationContext(), MyConstants.SAFENUMBER, "");

		et_safeNumber.setText(EncryptTools.decrypt(MyConstants.MUSIC, safeNumber));
		super.initData();
	}

	/**
	 * 子类需要覆盖此方法，来完成界面的显示
	 */
	public void initView() {
		setContentView(R.layout.activity_setup3);
		//安全号码的编辑框
		et_safeNumber = (EditText) findViewById(R.id.et_setup3_safenumber);

	}

	/**
	 * 从手机联系人里获取安全号码
	 *
	 * @param v
	 */
	public void selectSafeNumber(View v) {
		//弹出新的Activity来显示所有好友信息
		Intent friends = new Intent(this, FriendsActivity.class);
		startActivityForResult(friends, 1);//启动显示好友界面

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {//用户选择数据来关闭联系人界面,而不是直接点击返回按钮
			//取数据
			String phone = data.getStringExtra(MyConstants.SAFENUMBER);
			//显示安全号码
			et_safeNumber.setText(phone);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/*
	 * 覆盖父类的方法，完成业务
	 */
	@Override
	public void next(View v) {
		//保存安全号码

		//获取安全号码
		safeNumber = et_safeNumber.getText().toString().trim();

		if (TextUtils.isEmpty(safeNumber)) {
			//如果安全号码为空，下一步不进行页面的跳转
			Toast.makeText(getApplicationContext(), "安全号码不能为空", Toast.LENGTH_SHORT).show();
			//不调用父类的功能来进行页面的切换
			return;
		} else {
			//对安全号码进行加密
			safeNumber = EncryptTools.encrypt(MyConstants.MUSIC, safeNumber);
			//保存安全号码
			SpTools.putString(getApplicationContext(), MyConstants.SAFENUMBER, safeNumber);
		}

		//调用父类功能完成页面的切换
		super.next(v);
	}

	@Override
	public void nextActivity() {
		startActivity(Setup4Activity.class);
	}

	@Override
	public void prevActivity() {
		startActivity(Setup2Activity.class);
	}
}

