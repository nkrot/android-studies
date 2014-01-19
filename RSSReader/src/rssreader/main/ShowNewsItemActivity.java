package rssreader.main;

import krot.rssreader.R;
import nasa.rss.pictureoftheday.RSSFeedEntry;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ShowNewsItemActivity extends Activity {

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
        RSSFeedEntry entry = bundle.getParcelable("RSSFeedEntry");

        ImageView imageView = (ImageView) findViewById(R.id.icon);
        downloadAndSetImage(imageView, entry.getImageURL());

        TextView titleView = (TextView) findViewById(R.id.title);
        titleView.setText(entry.getTitle());

        TextView dateView = (TextView) findViewById(R.id.date);
        dateView.setText(entry.getDate());

        TextView descView = (TextView) findViewById(R.id.description);
        descView.setText(entry.getDescription());
    }

    // TODO: this is a literal duplicate of a method in RSSFeedAdapter
    private void downloadAndSetImage(ImageView view, String url) {
        BitmapDownloaderTask task = new BitmapDownloaderTask(view);
        task.execute(url);
    }
}
