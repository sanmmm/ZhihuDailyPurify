package(
    default_visibility = ["//android:__pkg__"],
)

filegroup(
    name = "android_code",
    srcs = glob(["src/main/java/io/github/izzyleung/zhihudailypurify/**/*.java"]),
)

filegroup(
    name = "resource_files",
    srcs = glob(["src/main/res/**"]),
)

filegroup(
    name = "manifest",
    srcs = ["src/main/AndroidManifest.xml"],
)

android_library(
    name = "app",
    srcs = [":android_code"],
    custom_package = "io.github.izzyleung.zhihudailypurify",
    manifest = ":manifest",
    resource_files = [":resource_files"],
    deps = [
        "//android/widgets:all_widgets",
        "//news_fetch/java:news_fetch_java",
        "//proto:zhihu_daily_purify_java_proto",
        "//third_party:android_app_dependencies",
        "@com_google_protobuf//:protobuf_java",
    ],
)
