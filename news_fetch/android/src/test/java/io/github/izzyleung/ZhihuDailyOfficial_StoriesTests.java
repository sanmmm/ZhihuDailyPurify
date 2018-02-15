package io.github.izzyleung;

import io.github.izzyleung.utils.Story;
import io.reactivex.subscribers.TestSubscriber;
import java.io.IOException;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

public class ZhihuDailyOfficial_StoriesTests {

  private TestSubscriber<Story> storySubscriber;

  @Before
  public void setUp() {
    storySubscriber = new TestSubscriber<>();
  }

  @Test
  public void testErrorResponse() throws IOException {
    setUpStoriesTest("json/stories/error_stories.json");

    storySubscriber.assertError(e -> e instanceof JSONException);
    storySubscriber.assertNotComplete();
  }

  @Test
  public void testNoStories() throws IOException {
    setUpStoriesTest("json/stories/no_stories.json");

    storySubscriber.assertNoValues();
    storySubscriber.assertComplete();
  }

  @Test
  public void testEmptyStories() throws IOException {
    setUpStoriesTest("json/stories/empty_stories.json");

    storySubscriber.assertNoValues();
    storySubscriber.assertComplete();
  }

  @Test
  public void testNoThumbnailUrl() throws IOException {
    setUpStoriesTest("json/stories/no_images.json");

    storySubscriber.assertValue(s -> s.thumbnailUrl().isEmpty());
    storySubscriber.assertComplete();
  }

  @Test
  public void testEmptyThumbnailUrl() throws IOException {
    setUpStoriesTest("json/stories/empty_images.json");

    storySubscriber.assertValue(s -> s.thumbnailUrl().isEmpty());
    storySubscriber.assertComplete();
  }

  @Test
  public void testMultipleThumbnailUrls() throws IOException {
    setUpStoriesTest("json/stories/multiple_images.json");

    storySubscriber.assertValue(s -> s.thumbnailUrl().equals("should be selected"));
    storySubscriber.assertComplete();
  }

  private void setUpStoriesTest(String testFileName) throws IOException {
    ZhihuDailyOfficial.stories(Commons.openInputStream(testFileName)).subscribe(storySubscriber);
  }
}