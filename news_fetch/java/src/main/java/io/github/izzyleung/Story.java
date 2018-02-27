package io.github.izzyleung;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Story {

  abstract int id();

  abstract String title();

  abstract String thumbnailUrl();

  static Builder newBuilder() {
    return new AutoValue_Story.Builder();
  }

  @AutoValue.Builder
  abstract static class Builder {

    abstract Builder setId(final int id);

    abstract Builder setTitle(final String title);

    abstract Builder setThumbnailUrl(final String thumbnailUrl);

    public abstract Story build();
  }
}
