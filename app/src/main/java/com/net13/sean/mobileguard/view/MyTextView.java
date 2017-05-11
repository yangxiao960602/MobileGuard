package com.net13.sean.mobileguard.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by SEAN on 2017/4/20.
 */

public class MyTextView extends android.support.v7.widget.AppCompatTextView {

	public MyTextView(Context context) {
		super(context);
	}

	public MyTextView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	public MyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	/**
	 * Returns true if this view has focus
	 * @return True if this view has focus, false otherwise.
	 */
	@Override
	public boolean isFocused() {
		return true;
	}
}
