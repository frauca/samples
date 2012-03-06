package ro.frauca.android.myrss.persistence;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "myrsss";
	
	protected TableFeedsHelper tableFeeds;
	protected SQLiteDatabase mDb;
	
	/**
	 * Contruct the table helber 
	 * The version code is get from the context 
	 * @see SQLiteOpenHelper#SQLiteOpenHelper(Context, String, CursorFactory, int)
	 * @param context 
	 * @throws NameNotFoundException if the version code could not be reach from context
	 */
	public DBHelper(Context context) throws NameNotFoundException {
		super(context, DATABASE_NAME, null, context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode);
		mDb=getWritableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		TableFeeds.onCreate(db);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		TableFeeds.onUpgrade(db, oldVersion, newVersion);

	}
	
	public synchronized TableFeedsHelper getTableFeeds(){
		if(tableFeeds==null){
			tableFeeds=new TableFeedsHelper(this);
		}
		return tableFeeds;
	}
	
}
