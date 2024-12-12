package au.com.alfie.ecomm.core.navigation.arguments.webview

import au.com.alfie.ecomm.core.commons.string.StringResource

fun webViewNavArgs(
    url: String,
    title: StringResource? = null,
    isTitleLeftAligned: Boolean = false,
    isBottomBarItem: Boolean = false,
    hasSearchAction: Boolean = false,
    hasAccountAction: Boolean = false,
    hasWishlistAction: Boolean = false
): WebViewNavArgs = WebViewNavArgs(
    url = url,
    title = title,
    isTitleLeftAligned = isTitleLeftAligned,
    isBottomBarItem = isBottomBarItem,
    hasSearchAction = hasSearchAction,
    hasAccountAction = hasAccountAction,
    hasWishlistAction = hasWishlistAction
)

data class WebViewNavArgs(
    val url: String,
    val title: StringResource?,
    val isTitleLeftAligned: Boolean,
    val isBottomBarItem: Boolean,
    val hasSearchAction: Boolean,
    val hasAccountAction: Boolean,
    val hasWishlistAction: Boolean
)
