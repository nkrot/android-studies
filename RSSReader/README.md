This application is an RSS reader for the following channel:
	http://www.nasa.gov/rss/dyn/image_of_the_day.rss

Questions:
==========
1. what is the difference between cache (getCacheDir) and Internal Storage (openFileInput(), openFIleOutput())
2. what is the correct approach of storing timestamps in DB/preferences. And how to show time correctly to the user (respecting user's locale)

Homework #5 -- database and SharedPrefs
===========
0. THE APPLICATION CRASHES WHEN SCROLLING DOWN THE LIST

1. DONE. Once downloaded, the news (except the image) should be saved into DB. 
2. DONE. The download timestamp should be saved as SharedPreference.
   TODO: in what format? maybe it will just enough to keep it in milliseconds?
3. DONE. If the REFRESH button is pressed within a specified period since the last download (say, 5 min), show data from the DB. Otherwise redownload.
4. DONE. Once downloaded, the image should be saved in the cache (getCacheDir()) and retrieved from there on the next query.
   [?] Unlike the RSSFeed iself, there is no expiry date for images: once they get to the cache, they are always served from there.
5. NEXT: When network is not available but there is data in database, tell the user network is off and ask him whether he wants to see cached data
   If there is no cached data, just say that network is not available -- in MainActivity
6. Show time to the user in the user's timezone and locale format
7. Can we rely on the RSSCache saves and retrieves data in the order entries come in the original RSSFeed or need to additionally sort it by Date?
8. Need to clean the cache directory: when its size exceeds some specific value, or when the cache is outdated (a newly downloaded rss brings all new images)
9. When showing the saved news, indicate somehow, that it is the cached version of the news that is shown.

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
8. The app crashes in ShowNewsItemActivity if ImageView in activity_show_news_item is placed as the 3rd element,
    after the title and the date.


Useful links
============

1. RSS format: http://www.realcoding.net/article/view/1883
2. Example code: http://code.google.com/p/android-imagedownloader/
3. http://www.android-ios-tutorials.com/182/show-progressbar-while-downloading-image-using-asynctask-in-android/

