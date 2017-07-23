package test.aplock;

import java.lang.reflect.Method;

import ccc.gallerylock.ImageGridActivity;
import ccc.gallerylock.StarterActivity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class MyAppLockHideService extends Service {
	
	Boolean hide_flag;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		
		
		PackageManager p = getPackageManager();
		ComponentName componentName = new ComponentName(this, ccc.gallerylock.StarterActivity.class);
		p.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
		
		
		
//		Intent i=new Intent(MyAppLockHideService.this,StarterActivity.class);
//		startActivity(i);
		
		
	}


	
	
	

}
