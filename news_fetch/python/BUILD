filegroup(
  name = "python_code",
  srcs = glob(["**/*.py"], exclude = ["tests.py"]),
  visibility = ["//visibility:private"],
)

py_library(
  name = "news_fetch_python",
  srcs = [
    ":python_code",
  ],
  deps = [
    "//proto:zhihu_daily_purify_py_proto",
    "@pypi_certifi//:certifi",
    "@bs4//:bs4",
    "@pypi_six//:six",
    "@protobuf_python//:protobuf_python",
  ],
  visibility = ["//news_fetch/server:__subpackages__"],
)

py_test(
  name = "news_fetch_python_tests",
  srcs = ["tests.py"],
  data = ["//news_fetch/test_files:test_files"],
  deps = [
    ":news_fetch_python",
    "//proto:zhihu_daily_purify_py_proto",
    "@bs4//:bs4",
    "@pypi_six//:six",
    "@protobuf_python//:protobuf_python",
  ],
  main = "tests.py",
  size = "small",
)