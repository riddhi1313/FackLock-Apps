package ccc.kiklock;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import ccc.kiklock.R;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import eu.janmuller.android.simplecropimage.CropImage;

public class SetPasscodeImagesActivity extends Activity implements OnClickListener {
	ArrayList<ImageButton> list = new ArrayList<ImageButton>();
	int w, h;
	int size;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_set_passcode_image);
		SharedPreferences mSharedPreference = PreferenceManager
				.getDefaultSharedPreferences(SetPasscodeImagesActivity.this);

		DisplayMetrics outMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		w = outMetrics.widthPixels;
		h = outMetrics.heightPixels;
		AssetManager asset = getAssets();
		String name = "";
		try {
			name = mSharedPreference.getString("name", asset.list("set")[0]);
		} catch (IOException e1) {
		
			e1.printStackTrace();
		}

		Bitmap icon = null;
		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;

			icon = BitmapFactory.decodeStream(asset.open("set/" + name),
					new Rect(0, 0, 0, 0), opts);
			opts.inSampleSize = BitmapHelper.calculateInSampleSize(opts, w,
					h - 100);
			opts.inJustDecodeBounds = false;
			icon = BitmapFactory.decodeStream(asset.open("set/" + name),
					new Rect(0, 0, 0, 0), opts);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		findViewById(R.id.rll_main).setBackgroundDrawable(
				new BitmapDrawable(icon));
		TextView text = (TextView) findViewById(R.id.textView3);
		text.setTypeface(Typeface.createFromAsset(asset, "fonts/h.ttf"));
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
			Bitmap bmp = BitmapFactory.decodeFile(getFilesDir()
					.getAbsolutePath() + "/dp" + ib.getTag() + ".png");
			if (bmp != null)
				ib.setImageBitmap(bmp);
		}
		imgBtnZero.setOnClickListener(this);
		imgBtnOne.setOnClickListener(this);
		imgBtnTwo.setOnClickListener(this);
		imgBtnThree.setOnClickListener(this);
		imgBtnFour.setOnClickListener(this);
		imgBtnFive.setOnClickListener(this);
		imgBtnSix.setOnClickListener(this);
		imgBtnSeven.setOnClickListener(this);
		imgBtnEight.setOnClickListener(this);
		imgBtnNine.setOnClickListener(this);
	}

	public static ImageButton ib;

	@Override
	public void onClick(View v) {
		ib = (ImageButton) v;
		selectImage();
	}

	private int REQUEST_CAMERA = 123123, SELECT_FILE = 456456,
			REQ_CROP_IMAGE = 777;

	private void selectImage() {
		final CharSequence[] items = { "Take Photo", "Choose from Gallary",
				"Cancel" };

		AlertDialog.Builder builder = new AlertDialog.Builder(
				SetPasscodeImagesActivity.this);
		builder.setTitle("Add Photo!");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (items[item].equals("Take Photo")) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					File f = new File(Environment.getExternalStorageDirectory()
							+ "/Android/data/" + getPackageName()
							+ "/tmp_image.png");

					File dir = new File(Environment
							.getExternalStorageDirectory()
							+ "/Android/data/"
							+ getPackageName());
					if (!dir.exists())
						dir.mkdirs();
					if (!f.exists()) {
						try {
							f.createNewFile();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
					startActivityForResult(intent, REQUEST_CAMERA);
				} else if (items[item].equals("Choose from Gallary")) {
					Intent intent = new Intent(
							Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					intent.setType("image/*");
					startActivityForResult(
							Intent.createChooser(intent, "Select File"),
							SELECT_FILE);
				} else if (items[item].equals("Cancel")) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}

	String tempPath;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK
				&& (requestCode == REQUEST_CAMERA || requestCode == SELECT_FILE)) {
			if (requestCode == REQUEST_CAMERA) {
				tempPath = Environment.getExternalStorageDirectory()
						+ "/Android/data/" + getPackageName()
						+ "/tmp_image.png";
			} else if (requestCode == SELECT_FILE) {
				Uri selectedImageUri = data.getData();
				String fromPath = getPath(selectedImageUri,
						SetPasscodeImagesActivity.this);
				File from = new File(fromPath);
				File to = new File(Environment.getExternalStorageDirectory()
						+ "/Android/data/" + getPackageName()
						+ "/tmp_image.png");
				File dir = new File(Environment.getExternalStorageDirectory()
						+ "/Android/data");
				if (!dir.exists())
					dir.mkdirs();

				if (!to.exists())
					try {
						to.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}

				try {
					FileUtils.copyFile(from, to);
				} catch (IOException e1) {
					Log.d("main", "Ex: " + e1);
					Toast.makeText(getApplicationContext(),
							"Exception During save...", Toast.LENGTH_LONG)
							.show();
				}

				tempPath = to.getAbsolutePath();
			}
			Intent it_crop = new Intent(getApplicationContext(),
					CropImage.class);
			DisplayMetrics outMetrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
			w = outMetrics.widthPixels;
			h = outMetrics.heightPixels;
			Log.d("main", "h:" + h + " W:" + w);
			if (h > 1000) {
				size = 185;
			} else if (h > 790) {
				size = 130;
			} else if (h > 470) {
				size = 80;
			} else {
				size = 165;
			}

			it_crop.putExtra(CropImage.IMAGE_PATH, tempPath);
			it_crop.putExtra(CropImage.CIRCLE_CROP, "true");
			it_crop.putExtra(CropImage.OUTPUT_X, size);
			it_crop.putExtra(CropImage.OUTPUT_Y, size);
			it_crop.putExtra(CropImage.ASPECT_X, 1);
			it_crop.putExtra(CropImage.ASPECT_Y, 1);

			startActivityForResult(it_crop, REQ_CROP_IMAGE);

		} else if (requestCode == REQ_CROP_IMAGE && data != null) {
			String path = data.getStringExtra(CropImage.IMAGE_PATH);
			if (path == null) {
				return;
			}
			
			File to = new File(getFilesDir().getAbsolutePath() + "/dp"
					+ ib.getTag() + ".png");
			if (!to.exists()) {
				try {
					to.createNewFile();
				} catch (IOException e) {
										e.printStackTrace();
				}
			}
			try {
				FileUtils.copyFile(new File(path), to);
			} catch (IOException e) {
				Toast.makeText(getApplicationContext(),
						"Error Saving Picture\n" + e.toString(),
						Toast.LENGTH_LONG).show();
			}
			Bitmap bmp = BitmapFactory.decodeFile(path);
			Log.d("main", "bmp height:" + bmp.getHeight() + "\n Bmp.width: "
					+ bmp.getWidth());
			try {
				FileOutputStream fos = new FileOutputStream(to);
				bmp.compress(CompressFormat.PNG, 100, fos);
				fos.close();

			} catch (FileNotFoundException e) {
				
				e.printStackTrace();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			ImageButton bb = (ImageButton) findViewById(ib.getId());
			bb.setImageBitmap(bmp);
		}
	}

	public String getPath(Uri uri, Activity activity) {
		String[] projection = { MediaColumns.DATA };
		Cursor cursor = activity
				.managedQuery(uri, projection, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	@Override
	protected void onDestroy() {
		System.gc();
		super.onDestroy();
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
