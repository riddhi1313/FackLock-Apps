package ccc.messeningLock;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Environment;

public final class Constants {
	public static String[] IMAGES = null;
	Cursor cursor;
	Context c;
	String SD_CARD_ROOT;

	public Constants(Context applicationContext) {
		this.c = applicationContext;
		File mFile = Environment.getExternalStorageDirectory();
		SD_CARD_ROOT = mFile.toString();
		final List<String> tFileList = new ArrayList<String>();
		Resources resources = c.getResources();
		// array of valid image file extensions
		String[] imageTypes = resources.getStringArray(R.array.image);
		FilenameFilter[] filter = new FilenameFilter[imageTypes.length];

		int i = 0;

		for (final String type : imageTypes) {
			filter[i] = new FilenameFilter() {
				public boolean accept(File dir, String name) {
					if (!dir.getAbsolutePath().contains(".thumbnail")) {
						return name.endsWith("." + type);
					} else {
						return false;
					}
				}
			};

			i++;
		}

		FileUtils fileUtils = new FileUtils();
		File[] allMatchingFiles = fileUtils.listFilesAsArray(new File(
				SD_CARD_ROOT), filter, -1);
		for (File f : allMatchingFiles) {
			if (!f.getAbsolutePath().contains(".thumbnail")) {
				tFileList.add(f.getAbsolutePath());
			}

		}

		for (String s : tFileList) {
			if (s == null) {
				tFileList.remove(s);
			}
		}
		IMAGES = new String[tFileList.size()];
		IMAGES = tFileList.toArray(IMAGES);

	}

	public static class Config {
		public static final boolean DEVELOPER_MODE = false;
	}

}
