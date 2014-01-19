package rssreader.cache;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import nasa.rss.pictureoftheday.RSSFeed;
import nasa.rss.pictureoftheday.RSSFeedEntry;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class RSSCache extends SQLiteOpenHelper {

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

    // SharedPreferences
    private static final String PREFS_NAME = "rssreader.prefs";
    private static final String PREFS_DOWNLOAD_TIME = "LastDownloadTime";
    public static final String DATE_FORMAT = "d-MM-yyyy HH:mm:ss z"; // as kept in shared prefs
    public static long EXPIRY_TIME_MILLISEC = 1000 * 60 * 5; // 5 min

    private Context context;

    // TODO: why is Context needed here?
    public RSSCache(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(SQL_CREATE_ENTRIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub
    }

    public boolean isUpToDate() {
        //Log.d("RSSCACHE", "isUpToDate called");

        Date lastDownloadTime = getDownloadTime();
        Date nowTime = new Date();
        long timeDiff = nowTime.getTime() - lastDownloadTime.getTime();
        Boolean upToDate = timeDiff < EXPIRY_TIME_MILLISEC;

        //SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
        //Log.d("RSSCACHE", "Now=" + df.format(nowTime) + " diff=" + String.valueOf(timeDiff));
        Log.d("RSSCACHE", "DB is up-to-date " + String.valueOf(upToDate));

        return upToDate;
    }

    public RSSFeed getRSSFeed() {
        Log.d("RSSCACHE", "retrieving data from DB");

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

    public RSSFeedEntry getRSSFeedEntry(Cursor cursor) {
        RSSFeedEntry entry = new RSSFeedEntry();

        entry.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
        entry.setDate(cursor.getString(cursor.getColumnIndex(DATE)));
        entry.setDescription(cursor.getString(cursor.getColumnIndex(DESCRIPTION)));
        entry.setImageURL(cursor.getString(cursor.getColumnIndex(IMAGEURL)));

        return entry;
    }

    // TODO: do it asynchronously
    public void saveToCache(RSSFeed feed) {
        Log.d("RSSCACHE", "saveToCache called");

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

        recordDownloadTime();
    }

    private Date getDownloadTime() {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        String downloadTime = settings.getString(PREFS_DOWNLOAD_TIME, null);
        //Log.d("RSSCACHE", PREFS_DOWNLOAD_TIME + "=" + downloadTime);
        Date date = new Date(0);
        if (downloadTime != null) {
            try {
                SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
                date = df.parse(downloadTime);
            } catch (ParseException e) {
                // TODO: react to the exception somehow
                e.printStackTrace();
            }
        }
        return date;
    }

    private void recordDownloadTime() {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(PREFS_DOWNLOAD_TIME, getTimeNow());
        editor.commit();
    }

    // TODO: maybe it is better to just keep time in milliseconds: Date#getTime
    private String getTimeNow() {
        Date now = new Date();
        SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        //Log.d("RSSCACHE", "getTimeNow=" + df.format(now));
        return df.format(now);
    }

    // TODO: save/access saved images here as well
}
