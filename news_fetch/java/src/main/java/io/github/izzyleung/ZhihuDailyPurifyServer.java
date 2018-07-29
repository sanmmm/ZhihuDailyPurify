package io.github.izzyleung;

import com.google.auto.value.AutoValue;
import java.io.IOException;
import java.io.InputStream;

import io.reactivex.Single;

@AutoValue
public abstract class ZhihuDailyPurifyServer {

  private static final String ZHIHU_DAILY_PURIFY_BASE = "https://zhihudailypurify.herokuapp.com/";
  private static final String ZHIHU_DAILY_PURIFY = ZHIHU_DAILY_PURIFY_BASE + "news/";
  private static final String ZHIHU_DAILY_PURIFY_SEARCH = ZHIHU_DAILY_PURIFY_BASE + "search/";

  public abstract String date();

  public static ZhihuDailyPurifyServer of(String date) {
    return new AutoValue_ZhihuDailyPurifyServer(date);
  }

  public Single<ZhihuDailyPurify.Feed> feed() throws IOException {
    InputStream inputStream = Network.openInputStream(ZHIHU_DAILY_PURIFY + date());
    return Single.just(ZhihuDailyPurify.Feed.parseFrom(inputStream));
  }

  public static Single<ZhihuDailyPurify.Feed> search(String keyword) throws IOException {
    InputStream inputStream = Network.openInputStream(ZHIHU_DAILY_PURIFY_SEARCH + keyword);
    return Single.just(ZhihuDailyPurify.Feed.parseFrom(inputStream));
  }
}
