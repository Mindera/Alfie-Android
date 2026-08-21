package com.mindera.alfie.repository.navigation.model

enum class HandleType(val handle: String) {
    /**
     * The Shop categories screen. Maps to Shopify's `main-menu`, not `"header"`: the BFF `menu`
     * query is keyed by the Shopify menu handle, and the header slot *is* that menu.
     */
    HEADER(handle = "main-menu"),
    FOOTER(handle = "footer"),
    SOCIAL(handle = "social"),
    TOPBAR(handle = "topbar")
}
