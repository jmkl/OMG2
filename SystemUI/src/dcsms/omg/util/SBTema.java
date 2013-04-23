package dcsms.omg.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;

public class SBTema {
	private PackageManager pm;

	public final static String STATUSBAR_SHOW = "expand";
	public final static String STATUSBAR_HIDE = "collapse";
	public final static String DATEVIEW_BG = "dateview_bg";

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

	public SBTema(Context context) {
		ctx = context;
		pm = context.getPackageManager();
		pref = new getPref(context, Model.PREF_JUDUL);
		if (pref != null)
			themePack = pref.getNamaPaketStatusbarTema();

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
			
		}

		return d;
	}

	public String[] getArrayWBg(String nama) {
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

	public Drawable getXMLorDrawable(String NAMOICON) {
		Resources themeResources = null;
		Drawable d = getDrawabledariSesuatu(NAMOICON);
		if (d == null) {
			String themePack = "Model.OMGkendali";
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

	public Bitmap getICON(String NAMOICON) {
		Resources themeResources = null;
		Bitmap d = getICONFromTheme(NAMOICON);
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
						d = ((BitmapDrawable) themeResources.getDrawable(resource_id)).getBitmap();
					} catch (Resources.NotFoundException e) {
						d = null;
					} catch (NullPointerException e) {
						d = null;
					}
				}
			}
		}

		return d;
	}

	private Bitmap getICONFromTheme(String NAMOICON) {
		Resources themeResources = null;
		Bitmap d = null;
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
					d = ((BitmapDrawable) themeResources
							.getDrawable(resource_id)).getBitmap();
				} catch (Resources.NotFoundException e) {
					d = null;
				} catch (NullPointerException e) {
					d = null;
				}
			}
		}

		return d;
	}
	public boolean getBoolen(String namaString, String themePack) {
		boolean bool = false;
		Resources res = null;
		try {
			res = pm.getResourcesForApplication(themePack);
		} catch (NameNotFoundException e) {
		} catch (NullPointerException e) {
		}

		if (res != null) {
			int resource_id = res
					.getIdentifier(namaString, "bool", themePack);
			if (resource_id != 0) {
				try {
					bool= res.getBoolean(resource_id);
				} catch (Resources.NotFoundException e) {
				}
			}
		}
		return bool;
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
