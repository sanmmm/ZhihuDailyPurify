workspace(name = "ZhihuDailyPurify")

API_LEVEL = 27
BUILD_TOOLS_VERSION = "27.0.2"

android_sdk_repository(
  name = "androidsdk",
  api_level = API_LEVEL,
  build_tools_version = BUILD_TOOLS_VERSION,
)

load("//third_party:gen_deps.bzl", 
        "generate_android_support_libs_dependency",
        "generate_news_fetch_android_dependencies",
        "generate_other_android_dependencies",
        "generate_news_fetch_android_test_dependencies",
        "setup_protobuf",
        "setup_rules_python",
        "setup_rules_docker",
        "setup_rules_apple",
)

generate_android_support_libs_dependency(BUILD_TOOLS_VERSION)
generate_other_android_dependencies()

generate_news_fetch_android_dependencies()

generate_news_fetch_android_test_dependencies()

setup_protobuf()

setup_rules_python()
setup_rules_docker()
setup_rules_apple()

# Set up pip dependencies
load("@io_bazel_rules_python//python:pip.bzl", "pip_import")

pip_import(
  name = "pip_dependencies",
  requirements = "//news_fetch/server:requirements.txt",
)

load("@pip_dependencies//:requirements.bzl", "pip_install")
pip_install()

# Set up docker
load(
  "@io_bazel_rules_docker//python:image.bzl",
  _py_image_repos = "repositories",
)

_py_image_repos()
