package dcsms.omg.notification;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import dcsms.omg.util.Model;
import dcsms.omg.util.Tema;
import dcsms.omg.util.getPref;

public class AppsLauncher {
	private Context mContext;
	private String namapaket = "dcsms";
	private String namaapps = "dcsms";
	int gw;
	static getPref pref;

	public AppsLauncher(Context mContext, int which) {
		gw = which;
		this.mContext = mContext;
		pref = new getPref(mContext, Model.PREF_JUDUL);
		namapaket = pref.getApplikasi(Model.APPS_LAUNCHER[gw]);
		namaapps = pref.getApplikasi(Model.NAMA_APPS_LAUNCHER[gw]);

	}

	public String get() {
		return namaapps;
	}
	public Drawable getBimtap(){
		if(namaapps.equals("dcsms"))
			return new Tema(mContext).getICON(Tema.IKON_NONE);
		else
			return null;//new Tema(mContext).getAppsIcon(namapaket);
	}

	public void launch() {
		SS.expandSB(mContext);
		if (namapaket.equals("dcsms")) {
			Intent i = new Intent();
			i.setClassName("dcsms.pusatkendali","dcsms.pusatkendali.apps.AddApp");
			i.putExtra(Model.APPS, Model.APPS_LAUNCHER[gw]);
			i.putExtra(Model.NAMAAPPS, Model.NAMA_APPS_LAUNCHER[gw]);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.startActivity(i);
		} else {
			Intent LaunchIntent = mContext.getPackageManager()
					.getLaunchIntentForPackage(namapaket);
			try {
				mContext.startActivity(LaunchIntent);
			} catch (ActivityNotFoundException e) {
			}

		}
	}
}
