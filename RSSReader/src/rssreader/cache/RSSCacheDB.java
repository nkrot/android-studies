package rssreader.cache;

import rssreader.rssfeed.RSSFeed;
import rssreader.rssfeed.RSSFeedEntry;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RSSCacheDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "rssreader.cache.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "rss_entry_table";

    // table fields, correspond to RSSFeedEntry fields
    public static final String UID = "_id";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String DATE = "date";
    public static final String IMAGEURL = "imageurl";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " ("
                    + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + TITLE + " VARCHAR(255), "
                    + DATE + " VARCHAR(255), "
                    + DESCRIPTION + " TEXT, "
                    + IMAGEURL + " VARCHAR(255)"
                    + ");";

    public RSSCacheDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
    }

    public int getRecordCount() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[] { UID },
                null, null, null, null, null);
        return cursor.getCount();
    }

    public RSSFeed getRSSFeed() {
        RSSFeed rssFeed = new RSSFeed();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] {
                UID, TITLE, DESCRIPTION, DATE, IMAGEURL },
                null, // The columns for the WHERE clause
                null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null // The sort order
                );

        while (cursor.moveToNext()) {
            rssFeed.add(getRSSFeedEntry(cursor));
        }

        cursor.close();
        db.close();

        return rssFeed;
    }

    private RSSFeedEntry getRSSFeedEntry(Cursor cursor) {
        RSSFeedEntry entry = new RSSFeedEntry();

        entry.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
        entry.setDate(cursor.getString(cursor.getColumnIndex(DATE)));
        entry.setDescription(cursor.getString(cursor.getColumnIndex(DESCRIPTION)));
        entry.setImageURL(cursor.getString(cursor.getColumnIndex(IMAGEURL)));

        return entry;
    }

    public void saveToCache(RSSFeed feed) {
        SQLiteDatabase db = getWritableDatabase();

        // purge the table
        // TODO: with this, the primary key will not be restarted from 1 but will be continued. any problem?
        db.delete(TABLE_NAME, null, null);

        for (RSSFeedEntry entry : feed) {
            ContentValues values = new ContentValues();
            values.put(TITLE, entry.getTitle());
            values.put(DESCRIPTION, entry.getDescription());
            values.put(DATE, entry.getDate());
            values.put(IMAGEURL, entry.getImageURL());
            db.insert(TABLE_NAME, null, values);
        }

        db.close();
    }
}
