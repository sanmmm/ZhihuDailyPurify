package io.github.izzyleung.zhihudailypurify.db;

import android.os.AsyncTask;
import android.util.Log;

import io.github.izzyleung.ZhihuDailyPurify;
import io.github.izzyleung.zhihudailypurify.ZhihuDailyPurifyApplication;

public class SaveToDBTask extends AsyncTask<Void, Void, Void> {
    private ZhihuDailyPurify.Feed feed;

    public SaveToDBTask(ZhihuDailyPurify.Feed feed) {
        this.feed = feed;
    }

    @Override
    protected Void doInBackground(Void... params) {
        saveToDB();

        return null;
    }

    private void saveToDB() {
        FeedDataSource dataSource = ZhihuDailyPurifyApplication.getDataSource();
        ZhihuDailyPurify.Feed defaultInstance = ZhihuDailyPurify.Feed.getDefaultInstance();

        if (!feed.equals(defaultInstance) && feed.getNewsList().size() > 0) {
            String date = feed.getNews(0).getDate();
            ZhihuDailyPurify.Feed originalFeed = dataSource.feedForDate(date);

            if (!feed.equals(originalFeed)) {
                dataSource.insertOrUpdateFeed(feed);
            }
        }
    }
}