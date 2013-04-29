package dcsms.omg.notification;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import dcsms.omg.util.Tema;

public class NotificationLayout extends LinearLayout {
	private Context cntx;
	private Tema tema;

	public NotificationLayout(Context context, AttributeSet atts) {
		super(context,atts);
		cntx = context;
		init();
	}

	public NotificationLayout(Context context) {
		super(context);
		cntx = context;
		init();
	}

	private void init() {
		 int count = getChildCount();
		 if (count > 0)
		 doshit();

	}



	public void doshit() {
		tema = new Tema(cntx);
		try {

			int count = getChildCount();
			OLog(Integer.toString(count));
			for (int i = 0; i < count; i++) {
				OLog("get lates item parent");
				LinearLayout v2 = (LinearLayout) getChildAt(i);// get
																// lates
																// item
																// parent
				if (v2 != null) {
					OLog("get latest item view");
					ViewGroup liView = (ViewGroup) v2.getChildAt(0);// get
																		// latesitemview
					if (liView != null) {
						OLog("ViewGRUP not null");
						liView.setLayoutParams(new LayoutParams(
								LayoutParams.MATCH_PARENT,
								LayoutParams.MATCH_PARENT));
						Drawable d = tema.getICON(Tema.NOTIF_KONTEN_BG);
						liView.setBackgroundDrawable(d);
						for (int j = 0; j < liView.getChildCount(); j++) {
							try {
								LinearLayout parent = (LinearLayout) liView
										.getChildAt(0);
								if (parent != null) {
									View icon = parent.getChildAt(0);
									LinearLayout konten = (LinearLayout) parent
											.getChildAt(1);

									if (icon != null) {
										Drawable d2 = tema
												.getICON(Tema.NOTIF_IKON_BG);
										icon.setBackgroundDrawable(d2);

									}

									if (konten != null) {
										OLog("On get Notification detail");
										TextView event_tit = (TextView) konten
												.getChildAt(0);
										if (event_tit != null)
											event_tit
													.setTextColor(tema
															.getWarna(Tema.WARNA_NOTIF_KONTEN_TITLE));// ok
																											// pulo

										LinearLayout des = (LinearLayout) konten
												.getChildAt(1);
										if (des != null) {
											TextView desc = (TextView) des
													.getChildAt(0);
											View date = (View) des
													.getChildAt(1);

											if (desc != null)
												desc.setTextColor(tema
														.getWarna(Tema.WARNA_NOTIF_KONTEN_DESC));

											if (date != null)
												((TextView) date)
														.setTextColor(tema
																.getWarna(Tema.WARNA_NOTIF_KONTEN_DATE));
										}
									}
								}

							} catch (NullPointerException e) {
								e.printStackTrace();
								OLog(e.toString());
							}

						}
					}
				}

			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (ClassCastException e) {
			e.printStackTrace();
		}

	}

	private void OLog(String msg) {
		Log.e("TAG", msg);
	}
}
