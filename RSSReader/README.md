This application is an RSS reader for the following channel:
	http://www.nasa.gov/rss/dyn/image_of_the_day.rss

Homework #5 -- database
===========
1. Once downloaded, the news (except the image) should be saved into DB. The download timestamp should be saved as SharedPreference
2. ??? The image should be saved to ???
3. If the REFRESH button is pressed within an hour from the last download, show data from the DB. Otherwise redownload.
4. When showing the saved news, indicate somehow, that the cached version of the news is shown
5. when network is not available but there is data in database, show cached data and show a message (in red? to make it visible + time)
6. save the image in the internal storage (getCacheDir) and keep the filename in the database
7. can I rely on the RSSCache saves and retrieves data in the order entries come in the original RSSFeed or need to additionally sort it by Date?

Homework #4 -- basic functionality of RSSFeed
===========

1. DONE. Nicer layout, maybe rescale the picture. 
2. DONE. Perform RSS downloading and parsing in background (using AsyncTask)
3. DONE. Show RSS download progress, using ProgressDialog
4. DONE. Show the complete News in a separate activity.

5. handle exceptions correctly
6. align the picture to the top in the list
7. ? How to use ProgressBar instead of ProgressDialog?
     http://stackoverflow.com/questions/4119009/progressbar-togther-with-asynctask
     http://stackoverflow.com/questions/10844116/display-progress-bar-while-loading
8. How to avoid downloading images twice, the 1st time it happens in RSSFeedAdapter and the other time it happens in ShowNewsItemActivity
9. ??? Check and fix: Avoid downloading images many times: each time user comes back from single news view, the image is redownloaded.
   Move downloading of images into RSSDownloaderTask, as AsyncTasks?
10. The app crashes in ShowNewsItemActivity if ImageView in activity_show_news_item is placed as the 3rd element,
    after the title and the date.


Useful links
============

1. RSS format: http://www.realcoding.net/article/view/1883
2. Example code: http://code.google.com/p/android-imagedownloader/
3. http://www.android-ios-tutorials.com/182/show-progressbar-while-downloading-image-using-asynctask-in-android/

