package ccc.gallerylock;

import java.io.File;

import test.aplock.MyAppLockService;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.haibison.android.lockpattern.LockPatternActivity;
import com.haibison.android.lockpattern.util.Settings;

public class ByQuestionAnswerActivity extends Activity implements
		OnClickListener {
	SharedPreferences prefs;
	Editor edit;
	boolean isFromReset, isFromPattern;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_by_question_answer);
		MyAppLockService.flag = false;
		Settings.Security.setAutoSavePattern(getApplicationContext(), true);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		if (getIntent().getExtras() != null) {
			isFromReset = getIntent().getBooleanExtra("isFromReset", false);
			isFromPattern = getIntent().getBooleanExtra("isFromPattern", false);
		}
		(findViewById(R.id.reset)).setOnClickListener(this);
		ans = (EditText) findViewById(R.id.answer);
		ans.requestFocus();
		String t = prefs.getString("answer", "def");
		if (t.equalsIgnoreCase("def")) {
			ans.setText("Que-Ans Has not been set.Try Email");
			ans.setFocusableInTouchMode(false);
			ans.setClickable(false);
			ans.setCursorVisible(false);
			ans.setFocusable(false);
		}

		TextView que = (TextView) findViewById(R.id.question);
		que.setText(prefs.getString("question",
				"what is your best friends name?"));
		Button rate = (Button) findViewById(R.id.rate);
		Button more = (Button) findViewById(R.id.more);

		rate.setOnClickListener(this);
		more.setOnClickListener(this);
	}

	EditText ans;
	int CREATE_PATTERN = 456789;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.reset:
			ans.clearFocus();
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(ans.getWindowToken(), 0);
			String a = ans.getText().toString();
			if (!a.equalsIgnoreCase("")) {
				if (a.equalsIgnoreCase(prefs.getString("answer", "default"))) {
					if (!isFromPattern) {
						Intent it = new Intent(getApplicationContext(),
								LockActivity.class);
						it.putExtra("isFromReset", isFromReset);
						it.putExtra("isAnswered", true);
						startActivity(it);
						finish();
					} else {
						Intent it = new Intent(
								LockPatternActivity.ACTION_CREATE_PATTERN,
								null, ByQuestionAnswerActivity.this,
								LockPatternActivity.class);
						it.putExtra("enableBack", false);
						File f = new File(
								Environment.getExternalStorageDirectory()
										+ "/Android/data/" + getPackageName()
										+ "/Background/lock_bg.jpg");
						if (f.exists()) {
							it.putExtra("path", f.getAbsolutePath());
						}
						it.putExtra("vib_flag",
								prefs.getBoolean("vib_chap", false));
						it.putExtra("s_flag",
								prefs.getBoolean("sound_chap", true));
						it.putExtra("fack_flag",
								prefs.getBoolean("fack_chap", true));
						it.putExtra("isFromReset", isFromReset);
						startActivityForResult(it, CREATE_PATTERN);
					}

				} else {
					Toast.makeText(getApplicationContext(), "Wrong Answer!",
							Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(getApplicationContext(),
						"Please Enter Answer First", Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.rate:

			Intent it_rate = new Intent(Intent.ACTION_VIEW,
					Uri.parse("https://play.google.com/store/apps/details?id="
							+ getPackageName()));
			startActivity(it_rate);
			break;
		case R.id.more:

			Intent it_more = new Intent(ByQuestionAnswerActivity.this,
					ExtraActivity.class);
			startActivity(it_more);
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CREATE_PATTERN && resultCode == RESULT_OK) {
			finish();
		} else {
			finish();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
