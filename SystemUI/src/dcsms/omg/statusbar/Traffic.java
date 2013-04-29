package dcsms.omg.statusbar;

import java.text.DecimalFormat;

import android.content.Context;
import android.net.TrafficStats;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import dcsms.omg.util.SBK;
import dcsms.omg.util.Sett;
import dcsms.omg.util.Tema;
import dcsms.omg.util.getPref;

public class Traffic extends TextView {
	private boolean mTrafik;
	private long mLastTime;
	private Handler mHandler = new Handler();
	private getPref pref, sbPref;
	private boolean showme = true;
	boolean show;
	private Tema mTema;
	private int shadx, shady, shadrad, shadcolor;

	public Traffic(Context context, AttributeSet att) {
		super(context, att);
	}

	private void updateState() {
		long mStartRX = TrafficStats.getTotalRxBytes();

		long mStartTX = TrafficStats.getTotalTxBytes();

		if (mStartRX == TrafficStats.UNSUPPORTED
				|| mStartTX == TrafficStats.UNSUPPORTED) {
			Log.w("DCsmsTraffic", "Not Support");
		} else {
			if (mTrafik == true) {
				mHandler.removeCallbacks(mRunnable);
			} else {
				mHandler.removeCallbacks(mRunnable);
				mHandler.postDelayed(mRunnable, 1000);
			}
		}

	}

	public void modifInterface() {
		show = pref.getBoolean(Sett.TrafficState, true);
		shadx = sbPref.getInt(SBK.T_X, 1);
		shady = sbPref.getInt(SBK.T_Y, 1);
		shadrad = sbPref.getInt(SBK.T_R, 1);
		shadcolor = sbPref.getInt(SBK.T_Col, 0x22000000);
		setShadowLayer(shadrad, shadx, shady, shadcolor);
		setTextColor(mTema.getWarna(SBK.T_MAIN_COL));
		setTypeface(mTema.getFontfromTheme(mTema
				.getStringfromTheme(SBK.FONT_TRAFIK)));
		setTextSize(mTema.getDefaultDimen(SBK.UKURAN_TRAFIK));
		setPadding(2, 0, 2, 0);

		showTraffikState();

	}

	public void TrafikOnOFF(boolean bool) {
		showme = bool;
		showTraffikState();
	}

	private void showTraffikState() {

		if(show){
			if (showme) {
				setVisibility(View.VISIBLE);
			} else {
				setVisibility(View.GONE);
			}
		}
		mTrafik = (show ? false : true);
		setVisibility(show ? View.VISIBLE : View.GONE);
		updateState();

	}

	private final Runnable mRunnable = new Runnable() {

		public void run() {

			long total = TrafficStats.getTotalRxBytes()
					+ TrafficStats.getTotalTxBytes();
			long lok = total - mLastTime;

			setText(Count(lok, true));
			mLastTime = total;
			mHandler.postDelayed(mRunnable, 1000);

		}

	};

	public static String Count(long bytes, boolean si) {
		int unit = si ? 1000 : 1024;
		if (bytes < unit)
			return new DecimalFormat("00.0").format(bytes / 1000) + " K/s";
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1)
				+ (si ? "" : "i");
		return String.format("%.1f %S/s", bytes / Math.pow(unit, exp), pre);
	}

	public void setReferensi(Tema mTema, getPref sbPref, getPref pref) {
		this.mTema = mTema;
		this.sbPref = sbPref;
		this.pref = pref;
		mLastTime = 0;
		modifInterface();

	}
}
