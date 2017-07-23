package ccc.kiklock;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import test.aplock.UnlockActivity;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;

import ccc.kiklock.R;

import com.haibison.android.lockpattern.LockPatternActivity;

public class StarterActivity extends Activity {
	final Class<?>[] cls = new Class[] { StartActivity.class,
			LockActivity_square.class, StartActivity.class,
			UnlockActivity.class };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_starter);

		File dir = new File(Environment.getExternalStorageDirectory()
				+ "/Android/data/" + getPackageName() + "/Background");
		if (!dir.exists())
			dir.mkdirs();
		File f = new File(Environment.getExternalStorageDirectory()
				+ "/Android/data/" + getPackageName()
				+ "/Background/lock_bg.jpg");
		if (!f.exists()) {
			try {
				int h, w;
				DisplayMetrics metrics = new DisplayMetrics();
				getWindowManager().getDefaultDisplay().getMetrics(metrics);
				h = metrics.heightPixels;
				w = metrics.widthPixels;

				BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inJustDecodeBounds = false;

				Bitmap bmp = BitmapFactory.decodeStream(
						getAssets().open("set/" + getAssets().list("set")[0]),
						new Rect(0, 0, 0, 0), opts);
				opts.inSampleSize = BitmapHelper.calculateInSampleSize(opts, w,
						h - 100);
				opts.inJustDecodeBounds = false;
				bmp = BitmapFactory.decodeStream(
						getAssets().open("set/" + getAssets().list("set")[0]),
						new Rect(0, 0, 0, 0), opts);
				bmp.compress(CompressFormat.JPEG, 100, new FileOutputStream(f));
			} catch (IOException e) {
			}
		}
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		int theme = prefs.getInt("lock_theme", 0);

		if (theme != 3) {
			if (theme == 2) {
				Intent it = new Intent(getApplicationContext(), cls[theme]);
				it.putExtra("doOpenAct", true);
				it.putExtra("isPhoto", false);
				startActivity(it);
				overridePendingTransition(0, 0);
			} else {
				Intent it = new Intent(getApplicationContext(), cls[theme]);
				it.putExtra("doOpenAct", true);
				startActivity(it);
				overridePendingTransition(0, 0);
			}

			finish();
		} else {
			Intent it = new Intent(LockPatternActivity.ACTION_COMPARE_PATTERN,
					null, StarterActivity.this, LockPatternActivity.class);
			if (f.exists()) {
				it.putExtra("path", f.getAbsolutePath());
			}
			it.putExtra(
					LockPatternActivity.EXTRA_INTENT_ACTIVITY_FORGOT_PATTERN,
					new Intent(this, ByQuestionAnswerActivity.class));
			it.putExtra("enableBack", true);
			it.putExtra("topBg", false);
			it.putExtra("s_flag", prefs.getBoolean("sound_chap", true));
			it.putExtra("vib_flag", prefs.getBoolean("vib_chap", false));
			it.putExtra("fack_flag", prefs.getBoolean("fack_chap", true));
			startActivityForResult(it, 007);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && requestCode == 007) {
			startActivity(new Intent(getApplicationContext(),
					MainActivity.class));
			finish();
		} else {
			finish();
		}

	}
}
