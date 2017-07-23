package ccc.kiklock;

import java.util.ArrayList;

import ccc.kiklock.R;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;
import b.b.CopyOfLazyAdapter;

public class ImageGridActivity extends Activity implements OnItemClickListener,
		OnClickListener {
	CopyOfLazyAdapter adapter;
	ArrayList<String> list;
	SharedPreferences prefs;
	Editor edit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_image_grid);

		prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		edit = prefs.edit();
		Button rate, more;
		rate = (Button) findViewById(R.id.rate);
		more = (Button) findViewById(R.id.more);

		rate.setOnClickListener(this);
		more.setOnClickListener(this);
		new imagenAMES().execute("abc");

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Toast.makeText(getApplicationContext(), "Background has been set.",
				Toast.LENGTH_LONG).show();
		BitmapHelper.saveGallaryBG(ImageGridActivity.this, list.get(arg2) + "");
		edit.commit();
		finish();
	}

	class imagenAMES extends AsyncTask<String, Void, String> {
		ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(ImageGridActivity.this);
			pd.setTitle("Loading");
			pd.setMessage("Please wait...");
			pd.show();

			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			
			list = getAllShownImagesPath(ImageGridActivity.this);
			
			return null;
		}

		public ArrayList<String> getAllShownImagesPath(Activity activity) {
			Uri uri;
			Cursor cursor;
			int column_index_data, column_index_folder_name;
			ArrayList<String> listOfAllImages = new ArrayList<String>();
			String absolutePathOfImage = null;
			uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

			String[] projection = { MediaColumns.DATA,
					MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

			cursor = activity.getContentResolver().query(uri, projection, null,
					null, null);

			column_index_data = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
			column_index_folder_name = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
			while (cursor.moveToNext()) {
				absolutePathOfImage = cursor.getString(column_index_data);

				listOfAllImages.add(absolutePathOfImage);
			}
			return listOfAllImages;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			pd.dismiss();
			GridView gv = (GridView) findViewById(R.id.gridView1);

			adapter = new CopyOfLazyAdapter(ImageGridActivity.this, list);
			gv.setAdapter(adapter);
			gv.setOnItemClickListener(ImageGridActivity.this);
		}
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
			Intent it_more = new Intent(ImageGridActivity.this,
					ExtraActivity.class);
			startActivity(it_more);
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
