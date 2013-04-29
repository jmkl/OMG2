package systemui.libsicon.adapter;

import systemui.libsicon.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SpinnerAdapter extends ArrayAdapter<String> {
	private String[] data;
	private int[] gambar;
	private Context context;

	public SpinnerAdapter(Context context, int textViewResourceId,
			String[] spinnermenu, int[] gambar) {
		super(context, textViewResourceId, spinnermenu);
		this.data = spinnermenu;
		this.gambar = gambar;
		this.context = context;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {

		LayoutInflater li = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = li.inflate(R.layout.omg_spinneritem, null);

		ImageView iv = (ImageView) v.findViewById(R.id.spinneritems_gambar);
		iv.setImageResource(gambar[position]);

		TextView tv = (TextView) v.findViewById(R.id.spinneritems_nama);
		tv.setPadding(10, 10, 10, 10);
		tv.setText(data[position]);
		return v;
	}

}