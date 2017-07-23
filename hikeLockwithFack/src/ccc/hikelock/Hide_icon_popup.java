package ccc.hikelock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;

public class Hide_icon_popup extends Activity implements OnClickListener{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		setContentView(R.layout.activity_hide_icon_popup);
		
		Button ok=(Button)findViewById(R.id.btnok);
		ok.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
		switch (arg0.getId()) {
		case R.id.btnok:
			Intent i=new Intent(Hide_icon_popup.this,MainActivity.class);
			startActivity(i);
			finish();
			break;

		default:
			break;
		}
		
	}

}
