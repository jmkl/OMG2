package dcsms.omg.statusbar;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import dcsms.omg.util.Model;
import dcsms.omg.util.SBK;
import dcsms.omg.util.Sett;
import dcsms.omg.util.Tema;
import dcsms.omg.util.getPref;

public class Karir extends SBIcon {
	private ImageView logo;
	boolean visible;


	public Karir(Context context, AttributeSet att) {
		super(context, att);
	}


	public void UpdateKarirView(Intent intent) {
		String action = intent.getAction();
		if(action.equals(Model.UPDATE_STATUSBAR)){
			arrangeLayout();
		}
	}

	@Override
	protected void Inisiasi() {
		super.Inisiasi();
		if (logo != null)
			removeView(logo);

		logo = new ImageView(mContext);
		logo.setAdjustViewBounds(true);
		logo.setLayoutParams(globalparams);
		addView(logo);
		

	}
	public void setReferensi(Tema mTema, getPref sbPref, getPref pref) {
		this.mTema =mTema;
		this.sbPref=sbPref;
		this.pref=pref;
		arrangeLayout();
		
	}
	public void arrangeLayout() {		
		logo.setImageDrawable(mTema.getICON(SBK.CARRIER));		
		visible = pref.getBoolean(Sett.Carrier, true);
		logo.setVisibility(visible ? View.VISIBLE : View.GONE);
	}

}