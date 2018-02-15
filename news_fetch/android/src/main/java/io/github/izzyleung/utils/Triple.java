package io.github.izzyleung.utils;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Triple<L, M, R> {

  public abstract L left();

  public abstract M middle();

  public abstract R right();

  public static <L, M, R> Triple<L, M, R> create(final L left, final M middle, final R right) {
    return new AutoValue_Triple<>(left, middle, right);
  }
}
