package com.haibison.android.lockpattern;

import static com.haibison.android.lockpattern.util.Settings.Display.METADATA_CAPTCHA_WIRED_DOTS;
import static com.haibison.android.lockpattern.util.Settings.Display.METADATA_MAX_RETRIES;
import static com.haibison.android.lockpattern.util.Settings.Display.METADATA_MIN_WIRED_DOTS;
import static com.haibison.android.lockpattern.util.Settings.Display.METADATA_STEALTH_MODE;
import static com.haibison.android.lockpattern.util.Settings.Security.METADATA_AUTO_SAVE_PATTERN;
import static com.haibison.android.lockpattern.util.Settings.Security.METADATA_ENCRYPTER_CLASS;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haibison.android.lockpattern.util.IEncrypter;
import com.haibison.android.lockpattern.util.InvalidEncrypterException;
import com.haibison.android.lockpattern.util.LoadingDialog;
import com.haibison.android.lockpattern.util.Settings;
import com.haibison.android.lockpattern.util.UI;
import com.haibison.android.lockpattern.widget.LockPatternUtils;
import com.haibison.android.lockpattern.widget.LockPatternView;
import com.haibison.android.lockpattern.widget.LockPatternView.Cell;
import com.haibison.android.lockpattern.widget.LockPatternView.DisplayMode;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class LockPatternActivity extends Activity {

	private static final String CLASSNAME = LockPatternActivity.class.getName();

	public static final String ACTION_CREATE_PATTERN = CLASSNAME
			+ ".create_pattern";

	public static final String ACTION_COMPARE_PATTERN = CLASSNAME
			+ ".compare_pattern";

	public static final String ACTION_VERIFY_CAPTCHA = CLASSNAME
			+ ".verify_captcha";
	boolean enableBack;
	public static final int RESULT_FAILED = RESULT_FIRST_USER + 1;

	public static final int RESULT_FORGOT_PATTERN = RESULT_FIRST_USER + 2;

	public static final String EXTRA_RETRY_COUNT = CLASSNAME + ".retry_count";

	public static final String EXTRA_THEME = CLASSNAME + ".theme";

	public static final String EXTRA_PATTERN = CLASSNAME + ".pattern";

	public static final String EXTRA_RESULT_RECEIVER = CLASSNAME
			+ ".result_receiver";

	public static final String EXTRA_PENDING_INTENT_OK = CLASSNAME
			+ ".pending_intent_ok";

	public static final String EXTRA_PENDING_INTENT_CANCELLED = CLASSNAME
			+ ".pending_intent_cancelled";
	public static final String EXTRA_INTENT_ACTIVITY_FORGOT_PATTERN = CLASSNAME
			+ ".intent_activity_forgot_pattern";

	private static enum ButtonOkCommand {
		CONTINUE, FORGOT_PATTERN, DONE
	}// ButtonOkCommand

	ViewStub stub1;
	View stubView;
	private static final long DELAY_TIME_TO_RELOAD_LOCK_PATTERN_VIEW = DateUtils.SECOND_IN_MILLIS;

	private int mMaxRetries, mMinWiredDots, mRetryCount = 0, mCaptchaWiredDots;
	private boolean mAutoSave, mStealthMode;
	private IEncrypter mEncrypter;
	private ButtonOkCommand mBtnOkCmd;
	private Intent mIntentResult;

	private TextView mTextInfo;
	private LockPatternView mLockPatternView;
	private View mFooter;
	private Button mBtnCancel;
	private Button mBtnConfirm;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		if (BuildConfig.DEBUG)
			Log.d(CLASSNAME, "ClassName = " + CLASSNAME);
		// this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		/*
		 * EXTRA_THEME
		 */

		// if (getIntent().hasExtra(EXTRA_THEME))
		// setTheme(getIntent().getIntExtra(EXTRA_THEME,
		// R.style.Alp_42447968_Theme_Dark));
		// setTheme(android.R.style.Theme_Black_NoTitleBar_Fullscreen);

		super.onCreate(savedInstanceState);

		loadSettings();

		mIntentResult = new Intent();
		setResult(RESULT_CANCELED, mIntentResult);

		initContentView();
	}// onCreate()

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		if (BuildConfig.DEBUG)
			Log.d(CLASSNAME, "onConfigurationChanged()");
		super.onConfigurationChanged(newConfig);
		initContentView();
	}// onConfigurationChanged()

	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if (keyCode == KeyEvent.KEYCODE_BACK
	// && ACTION_COMPARE_PATTERN.equals(getIntent().getAction())) {
	// /*
	// * Use this hook instead of onBackPressed(), because onBackPressed()
	// * is not available in API 4.
	// */
	// finishWithNegativeResult(RESULT_CANCELED);
	// return true;
	// }
	//
	// return super.onKeyDown(keyCode, event);
	// }// onKeyDown()

	/**
	 * Loads settings, either from manifest or {@link Settings}.
	 */
	private void loadSettings() {
		Bundle metaData = null;
		try {
			metaData = getPackageManager().getActivityInfo(getComponentName(),
					PackageManager.GET_META_DATA).metaData;
		} catch (NameNotFoundException e) {
			/*
			 * Never catch this.
			 */
			e.printStackTrace();
		}

		if (metaData != null && metaData.containsKey(METADATA_MIN_WIRED_DOTS))
			mMinWiredDots = Settings.Display.validateMinWiredDots(this,
					metaData.getInt(METADATA_MIN_WIRED_DOTS));
		else
			mMinWiredDots = Settings.Display.getMinWiredDots(this);

		if (metaData != null && metaData.containsKey(METADATA_MAX_RETRIES))
			mMaxRetries = 100;
		else
			mMaxRetries = 100;

		if (metaData != null
				&& metaData.containsKey(METADATA_AUTO_SAVE_PATTERN))
			mAutoSave = metaData.getBoolean(METADATA_AUTO_SAVE_PATTERN);
		else
			mAutoSave = Settings.Security.isAutoSavePattern(this);

		if (metaData != null
				&& metaData.containsKey(METADATA_CAPTCHA_WIRED_DOTS))
			mCaptchaWiredDots = Settings.Display.validateCaptchaWiredDots(this,
					metaData.getInt(METADATA_CAPTCHA_WIRED_DOTS));
		else
			mCaptchaWiredDots = Settings.Display.getCaptchaWiredDots(this);

		if (metaData != null && metaData.containsKey(METADATA_STEALTH_MODE))
			mStealthMode = metaData.getBoolean(METADATA_STEALTH_MODE);
		else
			mStealthMode = Settings.Display.isStealthMode(this);

		char[] encrypterClass;
		if (metaData != null && metaData.containsKey(METADATA_ENCRYPTER_CLASS))
			encrypterClass = metaData.getString(METADATA_ENCRYPTER_CLASS)
					.toCharArray();
		else
			encrypterClass = Settings.Security.getEncrypterClass(this);

		if (encrypterClass != null) {
			try {
				mEncrypter = (IEncrypter) Class.forName(
						new String(encrypterClass), false, getClassLoader())
						.newInstance();
			} catch (Throwable t) {
				throw new InvalidEncrypterException();
			}
		}
	}// loadSettings()

	private boolean s_flag, vib_flag, line_flag;

	/**
	 * Initializes UI...
	 */
	private void initContentView() {
		/*
		 * Save all controls' state to restore later.
		 */
		CharSequence infoText = mTextInfo != null ? mTextInfo.getText() : null;
		Boolean btnOkEnabled = mBtnConfirm != null ? mBtnConfirm.isEnabled()
				: null;
		LockPatternView.DisplayMode lastDisplayMode = mLockPatternView != null ? mLockPatternView
				.getDisplayMode() : null;
		List<Cell> lastPattern = mLockPatternView != null ? mLockPatternView
				.getPattern() : null;

		setContentView(R.layout.alp_42447968_lock_pattern_activity);
		stub1 = (ViewStub) findViewById(R.id.stub1);
		stubView = stub1.inflate();
		stubView.setVisibility(View.INVISIBLE);
		mlocker = new HomeKeyLocker();
		mlocker.lock(this);
		RelativeLayout rl_bg = (RelativeLayout) findViewById(R.id.rl_bg);
		RelativeLayout rl_text = (RelativeLayout) findViewById(R.id.rl_text);
		if (getIntent().getExtras() != null) {
			enableBack = getIntent().getBooleanExtra("enableBack", false);
			if (enableBack) {
				mlocker.unlock();
				mlocker = null;
			}
			String path = getIntent().getStringExtra("path");
			s_flag = getIntent().getBooleanExtra("s_flag", true);
			vib_flag = getIntent().getBooleanExtra("vib_flag", false);
			line_flag = getIntent().getBooleanExtra("line_flag", true);
			if (!getIntent().getBooleanExtra("topBg", true)) {
				rl_text.setBackgroundResource(R.drawable.top_bg_transparent);
			}
			Bitmap bg;
			if (path != null) {
				bg = BitmapFactory.decodeFile(path);
				rl_bg.setBackgroundDrawable(new BitmapDrawable(bg));
			} else {
				File f = new File(
						Environment.getExternalStorageDirectory()
								+ "/Android/data/ccc.ioslockscreen/Background/lock_bg.jpg");
				if (f.exists()) {
					rl_bg.setBackgroundDrawable(new BitmapDrawable(
							BitmapFactory.decodeFile(f.getAbsolutePath())));
				}
			}
		} else {
			File f = new File(Environment.getExternalStorageDirectory()
					+ "/Android/data/ccc.ioslockscreen/Background/lock_bg.jpg");
			if (f.exists()) {
				rl_bg.setBackgroundDrawable(new BitmapDrawable(BitmapFactory
						.decodeFile(f.getAbsolutePath())));
			}
		}
		UI.adjustDialogSizeForLargeScreens(getWindow());

		mTextInfo = (TextView) findViewById(R.id.alp_42447968_textview_info);
		mLockPatternView = (LockPatternView) findViewById(R.id.alp_42447968_view_lock_pattern);
		mLockPatternView.setVibANDsound_Flag(vib_flag, s_flag, line_flag);
		mFooter = findViewById(R.id.alp_42447968_viewgroup_footer);
		mBtnCancel = (Button) findViewById(R.id.alp_42447968_button_cancel);
		mBtnConfirm = (Button) findViewById(R.id.alp_42447968_button_confirm);

		/*
		 * LOCK PATTERN VIEW
		 */

		switch (getResources().getConfiguration().screenLayout
				& Configuration.SCREENLAYOUT_SIZE_MASK) {
		case Configuration.SCREENLAYOUT_SIZE_LARGE:
		case Configuration.SCREENLAYOUT_SIZE_XLARGE: {
			final int size = getResources().getDimensionPixelSize(
					R.dimen.alp_42447968_lockpatternview_size);
			LayoutParams lp = mLockPatternView.getLayoutParams();
			lp.width = size;
			lp.height = size;
			mLockPatternView.setLayoutParams(lp);

			break;
		}// LARGE / XLARGE
		}

		mLockPatternView.setInStealthMode(mStealthMode
				&& !ACTION_VERIFY_CAPTCHA.equals(getIntent().getAction()));
		mLockPatternView.setOnPatternListener(mLockPatternViewListener);
		if (lastPattern != null && lastDisplayMode != null
				&& !ACTION_VERIFY_CAPTCHA.equals(getIntent().getAction()))
			mLockPatternView.setPattern(lastDisplayMode, lastPattern);

		/*
		 * COMMAND BUTTONS
		 */

		if (ACTION_CREATE_PATTERN.equals(getIntent().getAction())) {
			mBtnCancel.setOnClickListener(mBtnCancelOnClickListener);
			mBtnConfirm.setOnClickListener(mBtnConfirmOnClickListener);

			mBtnCancel.setVisibility(View.VISIBLE);
			mFooter.setVisibility(View.VISIBLE);

			if (infoText != null)
				mTextInfo.setText(infoText);
			else
				mTextInfo
						.setText(R.string.alp_42447968_msg_draw_an_unlock_pattern);

			/*
			 * BUTTON OK
			 */
			if (mBtnOkCmd == null)
				mBtnOkCmd = ButtonOkCommand.CONTINUE;
			switch (mBtnOkCmd) {
			case CONTINUE:
				mBtnConfirm.setBackgroundResource(R.drawable.continuexml);
				break;
			case DONE:
				mBtnConfirm.setBackgroundResource(R.drawable.confirmxml);
				break;
			default:
				/*
				 * Do nothing.
				 */
				break;
			}
			if (btnOkEnabled != null) {
				mBtnConfirm.setAlpha(1.0f);
				mBtnConfirm.setEnabled(true);
			}
		}// ACTION_CREATE_PATTERN
		else if (ACTION_COMPARE_PATTERN.equals(getIntent().getAction())) {
			if (TextUtils.isEmpty(infoText))
				if (getIntent().getStringExtra("message") == null) {
					mTextInfo
							.setText(R.string.alp_42447968_msg_draw_pattern_to_unlock);
				} else
					mTextInfo.setText(R.string.draw_old_pattern);
			else
				mTextInfo.setText(infoText);
			if (getIntent().hasExtra(EXTRA_INTENT_ACTIVITY_FORGOT_PATTERN)) {
				mBtnConfirm.setOnClickListener(mBtnConfirmOnClickListener);
				mBtnConfirm.setBackgroundResource(R.drawable.forgotxml);
				mBtnConfirm.setText("Forgot pattern?");
				mBtnConfirm.setAlpha(1.0f);
				mBtnConfirm.setEnabled(true);

				mFooter.setVisibility(View.VISIBLE);
			}
		}// ACTION_COMPARE_PATTERN
		else if (ACTION_VERIFY_CAPTCHA.equals(getIntent().getAction())) {
			mTextInfo
					.setText(R.string.alp_42447968_msg_redraw_pattern_to_confirm);

			/*
			 * NOTE: EXTRA_PATTERN should hold a char[] array. In this case we
			 * use it as a temporary variable to hold a list of Cell.
			 */

			final ArrayList<Cell> pattern;
			if (getIntent().hasExtra(EXTRA_PATTERN)) {
				pattern = getIntent()
						.getParcelableArrayListExtra(EXTRA_PATTERN);
			} else {
				getIntent().putParcelableArrayListExtra(
						EXTRA_PATTERN,
						pattern = LockPatternUtils
								.genCaptchaPattern(mCaptchaWiredDots));

				mLockPatternView.setPattern(DisplayMode.Animate, pattern);
			}
		}
	}// initContentView()

	/**
	 * Compares {@code pattern} to the given pattern (
	 * {@link #ACTION_COMPARE_PATTERN}) or to the generated "CAPTCHA" pattern (
	 * {@link #ACTION_VERIFY_CAPTCHA}). Then finishes the activity if they
	 * match.
	 * 
	 * @param pattern
	 *            the pattern to be compared.
	 */
	private void doComparePattern(final List<Cell> pattern) {
		if (pattern == null)
			return;

		new LoadingDialog<Void, Void, Boolean>(this, false) {

			@Override
			protected Boolean doInBackground(Void... params) {
				if (ACTION_COMPARE_PATTERN.equals(getIntent().getAction())) {
					char[] currentPattern = getIntent().getCharArrayExtra(
							EXTRA_PATTERN);
					if (currentPattern == null)
						currentPattern = Settings.Security
								.getPattern(LockPatternActivity.this);
					if (currentPattern != null) {
						if (mEncrypter != null)
							return pattern.equals(mEncrypter.decrypt(
									LockPatternActivity.this, currentPattern));

						else
							return Arrays.equals(currentPattern,
									LockPatternUtils.patternToSha1(pattern)
											.toCharArray());
					}
				}// ACTION_COMPARE_PATTERN
				else if (ACTION_VERIFY_CAPTCHA.equals(getIntent().getAction())) {
					return pattern.equals(getIntent()
							.getParcelableArrayListExtra(EXTRA_PATTERN));
				}// ACTION_VERIFY_CAPTCHA

				return false;
			}// doInBackground()

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);

				if (result)
					finishWithResultOk(null);
				else {
					mRetryCount++;
					mIntentResult.putExtra(EXTRA_RETRY_COUNT, mRetryCount);

					if (mRetryCount >= mMaxRetries)
						finishWithNegativeResult(RESULT_FAILED);
					else {
						mLockPatternView.setDisplayMode(DisplayMode.Wrong);
						mTextInfo.setText(R.string.alp_42447968_msg_try_again);
						mLockPatternView.postDelayed(mLockPatternViewReloader,
								DELAY_TIME_TO_RELOAD_LOCK_PATTERN_VIEW);
					}
				}
			}// onPostExecute()

		}.execute();
	}// doComparePattern()

	/**
	 * Checks and creates the pattern.
	 * 
	 * @param pattern
	 *            the current pattern of lock pattern view.
	 */
	private void doCheckAndCreatePattern(final List<Cell> pattern) {
		if (pattern.size() < mMinWiredDots) {
			mLockPatternView.setDisplayMode(DisplayMode.Wrong);
			mTextInfo.setText(getResources().getQuantityString(
					R.plurals.alp_42447968_pmsg_connect_x_dots, mMinWiredDots,
					mMinWiredDots));
			mLockPatternView.postDelayed(mLockPatternViewReloader,
					DELAY_TIME_TO_RELOAD_LOCK_PATTERN_VIEW);
			return;
		}

		if (getIntent().hasExtra(EXTRA_PATTERN)) {
			/*
			 * Use a LoadingDialog because decrypting pattern might take time...
			 */
			new LoadingDialog<Void, Void, Boolean>(this, false) {

				@Override
				protected Boolean doInBackground(Void... params) {
					if (mEncrypter != null)
						return pattern.equals(mEncrypter.decrypt(
								LockPatternActivity.this, getIntent()
										.getCharArrayExtra(EXTRA_PATTERN)));
					else
						return Arrays.equals(
								getIntent().getCharArrayExtra(EXTRA_PATTERN),
								LockPatternUtils.patternToSha1(pattern)
										.toCharArray());
				}// doInBackground()

				@Override
				protected void onPostExecute(Boolean result) {
					super.onPostExecute(result);

					if (result) {
						mTextInfo
								.setText(R.string.alp_42447968_msg_your_new_unlock_pattern);
						mBtnConfirm.setAlpha(1.0f);
						mBtnConfirm.setEnabled(true);

					} else {
						stubView.setVisibility(View.VISIBLE);
						stubView.setOnTouchListener(null);
						(stubView.findViewById(R.id.button1))
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										stubView.setVisibility(View.GONE);

									}
								});

						mTextInfo
								.setText(R.string.alp_42447968_msg_redraw_pattern_to_confirm);
						mBtnConfirm.setAlpha(0.8f);
						mBtnConfirm.setEnabled(false);
						mLockPatternView.setDisplayMode(DisplayMode.Wrong);
						mLockPatternView.postDelayed(mLockPatternViewReloader,
								DELAY_TIME_TO_RELOAD_LOCK_PATTERN_VIEW);

					}
				}// onPostExecute()

			}.execute();
		} else {
			/*
			 * Use a LoadingDialog because encrypting pattern might take time...
			 */
			new LoadingDialog<Void, Void, char[]>(this, false) {

				@Override
				protected char[] doInBackground(Void... params) {
					return mEncrypter != null ? mEncrypter.encrypt(
							LockPatternActivity.this, pattern)
							: LockPatternUtils.patternToSha1(pattern)
									.toCharArray();
				}// onCancel()

				@Override
				protected void onPostExecute(char[] result) {
					super.onPostExecute(result);

					getIntent().putExtra(EXTRA_PATTERN, result);
					mTextInfo
							.setText(R.string.alp_42447968_msg_pattern_recorded);
					mBtnConfirm.setAlpha(1.0f);
					mBtnConfirm.setEnabled(true);
				}// onPostExecute()

			}.execute();
		}
	}// doCheckAndCreatePattern()

	/**
	 * Finishes activity with {@link Activity#RESULT_OK}.
	 * 
	 * @param pattern
	 *            the pattern, if this is in mode creating pattern. In any
	 *            cases, it can be set to {@code null}.
	 */
	private void finishWithResultOk(char[] pattern) {
		if (ACTION_CREATE_PATTERN.equals(getIntent().getAction()))
			mIntentResult.putExtra(EXTRA_PATTERN, pattern);
		else {
			/*
			 * If the user was "logging in", minimum try count can not be zero.
			 */
			mIntentResult.putExtra(EXTRA_RETRY_COUNT, mRetryCount + 1);
		}

		setResult(RESULT_OK, mIntentResult);

		/*
		 * ResultReceiver
		 */
		ResultReceiver receiver = getIntent().getParcelableExtra(
				EXTRA_RESULT_RECEIVER);
		if (receiver != null) {
			Bundle bundle = new Bundle();
			if (ACTION_CREATE_PATTERN.equals(getIntent().getAction()))
				bundle.putCharArray(EXTRA_PATTERN, pattern);
			else {
				/*
				 * If the user was "logging in", minimum try count can not be
				 * zero.
				 */
				bundle.putInt(EXTRA_RETRY_COUNT, mRetryCount + 1);
			}
			receiver.send(RESULT_OK, bundle);
		}

		/*
		 * PendingIntent
		 */
		PendingIntent pi = getIntent().getParcelableExtra(
				EXTRA_PENDING_INTENT_OK);
		if (pi != null) {
			try {
				pi.send(this, RESULT_OK, mIntentResult);
			} catch (Throwable t) {
				if (BuildConfig.DEBUG) {
					Log.e(CLASSNAME, "Error sending PendingIntent: " + pi);
					Log.e(CLASSNAME, ">>> " + t);
					t.printStackTrace();
				}
			}
		}

		finish();
	}// finishWithResultOk()

	/**
	 * Finishes the activity with negative result (
	 * {@link Activity#RESULT_CANCELED}, {@link #RESULT_FAILED} or
	 * {@link #RESULT_FORGOT_PATTERN}).
	 */
	private void finishWithNegativeResult(int resultCode) {
		if (ACTION_COMPARE_PATTERN.equals(getIntent().getAction()))
			mIntentResult.putExtra(EXTRA_RETRY_COUNT, mRetryCount);

		setResult(resultCode, mIntentResult);

		/*
		 * ResultReceiver
		 */
		ResultReceiver receiver = getIntent().getParcelableExtra(
				EXTRA_RESULT_RECEIVER);
		if (receiver != null) {
			Bundle resultBundle = null;
			if (ACTION_COMPARE_PATTERN.equals(getIntent().getAction())) {
				resultBundle = new Bundle();
				resultBundle.putInt(EXTRA_RETRY_COUNT, mRetryCount);
			}
			receiver.send(resultCode, resultBundle);
		}

		/*
		 * PendingIntent
		 */
		PendingIntent pi = getIntent().getParcelableExtra(
				EXTRA_PENDING_INTENT_CANCELLED);
		if (pi != null) {
			try {
				pi.send(this, resultCode, mIntentResult);
			} catch (Throwable t) {
				if (BuildConfig.DEBUG) {
					Log.e(CLASSNAME, "Error sending PendingIntent: " + pi);
					Log.e(CLASSNAME, ">>> " + t);
					t.printStackTrace();
				}
			}
		}

		finish();
	}// finishWithNegativeResult()

	/*
	 * LISTENERS
	 */

	private final LockPatternView.OnPatternListener mLockPatternViewListener = new LockPatternView.OnPatternListener() {

		@Override
		public void onPatternStart() {
			mLockPatternView.removeCallbacks(mLockPatternViewReloader);
			mLockPatternView.setDisplayMode(DisplayMode.Correct);

			if (ACTION_CREATE_PATTERN.equals(getIntent().getAction())) {
				mTextInfo
						.setText(R.string.alp_42447968_msg_release_finger_when_done);

				mBtnConfirm.setAlpha(0.8f);
				mBtnConfirm.setEnabled(false);
				if (mBtnOkCmd == ButtonOkCommand.CONTINUE)
					getIntent().removeExtra(EXTRA_PATTERN);
			}// ACTION_CREATE_PATTERN
			else if (ACTION_COMPARE_PATTERN.equals(getIntent().getAction())) {
				if (getIntent().getStringExtra("message") == null) {
					mTextInfo
							.setText(R.string.alp_42447968_msg_draw_pattern_to_unlock);
				} else {
					mTextInfo.setText(R.string.draw_old_pattern);

				}
			}// ACTION_COMPARE_PATTERN
			else if (ACTION_VERIFY_CAPTCHA.equals(getIntent().getAction())) {
				mTextInfo
						.setText(R.string.alp_42447968_msg_redraw_pattern_to_confirm);
			}// ACTION_VERIFY_CAPTCHA
		}// onPatternStart()

		@Override
		public void onPatternDetected(List<Cell> pattern) {
			if (ACTION_CREATE_PATTERN.equals(getIntent().getAction())) {
				doCheckAndCreatePattern(pattern);
			}// ACTION_CREATE_PATTERN
			else if (ACTION_COMPARE_PATTERN.equals(getIntent().getAction())) {
				doComparePattern(pattern);
			}// ACTION_COMPARE_PATTERN
			else if (ACTION_VERIFY_CAPTCHA.equals(getIntent().getAction())) {
				if (!DisplayMode.Animate.equals(mLockPatternView
						.getDisplayMode()))
					doComparePattern(pattern);
			}// ACTION_VERIFY_CAPTCHA
		}// onPatternDetected()

		@Override
		public void onPatternCleared() {
			mLockPatternView.removeCallbacks(mLockPatternViewReloader);

			if (ACTION_CREATE_PATTERN.equals(getIntent().getAction())) {
				mLockPatternView.setDisplayMode(DisplayMode.Correct);
				mBtnConfirm.setAlpha(0.8f);
				mBtnConfirm.setEnabled(false);
				if (mBtnOkCmd == ButtonOkCommand.CONTINUE) {
					getIntent().removeExtra(EXTRA_PATTERN);
					mTextInfo
							.setText(R.string.alp_42447968_msg_draw_an_unlock_pattern);
				} else
					mTextInfo
							.setText(R.string.alp_42447968_msg_redraw_pattern_to_confirm);
			}// ACTION_CREATE_PATTERN
			else if (ACTION_COMPARE_PATTERN.equals(getIntent().getAction())) {
				mLockPatternView.setDisplayMode(DisplayMode.Correct);
				if (getIntent().getStringExtra("message") == null) {
					mTextInfo
							.setText(R.string.alp_42447968_msg_draw_pattern_to_unlock);
				} else
					mTextInfo.setText(R.string.draw_old_pattern);

			}// ACTION_COMPARE_PATTERN
			else if (ACTION_VERIFY_CAPTCHA.equals(getIntent().getAction())) {
				mTextInfo
						.setText(R.string.alp_42447968_msg_redraw_pattern_to_confirm);
				List<Cell> pattern = getIntent().getParcelableArrayListExtra(
						EXTRA_PATTERN);
				mLockPatternView.setPattern(DisplayMode.Animate, pattern);
			}// ACTION_VERIFY_CAPTCHA
		}// onPatternCleared()

		@Override
		public void onPatternCellAdded(List<Cell> pattern) {
			// TODO Auto-generated method stub
		}// onPatternCellAdded()
	};// mLockPatternViewListener

	private final View.OnClickListener mBtnCancelOnClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			finishWithNegativeResult(RESULT_CANCELED);
		}// onClick()
	};// mBtnCancelOnClickListener

	private final View.OnClickListener mBtnConfirmOnClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			if (ACTION_CREATE_PATTERN.equals(getIntent().getAction())) {
				if (mBtnOkCmd == ButtonOkCommand.CONTINUE) {
					mBtnOkCmd = ButtonOkCommand.DONE;
					mLockPatternView.clearPattern();
					mTextInfo
							.setText(R.string.alp_42447968_msg_redraw_pattern_to_confirm);
					mBtnConfirm.setBackgroundResource(R.drawable.confirmxml);
					mBtnConfirm.setAlpha(0.8f);
					mBtnConfirm.setEnabled(false);
				} else {
					final char[] pattern = getIntent().getCharArrayExtra(
							EXTRA_PATTERN);
					if (mAutoSave)
						Settings.Security.setPattern(LockPatternActivity.this,
								pattern);
					finishWithResultOk(pattern);

				}
			}// ACTION_CREATE_PATTERN
			else if (ACTION_COMPARE_PATTERN.equals(getIntent().getAction())) {
				/*
				 * We don't need to verify the extra. First, this button is only
				 * visible if there is this extra in the intent. Second, it is
				 * the responsibility of the caller to make sure the extra is an
				 * Intent of Activity.
				 */
				Intent it = (Intent) getIntent().getParcelableExtra(
						EXTRA_INTENT_ACTIVITY_FORGOT_PATTERN);
				it.putExtra("isFromPattern", true);
				startActivity(it);

				// finishWithNegativeResult(RESULT_FORGOT_PATTERN);
			}// ACTION_COMPARE_PATTERN
		}// onClick()
	};// mBtnConfirmOnClickListener

	/**
	 * This reloads the {@link #mLockPatternView} after a wrong pattern.
	 */
	private final Runnable mLockPatternViewReloader = new Runnable() {

		@Override
		public void run() {
			mLockPatternView.clearPattern();
			mLockPatternViewListener.onPatternCleared();
		}// run()
	};// mLockPatternViewReloader

	@Override
	protected void onResume() {
		if (mlocker != null) {
			mlocker.lock(LockPatternActivity.this);
		}
		super.onResume();
	}

	protected void onPause() {
		if (mlocker != null)
			mlocker.unlock();
		super.onPause();

	};

	HomeKeyLocker mlocker;

	@Override
	public void onBackPressed() {
		// super.onBackPressed();
		Intent i = new Intent(Intent.ACTION_MAIN);
		i.addCategory(Intent.CATEGORY_HOME);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);
	}
}
