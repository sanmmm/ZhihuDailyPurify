load("@pip_dependencies//:requirements.bzl", "all_requirements")

load("@io_bazel_rules_docker//python:image.bzl", "py_image")
load("@io_bazel_rules_docker//container:container.bzl", "container_image")

filegroup(
  name = "python_code",
  srcs = glob(["**/*.py"], exclude = ["tests.py"]),
  visibility = ["//visibility:private"],
)

py_binary(
  name = "server",
  srcs = [":python_code"],
  data = [":database.ini"],
  deps = [
    "//proto:zhihu_daily_purify_py_proto",
  ] + all_requirements,
)

py_test(
  name = "server_tests",
  srcs = ["tests.py"],
  data = ["//news_fetch/test_files:test_files"],
  deps = [
    ":server",
    "//proto:zhihu_daily_purify_py_proto",
  ] + all_requirements,
  main = "tests.py",
  size = "small",
)

py_image(
  name = "zhihu_daily_purify_server_base",
  srcs = [":python_code"],
  deps = [":server"],
  main = "server.py",
)

container_image(
  name = "zhihu_daily_purify_server_docker",
  base = ":zhihu_daily_purify_server_base",
  directory = "/app/server",
  files = [":database.ini"],
  cmd = [""],
)
