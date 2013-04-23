package dcsms.omg.util;

import java.util.ArrayList;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;

public class getPref {
	private SharedPreferences prefs;
	String APPS1;

	public getPref(Context mContext, String key) {
		Context cont = null;
		try {
			cont = mContext.createPackageContext(Model.OMG, 0);
		} catch (NameNotFoundException e) {
		}
		prefs = cont.getSharedPreferences(key, Context.MODE_WORLD_READABLE);
		loadPreference();
	}

	public void saveInt(String key, int value) {
		SharedPreferences.Editor edit = prefs.edit();
		edit.putInt(key, value);
		edit.commit();
	}

	private void loadPreference() {

		Model.Posisi_1 = prefs.getInt(Model.POSISI_KEY[0], 0);
		Model.Posisi_2 = prefs.getInt(Model.POSISI_KEY[1], 1);
		Model.Posisi_3 = prefs.getInt(Model.POSISI_KEY[2], 2);
		Model.Posisi_4 = prefs.getInt(Model.POSISI_KEY[3], 3);
		Model.Posisi_5 = prefs.getInt(Model.POSISI_KEY[4], 4);
		Model.Posisi_6 = prefs.getInt(Model.POSISI_KEY[5], 5);
		Model.Posisi_7 = prefs.getInt(Model.POSISI_KEY[6], 6);
		Model.Posisi_8 = prefs.getInt(Model.POSISI_KEY[7], 7);
		Model.Posisi_9 = prefs.getInt(Model.POSISI_KEY[8], 8);
		Model.Posisi_10 = prefs.getInt(Model.POSISI_KEY[9], 9);
		Model.Posisi_11 = prefs.getInt(Model.POSISI_KEY[10], 10);
		Model.Posisi_12 = prefs.getInt(Model.POSISI_KEY[11], 11);
		Model.Posisi_13 = prefs.getInt(Model.POSISI_KEY[12], 12);
		Model.Posisi_14 = prefs.getInt(Model.POSISI_KEY[13], 13);
		Model.Posisi_15 = prefs.getInt(Model.POSISI_KEY[14], 14);
		Model.Posisi_16 = prefs.getInt(Model.POSISI_KEY[15], 15);
		Model.Posisi_17 = prefs.getInt(Model.POSISI_KEY[16], 16);
		Model.Posisi_18 = prefs.getInt(Model.POSISI_KEY[17], 17);
		Model.Posisi_19 = prefs.getInt(Model.POSISI_KEY[18], 18);
		Model.Posisi_20 = prefs.getInt(Model.POSISI_KEY[19], 19);
		Model.Posisi_21 = prefs.getInt(Model.POSISI_KEY[20], 20);
		Model.Posisi_22 = prefs.getInt(Model.POSISI_KEY[21], 21);
		Model.Posisi_23 = prefs.getInt(Model.POSISI_KEY[22], 22);
		Model.Posisi_24 = prefs.getInt(Model.POSISI_KEY[23], 23);
		Model.Posisi_25 = prefs.getInt(Model.POSISI_KEY[24], 24);
		Model.Posisi_26 = prefs.getInt(Model.POSISI_KEY[25], 25);
		Model.Posisi_27 = prefs.getInt(Model.POSISI_KEY[26], 26);
		Model.Posisi_28= prefs.getInt(Model.POSISI_KEY[27], 27);
		Model.TEMA = prefs.getString(Model.TEMA_YANG_DIPAKAI, "dcsms");
		Model.TOGEL_ACTIVE_VALUE = prefs.getInt(Model.TOGEL_ACTIVE, 1);
	}

	public SharedPreferences getPreference() {
		return prefs;
	}
	public int getBaterai_mode(){
		return prefs.getInt(SBK.BATTERYMODE, 0);
	}
public boolean getBoolean(String key,boolean def){
	return prefs.getBoolean(key, def);
}
public int getInt(String key,int def){
	return prefs.getInt(key, def);
}
public String getString(String key,String def){
	return prefs.getString(key, def);
}
	public int getToggleStatus() {
		return Model.TOGEL_ACTIVE_VALUE;
	}

	public String getApplikasi(String app_ke) {
		return prefs.getString(app_ke, "dcsms");
	}

	public String getFormatTanggal() {
		return prefs.getString(Model.FORMAT_TANGGAL, "dd,MMMM yyyy");
	}

	public String getNamaPaketTema() {
		return prefs.getString(Model.TEMA_YANG_DIPAKAI, "dcsms");
	}
	public String getNamaPaketStatusbarTema() {
		return prefs.getString(Model.TEMA_STATUSBAR_YANG_DIPAKAI, "dcsms");
	}

	public int[] getPrimitifArai() {
		ArrayList<Integer> pos = new ArrayList<Integer>();
		for (int i = 0; i < Model.POSISI_KEY.length; i++) {
			pos.add(i, prefs.getInt(Model.POSISI_KEY[i], i));
		}
		return GenArray.convertInt(pos);
	}

	public float getFloat(String key, float f) {
		// TODO Auto-generated method stub
		return prefs.getFloat(key, f);
	}

}
