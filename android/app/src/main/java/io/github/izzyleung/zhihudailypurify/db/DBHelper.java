package io.github.izzyleung.zhihudailypurify.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public final class DBHelper extends SQLiteOpenHelper {

  static final String TABLE_NAME = "feed";
  static final String COLUMN_ID = "_id";
  static final String COLUMN_DATE = "date";
  static final String COLUMN_FEED = "feed";

  static final int COLUMN_INDEX_FEED = 2;

  private static final String DATABASE_NAME = "feed.db";
  private static final int DATABASE_VERSION = 1;

  private static final String DATABASE_CREATE
      = "CREATE TABLE " + TABLE_NAME
      + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
      + COLUMN_DATE + " CHAR(8) UNIQUE, "
      + COLUMN_FEED + " BLOB NOT NULL);";

  DBHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(DATABASE_CREATE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    onCreate(db);
  }
}
