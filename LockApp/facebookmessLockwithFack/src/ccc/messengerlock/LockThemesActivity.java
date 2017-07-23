package ccc.messengerlock;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import utils.ThemeModel;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import ccc.messengerlock.R;

import com.haibison.android.lockpattern.LockPatternActivity;
import com.haibison.android.lockpattern.util.Settings;

public class LockThemesActivity extends Activity implements
		OnItemClickListener, OnClickListener {
	GridView gridView;
	ArrayList<ThemeModel> gridArray = new ArrayList<ThemeModel>();
	LockThemeAdapter customGridAdapter;
	SharedPreferences prefs;
	Editor edit;
	String[] names;
	int w, h;
	String[] labels = new String[] { "Passcode Photo", "Flat Lock",
			"Plain Rounded", "Pattern Lock" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_gallery);
		Settings.Security.setAutoSavePattern(getApplicationContext(), true);
		DisplayMetrics outMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		w = outMetrics.widthPixels;
		h = outMetrics.heightPixels;

		Button more, rate;
		rate = (Button) findViewById(R.id.rate);
		more = (Button) findViewById(R.id.more);
		rate.setOnClickListener(this);
		more.setOnClickListener(this);

		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		edit = prefs.edit();
		AssetManager asset = getAssets();
		try {
			names = getImage();
		} catch (IOException e) {

			e.printStackTrace();
		}
		for (int i = 0; i < names.length; i++) {
			gridArray.add(new ThemeModel(labels[i], "themes/" + names[i]));
		}

		gridView = (GridView) findViewById(R.id.gridView1);
		gridView.setNumColumns(2);
		customGridAdapter = new LockThemeAdapter(LockThemesActivity.this,
				gridArray);
		gridView.setAdapter(customGridAdapter);
		gridView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		switch (arg2) {
		case 0:
			Intent i = new Intent(this, LockActivity.class);
			i.putExtra("isAnswered", true);
			i.putExtra("isFromReset", true);
			startActivityForResult(i, 0);
			break;
		case 1:
			Intent i1 = new Intent(this, LockActivity_square.class);
			i1.putExtra("isAnswered", true);
			i1.putExtra("isFromReset", true);
			startActivityForResult(i1, 1);
			break;
		case 2:
			Intent i2 = new Intent(this, LockActivity.class);
			i2.putExtra("isAnswered", true);
			i2.putExtra("isFromReset", true);
			i2.putExtra("isPhoto", false);
			startActivityForResult(i2, 2);
			break;
		case 3:

			Intent it = new Intent(LockPatternActivity.ACTION_CREATE_PATTERN,
					null, LockThemesActivity.this, LockPatternActivity.class);
			it.putExtra("enableBack", false);
			File f = new File(Environment.getExternalStorageDirectory()
					+ "/Android/data/" + getPackageName()
					+ "/Background/lock_bg.jpg");
			if (f.exists()) {
				it.putExtra("path", f.getAbsolutePath());
			}
			it.putExtra("vib_flag", prefs.getBoolean("vib_chap", false));
			it.putExtra("s_flag", prefs.getBoolean("sound_chap", true));
			it.putExtra("fack_flag", prefs.getBoolean("fack_chap", true));
			startActivityForResult(it, CREATE_PATTERN);

			break;
		}

	}

	private String[] getImage() throws IOException {
		AssetManager assetManager = getAssets();
		String[] files = assetManager.list("themes");
		return files;
	}

	int CREATE_PATTERN = 456789;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CREATE_PATTERN && resultCode == RESULT_OK) {
			edit.putInt("lock_theme", 3);
			edit.commit();
			Toast.makeText(LockThemesActivity.this, "Theme Applied",
					Toast.LENGTH_LONG).show();
			finish();

		} else if (requestCode == CREATE_PATTERN
				&& resultCode == RESULT_CANCELED) {
			Toast.makeText(LockThemesActivity.this,
					"Canceled!! pattern is not enabled", Toast.LENGTH_LONG)
					.show();
		} else if ((requestCode == 0 || requestCode == 1 || requestCode == 2)
				&& resultCode == RESULT_OK) {
			edit.putInt("lock_theme", requestCode);
			edit.commit();
			Toast.makeText(LockThemesActivity.this, "Theme Applied",
					Toast.LENGTH_LONG).show();
			finish();
		} else {
			Toast.makeText(LockThemesActivity.this,
					"Canceled!! new Lock Not applied.", Toast.LENGTH_LONG)
					.show();
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {

		case R.id.rate:

			String pckgname = getPackageName();
			Intent it_rate = new Intent(Intent.ACTION_VIEW,
					Uri.parse("https://play.google.com/store/apps/details?id="
							+ pckgname));
			startActivity(it_rate);

			break;
		case R.id.more:
			startActivity(new Intent(getApplicationContext(),
					ExtraActivity.class));
			break;
		}
	}

	@Override
	protected void onPause() {
		ActivityManager manager = (ActivityManager) getApplicationContext()
				.getSystemService(Context.ACTIVITY_SERVICE);
		String info = manager.getRunningTasks(1).get(0).baseActivity
				.getPackageName();

		if (!info.contains(getPackageName())) {
			Log.d("asd", "ppp " + info);
			finishAffinity();
		}
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		System.gc();
		super.onDestroy();
	}
}
