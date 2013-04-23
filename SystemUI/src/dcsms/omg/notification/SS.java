package dcsms.omg.notification;

import android.app.StatusBarManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import dcsms.omg.util.Tema;

public class SS extends TGL{
	public SS(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	@Override
	public String getStatus() {
		return "ScreenCapture";
	}
	@Override
	public Drawable getIcon() {
		return mTema.getICON(Tema.IKON_SS);
	}
	@Override
	public void updateMe() {
		final Intent i = new Intent();
		i.setAction("com.sec.android.app.screencapture.capture");
		Handler h = new Handler();
		h.postDelayed(new Runnable() {			
			@Override
			public void run() {
				context.startService(i);	
				
			}
		}, 1000);	
		expandSB(context);
	}
	public static final String get(){
		return "ScreenCapture";
	}
	public static final void crot(final Context c) {
		final Intent i = new Intent();
		i.setAction("com.sec.android.app.screencapture.capture");
		Handler h = new Handler();
		h.postDelayed(new Runnable() {			
			@Override
			public void run() {
				c.startService(i);	
				
			}
		}, 1000);	
		expandSB(c);
	}
	public static void expandSB(Context c){
		StatusBarManager sb =(StatusBarManager)c.getSystemService(Context.STATUS_BAR_SERVICE);
		sb.collapse();
	}
	public static Drawable getBimtap(Context mContext) {

			return new Tema(mContext).getICON(Tema.IKON_SS);
	}

}
