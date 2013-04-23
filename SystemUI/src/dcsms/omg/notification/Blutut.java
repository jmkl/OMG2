package dcsms.omg.notification;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import dcsms.omg.util.CekStatus;
import dcsms.omg.util.Tema;

public class Blutut extends TGL {

	public Blutut(Context context) {
		super(context);
	}

	@Override
	public String getStatus() {
		return CekStatus.BLUETOOTH() ? "B/T On" : "B/T Off";
	}

	@Override
	public Drawable getIcon() {
		return CekStatus.BLUETOOTH() ? mTema.getICON(Tema.IKON_BT_ON) : mTema
				.getICON(Tema.IKON_BT_OFF);
	}

	@Override
	public void updateMe() {
		BluetoothAdapter blutut = BluetoothAdapter.getDefaultAdapter();
		if (!CekStatus.BLUETOOTH())
			blutut.enable();
		else
			blutut.disable();		
		super.updateMe();
	}

	public static void OnOffBlutut() {
		BluetoothAdapter blutut = BluetoothAdapter.getDefaultAdapter();
		if (!CekStatus.BLUETOOTH())
			blutut.enable();
		else
			blutut.disable();
	}

	public static String status() {
		if (CekStatus.BLUETOOTH())
			return "B/T ON";
		else
			return "B/T OFF";
	}

	public static Drawable getBimtap(Context mContext) {
		if (CekStatus.BLUETOOTH())
			return new Tema(mContext).getICON(Tema.IKON_BT_ON);
		else
			return new Tema(mContext).getICON(Tema.IKON_BT_OFF);
	}

}
