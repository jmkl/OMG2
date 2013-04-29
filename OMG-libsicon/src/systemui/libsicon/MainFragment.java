package systemui.libsicon;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class MainFragment extends SherlockFragmentActivity {
	@Override
	protected void onCreate(Bundle arg0) {
		setTheme(R.style.Theme_Sherlock);
		super.onCreate(arg0);
		setContentView(R.layout.fragmentsatu );
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.main_fragment, new PageFragment()).commit();

	}
}
