package com.android.systemui.statusbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import dcsms.omg.util.SBK;
import dcsms.omg.util.Tema;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ClocknShit extends LinearLayout {
	private TextView mclock;
	private TextView mDate;
	private RelativeLayout mainLayout;
	private Tema mTema;

	public ClocknShit(Context context, AttributeSet attrs) {
		super(context, attrs);
		mTema = new Tema(context);
		Inisiasi();
	}

	protected void Inisiasi() {
		LayoutParams match = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		RelativeLayout.LayoutParams jamParam = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		RelativeLayout.LayoutParams dateParam = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		RelativeLayout.LayoutParams settingParam = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);

		RelativeLayout.LayoutParams recentPar = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		mclock = new TextView(mContext);
		mDate = new TextView(mContext);

		mainLayout = new RelativeLayout(mContext);
		mclock.setId(1);

		jamParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		jamParam.setMargins(0, 0, 0, 0);
		jamParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		dateParam.addRule(RelativeLayout.RIGHT_OF, mclock.getId());
		dateParam.addRule(RelativeLayout.ALIGN_BOTTOM,mclock.getId());
		dateParam.setMargins(0, 0, 0, 5);
		settingParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		recentPar.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		settingParam.setMargins(5, 5, 5, 5);		

		mainLayout.addView(mclock, jamParam);
		mainLayout.addView(mDate, dateParam);

		addView(mainLayout, match);
		dandaneui();
		updateView();

	}

	void dandaneui() {
		mclock.setTextSize(40);
		mclock.setTextColor(Color.WHITE);
		mclock.setShadowLayer(1, 1, 1, 0xff000000);
		mDate.setPadding(5, 0, 0, 0);
		mDate.setTextSize(14);
		mDate.setTextColor(Color.WHITE);
		mDate.setShadowLayer(1, 1, 1, 0xff000000);
		Typeface tf = mTema.getFontfromTheme(mTema.getStringfromTheme(SBK.FONT_JAM));
		mclock.setTypeface(tf);
		mDate.setTypeface(tf);

	}

	private class SyncView extends AsyncTask<Void, Void, ModelJam> {

		@Override
		protected void onPostExecute(ModelJam m) {
			mclock.setText(m.jam.format(m.date));
			mDate.setText(m.tanggal.format(m.date));

		}

		@Override
		protected ModelJam doInBackground(Void... arg0) {
			Date now = new Date();
			ModelJam m = new ModelJam();
			try {
				m.jam = new SimpleDateFormat("HH:mm", Locale.US);
				m.tanggal = new SimpleDateFormat("EEEE\nMMMM, dd y", Locale.US);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
			m.date = now;
			return m;
		}

	}

	private class ModelJam {
		Date date;
		SimpleDateFormat jam, tanggal;
	}

	public void updateView() {
		new SyncView().execute();
	}

}
