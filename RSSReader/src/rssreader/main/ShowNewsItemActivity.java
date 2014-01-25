package rssreader.main;

import krot.rssreader.R;
import rssreader.datasource.BitmapFetcher;
import rssreader.datasource.BitmapFetcher.OnBitmapFetcherListener;
import rssreader.rssfeed.RSSFeedEntry;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ShowNewsItemActivity extends Activity
        implements OnBitmapFetcherListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_news_item);
        //Log.d("ShowNewsItem", "new activity started");

        Intent intent = getIntent();

        if (intent == null) {
            // TODO: can it happen?
        } else {
            //Log.d("ShowNewsItem", "intent has data!");
            populateViewFromIntent(intent);
        }
    }

    private void populateViewFromIntent(Intent intent) {
        Bundle bundle = intent.getExtras();
        RSSFeedEntry entry = bundle.getParcelable(RSSFeedEntry.CLASSNAME);

        fetchAndSetImage(entry.getImageURL());

        TextView titleView = (TextView) findViewById(R.id.title);
        titleView.setText(entry.getTitle());

        TextView dateView = (TextView) findViewById(R.id.date);
        dateView.setText(entry.getDate());

        TextView descView = (TextView) findViewById(R.id.description);
        descView.setText(entry.getDescription());
    }

    /* TODO: the code below duplicates the code in RSSFeedAdapter. get rid of duplication */

    private void fetchAndSetImage(String url) {
        BitmapFetcher task = new BitmapFetcher(this);
        task.execute(url);
    }

    public void onPreExecuteBitmapFetching() {
        // unused
    }

    public void onPostExecuteBitmapFetching(String url, Bitmap bitmap) {
        ImageView imageView = (ImageView) findViewById(R.id.image);
        imageView.setImageBitmap(bitmap);
    }

}
