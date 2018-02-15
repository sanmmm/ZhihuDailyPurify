package io.github.izzyleung;

import io.github.izzyleung.ZhihuDailyPurify.News;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.github.izzyleung.utils.Network;
import io.github.izzyleung.utils.Story;
import io.github.izzyleung.utils.Triple;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class ZhihuDailyOfficial {

  private ZhihuDailyOfficial() {

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

  private static Flowable<Story> convertToStory(JSONObject j) throws JSONException {
    Flowable<Integer> idFlowable = Flowable.just(j.getInt(ZhihuDaily.KEY_ID));
    Flowable<String> titleFlowable = Flowable.just(j.getString(ZhihuDaily.KEY_TITLE));
    Flowable<String> thumbnailUrlFlowable = Flowable.just(j)
        .filter(x -> x.has(ZhihuDaily.KEY_IMAGES))
        .map(x -> x.getJSONArray(ZhihuDaily.KEY_IMAGES))
        .filter(x -> x.length() > 0)
        .map(x -> (String) x.get(0))
        .switchIfEmpty(Flowable.just(""));

    return Flowable
        .zip(idFlowable, titleFlowable, thumbnailUrlFlowable, (id, title, thumbnailUrl) ->
            Story.newBuilder()
                .setId(id)
                .setTitle(title)
                .setThumbnailUrl(thumbnailUrl)
                .build()
        );
  }

  static Flowable<Document> documents(InputStream in) {
    return Flowable.just(mkString(in))
        .map(JSONObject::new)
        .filter(j -> j.has(ZhihuDaily.KEY_BODY))
        .map(j -> Jsoup.parse(j.getString(ZhihuDaily.KEY_BODY)))
        .defaultIfEmpty(new Document(""));
  }

  static Flowable<Story> stories(InputStream in) {
    return Flowable.just(mkString(in))
        .map(JSONObject::new)
        .filter(j -> j.has(ZhihuDaily.KEY_STORIES))
        .map(j -> j.getJSONArray(ZhihuDaily.KEY_STORIES))
        .flatMap(j -> Flowable.fromArray(toArray(j)))
        .flatMap(ZhihuDailyOfficial::convertToStory);
  }

  private static Flowable<News> convertToNews(
      Triple<String, Story, Document> triple) {
    return getQuestions(triple)
        .filter(qs -> !qs.isEmpty())
        .map(qs -> ZhihuDailyPurify.News
            .newBuilder()
            .setDate(triple.left())
            .setTitle(triple.middle().title())
            .setThumbnailUrl(triple.middle().thumbnailUrl())
            .addAllQuestions(qs)
            .build())
        .toFlowable();
  }

  private static Single<List<ZhihuDailyPurify.Question>> getQuestions(
      Triple<String, Story, Document> triple) {
    String dailyTitle = triple.middle().title();

    return Flowable.fromIterable(getQuestionElements(triple.right()))
        .map(questionElement ->
            ZhihuDailyPurify.Question
                .newBuilder()
                .setTitle(obtainQuestionTitle(questionElement).orElse(dailyTitle))
                .setUrl(obtainQuestionUrl(questionElement).orElse(""))
                .build())
        .filter(ZhihuDaily::isValidZhihuQuestion)
        .toList();
  }

  private static Elements getQuestionElements(Document document) {
    return document.select(Selectors.QUESTION);
  }

  private static Optional<String> obtainQuestionTitle(Element questionElement) {
    return Optional
        .ofNullable(questionElement.select(Selectors.QUESTION_TITLES).first())
        .map(Element::text)
        .filter(s -> !s.isEmpty());
  }

  private static Optional<String> obtainQuestionUrl(Element questionElement) {
    return Optional
        .ofNullable(questionElement.select(Selectors.QUESTION_LINKS).first())
        .map(e -> e.attr("href"));
  }

  public static Single<ZhihuDailyPurify.Feed> feedForDate(String date) throws IOException {
    Flowable<Story> stories = stories(ZhihuDaily.storiesForDate(date));
    Flowable<Document> documents = stories.map(ZhihuDaily::storyDetail)
        .flatMap(ZhihuDailyOfficial::documents);
    Flowable<String> dates = stories.map(s -> date);

    Single<ZhihuDailyPurify.Feed.Builder> builder = Single.just(ZhihuDailyPurify.Feed.newBuilder());
    Single<List<ZhihuDailyPurify.News>> news = Flowable
        .zip(dates, stories, documents, Triple::create)
        .flatMap(ZhihuDailyOfficial::convertToNews)
        .toList();

    return Single.zip(builder, news, (b, n) -> b.addAllNews(n).build());
  }

  private static class ZhihuDaily {

    private static final String ZHIHU_DAILY_NEWS_BASE = "https://news-at.zhihu.com/api/4/news/";
    private static final String ZHIHU_DAILY_NEWS_BEFORE = ZHIHU_DAILY_NEWS_BASE + "before/";

    private static final String KEY_STORIES = "stories";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_IMAGES = "images";
    private static final String KEY_BODY = "body";

    private static boolean isValidZhihuQuestion(ZhihuDailyPurify.Question question) {
      return question.getUrl().contains("zhihu.com/question/");
    }

    private static InputStream storiesForDate(String date) throws IOException {
      return Network.openInputStream(ZHIHU_DAILY_NEWS_BEFORE + date);
    }

    private static InputStream storyDetail(Story story) throws IOException {
      return Network.openInputStream(ZHIHU_DAILY_NEWS_BASE + story.id());
    }
  }

  private static class Selectors {

    private static final String QUESTION = "div.question";
    private static final String QUESTION_TITLES = "h2.question-title";
    private static final String QUESTION_LINKS = "div.view-more a";
  }
}