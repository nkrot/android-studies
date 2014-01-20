package rssreader.main;

import krot.rssreader.R;
import nasa.rss.pictureoftheday.RSSFeed;
import nasa.rss.pictureoftheday.RSSFeedDownloader;
import rssreader.cache.RSSCache;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

public class RSSDownloaderTask extends AsyncTask<String /*param*/, Void /*progress*/, RSSFeed /*result*/> {
    private final Activity targetActivity;
    private final ListView targetView;
    private ProgressDialog progressDialog;
    private RSSCache rssCache;
    private Boolean cacheIsUpToDate;

    public RSSDownloaderTask(Activity activity, ListView view) {
        // TODO: perhaps using WeakReference would be preferable?
        targetActivity = activity;
        targetView = view;
        progressDialog = null;
        cacheIsUpToDate = false;
    }

    @Override
    public RSSFeed doInBackground(String... args) {
        //publishProgress(true); // with ProgressBar
        RSSFeed rssFeed;
        if (cacheIsUpToDate) {
            rssFeed = rssCache.getRSSFeed();
        } else {
            rssFeed = downloadRSSFeed();
            rssCache.saveToCache(rssFeed);
        }
        return rssFeed;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        rssCache = new RSSCache(targetView.getContext());
        cacheIsUpToDate = rssCache.isUpToDate();

        int msg = cacheIsUpToDate ? R.string.loading_cached_data : R.string.downloading_anew;
        progressDialog = ProgressDialog.show(targetView.getContext(), null,
                targetView.getResources().getString(msg));
    }

    @Override
    protected void onPostExecute(RSSFeed feed) {
        //publishProgress(false); // with ProgressBar
        progressDialog.dismiss(); // with ProgressDialog
        rssCache.close();

        if (isCancelled()) {
            feed = null;
        }

        if (targetView != null) {
            RSSFeedAdapter adapter = new RSSFeedAdapter(targetActivity, R.layout.rss_feed_list_item, feed);
            targetView.setAdapter(adapter);
        }
    }

    /* 
     * with ProgressBar. unfinished
    @Override
    protected void onProgressUpdate(Boolean... progress) {
        Log.d("PROGRESSBAR", "updating");
        targetActivity.setProgressBarIndeterminateVisibility(progress[0]);
    }
    */

    private RSSFeed downloadRSSFeed() {
        RSSFeedDownloader downloader = new RSSFeedDownloader();
        downloader.download();

        RSSFeed feed = downloader.getResult();
        Log.d("FEED", " contains " + feed.size() + " items");

        return feed;
    }
}
