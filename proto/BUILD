load("@pip_dependencies//:requirements.bzl", "requirement")

objc_library(
  name = "zhihu_daily_purify_objc_proto",
  srcs = [":zhihu_daily_purify_proto_compiled_objc"],
  copts = ["-fno-objc-arc"],
  deps = [
    "@com_google_protobuf//:protobuf_objc",
    "@com_google_protobuf//:objectivec",
  ],
)

genrule(
  name = "zhihu_daily_purify_proto_compiled_objc",
  srcs = ["zhihu_daily_purify.proto"],
  outs = [
    "ZhihuDailyPurify.pbobjc.m",
    "ZhihuDailyPurify.pbobjc.h",
  ],
  tools = ["@com_google_protobuf//:protoc"],
  cmd = "$(location @com_google_protobuf//:protoc) -I=proto --objc_out=$(@D) $<",
)

py_library(
  name = "zhihu_daily_purify_py_proto",
  srcs = [":zhihu_daily_purify_proto_compiled_python"],
  deps = [
    requirement("six"),
    requirement("protobuf"),
  ],
  visibility = ["//news_fetch/server:__subpackages__"],
)

genrule(
  name = "zhihu_daily_purify_proto_compiled_python",
  srcs = ["zhihu_daily_purify.proto"],
  outs = ["zhihu_daily_purify_pb2.py"],
  tools = ["@com_google_protobuf//:protoc"],
  cmd = "$(location @com_google_protobuf//:protoc) -I=proto --python_out=$(@D) $<",
)

java_library(
  name = "zhihu_daily_purify_java_proto",
  srcs = [":zhihu_daily_purify_proto_compiled_java"],
  deps = ["@com_google_protobuf//:protobuf_java"],
  visibility = [
    "//android/app:__subpackages__",
    "//news_fetch/android:__subpackages__",
  ]
)

genrule(
  name = "zhihu_daily_purify_proto_compiled_java",
  srcs = ["zhihu_daily_purify.proto"],
  outs = ["ZhihuDailyPurify.java"],
  tools = ["@com_google_protobuf//:protoc"],
  cmd = "$(location @com_google_protobuf//:protoc) -I=proto --java_out=$(@D) $<" +
      " && mv $(@D)/io/github/izzyleung/ZhihuDailyPurify.java $(@D)",
)
