package com.net13.sean.mobileguard.activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;

import com.net13.sean.mobileguard.R;


/**
 * Created by SEAN on 2017/4/21.
 */

public abstract class BaseSetupActivity extends Activity {
	private GestureDetector gd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initGesture();//初始化手势识别器

		//Setup4Activity内的复选框->要求先设置事件,后初始化数据,因为初始化数据时会出发事件
		initData();//初始数据
		initEvent();//初始化组件的事件
	}

	public void initData() {

	}

	public void initEvent() {

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		gd.onTouchEvent(event);//绑定onTouch事件
		return super.onTouchEvent(event);
	}

	private void initGesture() {
		//初始化手势识别器,要想手势识别器生效，绑定onTouch事件
		gd = new GestureDetector(new OnGestureListener() {

			//覆盖此方法完成手势的切换效果

			/**
			 * e1,按下的点
			 * e2 松开屏幕的点
			 * velocityX x轴方向的速度
			 * velocityY y轴方向的速度
			 */
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
								   float velocityY) {
				//x轴方向的速度是否满足横向滑动的条件 pix/s
				if (Math.abs(velocityX) > 200) { //速度大于200像素每秒
					//可以完成滑动
					float dx = e2.getX() - e1.getX();//x轴方向滑动的间距
					if (Math.abs(dx) < 100) {
						return true;//如果间距不符合直接无效
					}
					if (dx < 0) {//从右往左滑动
						next(null);//不是组件的事件调用
					} else {//从左往右滑动
						prev(null);
					}
				}
				return true;
			}

			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				return false;
			}

			@Override
			public void onShowPress(MotionEvent e) {

			}

			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
									float distanceY) {
				return false;
			}

			@Override
			public void onLongPress(MotionEvent e) {
			}


			@Override
			public boolean onDown(MotionEvent e) {
				return false;
			}
		});
	}

	/**
	 * 子类需要覆盖此方法，来完成界面的显示
	 */
	public abstract void initView();

	/**
	 * 下一个页面的事件处理
	 *
	 * @param v
	 */
	public void next(View v) {
		// 1,完成界面的切换
		nextActivity();

		// 2,动画的播放
		nextAnimation();// 界面之间切换的动画
		/*
		 * Intent next = new Intent(this,Setup2Activity.class);
		 * startActivity(next);
		 */
	}


	/**
	 * 上一个界面的事件处理
	 * @param v
	 */
	public void prev(View v) {
		// 1,完成界面的切换
		prevActivity();
		// 2,动画的播放
		prevAnimation();// 界面之间企划的动画
	}

	/**
	 * 下一个界面显示的动画
	 */
	private void nextAnimation() {
		//第一个参数进来的动画，第二个参数是出去的动画
		overridePendingTransition(R.anim.next_in, R.anim.next_out);
	}

	/**
	 * 上一个界面进来的动画
	 */
	private void prevAnimation() {
		overridePendingTransition(R.anim.prev_in, R.anim.prev_out);
	}

	/**
	 * 共有的界面跳转封装
	 *
	 * @param type
	 */
	public void startActivity(Class type) {
		Intent next = new Intent(this, type);
		startActivity(next);
		finish();//关闭自己
	}


	public abstract void nextActivity();

	public abstract void prevActivity();

}