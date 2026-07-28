package com.mindera.alfie.buildconvention

import com.android.build.api.dsl.ApplicationDefaultConfig

private const val GRAPHQL_SUFFIX = "GraphQL"
private const val WEB_SUFFIX = "Web"

enum class Environment(
    val url: String,
    val legacyUrl: String,
    val buildType: BuildType,
    val webHost: String = "localhost:4000"
) {
    Dev(
        // Android emulator reaches the host machine via 10.0.2.2.
        // The local BFF serves on port 4000.
        // For a real device, use the Custom environment with your machine's LAN IP.
        //
        // Note both urls are the same host and port: the legacy server also used 4000, and the BFF
        // has taken it over. The only remaining legacy consumer is BrandServiceImpl, reached solely
        // through the currently unreferenced ShopBrandsScreen, so nothing live queries it — but if
        // Brands is wired back up it will hit the BFF, which has no `brands` field.
        url = "http://10.0.2.2:4000/graphql",
        legacyUrl = "http://10.0.2.2:4000/graphql",
        buildType = BuildType.DEBUG
    ),
    PreProd(
        // TODO(ALFMOB-336): Replace with real PreProd BFF URL once confirmed by the BFF team.
        url = "https://api-preprod.alfie.com/graphql",
        legacyUrl = "https://api-legacy-preprod.alfie.com/graphql",
        buildType = BuildType.BETA
    ),
    Prod(
        // TODO(ALFMOB-336): Replace with real Prod BFF URL once confirmed by the BFF team.
        url = "https://api.alfie.com/graphql",
        legacyUrl = "https://api-legacy.alfie.com/graphql",
        buildType = BuildType.RELEASE
    )
}

fun ApplicationDefaultConfig.setEnvironmentsFields() {
    Environment.values().forEach {
        buildConfigField("String", "${it.name}$GRAPHQL_SUFFIX", "\"${it.url}\"")
        buildConfigField("String", "${it.name}Legacy$GRAPHQL_SUFFIX", "\"${it.legacyUrl}\"")
        buildConfigField("String", "${it.name}$WEB_SUFFIX", "\"https://${it.webHost}\"")
    }
}
