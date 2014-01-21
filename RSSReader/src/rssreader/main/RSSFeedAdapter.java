package rssreader.main;

import krot.rssreader.R;
import nasa.rss.pictureoftheday.RSSFeed;
import nasa.rss.pictureoftheday.RSSFeedEntry;
import android.content.Context;
import android.content.Intent;
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

    public RSSFeedAdapter(Context context, int resource, RSSFeed items) {
        super(context, resource);
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.items = items;
        //Log.d("RSSFeedAdapted", "created, contains " + String.valueOf(items.size()) + " items");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Alternatively:
        // LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = convertView;
        if (rowView == null) {
            rowView = inflater.inflate(R.layout.rss_feed_list_item, parent, false);

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showNewsItem(v);
                }
            });

            ViewHolder holder = new ViewHolder();
            holder.icon = (ImageView) rowView.findViewById(R.id.icon);
            holder.title = (TextView) rowView.findViewById(R.id.title);
            holder.date = (TextView) rowView.findViewById(R.id.date);
            holder.description = (TextView) rowView.findViewById(R.id.description);
            holder.position = position;
            rowView.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();

        RSSFeedEntry item = items.get(position);
        //Log.d("getView()", "item #" + String.valueOf(position) + " has title " + item.getTitle());

        downloadAndSetImage(holder.icon, item.getImageURL());
        holder.title.setText(item.getTitle());
        holder.date.setText(item.getDate());
        holder.description.setText(item.getDescription());

        return rowView;
    }

    private void downloadAndSetImage(ImageView view, String url) {
        BitmapDownloaderTask task = new BitmapDownloaderTask(view);
        task.execute(url);
    }

    // this method is a must! w/o it ArrayAdapter#getCount returns 0 and the list is not populated
    // TODO: need to understand why this happens
    @Override
    public int getCount() {
        return items.size();
    }

    static class ViewHolder {
        ImageView icon;
        TextView title;
        TextView date;
        TextView description;
        //ProgressBar progress;
        int position;
    }

    public void showNewsItem(View v) {
        ViewHolder holder = (ViewHolder) v.getTag();
        int position = holder.position;
        //Log.d("NEWSITEM", "Single news item will be shown: " + String.valueOf(position));

        Intent intent = new Intent(context, ShowNewsItemActivity.class);
        intent.putExtra(RSSFeedEntry.CLASSNAME, (RSSFeedEntry) items.get(position));

        context.startActivity(intent);
    }
}
