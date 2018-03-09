java_library(
    name = "news_fetch_java",
    srcs = glob(["src/main/java/io/github/izzyleung/**/*.java"]),
    visibility = ["//android/app:__subpackages__"],
    deps = [
        "//proto:zhihu_daily_purify_java_proto",
        "//third_party:news_fetch_android_dependencies",
    ],
)

java_test(
    name = "news_fetch_java_all_tests",
    size = "small",
    srcs = glob(["src/test/java/io/github/izzyleung/**/*.java"]),
    data = [
        "//news_fetch/test_files",
    ],
    test_class = "io.github.izzyleung.TestSuite",
    deps = [
        ":news_fetch_java",
        "//proto:zhihu_daily_purify_java_proto",
        "//third_party:news_fetch_android_dependencies",
        "//third_party:news_fetch_android_test_dependencies",
    ],
)
