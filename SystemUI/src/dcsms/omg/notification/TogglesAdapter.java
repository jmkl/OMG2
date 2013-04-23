package dcsms.omg.notification;

import java.util.List;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import dcsms.omg.util.SBK;
import dcsms.omg.util.Tema;

public class TogglesAdapter extends BaseAdapter {
	private List<TGL> model;
	private Context mContext;
	private Tema mTema;
	int memClass;
	int cacheSize;
	private LruCache<String, Bitmap> mMemoryCache;

	public TogglesAdapter(Context context, List<TGL> toggles) {
		this.model = toggles;
		this.mContext = context;
		mTema=new Tema(mContext);
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		memClass = am.getMemoryClass();
		cacheSize = 1024 * 1024 * memClass / 8;

		mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getRowBytes() * bitmap.getHeight();
			}
		};
	}

	@Override
	public int getCount() {
		return model.size();
	}

	@Override
	public Object getItem(int arg0) {
		return  model.get(arg0);
	}

	@Override
	public long getItemId(int id) {
		return id;
	}


	@Override
	public View getView(int posisi, View cView, ViewGroup parent) {
		LinearLayout.LayoutParams txtparams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		TogglesHolder h = new TogglesHolder();
		LinearLayout v = new LinearLayout(mContext);
		h.tv = new TextView(mContext);
		h.iv = new ImageView(mContext);

		v.setOrientation(LinearLayout.VERTICAL);
		v.setGravity(Gravity.CENTER_HORIZONTAL);

		h.tv.setLayoutParams(txtparams);
		h.tv.setGravity(Gravity.CENTER_HORIZONTAL);
		h.tv.setSingleLine(true);
		h.tv.setTextSize(10);

		TGL tog = model.get(posisi);
		h.tv.setText(tog.getStatus());
		h.iv.setImageDrawable(tog.getIcon());//loadImage(tog.bmp, h.iv, tog.type);
		v.addView(h.iv, 0);
		v.addView(h.tv, 1);
		v.setBackgroundDrawable(mTema.getICON(SBK.TOG_STATE));
		v.setTag(h);

		return v;
	}
/**
	private void loadImage(Bitmap bmp, ImageView iv, int type) {
		String key = String.valueOf(type);
		Bitmap me = getBitmapFromMemCache(key);
		if (me != null)
			iv.setImageBitmap(me);
		else
			new LoadBitmap(bmp, iv).execute(type);
	}

	private class LoadBitmap extends AsyncTask<Integer, Void, Bitmap> {
		private Bitmap bmp;
		private ImageView iv;

		public LoadBitmap(Bitmap bmp, ImageView iv) {
			this.iv = iv;
			this.bmp = bmp;
		}

		@Override
		protected void onPostExecute(Bitmap bmp) {
			// TODO Auto-generated method stub
			super.onPostExecute(bmp);
			iv.setImageBitmap(bmp);
			this.bmp.recycle();
		}

		@Override
		protected Bitmap doInBackground(Integer... params) {
			Bitmap bitmap = Bitmap.createScaledBitmap(bmp, size, size, false);
			addBitmapToMemoryCache(String.valueOf(params[0]), bitmap);
			return bitmap;
		}

	}

	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (getBitmapFromMemCache(key) == null) {
			mMemoryCache.put(key, bitmap);
		}
	}

	public Bitmap getBitmapFromMemCache(String key) {
		return mMemoryCache.get(key);
	}
*/
}
