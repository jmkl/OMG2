package dcsms.omg.statusbar;
/*
 * Copyright (C) 2013 jimikill
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import dcsms.omg.util.SBK;
import dcsms.omg.util.Sett;
import dcsms.omg.util.Tema;
import dcsms.omg.util.getPref;

public class Sinyal extends SBIcon {

	private ImageView sinyal, inout, alarm, blutut, headset, ringmode;
	boolean isAlarm = false, isBlutut = false, isHeadset = false;

	boolean debug = false;

	FrameLayout overlap_layout;
	private boolean overlap, IOkanan;

	public Sinyal(Context context, AttributeSet attrs) {
		super(context, attrs);
		Inisiasi();
	}


	@Override
	protected void Inisiasi() {
		super.Inisiasi();
		setOrientation(LinearLayout.HORIZONTAL);
		setGravity(Gravity.CENTER_VERTICAL);

	}

	public void aturulangLayout() {
		Log.e(getClass().getSimpleName(), "ATURULANG");
		overlap = pref.getBoolean(Sett.Overlap, true);
		IOkanan = pref.getBoolean(Sett.SinyalDikanan, false);

		if (sinyal != null)
			removeView(sinyal);
		if (inout != null)
			removeView(inout);
		if (overlap_layout != null)
			removeView(overlap_layout);
		if (alarm != null)
			removeView(alarm);
		if (blutut != null)
			removeView(blutut);
		if (headset != null)
			removeView(headset);
		if (ringmode != null)
			removeView(ringmode);

		System.gc();

		LayoutParams par1 = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.MATCH_PARENT);

		overlap_layout = new FrameLayout(mContext);
		sinyal = new ImageView(mContext);
		inout = new ImageView(mContext);

		blutut = new ImageView(mContext);
		alarm = new ImageView(mContext);
		headset = new ImageView(mContext);
		ringmode = new ImageView(mContext);

		sinyal.setAdjustViewBounds(true);
		inout.setAdjustViewBounds(true);

		blutut.setAdjustViewBounds(true);
		headset.setAdjustViewBounds(true);
		alarm.setAdjustViewBounds(true);
		ringmode.setAdjustViewBounds(true);

		if (overlap) {
			overlap_layout.setLayoutParams(par1);
			overlap_layout.addView(inout, 0);
			overlap_layout.addView(sinyal, 1);
			addView(ringmode, 0);
			addView(overlap_layout, 1);
			addView(blutut, 2);
			addView(alarm, 3);
			addView(headset, 4);
		} else {
			inout.setPadding(0, 0, 2, 0);
			sinyal.setLayoutParams(par1);
			inout.setLayoutParams(par1);
			addView(ringmode, 0);
			if (IOkanan) {
				addView(sinyal, 1);
				addView(inout, 2);
			} else {
				addView(inout, 1);
				addView(sinyal, 2);
			}

			addView(blutut, 3);
			addView(alarm, 4);
			addView(headset, 5);
		}

	}

	public void setDataIO(Drawable d) {
		inout.setImageDrawable(d);
	}

	public void setSinyalStreng(Drawable d) {
		sinyal.setImageDrawable(d);
	}

	public void setRingerModeIcon(Drawable d) {
		ringmode.setImageDrawable(d);
	}

	public void setAlarmIcon(boolean boo) {
		alarm.setImageDrawable(boo ? mTema.getICON(SBK.ALARM) : null);
	}

	public void setHeadSetIcon(boolean bool) {

		headset.setImageDrawable(bool ? mTema.getICON(SBK.STATUSBAR_HEADSET)
				: null);
	}

	public void setBT(boolean boo) {
		blutut.setImageDrawable(boo ? mTema.getICON(SBK.STATUSBAR_BLUTUT)
				: null);
	}

	// public void updateView(Drawable si) {
	//
	// if (si == null)
	// sinyalicon = mTema.getICON(SBK.DATA_E[pos]);
	// else
	// sinyalicon = si;
	//
	// if (hasService() && mDataState == TelephonyManager.DATA_CONNECTED) {
	// inout.setImageDrawable(sinyalicon);
	// updateSinyal(signal);
	// } else if (getAirplaneState()) {
	// inout.setImageDrawable(mTema.getICON(SBK.SIGNAL_FLIGHT));
	// sinyal.setImageDrawable(mTema.getICON(SBK.SIGNAL_NULL));
	// } else {
	// inout.setImageDrawable(null);
	// }
	//
	// }

	private void setOtherIcon() {

		int vibe = getVibratorStatus();
		switch (vibe) {
		case 0:
			ringmode.setImageDrawable(mTema.getICON(SBK.STATUSBAR_SILENT));
			break;
		case 1:
			ringmode.setImageDrawable(mTema.getICON(SBK.STATUSBAR_VIBRATE));
			break;
		default:
			ringmode.setImageDrawable(null);
			break;
		}

		headset.setImageDrawable(isHeadset ? mTema
				.getICON(SBK.STATUSBAR_HEADSET) : null);
		alarm.setImageDrawable(isAlarm ? mTema.getICON(SBK.ALARM) : null);
		blutut.setImageDrawable(isBlutut ? mTema.getICON(SBK.STATUSBAR_BLUTUT)
				: null);

	}

	private void meWifi(int pos) {
		inout.setImageDrawable(mTema.getICON(SBK.WIFI_SIG[pos]));

	}

	// private void updateSinyal(int pos) {
	// sinyal.setImageDrawable(mTema.getICON(SBK.SINYAL_SIG[pos]));
	// }

	// private int s;

	// public void onDataActivity(int direction) {
	// meLog("direktion", Integer.toString(direction));
	//
	// if (direction == TelephonyManager.DATA_ACTIVITY_NONE)
	// pos = 0;
	// else if (direction == TelephonyManager.DATA_ACTIVITY_IN)
	// pos = 1;
	// else if (direction == TelephonyManager.DATA_ACTIVITY_OUT)
	// pos = 2;
	// else if (direction == TelephonyManager.DATA_ACTIVITY_INOUT)
	// pos = 3;
	// else
	// pos = 0;
	//
	// String type = null;
	//
	// try {
	// type = Get_NetWork_type();
	// } catch (NullPointerException e) {
	// type = "UNKNOWN";
	// }
	//
	// if (type != null) {
	// if (type.contains("EDGE")) {
	// updateView(mTema.getICON(SBK.DATA_E[pos]));
	// } else if (type.contains("GPRS")) {
	// updateView(mTema.getICON(SBK.DATA_G[pos]));
	// } else if (type.contains("HSDPA")) {
	// updateView(mTema.getICON(SBK.DATA_H[pos]));
	// } else if (type.contains("HSPA")) {
	// updateView(mTema.getICON(SBK.DATA_3G[pos]));
	// } else if (type.contains("WIFI")) {
	// meWifi(pos);
	// } else {
	// updateView(mTema.getICON(SBK.DATA_E[pos]));
	// }
	// }
	//
	// }
	//
	// public void onServiceStateChanged(ServiceState serviceState) {
	// updateView(null);
	//
	// }

	// public void onDataConnectionStateChanged(int state, int networkType) {
	// mDataState = state;
	// updateView(null);
	// mContext.sendBroadcast(new Intent().putExtra(SBK.TRAFFIC_STATE_BRODKES,
	// mDataState).setAction(SBK.TRAFFIC_STATE_BRODKES));
	//
	// }

	// public void onSignalStrengthsChanged(SignalStrength signalStrength) {
	// s = signalStrength.getGsmSignalStrength();
	// if (s <= 0)
	// signal = 0;
	// else if (s >= 1 && s <= 4)
	// signal = 1;
	// else if (s >= 5 && s <= 7)
	// signal = 2;
	// else if (s >= 8 && s <= 11)
	// signal = 3;
	// else if (s >= 12)
	// signal = 4;
	// else
	// signal = 0;
	//
	// updateSinyal(signal);
	//
	// }

	private int getVibratorStatus() {
		AudioManager audioManager = (AudioManager) mContext
				.getSystemService(Context.AUDIO_SERVICE);
		return audioManager.getRingerMode();
	}

	public void setReferensi(Tema mTema, getPref sbPref, getPref pref) {
		this.mTema = mTema;
		this.sbPref = sbPref;
		this.pref = pref;
		aturulangLayout();

	}
}
