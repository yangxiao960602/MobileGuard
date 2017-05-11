package com.net13.sean.mobileguard.activities;

import com.net13.sean.mobileguard.R;


/**
 * Created by SEAN on 2017/4/21.
 */


/**
 * @author Administrator
 *         第一个设置向导界面
 */
public class Setup1Activity extends BaseSetupActivity {

	/**
	 * 子类需要覆盖此方法，来完成界面的显示
	 */
	@Override
	public void initView() {
		setContentView(R.layout.activity_setup1);
	}

	@Override
	public void nextActivity() {
		// 跳转到第二个设置向导界面
		/*Intent intent = new Intent(this, Setup2Activity.class);
		startActivity(intent);*/

		startActivity(Setup2Activity.class);
	}

	@Override
	public void prevActivity() {

	}

}
