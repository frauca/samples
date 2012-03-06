package ro.frauca.android.myrss.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class TableFeedsHelper {

	protected DBHelper dbH;
	protected SQLiteDatabase mDb;
	
	public TableFeedsHelper(DBHelper mDb){
		this.dbH=mDb;
		this.mDb=dbH.mDb;
	}
	
	
	/**
     * Return a Cursor over the list of all notes in the database(id and name)
     * 
     * @param cols cols to return in the cursor
     * @return Cursor over all notes
     */
    public Cursor getAllFeeds(String[] cols) {

        return mDb.query(TableFeeds.NAME, cols
        				, null, null, null, null, null);
    }

	
    /**
     * Create a new fedd using the name and url provided. If the feed is
     * successfully created return the new rowId for that feed, otherwise return
     * a -1 to indicate failure.
     * 
     * @param name the name of the note
     * @param url the url of the note
     * @return rowId or -1 if failed
     */
    public long createFeed(String name, String url) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(TableFeeds.COLUMN_NAME, name);
        initialValues.put(TableFeeds.COLUMN_URL, url);

        return mDb.insert(TableFeeds.NAME, null, initialValues);
    }

    /**
     * Return a Cursor positioned at the feed that matches the given rowId
     * 
     * @param rowId id of feed to retrieve
     * @param cols cols of the feed to return
     * @return Cursor positioned to matching feed, if found
     * @throws SQLException if note could not be found/retrieved
     */
    public Cursor fetchFeed(long rowId,String[] cols) throws SQLException {

        Cursor mCursor =

            mDb.query(true, TableFeeds.NAME, cols, TableFeeds.COLUMN_ID + "=" + rowId, null,
                    null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }
    
    /**
     * Update the feed using the details provided. The feed to be updated is
     * specified using the rowId, and it is altered to use the name and url
     * values passed in
     * 
     * @param rowId id of note to update
     * @param name value to set feed name to
     * @param url value to set fed url to
     * @return true if the note was successfully updated, false otherwise
     */
    public boolean updateFeed(long rowId, String name, String url) {
        ContentValues args = new ContentValues();
        args.put(TableFeeds.COLUMN_NAME, name);
        args.put(TableFeeds.COLUMN_URL, url);

        return updateFeed(rowId,args);
    }
    public boolean updateFeed(long rowId, ContentValues args) {
    	return mDb.update(TableFeeds.NAME, args, TableFeeds.COLUMN_ID + "=" + rowId, null) > 0;
    }
}
