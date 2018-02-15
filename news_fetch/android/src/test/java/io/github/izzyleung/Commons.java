package io.github.izzyleung;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class Commons {

  private Commons() {

  }

  static InputStream openInputStream(String fileName) throws IOException {
    return new FileInputStream(
        System.getenv("TEST_SRCDIR") + "/ZhihuDailyPurify/news_fetch/test_files/" + fileName);
  }
}
