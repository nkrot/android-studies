package rssreader.main;

import krot.rssreader.R;
import nasa.rss.pictureoftheday.RSSFeed;
import nasa.rss.pictureoftheday.RSSFeedDownloader;
import rssreader.cache.RSSCache;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

public class RSSDownloaderTask extends AsyncTask<String /*param*/, Void /*progress*/, RSSFeed /*result*/> {
    private OnRSSDownloaderListener listener;

    public interface OnRSSDownloaderListener {
        public void onPreExecuteRSSDownload(int rStrId);

        public void onPostExecuteRSSDownload(RSSFeed feed);

        public RSSCache getRSSCache();
    }

    private RSSCache rssCache;
    private boolean cacheIsUpToDate = false;
    private boolean useCachedDataOnly;

    public RSSDownloaderTask(Activity activity) {
        setListener(activity);
        this.useCachedDataOnly = false;
    }

    public RSSDownloaderTask(Activity activity, boolean useCachedDataOnly) {
        setListener(activity);
        this.useCachedDataOnly = useCachedDataOnly;
    }

    @Override
    public RSSFeed doInBackground(String... args) {
        RSSFeed rssFeed;
        if (cacheIsUpToDate || useCachedDataOnly) {
            rssFeed = rssCache.getRSSFeed();
        } else {
            rssFeed = downloadRSSFeed();
            rssCache.saveToCache(rssFeed); // TODO: will it run in background? could be moved to onPostExecute?
        }
        return rssFeed;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        rssCache = listener.getRSSCache();
        cacheIsUpToDate = rssCache.isUpToDate();

        int rMsg = cacheIsUpToDate || useCachedDataOnly
                ? R.string.loading_cached_data
                : R.string.downloading_anew;

        listener.onPreExecuteRSSDownload(rMsg);
    }

    @Override
    protected void onPostExecute(RSSFeed feed) {
        if (isCancelled()) {
            feed = null;
        }
        listener.onPostExecuteRSSDownload(feed);
    }

    private void setListener(Activity activity) {
        if (activity instanceof OnRSSDownloaderListener) {
            listener = (OnRSSDownloaderListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement RSSDownloaderTask.OnRSSDownloaderListener");
        }
    }

    private RSSFeed downloadRSSFeed() {
        RSSFeedDownloader downloader = new RSSFeedDownloader();
        downloader.download();

        RSSFeed feed = downloader.getResult();
        Log.d("FEED", " contains " + feed.size() + " items");

        return feed;
    }
}
