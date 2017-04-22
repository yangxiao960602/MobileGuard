package com.net13.sean.mobileguard.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsMessage;

/**
 * Created by SEAN on 2017/4/22.
 */

public class LostFindService extends Service {

	private SmsReceiver receiver;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		//短信广播接收者
		receiver = new SmsReceiver();
		IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(Integer.MAX_VALUE);//级别一样，清单文件，谁先注册谁先执行，如果级别一样，代码比清单要高
		//注册短信监听
		registerReceiver(receiver, filter);
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		//取消注册短信的监听广播
		unregisterReceiver(receiver);
		super.onDestroy();
	}

	/**
	 * 短信的广播接收者
	 */
	private class SmsReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			//实现短信拦截功能
			Bundle extras = intent.getExtras();

			Object datas[] = (Object[]) extras.get("pdus");
			for (Object data : datas) {
				SmsMessage sm = SmsMessage.createFromPdu((byte[]) data);
				System.out.println(sm.getMessageBody() + ":" + sm.getOriginatingAddress());

			}
		}

	}

}
