package io.github.izzyleung;

import com.google.auto.value.AutoValue;
import io.reactivex.Flowable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

class Stories {

  private Stories() {

  }

  static Flowable<Story> of(String date) throws IOException {
    Flowable<Story.Metadata> metadata = Stories.metadataFromJson(Stories.metadataJsonOf(date));
    Flowable<Document> documents = metadata.map(Stories::details).flatMap(Stories::documents);

    return Flowable.zip(metadata, documents, Story::create);
  }

  static Flowable<Story.Metadata> metadataFromJson(InputStream in) {
    return Flowable.just(mkString(in))
        .map(JSONObject::new)
        .filter(j -> j.has(ZhihuDaily.KEY_STORIES))
        .map(j -> j.getJSONArray(ZhihuDaily.KEY_STORIES))
        .flatMap(j -> Flowable.fromArray(toArray(j)))
        .flatMap(Stories::toMetadata);
  }

  private static Flowable<Story.Metadata> toMetadata(JSONObject j) throws JSONException {
    Flowable<Integer> id = Flowable.just(j.getInt(ZhihuDaily.KEY_ID));
    Flowable<String> title = Flowable.just(j.getString(ZhihuDaily.KEY_TITLE));
    Flowable<String> thumbnailUrl = Flowable.just(j)
        .filter(x -> x.has(ZhihuDaily.KEY_IMAGES))
        .map(x -> x.getJSONArray(ZhihuDaily.KEY_IMAGES))
        .filter(x -> x.length() > 0)
        .map(x -> (String) x.get(0))
        .switchIfEmpty(Flowable.just(""));

    return Flowable.zip(id, title, thumbnailUrl, (storyId, storyTitle, url) ->
            Story.Metadata.newBuilder()
                .setId(storyId)
                .setTitle(storyTitle)
                .setThumbnailUrl(url)
                .build()
        );
  }

  private static InputStream metadataJsonOf(String date) throws IOException {
    return Network.openInputStream(ZhihuDaily.ZHIHU_DAILY_NEWS_BEFORE + date);
  }

  private static InputStream details(Story.Metadata metadata) throws IOException {
    return Network.openInputStream(ZhihuDaily.ZHIHU_DAILY_NEWS_BASE + metadata.id());
  }

  private static Flowable<Document> documents(InputStream in) {
    return Flowable.just(mkString(in))
        .map(JSONObject::new)
        .filter(j -> j.has(ZhihuDaily.KEY_BODY))
        .map(j -> Jsoup.parse(j.getString(ZhihuDaily.KEY_BODY)))
        .defaultIfEmpty(new Document(""));
  }

  private static String mkString(InputStream input) {
    return new BufferedReader(new InputStreamReader(input))
        .lines()
        .collect(Collectors.joining("\n"));
  }

  private static JSONObject[] toArray(JSONArray jsonArray) throws JSONException {
    JSONObject[] result = new JSONObject[jsonArray.length()];

    for (int i = 0; i < jsonArray.length(); i++) {
      result[i] = jsonArray.getJSONObject(i);
    }

    return result;
  }

  private static class ZhihuDaily {

    private static final String ZHIHU_DAILY_NEWS_BASE = "https://news-at.zhihu.com/api/4/news/";
    private static final String ZHIHU_DAILY_NEWS_BEFORE = ZHIHU_DAILY_NEWS_BASE + "before/";

    private static final String KEY_STORIES = "stories";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_IMAGES = "images";
    private static final String KEY_BODY = "body";
  }
}

@AutoValue
abstract class Story {

  abstract Metadata metadata();

  abstract Document document();

  static Story create(final Metadata metadata, final Document document) {
    return new AutoValue_Story(metadata, document);
  }

  @AutoValue
  static abstract class Metadata {

    abstract int id();

    abstract String title();

    abstract String thumbnailUrl();

    static Builder newBuilder() {
      return new AutoValue_Story_Metadata.Builder();
    }

    @AutoValue.Builder
    abstract static class Builder {

      abstract Builder setId(final int id);

      abstract Builder setTitle(final String title);

      abstract Builder setThumbnailUrl(final String thumbnailUrl);

      public abstract Metadata build();
    }
  }
}
