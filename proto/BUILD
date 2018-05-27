objc_library(
    name = "zhihu_daily_purify_objc_proto",
    srcs = ["ZhihuDailyPurify.pbobjc.m"],
    hdrs = ["ZhihuDailyPurify.pbobjc.h"],
    copts = ["-fno-objc-arc"],
    visibility = ["//news_fetch/objc:__subpackages__"],
    deps = [
        "@com_google_protobuf//:objectivec",
        "@com_google_protobuf//:protobuf_objc",
    ],
)

genrule(
    name = "zhihu_daily_purify_proto_compiled_objc",
    srcs = ["zhihu_daily_purify.proto"],
    outs = [
        "ZhihuDailyPurify.pbobjc.m",
        "ZhihuDailyPurify.pbobjc.h",
    ],
    cmd = "$(location @com_google_protobuf//:protoc) -I=proto --objc_out=$(@D) $<",
    tools = ["@com_google_protobuf//:protoc"],
)

py_library(
    name = "zhihu_daily_purify_py_proto",
    srcs = [":zhihu_daily_purify_proto_compiled_python"],
    visibility = [
        "//news_fetch/python:__subpackages__",
        "//news_fetch/server:__subpackages__",
    ],
    deps = [
        "@protobuf_python",
        "@pypi_six//:six",
    ],
)

genrule(
    name = "zhihu_daily_purify_proto_compiled_python",
    srcs = ["zhihu_daily_purify.proto"],
    outs = ["zhihu_daily_purify_pb2.py"],
    cmd = "$(location @com_google_protobuf//:protoc) -I=proto --python_out=$(@D) $<",
    tools = ["@com_google_protobuf//:protoc"],
)

java_library(
    name = "zhihu_daily_purify_java_proto",
    srcs = [":zhihu_daily_purify_proto_compiled_java"],
    visibility = [
        "//android/app:__subpackages__",
        "//news_fetch/java:__subpackages__",
    ],
    deps = ["@com_google_protobuf//:protobuf_java"],
)

genrule(
    name = "zhihu_daily_purify_proto_compiled_java",
    srcs = ["zhihu_daily_purify.proto"],
    outs = ["ZhihuDailyPurify.java"],
    cmd = "$(location @com_google_protobuf//:protoc) -I=proto --java_out=$(@D) $<" +
          " && mv $(@D)/io/github/izzyleung/ZhihuDailyPurify.java $(@D)",
    tools = ["@com_google_protobuf//:protoc"],
)
