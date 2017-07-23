package b.b;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import ccc.lineloc.R;

public class CopyOfLazyAdapter extends BaseAdapter {

	private Activity activity;
	private ArrayList<String> data;
	private static LayoutInflater inflater = null;
	public CopyOfImageLoader imageLoader;
	DisplayMetrics metrics;
	int h, w;

	public CopyOfLazyAdapter(Activity a, ArrayList<String> d) {
		activity = a;
		data = d;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = new CopyOfImageLoader(activity.getApplicationContext());
		metrics = new DisplayMetrics();
		a.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		h = metrics.heightPixels;
		w = metrics.widthPixels;
	}

	public int getCount() {
		return data.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.raw_item, null);

		ImageView image = (ImageView) vi.findViewById(R.id.iv1);
		image.setLayoutParams(new LayoutParams(174 * w / 480, 180 * h / 800));
		imageLoader.DisplayImage(data.get(position), image);
		return vi;
	}
}