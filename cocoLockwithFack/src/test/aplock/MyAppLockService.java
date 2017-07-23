package test.aplock;

import java.util.List;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import ccc.cocolock.LockActivity_square;
import ccc.cocolock.Notif;
import ccc.cocolock.second_fackActivity;

public class MyAppLockService extends Service {
	ActivityManager manager;
	public static boolean flag;
	public static boolean isLauncher;
	public static boolean RESET_PASSWORD_BY_PATTERN;
	private String tag = "main";
	public boolean run = true;
	SharedPreferences prefs;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		final Class<?>[] cls = new Class[] { AppLockActivity.class,
				LockActivity_square.class, AppLockActivity.class,
				UnlockActivity.class };
		manager = (ActivityManager) getApplicationContext().getSystemService(
				Context.ACTIVITY_SERVICE);
		final String packName = getApplicationContext().getPackageName();

		th = new Thread(new Runnable() {

			@Override
			public void run() {
				while (run) {

					List<ActivityManager.RunningTaskInfo> list = manager
							.getRunningTasks(1);
					String current = list.get(0).topActivity.getPackageName();
					Log.d(tag, "p " + flag);
					if (!flag) {
						if (current.contains("com.instanza.cocovoice")) {
//							int no = prefs.getInt("lock_theme", 0);
//							Intent it = new Intent(getApplicationContext(),
//									cls[no]);
//							if (no == 2)
//								it.putExtra("isPhoto", false);
//							it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//							it.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION
//									| Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
//									| Intent.FLAG_ACTIVITY_NO_HISTORY);
//							getApplicationContext().startActivity(it);
							
							Intent it = new Intent(getApplicationContext(),
									second_fackActivity.class);
							it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							it.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION
									| Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
									| Intent.FLAG_ACTIVITY_NO_HISTORY);
							getApplicationContext().startActivity(it);
							
							
							flag = true;

						}
					}
					if (!current.contains("com.instanza.cocovoice")
							&& !current.contains(packName) 
							&& !current.contains("com.haibison")) {
						flag = false;
					}

					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						Log.d(tag, "Exce: " + e);
					}
				}
			}

		});
		th.start();
		super.onCreate();

	}

	public static Thread th;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(tag, "onStart");
		startBackground();
		return START_NOT_STICKY;
	}

	@Override
	public void onDestroy() {
		if (th != null) {
			run = false;
			th = null;
		}
		stopBackground();
		super.onDestroy();
	}

	public void startBackground() {
		isRunning = true;
		startForeground(Notif.notifId,
				Notif.getNotification(getApplicationContext()));
	}

	public static boolean isRunning;

	void stopBackground() {
		isRunning = false;
		stopForeground(true);
	}
}
