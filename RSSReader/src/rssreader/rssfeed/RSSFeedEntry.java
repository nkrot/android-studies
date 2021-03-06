package rssreader.rssfeed;

import android.os.Parcel;
import android.os.Parcelable;

public class RSSFeedEntry implements Parcelable {
    public static final String CLASSNAME = "RSSFeedEntry";

    static final int TITLE = 1;
    static final int DESCRIPTION = 2;
    static final int DATE = 3;
    static final String DATE_FORMAT = "E, d MMM yyyy hh:mm:ss z"; // as seen in RSS feed

    private int fieldToAccept;

    private String imageLink;
    private StringBuilder title = new StringBuilder();
    private StringBuilder description = new StringBuilder();
    private StringBuilder date = new StringBuilder();

    public RSSFeedEntry() {

    }

    public void setField(String val) {
        switch (fieldToAccept) {
        case TITLE:
            if (title.length() > 0) {
                title.append(" ");
            }
            title.append(val);
            break;

        case DESCRIPTION:
            if (description.length() > 0) {
                description.append("\n");
            }
            description.append(val);
            break;

        case DATE:
            if (date.length() > 0) {
                date.append(" ");
            }
            date.append(val);
            break;
        }
    }

    public void acceptTitle() {
        fieldToAccept = TITLE;
    }

    public void acceptDescription() {
        fieldToAccept = DESCRIPTION;
    }

    public void acceptDate() {
        fieldToAccept = DATE;
    }

    public void acceptNothing() {
        fieldToAccept = 0;
    }

    public void setTitle(String str) {
        title.append(str);
    }

    public String getTitle() {
        return title.toString();
    }

    public void setDescription(String str) {
        description.append(str);
    }

    public String getDescription() {
        return description.toString();
    }

    public void setDate(String str) {
        date.append(str);
    }

    public String getDate() {
        return date.toString();
    }

    // TODO: hmmm. is this correct time conversion? Why USA time is greater than European time?
    // Fri, 17 Jan 2014 12:00:00 EDT --> Fri, 17 Jan 2014 04:00:00 UTC
    /*
    public String getDateUTC() {
        SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
        Date d = null;
        try {
            d = df.parse(getDate());
        } catch (ParseException e) {
            // TODO: react to the exception somehow
            e.printStackTrace();
        }
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        return df.format(d);
    }
    */

    public void setImageURL(String url) {
        imageLink = url;
    }

    public String getImageURL() {
        return imageLink;
    }

    /*
     * make it Parcelable
     */

    public static final Parcelable.Creator<RSSFeedEntry> CREATOR =
            new Parcelable.Creator<RSSFeedEntry>() {
                public RSSFeedEntry createFromParcel(Parcel in) {
                    return new RSSFeedEntry(in);
                };

                public RSSFeedEntry[] newArray(int size) {
                    return new RSSFeedEntry[size];
                };
            };

    public RSSFeedEntry(Parcel in) {
        readFromParcel(in);
    }

    private void readFromParcel(Parcel in) {
        //fieldToAccept = in.readInt();
        title.append(in.readString());
        date.append(in.readString());
        description.append(in.readString());
        setImageURL(in.readString());
    }

    @Override
    public void writeToParcel(Parcel out, int flag) {
        //out.writeInt(fieldToAccept);
        out.writeString(getTitle());
        out.writeString(getDate());
        out.writeString(getDescription());
        out.writeString(getImageURL());
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
