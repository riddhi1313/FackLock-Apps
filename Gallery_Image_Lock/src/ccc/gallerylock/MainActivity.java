package ccc.gallerylock;

import java.util.ArrayList;

import test.aplock.MyAppLockHideService;
import test.aplock.MyAppLockService;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements OnCheckedChangeListener,
		OnClickListener {
	ToggleButton b;
	SharedPreferences prefs;
	Editor edit;
	Button bg, rate, more;
	boolean bound;
	String imageNames[];
	AssetManager asset;
	ArrayList<Raw_Item> gridArray = new ArrayList<Raw_Item>();
	ToggleButton vib, sound, whatsapp;
	ToggleButton fackpage, hideicon;
	int w, h;
	TextView txtwhatsapp, belowwhatsapp, txtbackground, belowtaxtbackground,
			txtlocktheme, belowlocktheme, txtpasscode, belowtxtpasscode,
			txtsetting, belowtxtsetting, txtgallery, belowtxtgallery,
			txtchangepasscode, belowchangepasscode, txtsound, belowtxtsound,
			txtvibrate, belowtxtvibrate, txtfack, belowtxtfack, txthide,
			belowtxthide;
	LinearLayout background, lock_theme, pascode_theme, setting, gallery,
			gallery_layout, changepasscode, lock_img_layout;
	int theme_no;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_main);

		DisplayMetrics outMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		w = outMetrics.widthPixels;
		h = outMetrics.heightPixels;

		prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		edit = prefs.edit();

		if (prefs.getBoolean("hide_chap", false) == true) {
			PackageManager p = getPackageManager();
			ComponentName componentName = new ComponentName(this,
					ccc.gallerylock.StarterActivity.class);
			p.setComponentEnabledSetting(componentName,
					PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
					PackageManager.DONT_KILL_APP);
		} else {
			PackageManager p = getPackageManager();
			ComponentName componentName = new ComponentName(this,
					ccc.gallerylock.StarterActivity.class);
			p.setComponentEnabledSetting(componentName,
					PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
					PackageManager.DONT_KILL_APP);
		}

		Typeface face = Typeface.createFromAsset(getAssets(),
				"fonts/helvetica.otf");
		txtwhatsapp = (TextView) findViewById(R.id.txtwhatsapp);
		txtwhatsapp.setTypeface(face);

		belowwhatsapp = (TextView) findViewById(R.id.belowtxtwhatsapp);
		belowwhatsapp.setTypeface(face);

		txtbackground = (TextView) findViewById(R.id.txtbackground);
		txtbackground.setTypeface(face);

		belowtaxtbackground = (TextView) findViewById(R.id.belowtxtbackground);
		belowtaxtbackground.setTypeface(face);

		txtgallery = (TextView) findViewById(R.id.txtgallery);
		txtgallery.setTypeface(face);

		belowtxtgallery = (TextView) findViewById(R.id.belowtxtgallery);
		belowtxtgallery.setTypeface(face);

		txtlocktheme = (TextView) findViewById(R.id.txttheme);
		txtlocktheme.setTypeface(face);

		belowlocktheme = (TextView) findViewById(R.id.belowtxttheme);
		belowlocktheme.setTypeface(face);

		txtpasscode = (TextView) findViewById(R.id.txtpasscode);
		txtpasscode.setTypeface(face);

		belowtxtpasscode = (TextView) findViewById(R.id.txtpasscode);
		belowtxtpasscode.setTypeface(face);

		txtsetting = (TextView) findViewById(R.id.txtsetting);
		txtsetting.setTypeface(face);

		belowtxtsetting = (TextView) findViewById(R.id.belowtxtsetting);
		belowtxtsetting.setTypeface(face);

		txtchangepasscode = (TextView) findViewById(R.id.txtchangepasscode);
		txtchangepasscode.setTypeface(face);

		belowchangepasscode = (TextView) findViewById(R.id.belowtxtchangepasscode);
		belowchangepasscode.setTypeface(face);

		txtsound = (TextView) findViewById(R.id.txtsound);
		txtsound.setTypeface(face);

		belowtxtsound = (TextView) findViewById(R.id.belowtxtsound);
		belowtxtsound.setTypeface(face);

		txtvibrate = (TextView) findViewById(R.id.txtvibrate);
		txtvibrate.setTypeface(face);

		belowtxtvibrate = (TextView) findViewById(R.id.belowtxtvibrate);
		belowtxtvibrate.setTypeface(face);

		txtfack = (TextView) findViewById(R.id.txtfack);
		txtfack.setTypeface(face);

		belowtxtfack = (TextView) findViewById(R.id.belowtxtfack);
		belowtxtfack.setTypeface(face);

		txthide = (TextView) findViewById(R.id.txthide);
		txthide.setTypeface(face);

		belowtxthide = (TextView) findViewById(R.id.belowtxthide);
		belowtxthide.setTypeface(face);

		background = (LinearLayout) findViewById(R.id.relativeLayout1);
		background.setOnClickListener(this);

		lock_theme = (LinearLayout) findViewById(R.id.rl_lock_themes);
		lock_theme.setOnClickListener(this);

		pascode_theme = (LinearLayout) findViewById(R.id.rlllllllll_passcode_theme);
		pascode_theme.setOnClickListener(this);

		setting = (LinearLayout) findViewById(R.id.rl_settings);
		setting.setOnClickListener(this);

		gallery = (LinearLayout) findViewById(R.id.rl_gallary);
		gallery.setOnClickListener(this);

		gallery_layout = (LinearLayout) findViewById(R.id.gallery_layout);
		gallery_layout.setOnClickListener(this);

		lock_img_layout = (LinearLayout) findViewById(R.id.lock_img_layout);
		lock_img_layout.setOnClickListener(this);

		changepasscode = (LinearLayout) findViewById(R.id.rlllllllllll);
		changepasscode.setOnClickListener(this);

		// Button custom = (Button) findViewById(R.id.custom_wallpaper_btn);
		// custom.setOnClickListener(this);
		// Button settings = (Button) findViewById(R.id.btn_settings);
		// settings.setOnClickListener(this);
		// Button lock_themes = (Button) findViewById(R.id.btn_lock_themes);
		// lock_themes.setOnClickListener(this);
		// Button passcode_themes = (Button)
		// findViewById(R.id.btn_passcode_themes);
		// passcode_themes.setOnClickListener(this);

		theme_no = prefs.getInt("lock_theme", 0);
		if (theme_no != 0) {
			(findViewById(R.id.rlllllllll_passcode_theme))
					.setVisibility(View.GONE);
		}
		if (theme_no != 0 && theme_no != 2) {
			(findViewById(R.id.rl_gallary)).setVisibility(View.GONE);
			(findViewById(R.id.relativeLayout1)).setVisibility(View.GONE);
		}
		if (theme_no == 3) {
			(findViewById(R.id.rlllllllllll)).setVisibility(View.GONE);
		}
		asset = getAssets();
		// whatsapp = (ToggleButton) findViewById(R.id.btn_whatsapp);
		// whatsapp.setOnCheckedChangeListener(this);
		// whatsapp.setChecked(prefs.getBoolean("whatsapp", true));

		sound = (ToggleButton) findViewById(R.id.btn_sound);
		vib = (ToggleButton) findViewById(R.id.btn_vibrate);
		fackpage = (ToggleButton) findViewById(R.id.btn_fackpage);
		hideicon = (ToggleButton) findViewById(R.id.btn_hideicon);

		sound.setChecked(prefs.getBoolean("sound_chap", true));
		vib.setChecked(prefs.getBoolean("vib_chap", false));
		fackpage.setChecked(prefs.getBoolean("fack_chap", true));
		hideicon.setChecked(prefs.getBoolean("hide_chap", false));

		fackpage.setOnCheckedChangeListener(this);
		vib.setOnCheckedChangeListener(this);
		sound.setOnCheckedChangeListener(this);
		hideicon.setOnCheckedChangeListener(this);

		// bg = (Button) findViewById(R.id.background);
		// rate = (Button) findViewById(R.id.rate);
		more = (Button) findViewById(R.id.more);

		// bg.setOnClickListener(this);
		// rate.setOnClickListener(this);
		more.setOnClickListener(this);
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		switch (arg0.getId()) {
		// case R.id.btn_whatsapp:
		// if (arg1) {
		// edit.putBoolean("whatsapp", true);
		// edit.commit();
		// startService(new Intent(getApplicationContext(),
		// MyAppLockService.class));
		// } else {
		// edit.putBoolean("whatsapp", false);
		// edit.commit();
		// stopService(new Intent(getApplicationContext(),
		// MyAppLockService.class));
		// }
		// break;
		case R.id.btn_hideicon:
			if (arg1) {

				edit.putBoolean("hide_chap", true);
				edit.commit();

				PackageManager p = getPackageManager();
				ComponentName componentName = new ComponentName(this,
						ccc.gallerylock.StarterActivity.class);
				p.setComponentEnabledSetting(componentName,
						PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
						PackageManager.DONT_KILL_APP);
				// startService(new Intent(getApplicationContext(),
				// MyAppLockHideService.class));

				Intent i = new Intent(MainActivity.this, Hide_icon_popup.class);
				startActivity(i);
				finish();

			} else {
				edit.putBoolean("hide_chap", false);
				edit.commit();

				PackageManager p = getPackageManager();
				ComponentName componentName = new ComponentName(this,
						ccc.gallerylock.StarterActivity.class);
				p.setComponentEnabledSetting(componentName,
						PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
						PackageManager.DONT_KILL_APP);
				stopService(new Intent(getApplicationContext(),
						MyAppLockHideService.class));
			}
			break;
		case R.id.btn_fackpage:
			if (arg1) {

				edit.putBoolean("fack_chap", true);
				edit.commit();
			} else {
				edit.putBoolean("fack_chap", false);
				edit.commit();
			}
			// Toast.makeText(getApplicationContext(),""+prefs.getBoolean("fack_chap",
			// true),5000).show();
			break;
		case R.id.btn_sound:
			if (arg1) {

				edit.putBoolean("sound_chap", true);
				edit.commit();

			} else {
				edit.putBoolean("sound_chap", false);
				edit.commit();
			}

			break;
		case R.id.btn_vibrate:

			if (arg1) {

				edit.putBoolean("vib_chap", true);
				edit.commit();

			} else {
				edit.putBoolean("vib_chap", false);
				edit.commit();

			}

			break;
		}
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.gallery_layout:
			startActivity(new Intent(getApplicationContext(),
					ImageListForLockActivity.class));
			break;
		case R.id.lock_img_layout:
			startActivity(new Intent(getApplicationContext(),
					LockimageListActivity.class));
			break;
		case R.id.rl_gallary:
			startActivity(new Intent(getApplicationContext(),
					ImageGridActivity.class));
			break;
		case R.id.rlllllllll_passcode_theme:
			startActivity(new Intent(getApplicationContext(),
					SetImagesActivity.class));
			break;
		case R.id.rl_lock_themes:
			startActivity(new Intent(getApplicationContext(),
					LockThemesActivity.class));
			break;
		case R.id.relativeLayout1:
			Intent it_bg = new Intent(MainActivity.this, ScreenActivity.class);
			startActivity(it_bg);
			break;
		case R.id.rlllllllllll:
			if (theme_no == 2 || theme_no == 0) {
				Intent it = new Intent(getApplicationContext(),
						LockActivity.class);
				it.putExtra("isAnswered", false);
				it.putExtra("doOpenAct", false);
				it.putExtra("s_flag", prefs.getBoolean("sound_chap", true));
				it.putExtra("vib_flag", prefs.getBoolean("vib_chap", false));
				it.putExtra("hide_flag", prefs.getBoolean("hide_chap", false));

				startActivity(it);
			} else if (theme_no == 1) {
				Intent it = new Intent(getApplicationContext(),
						LockActivity_square.class);
				it.putExtra("isAnswered", false);
				it.putExtra("doOpenAct", false);
				it.putExtra("s_flag", prefs.getBoolean("sound_chap", true));
				it.putExtra("vib_flag", prefs.getBoolean("vib_chap", false));
				edit.putString("password", "" + 1);
				edit.commit();
				startActivity(it);

			}
			break;
		// case R.id.rate:
		// String pckgname = getPackageName();
		// Intent it_rate = new Intent(Intent.ACTION_VIEW,
		// Uri.parse("https://play.google.com/store/apps/details?id="
		// + pckgname));
		// startActivity(it_rate);
		//
		// break;
		case R.id.more:
			Intent it_more = new Intent(MainActivity.this, ExtraActivity.class);
			startActivity(it_more);
			break;
		case R.id.rl_settings:
			Intent it_settings = new Intent(MainActivity.this,
					SettingActivity.class);
			startActivity(it_settings);
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

	@Override
	protected void onResume() {
		if (rate != null) {
			onCreate(null);
		}
		super.onResume();
	}
}
