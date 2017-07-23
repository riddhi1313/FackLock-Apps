package ccc.lineloc;

import android.graphics.Bitmap;

public class Raw_Item {
	Bitmap image;

	public Raw_Item(Bitmap image) {
		super();
		this.image = image;
	}

	public Bitmap getImage() {

		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}
}
