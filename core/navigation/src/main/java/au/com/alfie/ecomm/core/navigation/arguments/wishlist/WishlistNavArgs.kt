package au.com.alfie.ecomm.core.navigation.arguments.wishlist

fun wishlistNavArgs(launchFromTop: Boolean = false): WishlistNavArgs =
    WishlistNavArgs(launchFromTop = launchFromTop)

data class WishlistNavArgs(val launchFromTop: Boolean)
