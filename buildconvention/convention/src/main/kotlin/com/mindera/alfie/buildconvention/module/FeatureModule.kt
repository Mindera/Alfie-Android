package com.mindera.alfie.buildconvention.module

object FeatureModule {

    const val featureSearch = ":feature:search"

    val modules = listOf(
        "account",
        "bag",
        "debug",
        "home",
        "pdp",
        "plp",
        "scanner",
        "search",
        "shop",
        "startup",
        "webview",
        "wishlist"
    ).map {
        ":feature:$it"
    }
}
