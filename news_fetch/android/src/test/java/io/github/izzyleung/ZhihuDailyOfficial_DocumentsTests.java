package io.github.izzyleung;

import io.reactivex.subscribers.TestSubscriber;
import java.io.IOException;
import org.json.JSONException;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

public class ZhihuDailyOfficial_DocumentsTests {

  private TestSubscriber<Document> documentSubscriber;

  @Before
  public void setUp() {
    documentSubscriber = new TestSubscriber<>();
  }

  @Test
  public void testErrorResponse() throws IOException {
    setUpDocumentsTest("json/news/error_story_detail.json");

    documentSubscriber.assertError(e -> e instanceof JSONException);
  }

  @Test
  public void testNoDocument() throws IOException {
    setUpDocumentsTest("json/news/no_document.json");

    documentSubscriber.assertValueCount(1);
    documentSubscriber.assertComplete();
  }

  private void setUpDocumentsTest(String fileName) throws IOException {
    ZhihuDailyOfficial.documents(Commons.openInputStream(fileName)).subscribe(documentSubscriber);
  }
}
