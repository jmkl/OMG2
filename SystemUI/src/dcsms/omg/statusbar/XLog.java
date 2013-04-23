package dcsms.omg.statusbar;

import android.util.Log;

public class XLog {
	static boolean LOG=true;
	public static void i(int value) {
		if(LOG)Log.d("dcsms", Integer.toString(value));
	}

	public static void s(String value) {
		if(LOG)Log.d("dcsms", value);
	}
	public static void s(String TAG,String value) {
		if(LOG)Log.d(TAG, value);
	}

	public static void f(float value) {
		if(LOG)Log.d("dcsms", Float.toString(value));
	}
}
