load("@io_bazel_rules_docker//python:image.bzl", "py_image")
load("@io_bazel_rules_docker//container:container.bzl", "container_image")

filegroup(
    name = "python_code",
    srcs = glob(
        ["**/*.py"],
        exclude = ["smoke.py"],
    ),
    visibility = ["//visibility:private"],
)

py_binary(
    name = "server",
    srcs = [":python_code"],
    data = [":database.ini"],
    deps = [
        "//news_fetch/python:news_fetch_python",
        "//proto:zhihu_daily_purify_py_proto",
        "@pypi_bottle//:bottle",
        "@pypi_pymongo//:pymongo",
        "@pypi_six//:six",
    ],
)

py_image(
    name = "zhihu_daily_purify_server_base",
    srcs = [":python_code"],
    main = "server.py",
    deps = [":server"],
)

container_image(
    name = "zhihu_daily_purify_server_docker",
    base = ":zhihu_daily_purify_server_base",
    cmd = [""],
    directory = "/app/news_fetch/server",
    files = [":database.ini"],
)
