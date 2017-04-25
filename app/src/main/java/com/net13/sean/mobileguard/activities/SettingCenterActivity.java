package com.net13.sean.mobileguard.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.net13.sean.mobileguard.R;
import com.net13.sean.mobileguard.utils.MyConstants;
import com.net13.sean.mobileguard.utils.SpTools;
import com.net13.sean.mobileguard.view.SettingCenterItemView;

/**
 * Created by SEAN on 2017/4/23.
 */

public class SettingCenterActivity extends Activity {
	private SettingCenterItemView sciv_autoupdate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		initView();//初始化界面

		initEvent();//初始化组件的事件

		initData();//初始化组件的数据
	}

	private void initData() {
		//初始化自动更新复选框的初始状态
		sciv_autoupdate.setChecked(SpTools.getBoolean(getApplicationContext(), MyConstants.AUTOUPDATE, false));
	}

	private void initEvent() {

		sciv_autoupdate.setItemClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//原来的复选框状态的功能不变
				sciv_autoupdate.setChecked(!sciv_autoupdate.isChecked());
				//添加新的功能
				//如果复选框选中，自动更新已经开启，否则不开启
				//记录复选框的状态
				SpTools.putBoolean(getApplicationContext(), MyConstants.AUTOUPDATE, sciv_autoupdate.isChecked());
			}
		});
	}

	private void initView() {
		setContentView(R.layout.activity_settingcenter);
		//获取自动更新的自定义view
		sciv_autoupdate = (SettingCenterItemView) findViewById(R.id.sciv_setting_center_autoupdate);
	}
}
