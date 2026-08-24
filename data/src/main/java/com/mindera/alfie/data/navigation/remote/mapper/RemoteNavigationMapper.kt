package com.mindera.alfie.data.navigation.remote.mapper

import com.mindera.alfie.data.database.navigation.model.NavigationEntryEntity
import com.mindera.alfie.graphql.bff.MainMenuQuery
import com.mindera.alfie.repository.navigation.model.NavItemType
import java.net.URI

/**
 * Maps the BFF `menu` tree onto navigation entities.
 *
 * Apollo generates a distinct type per nesting level (`Item` / `Item1` / `Item2`), so each level
 * gets a thin adapter over the shared [navigationEntry] builder. The query caps nesting at three
 * levels (Shopify's menu depth limit), so the deepest level has no children to convert.
 */
internal fun MainMenuQuery.Data.toEntity(): List<NavigationEntryEntity> =
    menu.items.mapNotNull { it.toEntity() }

private fun MainMenuQuery.Item.toEntity(): NavigationEntryEntity? = navigationEntry(
    title = title,
    rawUrl = url,
    children = items?.mapNotNull { it?.toEntity() }.orEmpty()
)

private fun MainMenuQuery.Item1.toEntity(): NavigationEntryEntity? = navigationEntry(
    title = title,
    rawUrl = url,
    children = items?.mapNotNull { it?.toEntity() }.orEmpty()
)

private fun MainMenuQuery.Item2.toEntity(): NavigationEntryEntity? = navigationEntry(
    title = title,
    rawUrl = url,
    children = emptyList()
)

private fun navigationEntry(
    title: String,
    rawUrl: String?,
    children: List<NavigationEntryEntity>
): NavigationEntryEntity? {
    val path = collectionHandlePath(url = rawUrl)

    // An entry that resolves to no collection handle and has no surviving children is not
    // actionable, so drop it. A parent whose children were all dropped keeps its own handle and
    // degrades to a chevron-less leaf.
    if (path == null && children.isEmpty()) return null

    return NavigationEntryEntity(
        title = title,
        path = path.orEmpty(),
        // Every menu entry routes through the PLP. The BFF `MenuItem` exposes no type, and
        // inferring one from the url only routes exotic values into a webview branch we do not
        // want, so this is deliberately fixed rather than derived.
        navItemType = NavItemType.LISTING.name,
        hasChildren = children.isNotEmpty(),
        items = children
    )
}

/**
 * Reduces a BFF menu url to `<collection-handle>`: the last non-empty path segment, lowercased,
 * with any query and fragment stripped. Shopify handles are lowercase, and the PLP strips the
 * leading `/` before passing the remainder to `productList` as its `collectionHandle` — so a
 * multi-segment path such as `/shop/new/dresses` must collapse to `dresses`.
 *
 * Returns null when nothing usable remains: empty, blank, `"/"`, or a host-only absolute url.
 */
private fun collectionHandlePath(url: String?): String? {
    val trimmed = url?.trim().orEmpty()
    if (trimmed.isEmpty()) return null

    // Absolute urls are parsed rather than split raw: "https://example.com" has no path and must
    // yield null, not "/example.com".
    val uri = runCatching { URI(trimmed) }.getOrNull()
    val path = if (uri != null && uri.isAbsolute) {
        uri.path.orEmpty()
    } else {
        trimmed.substringBefore(delimiter = '?').substringBefore(delimiter = '#')
    }

    val handle = path.split('/').lastOrNull { it.isNotBlank() } ?: return null
    return handle.lowercase()
}
