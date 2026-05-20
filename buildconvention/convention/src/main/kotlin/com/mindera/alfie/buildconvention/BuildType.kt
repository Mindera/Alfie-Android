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
    )
}
