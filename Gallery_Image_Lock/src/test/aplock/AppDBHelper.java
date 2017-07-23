package test.aplock;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppDBHelper extends SQLiteOpenHelper {
	private HashMap<String, String> map;
	private static String DATABASE_NAME = "myDb";
	private static String TABLE_NAME = "images";
	private static String IMGNAME = "imgname";
	private static String REALPATH = "realpath";
	Context c;

	public AppDBHelper(Context context) {
		super(context, DATABASE_NAME, null, 1);
		c = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL("create table images (id integer AUTO_INCREMENT primary key,imgname text,realpath text)");

	}

	// CREATE TABLE Persons
	// (
	// ID int NOT NULL AUTO_INCREMENT,
	// LastName varchar(255) NOT NULL,
	// FirstName varchar(255),
	// Address varchar(255),
	// City varchar(255),
	// PRIMARY KEY (ID)
	// )

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS images");
		onCreate(db);

	}

	public boolean insertimg(String imgname, String realpath) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("imgname", imgname);
		values.put("realpath", realpath);
		db.insert(TABLE_NAME, null, values);
		//db.close();
		return true;
	}

	public boolean updateimg(String imgname, String realpath) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		// values.put(NAME, name);
		values.put(REALPATH, realpath);
		db.update(TABLE_NAME, values, "imgname = ? ", new String[] { imgname });
		return true;
	}

	public HashMap<String, String> getImages() {
		map = new HashMap<String, String>();
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cr = db.rawQuery("select * from images", null);
		cr.moveToFirst();
		while (cr.isAfterLast() == false) {
			map.put(cr.getString(cr.getColumnIndex(IMGNAME)),
					cr.getString(cr.getColumnIndex(REALPATH)));
			cr.moveToNext();
		}

		return map;
	}

	// public ArrayList<String> getApsHasStateTrue() {
	// ArrayList<String> list = new ArrayList<String>();
	// SQLiteDatabase db = this.getReadableDatabase();
	// Cursor cr = db.rawQuery("select * from images where state='" + 1 + "'",
	// null);
	// cr.moveToFirst();
	// while (cr.isAfterLast() == false) {
	// list.add(cr.getString(cr.getColumnIndex(NAME)));
	// cr.moveToNext();
	// }
	// return list;
	// }

	public String getRealpath(String imgname) {
		boolean flag = true;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor i = db.rawQuery("select realpath from images where imgname='"+imgname+"'", null);
		i.moveToFirst();
		String realpath = i.getString(i.getColumnIndex(REALPATH));
		// if (realpath > 0) {
		// flag = true;
		// } else {
		// flag = false;
		// }
		return realpath;
	}

	public boolean removeImage(String imgname) {
		SQLiteDatabase db = getWritableDatabase();

		try {
			db.delete(TABLE_NAME, "imgname=?", new String[] { imgname });
		} catch (Exception ex) {
			return false;
		}
		return true;
	}
}
