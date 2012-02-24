package com.rofc.android.samplelist;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class InterActiveActivity extends ListActivity {
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		// Create an array of Strings, that will be put to our ListActivity
		ArrayAdapter<Model> adapter = new InterActiveArrayAdapter(this,
				getModel());
		//The adapter holds the context menu
		setListAdapter(adapter);
		registerForContextMenu(getListView());
		
		/*
		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int pos, long id) {
				String text = (String) getListAdapter().getItem(pos);
				Toast.makeText(InterActiveActivity.this, "long " + text,
						Toast.LENGTH_SHORT).show();
				return true;
			}
		});
		*/
	}

	private List<Model> getModel() {
		List<Model> list = new ArrayList<Model>();
		list.add(get("Linux"));
		list.add(get("Windows7"));
		list.add(get("Suse"));
		list.add(get("Eclipse"));
		list.add(get("Ubuntu"));
		list.add(get("Solaris"));
		list.add(get("Android"));
		list.add(get("iPhone"));
		// Initially select one of the items
		list.get(1).setSelected(true);
		return list;
	}

	private Model get(String s) {
		return new Model(s);
	}

	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_listitem, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		Log.i("InterActiveActivity", "context menu");
	    AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	    switch (item.getItemId()) {
	        case R.id.item1:
	            Toast.makeText(this, "item 1 "+info.position, Toast.LENGTH_SHORT).show();
	            return true;
	        case R.id.item2:
	        	Toast.makeText(this, "item 1 "+info.position, Toast.LENGTH_SHORT).show();
	            return true;
	        default:
	            return super.onContextItemSelected(item);
	    }
	}
}
