package dcsms.omg.util;

import android.util.Log;

public class XLog {
	static boolean DEBUG = true;

	public static final void s(String TAG, String msg) {
		if (DEBUG) Log.d(TAG, msg);
	}
}
