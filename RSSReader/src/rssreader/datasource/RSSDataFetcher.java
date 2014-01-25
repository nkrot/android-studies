package rssreader.datasource;

import krot.rssreader.R;
import rssreader.cache.RSSCache;
import rssreader.rssfeed.RSSFeed;
import rssreader.rssfeed.RSSFeedDownloader;
import android.os.AsyncTask;
import android.util.Log;

public class RSSDataFetcher
        extends AsyncTask<String /*param*/, Void /*progress*/, RSSFeed /*result*/> {

    private OnRSSDataFetcherListener listener;
    private RSSCache rssCache;
    private boolean cacheIsUpToDate = false;
    private boolean useCachedDataOnly = false;

    public interface OnRSSDataFetcherListener {
        public void onPreExecuteRSSDataFetching(int rStrId);

        public void onPostExecuteRSSDataFetching(RSSFeed feed);
    }

    public RSSDataFetcher(OnRSSDataFetcherListener listener) {
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
            listener.onPreExecuteRSSDataFetching(rMsg);
        }
    }

    @Override
    protected void onPostExecute(RSSFeed feed) {
        if (isCancelled()) {
            feed = null;
        }
        if (listener != null) {
            listener.onPostExecuteRSSDataFetching(feed);
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
