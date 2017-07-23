package ccc.gallerylock;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import test.aplock.AppDBHelper;
import test.aplock.lockimage;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class ImageListForLockActivity extends Activity {
	private int count;
	private Bitmap[] thumbnails;
	private boolean[] thumbnailsselection;
	private String[] arrPath;
	private ImageAdapter imageAdapter;
	int layout;
	private String filepath;
	ImageLoader imageLoader = ImageLoader.getInstance();
	String imageUrls = "";
	DisplayImageOptions options;
	String[] splitUrls;
	Cursor cursor;
	String fileName;
	AppDBHelper db;
	ArrayList<lockimage> main_list;
	private static String DATABASE_NAME = "myDb";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_imagelistforlock);
		
		db = new AppDBHelper(getApplicationContext());
		main_list = new ArrayList<lockimage>();

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

		final String[] columns = { MediaStore.Images.Media.DATA,
				MediaStore.Images.Media._ID };
		final String orderBy = MediaStore.Images.Media._ID;
		@SuppressWarnings("deprecation")
		Cursor imagecursor = managedQuery(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
				null, orderBy);

		this.count = imagecursor.getCount();
Log.i("cnt",""+count);
		this.arrPath = new String[this.count];
		this.thumbnailsselection = new boolean[this.count];
		for (int i = 0; i < this.count; i++) {
			imagecursor.moveToPosition(i);

			arrPath[i] = imagecursor.getString(imagecursor
					.getColumnIndex(MediaStore.Images.Media.DATA));

		}

		final GridView imagegrid = (GridView) findViewById(R.id.gridView1);
		imageAdapter = new ImageAdapter();
		imagegrid.setAdapter(imageAdapter);

		final Button selectBtn = (Button) findViewById(R.id.btn_apply);
		selectBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				final int len = thumbnailsselection.length;

				int cnt = 0;
				String selectImages = "";
				for (int i = 0; i < len; i++) {
					if (thumbnailsselection[i]) {
						selectImages = arrPath[i];
						fileName = new File(selectImages).getName();
						filepath = new File(selectImages).getParent();
						Log.i("path", filepath);
						main_list.add(new lockimage(fileName, filepath));
						db.insertimg(fileName, filepath);
					}
					

					File from = new File(selectImages);
					File to = new File(getFilesDir() + "/" + fileName);

					try {
						FileUtils.moveFile(from, to);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					 //arrPath = removeElement(arrPath,selectImages);
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

				Toast.makeText(getApplicationContext(), "Image(s) Lock",
						Toast.LENGTH_SHORT).show();
				Intent i=new Intent(getApplicationContext(),MainActivity.class);
				startActivity(i);
			}

		});
	}

	private File createFolders() {
		File mydir = getApplicationContext().getDir("LockImage",
				Context.MODE_PRIVATE); // Creating an internal dir;
		if (!mydir.exists()) {
			mydir.mkdirs();
		}
		return mydir;
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(ImageListForLockActivity.this,
				MainActivity.class);

		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);

		startActivity(intent);
		finish();
	}

	public class ImageAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public ImageAdapter() {
			mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public int getCount() {

			return count;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			final ViewHolder holder;

			ImageLoader imageLoader = ImageLoader.getInstance();
			imageLoader.init(ImageLoaderConfiguration
					.createDefault(getApplicationContext()));
			ImageView imageView = null;

			if (convertView == null) {
				holder = new ViewHolder();
				// convertView = mInflater.inflate(R.layout.raw_photo_grid,
				// null);

				convertView = getLayoutInflater().inflate(
						R.layout.raw_item_gallery_lock, parent, false);

				holder.imageview = (ImageView) convertView
						.findViewById(R.id.iv1);
				holder.togglebutton = (ToggleButton) convertView
						.findViewById(R.id.togglebtn);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.togglebutton.setId(position);
			holder.imageview.setId(position);
			holder.togglebutton
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

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
								int id1 = holder.imageview.getId();
							} else {
								tb.setChecked(false);
								thumbnailsselection[id] = false;
							}

						}
					});

			imageLoader.displayImage("file://" + Uri.parse(arrPath[position]),
					holder.imageview, options);

			// ImageLoader.getInstance().displayImage(
			// Uri.fromFile(
			// new File(cursor.getString(cursor.getColumnIndex(
			//
			// MediaStore.Images.Media.DATA)))).toString(),
			// holder.imageview,options);

			holder.togglebutton.setChecked(thumbnailsselection[position]);
			holder.id = position;
			return convertView;

		}

	}

	class ViewHolder {
		ImageView imageview;
		ToggleButton togglebutton;
		int id;

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
