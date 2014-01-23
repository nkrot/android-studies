package rssreader.main;

import krot.rssreader.R;
import nasa.rss.pictureoftheday.RSSFeed;
import nasa.rss.pictureoftheday.RSSFeedEntry;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RSSFeedAdapter extends ArrayAdapter<RSSFeedEntry> {
    private LayoutInflater inflater;
    private RSSFeed items;
    private Context context;

    //    private final BitmapDownloaderTask downloaderTask;

    public RSSFeedAdapter(Context context, int resource, RSSFeed items) {
        super(context, resource);
        this.context = context;
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
            holder.icon = (ImageView) rowView.findViewById(R.id.icon);
            holder.title = (TextView) rowView.findViewById(R.id.title);
            holder.date = (TextView) rowView.findViewById(R.id.date);
            holder.description = (TextView) rowView.findViewById(R.id.description);
            rowView.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();

        RSSFeedEntry item = getItem(position);
        //Log.d("getView()", "item #" + String.valueOf(position) + " has title " + item.getTitle());

        downloadAndSetImage(holder.icon, item.getImageURL());
        holder.title.setText(item.getTitle());
        holder.date.setText(item.getDate());
        holder.description.setText(item.getDescription());

        return rowView;
    }

    // TODO: do not recreate tasks! one if enough to download all
    private void downloadAndSetImage(ImageView view, String url) {
        BitmapDownloaderTask task = new BitmapDownloaderTask(view);
        task.execute(url);
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
        ImageView icon;
        TextView title;
        TextView date;
        TextView description;
    }
}
