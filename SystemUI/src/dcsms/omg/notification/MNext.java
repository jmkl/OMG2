package dcsms.omg.notification;

import dcsms.omg.util.Tema;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;

public class MNext extends Musik {
	public static  String state(Context c) {
		return "Next";
	}
	public static void next(Context c) {
		sendMusicBroadCast(c, KeyEvent.KEYCODE_MEDIA_NEXT, MUSIK_NEXT);
	}
	public static Drawable getBimtap(Context c) {
		// TODO Auto-generated method stub
		return new Tema(c).getICON(Tema.IKON_MUSIK_NEXT);
	}
}
