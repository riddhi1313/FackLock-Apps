package ccc.snapchatlock;

import test.aplock.MyAppLockHideService;
import test.aplock.MyAppLockService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class Hide_icon_reciver extends BroadcastReceiver {

	Context ctx;
	PackageManager p;
	SharedPreferences prefs;
	Editor edit;
	String pass;
	Boolean hide_flag;

	@Override
	public void onReceive(Context context, Intent intent) {

		String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
		Log.d(Hide_icon_reciver.class.getSimpleName(), intent.toString());
		prefs = PreferenceManager.getDefaultSharedPreferences(context);

		pass = prefs.getString("password", "" + 123);
		hide_flag = prefs.getBoolean("hide_chap", false);
		// Toast.makeText(context,pass, Toast.LENGTH_LONG).show();

		String str_hide_pass = "*" + pass;
		if (hide_flag == true) {
			if (phoneNumber.equals(str_hide_pass)) {
				// Toast.makeText(context, phoneNumber,
				// Toast.LENGTH_LONG).show();
				context.startService(new Intent(context,
						MyAppLockHideService.class));

				Intent i = new Intent();
				i.setClassName("ccc.snapchatlock",
						"ccc.snapchatlock.MainActivity");
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(i);

			}

		}
	}
}
