package com.net13.sean.mobileguard.engine;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.net13.sean.mobileguard.domain.ContactsBean;

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

			Cursor cursor2 = context.getContentResolver().query(uriDatas, new String[]{"data1", "mimetype"}, " raw_contact_id = ? ", new String[]{id}, null);

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
		return datas;
	}
}
