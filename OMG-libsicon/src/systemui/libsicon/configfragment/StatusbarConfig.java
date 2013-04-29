package systemui.libsicon.configfragment;

import systemui.libsicon.R;
import systemui.libsicon.C.Model;
import systemui.libsicon.C.Sett;
import systemui.libsicon.adapter.SpinnerAdapter;
import systemui.libsicon.utils.CustomPreference;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;

import com.actionbarsherlock.app.SherlockFragment;

public class StatusbarConfig extends SherlockFragment implements
		OnCheckedChangeListener, OnItemSelectedListener {
	// Preference
	CustomPreference pref;

	// Object
	private Spinner sp_batt, sp_layout;
	private CheckBox cb_trafik, cb_ampmsize, cb_hari, cb_ampm, cb_carrier,
			cb_overlap, cb_bott_batt_line, cb_roundcorner;

	// Adapter
	private ArrayAdapter<String> battmode_adapter, sblayout_adapter;

	// Array Data
	private String[] Spinner_Batt_Icon_Menu = { "20 STEP ICON",
			"100 STEP ICON", "% TEXT", "20 STEP ICON & % TEXT", "BATTERY BAR" };
	private int[] Spinner_Batt_Icon_Drawable = { R.drawable.bat_preview_20,
			R.drawable.bat_preview_100, R.drawable.bat_preview_teks,
			R.drawable.bat_preview_20teks, R.drawable.bat_preview_line };
	private String[] SB_Layout_Menu = { "CIEK", "DUO", "TIGO", "AMPEK" };
	private int[] SB_Layout_Drawable = { R.drawable.statbarpreview_1,
			R.drawable.statbarpreview_2, R.drawable.statbarpreview_3,
			R.drawable.statbarpreview_4 };

	@SuppressLint("WorldReadableFiles")
	@SuppressWarnings("deprecation")
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.omg_config_statusbar, container,
				false);
		pref = new CustomPreference(getSherlockActivity().getSharedPreferences(
				Model.PREF_JUDUL, Context.MODE_WORLD_READABLE));

		findView(v);
		setAdapter();
		cekStatus();
		
		return v;
	}

	// findView shit
	private void findView(View v) {
		sp_batt = (Spinner) v.findViewById(R.id.omg_battmode);
		sp_layout = (Spinner) v.findViewById(R.id.omg_sblayout);

		cb_trafik = (CheckBox) v.findViewById(R.id.omg_traffic);
		cb_ampmsize = (CheckBox) v.findViewById(R.id.omg_ampmsize);
		cb_hari = (CheckBox) v.findViewById(R.id.omg_hari);
		cb_ampm = (CheckBox) v.findViewById(R.id.omg_ampm);
		cb_carrier = (CheckBox) v.findViewById(R.id.omg_carrier);
		cb_overlap = (CheckBox) v.findViewById(R.id.omg_overlap);
		cb_bott_batt_line = (CheckBox) v.findViewById(R.id.omg_battline);
		cb_roundcorner = (CheckBox) v.findViewById(R.id.omg_rounded);

		cb_trafik.setOnCheckedChangeListener(this);
		cb_ampmsize.setOnCheckedChangeListener(this);
		cb_hari.setOnCheckedChangeListener(this);
		cb_ampm.setOnCheckedChangeListener(this);
		cb_carrier.setOnCheckedChangeListener(this);
		cb_overlap.setOnCheckedChangeListener(this);
		cb_bott_batt_line.setOnCheckedChangeListener(this);
		cb_roundcorner.setOnCheckedChangeListener(this);
		

		sp_batt.setOnItemSelectedListener(this);
		sp_layout.setOnItemSelectedListener(this);

	}

	private void cekStatus() {
		sp_batt.setSelection(pref.getInteger(Sett.ModelBatteryIcon, 0));
		sp_layout.setSelection(pref.getInteger(Sett.StatusbarLayout, 0));

		cb_trafik.setChecked(bools(Sett.TrafficState));
		cb_ampmsize.setChecked(bools(Sett.UkuranAMPM));
		cb_hari.setChecked(bools(Sett.Hari));
		cb_ampm.setChecked(bools(Sett.AMPM));
		cb_carrier.setChecked(bools(Sett.Carrier));
		cb_overlap.setChecked(bools(Sett.Overlap));
		cb_bott_batt_line.setChecked(bools(Sett.BottomBatteryLine));
		cb_roundcorner.setChecked(bools(Sett.RoundedCorner));

	}

	@SuppressWarnings("deprecation")
	private boolean bools(String key) {
		return new CustomPreference(getSherlockActivity().getSharedPreferences(
				Model.PREF_JUDUL, Context.MODE_WORLD_READABLE)).getBool(key, false);
	}

	private void setBools(String key) {
		boolean enable = bools(key);
		pref.saveBoolSetting(key, enable ? false : true);
	}

	private void setAdapter() {
		battmode_adapter = new SpinnerAdapter(getSherlockActivity(),
				android.R.layout.simple_spinner_item, Spinner_Batt_Icon_Menu,
				Spinner_Batt_Icon_Drawable);
		sblayout_adapter = new SpinnerAdapter(getSherlockActivity(),
				android.R.layout.simple_spinner_item, SB_Layout_Menu,
				SB_Layout_Drawable);

		sp_batt.setAdapter(battmode_adapter);
		sp_layout.setAdapter(sblayout_adapter);


	}

	@Override
	public void onCheckedChanged(CompoundButton btn, boolean bool) {

		switch (btn.getId()) {

		case R.id.omg_ampm:
			setBools(Sett.AMPM);
			break;
		case R.id.omg_ampmsize:
			setBools(Sett.UkuranAMPM);
			break;
		case R.id.omg_battline:
			setBools(Sett.BottomBatteryLine);
			break;
		case R.id.omg_carrier:
			setBools(Sett.Carrier);
			break;
		case R.id.omg_hari:
			setBools(Sett.Hari);
			break;
		case R.id.omg_overlap:
			setBools(Sett.Overlap);
			break;
		case R.id.omg_traffic:
			setBools(Sett.TrafficState);
			break;
		case R.id.omg_rounded:
			setBools(Sett.RoundedCorner);
			break;
		}

	}

	@Override
	public void onItemSelected(AdapterView<?> bb, View v, int pos, long arg3) {
		switch (bb.getId()) {
		case R.id.omg_sblayout:
			pref.saveIntSetting(Sett.StatusbarLayout, pos);
			
			break;

		case R.id.omg_battmode:
			pref.saveIntSetting(Sett.ModelBatteryIcon, pos);
			break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	};
	
}
