package ccc.cocolock;

import test.aplock.MyAppLockService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class BootReciever extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(arg0);
		if (prefs.getBoolean("whatsapp", true)) {
			arg0.startService(new Intent(arg0, MyAppLockService.class));
		}

	}
}
