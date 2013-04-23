package dcsms.omg.statusbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import dcsms.omg.util.Model;
import dcsms.omg.util.SBK;
import dcsms.omg.util.Tema;
import dcsms.omg.util.getPref;

public class Jam extends SBIcon {
	private static TextView jam;
	private SimpleDateFormat sdf;
	private boolean hari, ampm, mode;
	private int shadx, shady, shadrad, shadcolor;
	private boolean visibel;

	public Jam(Context context, AttributeSet attrs) {
		super(context, attrs);
	}



	public static void showme(boolean bool) {
		if (jam != null)
			jam.setVisibility(bool ? View.VISIBLE : View.GONE);
	}

	




	@Override
	protected void Inisiasi() {
		super.Inisiasi();
		setPadding(2, 0, 2, 0);
		jam = new TextView(mContext);
		setGravity(Gravity.CENTER_VERTICAL);
		jam.setGravity(Gravity.CENTER_VERTICAL);
		jam.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.MATCH_PARENT));
		addView(jam);

	}

	private void updateView() {
		visibel = pref.getBoolean(SBK.JAM_ONOFF, true);
		if (visibel) {
			setVisibility(View.VISIBLE);
			jam.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.MATCH_PARENT));
		} else {
			setVisibility(View.GONE);
			jam.setLayoutParams(new LayoutParams(0, 0));
		}

		shadx = sbPref.getInt(SBK.JAM_X, 1);
		shady = sbPref.getInt(SBK.JAM_Y, 1);
		shadrad = sbPref.getInt(SBK.JAM_R, 1);
		shadcolor = sbPref.getInt(SBK.JAM_Col, 0x22000000);
		jam.setShadowLayer(shadrad, shadx, shady, shadcolor);
		jam.setTextColor(mTema.getWarnaTogel(SBK.JAM_MAIN_COL));
		jam.setTypeface(mTema.getFontfromTheme(mTema
				.getStringfromTheme(SBK.FONT_JAM)));
		jam.setTextSize(mTema.getDefaultDimen(SBK.UKURAN_JAM));

		hari = pref.getBoolean(SBK.JAM_HARI, false);
		ampm = pref.getBoolean(SBK.JAM_AMPM, true);
		mode = pref.getBoolean(SBK.JAM_HARI_AMPM_MODE, false);
		UpdateJAM();
		

	}
	
	public void UpdateJAM(){
		Date now = new Date();
		String form = "E HH:mm a";

		if (!ampm) {
			form = "E HH:mm";
			if (!hari)
				form = "HH:mm";
		} else {
			form = "E HH:mm a";
			if (!hari)
				form = "HH:mm a";

		}

		try {
			sdf = new SimpleDateFormat(form, Locale.US);
			String thistime = sdf.format(now).toUpperCase(Locale.US);
			jam.setText(WhatTime(thistime));

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		
	}

	private CharSequence WhatTime(String now) {

		int len = now.length();

		SpannableStringBuilder formatted = new SpannableStringBuilder(now);

		if (mode) {
			if (hari)
				formatted.setSpan(new RelativeSizeSpan(0.7f), 0, 4,
						Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
			if (ampm)
				formatted.setSpan(new RelativeSizeSpan(0.7f), len - 3, len,
						Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		} else {
			formatted.setSpan(new RelativeSizeSpan(1f), 0, len,
					Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		}
		return formatted;
	}



	public void setReferensi(Tema mTema, getPref sbPref, getPref pref) {
		this.mTema =mTema;
		this.sbPref=sbPref;
		this.pref=pref;
		updateView();
		
	}

}
