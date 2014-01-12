This application is an RSS reader for the following channel:
	http://www.nasa.gov/rss/dyn/image_of_the_day.rss


TODO
====

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
    
DONE
====

1. Nicer layout, maybe rescale the picture. 
2. Perform RSS downloading and parsing in background (using AsyncTask)
3. Show RSS download progress, using ProgressDialog
4. Show the complete News in a separate activity.

Useful links
============

1. RSS format: http://www.realcoding.net/article/view/1883
2. Example code: http://code.google.com/p/android-imagedownloader/
3. http://www.android-ios-tutorials.com/182/show-progressbar-while-downloading-image-using-asynctask-in-android/

