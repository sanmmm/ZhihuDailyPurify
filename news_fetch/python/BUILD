load("@pip_dependencies//:requirements.bzl", "requirement")

filegroup(
  name = "python_code",
  srcs = glob(["**/*.py"], exclude = ["tests.py"]),
  visibility = ["//visibility:private"],
)

py_library(
  name = "news_fetch_python",
  srcs = [":python_code"],
  deps = [
    "//proto:zhihu_daily_purify_py_proto",
    requirement("beautifulsoup4"),
    requirement("pathlib"),
    requirement("six"),
    requirement("protobuf"),
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
    requirement("beautifulsoup4"),
    requirement("pathlib"),
    requirement("six"),
    requirement("protobuf"),
  ],
  main = "tests.py",
  size = "small",
)