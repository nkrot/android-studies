package rssreader.main;

import krot.rssreader.R;
import nasa.rss.pictureoftheday.RSSFeedEntry;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;

/*
  Q: Why there is no such import in Roman's starter code?
     import android.view.View.OnClickListener;
  Q: Using StrictMode requires minSdk=9. What is the correct approach to target this situation?
*/

public class MainActivity extends Activity implements OnClickListener {

    public ListView rssFeedView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_refresh_feed).setOnClickListener(this);

        rssFeedView = (ListView) findViewById(R.id.rss_feed_list);
        rssFeedView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(parent.getContext(), ShowNewsItemActivity.class);
                intent.putExtra(RSSFeedEntry.CLASSNAME, (RSSFeedEntry) parent.getAdapter().getItem(position));
                startActivity(intent);
            }
        });

        // this will allow running the network operations on the main thread
        // this is a temporary fix I use when developing FeedDownloader
        //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        //StrictMode.setThreadPolicy(policy);
    }

    @Override
    public void onClick(View view) {
        if (isNetworkAvailable()) {
            RSSDownloaderTask task = new RSSDownloaderTask(this, rssFeedView);
            task.execute();

        } else if (isCacheAvailable() /*TODO*/) {
            // TODO next: if network is not available but there is data in DB, ask the user 
            // if he wants to view cached data. IF yes, get data from cache and display it.
            //RSSDownloaderTask task = new RSSDownloaderTask(this, rssFeedView, useCache=true);
            //task.execute();
            showOfferCachedVersionAlert();

        } else {
            showNetworkIsDownAlert();
        }
    }

    private boolean isCacheAvailable() {
        return false; // TODO
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void showNetworkIsDownAlert() {
        //Log.d("NETWORK", "is DOWN");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.network_not_available)
                .setCancelable(true) /* true allows Back button; false disables Back button */
                .setPositiveButton(R.string.gotyou, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showOfferCachedVersionAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.network_not_available_but_can_show_cached_version)
                .setCancelable(true) /* true allows Back button; false disables Back button */
                .setPositiveButton(R.string.ok2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        // TODO: load and show cached data
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
