package com.net13.sean.mobileguard.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by SEAN on 2017/4/24.
 * 黑名单数据库
 */
public class BlackDBOpenHelper extends SQLiteOpenHelper {

	/**
	 * 初始版本信息
	 */
	public BlackDBOpenHelper(Context context) {
		super(context, "black.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//只初始一次
		//创建表
		db.execSQL("create table blacktb(_id integer primary key autoincrement,phone text,mode integer)");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//版本号发生变化，执行此方法
		//清空数据
		db.execSQL("drop table blacktb");
		onCreate(db);
	}

}