package com.net13.sean.mobileguard.domain;

/**
 * Created by SEAN on 2017/4/24.
 * 黑名单数据的封装类
 */
public class BlackBean {
	private String phone;
	private int mode;


	//保证TelSmsSafeActivity中添加黑名单时能顺利删除已经存在的数据
	@Override
	public boolean equals(Object o) {
		if (o instanceof BlackBean) {
			BlackBean bean = (BlackBean) o;
			return phone.equals(bean.getPhone());
		} else {
			return false;
		}
	}

	//保证TelSmsSafeActivity中添加黑名单时能顺利删除已经存在的数据
	@Override
	public int hashCode() {
		return phone.hashCode();
	}

	@Override
	public String toString() {
		return "[phone=" + phone + ", mode=" + mode + "]";
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}
}