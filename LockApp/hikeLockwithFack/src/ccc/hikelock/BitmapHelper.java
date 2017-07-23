package ccc.hikelock;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.widget.Toast;

public class BitmapHelper {
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {

		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	Bitmap getBG(Context c) {

		return BitmapFactory.decodeFile(Environment
				.getExternalStorageDirectory()
				+ "/Android/data/"
				+ c.getPackageName() + "/Background/lock_bg.jpg");
	}

	public static void saveAssetBG(final Activity c, final String name) {
		DisplayMetrics outMetrics = new DisplayMetrics();
		c.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		final int w = outMetrics.widthPixels;
		final int h = outMetrics.heightPixels;
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				if (Looper.myLooper() == null) {
					Looper.prepare();
				}

				try {

					InputStream is = c.getAssets().open("set/" + name);
					File dir = new File(
							Environment.getExternalStorageDirectory()
									+ "/Android/data/" + c.getPackageName()
									+ "/Background");
					if (!dir.exists())
						dir.mkdirs();
					File f = new File(Environment.getExternalStorageDirectory()
							+ "/Android/data/" + c.getPackageName()
							+ "/Background/lock_bg.jpg");
					if (!f.exists())
						f.createNewFile();
					FileOutputStream fos = new FileOutputStream(f);
					BitmapFactory.Options opts = new BitmapFactory.Options();
					opts.inSampleSize = 1;
					opts.inJustDecodeBounds = true;
					Bitmap bmp = BitmapFactory.decodeStream(is, new Rect(0, 0,
							0, 0), opts);
					opts.inSampleSize = calculateInSampleSize(opts, w, h - 100);
					opts.inJustDecodeBounds = false;
					bmp = BitmapFactory.decodeStream(is, new Rect(0, 0, 0, 0),
							opts);
					bmp.compress(CompressFormat.JPEG, 100, fos);
					fos.close();
					is.close();
				} catch (IOException e) {
					Toast.makeText(
							c.getApplicationContext(),
							"Error setting image..Check sdcard and try again\n"
									+ e, Toast.LENGTH_LONG).show();
				}

				return null;
			}
		}.execute();
	}

	public static void saveGallaryBG(final Activity c, final String name) {
		DisplayMetrics outMetrics = new DisplayMetrics();
		c.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		final int w = outMetrics.widthPixels;
		final int h = outMetrics.heightPixels;
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				if (Looper.myLooper() == null) {
					Looper.prepare();
				}

				try {

					File dir = new File(
							Environment.getExternalStorageDirectory()
									+ "/Android/data/" + c.getPackageName()
									+ "/Background");
					if (!dir.exists())
						dir.mkdirs();
					File f = new File(Environment.getExternalStorageDirectory()
							+ "/Android/data/" + c.getPackageName()
							+ "/Background/lock_bg.jpg");
					if (!f.exists())
						f.createNewFile();
					FileOutputStream fos = new FileOutputStream(f);
					BitmapFactory.Options opts = new BitmapFactory.Options();
					opts.inSampleSize = 1;
					opts.inJustDecodeBounds = true;
					Bitmap bmp = BitmapFactory.decodeFile(name, opts);
					opts.inSampleSize = calculateInSampleSize(opts, w, h - 100);
					opts.inJustDecodeBounds = false;
					bmp = BitmapFactory.decodeFile(name, opts);
					bmp.compress(CompressFormat.JPEG, 100, fos);
					fos.close();
				} catch (IOException e) {
					Toast.makeText(
							c.getApplicationContext(),
							"Error setting image..Check sdcard and try again\n"
									+ e, Toast.LENGTH_LONG).show();
				}

				return null;
			}
		}.execute();
	}
}
