package com.net13.sean.mobileguard.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.net13.sean.mobileguard.R;
import com.net13.sean.mobileguard.utils.MyConstants;
import com.net13.sean.mobileguard.utils.SpTools;

/**
 * Created by SEAN on 2017/4/20.
 */

public class LostFindActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//如果第一次访问该界面，要先进入设置向导界面
		if (SpTools.getBoolean(getApplicationContext(), MyConstants.ISSETUP, false)) {    //默认为没进入过设置界面
			// 进入过设置向导界面，直接显示本界面
			initView();//手机防盗界面
		} else {
			//要进入设置向导界面
			Intent intent = new Intent(this, Setup1Activity.class);
			startActivity(intent);
			finish();//关闭自己
		}
	}

	private void initView() {
		setContentView(R.layout.activity_lostfind);
	}
}