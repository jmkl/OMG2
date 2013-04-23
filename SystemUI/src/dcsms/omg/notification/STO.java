package dcsms.omg.notification;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import dcsms.omg.util.Tema;

public class STO extends TGL {
	public STO(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getStatus() {
		return howlong(context);
	}

	@Override
	public Drawable getIcon() {
		return getICON();
	}

	@Override
	public void updateMe() {
		update(context);
		super.updateMe();
	}

	/**
	 * get skrin time out teks
	 * */
	private String howlong(Context mContext) {
		int status = gettime(mContext);
		int sekon = status / 1000;

		if (sekon > 60)
			return (Integer.toString(sekon / 60) + " m");
		else
			return (Integer.toString(sekon) + " s");
	}

	public static final void update(Context c) {
		int me = gettime(c);
		if (me == 15000) {
			// setICON(Tema.IKON_TO_ON);
			setTimeOut(c, 1);
		} else if (me == 30000) {
			// setICON(Tema.IKON_TO_ON);
			setTimeOut(c, 2);
		} else if (me == 60000) {
			// setICON(Tema.IKON_TO_ON);
			setTimeOut(c, 3);
		} else if (me == 120000) {
			// setICON(Tema.IKON_TO_ON);
			setTimeOut(c, 4);
		} else if (me == 1800000) {
			// //setICON(Tema.IKON_TO_ON);
			setTimeOut(c, 5);
		} else if (me == -1) {
			setTimeOut(c, 0);
		}

	}

	public static final int gettime(Context mContext) {
		int me = 0;
		try {
			me = Settings.System.getInt(mContext.getContentResolver(),
					Settings.System.SCREEN_OFF_TIMEOUT);
		} catch (SettingNotFoundException e) {
			e.printStackTrace();
		}
		return me;
	}

	private static void setTimeOut(Context mContext, int time) {
		switch (time) {
		case 0:
			time = 15000;
			break;

		case 1:
			time = 30000;
			break;
		case 2:
			time = 60000;
			break;
		case 3:
			time = 120000;
			break;
		case 4:
			time = 1800000;
			break;
		case 5:
			time = -1;
			break;
		}

		Settings.System.putInt(mContext.getContentResolver(),
				Settings.System.SCREEN_OFF_TIMEOUT, time);
	}

	private Drawable getICON() {
		return mTema.getICON(Tema.IKON_TO_ON);
	}

}
