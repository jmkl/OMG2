package dcsms.omg.notification;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import dcsms.omg.util.Tema;

public class OFF extends TGL{
	public OFF(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	@Override
	public String getStatus() {
		return "Shutdown";
	}
	@Override
	public Drawable getIcon() {
		return mTema.getICON(Tema.IKON_FCUKDOWN);
	}
	@Override
	public void updateMe() {
		Intent i = new Intent();
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.setClass(context, ChuckNorris.class);
		i.putExtra("BOOT", true);
		context.startActivity(i);		
		SS.expandSB(context);
	}
	public static final String get() {
		return "Shutdown";
	}
	public static final void me(Context mContext){
		Intent i = new Intent();
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.setClass(mContext, ChuckNorris.class);
		i.putExtra("BOOT", true);
		mContext.startActivity(i);
	}
	public static Drawable getBimtap(Context c) {
		// TODO Auto-generated method stub
		return new Tema(c).getICON(Tema.IKON_FCUKDOWN);
	}
}
