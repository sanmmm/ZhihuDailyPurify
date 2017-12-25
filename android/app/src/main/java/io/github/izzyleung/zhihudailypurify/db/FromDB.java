package io.github.izzyleung.zhihudailypurify.db;

import io.github.izzyleung.ZhihuDailyPurify;
import io.github.izzyleung.zhihudailypurify.ZhihuDailyPurifyApplication;
import io.reactivex.Single;

public class FromDB {
    private FromDB() {

    }

    public static Single<ZhihuDailyPurify.Feed> feedForDate(String date) {
        return Single.just(ZhihuDailyPurifyApplication.getDataSource().feedForDate(date));
    }
}
