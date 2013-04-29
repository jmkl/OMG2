package dcsms.omg.statusbar;

import dcsms.omg.util.Tema;
import dcsms.omg.util.getPref;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class SBIcon extends LinearLayout {
	protected Context mContext;
	protected LayoutParams globalparams;
	protected Tema mTema;
	protected getPref pref, sbPref;

	public SBIcon(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		Inisiasi();
	}
	protected void Inisiasi() {
		globalparams = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.MATCH_PARENT);
	}

}
