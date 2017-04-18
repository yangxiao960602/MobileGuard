package com.net13.sean.mobileguard.domain;

/**
 * Created by SEAN on 2017/4/18.
 */

/**
 * url信息的封装
 */
public class UrlBean {
	private String url;        //apk下载路径
	private int versionCode;    //版本号
	private String desc;    //新版本描述信息

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
