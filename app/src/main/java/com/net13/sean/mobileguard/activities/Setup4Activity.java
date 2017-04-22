package com.net13.sean.mobileguard.activities;

import android.content.Intent;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.net13.sean.mobileguard.R;
import com.net13.sean.mobileguard.service.LostFindService;
import com.net13.sean.mobileguard.utils.MyConstants;
import com.net13.sean.mobileguard.utils.ServiceUtils;
import com.net13.sean.mobileguard.utils.SpTools;


/**
 * Created by SEAN on 2017/4/21.
 */


/**
 * 第四个设置向导界面
 */
public class Setup4Activity extends BaseSetupActivity {
	private CheckBox cb_isprotected;

	/**
	 * 子类需要覆盖此方法，来完成界面的显示
	 */
	public void initView() {
		setContentView(R.layout.activity_setup4);

		//打钩开启防盗保护的复选框
		cb_isprotected = (CheckBox) findViewById(R.id.cb_setup4_isprotected);
	}

	/*
	 * 初始化复选框的值
	 */
	@Override
	public void initData() {
		//初始化复选框的值 看服务是否开启
		//如果服务开启，打钩，否则不打钩
		if (ServiceUtils.isServiceRunning(getApplicationContext(), "com.net13.sean.mobileguard.service.LostFindService")) {
			// 服务在运行
			cb_isprotected.setChecked(true);
		} else {
			cb_isprotected.setChecked(false);//初始化复选框的状态
		}
		super.initData();
	}

	/*
	 * 初始复选框的事件
	 */
	@Override
	public void initEvent() {
		cb_isprotected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				//如果选择打钩，开启防盗保护,防盗保护是一个服务
				if (isChecked) {
					System.out.println("check true");
					//true,开启防盗保护
					Intent service = new Intent(Setup4Activity.this, LostFindService.class);
					//启动防盗保护的服务
					startService(service);
				} else {
					System.out.println("check false");
					//关闭防盗保护
					Intent service = new Intent(Setup4Activity.this, LostFindService.class);
					//启动防盗保护的服务
					stopService(service);
				}

			}
		});
		super.initEvent();
	}

	@Override
	public void nextActivity() {
		//保存设置完成的状态
		SpTools.putBoolean(getApplicationContext(), MyConstants.ISSETUP, true);
		startActivity(LostFindActivity.class);
	}

	@Override
	public void prevActivity() {
		startActivity(Setup3Activity.class);
	}
}

