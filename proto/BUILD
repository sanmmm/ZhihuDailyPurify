genrule(
    name = "zhihu_daily_purify_proto_compiled_swift",
    srcs = ["zhihu_daily_purify.proto"],
    outs = ["zhihu_daily_purify.pb.swift"],
    tools = [
        "@com_google_protobuf//:protoc",
        "@swift_protobuf//:protoc_swift_plugin",
    ],
    cmd = "$(location @com_google_protobuf//:protoc) --plugin=protoc-gen-custom=$(location @swift_protobuf//:protoc_swift_plugin) -I=proto --swift_out=$(@D) $<",
)

py_library(
    name = "zhihu_daily_purify_py_proto",
    srcs = [":zhihu_daily_purify_proto_compiled_python"],
    visibility = ["//server:__subpackages__"],
)

genrule(
    name = "zhihu_daily_purify_proto_compiled_python",
    srcs = ["zhihu_daily_purify.proto"],
    outs = ["zhihu_daily_purify_pb2.py"],
    tools = ["@com_google_protobuf//:protoc"],
    cmd = "$(location @com_google_protobuf//:protoc) -I=proto --python_out=$(@D) $<",
)

java_lite_proto_library(
    name = "zhihu_daily_purify_java_proto",
    deps = [":zhihu_daily_purify_proto"],
    visibility = [
        "//android:__subpackages__",
    ],
)

proto_library(
    name = "zhihu_daily_purify_proto",
    srcs = ["zhihu_daily_purify.proto"],
)
