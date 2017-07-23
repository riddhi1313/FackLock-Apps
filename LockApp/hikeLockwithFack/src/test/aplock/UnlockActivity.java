package test.aplock;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import ccc.hikelock.ByQuestionAnswerActivity;
import ccc.hikelock.R;

import com.haibison.android.lockpattern.LockPatternActivity;

public class UnlockActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_unlock);
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);

		Intent it = new Intent(LockPatternActivity.ACTION_COMPARE_PATTERN,
				null, UnlockActivity.this, LockPatternActivity.class);
		File f = new File(Environment.getExternalStorageDirectory()
				+ "/Android/data/" + getPackageName()
				+ "/Background/lock_bg.jpg");
		if (f.exists()) {
			it.putExtra("path", f.getAbsolutePath());
		}
		it.putExtra(LockPatternActivity.EXTRA_INTENT_ACTIVITY_FORGOT_PATTERN,
				new Intent(this, ByQuestionAnswerActivity.class));
		it.putExtra("enableBack", true);
		it.putExtra("topBg", false);
		it.putExtra("s_flag", prefs.getBoolean("sound_chap", true));
		it.putExtra("vib_flag", prefs.getBoolean("vib_chap", false));
		it.putExtra("fack_flag", prefs.getBoolean("fack_chap", true));
		startActivity(it);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 007 && resultCode == RESULT_OK) {
			MyAppLockService.flag = true;
			finish();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
