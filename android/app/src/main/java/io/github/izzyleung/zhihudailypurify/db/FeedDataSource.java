package io.github.izzyleung.zhihudailypurify.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.google.protobuf.InvalidProtocolBufferException;

import java.util.Optional;

import io.github.izzyleung.ZhihuDailyPurify;

public final class FeedDataSource {

  private SQLiteDatabase database;
  private DBHelper dbHelper;
  private String[] allColumns = {
      DBHelper.COLUMN_ID,
      DBHelper.COLUMN_DATE,
      DBHelper.COLUMN_FEED
  };

  public FeedDataSource(Context context) {
    dbHelper = new DBHelper(context);
  }

  public void open() throws SQLException {
    database = dbHelper.getWritableDatabase();
  }

  private void insertFeed(String date, ZhihuDailyPurify.Feed feed) {
    ContentValues values = new ContentValues();
    values.put(DBHelper.COLUMN_DATE, date);
    values.put(DBHelper.COLUMN_FEED, feed.toByteArray());

    database.insert(DBHelper.TABLE_NAME, null, values);
  }

  private void updateNewsList(String date, ZhihuDailyPurify.Feed feed) {
    ContentValues values = new ContentValues();
    values.put(DBHelper.COLUMN_DATE, date);
    values.put(DBHelper.COLUMN_FEED, feed.toByteArray());
    database.update(DBHelper.TABLE_NAME, values, DBHelper.COLUMN_DATE + "=" + date, null);
  }

  void insertOrUpdateFeed(ZhihuDailyPurify.Feed feed) {
    if (feed.equals(ZhihuDailyPurify.Feed.getDefaultInstance()) || feed.getNewsCount() == 0) {
      return;
    }

    String date = feed.getNews(0).getDate();
    Optional<ZhihuDailyPurify.Feed> fromDB = feedOf(date);

    if (!fromDB.isPresent()) {
      insertFeed(date, feed);
    } else if (!fromDB.get().equals(feed)) {
      updateNewsList(date, feed);
    }
  }

  Optional<ZhihuDailyPurify.Feed> feedOf(String date) {
    Cursor cursor = database.query(DBHelper.TABLE_NAME,
        allColumns, DBHelper.COLUMN_DATE + " = " + date, null, null, null, null);

    cursor.moveToFirst();

    Optional<ZhihuDailyPurify.Feed> result = cursorToFeed(cursor);

    cursor.close();

    return result;
  }

  private Optional<ZhihuDailyPurify.Feed> cursorToFeed(Cursor cursor) {
    return Optional.ofNullable(cursor)
        .filter(c -> c.getCount() > 0)
        .flatMap(c -> feedFromByteArray(c.getBlob(DBHelper.COLUMN_INDEX_FEED)));
  }

  private Optional<ZhihuDailyPurify.Feed> feedFromByteArray(byte[] bytes) {
    try {
      return Optional.of(ZhihuDailyPurify.Feed.parseFrom(bytes));
    } catch (InvalidProtocolBufferException e) {
      return Optional.empty();
    }
  }
}
