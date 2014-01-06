package krot.rssreader;

import nasa.rss.pictureoftheday.RSSFeed;
import nasa.rss.pictureoftheday.RSSFeedDownloader;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

/*
  Q: Why there is no such import in Roman's starter code?
     import android.view.View.OnClickListener;
  Q: Using StrictMode requires minSdk=9. What is the correct approach to target this situation?
*/

public class MainActivity extends Activity implements OnClickListener {

    public ListView rssFeedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_refresh_feed).setOnClickListener(this);

        rssFeedList = (ListView) findViewById(R.id.rss_feed_list);

        // this will allow running the network operations on the main thread
        // this is a temporary fix I use when developing FeedDownloader
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    public void onClick(View view) {
        if (isNetworkAvailable()) {
            //Log.d("NETWORK", "is available");
            RSSFeedDownloader downloader = new RSSFeedDownloader();
            downloader.download();

            RSSFeed feed = downloader.getResult();
            Log.d("FEED", " contains " + feed.size() + " items");

            RSSFeedAdapter adapter = new RSSFeedAdapter(this, R.layout.rss_feed_list_item, feed);
            rssFeedList.setAdapter(adapter);

        } else {
            // TODO: alert that no network is available
            Log.d("NETWORK", "is DOWN");
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
