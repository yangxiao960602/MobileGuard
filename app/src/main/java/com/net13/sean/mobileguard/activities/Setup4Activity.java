package com.net13.sean.mobileguard.activities;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.net13.sean.mobileguard.R;
import com.net13.sean.mobileguard.receiver.DeviceAdminSample;
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
	private DevicePolicyManager dpm;
	private ComponentName who;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		dpm = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
		who = new ComponentName(Setup4Activity.this, DeviceAdminSample.class);
		super.onCreate(savedInstanceState);
	}

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
		//初始化复选框的值
		//如果服务开启切设备管理员激活，打钩，否则不打钩
		if (ServiceUtils.isServiceRunning(getApplicationContext(),
				"com.net13.sean.mobileguard.service.LostFindService")
				&& dpm.isAdminActive(who)) {
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

				//检查设别管理员权限
				//如果没有激活设备管理员，提醒给用户做事

				if (cb_isprotected.isChecked() && !dpm.isAdminActive(who)) {
					Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
					intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, who);
					intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
							"激活设别管理器以启用防盗功能!!!");
					startActivityForResult(intent, 1);
					cb_isprotected.setChecked(false);
					return;
				}

				//如果选择打钩，开启防盗保护,防盗保护是一个服务
				if (isChecked) {

					SpTools.putBoolean(getApplicationContext(), MyConstants.LOSTFIND, true);
					System.out.println("check true");
					//true,开启防盗保护
					Intent service = new Intent(Setup4Activity.this, LostFindService.class);
					//启动防盗保护的服务
					startService(service);
				} else {
					SpTools.putBoolean(getApplicationContext(), MyConstants.LOSTFIND, false);
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("Setup4Activity", "resultCode: " + resultCode + " requestCode:" + requestCode);
		if (requestCode == 1 && resultCode == -1) {
			//设备管理器激活
			cb_isprotected.setChecked(true);
			SpTools.putBoolean(getApplicationContext(), MyConstants.LOSTFIND, true);
			System.out.println("check true");
			//true,开启防盗保护
			Intent service = new Intent(Setup4Activity.this, LostFindService.class);
			//启动防盗保护的服务
			startService(service);
		} else {
			cb_isprotected.setChecked(false);
		}
		super.onActivityResult(requestCode, resultCode, data);
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

