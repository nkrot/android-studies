package rssreader.main;

import krot.rssreader.R;
import nasa.rss.pictureoftheday.RSSFeed;
import nasa.rss.pictureoftheday.RSSFeedDownloader;
import rssreader.cache.RSSCache;
import android.os.AsyncTask;
import android.util.Log;

public class RSSDownloaderTask
        extends AsyncTask<String /*param*/, Void /*progress*/, RSSFeed /*result*/> {

    private OnRSSDownloaderListener listener;
    private RSSCache rssCache;
    private boolean cacheIsUpToDate = false;
    private boolean useCachedDataOnly = false;

    public interface OnRSSDownloaderListener {
        public void onPreExecuteRSSDownload(int rStrId);

        public void onPostExecuteRSSDownload(RSSFeed feed);

        public RSSCache getRSSCache();
    }

    public RSSDownloaderTask(OnRSSDownloaderListener listener) {
        this.listener = listener;
        this.useCachedDataOnly = false;
    }

    public RSSDownloaderTask(OnRSSDownloaderListener listener, boolean useCachedDataOnly) {
        this.listener = listener;
        this.useCachedDataOnly = useCachedDataOnly;
    }

    @Override
    public RSSFeed doInBackground(String... args) {
        RSSFeed rssFeed;
        if (cacheIsUpToDate || useCachedDataOnly) {
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

        if (listener != null) {
            rssCache = listener.getRSSCache();
            cacheIsUpToDate = rssCache.isUpToDate();

            int rMsg = cacheIsUpToDate || useCachedDataOnly
                    ? R.string.loading_cached_data
                    : R.string.downloading_anew;

            listener.onPreExecuteRSSDownload(rMsg);
        }
    }

    @Override
    protected void onPostExecute(RSSFeed feed) {
        if (isCancelled()) {
            feed = null;
        }
        if (listener != null) {
            listener.onPostExecuteRSSDownload(feed);
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
