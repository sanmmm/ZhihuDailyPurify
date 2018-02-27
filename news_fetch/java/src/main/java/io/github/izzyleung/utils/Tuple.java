package io.github.izzyleung.utils;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Tuple<L, R> {

  public abstract L left();

  public abstract R right();

  public static <L, R> Tuple<L, R> create(final L left, final R right) {
    return new AutoValue_Tuple<>(left, right);
  }
}
