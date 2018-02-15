java_library(
  name = "android",
  srcs = glob(["src/main/java/io/github/izzyleung/**/*.java"]),
  visibility = ["//android/app:__subpackages__"],
  deps = [
    "//third_party:news_fetch_android_dependencies",
    "//proto:zhihu_daily_purify_java_proto",
  ],
)

java_test(
  name = "news_fetch_android_all_tests",
  test_class = "io.github.izzyleung.TestSuite",
  size = "small",
  srcs = glob(["src/test/java/io/github/izzyleung/**/*.java"]),
  data = [
    "//news_fetch/test_files"
  ],
  deps = [
    ":android",
    "//third_party:news_fetch_android_dependencies",
    "//proto:zhihu_daily_purify_java_proto",
    "//third_party:news_fetch_android_test_dependencies",
  ],
)
