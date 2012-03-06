package ro.frauca.android.myrss.activity;

import ro.frauca.android.myrss.R;
import ro.frauca.android.myrss.persistence.DBHelper;
import ro.frauca.android.myrss.persistence.TableFeeds;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddFeed extends Activity implements  View.OnClickListener{
	
	private DBHelper db;
	protected Long mRowId;
	protected EditText mNameText;
	protected EditText mUrlText;
	protected Button btnSave;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_feet);
		try {
			db=new DBHelper(this);
		} catch (NameNotFoundException e) {
			Log.e(HomePage.class.getName(), "-could not open db");
			finish();
		}
		mNameText=(EditText)findViewById(R.id.add_feed_title);
		mUrlText=(EditText)findViewById(R.id.add_feed_url);
		btnSave=(Button)findViewById(R.id.btnAddFeed);
		btnSave.setOnClickListener(this);
		Intent intent=getIntent();
		mRowId = (savedInstanceState == null) ? null :
		    (Long) savedInstanceState.getSerializable(TableFeeds.COLUMN_ID);
		if (mRowId == null) {
		    Bundle extras = getIntent().getExtras();
		    mRowId = extras != null ? extras.getLong(TableFeeds.COLUMN_ID)
		                            : null;
		}
		populateFields();
		
	}
	@Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putSerializable(TableFeeds.COLUMN_ID, mRowId);
    }
	@Override
    protected void onPause() {
        super.onPause();
        saveState();
    }
	@Override
    protected void onResume() {
        super.onResume();
        populateFields();
    }
	private void saveState() {
        String name = mNameText.getText().toString();
        String url = mUrlText.getText().toString();

        if (mRowId == null) {
            long id = db.getTableFeeds().createFeed(name, url);
            if (id > 0) {
                mRowId = id;
            }
        } else{
        	db.getTableFeeds().updateFeed(mRowId, name,url);
        }
    }
	private void populateFields() {
	    if (mRowId != null) {
	        Cursor feed = db.getTableFeeds().fetchFeed(mRowId,new String[]{TableFeeds.COLUMN_NAME,TableFeeds.COLUMN_URL});
	        startManagingCursor(feed);
	        mNameText.setText(feed.getString(
	                    feed.getColumnIndexOrThrow(TableFeeds.COLUMN_NAME)));
	        mUrlText.setText(feed.getString(
	                feed.getColumnIndexOrThrow(TableFeeds.COLUMN_URL)));
	    }
	}
	@Override
	public void onClick(View v) {
		finish();
	}
	
}
