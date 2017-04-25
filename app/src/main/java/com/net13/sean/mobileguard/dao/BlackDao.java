package com.net13.sean.mobileguard.dao;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.net13.sean.mobileguard.db.BlackDBOpenHelper;
import com.net13.sean.mobileguard.domain.BlackBean;
import com.net13.sean.mobileguard.domain.BlackTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SEAN on 2017/4/24.
 * 黑名单数据业务的封装类
 */
public class BlackDao {
	private BlackDBOpenHelper blackDBOpenHelper;

	public BlackDao(Context context) {
		this.blackDBOpenHelper = new BlackDBOpenHelper(context);
	}

	/**
	 * @return 返回所有的黑名单数据
	 */
	public List<BlackBean> getAllDatas() {
		List<BlackBean> datas = new ArrayList<BlackBean>();
		SQLiteDatabase database = blackDBOpenHelper.getReadableDatabase();
		// 获取blacktb的所有数据游标
		Cursor cursor = database.rawQuery("select " + BlackTable.PHONE + "," + BlackTable.MODE
				+ " from " + BlackTable.BLACKTABLE
				+ " order by _id desc", null);

		while (cursor.moveToNext()) {
			// 有数据，数据封装
			BlackBean bean = new BlackBean();

			// 封装黑名单号码
			bean.setPhone(cursor.getString(0));

			// 封装拦截模式
			bean.setMode(cursor.getInt(1));

			// 添加数据到集合中
			datas.add(bean);
		}

		cursor.close();// 关闭游标
		database.close();// 关闭数据库

		return datas;
	}

	/**
	 * 删除黑名单号码
	 *
	 * @param phone 要删除的黑名单号码
	 */
	public void delete(String phone) {
		SQLiteDatabase db = this.blackDBOpenHelper.getWritableDatabase();
		// 根据黑名单号码，来删除黑名单数据
		db.delete(BlackTable.BLACKTABLE, BlackTable.PHONE + "=?",
				new String[]{phone});
		db.close();
	}

	/**
	 * 根据号码修改拦截模式
	 *
	 * @param phone 修改的黑名单号码
	 * @param mode  修改的新的拦截模式
	 */
	public void update(String phone, int mode) {
		SQLiteDatabase db = this.blackDBOpenHelper.getWritableDatabase();

		ContentValues values = new ContentValues();// 修改新的属性值
		// 设置新的拦截模式
		values.put(BlackTable.MODE, mode);

		// 根据号码更新新的模式
		db.update(BlackTable.BLACKTABLE, values, BlackTable.PHONE + "=?",
				new String[]{phone});

		db.close();// 关闭数据库

	}

	/**
	 * 添加黑名单号码
	 *
	 * @param bean 黑名单信息的封装bean
	 */
	public void add(BlackBean bean) {
		add(bean.getPhone(), bean.getMode());
	}

	/**
	 * 添加黑名单号码
	 *
	 * @param phone 黑名单号码
	 * @param mode  拦截模式
	 */
	public void add(String phone, int mode) {
		delete(phone);//删除数据
		// 获取黑名单数据库
		SQLiteDatabase db = blackDBOpenHelper.getWritableDatabase();
		ContentValues values = new ContentValues();

		// 设置黑名单号码
		values.put(BlackTable.PHONE, phone);

		// 设置黑名单拦截模式
		values.put(BlackTable.MODE, mode);

		// 往黑名单表中插入一条记录
		db.insert(BlackTable.BLACKTABLE, null, values);

		// 关闭数据库
		db.close();
	}

	/**
	 * @param phone 传入号码
	 * @return 拦截模式 1:短信 2:电话 3:全部拦截
	 */
	public int getMode(String phone) {
		SQLiteDatabase database = blackDBOpenHelper.getReadableDatabase();
		Cursor cursor = database.rawQuery("select " + BlackTable.MODE
				+ " from " + BlackTable.BLACKTABLE + " where "
				+ BlackTable.PHONE + "=?", new String[]{phone});
		int mode = 0;//默认为0,不是黑名单号码
		if (cursor.moveToNext()) {// 是黑名单号码
			mode = cursor.getInt(0);// 取出对应号码的拦截模式
		} else {
			mode = 0;// 不是黑名单号码
		}
		return mode;
	}
}
