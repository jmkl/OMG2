package dcsms.omg.notification;

import android.content.Context;
import android.graphics.drawable.Drawable;
import dcsms.omg.util.Tema;

public class TGL {
	protected Context context;
	protected Tema mTema;
	protected int posisi;
	private String status;
	private Drawable icon;

	public TGL(Context context) {
		this.context = context;
		mTema = new Tema(context);
	}

	public void setStatus(String state) {
		this.status = state;
	}

	public String getStatus() {
		return status;
	}

	public void updateView() {
		getStatus();
		getIcon();
	}

	public void updateMe() {
		getStatus();
		setStatus(status);
		getIcon();
		setIcon(icon);

	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

	public Drawable getIcon() {
		return icon;
	}

	public String getText() {
		return null;

	}

	public void setPosisi(int posisi) {
		this.posisi = posisi;
	}

	public int getPosisi() {
		return posisi;
	}

}
