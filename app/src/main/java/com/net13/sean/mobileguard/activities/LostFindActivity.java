package com.net13.sean.mobileguard.activities;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.net13.sean.mobileguard.R;
import com.net13.sean.mobileguard.receiver.DeviceAdminSample;
import com.net13.sean.mobileguard.utils.MyConstants;
import com.net13.sean.mobileguard.utils.ServiceUtils;
import com.net13.sean.mobileguard.utils.SpTools;

/**
 * Created by SEAN on 2017/4/20.
 */

public class LostFindActivity extends Activity {

	private Button bt_lostfindReset;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//判断防盗服务以及设备管理员是否开启
		DevicePolicyManager dpm = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
		ComponentName who = new ComponentName(this, DeviceAdminSample.class);
		if (!ServiceUtils.isServiceRunning(getApplicationContext(),
				"com.net13.sean.mobileguard.service.LostFindService")
				|| !dpm.isAdminActive(who)) {
			Toast.makeText(getApplicationContext(), "防盗服务未开启,请重新开启!",
					Toast.LENGTH_LONG).show();
			Intent intent = new Intent(this, Setup1Activity.class);
			startActivity(intent);
			finish();
		}

		super.onCreate(savedInstanceState);
		//如果第一次访问该界面，要先进入设置向导界面
		if (SpTools.getBoolean(getApplicationContext(), MyConstants.ISSETUP, false)) {    //默认为没进入过设置界面
			// 进入过设置向导界面，直接显示本界面
			initView();//手机防盗界面
			initEvent();//初始化事件
		} else {
			//要进入设置向导界面
			Intent intent = new Intent(this, Setup1Activity.class);
			startActivity(intent);
			finish();//关闭自己
		}
	}

	private void initView() {
		setContentView(R.layout.activity_lostfind);
		bt_lostfindReset = (Button) findViewById(R.id.bt_lostfind_reset);
	}

	/**
	 * 重新进入设置向导界面
	 */
	public void initEvent() {
		bt_lostfindReset.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LostFindActivity.this, Setup1Activity.class);
				startActivity(intent);
				finish();
			}
		});
	}
}