package dcsms.omg.statusbar;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.NinePatchDrawable;
import android.media.AudioManager;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import dcsms.omg.util.Model;
import dcsms.omg.util.SBK;
import dcsms.omg.util.SBTema;
import dcsms.omg.util.getPref;

public class StatusbarLayout extends RelativeLayout {
	private Context cntx;
	private boolean mAttached;
	private getPref pref;

	public StatusbarLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		cntx = context;

	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		if (!mAttached) {
			mAttached = true;
			IntentFilter filter = new IntentFilter();
			filter.addAction(Model.UPDATE_STATUSBAR);
			filter.addAction(Intent.ACTION_BOOT_COMPLETED);
			filter.addAction("android.intent.action.ALARM_CHANGED");
			filter.addAction(Intent.ACTION_HEADSET_PLUG);
			filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
			filter.addAction(AudioManager.RINGER_MODE_CHANGED_ACTION);

			getContext().registerReceiver(mIntentReceiver, filter, null,
					getHandler());

			Inisiasi();

		}
	}

	private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			if (action.equals(Model.UPDATE_STATUSBAR)
					|| action.equals(Intent.ACTION_BOOT_COMPLETED)
					|| action.equals("android.intent.action.ALARM_CHANGED")
					|| action.equals(Intent.ACTION_HEADSET_PLUG)
					|| action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)
					|| action.equals(AudioManager.RINGER_MODE_CHANGED_ACTION)) {

				Inisiasi();
			}
		}

	};

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (mAttached) {
			mAttached = false;
			getContext().unregisterReceiver(mIntentReceiver);

		}
	}

	private void Inisiasi() {
		pref = new getPref(cntx, Model.PREF_JUDUL);
		setPadding(5, 0, 5, 0);
		NinePatchDrawable d = new SBTema(cntx).getNineBMP(SBK.STATUSBAR_BG);
		setBackgroundDrawable(d);
		Handler h = new Handler();
		h.postDelayed(new Runnable() {

			@Override
			public void run() {
				arrangelayout();

			}
		}, 2000);

	}

	private void arrangelayout() {

		int mode = pref.getInt(SBK.LAYOUT_MODE, 0);
		int istop = pref.getInt(SBK.BAT_LINE_ISTOP, 1);

		View vbat = getChildAt(0);
		View vjam = getChildAt(1);
		View vsinyal = getChildAt(2);
		View vtraf = getChildAt(3);
		View vkarir = getChildAt(4);
		View vnotif = getChildAt(5);
		View vbar = getChildAt(6);
		vbat.setId(0);
		vjam.setId(1);
		vsinyal.setId(2);
		vtraf.setId(3);
		vkarir.setId(4);
		vnotif.setId(5);
		vbar.setId(6);

		LayoutParams bar = new LayoutParams(LayoutParams.MATCH_PARENT,
				pref.getInt(SBK.BAT_LINE_HEIGHT, 1));
		if (istop == 1)
			bar.addRule(ALIGN_PARENT_TOP);
		else
			bar.addRule(ALIGN_PARENT_BOTTOM);

		vbar.setLayoutParams(bar);

		LayoutParams kanan = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.MATCH_PARENT);
		kanan.addRule(ALIGN_PARENT_RIGHT);

		LayoutParams kiri = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.MATCH_PARENT);
		kiri.addRule(ALIGN_PARENT_LEFT);

		LayoutParams tengah = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.MATCH_PARENT);
		tengah.addRule(CENTER_IN_PARENT);

		LayoutParams jam = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.MATCH_PARENT);
		LayoutParams batt = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.MATCH_PARENT);
		LayoutParams karir = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.MATCH_PARENT);
		LayoutParams sinyal = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.MATCH_PARENT);
		LayoutParams trafik = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.MATCH_PARENT);
		LayoutParams notif = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.MATCH_PARENT);

		switch (mode) {
		case 0:
			showall();
			vjam.setLayoutParams(tengah);
			vbat.setLayoutParams(kanan);
			vkarir.setLayoutParams(kiri);

			sinyal.addRule(ALIGN_PARENT_RIGHT);
			sinyal.setMargins(0, 0, vbat.getWidth(), 0);
			vsinyal.setLayoutParams(sinyal);

			trafik.addRule(ALIGN_PARENT_RIGHT);
			trafik.setMargins(0, 0, vbat.getWidth() + vsinyal.getWidth(), 0);
			vtraf.setLayoutParams(trafik);

			notif.addRule(RIGHT_OF, vkarir.getId());
			vnotif.setLayoutParams(notif);

			break;

		case 1:
			showall();
			vjam.setLayoutParams(kanan);
			vkarir.setLayoutParams(kiri);

			batt.addRule(ALIGN_PARENT_RIGHT);
			batt.setMargins(0, 0, vjam.getWidth(), 0);
			vbat.setLayoutParams(batt);

			sinyal.addRule(ALIGN_PARENT_RIGHT);
			sinyal.setMargins(0, 0, vbat.getWidth() + vjam.getWidth(), 0);

			vsinyal.setLayoutParams(sinyal);

			trafik.addRule(LEFT_OF, vsinyal.getId());
			vtraf.setLayoutParams(trafik);

			notif.addRule(RIGHT_OF, vkarir.getId());
			vnotif.setLayoutParams(notif);

			break;
		case 2:
			Jam.showme(false);
			vjam.setLayoutParams(tengah);
			vbat.setLayoutParams(kanan);
			vkarir.setLayoutParams(kiri);

			sinyal.addRule(ALIGN_PARENT_RIGHT);
			sinyal.setMargins(0, 0, vbat.getWidth(), 0);
			vsinyal.setLayoutParams(sinyal);

			trafik.addRule(LEFT_OF, vsinyal.getId());
			vtraf.setLayoutParams(trafik);

			notif.addRule(RIGHT_OF, vkarir.getId());
			vnotif.setLayoutParams(notif);

			break;
		case 3:
			showall();
			vjam.setLayoutParams(tengah);
			vbat.setLayoutParams(kiri);

			vsinyal.setLayoutParams(kanan);

			karir.addRule(ALIGN_PARENT_RIGHT);
			karir.setMargins(0, 0, vsinyal.getWidth(), 0);
			vkarir.setLayoutParams(karir);

			trafik.addRule(LEFT_OF, vkarir.getId());
			vtraf.setLayoutParams(trafik);

			notif.addRule(ALIGN_PARENT_LEFT);
			notif.setMargins(vbat.getWidth(), 0, 0, 0);
			vnotif.setLayoutParams(notif);

			break;
		}

	}

	private void showall() {
		Jam.showme(true);

	}

}
