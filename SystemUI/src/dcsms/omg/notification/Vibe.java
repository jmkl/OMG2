package dcsms.omg.notification;

import dcsms.omg.util.Tema;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;

public class Vibe extends TGL{

	private static AudioManager audioManager;
	
	public Vibe(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
@Override
public String getStatus() {
	// TODO Auto-generated method stub
	return CurrentStatus(context);
}
@Override
public Drawable getIcon() {
	return getdrawable();
}
@Override
public void updateMe() {
	update(context);
	super.updateMe();
}

	private String CurrentStatus(Context c) {
		int status = status(c);
		if (status == 0)
			return "Silent";
		else if (status == 1)
			return "Vibrate";
		else if (status == 2)
			return "Ring n Vibe";
		else
			return "Unknown";

	}

	public static final void update(Context c) {
		int status = status(c);
		if (status == 0)
			audioManager.setRingerMode(1);
		else if (status == 1)
			audioManager.setRingerMode(2);
		else if (status == 2)
			audioManager.setRingerMode(0);

	}

	public static final int status(Context c) {
		audioManager = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
		return audioManager.getRingerMode();
	}

	private Drawable getdrawable() {
		if(status(context)==0)
			return mTema.getICON(Tema.IKON_MUTE_OFF);
		else if(status(context)==1)
			return mTema.getICON(Tema.IKON_VIBRATE_OFF);
		else
			return mTema.getICON(Tema.IKON_VIBRATE_ON);
	}

}
