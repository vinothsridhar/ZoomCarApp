package com.sri.zoomcar.app.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.sri.zoomcar.app.utils.ViewUtils;


public class TextAwesome extends TextView {

	public TextAwesome(Context context) {
		super(context);
		init();
	}

	public TextAwesome(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public void init() {
		setTypeface(ViewUtils.getFont(getContext(), ViewUtils.FONTAWESOME));
	}

}


