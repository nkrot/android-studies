package nasa.rss.pictureoftheday;

public class RSSFeedEntry {
    static final int TITLE = 1;
    static final int DESCRIPTION = 2;
    static final int DATE = 3;

    private int fieldToAccept;

    private String imageLink;
    private StringBuilder title = new StringBuilder();
    private StringBuilder description = new StringBuilder();
    private StringBuilder date = new StringBuilder();

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

    public void setImageLink(String url) {
        imageLink = url;
    }

    public String getTitle() {
        return title.toString();
    }

    public String getDescription() {
        return description.toString();
    }

    public String getDate() {
        return date.toString();
    }

    // TODO: return image
    public String getImage() {
        return imageLink;
    }
}
