package io.github.izzyleung.utils;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Story {

  public abstract int id();

  public abstract String title();

  public abstract String thumbnailUrl();

  public static Builder newBuilder() {
    return new AutoValue_Story.Builder();
  }

  @AutoValue.Builder
  public abstract static class Builder {

    public abstract Builder setId(final int id);

    public abstract Builder setTitle(final String title);

    public abstract Builder setThumbnailUrl(final String thumbnailUrl);

    public abstract Story build();
  }
}
