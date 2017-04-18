package com.net13.sean.mobileguard.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.net13.sean.mobileguard.R;

/**
 * 主界面
 * Created by SEAN on 2017/4/18.
 */

public class HomeActivity extends Activity {
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}

	private void initView() {
		setContentView(R.layout.activity_home);
	}
}
