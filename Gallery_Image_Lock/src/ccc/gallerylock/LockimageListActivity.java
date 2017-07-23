package ccc.gallerylock;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import test.aplock.AppDBHelper;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class LockimageListActivity extends Activity {
	private int count;
	private Bitmap[] thumbnails;
	private boolean[] thumbnailsselection;
	private String[] lockimg;
	private ImageAdapter imageAdapter;
	int layout;
	private String filepath;
	ImageLoader imageLoader = ImageLoader.getInstance();
	String imageUrls = "";
	DisplayImageOptions options;
	String[] splitUrls;
	Cursor cursor;
	String fileName="";
	AppDBHelper db;
	String unhideimg;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_lockimagelist);

		db = new AppDBHelper(getApplicationContext());
		if (!imageLoader.isInited()) {
			imageLoader.init(ImageLoaderConfiguration
					.createDefault(getApplicationContext()));
		}

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_stub)
				.showImageForEmptyUri(R.drawable.ic_empty)
				.showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		Bundle bundle = getIntent().getExtras();

		File mydir = getFilesDir();
		Log.d("main", "size: " + mydir.list().length);

		int size = mydir.list().length;
		this.thumbnailsselection = new boolean[size];

		for (String path : mydir.list()) {

			fileName = fileName + path + ",";
//Toast.makeText(getApplicationContext(), fileName, 5000).show();
		}
		Log.d("main", "FilePath: " + fileName);

		splitUrls = fileName.split(",");
		splitUrls = removeElement(splitUrls, "null");

		final GridView imagegrid = (GridView) findViewById(R.id.gridView1);
		imagegrid.setAdapter(new ImageAdapter(this, splitUrls));

		final Button selectBtn = (Button) findViewById(R.id.btn_apply);
		selectBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				final int len = thumbnailsselection.length;

				int cnt = 0;
				String selectImages = "";
				for (int i = 0; i < len; i++) {
					if (thumbnailsselection[i]) {
						cnt++;

						selectImages = splitUrls[i];
						fileName = new File(selectImages).getName();
						unhideimg = db.getRealpath(fileName);
						Log.i("path", unhideimg);
						db.removeImage(fileName);
					}
					File to = new File(unhideimg + "/" + fileName);
					File from = new File(getFilesDir() + "/" + fileName);
					try {
						FileUtils.moveFile(from, to);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					splitUrls = removeElement(splitUrls, selectImages);
					int sdkVersion = android.os.Build.VERSION.SDK_INT;
					if (sdkVersion > android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
						sendBroadcast(new Intent(
								Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri
										.parse(Environment
												.getExternalStorageDirectory()
												.getAbsolutePath())));
					} else {
						sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
								Uri.parse("file://"
										+ Environment
												.getExternalStorageDirectory())));
					}
					((BaseAdapter) imagegrid.getAdapter())
							.notifyDataSetChanged();

				}
				Toast.makeText(getApplicationContext(), "Image(s) Unlock",
						Toast.LENGTH_SHORT).show();
				Intent i=new Intent(getApplicationContext(),MainActivity.class);
				startActivity(i);

			}

		});
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(LockimageListActivity.this,
				MainActivity.class);

		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);

		startActivity(intent);
		finish();
	}

	public class ImageAdapter extends BaseAdapter {
		private Context context;
		private final String[] mobileValues;

		public ImageAdapter(Context context, String[] mobileValues) {
			this.context = context;
			this.mobileValues = mobileValues;
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View gridView;

			if (convertView == null) {

				gridView = new View(context);

				gridView = inflater.inflate(R.layout.raw_item_gallery_lock,
						null);
				ImageView imageView = (ImageView) gridView
						.findViewById(R.id.iv1);
				ToggleButton tb = (ToggleButton) gridView
						.findViewById(R.id.togglebtn);

				File filepath = context.getFileStreamPath(splitUrls[position]);
				Uri uri = Uri.parse(filepath.toString());
				imageView.setImageURI(uri);

				imageView.setId(position);
				tb.setId(position);

				tb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@SuppressLint("NewApi")
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub

						ToggleButton tb = (ToggleButton) buttonView;
						int id = tb.getId();

						if (tb.isChecked()) {
							tb.setChecked(true);
							thumbnailsselection[id] = true;
						} else {
							tb.setChecked(false);
							thumbnailsselection[id] = false;
						}

					}
				});

			} else {
				gridView = (View) convertView;
			}

			return gridView;
		}

		@Override
		public int getCount() {
			return mobileValues.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

	}

	public static String[] removeElement(String[] array, String key) {
		int found = 0;
		for (String s : array) {
			if (s.equals(key))
				found++;
		}
		if (found == 0)
			return array;
		String[] temp = new String[array.length - found];
		int x = 0;
		for (String s : array) {
			if (s.equals(key))
				continue;
			temp[x++] = s;
		}
		return temp;
	}
}
