package com.net13.sean.mobileguard.utils;

/**
 * Created by SEAN on 2017/4/20.
 */

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Utils {
	/**
	 * @param str 原文
	 * @return 密文
	 */
	public static String md5(String str) {
		StringBuilder mess = new StringBuilder();
		try {
			//获取MD5加密器
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] bytes = str.getBytes();
			byte[] digest = md.digest(bytes);    //加密后的字节码-

			for (byte b : digest) {
				//把每个字节转成16进制数
				int d = b & 0xff;// 0x000000ff
				String hexString = Integer.toHexString(d);
				if (hexString.length() == 1) {//字节的高4位为0
					hexString = "0" + hexString;
				}
				mess.append(hexString);//把每个字节对应的2位十六进制数当成字符串拼接一起

			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return mess + "";
	}
}
