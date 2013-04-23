package dcsms.omg.notification;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import dcsms.omg.util.Tema;

public class GPS extends TGL {	
	public GPS(Context context) {
		super(context);
	}	
	@Override
	public String getStatus() {
		return getGpsState(context)?"GPS On":"GPS Off";
	}
	@Override
	public Drawable getIcon() {
		if(getGpsState(context))
			return new Tema(context).getICON(Tema.IKON_GPS_ON);
		else
			return new Tema(context).getICON(Tema.IKON_GPS_OFF);
	}
	@Override
	public void updateMe() {
		boolean enabled = getGpsState(context);        
		String provider = Settings.Secure.getString(
				context.getContentResolver(),
				Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		
			if (enabled?provider.contains("gps"):!provider.contains("gps")) { // if gps is disabled
				final Intent poke = new Intent();
				poke.setClassName("com.android.settings",
				"com.android.settings.widget.SettingsAppWidgetProvider");
				poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
				poke.setData(Uri.parse("3"));
				context.sendBroadcast(poke);
			}
		super.updateMe();
	}
	
	public static String get(Context context){
		return getGpsState(context)?"GPS On":"GPS Off";
	}
    public static void update(Context context) {
        boolean enabled = getGpsState(context);        
		String provider = Settings.Secure.getString(
				context.getContentResolver(),
				Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		
			if (enabled?provider.contains("gps"):!provider.contains("gps")) { // if gps is disabled
				final Intent poke = new Intent();
				poke.setClassName("com.android.settings",
				"com.android.settings.widget.SettingsAppWidgetProvider");
				poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
				poke.setData(Uri.parse("3"));
				context.sendBroadcast(poke);
			}
			
    }
	public static boolean getGpsState(Context context) {
		ContentResolver resolver = context.getContentResolver();
		return Settings.Secure.isLocationProviderEnabled(resolver,LocationManager.GPS_PROVIDER);
	}
	public static Drawable getBimtap(Context mContext) {
		if(getGpsState(mContext))
			return new Tema(mContext).getICON(Tema.IKON_GPS_ON);
		else
			return new Tema(mContext).getICON(Tema.IKON_GPS_OFF);
	}

}
