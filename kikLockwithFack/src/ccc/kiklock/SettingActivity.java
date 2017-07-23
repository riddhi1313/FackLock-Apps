package ccc.kiklock;

import ccc.kiklock.R;
import test.aplock.FackActivity;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;
import android.widget.ToggleButton;

public class SettingActivity extends Activity implements OnClickListener,
		OnCheckedChangeListener {
	SharedPreferences prefs;
	Editor edit;
	ToggleButton vib, sound, fackpage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		edit = prefs.edit();
		
		Button more, rate;
		Button change_passcode = (Button) findViewById(R.id.btn_change_passcode);
		change_passcode.setOnClickListener(this);
		
		sound = (ToggleButton) findViewById(R.id.btn_sound);
		vib = (ToggleButton) findViewById(R.id.btn_vibrate);
		fackpage = (ToggleButton) findViewById(R.id.btn_fackpage);
		
		sound.setChecked(prefs.getBoolean("sound_chap", true));
		vib.setChecked(prefs.getBoolean("vib_chap", false));
		fackpage.setChecked(prefs.getBoolean("fack_chap", true));

		fackpage.setOnCheckedChangeListener(this);
		vib.setOnCheckedChangeListener(this);
		sound.setOnCheckedChangeListener(this);

		rate = (Button) findViewById(R.id.rate);
		more = (Button) findViewById(R.id.more);

		rate.setOnClickListener(this);
		more.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rate:
			String pckgname = getPackageName();
			Intent it_rate = new Intent(Intent.ACTION_VIEW,
					Uri.parse("https://play.google.com/store/apps/details?id="
							+ pckgname));
			startActivity(it_rate);

			break;
		case R.id.btn_change_passcode:
			Intent it = new Intent(getApplicationContext(), LockActivity.class);
			it.putExtra("isAnswered", false);
			it.putExtra("doOpenAct", false);

			startActivity(it);
			break;
		case R.id.more:
			Intent it_more = new Intent(SettingActivity.this,
					ExtraActivity.class);
			startActivity(it_more);
			break;

		}
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		switch (arg0.getId()) {

		case R.id.btn_fackpage:
			if (arg1) {

				edit.putBoolean("fack_chap", true);
				edit.commit();
			} else {
				edit.putBoolean("fack_chap", false);
				edit.commit();
			}
			
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if(fackpage.isChecked())
		{
			edit.putBoolean("fack_chap",true);
		}
		else
		{
			edit.putBoolean("fack_chap",false);
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
}
