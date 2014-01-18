package krot.rssreader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
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

        } else {
            showNetworkIsDownAlert();
        }
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
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
