package dcsms.omg.notification;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiManager;
import dcsms.omg.util.Tema;

public class WiFi extends TGL {
	public WiFi(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	@Override
	public String getStatus() {
		// TODO Auto-generated method stub

		return getStatusState()?"WiFi On":"WiFi Off";
	}
	@Override
	public Drawable getIcon() {
		if(getStatusState())
			return mTema.getICON(Tema.IKON_WIFI_ON);
		else
			return mTema.getICON(Tema.IKON_WIFI_OFF);
	}
	@Override
	public void updateMe() {
		setWiFi();
		super.updateMe();
	}
	
	private boolean getStatusState() {
		WifiManager wifi = (WifiManager) context 
				.getSystemService(Context.WIFI_SERVICE);
		if (wifi.isWifiEnabled())
			return true;
		else
			return false;
	}
	private void setWiFi() {
		boolean onoff = getStatusState();
		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		wifi.setWifiEnabled(onoff?false:true);
	}	

}
