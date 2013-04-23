package dcsms.omg.notification;

import java.lang.reflect.Method;

import dcsms.omg.util.Tema;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiManager;

public class WiFiAp extends TGL {
	public WiFiAp(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	private enum WIFI_AP_STATE {
		WIFI_AP_STATE_DISABLING, WIFI_AP_STATE_DISABLED, WIFI_AP_STATE_ENABLING, WIFI_AP_STATE_ENABLED, WIFI_AP_STATE_FAILED
	}

	@Override
	public String getStatus() {
		// TODO Auto-generated method stub

		return getStatusbool() ? "WiFiAp On" : "WiFiAp Off";
	}

	@Override
	public Drawable getIcon() {
		if (getStatusbool())
			return mTema.getICON(Tema.IKON_WIFI_HS_ON);
		else
			return mTema.getICON(Tema.IKON_WIFI_HS_OFF);

	}

	@Override
	public void updateMe() {
		setWiFi();
		super.updateMe();
	}

	private boolean getStatusbool() {
		return getWifiApState(context) == WIFI_AP_STATE.WIFI_AP_STATE_ENABLED;
	}

	private void setWiFi() {
		boolean onoff = getStatusbool();
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		Method[] methods = wifiManager.getClass().getDeclaredMethods();
		for (Method method : methods) {
			if (method.getName().equals("setWifiApEnabled")) {
				try {
					method.invoke(wifiManager, null, onoff ? false : true);
				} catch (Exception ex) {
				}
				break;
			}
		}
	}

	private static WIFI_AP_STATE getWifiApState(Context mContext) {
		WifiManager mWifiManager = (WifiManager) mContext
				.getSystemService(Context.WIFI_SERVICE);

		try {

			Method method = mWifiManager.getClass().getMethod("getWifiApState");

			int tmp = ((Integer) method.invoke(mWifiManager));

			if (tmp > 10) {
				tmp = tmp - 10;
			}

			return WIFI_AP_STATE.class.getEnumConstants()[tmp];
		} catch (Exception e) {
			return WIFI_AP_STATE.WIFI_AP_STATE_FAILED;
		}
	}

}
