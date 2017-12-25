package io.github.izzyleung;

import java.io.IOException;
import java.io.InputStream;

import io.reactivex.Single;

public class ZhihuDailyPurifyServer {
    private ZhihuDailyPurifyServer() {}

    private static final String ZHIHU_DAILY_PURIFY_BASE = "https://zhihudailypurify.herokuapp.com/";
    private static final String ZHIHU_DAILY_PURIFY = ZHIHU_DAILY_PURIFY_BASE + "news/";
    private static final String ZHIHU_DAILY_PURIFY_SEARCH = ZHIHU_DAILY_PURIFY_BASE + "search/";

    public static Single<ZhihuDailyPurify.Feed> feedForDate(String date) throws IOException {
        InputStream inputStream = Network.openInputStream(ZHIHU_DAILY_PURIFY + date);
        return Single.just(ZhihuDailyPurify.Feed.parseFrom(inputStream));
    }

    public static Single<ZhihuDailyPurify.Feed> searchForKeyword(String keyword) throws IOException {
        InputStream inputStream = Network.openInputStream(ZHIHU_DAILY_PURIFY_SEARCH + keyword);
        return Single.just(ZhihuDailyPurify.Feed.parseFrom(inputStream));
    }
}
