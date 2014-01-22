This application is an RSS reader for the following channel:
	http://www.nasa.gov/rss/dyn/image_of_the_day.rss

Questions:
==========
1. what is the difference between cache (getCacheDir) and Internal Storage (openFileInput(), openFIleOutput())
2. what is the correct approach of storing timestamps in DB/preferences. And how to show time correctly to the user (respecting user's locale)
   -- keep it in milliseconds

3. THE APPLICATION CRASHES WHEN SCROLLING DOWN THE LIST

4. The app crashes in ShowNewsItemActivity if ImageView in activity_show_news_item is placed as the 3rd element,
    after the title and the date.

5. in AsyncTask, is it in doInBackground() where the commands are run in background, or stuff in onPostExecute runs also in background?

6. I dont like how rssCache is shared between MainActivity and RSSDownloadTask. what could an alternative be?

Homework #5 -- database and SharedPrefs
===========

1. DONE. Once downloaded, the news (except the image) should be saved into DB. 

2. DONE. The download timestamp should be saved as SharedPreference.
   TODO: in what format? maybe it will just enough to keep it in milliseconds?
   
3. DONE. If the REFRESH button is pressed within a specified period since the last download (say, 5 min), show data from the DB. Otherwise redownload.

4. DONE. Once downloaded, the image should be saved in the cache (getCacheDir()) and retrieved from there on the next query.
   TODO: build the image file name in the cache on the basis of the complete URL to avoid possible collision if two images have the same
   filename but different URI. Use URI.encode
   [?] Unlike the RSSFeed itself, there is no expiry date for images: once they get to the cache, they are always served from there.

5. DONE. Refactor RSSCache: move DB into helper (let RSSCache associate various sources)

6. DONE: When network is not available but there is data in database, tell the user network is off and ask him whether he wants to see cached data
   If there is no cached data, just say that network is not available -- in MainActivity

7. Show time to the user in the user's timezone and locale format

8. Can we rely on the RSSCache saves and retrieves data in the order entries come in the original RSSFeed or need to additionally sort it by Date?

9. Need to clean the cache directory: when its size exceeds some specific value, or when the cache is outdated (a newly downloaded rss brings all new images)

10. When showing the saved news, indicate somehow, that it is the cached version of the news that is shown.

11. DONE. Remove hardcoded literals:
    a) RSS tags that are recognized by the RSSFeedParser are defined as class constants.
    b) when RSSFeedEntry is used as Parcelable, it is put into bundle as follows:
       in ShowNewsItemActivity: bundle.getParcelable("RSSFeedEntry") --> bundle.getParcelable(RSSFeedEntry.CLASSNAME)
       in RSSFeedAdapter: intent.putExtra("RSSFeedEntry", ...) -->  intent.putExtra(RSSFeedEntry.CLASSNAME, ...)

12. DONE. Showing a single news item when a list item is clicked moved from RSSFeedAdapter to MainActivity:
      rssFeedView.setOnItemClickListener(listener)

13. DONE. Refactor: RSSDownloaderTask should not keep references to UI  (targetActivity, targetView, progressDialog)
    Instead, keep a references to the interface in which onPreExecute() and onPostExecute() are defined.
    The interface is to be implemented in MainActivity.
    As a result, RSSDownloaderTask will be unaware of UI and will be able to work with Fragment as well.
  
14. Switch to using the package from here: http://code.google.com/p/android-imagedownloader/

Homework #4 -- basic functionality of RSSFeed
===========

1. DONE. Nicer layout, maybe rescale the picture. 
2. DONE. Perform RSS downloading and parsing in background (using AsyncTask).
3. DONE. Show RSS download progress, using ProgressDialog.
4. DONE. Show the complete News in a separate activity.

5. handle exceptions correctly
6. align the picture to the top in the list
7. ? How to use ProgressBar instead of ProgressDialog?
     http://stackoverflow.com/questions/4119009/progressbar-togther-with-asynctask
     http://stackoverflow.com/questions/10844116/display-progress-bar-while-loading


Useful links
============

1. RSS format: http://www.realcoding.net/article/view/1883
2. Example code: http://code.google.com/p/android-imagedownloader/
3. http://www.android-ios-tutorials.com/182/show-progressbar-while-downloading-image-using-asynctask-in-android/

