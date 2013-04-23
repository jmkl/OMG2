/*
 * Copyright (C) 2008 The Android Open Source Project
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

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

import com.android.systemui.R;

public class StatusBarView extends FrameLayout {
	private static final String TAG = "StatusBarView";

	static final int DIM_ANIM_TIME = 400;

	StatusBarService mService;
	boolean mTracking;
	int mStartX, mStartY;
	ViewGroup mNotificationIcons;
	ViewGroup mStatusIcons;

	boolean mHasSoftButtons = true;

	public StatusBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public int Height() {
		return getHeight();
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mNotificationIcons = (ViewGroup) findViewById(R.id.notificationIcons);
		mStatusIcons = (ViewGroup) findViewById(R.id.statusIcons);
		

	}


	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		mService.onBarViewDetached();
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		mService.onBarViewAttached();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mService.updateExpandedViewPos(StatusBarService.EXPANDED_LEAVE_ALONE);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);

		// put the date date view quantized to the icons

	}

	/**
	 * Gets the left position of v in this view. Throws if v is not a child of
	 * this.
	 */
	private int getViewOffset(View v) {
		int offset = 0;
		while (v != this) {
			offset += v.getLeft();
			ViewParent p = v.getParent();
			if (v instanceof View) {
				v = (View) p;
			} else {
				throw new RuntimeException(v + " is not a child of " + this);
			}
		}
		return offset;
	}

	/**
	 * Ensure that, if there is no target under us to receive the touch, that we
	 * process it ourself. This makes sure that onInterceptTouchEvent() is
	 * always called for the entire gesture.
	 */

	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() != MotionEvent.ACTION_DOWN) {
            mService.interceptTouchEvent(event);
        }
        return true;
	}

	

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		return mService.interceptTouchEvent(event);
	}

	
	public int getSoftButtonsWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	
}
