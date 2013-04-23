package dcsms.omg.notification;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import dcsms.omg.util.Tema;

public class Senter extends TGL {
	public Senter(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getStatus() {
		return "FlashLight";
	}

	@Override
	public Drawable getIcon() {
		return mTema.getICON(Tema.IKON_SENTER);
	}

	@Override
	public void updateMe() {
		Intent i = new Intent();
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.setClass(context, ChuckNorris.class);
		i.putExtra("SENTER", true);
		context.startActivity(i);
		SS.expandSB(context);
	}

	public static final String get() {
		return "FlashLight";
	}

	public static final void me(Context mContext) {
		Intent i = new Intent();
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.setClass(mContext, ChuckNorris.class);
		i.putExtra("SENTER", true);
		mContext.startActivity(i);
	}

	public static Drawable getBimtap(Context mContext) {

		return new Tema(mContext).getICON(Tema.IKON_SENTER);
	}

}
