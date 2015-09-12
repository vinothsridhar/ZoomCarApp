package com.sri.zoomcar.app.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.util.LruCache;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;


public class ViewUtils {

	public final static String FONTAWESOME = "FONTAWESOME";
	private static final LruCache<String, Typeface> sTypefaceCache = new LruCache<String, Typeface>(12);
	
	public static void toast(Context c, String message) {
		Toast.makeText(c.getApplicationContext(), message, Toast.LENGTH_LONG).show();
	}
	
	public static void setupUI(View view, final Activity activity) {

	    if(!(view instanceof EditText)) {
	        view.setOnTouchListener(new OnTouchListener() {
	            public boolean onTouch(View v, MotionEvent event) {
	            	ViewUtils.hideSoftKeyboard(activity);
	                return false;
	            }

	        });
	    }

	    //If a layout container, iterate over children and seed recursion.
	    if (view instanceof ViewGroup) {
	        for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
	            View innerView = ((ViewGroup) view).getChildAt(i);
	            setupUI(innerView, activity);
	        }
	    }
	}
	
	public static void hideSoftKeyboard(Activity activity) {
		try {
			InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
		} catch(Exception ex) {
			//Device with physical keyboard
		}
	}

	public static void hideSoftKeyboard(Activity activity, EditText editText) {
		try {
			InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
		} catch(Exception ex) {
			//Device with physical keyboard
		}
	}

	public static Typeface getFont(Context c, String name) {
		Typeface typeface = sTypefaceCache.get(name);
		if (typeface == null) {
			typeface = Typeface.createFromAsset(c.getApplicationContext().getAssets(), "fontawesome-webfont.ttf");
			sTypefaceCache.put(name, typeface);
		}
		return sTypefaceCache.get(name);
	}
	
	/*public static SweetAlertDialog getDialog(Activity activity) {
		return new SweetAlertDialog(activity);
	}
	
	public static SweetAlertDialog alert(SweetAlertDialog dialog, String title, String message) {
		return alert(dialog, SweetAlertDialog.NORMAL_TYPE, title, message);
	}
	
	public static SweetAlertDialog alert(SweetAlertDialog dialog, int type, String title, String message) {
		if (dialog.getAlerType() != type) {
			dialog.changeAlertType(type);
		}
		return dialog.setTitleText(title).setContentText(message);
	}*/
}
