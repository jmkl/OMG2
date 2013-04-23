package dcsms.omg.notification;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import dcsms.omg.util.CekStatus;
import dcsms.omg.util.Tema;

public class AutoRotasi extends TGL {

	public AutoRotasi(Context context) {
		super(context);
	}

	@Override
	public String getStatus() {
		return cekAutoRotation(context) ? "AutoRotation On"
				: "AutoRotation Off";
	}

	@Override
	public void updateMe() {
		boolean bool = cekAutoRotation(context);
		Settings.System.putInt(context.getContentResolver(),
				Settings.System.ACCELEROMETER_ROTATION, bool ? 0 : 1);
		super.updateMe();

	}

	@Override
	public Drawable getIcon() {
		if (cekAutoRotation(context))
			return new Tema(context).getICON(Tema.IKON_ROTATE_ON);
		else
			return new Tema(context).getICON(Tema.IKON_ROTATE_OFF);
	}

	public static void DoAutoRotate(Context mContext, TogglesHolder holder) {
		boolean bool = cekAutoRotation(mContext);
		if (bool)
			Settings.System.putInt(mContext.getContentResolver(),
					Settings.System.ACCELEROMETER_ROTATION, 0);
		else
			Settings.System.putInt(mContext.getContentResolver(),
					Settings.System.ACCELEROMETER_ROTATION, 1);

		holder.tv.setText(status(mContext));
		holder.iv.setImageDrawable(getBimtap(mContext));

	}

	public static String status(Context context) {
		return cekAutoRotation(context) ? "AutoRotation ON"
				: "AutoRotation OFF";
	}

	public static boolean cekAutoRotation(Context context) {
		String status = CekStatus.AUTOROTATE(context);
		if (status.equals("0"))
			return false;
		else
			return true;

	}

	public static Drawable getBimtap(Context mContext) {
		if (cekAutoRotation(mContext))
			return new Tema(mContext).getICON(Tema.IKON_ROTATE_ON);
		else
			return new Tema(mContext).getICON(Tema.IKON_ROTATE_OFF);
	}

}
