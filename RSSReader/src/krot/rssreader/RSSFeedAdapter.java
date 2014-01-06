package krot.rssreader;

import nasa.rss.pictureoftheday.RSSFeed;
import nasa.rss.pictureoftheday.RSSFeedEntry;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RSSFeedAdapter extends ArrayAdapter<RSSFeedEntry> {
    private LayoutInflater inflater;
    private RSSFeed items;

    public RSSFeedAdapter(Context context, int resource, RSSFeed items) {
        super(context, resource);
        this.inflater = LayoutInflater.from(context);
        this.items = items;
        Log.d("RSSFeedAdapted", "created, contains " + String.valueOf(items.size()) + " items");
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

        RSSFeedEntry item = items.get(position);
        Log.d("getView()", "item #" + String.valueOf(position) + " has title " + item.getTitle());

        holder.icon.setImageResource(R.drawable.ic_launcher); // TODO: fake
        holder.title.setText(item.getTitle());
        holder.date.setText(item.getDate());
        holder.description.setText(item.getDescription());

        return rowView;
    }

    static class ViewHolder {
        ImageView icon;
        TextView title;
        TextView date;
        TextView description;
        //ProgressBar progress;
        int position;
    }
}
