package test.aplock;

import java.util.List;

import ccc.instagramlock.LockActivity_square;
import ccc.instagramlock.R;
import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class FackActivity extends Activity implements OnClickListener{

	SharedPreferences prefs;
	private SearchView mSearchView;
	private Button mOpenButton;
	private Button mCloseButton;
	private TextView mStatusView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fackpage);

		prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		//Toast.makeText(getApplicationContext(),""+prefs.getBoolean("fack_chap", true),5000).show();
		Button more=(Button)findViewById(R.id.more);
		more.setOnClickListener(this);

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
		if(arg0.getId()==R.id.more);
		{
			final Class<?>[] cls = new Class[] { AppLockActivity.class,
					LockActivity_square.class, AppLockActivity.class,
					UnlockActivity.class };

			int no = prefs.getInt("lock_theme", 0);
			Intent it = new Intent(getApplicationContext(), cls[no]);

			if (no == 2)
				it.putExtra("isPhoto", false);

			it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			it.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION
					| Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
					| Intent.FLAG_ACTIVITY_NO_HISTORY);
			getApplicationContext().startActivity(it);
			finish();
		}
		
	}

}