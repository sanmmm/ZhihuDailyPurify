package io.github.izzyleung;

import io.github.izzyleung.ZhihuDailyPurify.News;
import io.github.izzyleung.utils.Story;
import io.github.izzyleung.utils.Triple;
import io.reactivex.subscribers.TestSubscriber;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

public class ZhihuDailyOfficial_ConvertToNewsTest {

  private static final String date = "Date";
  private static final String storyTitle = "Story Title";
  private static final String thumbnailUrl = "Thumbnail URL";

  private Triple<String, Story, Document> triple;
  private TestSubscriber<News> newsObserver;

  @Before
  public void setUp() {
    newsObserver = new TestSubscriber<>();
  }

  @Test
  public void testNoQuestions() throws IOException {
    triple = setUpTriple("html/no_questions.html");
    ZhihuDailyOfficial.convertToNews(triple).subscribe(newsObserver);

    newsObserver.assertNoValues();
    newsObserver.assertComplete();
  }

  @Test
  public void testNoQuestionTitle() throws IOException {
    triple = setUpTriple("html/no_title.html");
    ZhihuDailyOfficial.convertToNews(triple).subscribe(newsObserver);

    // When question has no title in details page, default to title of the story it associates with.
    newsObserver.assertValue(news -> news.getQuestions(0).getTitle().equals(storyTitle));
    newsObserver.assertComplete();
  }

  @Test
  public void testEmptyQuestionTitle() throws IOException {
    triple = setUpTriple("html/empty_question_title.html");
    ZhihuDailyOfficial.convertToNews(triple).subscribe(newsObserver);

    // When question has empty title in details page, default to title of the story it associates with.
    newsObserver.assertValue(news -> news.getQuestions(0).getTitle().equals(storyTitle));
    newsObserver.assertComplete();
  }


  @Test
  public void testNoQuestionUrl() throws IOException {
    triple = setUpTriple("html/no_question_url.html");
    ZhihuDailyOfficial.convertToNews(triple).subscribe(newsObserver);

    newsObserver.assertNoValues();
    newsObserver.assertComplete();
  }

  @Test
  public void testInvalidQuestionUrl() throws IOException {
    triple = setUpTriple("html/invalid_question_url.html");
    ZhihuDailyOfficial.convertToNews(triple).subscribe(newsObserver);

    newsObserver.assertNoValues();
    newsObserver.assertComplete();
  }

  @Test
  public void testNormalScenario() throws IOException {
    triple = setUpTriple("html/normal.html");
    ZhihuDailyOfficial.convertToNews(triple).subscribe(newsObserver);

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

  private Triple<String, Story, Document> setUpTriple(String fileName) throws IOException {
    Story story = Story.newBuilder()
        .setId(1)
        .setTitle(storyTitle)
        .setThumbnailUrl(thumbnailUrl)
        .build();

    Document document = Jsoup.parse(Commons.openInputStream(fileName), null, "");

    return Triple.create(date, story, document);
  }
}
