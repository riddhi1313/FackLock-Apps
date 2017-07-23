package ccc.bbmlock;

import java.util.ArrayList;

import ccc.bbmlock.R;

import test.aplock.MyAppLockService;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

public class LockActivity_square extends Activity implements OnClickListener {

	private EditText mEdtxtPassword;
	String password = "";
	SharedPreferences mSharedPreference;
	Editor mEditor;
	String pass;
	ArrayList<String> tmp = new ArrayList<String>();
	ImageButton delete;
	ToggleButton tb1, tb2, tb3, tb4;
	LinearLayout ll;
	SoundPool sp;
	boolean loaded;
	int soundID;
	float volume;
	boolean sound_flag, vib_flag, fack_flag, isFromResetPwd, doOpenAct,
			isfromreset, wasNotSet;;
	Vibrator vb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lock_square);
		ll = (LinearLayout) findViewById(R.id.llllllllllllllllll);
		Intent it = getIntent();
		if (it.getExtras() != null) {
			isAnswered = it.getBooleanExtra("isAnswered", false);
			isFromResetPwd = it.getBooleanExtra("isFromResetPwd", false);
			doOpenAct = it.getBooleanExtra("doOpenAct", false);
			isfromreset = it.getBooleanExtra("isFromReset", false);
		}
		vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		sp.setOnLoadCompleteListener(new OnLoadCompleteListener() {

			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId,
					int status) {
				loaded = true;
			}
		});
		soundID = sp.load(this, R.raw.click, 1);

		AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		float actualVolume = (float) audioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		float maxVolume = (float) audioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		volume = actualVolume / maxVolume;
		mSharedPreference = PreferenceManager
				.getDefaultSharedPreferences(LockActivity_square.this);
		sound_flag = mSharedPreference.getBoolean("sound_chap", true);
		vib_flag = mSharedPreference.getBoolean("vib_chap", false);
		fack_flag = mSharedPreference.getBoolean("fack_chap", true);
		pass = mSharedPreference.getString("password", "" + 123);
		mEditor = mSharedPreference.edit();

		if (pass.length() > 3) {

		} else {
			wasNotSet = true;

		}

		mEdtxtPassword = (EditText) findViewById(R.id.lock_editText1);
		mEdtxtPassword.setEnabled(false);

		ImageButton imgBtnOne = (ImageButton) findViewById(R.id.imageButton1);
		ImageButton imgBtnTwo = (ImageButton) findViewById(R.id.imageButton2);
		ImageButton imgBtnThree = (ImageButton) findViewById(R.id.imageButton3);
		ImageButton imgBtnFour = (ImageButton) findViewById(R.id.imageButton4);
		ImageButton imgBtnFive = (ImageButton) findViewById(R.id.imageButton5);
		ImageButton imgBtnSix = (ImageButton) findViewById(R.id.imageButton6);
		ImageButton imgBtnSeven = (ImageButton) findViewById(R.id.imageButton7);
		ImageButton imgBtnEight = (ImageButton) findViewById(R.id.imageButton8);
		ImageButton imgBtnNine = (ImageButton) findViewById(R.id.imageButton9);
		ImageButton imgBtnZero = (ImageButton) findViewById(R.id.imageButton0);
		ImageButton imgBtnforget = (ImageButton) findViewById(R.id.imageButton_camera);

		delete = (ImageButton) findViewById(R.id.delete);
		imgBtnforget.setOnClickListener(this);

		delete.setOnClickListener(this);
		imgBtnZero.setOnClickListener(btnClickListener);
		imgBtnOne.setOnClickListener(btnClickListener);
		imgBtnTwo.setOnClickListener(btnClickListener);
		imgBtnThree.setOnClickListener(btnClickListener);
		imgBtnFour.setOnClickListener(btnClickListener);
		imgBtnFive.setOnClickListener(btnClickListener);
		imgBtnSix.setOnClickListener(btnClickListener);
		imgBtnSeven.setOnClickListener(btnClickListener);
		imgBtnEight.setOnClickListener(btnClickListener);
		imgBtnNine.setOnClickListener(btnClickListener);
		tb1 = (ToggleButton) findViewById(R.id.imageView1);
		tb2 = (ToggleButton) findViewById(R.id.imageView2);
		tb3 = (ToggleButton) findViewById(R.id.imageView3);
		tb4 = (ToggleButton) findViewById(R.id.imageView4);
		final TextView t1, t2, t3, t4, t5, t6, t7, t8, t9, t0;
		t1 = (TextView) findViewById(R.id.textView1);
		t2 = (TextView) findViewById(R.id.textView2);
		t3 = (TextView) findViewById(R.id.textView3);
		t4 = (TextView) findViewById(R.id.textView4);
		t5 = (TextView) findViewById(R.id.textView5);
		t6 = (TextView) findViewById(R.id.textView6);
		t7 = (TextView) findViewById(R.id.textView7);
		t8 = (TextView) findViewById(R.id.textView8);
		t9 = (TextView) findViewById(R.id.textView9);
		t0 = (TextView) findViewById(R.id.textView0);
		Typeface face = Typeface.createFromAsset(getAssets(), "fonts/h.ttf");
		t1.setTypeface(face);
		t2.setTypeface(face);
		t3.setTypeface(face);
		t4.setTypeface(face);
		t5.setTypeface(face);
		t6.setTypeface(face);
		t7.setTypeface(face);
		t8.setTypeface(face);
		t9.setTypeface(face);
		t0.setTypeface(face);

		shake = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.shake);
		shake.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				InsertDot();
			}
		});

	}

	Animation shake;
	OnClickListener btnClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {

			switch (view.getId()) {
			case R.id.imageButton0:
				password += "0";
				tmp.add("0");
				mEdtxtPassword.setText(password);

				break;
			case R.id.imageButton1:
				tmp.add("1");
				password += "1";
				mEdtxtPassword.setText(password);

				break;
			case R.id.imageButton2:
				tmp.add("2");
				password += "2";
				mEdtxtPassword.setText(password);

				break;
			case R.id.imageButton3:
				tmp.add("3");
				password += "3";
				mEdtxtPassword.setText(password);

				break;
			case R.id.imageButton4:
				tmp.add("4");
				password += "4";
				mEdtxtPassword.setText(password);

				break;
			case R.id.imageButton5:
				tmp.add("5");
				password += "5";
				mEdtxtPassword.setText(password);

				break;
			case R.id.imageButton6:
				tmp.add("6");
				password += "6";
				mEdtxtPassword.setText(password);

				break;
			case R.id.imageButton7:
				tmp.add("7");
				password += "7";
				mEdtxtPassword.setText(password);

				break;
			case R.id.imageButton8:
				tmp.add("8");
				password += "8";
				mEdtxtPassword.setText(password);

				break;
			case R.id.imageButton9:
				tmp.add("9");
				password += "9";
				mEdtxtPassword.setText(password);

				break;
			}
			if (loaded && sound_flag) {
				sp.play(soundID, volume, volume, 1, 0, 1f);
			}
			InsertDot();
			exitActivity();

		}
	};

	void InsertDot() {

		switch (password.length()) {
		case 0:

			tb1.setChecked(false);
			tb2.setChecked(false);
			tb3.setChecked(false);
			tb4.setChecked(false);
			break;
		case 1:

			tb1.setChecked(true);
			tb2.setChecked(false);
			tb3.setChecked(false);
			tb4.setChecked(false);
			break;
		case 2:

			tb1.setChecked(true);
			tb2.setChecked(true);
			tb3.setChecked(false);
			tb4.setChecked(false);
			break;
		case 3:

			tb1.setChecked(true);
			tb2.setChecked(true);
			tb3.setChecked(true);
			tb4.setChecked(false);
			break;
		case 4:

			tb1.setChecked(true);
			tb2.setChecked(true);
			tb3.setChecked(true);
			tb4.setChecked(true);
			break;
		}
	}

	boolean isAnswered;

	private void exitActivity() {
		if (vib_flag) {
			vb.vibrate(20);
		}
		if (mEdtxtPassword.getText().length() == 4) {

			if (wasNotSet) {
				Intent itQue = new Intent(LockActivity_square.this,
						QuestionActivity.class);
				itQue.putExtra("pwd", mEdtxtPassword.getText().toString()
						.trim());
				itQue.putExtra("doOpenAct", false);
				startActivity(itQue);
				finish();
			} else {
				if (mEdtxtPassword.getText().toString().equals(pass)) {
					if (doOpenAct) {
						startActivity(new Intent(getApplicationContext(),
								MainActivity.class));
						overridePendingTransition(0, 0);
						setResult(RESULT_OK);
					}
					setResult(RESULT_OK);
					finish();
				}

				else {

					mEdtxtPassword.setText("");
					password = "";
					tmp.clear();
					ll.startAnimation(shake);
					vb.vibrate(300);

				}
			}
		} else {
			delete.setVisibility(View.VISIBLE);
		}

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.imageButton_camera:
			Intent it = new Intent(getApplicationContext(),
					ByQuestionAnswerActivity.class);
			it.putExtra("isFromReset", true);
			startActivity(it);
			MyAppLockService.flag = false;
			finish();
			break;
		case R.id.delete:
			if (!tmp.isEmpty()) {
				password = password.replaceFirst(".$", "");
				tmp.remove(tmp.size() - 1);
				InsertDot();

				break;
			}
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
	public void onBackPressed() {
		Intent it_Home = new Intent(Intent.ACTION_MAIN);
		it_Home.addCategory(Intent.CATEGORY_HOME);
		it_Home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(it_Home);
		super.onBackPressed();
	}

}