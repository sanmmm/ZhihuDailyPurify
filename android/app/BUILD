filegroup(
    name = "android_code",
    srcs = glob(["src/main/java/io/github/izzyleung/zhihudailypurify/**/*.java"]),
    visibility = ["//android:__pkg__"],
)

filegroup(
    name = "resource_files",
    srcs = glob(["src/main/res/**"]),
    visibility = ["//android:__pkg__"],
)

filegroup(
    name = "manifest",
    srcs = ["src/main/AndroidManifest.xml"],
    visibility = ["//android:__pkg__"],
)

android_library(
    name = "app",
    custom_package = "io.github.izzyleung.zhihudailypurify",
    srcs = [":android_code"],
    manifest = ":manifest",
    resource_files = [":resource_files"],
    visibility = ["//android:__pkg__"],
    deps = [
        "//android/widgets:all_widgets",
        "//android/news_fetch:news_fetch",
        "//third_party:android_app_dependencies",
        "//proto:zhihu_daily_purify_java_proto",
        "@com_google_protobuf_javalite//:protobuf_java_lite",
    ],
)