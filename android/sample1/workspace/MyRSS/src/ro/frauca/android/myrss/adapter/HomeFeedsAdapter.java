package ro.frauca.android.myrss.adapter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import ro.frauca.android.myrss.R;
import ro.frauca.android.myrss.persistence.TableFeeds;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;
import com.google.code.rome.android.repackaged.com.sun.syndication.fetcher.FeedFetcher;
import com.google.code.rome.android.repackaged.com.sun.syndication.fetcher.FetcherException;
import com.google.code.rome.android.repackaged.com.sun.syndication.fetcher.impl.HttpURLFeedFetcher;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.FeedException;

public class HomeFeedsAdapter extends SimpleCursorAdapter {
	Handler handler=new Handler();
	
	public class ViewTag{
		TextView text;
		TextView counter;
		SyndFeed feed;
	}

	public HomeFeedsAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to) {
		super(context, layout, c, from, to);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor)
	{
		inflateRow(view, cursor);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent)
	{
		LayoutInflater inflater=((Activity)context).getLayoutInflater();
		View row = inflater.inflate(R.layout.feeds_row, null);
		ViewTag tag=new ViewTag();
		tag.text=(TextView)row.findViewById(R.id.feeds_row_name);
		tag.counter=(TextView)row.findViewById(R.id.feeds_row_counter);
		row.setTag(tag);
		inflateRow(row, cursor);
		return row;
	}
	
	public void inflateRow(View row, Cursor cursor){
		
		ViewTag tag = (ViewTag)row.getTag();
		tag.text.setText(cursor.getString(cursor.getColumnIndex(TableFeeds.COLUMN_NAME)));
		new CounterRefresher(row).execute(cursor.getString(cursor.getColumnIndex(TableFeeds.COLUMN_URL)));
	}

	public class CounterRefresher extends AsyncTask<String, Void, SyndFeed>{
		View row;
		
		public CounterRefresher(View row) {
			this.row=row;
		}
		@Override
		protected SyndFeed doInBackground(String... params) {
			String feedUrl = params[0];
			FeedFetcher feedFetcher=new HttpURLFeedFetcher();
			try {
				Log.i(getClass().getName(), "parsing "+feedUrl);
				return feedFetcher.retrieveFeed( new URL( feedUrl ) );
			} catch (IllegalArgumentException e) {
				Log.e(getClass().getName(), "could not parse url",e);
			} catch (MalformedURLException e) {
				Log.e(getClass().getName(), "malformed url url",e);
			} catch (IOException e) {
				Log.e(getClass().getName(), "could not get url ",e);
			} catch (FeedException e) {
				Log.e(getClass().getName(), "feed exceptio url ",e);
			} catch (FetcherException e) {
				Log.e(getClass().getName(), "feddxer exeption url ",e);
			}
			return null;
		}
		@Override
		protected void onPostExecute(SyndFeed result) {
			if(result!=null){
				ViewTag tag=(ViewTag)row.getTag();
				tag.counter.setText(String.valueOf(result.getEntries().size()));
				super.onPostExecute(result);
			}
			
		}
	}
}
