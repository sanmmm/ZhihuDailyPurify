package io.github.izzyleung;

import com.google.auto.value.AutoValue;
import io.github.izzyleung.ZhihuDailyPurify.News;
import io.reactivex.Flowable;
import io.reactivex.Single;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@AutoValue
public abstract class ZhihuDailyOfficial {

  public abstract String date();

  public static ZhihuDailyOfficial of(String date) {
    return new AutoValue_ZhihuDailyOfficial(date);
  }

  public Single<ZhihuDailyPurify.Feed> feed() throws IOException {
    Flowable<Story> stories = Stories.of(this.date());

    Single<ZhihuDailyPurify.Feed.Builder> builder = Single.just(ZhihuDailyPurify.Feed.newBuilder());
    Single<List<ZhihuDailyPurify.News>> news = stories.flatMap(this::newsFrom).toList();

    return Single.zip(builder, news, (b, n) -> b.addAllNews(n).build());
  }

  Flowable<News> newsFrom(Story story) {
    return questionsFrom(story)
        .filter(qs -> !qs.isEmpty())
        .map(qs -> ZhihuDailyPurify.News
            .newBuilder()
            .setDate(this.date())
            .setTitle(story.metadata().title())
            .setThumbnailUrl(story.metadata().thumbnailUrl())
            .addAllQuestions(qs)
            .build())
        .toFlowable();
  }

  private static Single<List<ZhihuDailyPurify.Question>> questionsFrom(Story story) {
    String dailyTitle = story.metadata().title();

    return Flowable.fromIterable(questionElementsFrom(story.document()))
        .map(questionElement ->
            ZhihuDailyPurify.Question
                .newBuilder()
                .setTitle(questionTitleFrom(questionElement).orElse(dailyTitle))
                .setUrl(questionUrlFrom(questionElement).orElse(""))
                .build())
        .filter(ZhihuDailyOfficial::isValidZhihuQuestion)
        .toList();
  }

  private static Elements questionElementsFrom(Document document) {
    return document.select(Selectors.QUESTION);
  }

  private static Optional<String> questionTitleFrom(Element questionElement) {
    return Optional.ofNullable(questionElement.select(Selectors.QUESTION_TITLES).first())
        .map(Element::text)
        .filter(s -> !s.isEmpty());
  }

  private static Optional<String> questionUrlFrom(Element questionElement) {
    return Optional.ofNullable(questionElement.select(Selectors.QUESTION_LINKS).first())
        .map(e -> e.attr("href"));
  }

  private static boolean isValidZhihuQuestion(ZhihuDailyPurify.Question question) {
    return question.getUrl().contains("zhihu.com/question/");
  }

  private static class Selectors {

    private static final String QUESTION = "div.question";
    private static final String QUESTION_TITLES = "h2.question-title";
    private static final String QUESTION_LINKS = "div.view-more a";
  }
}
