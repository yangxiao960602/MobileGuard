package com.net13.sean.mobileguard.activities;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.net13.sean.mobileguard.R;
import com.net13.sean.mobileguard.utils.MyConstants;
import com.net13.sean.mobileguard.utils.SpTools;


/**
 * Created by SEAN on 2017/4/21.
 */


/**
 * @author Administrator
 *         第二个设置向导界面
 */
public class Setup2Activity extends BaseSetupActivity {
	private Button bt_bind;
	private ImageView iv_isBind;

	/*
	 * 初始化组件的数据
	 */
	@Override
	public void initData() {

		super.initData();
	}

	/**
	 * 子类需要覆盖此方法，来完成界面的显示
	 */
	public void initView() {
		setContentView(R.layout.activity_setup2);
		//获取bind sim卡按钮
		bt_bind = (Button) findViewById(R.id.bt_setup2_bindsim);
		//是否绑定sim卡的图标
		iv_isBind = (ImageView) findViewById(R.id.iv_setup2_isbind);
		if (TextUtils.isEmpty(SpTools.getString(getApplicationContext(), MyConstants.SIM, ""))) {
			//未绑定
			{
				//切换是否绑定sim卡的图标
				iv_isBind.setImageResource(R.drawable.unlock);//设置未加锁的图片
			}
		} else {
			{
				//切换是否绑定sim卡的图标
				iv_isBind.setImageResource(R.drawable.lock);//设置加锁的图片
			}
		}


	}

	/*
	 * 添加自己的事件
	 */
	@Override
	public void initEvent() {
		//添加自己的事件
		bt_bind.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//绑定和解绑
				if (TextUtils.isEmpty(SpTools.getString(getApplicationContext(), MyConstants.SIM, ""))) {
					//没有绑定sim卡
					{
						//绑定sim的业务
						//绑定sim卡,存储sim卡信息
						//获取sim卡信息
						TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
						//sim卡信息
						String simSerialNumber = tm.getSimSerialNumber();
						//保存sim卡信息,保存到sp中
						SpTools.putString(getApplicationContext(), MyConstants.SIM, simSerialNumber);
					}

					{
						//切换是否绑定sim卡的图标
						iv_isBind.setImageResource(R.drawable.lock);//设置加锁的图片
					}
				} else {
					//已经绑定sim卡,解绑sim卡，保存空值
					SpTools.putString(getApplicationContext(), MyConstants.SIM, "");
					{
						//切换是否绑定sim卡的图标
						iv_isBind.setImageResource(R.drawable.unlock);//设置未加锁的图片
					}
				}


			}
		});
		super.initEvent();
	}

	@Override
	public void next(View v) {
		if (TextUtils.isEmpty(SpTools.getString(getApplicationContext(), MyConstants.SIM, ""))) {
			//没有绑定sim
			Toast.makeText(getApplicationContext(), "请先绑定sim卡", Toast.LENGTH_SHORT).show();
			return;
		}
		super.next(v);//调用父类的功能
	}

	/*
	 * 进入下一个页面的逻辑处理
	 */
	@Override
	public void nextActivity() {
		startActivity(Setup3Activity.class);
	}

	@Override
	public void prevActivity() {
		startActivity(Setup1Activity.class);
	}


}
