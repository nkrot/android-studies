package krot.rssreader;

import nasa.rss.pictureoftheday.RSSFeed;
import nasa.rss.pictureoftheday.RSSFeedDownloader;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

public class RSSDownloaderTask extends AsyncTask<String, Void, RSSFeed> {
    private final Activity targetActivity;
    private final ListView targetView;

    public RSSDownloaderTask(Activity activity, ListView view) {
        // TODO: perhaps using WeakReference would be preferable?
        targetActivity = activity;
        targetView = view;
    }

    @Override
    public RSSFeed doInBackground(String... args) {
        return downloadRSSFeed();
    }

    @Override
    protected void onPostExecute(RSSFeed feed) {
        if (isCancelled()) {
            feed = null;
        }

        if (targetView != null) {
            RSSFeedAdapter adapter = new RSSFeedAdapter(targetActivity, R.layout.rss_feed_list_item, feed);
            targetView.setAdapter(adapter);
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
