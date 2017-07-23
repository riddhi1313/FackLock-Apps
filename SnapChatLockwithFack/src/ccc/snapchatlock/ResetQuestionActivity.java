package ccc.snapchatlock;

import ccc.snapchatlock.R;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ResetQuestionActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_reset);
		Button que;
		que = (Button) findViewById(R.id.byQuestion);
		que.setOnClickListener(this);
		Button rate = (Button) findViewById(R.id.rate);
		Button more = (Button) findViewById(R.id.more);

		rate.setOnClickListener(this);
		more.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.byQuestion:
			finish();
			startActivity(new Intent(getApplicationContext(),
					ByQuestionAnswerActivity.class));
			break;
		case R.id.rate:

			Intent it_rate = new Intent(Intent.ACTION_VIEW,
					Uri.parse("https://play.google.com/store/apps/details?id="
							+ getPackageName()));
			startActivity(it_rate);
			break;
		case R.id.more:

			Intent it_more = new Intent(ResetQuestionActivity.this, ExtraActivity.class);
			startActivity(it_more);
			break;
		}
	}

}
