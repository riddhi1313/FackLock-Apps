package test.aplock;

import java.util.List;

import ccc.gallerylock.LockActivity_square;
import ccc.gallerylock.R;
import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class FackActivity extends Activity implements OnClickListener,
		SearchView.OnQueryTextListener, SearchView.OnCloseListener {

	SharedPreferences prefs;
	private SearchView mSearchView;
	private Button mOpenButton;
	private Button mCloseButton;
	private TextView mStatusView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fackpage);

		prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		//Toast.makeText(getApplicationContext(),""+prefs.getBoolean("fack_chap", true),5000).show();

		mStatusView = (TextView) findViewById(R.id.status_text);
		mOpenButton = (Button) findViewById(R.id.btnserch);
		mOpenButton.setOnClickListener(this);
		Button fack = (Button) findViewById(R.id.btnfack);
		fack.setOnClickListener(this);
		ImageView image=(ImageView)findViewById(R.id.image1);
		image.setOnClickListener(this);
		ImageView back=(ImageView)findViewById(R.id.back);
		back.setOnClickListener(this);

		LinearLayout layout1 = (LinearLayout) findViewById(R.id.contact1);
		layout1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// v.setBackgroundColor(Color.BLUE);
			}
		});
		LinearLayout layout2 = (LinearLayout) findViewById(R.id.contact2);
		layout2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// v.setBackgroundColor(Color.BLUE);
			}
		});

		LinearLayout layout3 = (LinearLayout) findViewById(R.id.contact3);
		layout3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// v.setBackgroundColor(Color.BLUE);
			}
		});

		LinearLayout layout4 = (LinearLayout) findViewById(R.id.contact4);
		layout4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// v.setBackgroundColor(Color.BLUE);
			}
		});

		LinearLayout layout5 = (LinearLayout) findViewById(R.id.contact5);
		layout5.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// v.setBackgroundColor(Color.BLUE);
			}
		});

		LinearLayout layout6 = (LinearLayout) findViewById(R.id.contact6);
		layout6.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// v.setBackgroundColor(Color.BLUE);
			}
		});
		LinearLayout layout7 = (LinearLayout) findViewById(R.id.contact7);
		layout7.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// v.setBackgroundColor(Color.BLUE);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.searchview_in_menu, menu);
		 MenuItem searchItem = menu.findItem(R.id.actionsearch);
		mSearchView = (SearchView) menu.findItem(R.id.actionsearch)
				.getActionView();
//		mSearchView.performClick();
//	    mSearchView.requestFocus();
		setupSearchView(searchItem);

		return true;
	}

	 private void setupSearchView(MenuItem searchItem) {

	        if (isAlwaysExpanded()) {
	            mSearchView.setIconifiedByDefault(false);
	        } else {
	            searchItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM
	                    | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
	        }

	        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	        if (searchManager != null) {
	            List<SearchableInfo> searchables = searchManager.getSearchablesInGlobalSearch();

	            SearchableInfo info = searchManager.getSearchableInfo(getComponentName());
	            for (SearchableInfo inf : searchables) {
	                if (inf.getSuggestAuthority() != null
	                        && inf.getSuggestAuthority().startsWith("applications")) {
	                    info = inf;
	                }
	            }
	            mSearchView.setSearchableInfo(info);
	        }

	        mSearchView.setOnQueryTextListener(this);
	    }
	 
	  protected boolean isAlwaysExpanded() {
	        return false;
	    }


	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

		if (arg0.getId() == R.id.btnfack) {
			final Class<?>[] cls = new Class[] { AppLockActivity.class,
					LockActivity_square.class, AppLockActivity.class,
					UnlockActivity.class };

			int no = prefs.getInt("lock_theme", 0);
			Intent it = new Intent(getApplicationContext(), cls[no]);

			if (no == 2)
				it.putExtra("isPhoto", false);

			it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			it.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION
					| Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
					| Intent.FLAG_ACTIVITY_NO_HISTORY);
			getApplicationContext().startActivity(it);
			finish();
			MyAppLockService.flag = true;
		} else if (arg0.getId() == R.id.btnserch) {
			//Toast.makeText(getApplicationContext(),"open",5000).show();
			//mSearchView.setIconified(false);
			
			TextView chat=(TextView)findViewById(R.id.chat);
			chat.setVisibility(View.GONE);
			EditText search=(EditText)findViewById(R.id.status_text);
			search.setVisibility(View.VISIBLE);
			search.requestFocus();
			ImageView back=(ImageView)findViewById(R.id.back);
			back.setVisibility(View.VISIBLE);
			Button fack = (Button) findViewById(R.id.btnfack);
			fack.setVisibility(View.GONE);
			Button bs= (Button) findViewById(R.id.btnserch);
			
			
			
		}else if(arg0.getId()==R.id.back)
		{
			TextView chat=(TextView)findViewById(R.id.chat);
			chat.setVisibility(View.VISIBLE);
			EditText search=(EditText)findViewById(R.id.status_text);
			search.setVisibility(View.INVISIBLE);
			ImageView back=(ImageView)findViewById(R.id.back);
			back.setVisibility(View.GONE);
			Button fack = (Button) findViewById(R.id.btnfack);
			fack.setVisibility(View.VISIBLE);
			
		}
		else if(arg0.getId()==R.id.image1)
		{
			TextView chat=(TextView)findViewById(R.id.chat);
			chat.setVisibility(View.VISIBLE);
			EditText search=(EditText)findViewById(R.id.status_text);
			search.setVisibility(View.INVISIBLE);
			ImageView back=(ImageView)findViewById(R.id.back);
			back.setVisibility(View.GONE);
			Button fack = (Button) findViewById(R.id.btnfack);
			fack.setVisibility(View.VISIBLE);
			
		}


	}

	public boolean onQueryTextChange(String newText) {
		mStatusView.setText("Query = " + newText);
		return false;
	}

	public boolean onQueryTextSubmit(String query) {
		mStatusView.setText("Query = " + query + " : submitted");
		return false;
	}

	public boolean onClose() {
		mStatusView.setText("Closed!");
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		finish();
	}

}
