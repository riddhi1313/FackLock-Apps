package ccc.snapchatlock;


import java.util.ArrayList;

import ccc.snapchatlock.R;

import android.app.Activity;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class CustomGridViewAdapter extends ArrayAdapter<Raw_Item> {
	Context context;
	int layoutResourceId;
	ArrayList<Raw_Item> data = new ArrayList<Raw_Item>();
	int w, h;

	public CustomGridViewAdapter(Context context, int layoutResourceId,
			ArrayList<Raw_Item> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display d = manager.getDefaultDisplay();
		w = d.getWidth();
		h = d.getHeight();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		RecordHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new RecordHolder();
			holder.imageItem = (ImageView) row.findViewById(R.id.iv1);
			int CustomHeight = (h * 140) / 480;
			FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
					FrameLayout.LayoutParams.MATCH_PARENT, CustomHeight);
			holder.imageItem.setLayoutParams(layoutParams);

			row.setTag(holder);
		} else {
			holder = (RecordHolder) row.getTag();
		}

		Raw_Item item = data.get(position);
		holder.imageItem.setBackgroundResource(R.drawable.backg);
		holder.imageItem.setImageBitmap(item.getImage());
		return row;

	}

	static class RecordHolder {
		ImageView imageItem;

	}
}