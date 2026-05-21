package com.mindera.alfie.buildconvention

import com.android.build.api.dsl.ApplicationDefaultConfig

private const val GRAPHQL_SUFFIX = "GraphQL"
private const val WEB_SUFFIX = "Web"

enum class Environment(
    val url: String,
    val buildType: BuildType,
    val webHost: String = "localhost:4000"
) {
    Dev(
        // Android emulator reaches the host machine via 10.0.2.2.
        // BFF runs on port 3000 by default.
        // For a real device, use the Custom environment with your machine's LAN IP.
        url = "http://10.0.2.2:3000/graphql",
        buildType = BuildType.DEBUG
    ),
    PreProd(
        // TODO(ALFMOB-336): Replace with real PreProd BFF URL once confirmed by the BFF team.
        url = "https://api-preprod.alfie.com/graphql",
        buildType = BuildType.BETA
    ),
    Prod(
        // TODO(ALFMOB-336): Replace with real Prod BFF URL once confirmed by the BFF team.
        url = "https://api.alfie.com/graphql",
        buildType = BuildType.RELEASE
    )
}

fun ApplicationDefaultConfig.setEnvironmentsFields() {
    Environment.values().forEach {
        buildConfigField("String", "${it.name}$GRAPHQL_SUFFIX", "\"${it.url}\"")
        buildConfigField("String", "${it.name}$WEB_SUFFIX", "\"https://${it.webHost}\"")
    }
}
