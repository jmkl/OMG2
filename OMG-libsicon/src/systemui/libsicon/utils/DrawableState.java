package systemui.libsicon.utils;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.widget.CheckBox;

public class DrawableState extends StateListDrawable {

	private int FOCUSED = android.R.attr.state_focused;
	private int PRESSED = android.R.attr.state_pressed;

	public DrawableState(Drawable normal, Drawable press, Drawable focus) {
		if(focus!=null)
			addState(new int[] { FOCUSED }, focus);
		if(press!=null)
			addState(new int[] { PRESSED }, press);
		if(normal!=null)
			addState(new int[] {}, normal);
	}

	
}
