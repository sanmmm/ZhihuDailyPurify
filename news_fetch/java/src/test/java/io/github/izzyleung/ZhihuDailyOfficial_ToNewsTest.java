package io.github.izzyleung;

import io.github.izzyleung.ZhihuDailyPurify.News;
import io.github.izzyleung.utils.Tuple;
import io.reactivex.subscribers.TestSubscriber;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

public class ZhihuDailyOfficial_ToNewsTest {

  private static final String date = "Date";
  private static final String storyTitle = "Story Title";
  private static final String thumbnailUrl = "Thumbnail URL";

  private ZhihuDailyOfficial official;
  private Tuple<Story, Document> tuple;
  private TestSubscriber<News> newsObserver;

  @Before
  public void setUp() {
    official = ZhihuDailyOfficial.of(date);
    newsObserver = new TestSubscriber<>();
  }

  @Test
  public void testNoQuestions() throws IOException {
    tuple = setUpTuple("html/no_questions.html");
    official.toNews(tuple).subscribe(newsObserver);

    newsObserver.assertNoValues();
    newsObserver.assertComplete();
  }

  @Test
  public void testNoQuestionTitle() throws IOException {
    tuple = setUpTuple("html/no_title.html");
    official.toNews(tuple).subscribe(newsObserver);

    // When question has no title in details page, default to title of the story it associates with.
    newsObserver.assertValue(news -> news.getQuestions(0).getTitle().equals(storyTitle));
    newsObserver.assertComplete();
  }

  @Test
  public void testEmptyQuestionTitle() throws IOException {
    tuple = setUpTuple("html/empty_question_title.html");
    official.toNews(tuple).subscribe(newsObserver);

    // When question has empty title in details page, default to title of the story it associates with.
    newsObserver.assertValue(news -> news.getQuestions(0).getTitle().equals(storyTitle));
    newsObserver.assertComplete();
  }


  @Test
  public void testNoQuestionUrl() throws IOException {
    tuple = setUpTuple("html/no_question_url.html");
    official.toNews(tuple).subscribe(newsObserver);

    newsObserver.assertNoValues();
    newsObserver.assertComplete();
  }

  @Test
  public void testInvalidQuestionUrl() throws IOException {
    tuple = setUpTuple("html/invalid_question_url.html");
    official.toNews(tuple).subscribe(newsObserver);

    newsObserver.assertNoValues();
    newsObserver.assertComplete();
  }

  @Test
  public void testNormalScenario() throws IOException {
    tuple = setUpTuple("html/normal.html");
    official.toNews(tuple).subscribe(newsObserver);

    newsObserver.assertValue(news -> {
      boolean sizeMatch = news.getQuestionsCount() == 2;

      ZhihuDailyPurify.Question first = news.getQuestions(0);
      boolean firstQuestionMatch =
          first.getTitle().equals("First") && first.getUrl().endsWith("1234567");

      ZhihuDailyPurify.Question second = news.getQuestions(1);
      boolean secondQuestionMatch =
          second.getTitle().equals("Second") && second.getUrl().endsWith("2345678");

      return sizeMatch && firstQuestionMatch && secondQuestionMatch;
    });
    newsObserver.assertComplete();
  }

  private Tuple<Story, Document> setUpTuple(String fileName) throws IOException {
    Story story = Story.newBuilder()
        .setId(1)
        .setTitle(storyTitle)
        .setThumbnailUrl(thumbnailUrl)
        .build();

    Document document = Jsoup.parse(Commons.openInputStream(fileName), null, "");

    return Tuple.create(story, document);
  }
}
