package rssreader.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class RSSCache extends SQLiteOpenHelper {
    private static final String LOGTAG = "RSSCACHE";

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
        //Log.d(LOGTAG, "isUpToDate called");

        Date lastDownloadTime = getDownloadTime();
        Date nowTime = new Date();
        long timeDiff = nowTime.getTime() - lastDownloadTime.getTime();
        Boolean upToDate = timeDiff < EXPIRY_TIME_MILLISEC;

        //SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
        //Log.d(LOGTAG, "Now=" + df.format(nowTime) + " diff=" + String.valueOf(timeDiff));
        Log.d(LOGTAG, "DB is up-to-date " + String.valueOf(upToDate));

        return upToDate;
    }

    /*
     * storing RSSFeed in SQLite Database 
     */

    public RSSFeed getRSSFeed() {
        Log.d(LOGTAG, "retrieving data from DB");

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

    public void saveToCache(RSSFeed feed) {
        Log.d(LOGTAG, "saveToCache called");

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

    /*
     * managing timestamp of the most recent download via SharedPreferences 
     */

    private Date getDownloadTime() {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        String downloadTime = settings.getString(PREFS_DOWNLOAD_TIME, null);
        //Log.d(LOGTAG, PREFS_DOWNLOAD_TIME + "=" + downloadTime);
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
        //Log.d(LOGTAG, "getTimeNow=" + df.format(now));
        return df.format(now);
    }

    /*
     * Image caching
     */

    public Bitmap getBitmap(String url) {
        Bitmap bitmap = null;
        File imageFile = getPathToImageInCache(url);
        if (imageFile.exists()) {
            Log.d(LOGTAG, "Image file found in the cache " + imageFile);
            try {
                FileInputStream fis = new FileInputStream(imageFile);
                bitmap = BitmapFactory.decodeStream(fis); // or decodeFile
            } catch (FileNotFoundException e) {
                // TODO: handle this exception
            }
        }
        return bitmap;
    }

    // TODO: need to delete files from time to time. when the total size exceeds some limit,
    // delete the oldest files; getTotalSpace(); deleteFile(String)
    public void saveToCache(Bitmap bitmap, String url) {
        File imageFile = getPathToImageInCache(url);
        Log.d(LOGTAG, "saving image by URL to file: " + imageFile);

        try {
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            // TODO: handle this exception thrown by new FIleOutputStream()
        } catch (IOException e) {
            // TODO: handle this exception thrown by fos.close
        }
    }

    private File getPathToImageInCache(String url) {
        return new File(context.getCacheDir(), getFileNameFromUrl(url));
    }

    // http://www.nasa.gov/sites/default/files/styles/946xvariable_height/public/images/246076main_E60-6286_full.jpg?itok=jKIKw4DP
    // --> 246076main_E60-6286_full.jpg
    private String getFileNameFromUrl(String url) {
        String fileName;
        int slashIndex = url.lastIndexOf("/");
        int qIndex = url.lastIndexOf("?");
        if (qIndex > slashIndex) {
            fileName = url.substring(slashIndex + 1, qIndex);
        } else {
            fileName = url.substring(slashIndex + 1);
        }
        return fileName;
    }
}
