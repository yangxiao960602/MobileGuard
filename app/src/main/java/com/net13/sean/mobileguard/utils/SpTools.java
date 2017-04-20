package com.net13.sean.mobileguard.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by SEAN on 2017/4/20.
 */

public class SpTools {
	public static void putString(Context context, String key, String value) {
		SharedPreferences sp = context.getSharedPreferences(MyConstants.SPFILE, Context.MODE_PRIVATE);
		sp.edit().putString(key, value).commit();    //保存数据

	}

	public static String getString(Context context, String key, String defValue) {
		SharedPreferences sp = context.getSharedPreferences(MyConstants.SPFILE, Context.MODE_PRIVATE);
		return sp.getString(key, defValue);    //保存数据

	}

	public static void putBoolean(Context context, String key, boolean value) {
		SharedPreferences sp = context.getSharedPreferences(MyConstants.SPFILE, Context.MODE_PRIVATE);
		sp.edit().putBoolean(key, value).commit();    //保存数据

	}

	public static boolean getBoolean(Context context, String key, boolean defValue) {
		SharedPreferences sp = context.getSharedPreferences(MyConstants.SPFILE, Context.MODE_PRIVATE);
		return sp.getBoolean(key, defValue);    //保存数据

	}

}
