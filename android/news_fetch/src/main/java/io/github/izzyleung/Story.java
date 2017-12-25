package io.github.izzyleung;

public class Story {
    private int id;
    private String title;
    private String thumbnailUrl;

    private Story() {

    }

    int getId() {
        return id;
    }

    String getTitle() {
        return title;
    }

    String getThumbnailUrl() {
        return thumbnailUrl;
    }

    static Builder newBuilder() {
        return new Builder();
    }

    static class Builder {
        private Story instance;

        Builder() {
            instance = new Story();
        }

        Builder setId(int id) {
            instance.id = id;
            return this;
        }

        Builder setTitle(String title) {
            instance.title = title;
            return this;
        }

        Builder setThumbnailUrl(String thumbnailUrl) {
            instance.thumbnailUrl = thumbnailUrl;
            return this;
        }

        Story build() {
            return instance;
        }
    }
}
