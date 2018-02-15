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
        deps = [
            "@support_annotations//jar",
        ],
    )

    maven_jar(
        name = "arch_core",
        settings = "//third_party:google_maven.xml",
        artifact = "android.arch.core:common:1.0.0",
        deps = [
            "@support_annotations//jar",
        ],
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
        deps = [
            "@reactive_stream//jar"
        ],
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

# Protobuf
def setup_protobuf():
    native.http_archive(
        name = "com_google_protobuf",
        sha256 = "cef7f1b5a7c5fba672bec2a319246e8feba471f04dcebfe362d55930ee7c1c30",
        strip_prefix = "protobuf-3.5.0",
        urls = ["https://github.com/google/protobuf/archive/v3.5.0.zip"],
    )

# Bazel rules
def setup_rules_python():
    native.http_archive(
        name = "io_bazel_rules_python",
        strip_prefix = "rules_python-master",
        url = "https://github.com/bazelbuild/rules_python/archive/master.zip",
    )

def setup_rules_docker():
    native.http_archive(
        name = "io_bazel_rules_docker",
        strip_prefix = "rules_docker-master",
        url = "https://github.com/bazelbuild/rules_docker/archive/master.zip",
    )

def setup_rules_apple():
    native.http_archive(
        name = "build_bazel_rules_apple",
        url = "https://github.com/bazelbuild/rules_apple/archive/master.zip",
        strip_prefix = "rules_apple-master",
    )
