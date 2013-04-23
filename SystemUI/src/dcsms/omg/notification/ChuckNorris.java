package dcsms.omg.notification;


import com.android.internal.app.ShutdownThread;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class ChuckNorris extends Activity {
	private float value = 0.5f;
	private Context context;
	private boolean BOOTNOW = false;
	private boolean SENTER = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(android.R.style.Theme_Translucent_NoTitleBar);
		super.onCreate(savedInstanceState);
		context = this;
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			BOOTNOW = extras.getBoolean("BOOT");
			SENTER = extras.getBoolean("SENTER");
			if (BOOTNOW) {
				DialogReboot();
				return;
			}
			if (SENTER) {
				SenterinDonk();
				return;
			}

			value = extras.getFloat("SHIT");
			applyAndLeave();

		}

	}

	private void SenterinDonk() {
		WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
		layoutParams.screenBrightness = 1f;
		getWindow().setAttributes(layoutParams);
		LinearLayout l = new LinearLayout(this);
		l.setBackgroundColor(Color.WHITE);
		setContentView(l);

	}

	private void applyAndLeave() {
		WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
		layoutParams.screenBrightness = value;
		getWindow().setAttributes(layoutParams);
		int SysBackLightValue = (int) (value * 255);
		Settings.System.putInt(getContentResolver(),
				Settings.System.SCREEN_BRIGHTNESS, SysBackLightValue);

		Handler h = new Handler();
		h.postDelayed(new Runnable() {

			@Override
			public void run() {
				finish();

			}
		}, 10);

	}

	private void DialogReboot() {

		Dialog drop = new Dialog(context,
				android.R.style.Theme_Translucent_NoTitleBar);
		drop.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		RelativeLayout rl = new RelativeLayout(context);
		RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(
				android.widget.RelativeLayout.LayoutParams.MATCH_PARENT,
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
		rllp.addRule(RelativeLayout.CENTER_IN_PARENT, 0);

		LinearLayout add = new LinearLayout(context);
		add.setOrientation(LinearLayout.HORIZONTAL);

		Button a1 = new Button(context);
		Button a2 = new Button(context);
		Button a3 = new Button(context);
		a1.setId(1);
		a2.setId(2);
		a3.setId(3);
		a1.setOnClickListener(onmeClick);
		a2.setOnClickListener(onmeClick);
		a3.setOnClickListener(onmeClick);
		a1.setText("Shutdown");
		a2.setText("Reboot");
		a3.setText("Recovery");
		add.addView(a1);
		add.addView(a2);
		add.addView(a3);
		add.setLayoutParams(rllp);
		add.setGravity(Gravity.CENTER);
		rl.addView(add);
		rl.setVerticalGravity(Gravity.CENTER);

		drop.setContentView(rl);
		drop.show();
		drop.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface arg0) {
				finish();

			}
		});

	}

	private OnClickListener onmeClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			PowerManager pm = (PowerManager) context
					.getSystemService(Context.POWER_SERVICE);
			switch (v.getId()) {
			case 1:
				ShutdownThread.shutdown(context, false);
				break;

			case 2:
				pm.reboot("now");
				break;
			case 3:
				pm.reboot("recovery");
				break;
			}

		}
	};

}
