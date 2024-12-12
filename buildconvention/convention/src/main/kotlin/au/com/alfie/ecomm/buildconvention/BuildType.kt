package au.com.alfie.ecomm.buildconvention

enum class BuildType(
    val buildName: String,
    val applicationIdSuffix: String
) {
    DEBUG(
        buildName = "debug",
        applicationIdSuffix = ".debug"
    ),
    BETA(
        buildName = "beta",
        applicationIdSuffix = ".beta"
    ),
    RELEASE(
        buildName = "release",
        applicationIdSuffix = ""
    )
}
