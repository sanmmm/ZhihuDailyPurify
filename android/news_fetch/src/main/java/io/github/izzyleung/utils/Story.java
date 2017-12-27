package io.github.izzyleung.utils;

public final class Story {
    private int id;
    private String title;
    private String thumbnailUrl;

    private Story() {

    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public final static class Builder {
        private final Story instance;

        private Builder() {
            instance = new Story();
        }

        public Builder setId(final int id) {
            instance.id = id;
            return this;
        }

        public Builder setTitle(final String title) {
            instance.title = title;
            return this;
        }

        public Builder setThumbnailUrl(final String thumbnailUrl) {
            instance.thumbnailUrl = thumbnailUrl;
            return this;
        }

        public Story build() {
            return instance;
        }
    }
}
