java_library(
    name = "news_fetch",
    srcs = glob(["src/main/java/io/github/izzyleung/**/*.java"]),
    visibility = ["//android:__subpackages__"],
    deps = [
        "//third_party:news_fetch_dependencies",
        "//proto:zhihu_daily_purify_java_proto",
    ],
)

java_library(
    name = "news_fetch_test",
    srcs = glob(["src/test/java/io/github/izzyleung/**/*.java"]),
    deps = [],
)
