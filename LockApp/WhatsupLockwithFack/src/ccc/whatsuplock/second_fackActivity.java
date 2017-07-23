package ccc.whatsuplock;

import test.aplock.AppLockActivity;
import test.aplock.FackActivity;
import test.aplock.UnlockActivity;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class second_fackActivity extends Activity {
	
	SharedPreferences prefs;
	final Class<?>[] cls = new Class[] { AppLockActivity.class,
			LockActivity_square.class, AppLockActivity.class,
			UnlockActivity.class };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_unlock);
	

	prefs = PreferenceManager
			.getDefaultSharedPreferences(getApplicationContext());
	
	if(prefs.getBoolean("fack_chap", true)==true)
	{
		Intent it = new Intent(getApplicationContext(),
				FackActivity.class);
		it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		getApplicationContext().startActivity(it);
		finish();
	}
	else
	{
		int no = prefs.getInt("lock_theme", 0);
		 Intent it = new
		 Intent(getApplicationContext(),
		 cls[no]);
		
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
