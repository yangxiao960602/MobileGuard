package com.net13.sean.mobileguard.activities;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.net13.sean.mobileguard.R;
import com.net13.sean.mobileguard.domain.ContactsBean;
import com.net13.sean.mobileguard.utils.MyConstants;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by SEAN on 2017/4/21.
 */


/**
 * 显示所有好友信息的界面
 */

public abstract class BaseFriendsCallSmsActivity extends ListActivity {
	protected static final int LOADING = 1;
	protected static final int FINISH = 2;

	//获取联系人的数据
	private List<ContactsBean> datas = new ArrayList<ContactsBean>();
	private MyAdapter adapter;
	private Handler handler = new Handler() {

		private ProgressDialog pd;

		public void handleMessage(Message msg) {
			//更新界面
			switch (msg.what) {
				case LOADING://正在加载数据
					//显示对话框
					pd = new ProgressDialog(BaseFriendsCallSmsActivity.this);
					pd.setTitle("提示:");
					pd.setMessage("正在玩命加载数据。。。。。");
					pd.show();//显示对话框
					break;

				case FINISH://数据加载完成
					if (pd != null) {
						pd.dismiss();//关闭对话框
						pd = null;//垃圾回收 释放内存
					}

					//通过适配器来通知listview,将数据显示在ListView中
					adapter.notifyDataSetChanged();
					break;

				default:
					break;
			}
		}

	};
	private ListView lv_datas;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		lv_datas = getListView();
		adapter = new MyAdapter();

		//设置适配器，读取适配器的数据来显示
		lv_datas.setAdapter(adapter);

		//填充数据
		initData();

		//初始化事件
		initEvent();
	}

	/**
	 * 初始化listview的条目点击事件
	 */
	private void initEvent() {
		lv_datas.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//处理条目点击事件
				//获取当前条目的数据
				ContactsBean ContactsBean = datas.get(position);
				//获取号码
				String phone = ContactsBean.getPhone();
				Intent datas = new Intent();
				datas.putExtra(MyConstants.SAFENUMBER, phone);//保存安全号码
				//设置数据
				setResult(1, datas);
				//关闭自己
				finish();
			}
		});
	}

	private void initData() {
		//获取数据，2种 本地数据 和网络数据 存在耗时
		//子线程访问数据
		new Thread() {
			public void run() {
				//显示获取数据的进度
				Message msg = Message.obtain();
				msg.what = LOADING;
				handler.sendMessage(msg);

				//获取数据	核心代码
				//datas = ReadContactsEngine.readContants(getApplicationContext());
				datas = getDatas();
				SystemClock.sleep(1000);//为了展示进度条，休眠

				//数据获取完成,发送数据加载完成的消息
				msg = Message.obtain();
				msg.what = FINISH;
				handler.sendMessage(msg);
			}
		}.start();
	}


	public abstract List<ContactsBean> getDatas();


	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return datas.size();//数据的个数
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			//每个条目显示的数据
			View view = View.inflate(getApplicationContext(), R.layout.item_friend_listview, null);
			TextView tv_name = (TextView) view.findViewById(R.id.tv_friends_item_name);
			TextView tv_phone = (TextView) view.findViewById(R.id.tv_friends_item_phone);
			//获取当前行显示的数据
			ContactsBean bean = datas.get(position);
			tv_name.setText(bean.getName());//名字
			tv_phone.setText(bean.getPhone());//电话
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
