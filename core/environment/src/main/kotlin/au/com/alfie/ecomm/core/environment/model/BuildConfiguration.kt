package au.com.alfie.ecomm.core.environment.model

data class BuildConfiguration(
    val appName: String,
    val versionName: String,
    val applicationId: String,
    val debug: Boolean,
    val environments: Environments
)
