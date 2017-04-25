package com.net13.sean.mobileguard.activities;

import com.net13.sean.mobileguard.domain.ContactsBean;
import com.net13.sean.mobileguard.engine.ReadContactsEngine;

import java.util.List;


/**
 * Created by SEAN on 2017/4/21.
 */


/**
 * 显示所有好友信息的界面
 */

public class CalllogsActivity extends BaseFriendsCallSmsActivity {

	/**
	 * 提取数据的核心方法需要覆盖此方法完成数据的显示
	 *
	 * @return
	 */
	@Override
	public List<ContactsBean> getDatas() {
		return ReadContactsEngine.readCalllog(getApplicationContext());
	}
}
