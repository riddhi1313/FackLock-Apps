package ccc.cocolock;

import java.util.ArrayList;

import ccc.cocolock.R;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class LockActivity extends Activity implements OnClickListener {

	private EditText mEdtxtPassword;
	String password = "";
	TextView txtAppName;
	SharedPreferences mSharedPreference;
	Editor mEditor;
	String pass;
	ArrayList<String> tmp = new ArrayList<String>();
	Button cancel, delete;
	ToggleButton tb1, tb2, tb3, tb4;
	View ll;
	SoundPool sp;
	boolean loaded, isAnswered, doOpenAct, isfromreset, isPhoto = true;
	int soundID;
	float volume;
	boolean sound_flag, vib_flag, fack_flag;
	Vibrator vb;
	Typeface face;
	Animation shake;
	String tag = "main";
	ArrayList<ImageButton> list = new ArrayList<ImageButton>();
	ArrayList<Bitmap> blist = new ArrayList<Bitmap>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_lock);
		Intent it = getIntent();
		if (it.getExtras() != null) {
			isAnswered = it.getBooleanExtra("isAnswered", false);
			doOpenAct = it.getBooleanExtra("doOpenAct", true);
			isfromreset = it.getBooleanExtra("isFromReset", false);
			isPhoto = it.getBooleanExtra("isPhoto", true);
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
		AssetManager asset = getAssets();
		mSharedPreference = PreferenceManager
				.getDefaultSharedPreferences(LockActivity.this);

		face = Typeface.createFromAsset(asset, "fonts/h.ttf");

		sound_flag = mSharedPreference.getBoolean("sound_chap", true);
		vib_flag = mSharedPreference.getBoolean("vib_chap", false);
		fack_flag = mSharedPreference.getBoolean("fack_chap", true);
		pass = mSharedPreference.getString("password", "" + 123);
		txtAppName = (TextView) findViewById(R.id.lock_textView1);

		txtAppName.setText("Choose your Passcode");

		mEditor = mSharedPreference.edit();

		mEdtxtPassword = (EditText) findViewById(R.id.lock_editText1);
		mEdtxtPassword.setEnabled(false);

		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				if (Looper.myLooper() == null)
					Looper.prepare();
				InitViewResources();
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				int i = 0;

				for (ImageButton ib : list) {

					ib.setImageBitmap(blist.get(i));

					i++;
				}
				super.onPostExecute(result);
			}
		}.execute();
		new AsyncTask<Void, Void, Void>() {
			Bitmap bmp;

			@Override
			protected Void doInBackground(Void... params) {
				if (Looper.myLooper() == null)
					Looper.prepare();

				bmp = BitmapFactory.decodeFile(Environment
						.getExternalStorageDirectory()
						+ "/Android/data/"
						+ getPackageName() + "/Background/lock_bg.jpg");
				return null;
			}

			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				(findViewById(R.id.rll_main)).setBackground(new BitmapDrawable(
						bmp));
			};
		}.execute();
	}

	private void InitViewResources() {
		pass = mSharedPreference.getString("password", "" + 123);
		final TextView txtAppName = (TextView) findViewById(R.id.lock_textView1);

		mEditor = mSharedPreference.edit();

		mEdtxtPassword = (EditText) findViewById(R.id.lock_editText1);
		mEdtxtPassword.setEnabled(false);

		ImageButton imgBtnOne = (ImageButton) findViewById(R.id.lock_btn_one);
		ImageButton imgBtnTwo = (ImageButton) findViewById(R.id.lock_btn_two);
		ImageButton imgBtnThree = (ImageButton) findViewById(R.id.lock_btn_three);
		ImageButton imgBtnFour = (ImageButton) findViewById(R.id.lock_btn_four);
		ImageButton imgBtnFive = (ImageButton) findViewById(R.id.lock_btn_five);
		ImageButton imgBtnSix = (ImageButton) findViewById(R.id.lock_btn_six);
		ImageButton imgBtnSeven = (ImageButton) findViewById(R.id.lock_btn_seven);
		ImageButton imgBtnEight = (ImageButton) findViewById(R.id.lock_btn_eight);
		ImageButton imgBtnNine = (ImageButton) findViewById(R.id.lock_btn_nine);
		ImageButton imgBtnZero = (ImageButton) findViewById(R.id.lock_btn_zero);

		list.add(imgBtnZero);
		list.add(imgBtnOne);
		list.add(imgBtnTwo);
		list.add(imgBtnThree);
		list.add(imgBtnFour);
		list.add(imgBtnFive);
		list.add(imgBtnSix);
		list.add(imgBtnSeven);
		list.add(imgBtnEight);
		list.add(imgBtnNine);
		for (ImageButton ib : list) {
			Bitmap bmp = null;
			if (isPhoto)
				bmp = BitmapFactory.decodeFile(getFilesDir().getAbsolutePath()
						+ "/dp" + ib.getTag() + ".png");
			if (bmp != null)
				blist.add(bmp);
			else {
				blist.add(BitmapFactory.decodeResource(getResources(),
						R.drawable.pin_number_btn_normal));
			}
		}
		final TextView lockText = (TextView) findViewById(R.id.lock_textView1);
		final TextView t1, t2, t3, t4, t5, t6, t7, t8, t9, t0;
		t1 = (TextView) findViewById(R.id.t1);
		t2 = (TextView) findViewById(R.id.t2);
		t3 = (TextView) findViewById(R.id.t3);
		t4 = (TextView) findViewById(R.id.t4);
		t5 = (TextView) findViewById(R.id.t5);
		t6 = (TextView) findViewById(R.id.t6);
		t7 = (TextView) findViewById(R.id.t7);
		t8 = (TextView) findViewById(R.id.t8);
		t9 = (TextView) findViewById(R.id.t9);
		t0 = (TextView) findViewById(R.id.t0);
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (pass.equalsIgnoreCase("null") || pass.length() < 4) {
					txtAppName.setText("Choose your Passcode");
				} else {
					txtAppName.setText("Enter Passcode");
				}
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
				lockText.setTypeface(face);
			}
		});

		Button forget = (Button) findViewById(R.id.emergency);
		cancel = (Button) findViewById(R.id.cancel);
		delete = (Button) findViewById(R.id.delete);
		cancel.setOnClickListener(LockActivity.this);
		delete.setOnClickListener(LockActivity.this);
		if (!isAnswered)
			forget.setOnClickListener(LockActivity.this);
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
		ll = (LinearLayout) findViewById(R.id.ll_dots);
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

	OnClickListener btnClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {

			switch (view.getId()) {
			case R.id.lock_btn_zero:
				password += "0";
				tmp.add("0");
				mEdtxtPassword.setText(password);

				break;
			case R.id.lock_btn_one:
				tmp.add("1");
				password += "1";
				mEdtxtPassword.setText(password);

				break;
			case R.id.lock_btn_two:
				tmp.add("2");
				password += "2";
				mEdtxtPassword.setText(password);

				break;
			case R.id.lock_btn_three:
				tmp.add("3");
				password += "3";
				mEdtxtPassword.setText(password);

				break;
			case R.id.lock_btn_four:
				tmp.add("4");
				password += "4";
				mEdtxtPassword.setText(password);

				break;
			case R.id.lock_btn_five:
				tmp.add("5");
				password += "5";
				mEdtxtPassword.setText(password);

				break;
			case R.id.lock_btn_six:
				tmp.add("6");
				password += "6";
				mEdtxtPassword.setText(password);

				break;
			case R.id.lock_btn_seven:
				tmp.add("7");
				password += "7";
				mEdtxtPassword.setText(password);

				break;
			case R.id.lock_btn_eight:
				tmp.add("8");
				password += "8";
				mEdtxtPassword.setText(password);

				break;
			case R.id.lock_btn_nine:
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
			cancel.setVisibility(View.VISIBLE);
			delete.setVisibility(View.GONE);
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

	private void exitActivity() {
		if (vib_flag) {
			vb.vibrate(20);
		}
		if (mEdtxtPassword.getText().length() == 4) {
			if (!isAnswered) {
				Intent itQue = new Intent(LockActivity.this,
						QuestionActivity.class);
				itQue.putExtra("pwd", mEdtxtPassword.getText().toString()
						.trim());
				itQue.putExtra("doOpenAct", doOpenAct);
				startActivity(itQue);
			} else {
				mEditor.putString("password", mEdtxtPassword.getText()
						.toString().trim());
				mEditor.commit();

				if (!isfromreset) {
					startActivity(new Intent(getApplicationContext(),
							MainActivity.class));
				}
				Toast.makeText(
						LockActivity.this,
						"Your Password saved. \n"
								+ mEdtxtPassword.getText().toString().trim(),
						500000).show();
			}
			setResult(RESULT_OK);
			finish();

		} else {
			mEditor.putBoolean("pin", false);
			mEditor.commit();
			cancel.setVisibility(View.GONE);
			delete.setVisibility(View.VISIBLE);
		}

	}

	@Override
	public void onBackPressed() {
		setResult(RESULT_CANCELED);
		super.onBackPressed();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancel:
			finish();
			break;
		case R.id.delete:
			password = password.replaceFirst(".$", "");
			tmp.remove(tmp.size() - 1);
			InsertDot();
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