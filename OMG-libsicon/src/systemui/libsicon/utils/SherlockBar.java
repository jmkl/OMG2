package systemui.libsicon.utils;

import systemui.libsicon.R;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.slidingmenu.lib.app.SlidingFragmentActivity;

public class SherlockBar implements OnClickListener {
	public interface HomeListener {
		void onHomeClick();
	}

	public interface AboutListener {
		void onAboutClick();
	}

	private HomeListener homeListener;
	private AboutListener abtListener;

	public void setHomeListener(HomeListener listener) {
		this.homeListener = listener;
	}

	public void setAboutListener(AboutListener listener) {
		this.abtListener = listener;
	}

	private SlidingFragmentActivity c;

	public SherlockBar(SlidingFragmentActivity c) {
		this.c = c;
	}

	public void setup() {
		View v = LayoutInflater.from(c).inflate(R.layout.abar, null);
		c.getSupportActionBar().setCustomView(v);
		c.getSupportActionBar().setDisplayShowCustomEnabled(true);
		v.findViewById(R.id.bar_home).setOnClickListener(this);
		v.findViewById(R.id.bar_about).setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bar_home:
			if (homeListener != null)
				homeListener.onHomeClick();

			break;

		case R.id.bar_about:
			if (abtListener != null)
				abtListener.onAboutClick();
			break;
		}

	}

	private void ShowToast(String text) {
		Toast t = new Toast(c);
		LinearLayout l = new LinearLayout(c);
		l.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		TextView tv = new TextView(c);
		l.addView(tv);
		tv.setText(text);
		t.setView(l);
		t.setGravity(Gravity.BOTTOM, 0, 0);
		t.show();

	}
}
