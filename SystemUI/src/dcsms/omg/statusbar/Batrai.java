package dcsms.omg.statusbar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.BatteryManager;
import android.util.AttributeSet;
import android.util.secutil.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import dcsms.omg.util.Model;
import dcsms.omg.util.SBK;
import dcsms.omg.util.Tema;
import dcsms.omg.util.getPref;

public class Batrai extends SBIcon {

	private ImageView baterai;
	private TextView bat_text;
	private View v;
	private int batt_status, batt_level;
	Bitmap batt_bitmap,batt=null,battnumb=null,battcharge=null;

	private static final int BATT_MODE_ICON = 0;
	private static final int BATT_MODE_ICONNUMBER = 1;
	private static final int BATT_MODE_TXT = 2;
	private static final int BATT_MODE_ICON_N_TXT = 3;
	private static final int BATT_MODE_BAR = 4;
	private int batt_MODE = BATT_MODE_ICON;
	private int bx, by, x, y;
	private int shadx, shady, shadrad, shadcolor;
	int usb;
	Bitmap bat100,bat20;

	public Batrai(Context context, AttributeSet attrs) {
		super(context, attrs);
	}


	public void setReferensi(Tema mTema, getPref sbPref, getPref pref) {
		this.mTema =mTema;
		this.sbPref=sbPref;
		this.pref=pref;
		setBattBitmap();
		getBattTextReference();
		
	}
	public void setBattBitmap(){
		if(batt!=null)
			batt.recycle();
		if(battnumb!=null)
			battnumb.recycle();
		if(battcharge!=null)
			battcharge.recycle();
		
		batt=mTema.getIconBitmap(SBK.BATT);
		battnumb=mTema.getIconBitmap(SBK.BATT_NUMBER);
		battcharge=mTema.getIconBitmap(SBK.BATT_CHARGE);
	}


	public void UpdateBaterai(Intent intent) {
		String action = intent.getAction();

		if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
			batt_status = intent.getIntExtra(BatteryManager.EXTRA_STATUS,
					BatteryManager.BATTERY_STATUS_UNKNOWN);
			int rawlevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
			int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
			batt_level = -1;
			if (rawlevel >= 0 && scale > 0) {
				batt_level = (rawlevel * 100) / scale;
			}
			
			Log.e("BATRAI", "level : "+batt_level);
			UpdateBatraiView();

		} else if (action.equals(Model.UPDATE_STATUSBAR)) {
			UpdateBatraiView();
		}

	}

	@Override
	protected void Inisiasi() {
		super.Inisiasi();
		if (baterai != null)
			removeView(baterai);
		if (bat_text != null)
			removeView(bat_text);
		if (v != null)
			removeView(v);

		setOrientation(LinearLayout.HORIZONTAL);
		setGravity(Gravity.CENTER_VERTICAL);
		setPadding(2, 0, 2, 0);
		baterai = new ImageView(mContext);
		bat_text = new TextView(mContext);
		bat_text.setGravity(Gravity.CENTER_VERTICAL);
		v = new View(mContext);
		v.setLayoutParams(new LayoutParams(2, 2));
		setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.MATCH_PARENT));
		baterai.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.MATCH_PARENT));
		baterai.setAdjustViewBounds(true);

		addView(bat_text, 0);
		addView(v, 1);
		addView(baterai, 2);
	}

	private void Munculin(boolean image, boolean txt) {
		bat_text.setVisibility(txt ? View.VISIBLE : View.GONE);
		baterai.setVisibility(image ? View.VISIBLE : View.GONE);
	}

	private void UpdateBatraiView() {
		batt_MODE = pref.getBaterai_mode();

		switch (batt_MODE) {
		case BATT_MODE_ICON:
			if (batt_status == BatteryManager.BATTERY_STATUS_CHARGING) {
				Munculin(true, false);
				batt_bitmap = battcharge;
				x = batt_bitmap.getWidth();
				y = batt_bitmap.getHeight();
				bx = -(x / 4);
				by = -(y / 5);
				doBaterai_20(batt_level);
			} else {
				Munculin(true, false);
				batt_bitmap = batt;
				x = batt_bitmap.getWidth();
				y = batt_bitmap.getHeight();
				bx = -(x / 4);
				by = -(y / 5);
				doBaterai_20(batt_level);
			}
			break;

		case BATT_MODE_ICONNUMBER:
			Munculin(true, false);
			batt_bitmap = battnumb;
			x = batt_bitmap.getWidth();
			y = batt_bitmap.getHeight();
			bx = -(x / 10);
			by = -(y / 10);
			doBaterai_100(batt_level);
			break;
		case BATT_MODE_ICON_N_TXT:
			Munculin(true, true);
			if (batt_status == BatteryManager.BATTERY_STATUS_CHARGING) {
				batt_bitmap = battcharge;
				x = batt_bitmap.getWidth();
				y = batt_bitmap.getHeight();
				bx = -(x / 4);
				by = -(y / 5);
				doBaterai_20(batt_level);
			} else {
				batt_bitmap =batt;
				x = batt_bitmap.getWidth();
				y = batt_bitmap.getHeight();
				bx = -(x / 4);
				by = -(y / 5);
				doBaterai_20(batt_level);
			}
			doBaterai_text(batt_level);
			break;
		case BATT_MODE_TXT:
			Munculin(false, true);
			doBaterai_text(batt_level);
			break;
		case BATT_MODE_BAR:
			Munculin(false, false);
			break;
		}
	}

	private void doBaterai_text(int value) {
		String persen = Integer.toString(value);
		if (value < 0)
			persen = "100";

		bat_text.setText(persen + " %");
	}
	
	void getBattTextReference(){

		shadx = sbPref.getInt(SBK.BAT_X, 1);
		shady = sbPref.getInt(SBK.BAT_Y, 1);
		shadrad = sbPref.getInt(SBK.BAT_R, 1);
		shadcolor = sbPref.getInt(SBK.BAT_Col, 0xff000000);
		bat_text.setShadowLayer(shadrad, shadx, shady, shadcolor);
		bat_text.setTextColor(mTema.getWarnaTogel(SBK.BAT_MAIN_COL));
		bat_text.setTypeface(mTema.getFontfromTheme(mTema
				.getStringfromTheme(SBK.FONT_BAT)));
		bat_text.setTextSize(mTema.getDefaultDimen(SBK.UKURAN_BATT));
		
	}

	private void doBaterai_100(int value) {
		if (value < 0)
			update100(bx * (9), by * 9);
		else if (value > 0 && value < 9)
			update100(bx * value, 0);
		else if (value > 10 && value < 19)
			update100(bx * (value - 10), by);
		else if (value > 20 && value < 29)
			update100(bx * (value - 20), by * 2);
		else if (value > 30 && value < 39)
			update100(bx * (value - 30), by * 3);
		else if (value > 40 && value < 49)
			update100(bx * (value - 40), by * 4);
		else if (value > 50 && value < 59)
			update100(bx * (value - 50), by * 5);
		else if (value > 60 && value < 69)
			update100(bx * (value - 60), by * 6);
		else if (value > 70 && value < 79)
			update100(bx * (value - 70), by * 7);
		else if (value > 80 && value < 89)
			update100(bx * (value - 80), by * 8);
		else if (value > 90 && value < 99)
			update100(bx * (value - 90), by * 9);
		else {
			update100(bx * (9), by * 9);
		}

	}

	private void update100(int xpos, int ypos) {
		if(bat100!=null)
			bat100.recycle();
		
		bat100 = Bitmap.createBitmap(x / 10, y / 10,
				Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bat100);
		c.drawBitmap(batt_bitmap, xpos, ypos, null);
		baterai.setImageBitmap(null);
		baterai.setImageBitmap(bat100);

	}

	private void doBaterai_20(int e) {
		if (e < 0)
			updateBatrai(bx * 3, by * 4);
		else if (e <= 0) {
			updateBatrai(0, 0);
		} else if (e >= 6 && e <= 10) {
			updateBatrai(bx, 0);
		} else if (e >= 11 && e <= 15) {
			updateBatrai(bx * 2, 0);
		} else if (e >= 16 && e <= 20) {
			updateBatrai(bx * 3, 0);
		} else if (e >= 21 && e <= 25) {
			updateBatrai(0, by);
		} else if (e >= 26 && e <= 30) {
			updateBatrai(bx, by);
		} else if (e >= 31 && e <= 35) {
			updateBatrai(bx * 2, by);
		} else if (e >= 36 && e <= 40) {
			updateBatrai(bx * 3, by);
		} else if (e >= 41 && e <= 45) {
			updateBatrai(0, by * 2);
		} else if (e >= 46 && e <= 50) {
			updateBatrai(bx, by * 2);
		} else if (e >= 51 && e <= 55) {
			updateBatrai(bx * 2, by * 2);
		} else if (e >= 56 && e <= 60) {
			updateBatrai(bx * 3, by * 2);
		} else if (e >= 61 && e <= 65) {
			updateBatrai(0, by * 3);
		} else if (e >= 66 && e <= 70) {
			updateBatrai(bx, by * 3);
		} else if (e >= 71 && e <= 75) {
			updateBatrai(bx * 2, by * 3);
		} else if (e >= 76 && e <= 80) {
			updateBatrai(bx * 3, by * 3);
		} else if (e >= 81 && e <= 85) {
			updateBatrai(0, by * 4);
		} else if (e >= 86 && e <= 90) {
			updateBatrai(bx, by * 4);
		} else if (e >= 91 && e <= 95) {
			updateBatrai(bx * 2, by * 4);
		} else if (e > 95) {
			updateBatrai(bx * 3, by * 4);
		} else {
			updateBatrai(bx * 3, by * 4);
		}

	}

	private void updateBatrai(int i, int j) {
		if(bat20!=null)
			bat20.recycle();
		
		bat20 = Bitmap.createBitmap(x / 4, y / 5, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bat20);
		c.drawBitmap(batt_bitmap, i, j, null);
		baterai.setImageBitmap(null);
		baterai.setImageBitmap(bat20 );

	}

}
