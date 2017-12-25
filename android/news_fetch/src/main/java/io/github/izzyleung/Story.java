package io.github.izzyleung;

public class Story {
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

    static class Builder {
        private Story instance;

        private Builder() {
            instance = new Story();
        }

        public Builder setId(int id) {
            instance.id = id;
            return this;
        }

        public Builder setTitle(String title) {
            instance.title = title;
            return this;
        }

        public Builder setThumbnailUrl(String thumbnailUrl) {
            instance.thumbnailUrl = thumbnailUrl;
            return this;
        }

        public Story build() {
            return instance;
        }
    }
}
