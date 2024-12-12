package au.com.alfie.ecomm.buildconvention

import com.android.build.api.dsl.ApplicationDefaultConfig

private const val GRAPHQL_SUFFIX = "GraphQL"
private const val WEB_SUFFIX = "Web"

enum class Environment(
    val url: String,
    val buildType: BuildType,
    val webHost: String = "localhost:4000"
) {
    Dev(
        url = "http://localhost:4000/graphql",
        buildType = BuildType.DEBUG
    ),
    PreProd(
        url = "https://api-preprod.localhost:4000/graphql",
        buildType = BuildType.BETA
    ),
    Prod(
        url = "https://api.localhost:4000/graphql",
        buildType = BuildType.RELEASE
    )
}

fun ApplicationDefaultConfig.setEnvironmentsFields() {
    Environment.values().forEach {
        buildConfigField("String", "${it.name}$GRAPHQL_SUFFIX", "\"${it.url}\"")
        buildConfigField("String", "${it.name}$WEB_SUFFIX", "\"https://${it.webHost}\"")
    }
}
