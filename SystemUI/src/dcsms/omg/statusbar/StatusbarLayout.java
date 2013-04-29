package dcsms.omg.statusbar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import dcsms.omg.util.SBK;
import dcsms.omg.util.Tema;

public class StatusbarLayout extends RelativeLayout {
	private Context cntx;

	public StatusbarLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		cntx = context;
		Inisiasi();
		

	}
	private void Inisiasi() {
		setPadding(5, 0, 5, 0);
		Drawable d = new Tema(cntx).getICON(SBK.STATUSBAR_BG);
		setBackgroundDrawable(d);	
		
		
	}

	
}
