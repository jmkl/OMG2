package com.android.systemui.statusbar;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Slog;
import android.view.MotionEvent;
import android.widget.Chronometer;
import android.widget.FrameLayout;

public class CallOnGoingView extends FrameLayout {
	StatusBarService mService;
	private Chronometer mTimer;
	

	public CallOnGoingView(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
	}

	protected void dispatchDraw(Canvas paramCanvas) {
		super.dispatchDraw(paramCanvas);
	}

	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		mTimer.start();
		Slog.i("CallOnGoingView", "onAttachedToWindow");
	}

	protected void onConfigurationChanged(Configuration paramConfiguration) {
		super.onConfigurationChanged(paramConfiguration);
	}

	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		mTimer.stop();
		Slog.i("CallOnGoingView", "onDetachedFromWindow");
	}

	protected void onFinishInflate() {
		super.onFinishInflate();
		this.mTimer = ((Chronometer) findViewById(2131296283));
	}

	public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
		if (mService.interceptTouchEvent(paramMotionEvent))
			return true;
		return super.onInterceptTouchEvent(paramMotionEvent);
	}

	protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2,
			int paramInt3, int paramInt4) {
		super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
	}

	protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3,
			int paramInt4) {
		super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
	}

	public boolean onTouchEvent(MotionEvent paramMotionEvent) {
		if (paramMotionEvent.getAction() != MotionEvent.ACTION_DOWN)
			mService.interceptTouchEvent(paramMotionEvent);
		return true;
	}
}
