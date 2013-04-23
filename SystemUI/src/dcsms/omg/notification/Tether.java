package dcsms.omg.notification;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Environment;
import dcsms.omg.util.CekStatus;
import dcsms.omg.util.Tema;
import dcsms.omg.util.XLog;

public class Tether extends TGL {
	private String ON = "getTetherableIfaces";
	private String OFF = "getTetheredIfaces";
	private String onteter = "tether";
	private String offteter = "untether";
	private static String[] mUsbRegexs;
	static ConnectivityManager cm;

	public Tether(Context c) {
		super(c);
		cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		mUsbRegexs = cm.getTetherableUsbRegexs();
	}

	@Override
	public String getStatus() {
		// TODO Auto-generated method stub
		return CekStatus.USBTETHER(cekUSBTether(), context);
	}

	@Override
	public Drawable getIcon() {
		if (cekUSBTether())
			return mTema.getICON(Tema.IKON_USB_TETHER_ON);
		else
			return mTema.getICON(Tema.IKON_USB_TETHER_OFF);
	}

	@Override
	public void updateMe() {
		OnOffTether();
		super.updateMe();
	}

	public void OnOffTether() {
		boolean newState = cekUSBTether();
		if (newState)
			enableTeter(false);
		else
			enableTeter(true);

	}

	public boolean cekUSBTether() {
		return updateState();
	}

	private boolean updateState() {
		String[] available = cm.getTetherableIfaces();
		String[] tethered = cm.getTetheredIfaces();
		String[] errored = cm.getTetheringErroredIfaces();
		return updateState(available, tethered, errored);
	}

	private static boolean updateState(Object[] available, Object[] tethered,
			Object[] errored) {

		boolean me;
		boolean usbTethered = false;
		boolean usbAvailable = false;
		int usbError = ConnectivityManager.TETHER_ERROR_NO_ERROR;
		boolean usbErrored = false;
		boolean massStorageActive = Environment.MEDIA_SHARED.equals(Environment
				.getExternalStorageState());
		for (Object o : available) {
			String s = (String) o;
			for (String regex : mUsbRegexs) {
				if (s.matches(regex)) {
					usbAvailable = true;
					if (usbError == ConnectivityManager.TETHER_ERROR_NO_ERROR) {
						usbError = cm.getLastTetherError(s);
					}
				}
			}
		}
		for (Object o : tethered) {
			String s = (String) o;
			for (String regex : mUsbRegexs) {
				if (s.matches(regex))
					usbTethered = true;
			}
		}
		for (Object o : errored) {
			String s = (String) o;
			for (String regex : mUsbRegexs) {
				if (s.matches(regex))
					usbErrored = true;
			}
		}

		if (usbTethered) {

			XLog.s("USB TETHER", "teter");
			me = true;
		} else if (usbAvailable) {
			if (usbError == ConnectivityManager.TETHER_ERROR_NO_ERROR) {
				me = false;
				XLog.s("USB TETHER", "teter available");
			} else {
				XLog.s("USB TETHER", "teter eror");
				me = false;
			}
			XLog.s("USB TETHER", "teter available");
			me = false;
		} else if (usbErrored) {
			XLog.s("USB TETHER", "errror teter");
			me = false;
		} else if (massStorageActive) {
			XLog.s("USB TETHER", "mastorage actip");
			me = false;
		} else {
			XLog.s("USB TETHER", "not available");
			me = false;
		}
		return me;
	}

	private void enableTeter(boolean bool) {
		String getitfc;
		String ifname;
		if (bool) {
			getitfc = ON;
			ifname = onteter;
		} else {
			getitfc = OFF;
			ifname = offteter;
		}
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		String[] available = null;
		int code = -1;
		Method[] wmMethods = cm.getClass().getDeclaredMethods();

		for (Method method : wmMethods) {
			if (method.getName().equals(getitfc)) {
				try {
					available = (String[]) method.invoke(cm);
					break;
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				} catch (ArrayIndexOutOfBoundsException e) {
					enableTeter(false);
					e.printStackTrace();
					return;
				}
			}
		}

		for (Method method : wmMethods) {
			if (method.getName().equals(ifname)) {
				try {
					code = (Integer) method.invoke(cm, available[0]);

				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					return;
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					return;
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				} catch (ArrayIndexOutOfBoundsException e) {
					e.printStackTrace();
					// Log.e("ERROR ENABLINg", "ifname");
					return;
				}
				break;
			}
		}
	}

	public static Drawable getBimtap(boolean teter, Context mContext) {
		if (teter)
			return new Tema(mContext).getICON(Tema.IKON_USB_TETHER_ON);
		else
			return new Tema(mContext).getICON(Tema.IKON_USB_TETHER_OFF);
	}

}
