package dcsms.omg.notification;

import dcsms.omg.util.K;
import dcsms.omg.util.Tema;
import dcsms.omg.util.XLog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;

public class MPlay extends Musik {
	public static  String state(Context c) {
		if(isMusicActive(c))
			XLog.s(K.TAG_E, "actip");
		else
			XLog.s(K.TAG_E, "not actip");
		return isMusicActive(c) ? "Pause" : "Play";
	}
	public static void play(Context c) {
		sendMusicBroadCast(c, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE, MUSIK_PLAYPAUSE);
	}
	public static Drawable getBimtap(Context mContext) {
		if(isMusicActive(mContext))
			return new Tema(mContext).getICON(Tema.IKON_MUSIK_PAUSE);
		else
			return new Tema(mContext).getICON(Tema.IKON_MUSIK_PLAY);
	}

}
