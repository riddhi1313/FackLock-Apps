package com.haibison.android.lockpattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.FrameLayout;

public class HomeKeyLocker {
	private OverlayDialog mOverlayDialog;

	public void lock(Activity paramActivity) {
		if (this.mOverlayDialog == null) {
			this.mOverlayDialog = new OverlayDialog(paramActivity);
			this.mOverlayDialog.show();
		}
	}

	public void unlock() {
		if (this.mOverlayDialog != null) {
			this.mOverlayDialog.dismiss();
			this.mOverlayDialog = null;
		}
	}

	private static class OverlayDialog extends AlertDialog {
		public OverlayDialog(Activity paramActivity) {
			super(paramActivity, 2131427330);
			WindowManager.LayoutParams localLayoutParams = getWindow()
					.getAttributes();
			localLayoutParams.type = 2003;
			localLayoutParams.dimAmount = 0.0F;
			localLayoutParams.width = 0;
			localLayoutParams.height = -50;
			localLayoutParams.gravity = 80;
			getWindow().setAttributes(localLayoutParams);
			getWindow().setFlags(524320, 16777215);
			setOwnerActivity(paramActivity);
			setCancelable(false);
		}

		public final boolean dispatchTouchEvent(MotionEvent paramMotionEvent) {
			return true;
		}

		protected final void onCreate(Bundle paramBundle) {
			super.onCreate(paramBundle);
			FrameLayout localFrameLayout = new FrameLayout(getContext());
			localFrameLayout.setBackgroundColor(0);
			setContentView(localFrameLayout);
		}
	}
}
