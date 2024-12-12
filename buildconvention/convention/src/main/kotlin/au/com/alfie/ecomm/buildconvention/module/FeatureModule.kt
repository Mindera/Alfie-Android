package au.com.alfie.ecomm.buildconvention.module

object FeatureModule {

    const val featureSearch = ":feature:search"

    val modules = listOf(
        "account",
        "bag",
        "debug",
        "home",
        "pdp",
        "plp",
        "search",
        "shop",
        "startup",
        "webview",
        "wishlist"
    ).map {
        ":feature:$it"
    }
}
