package systemui.libsicon.utils;

import java.util.ArrayList;

import systemui.libsicon.C.Model;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

public class CustomPreference implements OnSharedPreferenceChangeListener {

	private final SharedPreferences prefs;

	public CustomPreference(SharedPreferences _prefs) {
		prefs = _prefs;
		prefs.registerOnSharedPreferenceChangeListener(this);
		loadPrefs(prefs);
		
	}

	@Override
	protected void finalize() {
		prefs.unregisterOnSharedPreferenceChangeListener(this);
	}

	public void setPosisi(int[] value) {
		for (int i = 0; i < Model.POSISI_KEY.length; i++) {
			saveIntSetting(Model.POSISI_KEY[i], value[i]);
		}

	}

	public void saveIntSetting(String key, int value) {
		getEditor().putInt(key, value).commit();
	}
	public void saveBoolSetting(String key, boolean value) {
		getEditor().putBoolean(key, value).commit();
	}
	public void removeString(String key){
		getEditor().remove(key).commit();
	}
	public void saveStringSetting(String key,String value){
		getEditor().putString(key, value).commit();
	}
	public int getInteger(String key, int def){
		return prefs.getInt(key, def);
	}
	public boolean getBool(String key, boolean def){
		return prefs.getBoolean(key, def);
	}
	public String getString(String key, String def){
		return prefs.getString(key, def);
	}

	public SharedPreferences.Editor getEditor() {
		
		return prefs.edit();
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
	
		loadPrefs(prefs);
	}
	public String getTemaTerAplai() {
		return Model.TEMA;
	}
	public String getNamaKota() {
		return Model.CUACA_NAMAKOTA;
	}

	public String getIDKota() {
		return Model.CUACA_IDKOTA;
	}
	public String getKondisie() {
		return Model.DATAKOTA_NAMA;
	}
	public String getIkone() {
		return Model.DATAKOTA_ICON;
	}
	public String getSuhue() {
		return Model.DATAKOTA_SUHU;
	}

	private void loadPrefs(SharedPreferences prefs) {
		Model.CUACA_NAMAKOTA = prefs.getString(Model.PREF_CUACA_NAMAKOTA,"dcsms");
		Model.CUACA_IDKOTA = prefs.getString(Model.PREF_CUACA_IDKOTA, "dcsms");
		Model.DATAKOTA_NAMA=prefs.getString(Model.PREF_DATAKOTA_NAMA, "dcsms");
		Model.DATAKOTA_ICON=prefs.getString(Model.PREF_DATAKOTA_ICON, "dcsms");
		Model.DATAKOTA_SUHU=prefs.getString(Model.PREF_DATAKOTA_SUHU, "dcsms");
		Model.TEMA = prefs.getString(Model.TEMA_YANG_DIPAKAI, "dcsms");
		
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
		
		//XXX Tombol Nih
		for (int i = 0; i < Model.APPS_SC.length; i++) {
			Model.APPS_SC[i]=prefs.getString(Model.APPS_LAUNCHER[i], "dcsms");
		}
	}

	public int[] getPrimitifArai() {
		ArrayList<Integer> pos = new ArrayList<Integer>();
		for (int i = 0; i < Model.POSISI_KEY.length; i++) {
			pos.add(i, prefs.getInt(Model.POSISI_KEY[i], i));
		}
		return GenArray.convertInt(pos);
	}

}
