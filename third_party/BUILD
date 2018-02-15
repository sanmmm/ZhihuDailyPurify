package(
  default_visibility = ["//visibility:public"],
)

android_library(
  name = "android_app_dependencies",
  exports = [
    "@arch_core//jar",
    "@arch_lifecycle//jar",
    "@arch_lifecycle_runtime//aar",
    "@support_animated_vector_drawable//aar",
    "@support_annotations//jar",
    "@support_appcompat_v7//aar",
    "@support_cardview_v7//aar",
    "@support_compat//aar",
    "@support_core_ui//aar",
    "@support_core_utils//aar",
    "@support_design//aar",
    "@support_fragment//aar",
    "@support_media_compat//aar",
    "@support_recyclerview_v7//aar",
    "@support_transition//aar",
    "@support_v4//aar",
    "@support_vector_drawable//aar",
    "@rxjava//jar",
    "@reactive_stream//jar",
    "@universal_image_loader//aar",
    "@android_times_square//aar",
    "@recyclerview_stickyheaders//aar",
    "@rxandroid//aar",
  ]
)

java_plugin(
  name = "autovalue_plugin",
  processor_class = "com.google.auto.value.processor.AutoValueProcessor",
  deps = [
    "@autovalue//jar",
  ],
)

java_library(
  name = "autovalue",
  neverlink = 1,
  exports = [
    "@autovalue//jar",
  ],
)

java_library(
  name = "news_fetch_android_dependencies",
  exported_plugins = [
    ":autovalue_plugin",
  ],
  exports = [
    "@jsoup//jar",
    "@reactive_stream//jar",
    "@rxjava//jar",
    "@json//jar",
    ":autovalue"
  ],
)

native.java_library(
  name = "news_fetch_android_test_dependencies",
  exports = [
    "@junit//jar",
  ],
)
