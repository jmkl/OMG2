package dcsms.omg.notification;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import dcsms.omg.util.Tema;

public class AirPlane extends TGL {
	public AirPlane(Context context) {
		super(context);
	}

	@Override
	public String getStatus() {
		return get(context) ? "AirPlane On" : "AirPlane Off";
	}

	@Override
	public void updateMe() {
		boolean state = get(context);
		Settings.System.putInt(context.getContentResolver(),
				Settings.System.AIRPLANE_MODE_ON, state ? 0 : 1);
		Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
		intent.putExtra("state", !state);
		context.sendBroadcast(intent);
		setIcon(getIcon());
		
		super.updateMe();
	}

	@Override
	public Drawable getIcon() {
		if (get(context))
			return mTema.getICON(Tema.IKON_AIRPLANE_ON);
		else
			return mTema.getICON(Tema.IKON_AIRPLANE_OFF);
	}
	

	private boolean get(Context context) {
		return Settings.System.getInt(context.getContentResolver(),
				Settings.System.AIRPLANE_MODE_ON, 0) == 1;
	}

}
