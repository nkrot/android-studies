This application is an RSS reader for the following channel:
	http://www.nasa.gov/rss/dyn/image_of_the_day.rss

Questions:
==========
1. what is the difference between cache (getCacheDir) and Internal Storage (openFileInput(), openFileOutput())

2. what is the correct approach of storing timestamps in DB/preferences. And how to show time correctly to the user (respecting user's locale)
   -- keep it in milliseconds

3. THE APPLICATION CRASHES WHEN SCROLLING DOWN THE LIST -- probably because there is too many downloading async tasks

4. ?? The app crashes in ShowNewsItemActivity if ImageView in activity_show_news_item is placed as the 3rd element,
    after the title and the date. -- no longer crashes. hummm

5. A: in AsyncTask, is it in doInBackground() only where the commands are run in background, meanwhile stuff in onPostExecute runs also in background?

6. Q: what is the best place to stop the underlying DB? Now i am doing it in MainActivity#onStop and it seems to be happening way too often!

7. Q: what is the best way to pass data to ShowNewsItemActivity:
      a) via an Intent with Parcelable RSSFeedEntry (current implementation)
      b) by passing an record ID in RSSCache and making the Activity retrieve that record from the RSSCache -- we believe that the cache exists and contains relevant data
      
8. Q: What is the best place to initialize the singleton RSSCache.initInstance(Context)?
      a) in MainActivity
      b) in a new class MyApplication (in onCreate). Is it possible to get Context here?

9. THE APP CRASHES (OUTOFMEMORY) IN THE FOLLOWING SCENARIO: LET THE LIST SHOWN, CLICK A NEWS AND SEE A NEWS ITEM, GO BACK, CLICK ANOTHER NEWS

Homework #5 -- database and SharedPrefs
===========

1. DONE. Once downloaded, the news (except the image) should be saved into DB. 

2. DONE. The download timestamp should be saved as SharedPreference.
   TODO: in what format? maybe it will just enough to keep it in milliseconds?
   
3. DONE. If the REFRESH button is pressed within a specified period since the last download (say, 5 min), 
   show data from the DB. Otherwise redownload.

4. DONE. Once downloaded, the image should be saved in the cache (getCacheDir()) and retrieved from there on the next query.
   DONE: build the image file name in the cache on the basis of the complete URL to avoid possible collision if two images have the same
         filename but different URI. Use URI.encode
   TODO: Unlike the RSSFeed itself, there is no expiry date for images: once they get to the cache, they are always served from there.

5. DONE. Refactor RSSCache: move DB into helper (let RSSCache associate various sources)

6. DONE: When network is not available but there is data in database, tell the user network is off and ask him
   whether he wants to see cached data. If there is no cached data, just say that network is not available

7. Show time to the user in the user's timezone and locale format

8. Can we rely on the RSSCache saves and retrieves data in the order entries come in the original RSSFeed
   or need to additionally sort it by Date?

9. Need to clean the cache directory: when its size exceeds some specific value, or when the cache is outdated
   (a newly downloaded rss brings all new images)

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

14. DONE. reimplement RSSCache as singleton and get rid of OnRSSDownloaderListener#getRSSCache.
    With such an implementation, there is no longer a need to pass Context from MainActivity down to RSSCache
    through RSSDownloaderTask.
    
    http://www.devahead.com/blog/2011/06/extending-the-android-application-class-and-dealing-with-singleton/
    http://en.wikipedia.org/wiki/Singleton_pattern
    http://stackoverflow.com/questions/2002288/static-way-to-get-context-on-android
  
15. Switch to using the package from here: http://code.google.com/p/android-imagedownloader/

16. ??? use a single instance of BitmapDownloaderTask tasks to download all images. Bare in mind the task can be executed only once.

17. for better code structuring, move all table column names into an interface that will contain but these names

18. When showing a single new item, allow going directly to the next news in the feed

19. DONE. do not cut the bottom of the text when it does not fit into the text area
    fixed: added proper layout_height and maxLines. also added 'ellipsize=end' to get those nice ... at the end of the text
    TODO: ugly text still appears at the end: 'Ring scientists can also use...t'

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
8. check mime-type of downloaded stuff

Useful links
============

1. RSS format: http://www.realcoding.net/article/view/1883
2. Example code: http://code.google.com/p/android-imagedownloader/
3. http://www.android-ios-tutorials.com/182/show-progressbar-while-downloading-image-using-asynctask-in-android/

