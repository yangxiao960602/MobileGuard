package com.net13.sean.mobileguard.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.util.Log;

import com.net13.sean.mobileguard.utils.EncryptTools;
import com.net13.sean.mobileguard.utils.MyConstants;
import com.net13.sean.mobileguard.utils.SpTools;

import java.util.List;

/**
 * Created by SEAN on 2017/4/22.
 * 定位的服务管理器,来获取定位的信息
 */
public class LocationService extends Service {

	private static final String TAG = "LocationService";

	private LocationManager lm;
	private LocationListener listener;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {

		lm = (LocationManager) getSystemService(LOCATION_SERVICE);
		listener = new LocationListener() {

			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {

			}

			@Override
			public void onProviderEnabled(String provider) {

			}

			@Override
			public void onProviderDisabled(String provider) {

			}

			/*
			 * 位置变化，就触发此方法调用，覆盖此方法可以追踪回调结果信息
			 */
			@Override
			public void onLocationChanged(Location location) {
				// 获取位置变化的结果
				float accuracy = location.getAccuracy();// 精确度,以米为单位
				double altitude = location.getAltitude();// 获取海拔高度
				double longitude = location.getLongitude();// 获取经度
				double latitude = location.getLatitude();// 获取纬度
				float speed = location.getSpeed();// 速度
				//组装定位信息
				StringBuilder tv_mess = new StringBuilder();
				tv_mess.append("accuracy:" + accuracy + "\n");
				tv_mess.append("altitude:" + altitude + "\n");
				tv_mess.append("longitude:" + longitude + "\n");
				tv_mess.append("latitude:" + latitude + "\n");
				tv_mess.append("speed:" + speed + "\n");

				// 发送短信
				String safeNumber = SpTools.getString(LocationService.this, MyConstants.SAFENUMBER, "");
				safeNumber = EncryptTools.decrypt(MyConstants.MUSIC, safeNumber);
				//发送短信给安全号码
				SmsManager sm = SmsManager.getDefault();
				sm.sendTextMessage(safeNumber, "", tv_mess + "", null, null);

				Log.d(TAG, "onLocationChanged: 给安全号码" + safeNumber + "发送放到短信，短信内容如下：" + tv_mess);

				//SystemClock.sleep(60000);//一分钟发一条位置信息

				// TODO: 2017/5/11 发送位置短信频率 
				// 关闭gps
				stopSelf();//关闭自己
			}
		};

		//获取所有的提供的定位方式，选用最合适的定位方式
		List<String> allProviders = lm.getAllProviders();
		for (String string : allProviders) {
			System.out.println(string + "->定位方式");
		}

		Criteria criteria = new Criteria();
		criteria.setCostAllowed(true);//产生费用
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		//动态获取手机的最佳定位方式
		String bestProvider = lm.getBestProvider(criteria, true);

		//动态权限检查
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}
		//注册监听回调
		lm.requestLocationUpdates(bestProvider, 0, 0, listener);
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		// 取消定位的监听
		lm.removeUpdates(listener);
		lm = null;
		super.onDestroy();
	}

}


//package com.net13.sean.mobileguard.service;
//
//import java.util.List;
//
//import android.Manifest;
//import android.app.Service;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.location.Criteria;
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
//import android.os.Bundle;
//import android.os.IBinder;
//import android.os.SystemClock;
//import android.support.v4.app.ActivityCompat;
//import android.telephony.SmsManager;
//
//import com.net13.sean.mobileguard.utils.EncryptTools;
//import com.net13.sean.mobileguard.utils.MyConstants;
//import com.net13.sean.mobileguard.utils.SpTools;
//
///**
// * @author Administrator 定位的服务管理器,来获取定位的信息
// */
//public class LocationService extends Service {
//
//	private LocationManager lm;
//	private LocationListener listener;
//
//	@Override
//	public IBinder onBind(Intent intent) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public void onCreate() {
//		lm = (LocationManager) getSystemService(LOCATION_SERVICE);
//		listener = new LocationListener() {
//
//			@Override
//			public void onStatusChanged(String provider, int status,
//										Bundle extras) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void onProviderEnabled(String provider) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void onProviderDisabled(String provider) {
//				// TODO Auto-generated method stub
//
//			}
//
//			/*
//			 * (non-Javadoc) 位置变化，就触发此方法调用，覆盖此方法可以追踪回调结果信息
//			 *
//			 * @see
//			 * android.location.LocationListener#onLocationChanged(android.location
//			 * .Location)
//			 */
//			@Override
//			public void onLocationChanged(Location location) {
//				// 获取位置变化的结果
//				float accuracy = location.getAccuracy();// 精确度,以米为单位
//				double altitude = location.getAltitude();// 获取海拔高度
//				double longitude = location.getLongitude();// 获取经度
//				double latitude = location.getLatitude();// 获取纬度
//				float speed = location.getSpeed();// 速度
//				//定位信息
//				StringBuilder tv_mess = new StringBuilder();
//				tv_mess.append("accuracy:" + accuracy + "\n");
//				tv_mess.append("altitude:" + altitude + "\n");
//				tv_mess.append("longitude:" + longitude + "\n");
//				tv_mess.append("latitude:" + latitude + "\n");
//				tv_mess.append("speed:" + speed + "\n");
//				// 发送短信
//
//				String safeNumber = SpTools.getString(LocationService.this, MyConstants.SAFENUMBER, "");
//				safeNumber = EncryptTools.decrypt(MyConstants.MUSIC, safeNumber);
//				//发送短信给安全号码
//				SmsManager sm = SmsManager.getDefault();
//				sm.sendTextMessage(safeNumber, "", tv_mess + "", null, null);
//
//				// 关闭gps
//				SystemClock.sleep(5000);
//				stopSelf();//关闭自己
//			}
//		};
//
//		//获取所有的提供的定位方式
//		List<String> allProviders = lm.getAllProviders();
//		for (String string : allProviders) {
//			System.out.println(string + "》》定位方式");
//		}
//
//		Criteria criteria = new Criteria();
//		criteria.setCostAllowed(true);//产生费用
//		criteria.setAccuracy(Criteria.ACCURACY_FINE);
//		//动态获取手机的最佳定位方式
//		String bestProvider = lm.getBestProvider(criteria, true);
//		//注册监听回调
//		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//			// TODO: Consider calling
//			//    ActivityCompat#requestPermissions
//			// here to request the missing permissions, and then overriding
//			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//			//                                          int[] grantResults)
//			// to handle the case where the user grants the permission. See the documentation
//			// for ActivityCompat#requestPermissions for more details.
//			return;
//		}
//		lm.requestLocationUpdates(bestProvider, 0, 0, listener);
//		super.onCreate();
//	}
//
//	@Override
//	public void onDestroy() {
//		// 取消定位的监听
//		lm.removeUpdates(listener);
//		lm = null;
//		super.onDestroy();
//	}
//
//}
