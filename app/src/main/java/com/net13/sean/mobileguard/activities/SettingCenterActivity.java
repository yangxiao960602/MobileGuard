package com.net13.sean.mobileguard.activities;

import android.app.Activity;
import android.os.Bundle;

import com.net13.sean.mobileguard.R;

/**
 * Created by SEAN on 2017/4/23.
 */

public class SettingCenterActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initView();//初始化界面
	}

	private void initView() {
		setContentView(R.layout.activity_settingcenter);
	}
}
