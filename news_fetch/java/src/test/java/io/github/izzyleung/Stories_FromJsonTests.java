package io.github.izzyleung;

import io.reactivex.subscribers.TestSubscriber;
import java.io.IOException;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

public class Stories_FromJsonTests {

  private TestSubscriber<Story> storySubscriber;

  @Before
  public void setUp() {
    storySubscriber = new TestSubscriber<>();
  }

  @Test
  public void testErrorResponse() throws IOException {
    setUpTest("json/error_stories.json");

    storySubscriber.assertError(e -> e instanceof JSONException);
    storySubscriber.assertNotComplete();
  }

  @Test
  public void testNoStories() throws IOException {
    setUpTest("json/no_stories.json");

    storySubscriber.assertNoValues();
    storySubscriber.assertComplete();
  }

  @Test
  public void testEmptyStories() throws IOException {
    setUpTest("json/empty_stories.json");

    storySubscriber.assertNoValues();
    storySubscriber.assertComplete();
  }

  @Test
  public void testNoThumbnailUrl() throws IOException {
    setUpTest("json/no_images.json");

    storySubscriber.assertValue(s -> s.thumbnailUrl().isEmpty());
    storySubscriber.assertComplete();
  }

  @Test
  public void testEmptyThumbnailUrl() throws IOException {
    setUpTest("json/empty_images.json");

    storySubscriber.assertValue(s -> s.thumbnailUrl().isEmpty());
    storySubscriber.assertComplete();
  }

  @Test
  public void testMultipleThumbnailUrls() throws IOException {
    setUpTest("json/multiple_images.json");

    storySubscriber.assertValue(s -> s.thumbnailUrl().equals("should be selected"));
    storySubscriber.assertComplete();
  }

  @Test
  public void testNormalScenario() throws IOException {
    setUpTest("json/normal.json");

    storySubscriber.assertValueAt(0, s -> {
      boolean idMatch = s.id() == 1;
      boolean titleMatch = s.title().equals("first title");
      boolean thumbnailUrlMath = s.thumbnailUrl().equals("image url 1");

      return idMatch && titleMatch && thumbnailUrlMath;
    });
    storySubscriber.assertValueAt(1, s -> {
      boolean idMatch = s.id() == 2;
      boolean titleMatch = s.title().equals("second title");
      boolean thumbnailUrlMath = s.thumbnailUrl().equals("image url 2");

      return idMatch && titleMatch && thumbnailUrlMath;
    });
    storySubscriber.assertComplete();
  }

  private void setUpTest(String testFileName) throws IOException {
    Stories.fromJson(Commons.openInputStream(testFileName)).subscribe(storySubscriber);
  }
}