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
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class RSSCache {
    private static final String LOGTAG = "RSSCACHE";

    // SharedPreferences
    private static final String PREFS_NAME = "rssreader.prefs";
    private static final String PREFS_DOWNLOAD_TIME = "LastDownloadTime";
    public static final String DATE_FORMAT = "d-MM-yyyy HH:mm:ss z"; // as kept in shared prefs
    public static long EXPIRY_TIME_MILLISEC = 1000 * 60 * 5; // 5 min

    private Context context;
    private RSSCacheDB db;

    public RSSCache(Context context) {
        this.context = context;
        this.db = null;
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
     * RSSFeed cache: forward the method calls to the underlying object
     */

    public RSSFeed getRSSFeed() {
        Log.d(LOGTAG, "retrieving data from DB");
        return getCacheDB().getRSSFeed();
    }

    public void saveToCache(RSSFeed feed) {
        Log.d(LOGTAG, "saveToCache called");
        getCacheDB().saveToCache(feed);
        recordDownloadTime();
    }

    public void close() {
        if (db != null) {
            db.close();
        }
    }

    private RSSCacheDB getCacheDB() {
        if (db == null) {
            db = new RSSCacheDB(context);
        }
        return db;
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
