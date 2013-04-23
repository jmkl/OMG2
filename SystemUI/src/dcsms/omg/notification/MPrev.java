package dcsms.omg.notification;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import dcsms.omg.util.Tema;

public class MPrev extends Musik {
	public static  String state(Context c) {
		return "Prev";
	}
	public static void prev(Context c) {
		sendMusicBroadCast(c, KeyEvent.KEYCODE_MEDIA_PREVIOUS, MUSIK_PREV);
	}
	public static Drawable getBimtap(Context c) {
		// TODO Auto-generated method stub
		return new Tema(c).getICON(Tema.IKON_MUSIK_PREV);
	}
}
