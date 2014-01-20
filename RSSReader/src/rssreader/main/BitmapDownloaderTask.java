package rssreader.main;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import rssreader.cache.RSSCache;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {
    private final ImageView targetImageView;

    // Alternatively: TODO: is it better?
    //private final WeakReference<ImageView> targetImageViewReference;

    public BitmapDownloaderTask(ImageView view) {
        targetImageView = view;
        // Alternatively: TODO is it better?
        // targetImageViewReference = new WeakReference<ImageView>(view);
    }

    @Override
    protected Bitmap doInBackground(String... args) {
        String url = args[0];

        RSSCache rssCache = new RSSCache(targetImageView.getContext());

        Bitmap bitmap = rssCache.getBitmap(url);
        if (bitmap == null) {
            bitmap = downloadBitmap(url);
            rssCache.saveToCache(bitmap, url);
        }

        rssCache.close();

        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }

        if (targetImageView != null) {
            targetImageView.setImageBitmap(bitmap);
        }

        // Alternatively: TODO: is it better than the above?
        /*
        if (targetImageViewReference != null) {
            ImageView targetImageView = targetImageViewReference.get();
            if (targetImageView != null) {
                targetImageView.setImageBitmap(bitmap);
            }
        }
        */
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
