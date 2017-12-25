workspace(name = "ZhihuDailyPurify")

android_sdk_repository(
    name = "androidsdk",
    api_level = 27,
    build_tools_version = "27.0.2"
)

load("//third_party:gen_deps.bzl", 
        "generate_maven_dependencies", 
        "setup_protobuf",
        "setup_rules_bazel",
        "setup_rules_docker"
)

generate_maven_dependencies()
setup_protobuf()
setup_rules_bazel()
setup_rules_docker()

# Set up pip dependencies
load("@io_bazel_rules_python//python:pip.bzl", "pip_import")

pip_import(
    name = "pip_dependencies",
    requirements = "//server:requirements.txt",
)

load("@pip_dependencies//:requirements.bzl", "pip_install")
pip_install()

# Set up docker
load(
    "@io_bazel_rules_docker//python:image.bzl",
    _py_image_repos = "repositories",
)

_py_image_repos()

load(
    "@io_bazel_rules_docker//container:container.bzl",
    "container_pull",
    container_repositories = "repositories",
)

container_repositories()
