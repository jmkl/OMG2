/*
 * Copyright (C) 2010 The Android Open Source Project
 * Patched by Sven Dawitz; Copyright (C) 2011 CyanogenMod Project
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

package com.android.systemui.statusbar;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;

import android.app.ActivityManagerNative;
import android.app.Dialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.app.StatusBarManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.IPowerManager;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.text.TextUtils;
import android.util.Pair;
import android.util.Slog;
import android.util.secutil.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManagerImpl;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

import com.android.internal.statusbar.IStatusBarService;
import com.android.internal.statusbar.StatusBarIcon;
import com.android.internal.statusbar.StatusBarIconList;
import com.android.internal.statusbar.StatusBarNotification;
import com.android.systemui.R;

import dcsms.omg.notification.NotificationLayout;
import dcsms.omg.notification.Toggles;
import dcsms.omg.statusbar.Batrai;
import dcsms.omg.statusbar.BatteryBar;
import dcsms.omg.statusbar.Jam;
import dcsms.omg.statusbar.Karir;
import dcsms.omg.statusbar.Sinyal;
import dcsms.omg.statusbar.StatusbarLayout;
import dcsms.omg.statusbar.Traffic;
import dcsms.omg.util.Model;
import dcsms.omg.util.SBK;
import dcsms.omg.util.Sett;
import dcsms.omg.util.Tema;
import dcsms.omg.util.getPref;

public class StatusBarService extends Service implements CommandQueue.Callbacks {
	static final String TAG = "StatusBarService";
	static final boolean SPEW_ICONS = false;
	static final boolean SPEW = false;
	ItemTouchDispatcher mTouchDispatcher;
	CallOnGoingView mCallOnGoingView;
	WindowManager.LayoutParams wmstatbar;
	WindowManager.LayoutParams wmanrounded;
	StatusbarLayout sblayout;
	View roundedView;
	Batrai sb_batt;
	Sinyal sb_sinyal;
	Traffic sb_trafik;
	Karir sb_karir;
	Jam sb_jam;
	BatteryBar sb_batbar;

	getPref pref;
	getPref sbPref;
	Tema sTema;

	public static final String ACTION_STATUSBAR_START = "com.android.internal.policy.statusbar.START";

	// values changed onCreate if its a bottomBar
	static int EXPANDED_LEAVE_ALONE = -10000;
	static int EXPANDED_FULL_OPEN = -10001;

	private static final int MSG_ANIMATE = 1000;
	private static final int MSG_ANIMATE_REVEAL = 1001;
	private Tema curTema;
	StatusBarPolicy mIconPolicy;
	StatusBarView mStatusBarView;
	ClocknShit klok;

	CommandQueue mCommandQueue;
	IStatusBarService mBarService;
	private boolean mPanelSlightlyVisible;

	private TextView tab_tog, tab_notif;
	Toggles TOGEL;
	FrameLayout NOTIF;
	boolean isNotif = true;
	private Button mSettingButton;
	private Tema mTema;
	/**
	 * Shallow container for {@link #mStatusBarView} which is added to the
	 * window manager impl as the actual status bar root view. This is done so
	 * that the original status_bar layout can be reinflated into this container
	 * on skin change.
	 */
	FrameLayout mStatusBarContainer;

	int mIconSize;
	Display mDisplay;

	// private class MyDisplay {
	// private int width;
	// private int height;
	//
	// public MyDisplay(WindowManager wm) {
	// DisplayMetrics dm = new DisplayMetrics();
	// wm.getDefaultDisplay().getMetrics(dm);
	// width = dm.widthPixels;
	// height = dm.heightPixels;
	// }
	//
	// public int getHeight() {
	// Log.e("TIGNGGIG", ": " + height);
	// return (int) (height * 1);
	// }
	//
	// public int getWidth() {
	// Log.e("LEBAR", ": " + width);
	// return (int) (width * 1);
	// }
	//
	// }

	int mPixelFormat;
	H mHandler = new H();
	Object mQueueLock = new Object();

	// icons
	LinearLayout mIcons;
	IconMerger mNotificationIcons;
	LinearLayout mStatusIcons;

	// expanded notifications
	Dialog mExpandedDialog;
	ExpandedView mExpandedView;
	WindowManager.LayoutParams mExpandedParams;
	ScrollView mScrollView;
	LinearLayout mNotificationLinearLayout;
	View mExpandedContents;
	// top bar
	TextView mNoNotificationsTitle;
	Button mClearButton;
	ViewGroup mClearButtonParent;
	// drag bar
	CloseDragHandle mCloseView;
	// ongoing
	NotificationData mOngoing = new NotificationData();
	TextView mOngoingTitle;
	NotificationLayout mOngoingItems;
	// latest
	NotificationData mLatest = new NotificationData();
	TextView mLatestTitle;
	NotificationLayout mLatestItems;
	// position
	int[] mPositionTmp = new int[2];
	boolean mExpanded;
	boolean mExpandedVisible;

	// the tracker view
	TrackingView mTrackingView;
	WindowManager.LayoutParams mTrackingParams;
	int mTrackingPosition; // the position of the top of the tracking view.

	// Carrier label stuff

	// ticker
	private MyTicker mTicker;
	private View mTickerView;
	private boolean mTicking;

	// Tracking finger for opening/closing.
	int mEdgeBorder; // corresponds to R.dimen.status_bar_edge_ignore
	boolean mTracking;
	VelocityTracker mVelocityTracker;

	static final int ANIM_FRAME_DURATION = (1000 / 60);

	boolean mAnimating;
	long mCurAnimationTime;
	float mDisplayHeight;
	float mAnimY;
	float mAnimVel;
	float mAnimAccel;
	long mAnimLastTime;
	boolean mAnimatingReveal = false;
	int mViewDelta;
	int[] mAbsPos = new int[2];

	// for disabling the status bar
	int mDisabled = 0;

	// weather or not to show status bar on bottom
	boolean mBottomBar;
	boolean mButtonsLeft;
	boolean mDeadZone;
	boolean mHasSoftButtons;
	Context mContext;

	// for brightness control on status bar
	int mLinger = 0;

	private class ExpandedDialog extends Dialog {
		ExpandedDialog(Context context) {
			super(context, com.android.internal.R.style.Theme_Light_NoTitleBar);
		}

		@Override
		public boolean dispatchKeyEvent(KeyEvent event) {
			boolean down = event.getAction() == KeyEvent.ACTION_DOWN;
			switch (event.getKeyCode()) {
			case KeyEvent.KEYCODE_BACK:
				if (!down) {
					animateCollapse();
				}
				return true;
			}
			return super.dispatchKeyEvent(event);
		}
	}

	@Override
	public void onCreate() {
		// First set up our views and stuff.
		mTema = new Tema(this);
		mDisplay = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay();
		makeStatusBarView(this);

		// receive broadcasts
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_CONFIGURATION_CHANGED);
		filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		filter.addAction(Intent.ACTION_TIME_TICK);
		registerReceiver(mBroadcastReceiver, filter);

		// Connect in to the status bar manager service
		StatusBarIconList iconList = new StatusBarIconList();
		ArrayList<IBinder> notificationKeys = new ArrayList<IBinder>();
		ArrayList<StatusBarNotification> notifications = new ArrayList<StatusBarNotification>();
		mCommandQueue = new CommandQueue(this, iconList);
		mBarService = IStatusBarService.Stub.asInterface(ServiceManager
				.getService(Context.STATUS_BAR_SERVICE));
		try {
			mBarService.registerStatusBar(mCommandQueue, iconList,
					notificationKeys, notifications);
		} catch (RemoteException ex) {
			// If the sys)tem process isn't there we're doomed anyway.
		}

		// Set up the initial icon state
		int N = iconList.size();
		int viewIndex = 0;
		for (int i = 0; i < N; i++) {
			StatusBarIcon icon = iconList.getIcon(i);
			if (icon != null) {
				addIcon(iconList.getSlot(i), i, viewIndex, icon);
				viewIndex++;
			}
		}

		// Set up the initial notification state
		N = notificationKeys.size();
		if (N == notifications.size()) {
			for (int i = 0; i < N; i++) {
				addNotification(notificationKeys.get(i), notifications.get(i));
			}
		} else {
			Slog.e(TAG, "Notification list length mismatch: keys=" + N
					+ " notifications=" + notifications.size());
		}

		// Put up the view
		FrameLayout container = new FrameLayout(this);
		container.addView(mStatusBarView);
		mStatusBarContainer = container;
		addStatusBarView();

		// Lastly, call to the icon policy to install/update all the icons.
		mIconPolicy = new StatusBarPolicy(this);

	}

	@Override
	public void onDestroy() {
		// we're never destroyed
	}

	/**
	 * Nobody binds to us.
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	// ================================================================================
	// Constructing the view
	// ================================================================================
	private void makeStatusBarView(Context context) {
		Resources res = context.getResources();
		curTema = new Tema(context);
		pref = new getPref(context, Model.PREF_JUDUL);
		sbPref = new getPref(context, SBK.STATBAR_PREF);
		sTema = new Tema(context);

		mTouchDispatcher = new ItemTouchDispatcher(this);
		mIconSize = res
				.getDimensionPixelSize(com.android.internal.R.dimen.status_bar_icon_size);
		ExpandedView expanded = (ExpandedView) View.inflate(context,
				R.layout.status_bar_expanded, null);
		expanded.mService = this;
		expanded.mTouchDispatcher = mTouchDispatcher;

		StatusBarView sb = (StatusBarView) View.inflate(context,
				R.layout.status_bar, null);
		sb.mService = this;

		// figure out which pixel-format to use for the status bar.
		mPixelFormat = PixelFormat.TRANSLUCENT;
		Drawable bg = sb.getBackground();
		if (bg != null) {
			mPixelFormat = bg.getOpacity();
		}

		mStatusBarView = sb;
		mStatusIcons = (LinearLayout) sb.findViewById(R.id.statusIcons);
		mNotificationIcons = (IconMerger) sb
				.findViewById(R.id.notificationIcons);
		mIcons = (LinearLayout) sb.findViewById(R.id.icons);
		mTickerView = sb.findViewById(R.id.ticker);
		/*
		 * Destroy any existing widgets before recreating the expanded dialog to
		 * ensure there are no lost context issues
		 */

		mExpandedDialog = new ExpandedDialog(context);
		mExpandedView = expanded;
		mExpandedContents = expanded.findViewById(R.id.chucknoris_layout);
		mOngoingTitle = (TextView) expanded.findViewById(R.id.ongoingTitle);
		mOngoingItems = (NotificationLayout) expanded
				.findViewById(R.id.ongoingItems);
		mLatestTitle = (TextView) expanded.findViewById(R.id.latestTitle);
		mLatestItems = (NotificationLayout) expanded
				.findViewById(R.id.latestItems);
		mNoNotificationsTitle = (TextView) expanded
				.findViewById(R.id.noNotificationsTitle);
		mClearButton = (Button) expanded.findViewById(R.id.clear_all_button);
		mClearButton.setOnClickListener(mClearButtonListener);
		mScrollView = (ScrollView) expanded.findViewById(R.id.scroll);
		mNotificationLinearLayout = (LinearLayout) expanded
				.findViewById(R.id.notificationLinearLayout);

		TOGEL = (Toggles) expanded.findViewById(R.id.toggles_shit);
		NOTIF = (FrameLayout) expanded.findViewById(R.id.notification_shit);

		mExpandedView.setVisibility(View.GONE);
		mOngoingTitle.setVisibility(View.GONE);
		mLatestTitle.setVisibility(View.GONE);

		mTicker = new MyTicker(context, sb);
		klok = (ClocknShit) expanded.findViewById(R.id.jam_expandedbar);
		mSettingButton = (Button) expanded.findViewById(R.id.tombol_seting);
		mSettingButton.setBackgroundDrawable(mTema.getICON(SBK.BTN_SETTING));
		mClearButton.setBackgroundDrawable(mTema.getICON(SBK.BTN_CLEAR));
		mSettingButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent();
				i.setClassName("com.android.settings",
						"com.android.settings.Settings");
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(i);
				animateCollapse();

			}
		});

		TickerView tickerView = (TickerView) sb.findViewById(R.id.tickerText);
		tickerView.mTicker = mTicker;

		mTrackingView = (TrackingView) View.inflate(context,
				R.layout.status_bar_tracking, null);
		mTrackingView.mService = this;
		mCloseView = (CloseDragHandle) mTrackingView.findViewById(R.id.close);
		mCloseView.mService = this;
		mTrackingView.setTrackingViewBG(mTema.getICON(SBK.TRACKINGVIEWBG_ALT));
		tab_tog = (TextView) expanded.findViewById(R.id.tab_togel);
		tab_notif = (TextView) expanded.findViewById(R.id.tab_notifikasi);
		tab_notif.setOnClickListener(ontabclick);
		tab_tog.setOnClickListener(ontabclick);
		TOGEL.setBackgroundDrawable(mTema.getICON(SBK.NOTIF_BG));
		mCloseView.setHandelImage(mTema.getICON(Tema.CLOSEDRAGHAND));
		setToggleState();
		mContext = context;
		updateLayout();

		mEdgeBorder = res.getDimensionPixelSize(R.dimen.status_bar_edge_ignore);
		mExpandedView.setBackgroundDrawable(mTema.getICON(SBK.TRACKINGVIEWBG));
		// set the inital view visibility
		//
		mCallOnGoingView = (CallOnGoingView) View.inflate(this,
				R.layout.status_bar_call_ongoing, null);
		mCallOnGoingView.mService = this;

		// XXX statusbarshit
		sblayout = (StatusbarLayout) sb.findViewById(R.id.statusbar_layout);
		sb_batt = (Batrai) sb.findViewById(R.id.sb_batrai);
		sb_sinyal = (Sinyal) sb.findViewById(R.id.sb_sinyal);
		sb_jam = (Jam) sb.findViewById(R.id.sb_jam);
		sb_karir = (Karir) sb.findViewById(R.id.sb_karir);

		sb_trafik = (Traffic) sb.findViewById(R.id.sb_trafik);
		sb_batbar = (BatteryBar) sb.findViewById(R.id.sb_baterai_line);
		//
		setTemanPref();
		setAreThereNotifications();
		arrangelayout();
	}

	private void setTemanPref() {
		sb_batt.setReferensi(mTema, sbPref, pref);
		sb_sinyal.setReferensi(mTema, sbPref, pref);
		sb_jam.setReferensi(mTema, sbPref, pref);
		sb_karir.setReferensi(mTema, sbPref, pref);
		sb_trafik.setReferensi(mTema, sbPref, pref);

	}

	public void hideCallOnGoingView() {
		mStatusBarView.removeView(mCallOnGoingView);
	}

	public void showCallOnGoingView() {
		if (mStatusBarView.indexOfChild(this.mCallOnGoingView) == -1)
			mStatusBarView.addView(this.mCallOnGoingView);
	}

	private class MyTicker extends Ticker {
		public MyTicker(Context context, StatusBarView sb) {
			super(context, sb);
		}

		@Override
		void tickerStarting() {
			if (SPEW)
				Slog.d(TAG, "tickerStarting");
			mTicking = true;
			mIcons.setVisibility(View.GONE);
			mTickerView.setVisibility(View.VISIBLE);
			mTickerView.startAnimation(loadAnim(
					com.android.internal.R.anim.push_up_in, null));
			mIcons.startAnimation(loadAnim(
					com.android.internal.R.anim.push_up_out, null));

		}

		@Override
		void tickerDone() {
			if (SPEW)
				Slog.d(TAG, "tickerDone");
			mTicking = false;
			mIcons.setVisibility(View.VISIBLE);
			mTickerView.setVisibility(View.GONE);
			mIcons.startAnimation(loadAnim(
					com.android.internal.R.anim.push_down_in, null));
			mTickerView.startAnimation(loadAnim(
					com.android.internal.R.anim.push_down_out, null));

		}

		void tickerHalting() {
			if (SPEW)
				Slog.d(TAG, "tickerHalting");
			mTicking = false;
			mIcons.setVisibility(View.VISIBLE);
			mTickerView.setVisibility(View.GONE);
			mIcons.startAnimation(loadAnim(com.android.internal.R.anim.fade_in,
					null));
			mTickerView.startAnimation(loadAnim(
					com.android.internal.R.anim.fade_out, null));

		}
	}

	private void updateLayout() {
		if (mTrackingView == null || mCloseView == null
				|| mExpandedView == null)
			return;

		// handle trackingview
		mTrackingView.removeView(mCloseView);
		mTrackingView.addView(mCloseView, mBottomBar ? 0 : 1);

		// FrameLayout
		// notifications=(FrameLayout)mExpandedView.findViewById(R.id.notifications);

		// Remove all notification views
		mNotificationLinearLayout.removeAllViews();

		// Readd to correct scrollview depending on mBottomBar

		mNotificationLinearLayout.addView(mNoNotificationsTitle);
		mNotificationLinearLayout.addView(mOngoingTitle);
		mNotificationLinearLayout.addView(mOngoingItems);
		mNotificationLinearLayout.addView(mLatestTitle);
		mNotificationLinearLayout.addView(mLatestItems);
		mScrollView.setVisibility(View.VISIBLE);

	}

	protected void addStatusBarView() {
		Resources res = getResources();
		final int height = res
				.getDimensionPixelSize(com.android.internal.R.dimen.status_bar_height);

		final View view = mStatusBarContainer;
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				height,
				WindowManager.LayoutParams.TYPE_STATUS_BAR,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_TOUCHABLE_WHEN_WAKING,
				PixelFormat.RGBX_8888);
		lp.gravity = Gravity.TOP | Gravity.FILL_HORIZONTAL;
		lp.setTitle("StatusBar");
		lp.windowAnimations = com.android.internal.R.style.Animation_StatusBar;
		wmstatbar = lp;
		WindowManagerImpl.getDefault().addView(view, lp);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		roundedView = inflater.inflate(R.layout.rounded, null);
		wmanrounded = new WindowManager.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, 0, 0,
				WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
						| WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
				PixelFormat.TRANSLUCENT);
		WindowManagerImpl.getDefault().addView(roundedView, wmanrounded);

	}

	public void addIcon(String slot, int index, int viewIndex,
			StatusBarIcon icon) {
		if (SPEW_ICONS) {
			Slog.d(TAG, "addIcon slot=" + slot + " index=" + index
					+ " viewIndex=" + viewIndex + " icon=" + icon);
		}
		StatusBarIconView view = new StatusBarIconView(this, slot);

		view.set(icon);
		mStatusIcons.addView(view, viewIndex, new LinearLayout.LayoutParams(
				mIconSize, mIconSize));
	}

	public void updateIcon(String slot, int index, int viewIndex,
			StatusBarIcon old, StatusBarIcon icon) {
		if (SPEW_ICONS) {
			Slog.d(TAG, "updateIcon slot=" + slot + " index=" + index
					+ " viewIndex=" + viewIndex + " old=" + old + " icon="
					+ icon);
		}
		StatusBarIconView view = (StatusBarIconView) mStatusIcons
				.getChildAt(viewIndex);
		view.set(icon);
	}

	public void removeIcon(String slot, int index, int viewIndex) {
		if (SPEW_ICONS) {
			Slog.d(TAG, "removeIcon slot=" + slot + " index=" + index
					+ " viewIndex=" + viewIndex);
		}
		mStatusIcons.removeViewAt(viewIndex);
	}

	public void addNotification(IBinder key, StatusBarNotification notification) {
		boolean shouldTick = true;
		if (notification.notification.fullScreenIntent != null) {
			shouldTick = false;
			Slog.d(TAG,
					"Notification has fullScreenIntent; sending fullScreenIntent");
			try {
				notification.notification.fullScreenIntent.send();
			} catch (PendingIntent.CanceledException e) {
			}
		}

		StatusBarIconView iconView = addNotificationViews(key, notification);
		if (iconView == null)
			return;

		if (shouldTick) {
			tick(notification);
		}

		// Recalculate the position of the sliding windows and the titles.
		setAreThereNotifications();
		updateExpandedViewPos(EXPANDED_LEAVE_ALONE);
	}

	public void updateNotification(IBinder key,
			StatusBarNotification notification) {
		NotificationData oldList;
		int oldIndex = mOngoing.findEntry(key);
		if (oldIndex >= 0) {
			oldList = mOngoing;
		} else {
			oldIndex = mLatest.findEntry(key);
			if (oldIndex < 0) {
				Slog.w(TAG, "updateNotification for unknown key: " + key);
				return;
			}
			oldList = mLatest;
		}
		final NotificationData.Entry oldEntry = oldList.getEntryAt(oldIndex);
		final StatusBarNotification oldNotification = oldEntry.notification;
		final RemoteViews oldContentView = oldNotification.notification.contentView;

		final RemoteViews contentView = notification.notification.contentView;

		// Can we just reapply the RemoteViews in place? If when didn't change,
		// the order
		// didn't change.
		if (notification.notification.when == oldNotification.notification.when
				&& notification.isOngoing() == oldNotification.isOngoing()
				&& oldEntry.expanded != null && contentView != null
				&& oldContentView != null && contentView.getPackage() != null
				&& oldContentView.getPackage() != null
				&& oldContentView.getPackage().equals(contentView.getPackage())
				&& oldContentView.getLayoutId() == contentView.getLayoutId()) {
			if (SPEW)
				Slog.d(TAG, "reusing notification");
			oldEntry.notification = notification;
			try {
				// Reapply the RemoteViews
				contentView.reapply(this, oldEntry.content);
				// update the contentIntent
				final PendingIntent contentIntent = notification.notification.contentIntent;
				if (contentIntent != null) {
					oldEntry.content.setOnClickListener(new Launcher(
							contentIntent, notification.pkg, notification.tag,
							notification.id));
				}
				// Update the icon.
				final StatusBarIcon ic = new StatusBarIcon(notification.pkg,
						notification.notification.icon,
						notification.notification.iconLevel,
						notification.notification.number);
				if (!oldEntry.icon.set(ic)) {
					handleNotificationError(key, notification,
							"Couldn't update icon: " + ic);
					return;
				}
			} catch (RuntimeException e) {
				// It failed to add cleanly. Log, and remove the view from the
				// panel.
				Slog.w(TAG,
						"Couldn't reapply views for package "
								+ contentView.getPackage(), e);
				removeNotificationViews(key);
				addNotificationViews(key, notification);
			}
		} else {
			if (SPEW)
				Slog.d(TAG, "not reusing notification");
			removeNotificationViews(key);
			addNotificationViews(key, notification);
		}

		// Restart the ticker if it's still running
		if (notification.notification.tickerText != null
				&& !TextUtils.equals(notification.notification.tickerText,
						oldEntry.notification.notification.tickerText)) {
			tick(notification);
		}

		// Recalculate the position of the sliding windows and the titles.
		setAreThereNotifications();
		updateExpandedViewPos(EXPANDED_LEAVE_ALONE);
	}

	public void removeNotification(IBinder key) {
		if (SPEW)
			Slog.d(TAG, "removeNotification key=" + key);
		StatusBarNotification old = removeNotificationViews(key);

		if (old != null) {
			// Cancel the ticker if it's still running
			mTicker.removeEntry(old);

			// Recalculate the position of the sliding windows and the titles.
			setAreThereNotifications();
			updateExpandedViewPos(EXPANDED_LEAVE_ALONE);
		}
	}

	private int chooseIconIndex(boolean isOngoing, int viewIndex) {
		final int latestSize = mLatest.size();
		if (isOngoing) {
			return latestSize + (mOngoing.size() - viewIndex);
		} else {
			return latestSize - viewIndex;
		}
	}

	View[] makeNotificationView(final StatusBarNotification notification,
			ViewGroup parent) {
		Notification n = notification.notification;
		RemoteViews remoteViews = n.contentView;
		if (remoteViews == null) {
			return null;
		}

		// create the row view
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LatestItemContainer row = (LatestItemContainer) inflater.inflate(
				R.layout.status_bar_latest_event, parent, false);
		if ((n.flags & Notification.FLAG_ONGOING_EVENT) == 0
				&& (n.flags & Notification.FLAG_NO_CLEAR) == 0) {
			row.setOnSwipeCallback(mTouchDispatcher, new Runnable() {
				public void run() {
					try {
						mBarService.onClearAllNotifications();
						// mBarService.onNotificationClick(notification.pkg,
						// notification.tag, notification.id);

					} catch (RemoteException e) {
						// Skip it, don't crash.
					}
				}
			});
		}

		// bind the click event to the content area
		ViewGroup content = (ViewGroup) row.findViewById(R.id.content);
		content.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
		content.setOnFocusChangeListener(mFocusChangeListener);
		PendingIntent contentIntent = n.contentIntent;
		if (contentIntent != null) {
			content.setOnClickListener(new Launcher(contentIntent,
					notification.pkg, notification.tag, notification.id));
		}

		View expanded = null;
		Exception exception = null;
		try {
			expanded = remoteViews.apply(this, content);
		} catch (RuntimeException e) {
			exception = e;
		}
		if (expanded == null) {
			String ident = notification.pkg + "/0x"
					+ Integer.toHexString(notification.id);
			Slog.e(TAG, "couldn't inflate view for notification " + ident,
					exception);
			return null;
		} else {
			content.addView(expanded);
			row.setDrawingCacheEnabled(true);
		}

		return new View[] { row, content, expanded };
	}

	StatusBarIconView addNotificationViews(IBinder key,
			StatusBarNotification notification) {
		NotificationData list;
		ViewGroup parent;
		final boolean isOngoing = notification.isOngoing();
		if (isOngoing) {
			list = mOngoing;
			parent = mOngoingItems;

		} else {
			list = mLatest;
			parent = mLatestItems;
		}
		// Construct the expanded view.
		final View[] views = makeNotificationView(notification, parent);
		if (views == null) {
			handleNotificationError(key, notification,
					"Couldn't expand RemoteViews for: " + notification);
			return null;
		}
		final View row = views[0];
		final View content = views[1];
		final View expanded = views[2];
		// Construct the icon.
		final StatusBarIconView iconView = new StatusBarIconView(this,
				notification.pkg + "/0x" + Integer.toHexString(notification.id));
		String paket = notification.pkg;

		StatusBarIcon ic = new StatusBarIcon(notification.pkg,
				notification.notification.icon,
				notification.notification.iconLevel,
				notification.notification.number);

		if (!iconView.set(ic)) {
			handleNotificationError(key, notification, "Coulding create icon: "
					+ ic);
			return null;
		}
		// Add the expanded view.
		final int viewIndex = list.add(key, notification, row, content,
				expanded, iconView);
		parent.addView(row, viewIndex);
		// XXX update notification
		((NotificationLayout) parent).doshit();
		// Add the icon.
		final int iconIndex = chooseIconIndex(isOngoing, viewIndex);
		mNotificationIcons.addView(iconView, iconIndex);
		return iconView;
	}

	StatusBarNotification removeNotificationViews(IBinder key) {
		NotificationData.Entry entry = mOngoing.remove(key);
		if (entry == null) {
			entry = mLatest.remove(key);
			if (entry == null) {
				Slog.w(TAG, "removeNotification for unknown key: " + key);
				return null;
			}
		}
		// Remove the expanded view.
		((ViewGroup) entry.row.getParent()).removeView(entry.row);
		// Remove the icon.
		((ViewGroup) entry.icon.getParent()).removeView(entry.icon);

		return entry.notification;
	}

	private void setAreThereNotifications() {
		boolean ongoing = mOngoing.hasVisibleItems();
		boolean latest = mLatest.hasVisibleItems();

		// (no ongoing notifications are clearable)
		if (mLatest.hasClearableItems()) {

			mClearButton.setVisibility(View.VISIBLE);
		} else {
			mClearButton.setVisibility(View.INVISIBLE);
		}

		mOngoingTitle.setVisibility(ongoing ? View.VISIBLE : View.GONE);
		mLatestTitle.setVisibility(latest ? View.VISIBLE : View.GONE);

		if (ongoing || latest) {
			mNoNotificationsTitle.setVisibility(View.GONE);
		} else {
			mNoNotificationsTitle.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * State is one or more of the DISABLE constants from StatusBarManager.
	 */
	public void disable(int state) {
		final int old = mDisabled;
		final int diff = state ^ old;
		mDisabled = state;

		if ((diff & StatusBarManager.DISABLE_EXPAND) != 0) {
			if ((state & StatusBarManager.DISABLE_EXPAND) != 0) {
				if (SPEW)
					Slog.d(TAG, "DISABLE_EXPAND: yes");
				animateCollapse();
			}
		}
		if ((diff & StatusBarManager.DISABLE_NOTIFICATION_ICONS) != 0) {
			if ((state & StatusBarManager.DISABLE_NOTIFICATION_ICONS) != 0) {
				if (SPEW)
					Slog.d(TAG, "DISABLE_NOTIFICATION_ICONS: yes");
				if (mTicking) {
					mTicker.halt();
				} else {
					setNotificationIconVisibility(false,
							com.android.internal.R.anim.fade_out);
				}
			} else {
				if (SPEW)
					Slog.d(TAG, "DISABLE_NOTIFICATION_ICONS: no");
				if (!mExpandedVisible) {
					setNotificationIconVisibility(true,
							com.android.internal.R.anim.fade_in);
				}
			}
		} else if ((diff & StatusBarManager.DISABLE_NOTIFICATION_TICKER) != 0) {
			if (mTicking
					&& (state & StatusBarManager.DISABLE_NOTIFICATION_TICKER) != 0) {
				if (SPEW)
					Slog.d(TAG, "DISABLE_NOTIFICATION_TICKER: yes");
				mTicker.halt();
			}
		}
	}

	/**
	 * All changes to the status bar and notifications funnel through here and
	 * are batched.
	 */
	private class H extends Handler {
		public void handleMessage(Message m) {
			switch (m.what) {
			case MSG_ANIMATE:
				doAnimation();
				break;
			case MSG_ANIMATE_REVEAL:
				doRevealAnimation();
				break;
			}
		}
	}

	View.OnFocusChangeListener mFocusChangeListener = new View.OnFocusChangeListener() {
		public void onFocusChange(View v, boolean hasFocus) {
			// Because 'v' is a ViewGroup, all its children will be (un)selected
			// too, which allows marqueeing to work.
			v.setSelected(hasFocus);
		}
	};

	private void makeExpandedVisible() {
		if (SPEW)
			Slog.d(TAG, "Make expanded visible: expanded visible="
					+ mExpandedVisible);
		if (mExpandedVisible) {
			return;
		}
		mExpandedVisible = true;
		visibilityChanged(true);

		updateExpandedViewPos(EXPANDED_LEAVE_ALONE);
		mExpandedParams.flags &= ~WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		mExpandedParams.flags |= WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
		mExpandedDialog.getWindow().setAttributes(mExpandedParams);
		mExpandedView.requestFocus(View.FOCUS_FORWARD);
		mTrackingView.setVisibility(View.VISIBLE);
		mExpandedView.setVisibility(View.VISIBLE);

	}

	public void animateExpand() {
		if (SPEW)
			Slog.d(TAG, "Animate expand: expanded=" + mExpanded);
		if ((mDisabled & StatusBarManager.DISABLE_EXPAND) != 0) {
			return;
		}
		if (mExpanded) {
			return;
		}

		prepareTracking(0, true);
		performFling(0, 2000.0f, true);
	}

	public void animateCollapse() {
		if (SPEW) {
			Slog.d(TAG, "animateCollapse(): mExpanded=" + mExpanded
					+ " mExpandedVisible=" + mExpandedVisible + " mExpanded="
					+ mExpanded + " mAnimating=" + mAnimating + " mAnimY="
					+ mAnimY + " mAnimVel=" + mAnimVel);
		}

		if (!mExpandedVisible) {
			return;
		}

		int y;
		if (mAnimating) {
			y = (int) mAnimY;
		} else {
			if (mBottomBar)
				y = 0;
			else
				y = mDisplay.getHeight() - 1;
		}
		// Let the fling think that we're open so it goes in the right direction
		// and doesn't try to re-open the windowshade.
		mExpanded = true;
		prepareTracking(y, false);
		performFling(y, -2000.0f, true);
	}

	void performExpand() {
		if (SPEW)
			Slog.d(TAG, "performExpand: mExpanded=" + mExpanded);
		if ((mDisabled & StatusBarManager.DISABLE_EXPAND) != 0) {
			return;
		}
		if (mExpanded) {
			return;
		}

		mExpanded = true;
		makeExpandedVisible();
		updateExpandedViewPos(EXPANDED_FULL_OPEN);

		if (false)
			postStartTracing();
	}

	void performCollapse() {
		if (SPEW)
			Slog.d(TAG, "performCollapse: mExpanded=" + mExpanded
					+ " mExpandedVisible=" + mExpandedVisible + " mTicking="
					+ mTicking);

		if (!mExpandedVisible) {
			return;
		}
		mExpandedVisible = false;
		visibilityChanged(false);
		mExpandedParams.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		mExpandedParams.flags &= ~WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
		mExpandedDialog.getWindow().setAttributes(mExpandedParams);
		mTrackingView.setVisibility(View.GONE);
		mExpandedView.setVisibility(View.GONE);

		if ((mDisabled & StatusBarManager.DISABLE_NOTIFICATION_ICONS) == 0) {
			setNotificationIconVisibility(true,
					com.android.internal.R.anim.fade_in);
		}

		if (!mExpanded) {
			return;
		}
		mExpanded = false;
	}

	void doAnimation() {
		if (mAnimating) {
			if (SPEW)
				Slog.d(TAG, "doAnimation");
			if (SPEW)
				Slog.d(TAG, "doAnimation before mAnimY=" + mAnimY);
			incrementAnim();
			if (SPEW)
				Slog.d(TAG, "doAnimation after  mAnimY=" + mAnimY);
			if ((!mBottomBar && mAnimY >= mDisplay.getHeight() - 1)
					|| (mBottomBar && mAnimY <= 0)) {
				if (SPEW)
					Slog.d(TAG, "Animation completed to expanded state.");
				mAnimating = false;
				updateExpandedViewPos(EXPANDED_FULL_OPEN);
				performExpand();
			} else if ((!mBottomBar && mAnimY < mStatusBarView.Height())
					|| (mBottomBar && mAnimY > (mDisplay.getHeight() - mStatusBarView
							.getHeight()))) {
				if (SPEW)
					Slog.d(TAG, "Animation completed to collapsed state.");
				mAnimating = false;
				if (mBottomBar)
					updateExpandedViewPos(mDisplay.getHeight());
				else
					updateExpandedViewPos(0);
				performCollapse();
			} else {
				updateExpandedViewPos((int) mAnimY);
				mCurAnimationTime += ANIM_FRAME_DURATION;
				mHandler.sendMessageAtTime(mHandler.obtainMessage(MSG_ANIMATE),
						mCurAnimationTime);
			}
		}
	}

	void stopTracking() {
		mTracking = false;
		mVelocityTracker.recycle();
		mVelocityTracker = null;
	}

	void incrementAnim() {
		long now = SystemClock.uptimeMillis();
		float t = ((float) (now - mAnimLastTime)) / 1000; // ms -> s
		final float y = mAnimY;
		final float v = mAnimVel; // px/s
		final float a = mAnimAccel; // px/s/s
		if (mBottomBar)
			mAnimY = y - (v * t) - (0.5f * a * t * t); // px
		else
			mAnimY = y + (v * t) + (0.5f * a * t * t); // px
		mAnimVel = v + (a * t); // px/s
		mAnimLastTime = now; // ms
		// Slog.d(TAG, "y=" + y + " v=" + v + " a=" + a + " t=" + t + " mAnimY="
		// + mAnimY
		// + " mAnimAccel=" + mAnimAccel);
	}

	void doRevealAnimation() {
		int h = mCloseView.Height() + mStatusBarView.Height();

		if (mBottomBar)
			h = mDisplay.getHeight() - mStatusBarView.Height();
		if (mAnimatingReveal && mAnimating
				&& ((mBottomBar && mAnimY > h) || (!mBottomBar && mAnimY < h))) {
			incrementAnim();
			if ((mBottomBar && mAnimY <= h) || (!mBottomBar && mAnimY >= h)) {
				mAnimY = h;
				updateExpandedViewPos((int) mAnimY);
			} else {
				updateExpandedViewPos((int) mAnimY);
				mCurAnimationTime += ANIM_FRAME_DURATION;
				mHandler.sendMessageAtTime(
						mHandler.obtainMessage(MSG_ANIMATE_REVEAL),
						mCurAnimationTime);
			}
		}
	}

	void prepareTracking(int y, boolean opening) {
		mTracking = true;
		mVelocityTracker = VelocityTracker.obtain();
		if (opening) {
			mAnimAccel = 2000.0f;
			mAnimVel = 200;
			mAnimY = mBottomBar ? mDisplay.getHeight() : mStatusBarView
					.getHeight();
			updateExpandedViewPos((int) mAnimY);
			mAnimating = true;
			mAnimatingReveal = true;
			mHandler.removeMessages(MSG_ANIMATE);
			mHandler.removeMessages(MSG_ANIMATE_REVEAL);
			long now = SystemClock.uptimeMillis();
			mAnimLastTime = now;
			mCurAnimationTime = now + ANIM_FRAME_DURATION;
			mAnimating = true;
			mHandler.sendMessageAtTime(
					mHandler.obtainMessage(MSG_ANIMATE_REVEAL),
					mCurAnimationTime);
			makeExpandedVisible();
		} else {
			// it's open, close it?
			if (mAnimating) {
				mAnimating = false;
				mHandler.removeMessages(MSG_ANIMATE);
			}
			updateExpandedViewPos(y + mViewDelta);
		}
	}

	void performFling(int y, float vel, boolean always) {
		mAnimatingReveal = false;
		mDisplayHeight = mDisplay.getHeight();

		mAnimY = y;
		mAnimVel = vel;

		// Slog.d(TAG, "starting with mAnimY=" + mAnimY + " mAnimVel=" +
		// mAnimVel);

		if (mExpanded) {
			if (!always
					&& ((mBottomBar && (vel < -200.0f || (y < 25 && vel < 200.0f))) || (!mBottomBar && (vel > 200.0f || (y > (mDisplayHeight - 25) && vel > -200.0f))))) {
				// We are expanded, but they didn't move sufficiently to cause
				// us to retract. Animate back to the expanded position.
				mAnimAccel = 2000.0f;
				if (vel < 0) {
					mAnimVel *= -1;
				}
			} else {
				// We are expanded and are now going to animate away.
				mAnimAccel = -2000.0f;
				if (vel > 0) {
					mAnimVel *= -1;
				}
			}
		} else {
			if (always
					|| (mBottomBar && (vel < -200.0f || (y < (mDisplayHeight / 2) && vel < 200.0f)))
					|| (!mBottomBar && (vel > 200.0f || (y > (mDisplayHeight / 2) && vel > -200.0f)))) {
				// We are collapsed, and they moved enough to allow us to
				// expand. Animate in the notifications.
				mAnimAccel = 2000.0f;
				if (vel < 0) {
					mAnimVel *= -1;
				}
			} else {
				// We are collapsed, but they didn't move sufficiently to cause
				// us to retract. Animate back to the collapsed position.
				mAnimAccel = -2000.0f;
				if (vel > 0) {
					mAnimVel *= -1;
				}
			}
		}
		// Slog.d(TAG, "mAnimY=" + mAnimY + " mAnimVel=" + mAnimVel
		// + " mAnimAccel=" + mAnimAccel);

		long now = SystemClock.uptimeMillis();
		mAnimLastTime = now;
		mCurAnimationTime = now + ANIM_FRAME_DURATION;
		mAnimating = true;
		mHandler.removeMessages(MSG_ANIMATE);
		mHandler.removeMessages(MSG_ANIMATE_REVEAL);
		mHandler.sendMessageAtTime(mHandler.obtainMessage(MSG_ANIMATE),
				mCurAnimationTime);
		stopTracking();
	}

	boolean interceptTouchEvent(MotionEvent event) {
		if (SPEW) {
			Slog.d(TAG, "Touch: rawY=" + event.getRawY() + " event=" + event
					+ " mDisabled=" + mDisabled);
		}

		if ((mDisabled & StatusBarManager.DISABLE_EXPAND) != 0) {
			return false;
		}

		if (!mTrackingView.mIsAttachedToWindow) {
			return false;
		}

		final int statusBarSize = mStatusBarView.Height();
		final int hitSize = statusBarSize * 2;
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			final int y = (int) event.getRawY();
			mLinger = 0;
			if (!mExpanded) {
				mViewDelta = mBottomBar ? mDisplay.getHeight() - y
						: statusBarSize - y;
			} else {
				mTrackingView.getLocationOnScreen(mAbsPos);
				mViewDelta = mAbsPos[1]
						+ (mBottomBar ? 0 : mTrackingView.getHeight()) - y;
			}
			if ((!mBottomBar && ((!mExpanded && y < hitSize) || (mExpanded && y > (mDisplay
					.getHeight() - hitSize))))
					|| (mBottomBar && ((mExpanded && y < hitSize) || (!mExpanded && y > (mDisplay
							.getHeight() - hitSize))))) {

				// We drop events at the edge of the screen to make the
				// windowshade come
				// down by accident less, especially when pushing open a device
				// with a keyboard
				// that rotates (like g1 and droid)
				int x = (int) event.getRawX();

				final int edgeBorder = mEdgeBorder;
				int edgeLeft = mButtonsLeft ? mStatusBarView
						.getSoftButtonsWidth() : 0;
				int edgeRight = mButtonsLeft ? 0 : mStatusBarView
						.getSoftButtonsWidth();

				final int w = mDisplay.getWidth();
				final int deadLeft = w / 2 - w / 4; // left side of the dead
													// zone
				final int deadRight = w / 2 + w / 4; // right side of the dead
														// zone

				boolean expandedHit = (mExpanded && (x >= edgeBorder && x < w
						- edgeBorder));
				boolean collapsedHit = (!mExpanded
						&& (x >= edgeBorder + edgeLeft && x < w - edgeBorder
								- edgeRight) && (!mDeadZone || mDeadZone
						&& (x < deadLeft || x > deadRight)));

				if (expandedHit || collapsedHit) {
					prepareTracking(y, !mExpanded);// opening if we're not
													// already fully visible
					mVelocityTracker.addMovement(event);
				}
			}
		} else if (mTracking) {
			mVelocityTracker.addMovement(event);
			int minY = statusBarSize + mCloseView.Height();
			if (mBottomBar)
				minY = mDisplay.getHeight() - statusBarSize
						- mCloseView.Height();
			if (event.getAction() == MotionEvent.ACTION_MOVE) {
				int y = (int) event.getRawY();
				if ((!mBottomBar && mAnimatingReveal && y < minY)
						|| (mBottomBar && mAnimatingReveal && y > minY)) {
					try {
						if (Settings.System.getInt(mStatusBarView.getContext()
								.getContentResolver(),
								Settings.System.SCREEN_BRIGHTNESS) == 1) {
							// Credit for code goes to daryelv github :
							// https://github.com/daryelv/android_frameworks_base
							// See if finger is moving left/right an adequate
							// amount
							mVelocityTracker.computeCurrentVelocity(1000);
							float yVel = mVelocityTracker.getYVelocity();
							if (yVel < 0) {
								yVel = -yVel;
							}
							if (yVel < 50.0f) {
								if (mLinger > 50) {
									// Check that Auto-Brightness not enabled
									Context context = mStatusBarView
											.getContext();
									boolean auto_brightness = false;
									int brightness_mode = 0;
									try {
										brightness_mode = Settings.System
												.getInt(context
														.getContentResolver(),
														Settings.System.SCREEN_BRIGHTNESS_MODE);
									} catch (SettingNotFoundException e) {
										auto_brightness = false;
									}
									auto_brightness = (brightness_mode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
									if (auto_brightness) {
										// do nothing - Don't manually set
										// brightness from statusbar
									} else {
										// set brightness according to x
										// position on statusbar
										float x = (float) event.getRawX();
										float screen_width = (float) (context
												.getResources()
												.getDisplayMetrics().widthPixels);
										// Brightness set from the 90% of pixels
										// in the middle of screen, can't always
										// get to the edges
										int new_brightness = (int) (((x - (screen_width * 0.05f)) / (screen_width * 0.9f)) * (float) android.os.Power.BRIGHTNESS_ON);
										// don't let screen go completely dim or
										// past 100% bright
										if (new_brightness < 10)
											new_brightness = 10;
										if (new_brightness > android.os.Power.BRIGHTNESS_ON)
											new_brightness = android.os.Power.BRIGHTNESS_ON;
										// Set the brightness
										try {
											IPowerManager.Stub
													.asInterface(
															ServiceManager
																	.getService("power"))
													.setBacklightBrightness(
															new_brightness);
											Settings.System
													.putInt(context
															.getContentResolver(),
															Settings.System.SCREEN_BRIGHTNESS,
															new_brightness);
										} catch (Exception e) {
											Slog.w(TAG,
													"Setting Brightness failed: "
															+ e);
										}
									}
								} else {
									mLinger++;
								}
							} else {
								mLinger = 0;
							}
						}
					} catch (SettingNotFoundException e) {
					}
				} else {
					mAnimatingReveal = false;
					updateExpandedViewPos(y
							+ (mBottomBar ? -mViewDelta : mViewDelta));
				}
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				mVelocityTracker.computeCurrentVelocity(1000);

				float yVel = mVelocityTracker.getYVelocity();
				boolean negative = yVel < 0;

				float xVel = mVelocityTracker.getXVelocity();
				if (xVel < 0) {
					xVel = -xVel;
				}
				if (xVel > 150.0f) {
					xVel = 150.0f; // limit how much we care about the x axis
				}

				float vel = (float) Math.hypot(yVel, xVel);
				if (negative) {
					vel = -vel;
				}

				performFling((int) event.getRawY(), vel, false);
			}

		}
		return false;
	}

	private class Launcher implements View.OnClickListener {
		private PendingIntent mIntent;
		private String mPkg;
		private String mTag;
		private int mId;

		Launcher(PendingIntent intent, String pkg, String tag, int id) {
			mIntent = intent;
			mPkg = pkg;
			mTag = tag;
			mId = id;
		}

		public void onClick(View v) {
			try {
				// The intent we are sending is for the application, which
				// won't have permission to immediately start an activity after
				// the user switches to home. We know it is safe to do at this
				// point, so make sure new activity switches are now allowed.
				ActivityManagerNative.getDefault().resumeAppSwitches();
			} catch (RemoteException e) {
			}

			if (mIntent != null) {
				int[] pos = new int[2];
				v.getLocationOnScreen(pos);
				Intent overlay = new Intent();
				overlay.setSourceBounds(new Rect(pos[0], pos[1], pos[0]
						+ v.getWidth(), pos[1] + v.getHeight()));
				try {
					mIntent.send(StatusBarService.this, 0, overlay);
				} catch (PendingIntent.CanceledException e) {
					// the stack trace isn't very helpful here. Just log the
					// exception message.
					Slog.w(TAG, "Sending contentIntent failed: " + e);
				}
			}

			try {
				mBarService.onNotificationClick(mPkg, mTag, mId);
			} catch (RemoteException ex) {
				// system process is dead if we're here.
			}

			// close the shade if it was open
			animateCollapse();
		}
	}

	private void tick(StatusBarNotification n) {
		// Show the ticker if one is requested. Also don't do this
		// until status bar window is attached to the window manager,
		// because... well, what's the point otherwise? And trying to
		// run a ticker without being attached will crash!
		if (n.notification.tickerText != null
				&& mStatusBarView.getWindowToken() != null) {
			if (0 == (mDisabled & (StatusBarManager.DISABLE_NOTIFICATION_ICONS | StatusBarManager.DISABLE_NOTIFICATION_TICKER))) {
				if (!mHasSoftButtons) {
					mTicker.addEntry(n);
					Slog.e("NOTIFICATION STATUSBAR", n.pkg);
				}
			}
		}
	}

	/**
	 * Cancel this notification and tell the StatusBarManagerService /
	 * NotificationManagerService about the failure.
	 * 
	 * WARNING: this will call back into us. Don't hold any locks.
	 */
	void handleNotificationError(IBinder key, StatusBarNotification n,
			String message) {
		removeNotification(key);
		try {
			mBarService.onNotificationError(n.pkg, n.tag, n.id, n.uid,
					n.initialPid, message);
		} catch (RemoteException ex) {
			// The end is nigh.
		}
	}

	private Animation loadAnim(int id, Animation.AnimationListener listener) {
		Animation anim = AnimationUtils
				.loadAnimation(StatusBarService.this, id);
		if (listener != null) {
			anim.setAnimationListener(listener);
		}
		return anim;
	}

	public String viewInfo(View v) {
		return "(" + v.getLeft() + "," + v.getTop() + ")(" + v.getRight() + ","
				+ v.getBottom() + " " + v.getWidth() + "x" + v.getHeight()
				+ ")";
	}

	protected void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
		if (checkCallingOrSelfPermission(android.Manifest.permission.DUMP) != PackageManager.PERMISSION_GRANTED) {
			pw.println("Permission Denial: can't dump StatusBar from from pid="
					+ Binder.getCallingPid() + ", uid="
					+ Binder.getCallingUid());
			return;
		}

		synchronized (mQueueLock) {
			pw.println("Current Status Bar state:");
			pw.println("  mExpanded=" + mExpanded + ", mExpandedVisible="
					+ mExpandedVisible);
			pw.println("  mTicking=" + mTicking);
			pw.println("  mTracking=" + mTracking);
			pw.println("  mAnimating=" + mAnimating + ", mAnimY=" + mAnimY
					+ ", mAnimVel=" + mAnimVel + ", mAnimAccel=" + mAnimAccel);
			pw.println("  mCurAnimationTime=" + mCurAnimationTime
					+ " mAnimLastTime=" + mAnimLastTime);
			pw.println("  mDisplayHeight=" + mDisplayHeight
					+ " mAnimatingReveal=" + mAnimatingReveal + " mViewDelta="
					+ mViewDelta);
			pw.println("  mDisplayHeight=" + mDisplayHeight);
			pw.println("  mExpandedParams: " + mExpandedParams);
			pw.println("  mExpandedView: " + viewInfo(mExpandedView));
			pw.println("  mExpandedDialog: " + mExpandedDialog);
			pw.println("  mTrackingParams: " + mTrackingParams);
			pw.println("  mTrackingView: " + viewInfo(mTrackingView));
			pw.println("  mOngoingTitle: " + viewInfo(mOngoingTitle));
			pw.println("  mOngoingItems: " + viewInfo(mOngoingItems));
			pw.println("  mLatestTitle: " + viewInfo(mLatestTitle));
			pw.println("  mLatestItems: " + viewInfo(mLatestItems));
			pw.println("  mNoNotificationsTitle: "
					+ viewInfo(mNoNotificationsTitle));
			pw.println("  mCloseView: " + viewInfo(mCloseView));
			pw.println("  mTickerView: " + viewInfo(mTickerView));
			pw.println("  mScrollView: " + viewInfo(mScrollView) + " scroll "
					+ mScrollView.getScrollX() + "," + mScrollView.getScrollY());
		}

		if (true) {
			// must happen on ui thread
			mHandler.post(new Runnable() {
				public void run() {
					Slog.d(TAG, "mStatusIcons:");
					mStatusIcons.debug();
				}
			});
		}

	}

	void onBarViewAttached() {
		WindowManager.LayoutParams lp;
		int pixelFormat;
		Drawable bg;

		// / ---------- Tracking View --------------
		pixelFormat = PixelFormat.TRANSLUCENT;
		bg = mTrackingView.getBackground();
		if (bg != null) {
			pixelFormat = bg.getOpacity();
		}

		lp = new WindowManager.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
				WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
						| WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
						| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
				pixelFormat);
		// lp.token = mStatusBarView.getWindowToken();
		lp.gravity = Gravity.TOP | Gravity.FILL_HORIZONTAL;
		lp.setTitle("TrackingView");
		lp.y = mTrackingPosition;
		mTrackingParams = lp;

		WindowManagerImpl.getDefault().addView(mTrackingView, lp);
	}

	void onBarViewDetached() {
		WindowManagerImpl.getDefault().removeView(mTrackingView);
	}

	void onTrackingViewAttached() {
		WindowManager.LayoutParams lp;
		int pixelFormat;
		Drawable bg;

		// / ---------- Expanded View --------------
		pixelFormat = PixelFormat.TRANSLUCENT;

		final int disph = mDisplay.getHeight();
		lp = mExpandedDialog.getWindow().getAttributes();
		lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
		lp.height = getExpandedHeight();
		lp.x = 0;
		mTrackingPosition = lp.y = (mBottomBar ? disph : -disph); // sufficiently
																	// large
																	// positive
		lp.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
		lp.flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
				| WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
				| WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
				| WindowManager.LayoutParams.FLAG_DITHER
				| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		lp.format = pixelFormat;
		lp.gravity = Gravity.TOP | Gravity.FILL_HORIZONTAL;
		lp.setTitle("StatusBarExpanded");
		mExpandedDialog.getWindow().setAttributes(lp);
		mExpandedDialog.getWindow().setFormat(pixelFormat);
		mExpandedParams = lp;

		mExpandedDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		mExpandedDialog.setContentView(mExpandedView,
				new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
						ViewGroup.LayoutParams.MATCH_PARENT));
		mExpandedDialog.getWindow().setBackgroundDrawable(null);
		mExpandedDialog.show();
		FrameLayout hack = (FrameLayout) mExpandedView.getParent();
	}

	void onTrackingViewDetached() {
	}

	void setNotificationIconVisibility(boolean visible, int anim) {
		int old = mNotificationIcons.getVisibility();
		int v = visible ? View.VISIBLE : View.INVISIBLE;
		if (old != v) {
			mNotificationIcons.setVisibility(v);
			mNotificationIcons.startAnimation(loadAnim(anim, null));
		}
	}

	void updateExpandedViewPos(int expandedPosition) {
		if (SPEW) {
			Slog.d(TAG, "updateExpandedViewPos before expandedPosition="
					+ expandedPosition + " mTrackingParams.y="
					+ ((mTrackingParams == null) ? "???" : mTrackingParams.y)
					+ " mTrackingPosition=" + mTrackingPosition);
		}

		int h = 0;// mStatusBarView.Height();
		int disph = mDisplay.getHeight();

		// If the expanded view is not visible, make sure they're still off
		// screen.
		// Maybe the view was resized.
		if (!mExpandedVisible) {
			if (mTrackingView != null) {
				mTrackingPosition = mBottomBar ? disph : -disph;
				if (mTrackingParams != null) {
					mTrackingParams.y = mTrackingPosition;
					WindowManagerImpl.getDefault().updateViewLayout(
							mTrackingView, mTrackingParams);
				}
			}
			if (mExpandedParams != null) {
				mExpandedParams.y = mBottomBar ? disph : -disph;
				mExpandedDialog.getWindow().setAttributes(mExpandedParams);
			}
			return;
		}

		// tracking view...
		int pos;
		if (expandedPosition == EXPANDED_FULL_OPEN) {
			pos = h;
		} else if (expandedPosition == EXPANDED_LEAVE_ALONE) {
			pos = mTrackingPosition;
		} else {
			if ((mBottomBar && expandedPosition >= 0)
					|| (!mBottomBar && expandedPosition <= disph)) {
				pos = expandedPosition;
			} else {
				pos = disph;
			}
			pos -= mBottomBar ? mCloseView.Height() : disph - h;
		}
		if (mBottomBar && pos < 0)
			pos = 0;

		mTrackingPosition = mTrackingParams.y = pos;
		mTrackingParams.height = disph - h;
		WindowManagerImpl.getDefault().updateViewLayout(mTrackingView,
				mTrackingParams);

		if (mExpandedParams != null) {
			mCloseView.getLocationInWindow(mPositionTmp);
			final int closePos = mPositionTmp[1];

			mExpandedContents.getLocationInWindow(mPositionTmp);
			final int contentsBottom = mPositionTmp[1]
					+ mExpandedContents.getHeight();

			if (expandedPosition != EXPANDED_LEAVE_ALONE) {
				if (mBottomBar)
					mExpandedParams.y = pos + mCloseView.Height();
				else
					mExpandedParams.y = pos + mTrackingView.getHeight()
							- (mTrackingParams.height - closePos)
							- contentsBottom;
				int max = mBottomBar ? mDisplay.getHeight() : h;
				if (mExpandedParams.y > max) {
					mExpandedParams.y = max;
				}
				int min = mBottomBar ? mCloseView.Height() : mTrackingPosition;
				if (mExpandedParams.y < min) {
					mExpandedParams.y = min;
					if (mBottomBar)
						mTrackingParams.y = 0;
				}

				boolean visible = mBottomBar ? mTrackingPosition < mDisplay
						.getHeight() : (mTrackingPosition + mTrackingView
						.getHeight()) > h;
				if (!visible) {
					// if the contents aren't visible, move the expanded view
					// way off screen
					// because the window itself extends below the content view.
					mExpandedParams.y = mBottomBar ? disph : -disph;
				}
				mExpandedDialog.getWindow().setAttributes(mExpandedParams);

				if (SPEW)
					Slog.d(TAG, "updateExpandedViewPos visibilityChanged("
							+ visible + ")");
				visibilityChanged(visible);
			}
		}

		if (SPEW) {
			Slog.d(TAG, "updateExpandedViewPos after  expandedPosition="
					+ expandedPosition + " mTrackingParams.y="
					+ mTrackingParams.y + " mTrackingView.getHeight="
					+ mTrackingView.getHeight() + " mTrackingPosition="
					+ mTrackingPosition + " mExpandedParams.y="
					+ mExpandedParams.y + " mExpandedParams.height="
					+ mExpandedParams.height + " mDisplay.height="
					+ mExpandedParams.height);
		}
	}

	int getExpandedHeight() {
		return mDisplay.getHeight() - mCloseView.Height();// XXX
															// -mStatusBarView.Height()
	}

	void updateExpandedHeight() {
		if (mExpandedView != null) {
			mExpandedParams.height = getExpandedHeight();
			mExpandedDialog.getWindow().setAttributes(mExpandedParams);
		}
	}

	/**
	 * The LEDs are turned o)ff when the notification panel is shown, even just
	 * a little bit. This was added last-minute and is inconsistent with the way
	 * the rest of the notifications are handled, because the notification isn't
	 * really cancelled. The lights are just turned off. If any other
	 * notifications happen, the lights will turn back on. Steve says this is
	 * what he wants. (see bug 1131461)
	 */
	void visibilityChanged(boolean visible) {
		if (mPanelSlightlyVisible != visible) {
			mPanelSlightlyVisible = visible;
			try {
				mBarService.onPanelRevealed();
			} catch (RemoteException ex) {
				// Won't fail unless the world has ended.
			}
		}
	}

	void performDisableActions(int net) {
		int old = mDisabled;
		int diff = net ^ old;
		mDisabled = net;

		// act accordingly
		if ((diff & StatusBarManager.DISABLE_EXPAND) != 0) {
			if ((net & StatusBarManager.DISABLE_EXPAND) != 0) {
				Slog.d(TAG, "DISABLE_EXPAND: yes");
				animateCollapse();
			}
		}
		if ((diff & StatusBarManager.DISABLE_NOTIFICATION_ICONS) != 0) {
			if ((net & StatusBarManager.DISABLE_NOTIFICATION_ICONS) != 0) {
				Slog.d(TAG, "DISABLE_NOTIFICATION_ICONS: yes");
				if (mTicking) {
					mNotificationIcons.setVisibility(View.INVISIBLE);
					mTicker.halt();
				} else {
					setNotificationIconVisibility(false,
							com.android.internal.R.anim.fade_out);
				}
			} else {
				Slog.d(TAG, "DISABLE_NOTIFICATION_ICONS: no");
				if (!mExpandedVisible) {
					setNotificationIconVisibility(true,
							com.android.internal.R.anim.fade_in);
				}
			}
		} else if ((diff & StatusBarManager.DISABLE_NOTIFICATION_TICKER) != 0) {
			Slog.d(TAG,
					"DISABLE_NOTIFICATION_TICKER: "
							+ (((net & StatusBarManager.DISABLE_NOTIFICATION_TICKER) != 0) ? "yes"
									: "no"));
			if (mTicking
					&& (net & StatusBarManager.DISABLE_NOTIFICATION_TICKER) != 0) {
				mTicker.halt();
			}
		}
	}

	private View.OnClickListener mClearButtonListener = new View.OnClickListener() {
		public void onClick(View v) {
			try {
				mBarService.onClearAllNotifications();
			} catch (RemoteException ex) {
				// system process is dead if we're here.
			}
			animateCollapse();
		}
	};

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(action)
					|| Intent.ACTION_SCREEN_OFF.equals(action)) {
				animateCollapse();
			} else if (Intent.ACTION_CONFIGURATION_CHANGED.equals(action)) {
				updateResources();
			} else if (action.equals(Intent.ACTION_TIME_TICK))
				klok.updateView();

		}
	};

	private static void copyNotifications(
			ArrayList<Pair<IBinder, StatusBarNotification>> dest,
			NotificationData source) {
		int N = source.size();
		for (int i = 0; i < N; i++) {
			NotificationData.Entry entry = source.getEntryAt(i);
			dest.add(Pair.create(entry.key, entry.notification));
		}
	}

	/**
	 * Reload some of our resources when the configuration changes.
	 * 
	 * We don't reload everything when the configuration changes -- we probably
	 * should, but getting that smooth is tough. Someday we'll fix that. In the
	 * meantime, just update the things that we know change.
	 */
	void updateResources() {
		Resources res = getResources();
		mDisplay = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay();
		mOngoingTitle
				.setText(getText(R.string.status_bar_ongoing_events_title));
		mLatestTitle.setText(getText(R.string.status_bar_latest_events_title));
		mNoNotificationsTitle
				.setText(getText(R.string.status_bar_no_notifications_title));

		mEdgeBorder = res.getDimensionPixelSize(R.dimen.status_bar_edge_ignore);
	}

	//
	// tracing
	//

	void postStartTracing() {
		mHandler.postDelayed(mStartTracing, 3000);
	}

	void vibrate() {
		android.os.Vibrator vib = (android.os.Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vib.vibrate(250);
	}

	Runnable mStartTracing = new Runnable() {
		public void run() {
			vibrate();
			SystemClock.sleep(250);
			Slog.d(TAG, "startTracing");
			android.os.Debug.startMethodTracing("/data/statusbar-traces/trace");
			mHandler.postDelayed(mStopTracing, 10000);
		}
	};

	Runnable mStopTracing = new Runnable() {
		public void run() {
			android.os.Debug.stopMethodTracing();
			Slog.d(TAG, "stopTracing");
			vibrate();
		}
	};

	void setToggleState() {
		NOTIF.setVisibility(isNotif ? View.VISIBLE : View.GONE);
		TOGEL.setVisibility(isNotif ? View.GONE : View.VISIBLE);

		Drawable on = curTema.getICON(Tema.TAB_AKTIF);
		Drawable off = curTema.getICON(Tema.TAB_NONAKTIF);
		tab_tog.setBackgroundDrawable(isNotif ? off : on);
		tab_notif.setBackgroundDrawable(isNotif ? on : off);

	}

	private OnClickListener ontabclick = new OnClickListener() {

		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.tab_togel:
				isNotif = false;
				TOGEL.slideAnimate();
				setToggleState();
				break;

			case R.id.tab_notifikasi:
				isNotif = true;
				setToggleState();
				break;
			}

		}
	};

	public void setBattLevel(Intent intent) {
		sb_batt.UpdateBaterai(intent);

	}

	// /XXX SINYAL
	public void setSinyalIO(String type, int pos) {

		Drawable d = null;
		if (pos == -666)
			d = null;
		else {
			if (type.equals(StatusBarPolicy.TYPE[0]))
				d = mTema.getICON(SBK.DATA_E[pos]);
			else if (type.equals(StatusBarPolicy.TYPE[1]))
				d = mTema.getICON(SBK.DATA_G[pos]);
			else if (type.equals(StatusBarPolicy.TYPE[2]))
				d = mTema.getICON(SBK.DATA_H[pos]);
			else if (type.equals(StatusBarPolicy.TYPE[3]))
				d = mTema.getICON(SBK.DATA_3G[pos]);
		}

		sb_sinyal.setDataIO(d);

	}

	public void showblutut(boolean boo) {
		sb_sinyal.setBT(boo);
	}

	public void setAlarm(boolean alarmSet) {
		sb_sinyal.setAlarmIcon(alarmSet);

	}

	public void setRingIcon(int i) {
		switch (i) {
		case 0:
			sb_sinyal.setRingerModeIcon(mTema.getICON(SBK.STATUSBAR_SILENT));
			break;
		case 1:
			sb_sinyal.setRingerModeIcon(mTema.getICON(SBK.STATUSBAR_VIBRATE));
			break;
		default:
			sb_sinyal.setRingerModeIcon(null);
			break;
		}

	}

	public void setSinyalStrenght(int iconLevel) {
		if (iconLevel == -1) {
			sb_sinyal.setSinyalStreng(mTema.getICON(SBK.SIGNAL_FLIGHT));
			sb_sinyal.setDataIO(null);
		} else
			sb_sinyal.setSinyalStreng(mTema.getICON(SBK.SINYAL_SIG[iconLevel]));

	}

	// XXX Traffic
	public void setTraffic(boolean bool) {
		sb_trafik.TrafikOnOFF(bool);
	}

	public void setJam() {
		sb_jam.UpdateJAM();
	}

	public void updateStatusBarLayout() {
		curTema = new Tema(mContext);
		pref = new getPref(mContext, Model.PREF_JUDUL);
		sbPref = new getPref(mContext, SBK.STATBAR_PREF);
		sTema = new Tema(mContext);

		sb_jam.UpdateJAM();
		sb_batt.UpdateBatraiView();
		sb_karir.arrangeLayout();
		sb_sinyal.aturulangLayout();
		sb_trafik.modifInterface();

		updateroundedCorner();

		arrangelayout();

	}

	private void updateroundedCorner() {

		boolean round = pref.getBoolean(Sett.RoundedCorner, false);
		roundedView.setVisibility(round ? View.VISIBLE : View.INVISIBLE);
		WindowManagerImpl.getDefault().updateViewLayout(roundedView, wmanrounded);

	}

	/**
	 * sebenernya banyak kemungkinan buat ngeleot statusbar ni matematikanya
	 * kira gmana ya?? kita punya view 7 biji ...di kira2in aj itu silahkan di
	 * tambhain kemungkinan lain n tambahin d libicon cek
	 * 
	 * @see systemui.libsicon.configfragment.StatusbarConfig.java
	 */
	public void arrangelayout() {

		pref = new getPref(mContext, Model.PREF_JUDUL);
		int mode = pref.getInt(Sett.StatusbarLayout, 0);
		boolean istop = pref.getBoolean(Sett.BottomBatteryLine, false);
		android.util.Log.e("Layout", "On Layout Update" + mode);
		View vbat = sb_batt;
		View vjam = sb_jam;
		View vsinyal = sb_sinyal;
		View vtraf = sb_trafik;
		View vkarir = sb_karir;
		View vnotif = mNotificationIcons;
		View vbar = sb_batbar;

		LayoutParams bar = new LayoutParams(LayoutParams.MATCH_PARENT,
				pref.getInt(SBK.BAT_LINE_HEIGHT, 1));
		if (istop)
			bar.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		else
			bar.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

		vbar.setLayoutParams(bar);

		LayoutParams kanan = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.MATCH_PARENT);
		kanan.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

		LayoutParams kiri = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.MATCH_PARENT);
		kiri.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

		LayoutParams tengah = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.MATCH_PARENT);
		tengah.addRule(RelativeLayout.CENTER_IN_PARENT);

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

			sinyal.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			sinyal.setMargins(0, 0, vbat.getWidth(), 0);
			vsinyal.setLayoutParams(sinyal);

			trafik.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			trafik.setMargins(0, 0, vbat.getWidth() + vsinyal.getWidth(), 0);
			vtraf.setLayoutParams(trafik);

			notif.addRule(RelativeLayout.RIGHT_OF, vkarir.getId());
			vnotif.setLayoutParams(notif);

			break;

		case 1:
			showall();
			vjam.setLayoutParams(kanan);
			vkarir.setLayoutParams(kiri);

			batt.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			batt.setMargins(0, 0, vjam.getWidth(), 0);
			vbat.setLayoutParams(batt);

			sinyal.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			sinyal.setMargins(0, 0, vbat.getWidth() + vjam.getWidth(), 0);

			vsinyal.setLayoutParams(sinyal);

			trafik.addRule(RelativeLayout.LEFT_OF, vsinyal.getId());
			vtraf.setLayoutParams(trafik);

			notif.addRule(RelativeLayout.RIGHT_OF, vkarir.getId());
			vnotif.setLayoutParams(notif);

			break;
		case 2:
			Jam.showme(false);
			vjam.setLayoutParams(tengah);
			vbat.setLayoutParams(kanan);
			vkarir.setLayoutParams(kiri);

			sinyal.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			sinyal.setMargins(0, 0, vbat.getWidth(), 0);
			vsinyal.setLayoutParams(sinyal);

			trafik.addRule(RelativeLayout.LEFT_OF, vsinyal.getId());
			vtraf.setLayoutParams(trafik);

			notif.addRule(RelativeLayout.RIGHT_OF, vkarir.getId());
			vnotif.setLayoutParams(notif);

			break;
		case 3:
			showall();
			vjam.setLayoutParams(tengah);
			vbat.setLayoutParams(kiri);

			vsinyal.setLayoutParams(kanan);

			karir.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			karir.setMargins(0, 0, vsinyal.getWidth(), 0);
			vkarir.setLayoutParams(karir);

			trafik.addRule(RelativeLayout.LEFT_OF, vkarir.getId());
			vtraf.setLayoutParams(trafik);

			notif.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			notif.setMargins(vbat.getWidth(), 0, 0, 0);
			vnotif.setLayoutParams(notif);

			break;
		}

	}

	private void showall() {
		Jam.showme(true);

	}

	// public void UpdateJam(Intent intent) {
	// sb_jam.UpdateJamView(intent);
	//
	// }
	//

	//
	// public void updateSinyal_OndataActivity(int direction) {
	// sb_sinyal.onDataActivity(direction);
	//
	// }
	//
	// public void updateSinyal_OnServiceStateChange(ServiceState state) {
	// sb_sinyal.onServiceStateChanged(state);
	//
	// }
	//
	// public void updateSinyal_OnDataConnStateChange(int state, int
	// networkType) {
	// sb_sinyal.onDataConnectionStateChanged(state, networkType);
	//
	// }

}
