package io.github.izzyleung.zhihudailypurify.ui.activity;

import android.os.Bundle;

import java.text.DateFormat;
import java.util.Date;
import java.util.Optional;

import io.github.izzyleung.zhihudailypurify.support.LocalDate;

public class FeedDetailActivity extends BasicFeedActivity {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Optional.ofNullable(getSupportActionBar())
        .ifPresent(ab -> {
          DateFormat format = DateFormat.getDateInstance();
          Date original = LocalDate.of(date).plusDays(-1).getTime();

          ab.setTitle(format.format(original));
          ab.setDisplayHomeAsUpEnabled(true);
        });
  }
}
