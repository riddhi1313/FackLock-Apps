package ccc.gallerylock;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
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
import b.b.LockThemeAdapter;

public class ScreenActivity extends Activity implements OnItemClickListener,
		OnClickListener {
	GridView gridView;
	ArrayList<String> gridArray = new ArrayList<String>();
	LockThemeAdapter customGridAdapter;
	SharedPreferences prefs;
	Editor edit;
	String[] names;
	int w, h;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_screen);
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
			gridArray.add("set/" + names[i]);
		}

		gridView = (GridView) findViewById(R.id.gridView1);
		customGridAdapter = new LockThemeAdapter(ScreenActivity.this, gridArray);
		gridView.setAdapter(customGridAdapter);
		gridView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
			long arg3) {
		edit.putString("name", names[arg2]);
		// edit.putBoolean("wallpaper_from", false);
		// edit.putBoolean("wallpaper_asset", false);
		edit.commit();
		BitmapHelper.saveAssetBG(ScreenActivity.this, names[arg2]);
		finish();
	}

	private String[] getImage() throws IOException {

		AssetManager assetManager = getAssets();
		String[] files = assetManager.list("set");
		return files;

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
