package com.net13.sean.mobileguard.engine;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.net13.sean.mobileguard.domain.ContactsBean;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SEAN on 2017/4/21.
 */

/**
 * 读取手机联系人的功能类
 */
public class ReadContactsEngine {

	/**
	 * @return
	 * 读取短信记录
	 */
	public static List<ContactsBean> readSmslog(Context context) {
		//1，电话日志的数据库
		//2,通过分析，db不能直接访问，需要内容提供者访问该数据库
		//3,看上层源码 找到uri content://sms
		Uri uri = Uri.parse("content://sms");
		//获取电话记录的联系人游标
		Cursor cursor = context.getContentResolver().query(uri, new String[]{"address"}, null, null, " _id desc");
		List<ContactsBean> datas = new ArrayList<ContactsBean>();

		while (cursor.moveToNext()) {
			ContactsBean bean = new ContactsBean();

			String phone = cursor.getString(0);//获取号码
			//String name = cursor.getString(1);//获取名字

			//bean.setName(name);
			bean.setPhone(phone);

			//添加数据
			datas.add(bean);

		}
		return datas;

	}

	/**
	 * @return 读取通话记录
	 */
	public static List<ContactsBean> readCalllog(Context context) {
		//1，电话日志的数据库
		//2,通过分析，db不能直接访问，需要内容提供者访问该数据库
		//3,看上层源码 找到uri content://calls
		Uri uri = Uri.parse("content://call_log/calls");
		//获取电话记录的联系人游标
		Cursor cursor = context.getContentResolver().query(uri, new String[]{"number", "name"}, null, null, " _id desc");
		List<ContactsBean> datas = new ArrayList<ContactsBean>();

		while (cursor.moveToNext()) {
			ContactsBean bean = new ContactsBean();

			String phone = cursor.getString(0);//获取号码
			String name = cursor.getString(1);//获取名字

			bean.setName(name);
			bean.setPhone(phone);

			//添加数据
			datas.add(bean);

		}
		return datas;

	}


	/**
	 * 读取手机联系人
	 */
	public static List<ContactsBean> readContants(Context context) {
		List<ContactsBean> datas = new ArrayList<ContactsBean>();
		Uri uriContants = Uri.parse("content://com.android.contacts/contacts");
		Uri uriDatas = Uri.parse("content://com.android.contacts/data");

		Cursor cursor = context.getContentResolver().query(uriContants, new String[]{"_id"}, null, null, null);
		//循环取数据
		while (cursor.moveToNext()) {
			//好友信息的封装bean
			ContactsBean bean = new ContactsBean();
			//直接打印id
			String id = cursor.getString(0);//获取到联系人的id

			Cursor cursor2 = context.getContentResolver().query(uriDatas, new String[]{"data1", "mimetype"}, " raw_contact_id = ? ", new String[]{id}, " data1 ");

			//循环每条数据信息 都是一个好友的一部分信息
			while (cursor2.moveToNext()) {
				String data = cursor2.getString(0);
				String mimeType = cursor2.getString(1);

				if (mimeType.equals("vnd.android.cursor.item/name")) {
					System.out.println("第" + id + "个用户：名字：" + data);
					bean.setName(data);
				} else if (mimeType.equals("vnd.android.cursor.item/phone_v2")) {
					System.out.println("第" + id + "个用户：电话：" + data);
					bean.setPhone(data);
				}
			}
			cursor2.close();//关闭游标释放资源
			datas.add(bean);//加一条好友信息
		}
		cursor.close();

		// TODO: 2017/4/25
		//通讯录按姓名排序
		/*if (datas.size() != 0){
			Collections.sort(datas, new Comparator<ContactsBean>() {
				@Override
				public int compare(ContactsBean o1, ContactsBean o2) {
					char[] a1 = o1.getName().toCharArray();
					char[] a2 = o2.getName().toCharArray();

					for (int i = 0; i < a1.length && i < a2.length; i++) {
						int c1 = getGBCode(a1[i]);
						int c2 = getGBCode(a2[i]);
						new StringBuffer().append(c1).toString();
						new StringBuffer().append(c2).toString();
						if (c1 == c2) {
							continue;
						}
						return c1-c2;
					}
					if (a1.length == a2.length){
						return 0;
					}
					return a1.length - a2.length;
				}
			});
		}*/

		return datas;
	}


	static int getGBCode(char c) {
		byte[] bytes = null;
		try {
			bytes = new StringBuffer().append(c).toString().getBytes("gbk");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (bytes.length == 1) {
			return bytes[0];
		}
		int a = bytes[0] - 0xA0 + 256;
		int b = bytes[1] - 0xA0 + 256;

		return a * 100 + b;
	}

}
