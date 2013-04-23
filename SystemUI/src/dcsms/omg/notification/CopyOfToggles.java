//package dcsms.omg.notification;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import android.app.StatusBarManager;
//import android.bluetooth.BluetoothAdapter;
//import android.content.BroadcastReceiver;
//import android.content.ContentResolver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.res.Configuration;
//import android.database.ContentObserver;
//import android.graphics.drawable.Drawable;
//import android.location.LocationManager;
//import android.media.AudioManager;
//import android.net.ConnectivityManager;
//import android.net.wifi.WifiManager;
//import android.os.Handler;
//import android.os.Parcelable;
//import android.provider.Settings;
//import android.telephony.PhoneStateListener;
//import android.telephony.TelephonyManager;
//import android.util.AttributeSet;
//import android.view.Display;
//import android.view.View;
//import android.view.WindowManager;
//import android.view.animation.Animation;
//import android.view.animation.AnimationUtils;
//import android.view.animation.GridLayoutAnimationController;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.GridView;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import dcsms.omg.util.AnimKontek;
//import dcsms.omg.util.CekStatus;
//import dcsms.omg.util.K;
//import dcsms.omg.util.Model;
//import dcsms.omg.util.SBK;
//import dcsms.omg.util.Tema;
//import dcsms.omg.util.XLog;
//import dcsms.omg.util.getPref;
//public class CopyOfToggles extends LinearLayout {
//	Tema tema;
//	private boolean mAttached;
//	private Context mContext;
//	GridView mGrid;
//	TogglesAdapter adapter;
//	SETObserver observer = null;
//	mPSListener phonelisten;
//	TelephonyManager tman;
//	getPref pref;
//	StatusBarManager sm;
//	WindowManager wm;
//	AnimKontek aKontek;
//	Animation anim;
//
//	boolean tetering_state = false;
//
//	AppsLauncher app0, app1, app2, app3, app4, app5, app6, app7, app8, app9,
//			app10, app11;
//	AppsLauncher[] appslauncher = { app0, app1, app2, app3, app4, app5, app6,
//			app7, app8, app9, app10, app11 };
//
//	private List<TogglesModel> toggles;
//	private List<Integer> tog_pos;
//
//	private TogglesModel TOGGLES_SKRINTIMEOUT;
//	private TogglesModel TOGGLES_VIBRATOR;
//	private TogglesModel TOGGLES_ROTASI;
//	private TogglesModel TOGGLES_BLUTUT;
//	private TogglesModel TOGGLES_GPS;
//	private TogglesModel TOGGLES_SCREENCAPT;
//	private TogglesModel TOGGLES_WIFI;
//	private TogglesModel TOGGLES_WIFI_AP;
//	private TogglesModel TOGGLES_USB_TETHER;
//	private TogglesModel TOGGLES_AIRPLANE;
//	private TogglesModel TOGGLES_SHUTDOWN;
//	private TogglesModel TOGGLES_DATA;
//	private TogglesModel TOGGLES_MUSICPLAY;
//	private TogglesModel TOGGLES_MUSICMAJU;
//	private TogglesModel TOGGLES_MUSICMUNDUR;
//	private TogglesModel TOGGLES_FLASHLIGHT;
//	private TogglesModel TOGGLES_APPS0;
//	private TogglesModel TOGGLES_APPS1;
//	private TogglesModel TOGGLES_APPS2;
//	private TogglesModel TOGGLES_APPS3;
//	private TogglesModel TOGGLES_APPS4;
//	private TogglesModel TOGGLES_APPS5;
//	private TogglesModel TOGGLES_APPS6;
//	private TogglesModel TOGGLES_APPS7;
//	private TogglesModel TOGGLES_APPS8;
//	private TogglesModel TOGGLES_APPS9;
//	private TogglesModel TOGGLES_APPS10;
//	private TogglesModel TOGGLES_APPS11;
//
//	// posisi togel
//	private int POS_SKRINTIMEOUT;
//	private int POS_VIBRATOR;
//	private int POS_ROTASI;
//	private int POS_BLUTUT;
//	private int POS_GPS;
//	private int POS_SCREENCAPT;
//	private int POS_WIFI;
//	private int POS_WIFI_AP;
//	private int POS_USB_TETHER;
//	private int POS_AIRPLANE;
//	private int POS_SHUTDOWN;
//	private int POS_DATA;
//	private int POS_MUSICPLAY;
//	private int POS_MUSICMAJU;
//	private int POS_MUSICMUNDUR;
//	private int POS_FLASHLIGHT;
//	private int POS_APPS0;
//	private int POS_APPS1;
//	private int POS_APPS2;
//	private int POS_APPS3;
//	private int POS_APPS4;
//	private int POS_APPS5;
//	private int POS_APPS6;
//	private int POS_APPS7;
//	private int POS_APPS8;
//	private int POS_APPS9;
//	private int POS_APPS10;
//	private int POS_APPS11;
//	private int tooglescount = 12;
//	private TogglesModel[] MY_TOGGLES = { TOGGLES_SKRINTIMEOUT,
//			TOGGLES_VIBRATOR, TOGGLES_ROTASI, TOGGLES_BLUTUT, TOGGLES_GPS,
//			TOGGLES_SCREENCAPT, TOGGLES_WIFI, TOGGLES_WIFI_AP,
//			TOGGLES_USB_TETHER, TOGGLES_AIRPLANE, TOGGLES_SHUTDOWN,
//			TOGGLES_DATA, TOGGLES_MUSICPLAY, TOGGLES_MUSICMAJU,
//			TOGGLES_MUSICMUNDUR, TOGGLES_FLASHLIGHT, TOGGLES_APPS0,
//			TOGGLES_APPS1, TOGGLES_APPS2, TOGGLES_APPS3, TOGGLES_APPS4,
//			TOGGLES_APPS5, TOGGLES_APPS6, TOGGLES_APPS7, TOGGLES_APPS8,
//			TOGGLES_APPS9, TOGGLES_APPS10, TOGGLES_APPS11 };
//	private Integer[] MY_POS = { POS_SKRINTIMEOUT, POS_VIBRATOR, POS_ROTASI,
//			POS_BLUTUT, POS_GPS, POS_SCREENCAPT, POS_WIFI, POS_WIFI_AP,
//			POS_USB_TETHER, POS_AIRPLANE, POS_SHUTDOWN, POS_DATA,
//			POS_MUSICPLAY, POS_MUSICMAJU, POS_MUSICMUNDUR, POS_FLASHLIGHT,
//			POS_APPS0, POS_APPS1, POS_APPS2, POS_APPS3, POS_APPS4, POS_APPS5,
//			POS_APPS6, POS_APPS7, POS_APPS8, POS_APPS9, POS_APPS10, POS_APPS11 };
//	
//
//	
//	@Override
//	protected void onConfigurationChanged(Configuration config) {
//		super.onConfigurationChanged(config);
//		
//		
//		Display d = wm.getDefaultDisplay();
//		if (d.getWidth() > d.getHeight())
//			mGrid.setNumColumns(6);
//		else
//			mGrid.setNumColumns(4);
//
//		adapter.notifyDataSetChanged();
//		
//		
//	}
//
//	public CopyOfToggles(Context context, AttributeSet attrs) {
//		
//		super(context, attrs);
//		tema = new Tema(context);
//		aKontek = new AnimKontek();
//		wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//		app0 = new AppsLauncher(context, 0);
//		app1 = new AppsLauncher(context, 1);
//		app2 = new AppsLauncher(context, 2);
//		app3 = new AppsLauncher(context, 3);
//		app4 = new AppsLauncher(context, 4);
//		app5 = new AppsLauncher(context, 5);
//		app6 = new AppsLauncher(context, 6);
//		app7 = new AppsLauncher(context, 7);
//		app8 = new AppsLauncher(context, 8);
//		app9 = new AppsLauncher(context, 9);
//		app10 = new AppsLauncher(context, 10);
//		app11 = new AppsLauncher(context, 11);
//
//		mContext = context;
//		pref = new getPref(context, Model.PREF_JUDUL);
//		tetering_state = new Tether(context).cekUSBTether();
//
//		MY_TOGGLES[0] = new TogglesModel(STO.howlong(mContext),
//				STO.getBimtap(mContext), K.TYPE_TOGGLES_SKRINTIMEOUT);
//		MY_TOGGLES[1] = new TogglesModel(Vibe.CurrentStatus(context),
//				Vibe.getBimtap(mContext), K.TYPE_TOGGLES_VIBRATOR);
//		MY_TOGGLES[2] = new TogglesModel(AutoRotasi.status(context),
//				AutoRotasi.getBimtap(mContext), K.TYPE_TOGGLES_ROTASI);
//		MY_TOGGLES[3] = new TogglesModel(Blutut.status(),
//				Blutut.getBimtap(mContext), K.TYPE_TOGGLES_BLUTUT);
//		MY_TOGGLES[4] = new TogglesModel(GPS.get(mContext),
//				GPS.getBimtap(mContext), K.TYPE_TOGGLES_GPS);
//		MY_TOGGLES[5] = new TogglesModel(SS.get(), SS.getBimtap(mContext),
//				K.TYPE_TOGGLES_SCREENCAPT);
//		MY_TOGGLES[6] = new TogglesModel(WiFi.status(mContext),
//				WiFi.getBimtap(mContext), K.TYPE_TOGGLES_WIFI);
//		MY_TOGGLES[7] = new TogglesModel(WiFiAp.status(mContext),
//				WiFiAp.getBimtap(mContext), K.TYPE_TOGGLES_WIFI_AP);
//		MY_TOGGLES[8] = new TogglesModel(CekStatus.USBTETHER(tetering_state,
//				mContext), Tether.getBimtap(tetering_state, mContext),
//				K.TYPE_TOGGLES_USB_TETHER);
//		MY_TOGGLES[9] = new TogglesModel(AirPlane.getStatus(mContext),
//				AirPlane.getBimtap(mContext), K.TYPE_TOGGLES_AIRPLANE);
//		MY_TOGGLES[10] = new TogglesModel(OFF.get(), OFF.getBimtap(mContext),
//				K.TYPE_TOGGLES_SHUTDOWN);
//		MY_TOGGLES[11] = new TogglesModel(NetWorkData.state(mContext),
//				NetWorkData.getBimtap(mContext), K.TYPE_TOGGLES_DATA);
//		MY_TOGGLES[12] = new TogglesModel(MPlay.state(mContext),
//				MPlay.getBimtap(mContext), K.TYPE_TOGGLES_MUSICPLAY);
//		MY_TOGGLES[13] = new TogglesModel(MNext.state(mContext),
//				MNext.getBimtap(mContext), K.TYPE_TOGGLES_MUSICMAJU);
//		MY_TOGGLES[14] = new TogglesModel(MPrev.state(mContext),
//				MPrev.getBimtap(mContext), K.TYPE_TOGGLES_MUSICMUNDUR);
//		MY_TOGGLES[15] = new TogglesModel(Senter.get(),
//				Senter.getBimtap(mContext), K.TYPE_TOGGLES_FLASHLIGHT);
//		MY_TOGGLES[16] = new TogglesModel(app0.get(), app0.getBimtap(),
//				K.TYPE_TOGGLES_APPS0);
//		MY_TOGGLES[17] = new TogglesModel(app1.get(), app1.getBimtap(),
//				K.TYPE_TOGGLES_APPS1);
//		MY_TOGGLES[18] = new TogglesModel(app2.get(), app2.getBimtap(),
//				K.TYPE_TOGGLES_APPS2);
//		MY_TOGGLES[19] = new TogglesModel(app3.get(), app3.getBimtap(),
//				K.TYPE_TOGGLES_APPS3);
//		MY_TOGGLES[20] = new TogglesModel(app4.get(), app4.getBimtap(),
//				K.TYPE_TOGGLES_APPS4);
//		MY_TOGGLES[21] = new TogglesModel(app5.get(), app5.getBimtap(),
//				K.TYPE_TOGGLES_APPS5);
//		MY_TOGGLES[22] = new TogglesModel(app6.get(), app6.getBimtap(),
//				K.TYPE_TOGGLES_APPS6);
//		MY_TOGGLES[23] = new TogglesModel(app7.get(), app7.getBimtap(),
//				K.TYPE_TOGGLES_APPS7);
//		MY_TOGGLES[24] = new TogglesModel(app8.get(), app8.getBimtap(),
//				K.TYPE_TOGGLES_APPS8);
//		MY_TOGGLES[25] = new TogglesModel(app9.get(), app9.getBimtap(),
//				K.TYPE_TOGGLES_APPS9);
//		MY_TOGGLES[26] = new TogglesModel(app10.get(), app10.getBimtap(),
//				K.TYPE_TOGGLES_APPS10);
//		MY_TOGGLES[27] = new TogglesModel(app11.get(), app11.getBimtap(),
//				K.TYPE_TOGGLES_APPS11);
//
//		setupButton(context);
//	}
//
//	public void setupToggles() {
//		super.onAttachedToWindow();
//		setupButton(mContext);
//
//	}
//
//	public void destroyToggles() {
//		super.onDetachedFromWindow();
//		toggles.clear();
//
//	}
//
//	private void setupButton(Context context) {
//		XLog.s(K.TAG_E, "OnCreateSomeViews");
//		removeAllViews();
//
//		setOrientation(LinearLayout.VERTICAL);
//		mGrid = new GridView(context);
//		mGrid.setLayoutParams(new LinearLayout.LayoutParams(
//				LinearLayout.LayoutParams.MATCH_PARENT,
//				LinearLayout.LayoutParams.WRAP_CONTENT));
//		mGrid.setNumColumns(K.TOGGLES_COLUMN);
//		mGrid.setVerticalScrollBarEnabled(false);
//		mGrid.buildDrawingCache(true);
//		updateGridView(context);
//
//		LinearLayout slroot = new LinearLayout(context);
//		ImageView low = new ImageView(context);
//		ImageView hi = new ImageView(context);
//		low.setAdjustViewBounds(true);
//		hi.setAdjustViewBounds(true);
//		low.setImageDrawable(new Tema(context).getICON(SBK.SLIDER_MIN));
//		hi.setImageDrawable(new Tema(context).getICON(SBK.SLIDER_MAX));
//		// slider
//		LinearLayout.LayoutParams par = new LinearLayout.LayoutParams(
//				LinearLayout.LayoutParams.MATCH_PARENT,
//				LinearLayout.LayoutParams.MATCH_PARENT, 0.9f);
//
//		// icon
//		LinearLayout.LayoutParams par2 = new LinearLayout.LayoutParams(
//				LinearLayout.LayoutParams.WRAP_CONTENT,
//				LinearLayout.LayoutParams.MATCH_PARENT, 0.05f);
//		// main
//		LinearLayout.LayoutParams par3 = new LinearLayout.LayoutParams(
//				LinearLayout.LayoutParams.MATCH_PARENT,
//				LinearLayout.LayoutParams.MATCH_PARENT, 1f);
//
//		slroot.setLayoutParams(par3);
//
//		slroot.addView(low, 0, par2);
//		slroot.addView(new SlaiderBraiknesShit(context), 1, par);
//		slroot.addView(hi, 2, par2);
//		addView(mGrid);
//		View gap = new View(context);
//		gap.setLayoutParams(new LayoutParams(5, 5));
//		addView(gap);
//		slroot.setPadding(5, 0, 5, 0);
//		addView(slroot);
//
//	}
//
//	private void updateGridView(Context context) {
//		XLog.s(K.TAG_E, "OnUpdateGridView");
//		for (int i = 0; i < MY_POS.length; i++) {
//			MY_POS[i] = i;
//		}
//		toggles = new ArrayList<TogglesModel>();
//		for (int i = 0; i < tooglescount; i++) {
//			toggles.add(MY_TOGGLES[i]);
//		}
//
//		adapter = new TogglesAdapter(context, toggles);
//		mGrid.setAdapter(adapter);
//		mGrid.setSelector(tema.getICON(SBK.TOG_STATE));
//
//		// XXX set pos
//
//		mGrid.setOnItemClickListener(OnGridViewClickListener);
//
//	}
//
//	@Override
//	protected void onAttachedToWindow() {
//		super.onAttachedToWindow();
//		XLog.s(K.TAG_E, "OnAttachWindows");
//		if (!mAttached) {
//			mAttached = true;
//			IntentFilter filter = new IntentFilter();
//			filter.addAction(ConnectivityManager.ACTION_TETHER_STATE_CHANGED);
//			filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
//			filter.addAction(Intent.ACTION_CONFIGURATION_CHANGED);
//			filter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION);
//			filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
//			filter.addAction("android.net.wifi.WIFI_AP_STATE_CHANGED");
//			filter.addAction(AudioManager.RINGER_MODE_CHANGED_ACTION);
//			filter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION);
//			filter.addAction("android.intent.action.SERVICE_STATE");
//			filter.addAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
//			filter.addAction(Intent.ACTION_BATTERY_CHANGED);
//			filter.addAction(Musik.MUSIK_PLAY_STATECHANGE);
//			filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
//			getContext().registerReceiver(myReceiver, filter, null,
//					getHandler());
//
//			observer = new SETObserver(getHandler());
//			observer.observe();
//
//			phonelisten = new mPSListener();
//			tman = (TelephonyManager) mContext
//					.getSystemService(Context.TELEPHONY_SERVICE);
//			tman.listen(phonelisten,
//					PhoneStateListener.LISTEN_DATA_CONNECTION_STATE);
//
//		}
//	}
//
//	@Override
//	protected void onDetachedFromWindow() {
//		super.onDetachedFromWindow();
//		XLog.s(K.TAG_E, "OnDetachedFromWindows");
//		if (mAttached) {
//			mAttached = false;
//			getContext().unregisterReceiver(myReceiver);
//		}
//		if (observer != null) {
//			observer.unobserve();
//			observer = null;
//		}
//		tman.listen(phonelisten, PhoneStateListener.LISTEN_NONE);
//		phonelisten = null;
//		tman = null;
//	}
//
//	private OnItemClickListener OnGridViewClickListener = new OnItemClickListener() {
//
//		@Override
//		public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
//			TogglesHolder holder = (TogglesHolder) v.getTag();
//			switch (holder.type) {
//			case K.TYPE_TOGGLES_SKRINTIMEOUT:
//				STO.update(mContext);
//				break;
//
//			case K.TYPE_TOGGLES_VIBRATOR:
//				Vibe.update(mContext);
//				DoUpdateToggles(holder, Vibe.CurrentStatus(mContext),
//						Vibe.getBimtap(mContext));
//				break;
//			case K.TYPE_TOGGLES_ROTASI:
//				AutoRotasi.DoAutoRotate(mContext, holder);
//				break;
//			case K.TYPE_TOGGLES_BLUTUT:
//				Blutut.OnOffBlutut();
//				break;
//			case K.TYPE_TOGGLES_GPS:
//				GPS.update(mContext);
//				break;
//			case K.TYPE_TOGGLES_SCREENCAPT:
//				SS.crot(mContext);
//				break;
//			case K.TYPE_TOGGLES_WIFI:
//				WiFi.setWiFi(mContext);
//				break;
//			case K.TYPE_TOGGLES_WIFI_AP:
//				WiFiAp.setWiFi(mContext);
//				break;
//
//			case K.TYPE_TOGGLES_USB_TETHER:
//				new Tether(mContext).OnOffTether();
//				break;
//			case K.TYPE_TOGGLES_AIRPLANE:
//				AirPlane.set(mContext);
//				break;
//			case K.TYPE_TOGGLES_SHUTDOWN:
//				OFF.me(mContext);
//				break;
//			case K.TYPE_TOGGLES_DATA:
//				NetWorkData.setState(mContext);
//				break;
//			case K.TYPE_TOGGLES_MUSICPLAY:
//				MPlay.play(mContext);
//				break;
//			case K.TYPE_TOGGLES_MUSICMAJU:
//				MNext.next(mContext);
//				break;
//			case K.TYPE_TOGGLES_MUSICMUNDUR:
//				MPrev.prev(mContext);
//				break;
//			case K.TYPE_TOGGLES_FLASHLIGHT:
//				Senter.me(mContext);
//				break;
//			case K.TYPE_TOGGLES_APPS0:
//				app0.launch();
//				break;
//			case K.TYPE_TOGGLES_APPS1:
//				app1.launch();
//				break;
//			case K.TYPE_TOGGLES_APPS2:
//				app2.launch();
//				break;
//			case K.TYPE_TOGGLES_APPS3:
//				app3.launch();
//				break;
//			case K.TYPE_TOGGLES_APPS4:
//				app4.launch();
//				break;
//			case K.TYPE_TOGGLES_APPS5:
//				app5.launch();
//				break;
//			case K.TYPE_TOGGLES_APPS6:
//				app6.launch();
//				break;
//			case K.TYPE_TOGGLES_APPS7:
//				app7.launch();
//				break;
//			case K.TYPE_TOGGLES_APPS8:
//				app8.launch();
//				break;
//			case K.TYPE_TOGGLES_APPS9:
//				app9.launch();
//				break;
//			case K.TYPE_TOGGLES_APPS10:
//				app10.launch();
//				break;
//			case K.TYPE_TOGGLES_APPS11:
//				app11.launch();
//				break;
//
//			}
//
//		}
//	};
//
//	/**
//	 * GLOBAL UPDATE
//	 */
//	private void DoUpdateToggles(TogglesHolder holder, String teks, Drawable bmp) {
//		if (holder == null)
//			XLog.s(VIEW_LOG_TAG, teks);
//		else {
//			holder.tv.setText(teks);
//			holder.iv.setImageDrawable(bmp);
//		}
//	}
//
//	private class SETObserver extends ContentObserver {
//
//		public SETObserver(Handler handler) {
//			super(handler);
//		}
//
//		private void observe() {
//			ContentResolver resolver = mContext.getContentResolver();
//			resolver.registerContentObserver(Settings.System
//					.getUriFor(Settings.System.SCREEN_OFF_TIMEOUT), false, this);
//			resolver.registerContentObserver(Settings.System
//					.getUriFor(Settings.System.LOCATION_PROVIDERS_ALLOWED),
//					false, this);
//
//		}
//
//		public void unobserve() {
//			ContentResolver resolver = mContext.getContentResolver();
//			resolver.unregisterContentObserver(this);
//		}
//
//		@Override
//		public void onChange(boolean bool) {
//			super.onChange(bool);
//			DoUpdateToggles(getTogHolder(POS_SKRINTIMEOUT),
//					STO.howlong(mContext), STO.getBimtap(mContext));
//
//			DoUpdateToggles(getTogHolder(MY_POS[4]), GPS.get(mContext),
//					GPS.getBimtap(mContext));
//
//		}
//
//	}
//
//	private class mPSListener extends PhoneStateListener {
//
//		@Override
//		public void onDataConnectionStateChanged(int arg0) {
//			super.onDataConnectionStateChanged(arg0);
//			DoUpdateToggles(getTogHolder(MY_POS[11]),
//					NetWorkData.state(mContext),
//					NetWorkData.getBimtap(mContext));
//
//		}
//
//	}
//
//	private BroadcastReceiver myReceiver = new BroadcastReceiver() {
//
//		@Override
//		public void onReceive(Context contx, Intent intent) {
//			XLog.s(K.TAG_E, "OnReceiveBroadCast : " + intent.getAction());
//			String action = intent.getAction();
//			if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED))
//				DoUpdateToggles(getTogHolder(MY_POS[3]), Blutut.status(),
//						Blutut.getBimtap(mContext));
//			else if (intent.getAction().equals(
//					ConnectivityManager.ACTION_TETHER_STATE_CHANGED)) {
//				tetering_state = new Tether(mContext).cekUSBTether();
//				DoUpdateToggles(getTogHolder(MY_POS[8]),
//						CekStatus.USBTETHER(tetering_state, mContext),
//						Tether.getBimtap(tetering_state, mContext));
//			} else if (intent.getAction().equals(
//					WifiManager.WIFI_STATE_CHANGED_ACTION))
//				DoUpdateToggles(getTogHolder(MY_POS[6]), WiFi.status(mContext),
//						WiFi.getBimtap(mContext));
//			else if (intent.getAction().equals(
//					WifiManager.WIFI_AP_STATE_CHANGED_ACTION))
//				DoUpdateToggles(getTogHolder(MY_POS[7]),
//						WiFiAp.status(mContext), WiFiAp.getBimtap(mContext));
//			else if (intent.getAction().equals(
//					Intent.ACTION_AIRPLANE_MODE_CHANGED))
//				DoUpdateToggles(getTogHolder(MY_POS[9]),
//						AirPlane.getStatus(mContext),
//						AirPlane.getBimtap(mContext));
//			else if (action.equals(Musik.MUSIK_PLAY_STATECHANGE)) {
//				new Handler().postDelayed(new Runnable() {
//					@Override
//					public void run() {
//						DoUpdateToggles(getTogHolder(MY_POS[12]),
//								MPlay.state(mContext),
//								MPlay.getBimtap(mContext));
//					}
//				}, 1000);
//			}
//
//		}
//	};
//
//	private TogglesHolder getTogHolder(int which) {
//		XLog.s("GETTOGHOLDER", "xxx" + mGrid.getChildCount());
//		if (which > tooglescount || mGrid.getChildCount() < which)
//			return null;
//		else
//			return (TogglesHolder) (mGrid.getChildAt(which)).getTag();
//	}
//
//
//
//public void updateGrid() {
//
//	}
//
//	public void slideAnimate() {
//
//		aKontek = tema.getAnim(SBK.GRID_ANIM);
//		anim = AnimationUtils.loadAnimation(aKontek.kontek, aKontek.intejer);
//		GridLayoutAnimationController gridAnim = new GridLayoutAnimationController(
//				anim);
//		gridAnim.setDirection(GridLayoutAnimationController.DIRECTION_TOP_TO_BOTTOM
//				| GridLayoutAnimationController.DIRECTION_VERTICAL_MASK);
//		gridAnim.setRowDelay(0.3f);
//		mGrid.setLayoutAnimation(gridAnim);
//
//	}
//
//}
