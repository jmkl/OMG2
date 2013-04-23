package dcsms.omg.notification;

import dcsms.omg.util.Tema;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.NinePatchDrawable;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.SeekBar;

public class SlaiderBraiknesShit extends SeekBar {
	private Context cntx;
	float BackLightValue = 0.5f;

	public SlaiderBraiknesShit(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		cntx = context;
		Inisiasi();
	}

	public SlaiderBraiknesShit(Context context, AttributeSet attrs) {
		super(context, attrs);
		cntx = context;
		Inisiasi();
	}

	public SlaiderBraiknesShit(Context context) {
		super(context);
		cntx = context;
		Inisiasi();
	}



	public void Inisiasi() {
		setThumb(new Tema(cntx).getICON(Tema.SEEKBAR_THUMBNAIL));
		setThumbOffset(15);
		setSeekBarImages();		

		int now = 10;
		try {
			now = Settings.System.getInt(cntx.getContentResolver(),
					Settings.System.SCREEN_BRIGHTNESS);
		} catch (SettingNotFoundException e) {
			e.printStackTrace();
		}
		setPadding(10, 0, 10, 0);
		setMax(255);
		setProgress(now);
		setOnSeekBarChangeListener(onMakeOut);

	}

	private OnSeekBarChangeListener onMakeOut = new OnSeekBarChangeListener() {
		int proses;

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			if (proses < 10)
				proses = 10;
			setBrightness(proses);
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {

		}

		@Override
		public void onProgressChanged(SeekBar v, int progress, boolean fromUser) {
			proses = progress;

		}
	};

	private void setBrightness(int arg1) {
		BackLightValue = (float) arg1 / 255;
		Intent i = new Intent();
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.setClass(cntx, ChuckNorris.class);
		i.putExtra("SHIT", BackLightValue);
		cntx.startActivity(i);
	}

	public void setSeekBarImages() {
		NinePatchDrawable drawable = new Tema(cntx)
				.getNineBMP(Tema.SEEKBAR_ATAS);
		ClipDrawable clip = new ClipDrawable(drawable, Gravity.LEFT,
				ClipDrawable.HORIZONTAL);
		NinePatchDrawable drawable2 = new Tema(cntx)
				.getNineBMP(Tema.SEEKBAR_BAWAH);
		InsetDrawable d1 = new InsetDrawable(drawable2, 0);
		// the padding u want to use
		LayerDrawable mylayer = new LayerDrawable(new Drawable[] { d1, clip });
		setProgressDrawable(mylayer);
	}

}
