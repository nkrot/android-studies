package rssreader.rssfeed;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

public class RSSFeedParser {
    private XmlPullParser xmlParser;

    // XML elements that are recognized
    private final String EL_ITEM = "item";
    private final String EL_TITLE = "title";
    private final String EL_DESCRIPTION = "description";
    private final String EL_PUBDATE = "pubDate";
    private final String EL_ENCLOSURE = "enclosure";

    public RSSFeed parse(InputStream inputStream)
            throws XmlPullParserException, IOException {

        try {
            xmlParser = Xml.newPullParser();
            xmlParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlParser.setInput(inputStream, null);

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        // TODO: should IOException be handled here? : inputStream.close()

        return readFeed();
    }

    private RSSFeed readFeed()
            throws XmlPullParserException, IOException {

        RSSFeed entries = new RSSFeed();

        xmlParser.nextTag();
        xmlParser.require(XmlPullParser.START_TAG, null, "rss");

        int eventType = xmlParser.getEventType();

        RSSFeedEntry entry = new RSSFeedEntry();

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                //Log.d("Parsing, START_TAG", " " + xmlParser.getName());

                if (isElement(EL_ITEM)) {
                    entry = new RSSFeedEntry();
                } else if (isElement(EL_TITLE)) {
                    entry.acceptTitle();
                } else if (isElement(EL_DESCRIPTION)) {
                    entry.acceptDescription();
                } else if (isElement(EL_PUBDATE)) {
                    entry.acceptDate();
                } else if (isElement(EL_ENCLOSURE)) {
                    entry.setImageURL(xmlParser.getAttributeValue(null, "url"));
                }

            } else if (eventType == XmlPullParser.END_TAG) {
                //Log.d("Parsing, END_TAG", " " + xmlParser.getName());
                entry.acceptNothing();
                if (isElement(EL_ITEM)) {
                    entries.add(entry);
                }

            } else if (eventType == XmlPullParser.TEXT) {
                // Log.d("Parsing, TEXT", xmlParser.getText());
                entry.setField(xmlParser.getText());
            }

            eventType = xmlParser.next();
        }

        return entries;
    }

    private boolean isElement(String expectedValue) {
        return expectedValue.equalsIgnoreCase(xmlParser.getName());
    }
}
