package dcsms.omg.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;

public class Tema {
	private PackageManager pm;
	public final static String TAB_AKTIF = "tab_aktif";
	public final static String TAB_NONAKTIF = "tab_nonaktif";
	public final static String IKON_NONE = "dcsms_apps_none";
	public final static String IKON_TO_OFF = "screentimeout_off";
	public final static String IKON_TO_ON = "screentimeout_on";
	public final static String IKON_BT_OFF = "bluetooth_off";
	public final static String IKON_BT_ON = "bluetooth_on";
	public final static String IKON_SENTER = "senter";
	public final static String IKON_AIRPLANE_OFF = "flight_mode_off";
	public final static String IKON_AIRPLANE_ON = "flight_mode_on";
	public final static String IKON_GPS_OFF = "gps_off";
	public final static String IKON_GPS_ON = "gps_on";
	public final static String IKON_MUTE_OFF = "mute";
	public final static String IKON_DATA_ON = "mobile_data_on";
	public final static String IKON_DATA_OFF = "mobile_data_off";
	public final static String IKON_USB_TETHER_ON = "usb_tethering_on";
	public final static String IKON_USB_TETHER_OFF = "usb_tethering_off";
	public final static String IKON_SS = "screenshot";
	public final static String IKON_FCUKDOWN = "shutdown_tombol_reboot";
	
	public final static String IKON_VIBRATE_OFF = "vibrate";
	public final static String IKON_VIBRATE_ON = "sound_vibrate";
	
	public final static String IKON_WIFI_OFF = "wifi_off";
	public final static String IKON_WIFI_ON = "wifi_on";
	public final static String IKON_WIFI_HS_OFF = "wifi_hotspot_off";
	public final static String IKON_WIFI_HS_ON = "wifi_hotspot_on";
	public final static String IKON_ROTATE_OFF = "rotate_off";
	public final static String IKON_ROTATE_ON = "rotate_on";
	public final static String IKON_MUSIK_PLAY = "musik_play";
	public final static String IKON_MUSIK_PAUSE = "musik_pause";
	public final static String IKON_MUSIK_NEXT = "musik_next";
	public final static String IKON_MUSIK_PREV = "musik_prev";
	public final static String IKON_FLASHLIGHT = "senter";

	public final static String TOGEL_BG = "tracking_view_bg";
	public final static String SHADOW = "icon_shadow";
	public final static String SEEKBAR_THUMBNAIL = "seekbar_thumbnail";
	public final static String SEEKBAR_ATAS = "seekbar_bagian_atas";
	public final static String SEEKBAR_BAWAH = "seekbar_bagian_bawah";
	public final static String STATUSBAR_SHOW = "expand";
	public final static String STATUSBAR_HIDE = "collapse";
	public final static String SHUTDOWN_OFF = "shutdown_tombol_off";
	public final static String SHUTDOWN_REBOOT = "shutdown_tombol_reboot";
	public final static String SHUTDOWN_RECOVERY = "shutdown_tombol_recovery";
	public final static String SHUTDOWN_BG = "shutdown_bg";
	public final static String CLOSEDRAGHAND = "drag_handler";
	public final static String TREK_BG = "tracking_view_bg";
	// dateview
	public final static String DATEVIEW_BG = "dateview_bg";
	public final static String TOGEL_STATE_BG = "togel_state";
	// notif bg
	public final static String NOTIF_LATEST_BG = "latest_item_bg";
	public final static String NOTIF_KONTEN_BG = "notif_konten_bg";
	public final static String NOTIF_IKON_BG = "notif_ikon_bg";
	public final static String CLEAR_BTN = "clear_button";
	public final static String SETTINGS_BTN = "settings_button";

	// Warna
	public final static String WARNA_TOGEL = "dcsms_togel_teks";
	public final static String WARNA_SHADOW_TOGEL = "dcsms_togel_teks_shadow";
	public final static String WARNA_TAB = "dcsms_tab_teks";
	public final static String WARNA_SHADOW_TAB = "dcsms_tab_teks_shadow";
	public final static String WARNA_DATEVIEW = "dcsms_dateview_color";
	public final static String WARNA_NOTIF_TITLE = "dcsms_notif_title_color";
	public final static String WARNA_NOTIF_KONTEN_TITLE = "dcsms_notif_konten_title";
	public final static String WARNA_NOTIF_KONTEN_DESC = "dcsms_notif_konten_desc";
	public final static String WARNA_NOTIF_KONTEN_DATE = "dcsms_notif_konten_date";

	public final static int STATUS_ON = 0;
	public final static int STATUS_OFF = 1;
	public final static int STATUS_RANDOM = 2;
	public final static int IC_WIFI = 0, IC_ROTASI = 1, IC_DATA = 2,
			IC_SKRIN_TO = 3, IC_BLUTUT = 4, IC_GPS = 5;

	public final static int IC_DIAM = 0, IC_VIBRATE = 1, IC_VIBRASOUND = 2,
			IC_WIFI_TETER = 3, IC_SS = 4, IC_REBOOT = 5;

	private Context ctx;
	private getPref pref;
	public final static boolean inStatusbarOnly = false;

	private String themePack;

	public Tema(Context context) {
		ctx = context;
		pm = context.getPackageManager();
		pref = new getPref(context, Model.PREF_JUDUL);
		if (pref != null)
			themePack = pref.getNamaPaketTema();

		if (themePack.equals("dcsms"))
			themePack = Model.OMG;
	}

	private Context getFiledirektori() {
		Context cont = null;
		try {
			cont = ctx.createPackageContext(Model.OMG, 0);
		} catch (NameNotFoundException e) {
		}
		return cont;
	}

	public Drawable drawableDariAssets(String nama) {
		Drawable d = null;
		try {
			InputStream ims = ctx.getAssets().open(nama);
			d = Drawable.createFromStream(ims, null);
		} catch (IOException ex) {

		}
		return d;
	}

	public Typeface getFontfromTheme(String nama) {
		Typeface d = getFont(nama);
		if (d == null) {
			Context c = getKontek(Model.OMG);
			d = Typeface.createFromAsset(c.getAssets(), nama);
		}
		return d;
	}

	public Typeface getFont(String nama) {
		Typeface d = null;
		Context c = getKontek(themePack);
		try {
			d = Typeface.createFromAsset(c.getAssets(), nama);
		} catch (Exception e) {
			d = null;
			XLog.s("","ndak basobok font e doh..pakai se font default lai");
		}

		return d;
	}

	public String[] getArray(String nama) {
		Resources themeResources = null;
		String[] d = getArrayWBg_Theme(nama);
		if (d == null) {
			themePack = Model.OMG;
			try {
				themeResources = pm.getResourcesForApplication(themePack);
			} catch (NameNotFoundException e) {
			} catch (NullPointerException e) {
			}

			if (themeResources != null) {
				int resource_id = themeResources.getIdentifier(nama, "array",
						themePack);
				if (resource_id != 0) {
					try {
						d = themeResources.getStringArray(resource_id);
					} catch (Resources.NotFoundException e) {
						d = null;
					}
				}
			}
		}
		return d;
	}

	public String[] getArrayWBg_Theme(String nama) {
		Resources themeResources = null;
		String[] d = null;
		try {
			themeResources = pm.getResourcesForApplication(themePack);
		} catch (NameNotFoundException e) {
		} catch (NullPointerException e) {
		}

		if (themeResources != null) {
			int resource_id = themeResources.getIdentifier(nama, "array",
					themePack);
			if (resource_id != 0) {
				try {
					d = themeResources.getStringArray(resource_id);
				} catch (Resources.NotFoundException e) {
					d = null;
				}
			}
		}
		return d;
	}

	public AnimKontek getAnim(String anim) {
		Resources themeResources = null;
		AnimKontek akon = new AnimKontek();
		akon = getAnimDef(anim);
		int d = akon.intejer;
		if (d == 0) {
			String themePack = Model.OMG;
			try {
				themeResources = pm.getResourcesForApplication(themePack);
			} catch (NameNotFoundException e) {
			} catch (NullPointerException e) {
			}

			if (themeResources != null) {
				akon.intejer = themeResources.getIdentifier(anim, "anim",
						themePack);
				akon.kontek = getKontek(themePack);
			}
		}

		return akon;
	}

	public AnimKontek getAnimDef(String anim) {
		Resources themeResources = null;
		AnimKontek akon = new AnimKontek();
		akon.intejer = 0;
		akon.kontek = getKontek(themePack);
		try {
			themeResources = pm.getResourcesForApplication(themePack);
		} catch (NameNotFoundException e) {
		} catch (NullPointerException e) {
		}

		if (themeResources != null) {
			try {
				akon.intejer = themeResources.getIdentifier(anim, "anim",
						themePack);

			} catch (Exception e) {
				akon.intejer = 666;
			}

		}

		return akon;
	}
	public Drawable getDefaultXMLORDRAWABLE(String NAMOICON) {
		Resources themeResources = null;
		Drawable d =null;

			String themePack = Model.OMG;
			try {
				themeResources = pm.getResourcesForApplication(themePack);
			} catch (NameNotFoundException e) {
			} catch (NullPointerException e) {
			}

			if (themeResources != null) {
				int resource_id = themeResources.getIdentifier(NAMOICON,
						"drawable", themePack);
				if (resource_id != 0) {
					try {
						d = themeResources.getDrawable(resource_id);
					} catch (Resources.NotFoundException e) {
					}
				}
			}
		
		return d;
	}
	
	public Bitmap getIconBitmap(String namaicon){
		Drawable d  =getICON(namaicon);
		return ((BitmapDrawable)d).getBitmap();
	}

	public Drawable getICON(String NAMOICON) {
		Resources themeResources = null;
		Drawable d = getDrawabledariSesuatu(NAMOICON);
		if (d == null) {
			String themePack = Model.OMG;
			try {
				themeResources = pm.getResourcesForApplication(themePack);
			} catch (NameNotFoundException e) {
			} catch (NullPointerException e) {
			}

			if (themeResources != null) {
				int resource_id = themeResources.getIdentifier(NAMOICON,
						"drawable", themePack);
				if (resource_id != 0) {
					try {
						d = themeResources.getDrawable(resource_id);
					} catch (Resources.NotFoundException e) {
					}
				}
			}
		}
		return d;
	}

	public Drawable getDrawabledariSesuatu(String NAMOICON) {
		Resources themeResources = null;
		Drawable d = null;
		try {
			themeResources = pm.getResourcesForApplication(themePack);
		} catch (NameNotFoundException e) {
		} catch (NullPointerException e) {
		}

		if (themeResources != null) {
			int resource_id = themeResources.getIdentifier(NAMOICON,
					"drawable", themePack);
			if (resource_id != 0) {
				try {
					d = themeResources.getDrawable(resource_id);
				} catch (Resources.NotFoundException e) {
					d = null;
				}
			}
		}

		return d;
	}

	public Drawable getDrawablePreview(String themePack) {
		Resources themeResources = null;
		Drawable d = getDrawablePreviewFromTheme(themePack);
		if (d == null) {
			themePack = Model.OMG;
			try {
				themeResources = pm.getResourcesForApplication(themePack);
			} catch (NameNotFoundException e) {
			} catch (NullPointerException e) {
			}

			if (themeResources != null) {
				int resource_id = themeResources.getIdentifier("preview",
						"drawable", themePack);
				if (resource_id != 0) {
					try {
						d = themeResources.getDrawable(resource_id);
					} catch (Resources.NotFoundException e) {
					}
				}
			}
		}

		return d;
	}

	public Drawable getDrawablePreviewFromTheme(String themePack) {
		Resources themeResources = null;
		Drawable d = null;
		try {
			themeResources = pm.getResourcesForApplication(themePack);
		} catch (NameNotFoundException e) {
		} catch (NullPointerException e) {
		}

		if (themeResources != null) {
			int resource_id = themeResources.getIdentifier("preview",
					"drawable", themePack);
			if (resource_id != 0) {
				try {
					d = themeResources.getDrawable(resource_id);
				} catch (Resources.NotFoundException e) {
				}
			}
		}

		return d;
	}


	public Bitmap getICONDefault(String NAMOICON) {
		Resources themeResources = null;
		Bitmap d = null;
		try {
			themeResources = pm.getResourcesForApplication(Model.OMG);
		} catch (NameNotFoundException e) {
			
		} catch (NullPointerException e) {
		}

		if (themeResources != null) {
			int resource_id = themeResources.getIdentifier(NAMOICON,
					"drawable", Model.OMG);
			if (resource_id != 0) {
				try {

					d = ((BitmapDrawable) themeResources
							.getDrawable(resource_id)).getBitmap();
				} catch (Resources.NotFoundException e) {
				}
			}
		}

		return d;
	}


	public String getString(String namaString, String themePack) {
		String appname = null;
		Resources res = null;
		try {
			res = pm.getResourcesForApplication(themePack);
		} catch (NameNotFoundException e) {
		} catch (NullPointerException e) {
		}

		if (res != null) {
			int resource_id = res
					.getIdentifier(namaString, "string", themePack);
			if (resource_id != 0) {
				try {
					appname = res.getString(resource_id);
				} catch (Resources.NotFoundException e) {
				}
			}
		}
		return appname;
	}

	public String getStringfromTheme(String namaString) {
		String appname = null;
		Resources res = null;
		try {
			res = pm.getResourcesForApplication(themePack);
		} catch (NameNotFoundException e) {
		} catch (NullPointerException e) {
		}

		if (res != null) {
			int resource_id = res
					.getIdentifier(namaString, "string", themePack);
			if (resource_id != 0) {
				try {
					appname = res.getString(resource_id);
				} catch (Resources.NotFoundException e) {
					appname = null;
				}
			}
		}

		if (appname == null) {
			try {
				res = pm.getResourcesForApplication(Model.OMG);
			} catch (NameNotFoundException e) {
			} catch (NullPointerException e) {
			}

			if (res != null) {
				int resource_id = res.getIdentifier(namaString, "string",
						Model.OMG);
				if (resource_id != 0) {
					try {
						appname = res.getString(resource_id);
					} catch (Resources.NotFoundException e) {
					}
				}
			}
		}
		return appname;

	}

	public int getDefaultDimen(String dimen_key) {
		Resources themeResources = null;
		int d = getDimenDariTema(dimen_key);
		if (d == -1) {
			String themePack = Model.OMG;
			try {
				themeResources = pm.getResourcesForApplication(themePack);
			} catch (NameNotFoundException e) {
			} catch (NullPointerException e) {
			}

			if (themeResources != null) {
				int resource_id = themeResources.getIdentifier(dimen_key,
						"dimen", themePack);
				if (resource_id != 0) {
					try {
						d = themeResources.getDimensionPixelSize(resource_id);
					} catch (Resources.NotFoundException e) {
					}
				}
			}
		}
		return d;
	}

	private int getDimenDariTema(String dimen_key) {
		Resources themeResources = null;
		int d = -1;
		try {
			themeResources = pm.getResourcesForApplication(themePack);
		} catch (NameNotFoundException e) {
		} catch (NullPointerException e) {
		}

		if (themeResources != null) {
			int resource_id = themeResources.getIdentifier(dimen_key, "dimen",
					themePack);
			if (resource_id != 0) {
				try {
					d = themeResources.getDimensionPixelSize(resource_id);
				} catch (Resources.NotFoundException e) {
					d = -1;
				}
			}
		}

		return d;
	}

	public int getWarnaTogel(String namaString) {
		int warna = 0xffffffff;
		Resources res = null;
		try {
			res = pm.getResourcesForApplication(themePack);
		} catch (NameNotFoundException e) {
		} catch (NullPointerException e) {
		}

		if (res != null) {
			int resource_id = res.getIdentifier(namaString, "color", themePack);
			if (resource_id != 0) {
				try {
					warna = res.getInteger(resource_id);
				} catch (Resources.NotFoundException e) {
				}
			}
		}
		return warna;

	}
	public NinePatchDrawable getNineBMPDefault(String namaFile) {
		NinePatchDrawable d = null;
		Resources res = null;
		if (d == null) {
			String themePack = Model.OMG;
			try {

				res = pm.getResourcesForApplication(themePack);
			} catch (NameNotFoundException e) {
			} catch (NullPointerException e) {
			}

			if (res != null) {
				int resource_id = res.getIdentifier(namaFile, "drawable",
						themePack);
				if (resource_id != 0) {
					try {
						d = (NinePatchDrawable) res.getDrawable(resource_id);
					} catch (Resources.NotFoundException e) {
					} catch (NullPointerException e) {
					}
				}
			}
		}
		return d;
	}

	public NinePatchDrawable getNineBMP(String namaFile) {
		NinePatchDrawable d = getNineBMP_fromTheme(namaFile);
		Resources res = null;
		if (d == null) {
			String themePack = Model.OMG;
			try {

				res = pm.getResourcesForApplication(themePack);
			} catch (NameNotFoundException e) {
			} catch (NullPointerException e) {
			}

			if (res != null) {
				int resource_id = res.getIdentifier(namaFile, "drawable",
						themePack);
				if (resource_id != 0) {
					try {
						d = (NinePatchDrawable) res.getDrawable(resource_id);
					} catch (Resources.NotFoundException e) {
					} catch (NullPointerException e) {
					}
				}
			}
		}
		return d;
	}

	public NinePatchDrawable getNineBMP_fromTheme(String namaFile) {
		NinePatchDrawable d = null;
		Resources res = null;
		try {
			res = pm.getResourcesForApplication(themePack);
		} catch (NameNotFoundException e) {
			d = null;
		} catch (NullPointerException e) {
			d = null;
		}

		if (res != null) {
			int resource_id = res
					.getIdentifier(namaFile, "drawable", themePack);
			if (resource_id != 0) {
				try {
					d = (NinePatchDrawable) res.getDrawable(resource_id);
				} catch (Resources.NotFoundException e) {
					d = null;
				} catch (NullPointerException e) {
					d = null;
				}
			}
		}
		return d;
	}

	public void HackOnClick(String AKSI) {
		Object sb = ctx.getSystemService("statusbar");
		try {
			Class<?> sbMan = Class.forName("android.app.StatusBarManager");
			Method show = sbMan.getMethod(AKSI);// expand
			show.setAccessible(true);
			show.invoke(sb);
		} catch (ClassNotFoundException e) {
		} catch (NoSuchMethodException e) {
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		}
	}

	public Context getKontek(String theme) {
		Context cont = null;
		try {
			cont = ctx.createPackageContext(theme, 0);
		} catch (NameNotFoundException e) {
		}
		return cont;
	}
}
