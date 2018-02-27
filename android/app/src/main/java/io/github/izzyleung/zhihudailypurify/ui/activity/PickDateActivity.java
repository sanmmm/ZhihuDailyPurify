package io.github.izzyleung.zhihudailypurify.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.squareup.timessquare.CalendarPickerView;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import io.github.izzyleung.zhihudailypurify.R;
import io.github.izzyleung.zhihudailypurify.support.Constants;
import io.github.izzyleung.zhihudailypurify.support.LocalDate;

public class PickDateActivity extends BaseActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Optional.ofNullable(getSupportActionBar())
        .ifPresent(ab -> ab.setDisplayHomeAsUpEnabled(true));

    CalendarPickerView calendarPickerView = findViewById(R.id.calendar_view);

    Date start = LocalDate.of(2013, 5, 19).getTime();
    Date end = LocalDate.now().plusDays(1).getTime();

    calendarPickerView
        .init(start, end)
        .withSelectedDate(Calendar.getInstance().getTime());

    calendarPickerView.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
      @Override
      public void onDateSelected(Date date) {
        Intent intent = new Intent(PickDateActivity.this, FeedDetailActivity.class);
        intent.putExtra(Constants.BundleKeys.DATE, LocalDate.of(date).plusDays(1).format());
        startActivity(intent);
      }

      @Override
      public void onDateUnselected(Date date) {

      }
    });

    calendarPickerView.setOnInvalidDateSelectedListener(date -> {
      if (date.after(new Date())) {
        showSnackbar(R.string.not_coming);
      } else {
        showSnackbar(R.string.not_born);
      }
    });
  }

  @Override
  protected int layoutResId() {
    return R.layout.activity_pick_date;
  }
}
