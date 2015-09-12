package com.sri.zoomcar.app.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import com.sri.zoomcar.app.utils.ViewUtils;


public class ButtonAwesome extends Button{

	public ButtonAwesome(Context context) {
		super(context);
		init();

	}
	public ButtonAwesome(Context context,AttributeSet attrs) {
		super(context,attrs);
		init();
	}
	public ButtonAwesome(Context context, AttributeSet attrs, int defStyle){
		super(context,attrs,defStyle);
		init();
	}

	@SuppressLint("NewApi")
	public void init(){
		setTypeface(ViewUtils.getFont(getContext(), ViewUtils.FONTAWESOME));
	}
}


