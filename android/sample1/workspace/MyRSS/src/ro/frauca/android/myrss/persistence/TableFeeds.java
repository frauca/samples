package ro.frauca.android.myrss.persistence;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TableFeeds {

	public static final String NAME = "feeds";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_URL = "url";

	// Database creation SQL statement
		private static final String DATABASE_CREATE = "create table " 
				+ NAME
				+ "(" 
				+ COLUMN_ID + " integer primary key autoincrement, " 
				+ COLUMN_NAME + " text not null, " 
				+ COLUMN_URL + " text not null" 
				+ ");";

		public static void onCreate(SQLiteDatabase database) {
			database.execSQL(DATABASE_CREATE);
		}

		public static void onUpgrade(SQLiteDatabase database, int oldVersion,
				int newVersion) {
			Log.w(TableFeeds.class.getName(), "Upgrading database from version "
					+ oldVersion + " to " + newVersion
					+ ", which will destroy all old data");
			database.execSQL("DROP TABLE IF EXISTS " + NAME);
			onCreate(database);
		}

}
