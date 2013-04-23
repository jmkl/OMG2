package dcsms.omg.notification;

import dcsms.omg.util.K;
import dcsms.omg.util.XLog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.view.KeyEvent;

public class Musik {
	public static final String MUSIK_PLAY_STATECHANGE = "com.android.music.playstatechanged";
	protected static String MUSIK_PREV = "com.android.music.musicservicecommand.previous";
	protected static String MUSIK_NEXT = "com.android.music.musicservicecommand.next";
	protected static String MUSIK_PLAYPAUSE = "com.android.music.musicservicecommand.togglepause";
	private static final int MEDIA_STATE_UNKNOWN = -1;
	private static final int MEDIA_STATE_INACTIVE = 0;
	private static final int MEDIA_STATE_ACTIVE = 1;
	private static int mCurrentState = MEDIA_STATE_UNKNOWN;
	
	
	protected static boolean isMusicActive(Context context) {
		
		if (mCurrentState == MEDIA_STATE_UNKNOWN) {
			mCurrentState = MEDIA_STATE_INACTIVE;
			
			AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
			if (am != null) {
				mCurrentState = (am.isMusicActive() ? MEDIA_STATE_ACTIVE: MEDIA_STATE_INACTIVE);
			}			
			return (mCurrentState == MEDIA_STATE_ACTIVE);
			
		} else {
			boolean active = (mCurrentState == MEDIA_STATE_ACTIVE);
			mCurrentState = MEDIA_STATE_UNKNOWN;			
			return active;
		}
		
		
	}

	


	public static void sendMusicBroadCast(Context mContext, int code, String nextorprev) {
		Intent i = new Intent(nextorprev);
		KeyEvent key = null;
		if (nextorprev.equals(MUSIK_NEXT))
			key = new KeyEvent(KeyEvent.ACTION_DOWN,
					KeyEvent.KEYCODE_MEDIA_NEXT);
		else if (nextorprev.equals(MUSIK_PREV))
			key = new KeyEvent(KeyEvent.ACTION_DOWN,
					KeyEvent.KEYCODE_MEDIA_PREVIOUS);
		else if (nextorprev.equals(MUSIK_PLAYPAUSE))
			key = new KeyEvent(KeyEvent.ACTION_DOWN,
					KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE);

		i.putExtra(Intent.EXTRA_KEY_EVENT, key);
		if (key != null)
			mContext.sendBroadcast(i);

	}
}
