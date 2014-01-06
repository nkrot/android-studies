package nasa.rss.pictureoftheday;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

public class RSSFeedParser {
    private XmlPullParser xmlParser;

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

                if (isElement("item")) {
                    entry = new RSSFeedEntry();
                } else if (isElement("title")) {
                    entry.acceptTitle();
                } else if (isElement("description")) {
                    entry.acceptDescription();
                } else if (isElement("pubDate")) {
                    entry.acceptDate();
                } else if (isElement("enclosure")) {
                    entry.setImageLink(xmlParser.getAttributeValue(null, "url"));
                }

            } else if (eventType == XmlPullParser.END_TAG) {
                //Log.d("Parsing, END_TAG", " " + xmlParser.getName());
                entry.acceptNothing();
                if (isElement("item")) {
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
