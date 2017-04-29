package com.net13.sean.mobileguard.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.net13.sean.mobileguard.R;
import com.net13.sean.mobileguard.utils.Md5Utils;
import com.net13.sean.mobileguard.utils.MyConstants;
import com.net13.sean.mobileguard.utils.SpTools;

/**
 * 主界面
 * Created by SEAN on 2017/4/18.
 */

public class HomeActivity extends Activity {

	private static final String TAG = "HomeActivity";
	private GridView gv_menus;
	//
//	private int icons[] = {R.drawable.safe, R.drawable.callmsgsafe, R.drawable.app,
//			R.drawable.taskmanager, R.drawable.netmanager, R.drawable.trojan,
//			R.drawable.sysoptimize, R.drawable.atools, R.drawable.settings};
	private int icons[] = {R.drawable.safe, R.drawable.callmsgsafe,
			R.drawable.app, R.drawable.settings};

	private String names[] = {"手机防盗", "通讯卫士", "软件管家", "设置中心",};
	private MyAdapter adapter;    //gridview的适配器
	private AlertDialog dialog;


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();    //初始化界面
		initData();    //给GridView设置数据
		initEvent();    //初始化事件


	}

	/**
	 * 初始化组件的事件
	 */
	private void initEvent() {
		gv_menus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//判断点击的位置
				switch (position) {
					case 0:
						//自定义对话框
						//判断是否设置过密码,没有设置过则设置密码,设置过则验证密码
						if (TextUtils.isEmpty(SpTools.getString(getApplicationContext(), MyConstants.PASSWORD, ""))) {
							//没有设置过密码
							//设置密码对话框
							showSetPassDialog();
						} else {
							//设置过密码,验证密码
							showEnterDialog();
						}
						break;

					case 1:
						//打开通讯卫士的界面
						Intent telSmsSafe = new Intent(HomeActivity.this, TelSmsSafeActivity.class);
						startActivity(telSmsSafe);
						break;

					case 2:
						//打开通讯卫士的界面
						Intent appManager = new Intent(HomeActivity.this, AppManagerActivity.class);
						startActivity(appManager);
						break;

					case 3://设置中心
						//打开设置中心的界面
						Intent setting = new Intent(HomeActivity.this, SettingCenterActivity.class);
						startActivity(setting);
						break;
				}
			}
		});
	}

	/**
	 * 显示自定义的输入密码对话框
	 */
	private void showEnterDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View view = View.inflate(getApplicationContext(), R.layout.dialog_enter_password, null);

		final EditText et_passone = (EditText) view.findViewById(R.id.et_dialog_enter_password_one);
		Button bt_login = (Button) view.findViewById(R.id.bt_dialog_enter_password_login);
		Button bt_cancel = (Button) view.findViewById(R.id.bt_dialog_enter_password_cancel);

		builder.setView(view);
		bt_login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//设置密码
				String passone = et_passone.getText().toString().trim();
				if (TextUtils.isEmpty(passone)) {
					Toast.makeText(getApplicationContext(), "密码不能为空!", Toast.LENGTH_SHORT).show();
					return;
				} else {
					//密码判断,MD5 两次加密
					passone = Md5Utils.md5(Md5Utils.md5(passone));
					//读取sp中保存的密文进行判断
					if (passone.equals(SpTools.getString(getApplicationContext(), MyConstants.PASSWORD, ""))) {
						//如果密码一致
						//进入手机防盗界面
						Intent intent = new Intent(HomeActivity.this, LostFindActivity.class);
						startActivity(intent);
					} else {
						//密码不一致
						Toast.makeText(getApplicationContext(), "密码错误!", Toast.LENGTH_SHORT).show();
						return;
					}
					//关闭对话框
					dialog.dismiss();
				}
			}
		});
		bt_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog = builder.create();
		dialog.show();
	}

	/**
	 * 显示自定义对话框设置密码
	 */
	private void showSetPassDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View view = View.inflate(getApplicationContext(), R.layout.dialog_set_password, null);

		final EditText et_passone = (EditText) view.findViewById(R.id.et_dialog_set_password_one);
		final EditText et_passtwo = (EditText) view.findViewById(R.id.et_dialog_set_password_two);
		Button bt_setpass = (Button) view.findViewById(R.id.bt_dialog_set_password_setpass);
		Button bt_cancel = (Button) view.findViewById(R.id.bt_dialog_set_password_cancel);

		builder.setView(view);
		bt_setpass.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//设置密码
				String passone = et_passone.getText().toString().trim();
				String passtwo = et_passtwo.getText().toString().trim();
				if (TextUtils.isEmpty(passone) || TextUtils.isEmpty(passtwo)) {
					Toast.makeText(getApplicationContext(), "密码不能为空!", Toast.LENGTH_SHORT).show();
					return;
				} else if (!passone.equals(passtwo)) {    //密码不一致
					Toast.makeText(getApplicationContext(), "两次输入密码不一致!", Toast.LENGTH_SHORT).show();
					return;
				} else {
					//保存密码到sharedPreference
					//对密码进行加密,MD5 两次加密
					passone = Md5Utils.md5(Md5Utils.md5(passone));
					SpTools.putString(getApplicationContext(), MyConstants.PASSWORD, passone);
					Log.d(TAG, "onClick: 密码密文为 : " + passone);
					dialog.dismiss();

					//设置完密码后直接进入防盗设置界面
					Intent intent = new Intent(HomeActivity.this, Setup1Activity.class);
					startActivity(intent);
				}
			}
		});
		bt_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog = builder.create();
		dialog.show();
	}

	/**
	 * 初始化组件的数据
	 */
	private void initData() {
		adapter = new MyAdapter();
		gv_menus.setAdapter(adapter);    //设置主界面GridView的适配器
	}

	private void initView() {
		setContentView(R.layout.activity_home);
		gv_menus = (GridView) findViewById(R.id.gv_home_menus);

		WindowManager wm = this.getWindowManager();
		int height = wm.getDefaultDisplay().getHeight();

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(gv_menus.getLayoutParams());
		lp.setMargins(0, height / 7, 0, 0);
		gv_menus.setLayoutParams(lp);
	}

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return icons.length;    //图标个数
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(getApplicationContext(), R.layout.item_home_gridview, null);
			//获取组件
			ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_item_home_gv_icon);
			TextView tv_name = (TextView) view.findViewById(R.id.tv_item_home_gv_name);
			//设置数据
			iv_icon.setImageResource(icons[position]);    //图片
			tv_name.setText(names[position]);    //文字
			return view;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}
	}
}
