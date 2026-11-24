package com.mindera.alfie.buildconvention

enum class BuildType(
    val buildName: String,
    val applicationIdSuffix: String
) {
    DEBUG(
        buildName = "debug",
        applicationIdSuffix = ""
    ),
    BETA(
        buildName = "beta",
        applicationIdSuffix = ""
    ),
    RELEASE(
        buildName = "release",
        applicationIdSuffix = ""
    ),

    // The UI test build: debug-derived and never distributed. :app's instrumented tests run on it.
    UI_TEST(
        buildName = "uiTest",
        applicationIdSuffix = ""
    )
}
