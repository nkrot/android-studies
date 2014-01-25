package rssreader.main;

import java.util.HashMap;

import krot.rssreader.R;
import rssreader.datasource.BitmapFetcher;
import rssreader.datasource.BitmapFetcher.OnBitmapFetcherListener;
import rssreader.rssfeed.RSSFeed;
import rssreader.rssfeed.RSSFeedEntry;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RSSFeedAdapter extends ArrayAdapter<RSSFeedEntry>
        implements OnBitmapFetcherListener {

    private LayoutInflater inflater;
    private RSSFeed items;

    public RSSFeedAdapter(Context context, int resource, RSSFeed items) {
        super(context, resource);
        this.inflater = LayoutInflater.from(context);
        this.items = items;
        //Log.d("RSSFeedAdapter", "created, contains " + String.valueOf(items.size()) + " items");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Alternatively:
        // LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = convertView;
        if (rowView == null) {
            rowView = inflater.inflate(R.layout.rss_feed_list_item, parent, false);

            ViewHolder holder = new ViewHolder();
            holder.image = (ImageView) rowView.findViewById(R.id.image);
            holder.title = (TextView) rowView.findViewById(R.id.title);
            holder.date = (TextView) rowView.findViewById(R.id.date);
            holder.description = (TextView) rowView.findViewById(R.id.description);
            rowView.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();

        RSSFeedEntry item = getItem(position);
        //Log.d("getView()", "item #" + String.valueOf(position) + " has title " + item.getTitle());

        fetchAndSetImage(holder.image, item.getImageURL());
        holder.title.setText(item.getTitle());
        holder.date.setText(item.getDate());
        holder.description.setText(item.getDescription());

        return rowView;
    }

    // this method is a must! w/o it ArrayAdapter#getCount returns 0 and the list is not populated
    @Override
    public int getCount() {
        return items.size();
    }

    // this method is a must! w/o it can not access items from MainActivity
    @Override
    public RSSFeedEntry getItem(int position) {
        return items.get(position);
    }

    static class ViewHolder {
        ImageView image;
        TextView title;
        TextView date;
        TextView description;
    }

    private HashMap<String, View> urlToViewMap = new HashMap<String, View>();

    private void fetchAndSetImage(ImageView view, String url) {
        urlToViewMap.put(url, view);
        BitmapFetcher task = new BitmapFetcher(this);
        task.execute(url);
    }

    /*
     * (non-Javadoc)
     * @see rssreader.datasource.BitmapFetcher.OnBitmapFetcherListener#onPreExecuteBitmapFetching()
     */

    public void onPreExecuteBitmapFetching() {
        // unused
    }

    public void onPostExecuteBitmapFetching(String url, Bitmap bitmap) {
        ImageView imageView = (ImageView) urlToViewMap.get(url);
        imageView.setImageBitmap(bitmap);
    }
}
