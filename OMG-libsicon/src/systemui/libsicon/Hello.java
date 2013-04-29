package systemui.libsicon;

import systemui.libsicon.configfragment.StatusbarConfig;
import systemui.libsicon.utils.SherlockBar;
import systemui.libsicon.utils.SherlockBar.HomeListener;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;

import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

public class Hello extends SlidingFragmentActivity implements OnClickListener {
	private int width;
	private SherlockBar sb;
	SlidingMenu menu ;

	/**
	 * FIXME ga tau nih,,,kalo ga d override... kadang2 fragment jadi null lagi
	 * pass onresume
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	public SherlockBar getSherlockBar() {
		return sb;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.Main);
		super.onCreate(savedInstanceState);
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		width = dm.widthPixels;
		sb = new SherlockBar(this);
		sb.setup();
		
		sb.setHomeListener(new HomeListener() {
			
			@Override
			public void onHomeClick() {
				Log.d(getLocalClassName(), "OnHomeClick");
				Intent i = new Intent("dcsms.omg.UPDATELAYOUT");
				sendBroadcast(i);
				
				
			}
		});

		setContentView(R.layout.main);
		setBehindContentView(R.layout.menu);

		menu = getSlidingMenu();
		menu.setMode(SlidingMenu.LEFT);

		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setShadowWidth(10);
		menu.setShadowDrawable(R.drawable.defaultshadow);
		menu.setBehindOffset(width / 4);
		menu.setFadeDegree(0.35f);

		menu.setSelectorEnabled(true);
		menu.setSelectorDrawable(R.drawable.ic_logo);
		menu.setSelectedView(getSlidingMenu().getChildAt(0));
		menu.findViewById(R.id.button1).setOnClickListener(this);
		menu.findViewById(R.id.button2).setOnClickListener(this);
		menu.findViewById(R.id.button3).setOnClickListener(this);
		menu.findViewById(R.id.button4).setOnClickListener(this);
		menu.findViewById(R.id.button5).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/* power widget */
		case R.id.button1:
			break;
		/* status bar */
		case R.id.button2:
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.konten_layout, new StatusbarConfig())
					.commit();
			break;
		/* color */
		case R.id.button3:
			break;
		/* misc*/
		case R.id.button4:
			break;
		/* tutorial */
		case R.id.button5:
			break;

		}

	}

}
