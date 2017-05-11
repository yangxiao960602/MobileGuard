package com.net13.sean.mobileguard.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.net13.sean.mobileguard.R;
import com.net13.sean.mobileguard.domain.AppBean;
import com.net13.sean.mobileguard.engine.AppManagerEngine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * Created by SEAN on 2017/4/26.
 * 软件管家的界面
 */
public class AppManagerActivity extends Activity {

	protected static final int LOADING = 1;
	protected static final int FINISH = 2;
	private TextView tv_sdAvail;
	private TextView tv_romAvail;
	private ListView lv_datas;
	private ProgressBar pb_loading;

	private long sdAvail;// sd可用空间
	private long romAvail;// rom可用空间

	private AppBean clickBean;// 当前点击的bean

	// 用户apk的容器
	private List<AppBean> userApks = new ArrayList<AppBean>();

	// 系统的apk容器
	private List<AppBean> sysApks = new ArrayList<AppBean>();

	private MyAdapter adapter;
	private TextView tv_userApp_lable;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case LOADING:// 正在加载数据
					pb_loading.setVisibility(View.VISIBLE);// 显示加载数据进度
					lv_datas.setVisibility(View.GONE);// 隐藏listview
					tv_userApp_lable.setVisibility(View.GONE);
					break;

				case FINISH:// 数据加载完成
					pb_loading.setVisibility(View.GONE);// 隐藏加载数据进度
					lv_datas.setVisibility(View.VISIBLE);// 隐藏listview
					tv_userApp_lable.setVisibility(View.VISIBLE);

					// 设置存储空间剩余大小
					tv_sdAvail.setText("SD卡可用空间:"
							+ Formatter.formatFileSize(getApplicationContext(),
							sdAvail));
					tv_romAvail.setText("本机可用空间:"
							+ Formatter.formatFileSize(getApplicationContext(),
							romAvail));

					// 初始化标签
					tv_userApp_lable.setText("用户软件(" + userApks.size() + ")");
					adapter.notifyDataSetChanged();// 刷新数据
					break;

				default:
					break;
			}
		}

	};
	private View popupView;
	private PopupWindow pw;
	private ScaleAnimation sa;
	private PackageManager pm;
	private BroadcastReceiver receiver;

	private void initPopupWindow() {
		// 弹出窗体的界面
		popupView = View.inflate(getApplicationContext(),
				R.layout.popup_appmanager, null);
		LinearLayout ll_remove =
				(LinearLayout) popupView.findViewById(R.id.ll_appmanager_pop_remove);
		LinearLayout ll_setting =
				(LinearLayout) popupView.findViewById(R.id.ll_appmanager_pop_setting);
		LinearLayout ll_start =
				(LinearLayout) popupView.findViewById(R.id.ll_appmanager_pop_start);

		//处理popupwindow各按钮的点击事件
		View.OnClickListener listener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
					case R.id.ll_appmanager_pop_remove:// 卸载软件
						removeApk();// 卸载apk
						break;

					case R.id.ll_appmanager_pop_setting:// 设置中心
						settingCenter();// 设置中心
						break;

					case R.id.ll_appmanager_pop_start:// 启动软件
						startApk();// 启动软件
						break;

					default:
						break;
				}
				closePopupWindow();// 关闭弹出窗体
			}
		};
		ll_remove.setOnClickListener(listener);
		ll_setting.setOnClickListener(listener);
		ll_start.setOnClickListener(listener);
		// 初始化弹出窗体
		pw = new PopupWindow(popupView, -2, -2);

		// 有动画效果必须设置背景
		pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));// 透明的背景

		// 弹出窗体的动画
		sa = new ScaleAnimation(0, 1, 0.5f, 1, Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		sa.setDuration(400);

	}

	protected void removeApk() {
		// 卸载软件
		// 1,用户apk
		/*
		 * <intent-filter> <action android:name="android.intent.action.VIEW" />
		 * <action android:name="android.intent.action.DELETE" /> <category
		 * android:name="android.intent.category.DEFAULT" /> <data
		 * android:scheme="package" /> </intent-filter>
		 */
		if (!clickBean.isSystem()) {
			//用户apk
			Intent intent = new Intent("android.intent.action.DELETE");
			intent.addCategory("android.intent.category.DEFAULT");
			intent.setData(Uri.parse("package:" + clickBean.getPackName()));
			startActivity(intent);// 删除用户apk的Activity
			//刷新自己的数据,直观滴显示删除的效果 : 监听：package remove 注册删除数据广播,通过广播来更新数据
		} else {
			Toast.makeText(getApplicationContext(), "系统应用无法卸载!!!", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initView();// 初始化界面

		initData();// 初始化数据

		initEvent();// 初始化事件

		initPopupWindow();// 初始化弹出窗体

		initRemoveApkReceiver();//注册删除apk的广播接受者
	}

	/**
	 * 注册删除apk的广播接受者
	 */
	private void initRemoveApkReceiver() {
		//删除apk(包括系统apk)的监听广播
		receiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				//更新数据&刷新数据
				initData();
				System.out.println("删除应用。。。。。。。。。。。");
			}
		};

		//注册删除apk广播
		IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_REMOVED);
		//注意配置数据模式
		filter.addDataScheme("package");
		registerReceiver(receiver, filter);
	}

	private void closePopupWindow() {
		if (pw != null && pw.isShowing()) {
			pw.dismiss();// 关闭
		}
	}

	private void showPopupWindow(View parrent, int x, int y) {
		closePopupWindow();// 关闭原来的弹出窗体
		pw.showAtLocation(parrent, Gravity.LEFT | Gravity.TOP, x, y);
		popupView.startAnimation(sa);

	}

	protected void startApk() {
		System.out.println("startApk");
		// 包名
		String packName = clickBean.getPackName();

		// 通过包名获取意图
		Intent launchIntentForPackage = pm.getLaunchIntentForPackage(packName);
		try {
			startActivity(launchIntentForPackage);
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "此应用无法打开!", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}

	}


	protected void settingCenter() {
		Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
		intent.setData(Uri.parse("package:" + clickBean.getPackName()));
		startActivity(intent);
	}

	private void initEvent() {
		//item点击事件的处理
		lv_datas.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//用户软件标签通过给textview设置为可点击抢走listview的时间
				// 获取当前点击的位置的值，如果点击标签不做处理
				// 如果点击的是系统软件标签位置，不做处理
				if (position == userApks.size() + 1) {
					return;
				}
				// 点击是条目的信息，获取当前点击位置的信息
				// 获取数据listview.getItemAtPosition 本质调用adapter.getItem();
				clickBean = (AppBean) lv_datas.getItemAtPosition(position);
				int[] location = new int[2];
				view.getLocationInWindow(location);
				View img = view.findViewById(R.id.iv_appmanager_listview_item_icon);
				showPopupWindow(view, location[0] + img.getWidth() + 20,
						location[1] - (img.getHeight() / 2));
			}
		});

		// listview滑动的事件处理
		lv_datas.setOnScrollListener(new AbsListView.OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			/*
			 * 监听listview滑动变化,动态更改"用户软件"和"系统软件"标签
			 */
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
								 int visibleItemCount, int totalItemCount) {
				closePopupWindow();// 关闭弹出窗体
				// 判断显示位置
				if (firstVisibleItem >= userApks.size() + 1) {
					// 显示系统apk
					// 改变标签的内容为系统apk
					tv_userApp_lable.setText("系统软件(" + sysApks.size() + ")");
					tv_userApp_lable.setTextSize(20);
					tv_userApp_lable.setPadding(10, 0, 0, 0);
				} else {
					tv_userApp_lable.setText("用户软件(" + userApks.size() + ")");
					tv_userApp_lable.setTextSize(20);
					tv_userApp_lable.setPadding(10, 0, 0, 0);
				}
			}
		});
	}

	private void initData() {
		new Thread() {
			public void run() {
				// 发送正在加载数据的消息
				handler.obtainMessage(LOADING).sendToTarget();
				SystemClock.sleep(500);
				// 获取所有apk数据
				List<AppBean> datas = AppManagerEngine.getAllApks(getApplicationContext());

				//添加新的数据之前，要先清空原来的数据
				sysApks.clear();
				userApks.clear();
				// 分类，系统还是用户
				for (AppBean appBean : datas) {
					if (appBean.isSystem()) {
						// 系统apk
						sysApks.add(appBean);
					} else {
						// 用户apk
						userApks.add(appBean);
					}
				}

				sdAvail = AppManagerEngine.getSDAvail();
				romAvail = AppManagerEngine.getRomAvail();

				// 数据处理完毕，发送消息
				handler.obtainMessage(FINISH).sendToTarget();

			}

		}.start();
	}

	private void initView() {
		setContentView(R.layout.activity_appmanager);

		// sd剩余空间的显示
		tv_sdAvail = (TextView) findViewById(R.id.tv_appmanager_sdsize);

		// rom剩余空间
		tv_romAvail = (TextView) findViewById(R.id.tv_appmanager_romsize);

		// 显示所有apk的listview
		lv_datas = (ListView) findViewById(R.id.lv_appmanager_appdatas);

		// 加载数据进度
		pb_loading = (ProgressBar) findViewById(R.id.pb_appmanager_loading);

		// 用户软件listview的标签
		tv_userApp_lable = (TextView) findViewById(R.id.tv_appmanager_userApp_lable);

		// listview的适配器
		adapter = new MyAdapter();

		lv_datas.setAdapter(adapter);

		pm = getPackageManager();

	}

	@Override
	protected void onDestroy() {
		//取消注册删除apk的广播
		unregisterReceiver(receiver);
		super.onDestroy();
	}

	private class ViewHolder {
		ImageView iv_icon;
		TextView tv_title;
		TextView tv_location;
		TextView tv_size;
	}

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {

			return userApks.size() + 1 + sysApks.size() + 1;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			//分别获取到"用户软甲标签"和"系统软件标签"和应用程序信息的View
			if (position == 0) {
				// 用户apk的标签
				TextView tv_userTable = new TextView(getApplicationContext());
				tv_userTable.setText("用户软件(" + userApks.size() + ")");//用户软件个数
				tv_userTable.setTextColor(Color.WHITE);// 文字为白色
				tv_userTable.setBackgroundColor(Color.GRAY);// 文字背景为灰色
				return tv_userTable;
			} else if (position == userApks.size() + 1) {
				// 系统apk标签
				TextView tv_userTable = new TextView(getApplicationContext());
				tv_userTable.setText("系统软件(" + sysApks.size() + ")");
				tv_userTable.setTextColor(Color.WHITE);// 文字为白色
				tv_userTable.setBackgroundColor(Color.GRAY);// 文字背景为灰色
				tv_userTable.setText("系统软件(" + sysApks.size() + ")");
				tv_userTable.setTextSize(20);
				tv_userTable.setPadding(10, 0, 0, 0);
				return tv_userTable;
			} else {
				// 界面的缓存
				ViewHolder holder = new ViewHolder();

				// 判断是否存在缓存
				if (convertView != null && convertView instanceof RelativeLayout) {
					holder = (ViewHolder) convertView.getTag();
				} else {
					convertView = View.inflate(getApplicationContext(),
							R.layout.item_appmanager_listview, null);
					holder.iv_icon = (ImageView) convertView.findViewById(
							R.id.iv_appmanager_listview_item_icon);
					holder.tv_title = (TextView) convertView.findViewById(
							R.id.tv_appmanager_listview_item_title);
					holder.tv_location = (TextView) convertView.findViewById(
							R.id.tv_appmanager_listview_item_location);
					holder.tv_size = (TextView) convertView.findViewById(
							R.id.tv_appmanager_listview_item_size);
					// 绑定tag
					convertView.setTag(holder);
				}

				//根据position取得AppBean
				AppBean bean = getItem(position);

				// 设置数据
				holder.iv_icon.setImageDrawable(bean.getIcon());// 设置图标

				if (bean.isSd()) {
					// 设置存储位置
					holder.tv_location.setText("SD卡存储");
				} else {
					holder.tv_location.setText("本机存储");
				}

				holder.tv_title.setText(bean.getAppName());// 设置名字

				// 设置应用占用空间的大小
				holder.tv_size.setText(Formatter.formatFileSize(
						getApplicationContext(), bean.getSize()));
				return convertView;
			}

		}


		/*
		 * 通过位置获取数据
		 */
		@Override
		public AppBean getItem(int position) {
			// 获取数据
			AppBean bean = null;
			if (position <= userApks.size()) {
				bean = userApks.get(position - 1);
			} else {
				// 系统apk
				bean = sysApks.get(position - 1 - 1 - userApks.size());
			}
			return bean;
		}


		@Override
		public long getItemId(int position) {
			return 0;
		}
	}
}
