package dcsms.omg.notification;

import android.graphics.drawable.Drawable;

public class TogglesModel {
	int type;
	Drawable bmp;
	String teks;

	public TogglesModel() {
	};
public void updateShit(){
	
}
	public TogglesModel(String nama, Drawable bmp, int type) {
		teks = nama;
		this.bmp = bmp;
		this.type = type;
	};

	public int getType() {
		return type;
	}

	public String getNama() {
		return teks;
	}

	public Drawable getBitmap() {
		return bmp;
	}
}
