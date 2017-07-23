package ccc.messengerlock;

import java.util.List;

import utils.ThemeModel;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Typeface;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;

import ccc.messengerlock.R;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

@SuppressLint("NewApi")
public class LockThemeAdapter extends BaseAdapter {

	LayoutInflater inflater;
	List<ThemeModel> items;
	Context c;
	LayoutParams params;
	int w, h;
	Typeface face;
	ImageLoader iloader;
	DisplayImageOptions options;

	public LockThemeAdapter(Context context, List<ThemeModel> items) {
		this.items = items;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		c = context;
		face = Typeface.createFromAsset(c.getAssets(), "fonts/h.ttf");
		WindowManager manager = (WindowManager) c
				.getSystemService(Context.WINDOW_SERVICE);

		Point p = new Point();
		Display d = manager.getDefaultDisplay();
		d.getRealSize(p);
		w = p.x;
		h = p.y;
		params = new LayoutParams((w / 2) - (15 * w / 720), (h / 2)
				- (140 * h / 1200));
		params.setMargins(1, 1, 2, 2);
		initImageLoader(c);
		options = new DisplayImageOptions.Builder().showImageOnLoading(0)
				.showImageForEmptyUri(0).showImageOnFail(0).cacheInMemory(true)
				.cacheOnDisc(true).considerExifParams(true)
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2).build();

	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	ImageView imageView;

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.raw_themes_item, null);
		}

		TextView text = (TextView) convertView.findViewById(R.id.textView1);
		text.setTypeface(face);
		String name = items.get(position).label;
		text.setText(name);
		imageView = (ImageView) convertView.findViewById(R.id.iv1);
		imageView.setLayoutParams(params);
		imageView.setScaleType(ScaleType.FIT_XY);

		ImageLoader.getInstance().displayImage(
				"assets://" + items.get(position).path, imageView, options);

		return convertView;
	}

	public static void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.discCacheSize(50 * 1024 * 1024)
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs()
				.build();
		ImageLoader.getInstance().init(config);
	}
}