package io.github.izzyleung.zhihudailypurify.db;

import io.github.izzyleung.ZhihuDailyPurify;
import io.github.izzyleung.zhihudailypurify.ZhihuDailyPurifyApplication;
import io.reactivex.Single;

public class FromDB {
    private FromDB() {

    }

    public static Single<ZhihuDailyPurify.Feed> feedForDate(String date) {
        FeedDataSource source = ZhihuDailyPurifyApplication.getDataSource();
        return Single.just(source.feedForDate(date).orElse(ZhihuDailyPurify.Feed.getDefaultInstance()));
    }
}
