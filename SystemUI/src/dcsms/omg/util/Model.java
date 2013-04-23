package dcsms.omg.util;

import android.content.Context;
import android.content.Intent;

public class Model {

	public final static String TEMA_YANG_DIPAKAI = "tema_yang_dipakai";
	public final static String TEMA_STATUSBAR_YANG_DIPAKAI = "tema_statusbar_yang_dipakai";
	public final static String USB_PLUG = "usb_plugin";
	public final static String FORMAT_TANGGAL = "format_tanggal";
	public static String TEMA = "";
	public final static String UPDATE_VIEW = "dcsms.powerwidget.UPDATE";
	public final static String UPDATE_STATUSBAR = "dcsms.statusbar.UPDATE";
	public final static String UPDATE_ORDER = "dcsms.powerwidget.ORDERUPDATE";
	public final static String OMG = "systemui.libsicon";
	public static final String PREF_JUDUL = "Togel_Order_Madafaka";
	public static final String TOGEL_ACTIVE = "togel_on";
	public static int TOGEL_ACTIVE_VALUE = 1;
	public static int[] mTogel = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
			13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27 };

	public static String[] POSISI_KEY = { "Screen TimeOut",
			"Vibration & Sound", "Auto Rotation", "BlueTooth", "GPS",
			"ScreenCapture", "WiFi", "WiFi Tethering", "USB Tethering",
			"AirPlane Mode", "Reboot & Recovery", "Mobile Data",
			"Music Play & Pause", "Music Next", "Music Previous", "FlashLight",
			"Application 1", "Application 2", "Application 3", "Application 4",
			"Application 5", "Application 6", "Application 7", "Application 8",
			"Application 9", "Application 10", "Application 11",
			"Application 12" };
	
	public static String[] KEYWORD={"Application 1", "Application 2", "Application 3", "Application 4",
		"Application 5", "Application 6", "Application 7", "Application 8",
		"Application 9", "Application 10", "Application 11",
		"Application 12" };
	public static int Posisi_1, Posisi_2, Posisi_3, Posisi_4, Posisi_5,
			Posisi_6, Posisi_7, Posisi_8, Posisi_9, Posisi_10, Posisi_11,
			Posisi_12, Posisi_13, Posisi_14, Posisi_15, Posisi_16, Posisi_17,
			Posisi_18, Posisi_19, Posisi_20, Posisi_21, Posisi_22, Posisi_23,
			Posisi_24, Posisi_25, Posisi_26, Posisi_27, Posisi_28;

	// weather shit
	public static final String PREF_CUACA_NAMAKOTA = "nama_kota";
	public static final String PREF_CUACA_IDKOTA = "id_kota";

	public static final String PREF_DATAKOTA_NAMA = "kondisi";
	public static final String PREF_DATAKOTA_ICON = "namaicon";
	public static final String PREF_DATAKOTA_SUHU = "suhu";

	public static String CUACA_NAMAKOTA = "";
	public static String CUACA_IDKOTA = "";
	public static String DATAKOTA_NAMA = "";
	public static String DATAKOTA_ICON = "";
	public static String DATAKOTA_SUHU = "";

	public static final String REFERAL = "http://www.wunderground.com/?apiref=9dd4d19f18abe851";
	public static final String cari_kota = "http://autocomplete.wunderground.com/aq?query=";
	public static final String TAG_RESULT = "RESULTS";
	public static final String TAG_NAME = "name";
	public static final String TAG_TYPE = "type";
	public static final String TAG_C = "c";
	public static final String TAG_ZMW = "zmw";
	public static final String TAG_TZ = "tz";
	public static final String TAG_TZS = "tzs";
	public static final String TAG_L = "l";
	private String API = "eb90c2f0abc44599";// OMG DCSMSDUMMY
	// private String IDKOTA = "00000.1.96163";

	public static final String TAG_INFOCUACA_CUR_OBSERVE = "current_observation";
	public static final String TAG_INFOCUACA_CUR_OBSERVE_IMG = "image";
	public static final String TAG_INFOCUACAERROR = "error";
	public static final String TAG_INFOCUACA_CUR_OBSERVE_WEATHER = "weather";
	public static final String TAG_INFOCUACA_CUR_OBSERVE_TEMP_C = "temp_c";
	public static final String TAG_INFOCUACA_CUR_OBSERVE_TEMP_F = "temp_f";
	public static final String TAG_INFOCUACA_CUR_OBSERVE_TEMP_ICON = "icon";
	public static final String TAG_INFOCUACA_CUR_OBSERVE_TEMP_HUMIDITY = "relative_humidity";
	
	public static final String[] APPS_LAUNCHER = { "applikasi1", "applikasi2",
			"applikasi3", "applikasi4", "applikasi5", "applikasi6",
			"applikasi7", "applikasi8", "applikasi9", "applikasi10",
			"applikasi11", "applikasi12" };
	public static String[] APPS_SC = { "Shotcut_1", "Shotcut_2", "Shotcut_3",
			"Shotcut_4", "Shotcut_5", "Shotcut_6", "Shotcut_7", "Shotcut_8",
			"Shotcut_9", "Shotcut_10", "Shotcut_11", "Shotcut_12" };
	public static final String[] NAMA_APPS_LAUNCHER = { "nama_applikasi1",
			"nama_applikasi2", "nama_applikasi3", "nama_applikasi4",
			"nama_applikasi5", "nama_applikasi6", "nama_applikasi7",
			"nama_applikasi8", "nama_applikasi9", "nama_applikasi10",
			"nama_applikasi11", "nama_applikasi12" };

	public static final String REKUES_JSON_KONDISI = "http://api.wunderground.com/api/%1$s/conditions/q/zmw:%2$s.json";
	public static final String APPS = "APPS_POSISI";
	public static final String NAMAAPPS = "NAMA_APPS_POSISI";

	public String NamaKota = "";
	public String IDKota = "";

	public String AmbilInfoCuaca(String Idkota,String APIKey) {
		return String.format(REKUES_JSON_KONDISI, APIKey, Idkota);
	}

	public void setModel(String namakota, String id) {
		NamaKota = namakota;
		IDKota = id;
	}

	public String getNamaKota() {
		return NamaKota;
	}

	public String getIdKota() {
		return IDKota;
	}

	public final static void Update_View(Context c) {
		Intent i = new Intent();
		i.setAction(Model.UPDATE_VIEW);
		Intent i2 = new Intent();
		i2.setAction(Model.UPDATE_ORDER);
		c.sendBroadcast(i2);
		c.sendBroadcast(i);
		c.sendBroadcast(new Intent().setAction(SBK.UPDATE_LAYOUT));
		c.sendBroadcast(new Intent().setAction(Model.UPDATE_STATUSBAR));

	}

}

// "name":"Padang, Indonesia",
// "type":"city",
// "c":"ID",
// "zmw":"00000.1.96163",
// "tz":"Asia/Jakarta",
// "tzs":"WIT",
// "l":"/q/zmw:00000.1.96163"

//
//

// {
// "response":{
// "version":"0.1",
// "termsofService":"http://www.wunderground.com/weather/api/d/terms.html",
// "features":{
// "conditions":1
// }
// },

// "current_observation":{
// "image":{
// "url":"http://icons-ak.wxug.com/graphics/wu2/logo_130x80.png",
// "title":"Weather Underground",
// "link":"http://www.wunderground.com"
// },
// "display_location":{
// "full":"Padang, Indonesia",
// "city":"Padang",
// "state":"",
// "state_name":"Indonesia",
// "country":"ID",
// "country_iso3166":"ID",
// "zip":"00000",
// "latitude":"-0.88000000",
// "longitude":"100.34999847",
// "elevation":"3.00000000"
// },
// "observation_location":{
// "full":"Padang / Tabing, ",
// "city":"Padang / Tabing",
// "state":"",
// "country":"Indonesia",
// "country_iso3166":"ID",
// "latitude":"-0.88333303",
// "longitude":"100.34999847",
// "elevation":"10 ft"
// },
// "estimated":{
//
// },
// "station_id":"96163",
// "observation_time":"Last Updated on February 7, 7:00 AM WIT",
// "observation_time_rfc822":"Thu, 07 Feb 2013 07:00:00 +0700",
// "observation_epoch":"1360195200",
// "local_time_rfc822":"Thu, 07 Feb 2013 11:28:53 +0700",
// "local_epoch":"1360211333",
// "local_tz_short":"WIT",
// "local_tz_long":"Asia/Jakarta",
// "local_tz_offset":"+0700",
// "weather":"Scattered Clouds",
// "temperature_string":"74 F (23 C)",
// "temp_f":74,
// "temp_c":23,
// "relative_humidity":"97%",
// "wind_string":"Calm",
// "wind_dir":"",
// "wind_degrees":0,
// "wind_mph":0,
// "wind_gust_mph":0,
// "wind_kph":0,
// "wind_gust_kph":0,
// "pressure_mb":"1010",
// "pressure_in":"29.84",
// "pressure_trend":"+",
// "dewpoint_string":"73 F (23 C)",
// "dewpoint_f":73,
// "dewpoint_c":23,
// "heat_index_string":"NA",
// "heat_index_f":"NA",
// "heat_index_c":"NA",
// "windchill_string":"NA",
// "windchill_f":"NA",
// "windchill_c":"NA",
// "feelslike_string":"74 F (23 C)",
// "feelslike_f":"74",
// "feelslike_c":"23",
// "visibility_mi":"5.0",
// "visibility_km":"8.0",
// "solarradiation":"",
// "UV":"-1",
// "precip_1hr_string":" in ( mm)",
// "precip_1hr_in":"",
// "precip_1hr_metric":"",
// "precip_today_string":" in ( mm)",
// "precip_today_in":"",
// "precip_today_metric":"",
// "icon":"partlycloudy",
// "icon_url":"http://icons-ak.wxug.com/i/c/k/partlycloudy.gif",
// "forecast_url":"http://www.wunderground.com/global/stations/96163.html",
// "history_url":"http://www.wunderground.com/history/airport/96163/2013/2/7/DailyHistory.html",
// "ob_url":"http://www.wunderground.com/cgi-bin/findweather/getForecast?query=-0.88333303,100.34999847"
// }
// }

// XXX =============================================iconstate
/**
 * chanceflurries 
 * chancerain 
 * chancesleet 
 * chancesnow 
 * chancetstorms
 * clear 
 * cloudy 
 * flurries 
 * fog 
 * hazy 
 * mostlycloudy 
 * mostlysunny
 * partlycloudy 
 * partlysunny 
 * sleet 
 * rain 
 * snow 
 * sunny 
 * tstorms 
 * unknown
 */

