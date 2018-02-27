package io.github.izzyleung;

import io.github.izzyleung.utils.Network;
import io.github.izzyleung.utils.Tuple;
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

public class Stories {

  private Stories() {

  }

  static Flowable<Tuple<Story, Document>> of(String date) throws IOException {
    Flowable<Story> stories = Stories.fromJson(Stories.storiesOf(date));
    Flowable<Document> documents = stories.map(Stories::details).flatMap(Stories::documents);

    return Flowable.zip(stories, documents, Tuple::create);
  }

  static Flowable<Story> fromJson(InputStream in) {
    return Flowable.just(mkString(in))
        .map(JSONObject::new)
        .filter(j -> j.has(ZhihuDaily.KEY_STORIES))
        .map(j -> j.getJSONArray(ZhihuDaily.KEY_STORIES))
        .flatMap(j -> Flowable.fromArray(toArray(j)))
        .flatMap(Stories::toStory);
  }

  private static Flowable<Story> toStory(JSONObject j) throws JSONException {
    Flowable<Integer> id = Flowable.just(j.getInt(ZhihuDaily.KEY_ID));
    Flowable<String> title = Flowable.just(j.getString(ZhihuDaily.KEY_TITLE));
    Flowable<String> thumbnailUrl = Flowable.just(j)
        .filter(x -> x.has(ZhihuDaily.KEY_IMAGES))
        .map(x -> x.getJSONArray(ZhihuDaily.KEY_IMAGES))
        .filter(x -> x.length() > 0)
        .map(x -> (String) x.get(0))
        .switchIfEmpty(Flowable.just(""));

    return Flowable.zip(id, title, thumbnailUrl, (storyId, storyTitle, url) ->
            Story.newBuilder()
                .setId(storyId)
                .setTitle(storyTitle)
                .setThumbnailUrl(url)
                .build()
        );
  }

  private static InputStream storiesOf(String date) throws IOException {
    return Network.openInputStream(ZhihuDaily.ZHIHU_DAILY_NEWS_BEFORE + date);
  }

  private static InputStream details(Story story) throws IOException {
    return Network.openInputStream(ZhihuDaily.ZHIHU_DAILY_NEWS_BASE + story.id());
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
