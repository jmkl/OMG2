package dcsms.omg.util;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.provider.Settings;

public class CekStatus {
	public static String AUTOROTATE(Context mContext) {
		return Settings.System.getString(mContext.getContentResolver(),
				Settings.System.ACCELEROMETER_ROTATION);
	}

	public static boolean BLUETOOTH() {
		BluetoothAdapter blutut = BluetoothAdapter.getDefaultAdapter();
		return blutut.isEnabled();
	}

	public static String USBTETHER(boolean isTether,Context c) {
		if (isTether)
			return "USBTether On";
		else
			return "USBTether Off";
	}
}
