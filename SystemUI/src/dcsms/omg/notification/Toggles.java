package dcsms.omg.notification;

import java.util.ArrayList;
import java.util.List;

import android.app.StatusBarManager;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.AttributeSet;
import android.util.secutil.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.GridLayoutAnimationController;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import dcsms.omg.util.AnimKontek;
import dcsms.omg.util.CekStatus;
import dcsms.omg.util.K;
import dcsms.omg.util.Model;
import dcsms.omg.util.SBK;
import dcsms.omg.util.Tema;
import dcsms.omg.util.XLog;
import dcsms.omg.util.getPref;

public class Toggles extends LinearLayout {
	Tema tema;
	private boolean mAttached;
	private Context mContext;
	GridView mGrid;
	TogglesAdapter adapter;
	SETObserver observer = null;
	mPSListener phonelisten;
	TelephonyManager tman;
	getPref pref;
	StatusBarManager sm;
	WindowManager wm;
	AnimKontek aKontek;
	Animation anim;

	private List<TGL> toggles;
	private AirPlane airplane;
	private AutoRotasi rotasi;
	private Blutut blutut;
	private GPS gps;
	private NetWorkData data;
	private OFF off;
	private Senter senter;
	private SS ss;
	private STO sto;
	private Tether tether;
	private Vibe vibrator;
	private WiFi wifi;
	private WiFiAp wifiap;

	private int tooglescount = 12;

	// XXX Config Change
	@Override
	protected void onConfigurationChanged(Configuration config) {
		super.onConfigurationChanged(config);
		Display d = wm.getDefaultDisplay();
		if (d.getWidth() > d.getHeight())
			mGrid.setNumColumns(6);
		else
			mGrid.setNumColumns(4);
		
		adapter.notifyDataSetChanged();
		

	}

	public Toggles(Context context, AttributeSet attrs) {

		super(context, attrs);
		tema = new Tema(context);
		aKontek = new AnimKontek();
		wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

		mContext = context;
		pref = new getPref(context, Model.PREF_JUDUL);

		// XXX declare

		airplane = new AirPlane(context);
		rotasi = new AutoRotasi(context);
		blutut = new Blutut(context);
		gps = new GPS(context);
		data = new NetWorkData(context);
		off = new OFF(context);
		senter = new Senter(context);
		ss = new SS(context);
		sto = new STO(context);
		tether = new Tether(context);
		vibrator = new Vibe(context);
		wifi = new WiFi( context);
		wifiap = new WiFiAp(context);
		

		setupButton(context);
	}

	public void setupToggles() {
		super.onAttachedToWindow();
		setupButton(mContext);

	}

	public void destroyToggles() {
		super.onDetachedFromWindow();
		toggles.clear();

	}

	private void setupButton(Context context) {
		XLog.s(K.TAG_E, "OnCreateSomeViews");
		removeAllViews();

		setOrientation(LinearLayout.VERTICAL);
		mGrid = new GridView(context);
		mGrid.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		mGrid.setNumColumns(K.TOGGLES_COLUMN);
		mGrid.setVerticalScrollBarEnabled(false);
		mGrid.buildDrawingCache(true);
		updateGridView(context);

		LinearLayout slroot = new LinearLayout(context);
		ImageView low = new ImageView(context);
		ImageView hi = new ImageView(context);
		low.setAdjustViewBounds(true);
		hi.setAdjustViewBounds(true);
		low.setImageDrawable(new Tema(context).getICON(SBK.SLIDER_MIN));
		hi.setImageDrawable(new Tema(context).getICON(SBK.SLIDER_MAX));
		// slider
		LinearLayout.LayoutParams par = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT, 0.9f);

		// icon
		LinearLayout.LayoutParams par2 = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.MATCH_PARENT, 0.05f);
		// main
		LinearLayout.LayoutParams par3 = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT, 1f);

		slroot.setLayoutParams(par3);

		slroot.addView(low, 0, par2);
		slroot.addView(new SlaiderBraiknesShit(context), 1, par);
		slroot.addView(hi, 2, par2);
		addView(mGrid);
		View gap = new View(context);
		gap.setLayoutParams(new LayoutParams(5, 5));
		addView(gap);
		slroot.setPadding(5, 0, 5, 0);
		addView(slroot);

	}

	private void updateGridView(Context context) {
		XLog.s(K.TAG_E, "OnUpdateGridView");

		toggles = new ArrayList<TGL>();
		// XXX addtheme
		toggles.add(airplane);
		toggles.add(rotasi);
		toggles.add(blutut);
		toggles.add(gps);
		toggles.add(data);
		toggles.add(off);
		toggles.add(senter);
		toggles.add(ss);
		toggles.add(sto);
		toggles.add(tether);
		toggles.add(vibrator);
		toggles.add(wifi);
		//toggles.add(wifiap);
		
		for (int i = 0; i < toggles.size(); i++) {
			toggles.get(i).updateView();
		}
		

		adapter = new TogglesAdapter(context, toggles);
		mGrid.setAdapter(adapter);
		mGrid.setSelector(tema.getICON(SBK.TOG_STATE));

		// XXX set pos

		mGrid.setOnItemClickListener(OnGridViewClickListener);

	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		XLog.s(K.TAG_E, "OnAttachWindows");
		if (!mAttached) {
			mAttached = true;
			IntentFilter filter = new IntentFilter();
			filter.addAction(ConnectivityManager.ACTION_TETHER_STATE_CHANGED);
			filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
			filter.addAction(Intent.ACTION_CONFIGURATION_CHANGED);
			filter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION);
			filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
			filter.addAction("android.net.wifi.WIFI_AP_STATE_CHANGED");
			filter.addAction(AudioManager.RINGER_MODE_CHANGED_ACTION);
			filter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION);
			filter.addAction("android.intent.action.SERVICE_STATE");
			filter.addAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
			filter.addAction(Intent.ACTION_BATTERY_CHANGED);
			filter.addAction(Musik.MUSIK_PLAY_STATECHANGE);
			filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
			getContext().registerReceiver(myReceiver, filter, null,
					getHandler());

			observer = new SETObserver(getHandler());
			observer.observe();

			phonelisten = new mPSListener();
			tman = (TelephonyManager) mContext
					.getSystemService(Context.TELEPHONY_SERVICE);
			tman.listen(phonelisten,
					PhoneStateListener.LISTEN_DATA_CONNECTION_STATE);

		}
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		XLog.s(K.TAG_E, "OnDetachedFromWindows");
		if (mAttached) {
			mAttached = false;
			getContext().unregisterReceiver(myReceiver);
		}
		if (observer != null) {
			observer.unobserve();
			observer = null;
		}
		tman.listen(phonelisten, PhoneStateListener.LISTEN_NONE);
		phonelisten = null;
		tman = null;
	}

	private OnItemClickListener OnGridViewClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> av, View v, int pos, long id) {

			toggles.get(pos).updateMe();

		}
	};

	private class SETObserver extends ContentObserver {

		public SETObserver(Handler handler) {
			super(handler);
		}

		private void observe() {
			ContentResolver resolver = mContext.getContentResolver();
			resolver.registerContentObserver(Settings.System
					.getUriFor(Settings.System.SCREEN_OFF_TIMEOUT), false, this);
			resolver.registerContentObserver(Settings.System
					.getUriFor(Settings.System.LOCATION_PROVIDERS_ALLOWED),
					false, this);
			resolver.registerContentObserver(Settings.System
					.getUriFor(Settings.System.ACCELEROMETER_ROTATION), false,
					this);

		}

		public void unobserve() {
			ContentResolver resolver = mContext.getContentResolver();
			resolver.unregisterContentObserver(this);
		}

		@Override
		public void onChange(boolean bool) {
			super.onChange(bool);
			rotasi.updateView();
			sto.updateView();
			gps.updateView();

			adapter.notifyDataSetChanged();

		}

	}

	private class mPSListener extends PhoneStateListener {

		@Override
		public void onDataConnectionStateChanged(int arg0) {
			super.onDataConnectionStateChanged(arg0);
			data.updateView();
			
			adapter.notifyDataSetChanged();

		}

	}

	private BroadcastReceiver myReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context contx, Intent intent) {
			XLog.s(K.TAG_E, "OnReceiveBroadCast : " + intent.getAction());
			final String action = intent.getAction();
			if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
				blutut.updateView();
			} else if (intent.getAction().equals(
					ConnectivityManager.ACTION_TETHER_STATE_CHANGED)) {
				tether.updateView();
			} else if (intent.getAction().equals(
					WifiManager.WIFI_STATE_CHANGED_ACTION))
				wifi.updateView();
			else if (intent.getAction().equals(
					WifiManager.WIFI_AP_STATE_CHANGED_ACTION))
				wifiap.updateView();
			else if (action.equals(Intent.ACTION_AIRPLANE_MODE_CHANGED)) {
				airplane.updateView();
			} else if (action.equals(Musik.MUSIK_PLAY_STATECHANGE)) {
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						Log.d(VIEW_LOG_TAG, action);
					}
				}, 1000);
			}

			adapter.notifyDataSetChanged();

		}
	};

	public void slideAnimate() {

		aKontek = tema.getAnim(SBK.GRID_ANIM);
		anim = AnimationUtils.loadAnimation(aKontek.kontek, aKontek.intejer);
		GridLayoutAnimationController gridAnim = new GridLayoutAnimationController(
				anim);
		gridAnim.setDirection(GridLayoutAnimationController.DIRECTION_TOP_TO_BOTTOM
				| GridLayoutAnimationController.DIRECTION_VERTICAL_MASK);
		gridAnim.setRowDelay(0.3f);
		mGrid.setLayoutAnimation(gridAnim);

	}

}
