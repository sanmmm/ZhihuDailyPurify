package io.github.izzyleung.zhihudailypurify.task;

import android.os.AsyncTask;

import io.github.izzyleung.ZhihuDailyPurify;
import io.github.izzyleung.zhihudailypurify.ZhihuDailyPurifyApplication;
import io.github.izzyleung.zhihudailypurify.db.DailyNewsDataSource;

public class SaveFeedTask extends AsyncTask<Void, Void, Void> {
    private ZhihuDailyPurify.Feed feed;

    public SaveFeedTask(ZhihuDailyPurify.Feed newsList) {
        this.feed = newsList;
    }

    @Override
    protected Void doInBackground(Void... params) {
        if (!feed.equals(ZhihuDailyPurify.Feed.getDefaultInstance())) {
            saveFeed(feed);
        }

        return null;
    }

    private void saveFeed(ZhihuDailyPurify.Feed newsList) {
        DailyNewsDataSource dataSource = ZhihuDailyPurifyApplication.getDataSource();
        String date = newsList.getNewsList().get(0).getDate();

        ZhihuDailyPurify.Feed originalFeed = dataSource.feedForDate(date);

        if (originalFeed == null || !originalFeed.equals(newsList)) {
            dataSource.insertOrUpdateFeed(date, feed);
        }
    }
}