package rssreader.datasource;

import krot.rssreader.R;
import rssreader.cache.RSSCache;
import rssreader.rssfeed.RSSFeed;
import rssreader.rssfeed.RSSFeedDownloader;
import android.os.AsyncTask;
import android.util.Log;

public class RSSDataFetcher
        extends AsyncTask<String /*param*/, Void /*progress*/, RSSFeed /*result*/> {

    private OnRSSDownloaderListener listener;
    private RSSCache rssCache;
    private boolean cacheIsUpToDate = false;
    private boolean useCachedDataOnly = false;

    public interface OnRSSDownloaderListener {
        public void onPreExecuteRSSDownload(int rStrId);

        public void onPostExecuteRSSDownload(RSSFeed feed);
    }

    public RSSDataFetcher(OnRSSDownloaderListener listener) {
        this.listener = listener;
        this.useCachedDataOnly = false;
    }

    public void allowDownloading() {
        useCachedDataOnly = false;
    }

    public void forbidDownloading() {
        useCachedDataOnly = true;
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

        rssCache = RSSCache.getInstance();
        cacheIsUpToDate = rssCache.isUpToDate();

        int rMsg = cacheIsUpToDate || useCachedDataOnly
                ? R.string.loading_cached_data
                : R.string.downloading_anew;

        if (listener != null) {
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
