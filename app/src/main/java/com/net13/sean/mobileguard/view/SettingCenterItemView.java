package com.net13.sean.mobileguard.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.net13.sean.mobileguard.R;


/**
 * Created by SEAN on 2017/4/23.
 * 自定义组合控件
 */
public class SettingCenterItemView extends LinearLayout {

	private TextView tv_title;
	private TextView tv_content;
	private CheckBox cb_check;
	private String[] contents;
	private View item;

	/**
	 * 配置文件中，反射实例化设置属性参数
	 *
	 * @param context
	 * @param attrs
	 */
	public SettingCenterItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
		initEvent();

		String title = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "title");
		String content = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "content");

		tv_title.setText(title);
		contents = content.split("-");

	}


	/**
	 * 代码实例化调用该构造函数
	 *
	 * @param context
	 */
	public SettingCenterItemView(Context context) {
		super(context);
		initView();
	}


	/**
	 * 初始化复选框事件
	 */
	private void initEvent() {
		//item 相对布局
		item.setOnClickListener(new OnClickListener() {

			//通过整个相对布局的点击事件来切换CheckBox的选中状态
			@Override
			public void onClick(View v) {
				cb_check.setChecked(!cb_check.isChecked());
			}
		});

		//复选框变化事件
		cb_check.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					//设置选中的颜色为绿色
					tv_content.setTextColor(Color.GREEN);
					tv_content.setText(contents[1]);
				} else {
					//设置选中的颜色为绿色
					tv_content.setTextColor(Color.RED);
					tv_content.setText(contents[0]);
				}
			}
		});
	}

	/**
	 * 初始化LinearLayout的子组件
	 */
	private void initView() {
		//把布局文件转换成一个view组件
		item = View.inflate(getContext(), R.layout.item_settingcenter_view, null);
		//显示标题
		tv_title = (TextView) item.findViewById(R.id.tv_settingcenter_autoupdate_title);
		//显示的内容
		tv_content = (TextView) item.findViewById(R.id.tv_settingcenter_autoupdate_content);
		//设置复选框
		cb_check = (CheckBox) item.findViewById(R.id.cb_settingcenter_autoupdate_checked);
		//将view组件加载到线性布局中
		addView(item);
	}

}
