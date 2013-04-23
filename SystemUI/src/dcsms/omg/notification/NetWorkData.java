package dcsms.omg.notification;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.telephony.TelephonyManager;
import dcsms.omg.util.Tema;

public class NetWorkData extends TGL {
	
	
	public NetWorkData(Context context) {
		super(context);
	}
	
	@Override
	public String getStatus() {
		return getStatus(context)?"Data On":"Data Off";
	}
	@Override
	public Drawable getIcon() {
		if(getStatus(context))
			return new Tema(context).getICON(Tema.IKON_DATA_ON);
		else
			return new Tema(context).getICON(Tema.IKON_DATA_OFF);
	}
	@Override
	public void updateMe() {
		ConnectivityManager con = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		con.setMobileDataEnabled(!getStatus(context));
		super.updateMe();
	}
	
	
	public static String state(Context mContext){
		return getStatus(mContext)?"Data On":"Data Off";
	}
	public static void setState(Context context){
		ConnectivityManager con = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		con.setMobileDataEnabled(!getStatus(context));
	}
	
	
	private static boolean getStatus(Context mContext) {
		int stat = 0;
		TelephonyManager telephonyManager = (TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE);
		switch (telephonyManager.getDataState()) {
		case TelephonyManager.DATA_CONNECTED:
			stat = 1;
			break;
		case TelephonyManager.DATA_DISCONNECTED:
			stat = 0;
			break;
		}

		return stat==1;
	}
	public static Drawable getBimtap(Context mContext) {
		if(getStatus(mContext))
			return new Tema(mContext).getICON(Tema.IKON_DATA_ON);
		else
			return new Tema(mContext).getICON(Tema.IKON_DATA_OFF);
	}

}
