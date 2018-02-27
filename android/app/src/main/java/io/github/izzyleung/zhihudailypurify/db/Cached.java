package io.github.izzyleung.zhihudailypurify.db;

import io.github.izzyleung.ZhihuDailyPurify;
import io.github.izzyleung.zhihudailypurify.ZhihuDailyPurifyApplication;
import io.reactivex.Single;

public class Cached {

  private final String date;

  private Cached(String date) {
    this.date = date;
  }

  public String date() {
    return this.date;
  }

  public static Cached of(String date) {
    if (date == null) {
      throw new NullPointerException("Null date");
    }

    return new Cached(date);
  }

  public Single<ZhihuDailyPurify.Feed> feed() {
    FeedDataSource source = ZhihuDailyPurifyApplication.getDataSource();
    return Single.just(source.feedOf(date()).orElse(ZhihuDailyPurify.Feed.getDefaultInstance()));
  }
}
