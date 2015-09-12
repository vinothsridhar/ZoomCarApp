package com.sri.zoomcar.app.utils;

import android.util.Log;

public class L {

	private static final boolean debug = LocalConfig.DEBUG;
	private static final String TAG = "EmpApp";

	public static void i(String msg) {
		i(TAG, msg);
	}

	public static void i(String tag, String msg) {
		log(tag,msg,Log.INFO);
	}
	
	public static void e(String msg) {
		e(TAG,msg);
	}
	
	public static void e(String tag,String msg) {
		log(tag,msg,Log.ERROR);
	}
	
	public static void d(String msg) {
		d(TAG,msg);
	}
	
	public static void d(String tag,String msg) {
		log(tag,msg,Log.DEBUG);
	}
	
	public static void d(String tag, Exception e) {
		d(tag, "Exception: ", e);
	}
	
	public static void d(String tag, String msg, Exception e) {
		log(tag, msg + "\n" + e.toString(), Log.DEBUG);
	}
	
	public static void v(String msg) {
		v(TAG,msg);
	}
	
	public static void v(String tag,String msg) {
		log(tag,msg,Log.VERBOSE);
	}
	
	public static void w(String msg) {
		w(TAG,msg);
	}
	
	public static void w(String tag,String msg) {
		log(tag,msg,Log.WARN);
	}
	
	public static void wtf(String msg) {
		wtf(TAG,msg);
	}
	
	public static void wtf(String tag,String msg) {
		log(tag,msg,Log.ASSERT);
	}
	
	public static void log(String tag, String msg, int type) {
		if (debug) {
			switch (type) {
			case Log.INFO:
				Log.i(tag, msg);
				break;
			case Log.DEBUG:
				Log.d(tag, msg);
				break;
			case Log.ERROR:
				Log.e(tag, msg);
				break;
			case Log.VERBOSE:
				Log.v(tag, msg);
				break;
			case Log.ASSERT:
				Log.wtf(tag, msg);
				break;
			case Log.WARN:
				Log.w(tag, msg);
				break;
			}
		}
	}

}
