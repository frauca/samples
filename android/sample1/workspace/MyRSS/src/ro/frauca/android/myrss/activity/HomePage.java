package ro.frauca.android.myrss.activity;

import ro.frauca.android.myrss.R;
import ro.frauca.android.myrss.adapter.HomeFeedsAdapter;
import ro.frauca.android.myrss.persistence.DBHelper;
import ro.frauca.android.myrss.persistence.TableFeeds;
import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;


public class HomePage extends ListActivity implements AdapterView.OnItemLongClickListener{
	
	private DBHelper db;
	private Cursor mFeeds;
	
	private static final int FEED_CREATE=0;
    private static final int FEED_EDIT=1;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        try {
			db=new DBHelper(this);
		} catch (NameNotFoundException e) {
			Log.e(HomePage.class.getName(), "-could not open db");
			finish();
		}
        fillData();
        getListView().setOnItemLongClickListener(this );
    }
    
    public void fillData(){
    	// Get all of the rows from the database and create the item list
    	mFeeds = db.getTableFeeds().getAllFeeds(new String[]{TableFeeds.COLUMN_ID,TableFeeds.COLUMN_NAME,TableFeeds.COLUMN_URL});
        startManagingCursor(mFeeds);
        
        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[]{TableFeeds.COLUMN_NAME};
        
        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[]{R.id.feeds_row_name};
        
        // Now create a simple cursor adapter and set it to display
        HomeFeedsAdapter notes = 
        	    new HomeFeedsAdapter(this, R.layout.feeds_row, mFeeds, from, to);
        setListAdapter(notes);

    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater=getMenuInflater();
    	inflater.inflate(R.menu.home_menu, menu);
    	return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
    	switch (item.getItemId()) {
		case R.id.home_menu_add_item:
			createFeed();
			break;

		default:
			break;
		}
    	return super.onMenuItemSelected(featureId, item);
    }
    

    public void createFeed(){
    	Intent intent=new Intent(this,AddFeed.class);
    	startActivityForResult(intent, FEED_CREATE);
    }
    
    public void editFeed(long mRowId){
    	Intent intent=new Intent(this,AddFeed.class);
    	intent.putExtra(TableFeeds.COLUMN_ID, mRowId);
    	startActivityForResult(intent, FEED_CREATE);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	super.onActivityResult(requestCode, resultCode, intent);
        fillData();
    }

	@Override
	public boolean onItemLongClick(AdapterView<?> adapter, View view, int pos,
			long id) {
		editFeed(id);
		return true;
	}
	
	@Override
	protected void onResume() {
		//mFeeds.close();
		super.onResume();
	}
}