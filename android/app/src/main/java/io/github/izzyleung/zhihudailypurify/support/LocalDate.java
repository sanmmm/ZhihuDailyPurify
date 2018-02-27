package io.github.izzyleung.zhihudailypurify.support;

import java.util.Calendar;
import java.util.Date;

public class LocalDate {

  private Calendar calendar;

  private LocalDate() {
    calendar = Calendar.getInstance();
  }

  private LocalDate(Calendar calendar) {
    this.calendar = calendar;
  }

  public static LocalDate now() {
    return new LocalDate();
  }

  public static LocalDate of(String date) {
    int year = Integer.parseInt(date.substring(0, 4));
    int month = Integer.parseInt(date.substring(4, 6));
    int day = Integer.parseInt(date.substring(6));

    return LocalDate.of(year, month, day);
  }

  public static LocalDate of(Date date) {
    Calendar result = Calendar.getInstance();
    result.setTime(date);

    return new LocalDate(result);
  }

  public static LocalDate of(int year, int month, int day) {
    Calendar result = Calendar.getInstance();
    result.set(year, month - 1, day);

    return new LocalDate(result);
  }

  public static boolean isToday(String date) {
    Calendar calendar = Calendar.getInstance();
    Calendar another = LocalDate.of(date).plusDays(-1).calendar;

    return calendar.get(Calendar.YEAR) == another.get(Calendar.YEAR)
        && calendar.get(Calendar.MONTH) == another.get(Calendar.MONTH)
        && calendar.get(Calendar.DAY_OF_MONTH) == another.get(Calendar.DAY_OF_MONTH);
  }

  public LocalDate plusDays(int numOfDays) {
    Calendar result = Calendar.getInstance();
    calendar.add(Calendar.DAY_OF_MONTH, numOfDays);
    result.setTime(calendar.getTime());

    return new LocalDate(result);
  }

  public String format() {
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH) + 1;
    int day = calendar.get(Calendar.DAY_OF_MONTH);

    StringBuilder sb = new StringBuilder();

    sb.append(year);

    if (month < 10) {
      sb.append(0);
    }
    sb.append(month);

    if (day < 10) {
      sb.append(0);
    }
    sb.append(day);

    return sb.toString();
  }

  public Date getTime() {
    return calendar.getTime();
  }
}
