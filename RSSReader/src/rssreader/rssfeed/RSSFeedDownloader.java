package rssreader.rssfeed;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

public class RSSFeedDownloader {
    private URL url;
    private RSSFeed downloadedFeed;

    public RSSFeedDownloader() {
        try {
            url = new URL("http://www.nasa.gov/rss/dyn/image_of_the_day.rss");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void download() {
        downloadUrl(url);
    }

    public RSSFeed getResult() {
        return downloadedFeed;
    }

    private void downloadUrl(URL url) {
        try {
            InputStream inputStream = null;
            try {
                HttpURLConnection connection = connectTo(url);

                int responseCode = connection.getResponseCode();
                // TODO: handle response code. do it in connectTo() method ?

                inputStream = connection.getInputStream();

                RSSFeedParser parser = new RSSFeedParser();
                downloadedFeed = parser.parse(inputStream);

            } catch (XmlPullParserException e) {
                // TODO: what should we do here?
                Log.d("EXCEPTION", "XmlPullParserException occurred");

            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HttpURLConnection connectTo(URL url) throws IOException {
        HttpURLConnection connection;
        connection = (HttpURLConnection) url.openConnection();

        connection.setReadTimeout(10000 /* milliseconds */);
        connection.setConnectTimeout(15000 /* milliseconds */);
        connection.setRequestMethod("GET");
        connection.setDoInput(true);

        // Perform the query
        connection.connect();

        return connection;
    }
}
