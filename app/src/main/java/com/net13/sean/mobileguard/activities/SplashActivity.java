package com.net13.sean.mobileguard.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Interpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.net13.sean.mobileguard.R;
import com.net13.sean.mobileguard.domain.UrlBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 手机卫士splash界面
 */

public class SplashActivity extends AppCompatActivity {
	private static final String TAG = "SplashActivity";
	private static final int LOADMAIN = 1;    //加载主界面
	private static final int SHOWUPDATEDIALOG = 2;    //显示更新对话框
	private RelativeLayout rl_root;    //界面根布局组件
	private int versionCode;    //版本号
	private String versionName;    //版本名
	private TextView tv_versionName;    //显示版本名的组件
	private UrlBean parsedUrlData;    //url的信息封装
	private long startTimeMilllis;    //记录开始访问网络的时间

	@Override
	protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

		//初始化界面
		initView();

		//初始化数据
		initData();

		//初始化动画
		initAnimation();

		//检查更新
		checkVersion();
	}

	/**
	 * 获取自己的版本信息
	 */
	private void initData() {
		PackageManager pm = getPackageManager();
		try {
			PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
			//Log.d(TAG, "initData: packageName : " + getPackageName());
			//版本号
			versionCode = packageInfo.versionCode;
			//版本名
			versionName = packageInfo.versionName;
			Log.d(TAG, "initData: versionName:" + versionName +
					"versionCode" + versionCode);

			//设置textview
			tv_versionName.setText(versionName);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 访问服务器,获取最新的版本信息
	 */
	private void checkVersion() {
		//访问服务器,获取数据url
		//子线程中执行,访问服务器
		new Thread() {    //匿名内部类来创建子线程对象

			@Override
			public void run() {
				try {
					//获取当前时间
					startTimeMilllis = System.currentTimeMillis();

					URL url = new URL("http://192.168.1.107:8080/guardversion.json");
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setReadTimeout(5000);    //读取数据超时
					conn.setConnectTimeout(5000);    //网络连接超时
					conn.setRequestMethod("GET");    //设置请求方式
					int responseCode = conn.getResponseCode();
					if (responseCode == 200) {    //请求成功
						InputStream is = conn.getInputStream();    //获取字节流
						//把字节流转成缓冲的字符流
						BufferedReader reader = new BufferedReader(new InputStreamReader(is));
						String line = reader.readLine();    //读取一行信息
						StringBuilder jsonString = new StringBuilder();
						while (line != null) {
							jsonString.append(line);
							//继续读取
							line = reader.readLine();    //读取一行信息
						}
						Log.d(TAG, "run: ***********************************");
						Log.d(TAG, "run: " + jsonString);

						//解析json数据
						parsedUrlData = parseJson(jsonString);
						Log.d(TAG, "run: version : " + parsedUrlData.getVersionCode());

						//是否有新版本
						isNewVersion(parsedUrlData);

						reader.close();    //关闭输入流
						conn.disconnect();    //断开连接
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			//处理消息
			switch (msg.what) {
				case LOADMAIN:    //加载主界面
					loadMain();
					break;
				case SHOWUPDATEDIALOG:    //显示是否更新的对话框
					showUpadeDialog();
				default:
					break;
			}
		}
	};

	private void loadMain() {
		Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
		startActivity(intent);
	}

	/**
	 * 显示是否更新的对话框
	 */
	private void showUpadeDialog() {
		//对话框的上下文要是Activity的class类型,AlertDialog是Activity的一部分
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("更新?")
				.setMessage("新版本信息如下:\n" + parsedUrlData.getDesc())
				.setPositiveButton("更新", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//更新apk
						Log.d(TAG, "onClick: 更新APK");
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//进入主界面
						loadMain();
					}
				});
		builder.show();
	}

	/**
	 * 在子线程中执行
	 * 判断是否有新版本
	 *
	 * @param parsedJsonData
	 */
	private void isNewVersion(UrlBean parsedJsonData) {
		int serverCode = parsedJsonData.getVersionCode();    //服务器版本

		//强制拖长时间,以完整展示splash动画
		long endTimeMills = System.currentTimeMillis();
		if (endTimeMills - startTimeMilllis < 3000) {
			//设置休眠的时间
			SystemClock.sleep(4000 - (endTimeMills - startTimeMilllis));
		}

		//对比自己的版本
		if (serverCode == versionCode) {    //版本一致
			//进入主界面
			Message msg = Message.obtain();
			msg.what = LOADMAIN;
			handler.sendMessage(msg);
		} else {    //有新版本
			//弹出对话框,显示新版本的描述信息,让用户选择是否更新
			Message msg = Message.obtain();
			msg.what = SHOWUPDATEDIALOG;
			handler.sendMessage(msg);
		}
	}

	/**
	 * 从服务器获取的json数据
	 *
	 * @param jsonString
	 * @return url信息的封装对象
	 */
	private UrlBean parseJson(StringBuilder jsonString) {
		UrlBean bean = new UrlBean();
		try {
			//把json字符串数据封装成json对象
			JSONObject jobject = new JSONObject(jsonString + "");
			int version = jobject.getInt("version");
			String apkPath = jobject.getString("url");
			String desc = jobject.getString("desc");
			//数据封装到bean中
			bean.setDesc(desc);    //描述信息
			bean.setUrl(apkPath);    //新版本apk下载路径
			bean.setVersionCode(version);    //新版本号
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bean;
	}

	/**
	 * 界面组件的初始化
	 */
	private void initView() {
		setContentView(R.layout.activity_splash);
		rl_root = (RelativeLayout) findViewById(R.id.rl_splash_root);
		tv_versionName = (TextView) findViewById(R.id.tv_splash_version_name);

	}


	/**
	 * 动画的显示
	 */
	private void initAnimation() {
		//定义Alpha动画
		//0.0完全透明, 1.0完全显示
		AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
		//动画播放的时间
		aa.setDuration(3000);
		//动画停留在结束状态
		aa.setFillAfter(true);


		//定义旋转动画
		RotateAnimation ra = new RotateAnimation(
				//旋转方向,负数为逆时针
				0, -360,
				//设置锚点
				Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		//设置动画时间
		ra.setDuration(3000);
		//动画停留在结束状态
		ra.setFillAfter(true);


		//比例动画
		ScaleAnimation sa = new ScaleAnimation(
				0.0f, 1.f,
				0.0f, 1.0f,
				Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		//设置动画时间
		sa.setDuration(3000);
		//动画停留在结束状态
		sa.setFillAfter(true);


		//创建动画集合
		AnimationSet as = new AnimationSet(true);    //创建动画集合
		as.addAnimation(sa);    //加载比例动画
		as.addAnimation(ra);    //加载旋转动画
		as.addAnimation(aa);    //加载渐变动画

		//显示动画,根布局设置动画
		rl_root.startAnimation(as);    //同事播放三个动画

	}
}
