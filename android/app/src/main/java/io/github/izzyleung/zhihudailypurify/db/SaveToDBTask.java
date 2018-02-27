package io.github.izzyleung.zhihudailypurify.db;

import android.os.AsyncTask;

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
    dataSource.insertOrUpdateFeed(feed);
  }
}