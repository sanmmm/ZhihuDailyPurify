filegroup(
    name = "python_code",
    srcs = glob(
        ["**/*.py"],
        exclude = ["tests.py"],
    ),
    visibility = ["//visibility:private"],
)

py_library(
    name = "news_fetch_python",
    srcs = [
        ":python_code",
    ],
    visibility = ["//news_fetch/server:__subpackages__"],
    deps = [
        "//proto:zhihu_daily_purify_py_proto",
        "@bs4",
        "@protobuf_python",
        "@pypi_six//:six",
    ],
)

py_test(
    name = "news_fetch_python_tests",
    size = "small",
    srcs = ["tests.py"],
    data = ["//news_fetch/test_files"],
    main = "tests.py",
    deps = [
        ":news_fetch_python",
        "//proto:zhihu_daily_purify_py_proto",
        "@bs4",
        "@protobuf_python",
        "@pypi_six//:six",
    ],
)
