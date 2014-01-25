package rssreader.datasource;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import rssreader.cache.RSSCache;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

public class BitmapFetcher extends AsyncTask<String, Void, Bitmap> {
    private OnBitmapFetcherListener listener;

    public interface OnBitmapFetcherListener {
        public void onPreExecuteBitmapFetching();

        public void onPostExecuteBitmapFetching(String url, Bitmap bitmap);
    }

    private String url;

    public BitmapFetcher(OnBitmapFetcherListener listener) {
        this.listener = listener;
    }

    @Override
    protected Bitmap doInBackground(String... args) {
        url = args[0];

        RSSCache rssCache = RSSCache.getInstance();

        Bitmap bitmap = rssCache.getBitmap(url);
        if (bitmap == null) {
            bitmap = downloadBitmap(url);
            rssCache.saveToCache(bitmap, url);
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }

        if (listener != null) {
            listener.onPostExecuteBitmapFetching(url, bitmap);
        }
    }

    private Bitmap downloadBitmap(String link) {
        try {
            InputStream inputStream = null;
            try {
                URL url = new URL(link);
                HttpURLConnection connection = connectTo(url);

                int responseCode = connection.getResponseCode();
                // TODO: handle response code. do it in connectTo() method ?

                inputStream = connection.getInputStream();
                return BitmapFactory.decodeStream(inputStream);

            } catch (MalformedURLException e) {
                e.printStackTrace();

            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
