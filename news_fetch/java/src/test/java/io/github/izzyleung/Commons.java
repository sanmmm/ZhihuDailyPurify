package io.github.izzyleung;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class Commons {

  private Commons() {

  }

  static InputStream openInputStream(String fileName) throws IOException {
    String base = System.getenv("TEST_SRCDIR");
    return new FileInputStream(base + "/ZhihuDailyPurify/news_fetch/test_files/" + fileName);
  }
}
