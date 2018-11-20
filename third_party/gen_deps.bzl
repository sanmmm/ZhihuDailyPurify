load("@bazel_tools//tools/build_defs/repo:maven_rules.bzl", "maven_aar", "maven_jar")

def generate_android_support_libs_dependency(android_build_tools_version):
    maven_aar(
        name = "support_v4",
        settings = "//third_party:google_maven.xml",
        artifact = "com.android.support:support-v4:{}".format(android_build_tools_version),
        deps = [
            "@support_compat//aar",
            "@support_media_compat//aar",
            "@support_core_utils//aar",
            "@support_core_ui//aar",
            "@support_fragment//aar",
        ],
    )

    maven_aar(
        name = "support_appcompat_v7",
        settings = "//third_party:google_maven.xml",
        artifact = "com.android.support:appcompat-v7:{}".format(android_build_tools_version),
        deps = [
            "@support_annotations//jar",
            "@support_core_utils//aar",
            "@support_fragment//aar",
            "@support_vector_drawable//aar",
            "@support_animated_vector_drawable//aar",
        ],
    )

    maven_aar(
        name = "support_cardview_v7",
        settings = "//third_party:google_maven.xml",
        artifact = "com.android.support:cardview-v7:{}".format(android_build_tools_version),
        deps = [
            "@support_annotations//jar",
        ],
    )

    maven_aar(
        name = "support_recyclerview_v7",
        settings = "//third_party:google_maven.xml",
        artifact = "com.android.support:recyclerview-v7:{}".format(android_build_tools_version),
        deps = [
            "@support_annotations//jar",
            "@support_compat//aar",
            "@support_core_ui//aar",
        ],
    )

    maven_aar(
        name = "support_design",
        settings = "//third_party:google_maven.xml",
        artifact = "com.android.support:design:{}".format(android_build_tools_version),
        deps = [
            "@support_v4//aar",
            "@support_appcompat_v7//aar",
            "@support_recyclerview_v7//aar",
            "@support_transition//aar",
        ],
    )

    maven_aar(
        name = "support_compat",
        settings = "//third_party:google_maven.xml",
        artifact = "com.android.support:support-compat:{}".format(android_build_tools_version),
        deps = [
            "@support_annotations//jar",
            "@arch_lifecycle_runtime//aar",
        ],
    )

    maven_aar(
        name = "support_media_compat",
        settings = "//third_party:google_maven.xml",
        artifact = "com.android.support:support-media-compat:{}".format(android_build_tools_version),
        deps = [
            "@support_annotations//jar",
            "@support_compat//aar",
        ],
    )

    maven_aar(
        name = "support_core_utils",
        settings = "//third_party:google_maven.xml",
        artifact = "com.android.support:support-core-utils:{}".format(android_build_tools_version),
        deps = [
            "@support_annotations//jar",
            "@support_compat//aar",
        ],
    )

    maven_aar(
        name = "support_core_ui",
        settings = "//third_party:google_maven.xml",
        artifact = "com.android.support:support-core-ui:{}".format(android_build_tools_version),
        deps = [
            "@support_annotations//jar",
            "@support_compat//aar",
        ],
    )

    maven_aar(
        name = "support_fragment",
        settings = "//third_party:google_maven.xml",
        artifact = "com.android.support:support-fragment:{}".format(android_build_tools_version),
        deps = [
            "@support_compat//aar",
            "@support_core_ui//aar",
            "@support_core_utils//aar",
            "@support_annotations//jar",
        ],
    )

    maven_jar(
        name = "support_annotations",
        settings = "//third_party:google_maven.xml",
        artifact = "com.android.support:support-annotations:{}".format(android_build_tools_version),
    )

    maven_aar(
        name = "support_vector_drawable",
        settings = "//third_party:google_maven.xml",
        artifact = "com.android.support:support-vector-drawable:{}".format(android_build_tools_version),
        deps = [
            "@support_annotations//jar",
            "@support_compat//aar",
        ],
    )

    maven_aar(
        name = "support_animated_vector_drawable",
        settings = "//third_party:google_maven.xml",
        artifact = "com.android.support:animated-vector-drawable:{}".format(android_build_tools_version),
        deps = [
            "@support_vector_drawable//aar",
            "@support_core_ui//aar",
        ],
    )

    maven_aar(
        name = "support_transition",
        settings = "//third_party:google_maven.xml",
        artifact = "com.android.support:transition:{}".format(android_build_tools_version),
        deps = [
            "@support_annotations//jar",
            "@support_compat//aar",
        ],
    )

    maven_aar(
        name = "arch_lifecycle_runtime",
        settings = "//third_party:google_maven.xml",
        artifact = "android.arch.lifecycle:runtime:1.0.3",
        deps = [
            "@arch_lifecycle//jar",
            "@arch_core//jar",
            "@support_annotations//jar",
        ],
    )

    maven_jar(
        name = "arch_lifecycle",
        settings = "//third_party:google_maven.xml",
        artifact = "android.arch.lifecycle:common:1.0.3",
    )

    maven_jar(
        name = "arch_core",
        settings = "//third_party:google_maven.xml",
        artifact = "android.arch.core:common:1.0.0",
    )

def generate_news_fetch_android_dependencies():
    maven_jar(
        name = "json",
        artifact = "org.json:json:20171018",
    )

    maven_jar(
        name = "jsoup",
        artifact = "org.jsoup:jsoup:1.7.3",
    )

    maven_jar(
        name = "reactive_stream",
        artifact = "org.reactivestreams:reactive-streams:1.0.2",
    )

    maven_jar(
        name = "rxjava",
        artifact = "io.reactivex.rxjava2:rxjava:2.1.7",
    )

    maven_jar(
        name = "autovalue",
        artifact = "com.google.auto.value:auto-value:1.5.3",
    )

def generate_news_fetch_android_test_dependencies():
    maven_jar(
        name = "junit",
        artifact = "junit:junit:4.12",
    )

def generate_other_android_dependencies():
    maven_aar(
        name = "universal_image_loader",
        artifact = "com.nostra13.universalimageloader:universal-image-loader:1.9.5",
    )

    maven_aar(
        name = "android_times_square",
        artifact = "com.squareup:android-times-square:1.6.4",
    )

    maven_aar(
        name = "recyclerview_stickyheaders",
        artifact = "com.eowise:recyclerview-stickyheaders:0.5.2",
    )


    maven_aar(
        name = "rxandroid",
        artifact = "io.reactivex.rxjava2:rxandroid:2.0.1",
        deps = [
            "@rxjava//jar",
        ]
    )

# Python Dependencies from PyPI
def generate_python_pypi_dependencies():
    native.new_http_archive(
        name = "bs4",
        url = "https://pypi.python.org/packages/fa/8d/1d14391fdaed5abada4e0f63543fef49b8331a34ca60c88bd521bcf7f782/beautifulsoup4-4.6.0.tar.gz",
        build_file_content = """
py_library(
    name = "bs4",
    srcs = glob([
        "*.py",
        "builder/__init__.py",
        "builder/_htmlparser.py",
    ], exclude=[
        "testing.py",
        "diagnose.py",
    ]),
    visibility = ["//visibility:public"],
)
        """,
        strip_prefix = "beautifulsoup4-4.6.0/bs4",
    )

    native.new_http_archive(
        name = "pypi_pymongo",
        url = "https://pypi.python.org/packages/69/8a/2384c55f4bd494eeb6104a9b35c36714ba1178dcd08ee5a73b92eed3d8c1/pymongo-3.6.0.tar.gz",
        build_file_content = """
package(default_visibility = ["//visibility:public"])

py_library(
    name = "bson",
    srcs = glob(["bson/*.py"]),
)

py_library(
    name = "gridfs",
    srcs = glob(["gridfs/*.py"]),
)

py_library(
    name = "pymongo",
    srcs = glob(["pymongo/*.py"]),
    deps = [
        ":bson",
        ":gridfs",
    ],
)
        """,
        strip_prefix = "pymongo-3.6.0",
    )

    native.new_http_archive(
        name = "pypi_six",
        url = "https://pypi.python.org/packages/16/d8/bc6316cf98419719bd59c91742194c111b6f2e85abac88e496adefaf7afe/six-1.11.0.tar.gz",
        build_file_content = """
py_library(
    name = "six",
    srcs = ["six.py"],
    visibility = ["//visibility:public"],
)
        """,
        strip_prefix = "six-1.11.0",
    )

    native.new_http_archive(
        name = "pypi_bottle",
        url = "https://pypi.python.org/packages/bd/99/04dc59ced52a8261ee0f965a8968717a255ea84a36013e527944dbf3468c/bottle-0.12.13.tar.gz",
        build_file_content = """
py_library(
    name = "bottle",
    srcs = ["bottle.py"],
    visibility = ["//visibility:public"],
)
        """,
        strip_prefix = "bottle-0.12.13",
    )

    native.new_http_archive(
        name = "protobuf_python",
        url = "https://pypi.python.org/packages/14/03/ff5279abda7b46e9538bfb1411d42831b7e65c460d73831ed2445649bc02/protobuf-3.5.1.tar.gz",
        build_file_content = """
py_library(
    name = "protobuf_python",
    srcs = glob(["google/protobuf/**/*.py"]),
    visibility = ["//visibility:public"],
    imports = [
        "@pypi_six//:six",
    ],
)
        """,
        strip_prefix = "protobuf-3.5.1",
    )

# Protobuf
def setup_protobuf():
    native.http_archive(
        name = "com_google_protobuf",
        sha256 = "1f8b9b202e9a4e467ff0b0f25facb1642727cdf5e69092038f15b37c75b99e45",
        strip_prefix = "protobuf-3.5.1",
        urls = ["https://github.com/google/protobuf/archive/v3.5.1.zip"],
    )

# Bazel rules
def setup_rules_docker():
    native.http_archive(
        name = "io_bazel_rules_docker",
        strip_prefix = "rules_docker-master",
        url = "https://github.com/bazelbuild/rules_docker/archive/master.zip",
    )
