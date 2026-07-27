package com.mindera.alfie.data.navigation

import com.mindera.alfie.data.database.navigation.model.NavigationEntryEntity
import com.mindera.alfie.graphql.bff.MainMenuQuery
import com.mindera.alfie.repository.navigation.model.NavEntry
import com.mindera.alfie.repository.navigation.model.NavItemType

// region BFF mainMenu builders

internal fun mainMenuData(vararg items: MainMenuQuery.Item) = MainMenuQuery.Data(
    mainMenu = MainMenuQuery.MainMenu(
        handle = "main-menu",
        title = "Main",
        items = items.toList()
    )
)

internal fun menuItem(
    title: String,
    url: String?,
    id: String = "l1-$title",
    items: List<MainMenuQuery.Item1>? = null
) = MainMenuQuery.Item(id = id, title = title, url = url, items = items)

internal fun subMenuItem(
    title: String,
    url: String?,
    id: String = "l2-$title",
    items: List<MainMenuQuery.Item2>? = null
) = MainMenuQuery.Item1(id = id, title = title, url = url, items = items)

internal fun leafMenuItem(
    title: String,
    url: String?,
    id: String = "l3-$title"
) = MainMenuQuery.Item2(id = id, title = title, url = url)

/** A two-level menu: one parent with a handle plus one child, and one plain leaf. */
internal val navEntriesData = mainMenuData(
    menuItem(
        title = "Women",
        url = "/women",
        items = listOf(subMenuItem(title = "Dresses", url = "/dresses"))
    ),
    menuItem(title = "Men", url = "/men")
)

internal val navEntryEntitiesFromGraph = listOf(
    NavigationEntryEntity(
        title = "Women",
        path = "women",
        navItemType = NavItemType.LISTING.name,
        hasChildren = true,
        items = listOf(
            NavigationEntryEntity(
                title = "Dresses",
                path = "dresses",
                navItemType = NavItemType.LISTING.name,
                hasChildren = false
            )
        )
    ),
    NavigationEntryEntity(
        title = "Men",
        path = "men",
        navItemType = NavItemType.LISTING.name,
        hasChildren = false
    )
)

// endregion

// region Cache mapper fixtures

val navEntryEntities = listOf(
    NavigationEntryEntity(
        id = 1,
        title = "Home Item",
        path = "https://home.item",
        navItemType = "HOME",
        hasChildren = true
    ),
    NavigationEntryEntity(
        id = 2,
        title = "Product Item",
        path = "https://product.item",
        navItemType = "PRODUCT",
        hasChildren = true
    ),
    NavigationEntryEntity(
        id = 3,
        parentId = 1,
        title = "Home Subitem 1",
        path = "https://home.subitem1",
        navItemType = "HOME"
    ),
    NavigationEntryEntity(
        id = 4,
        parentId = 1,
        title = "Home Subitem 2",
        path = "https://home.subitem2",
        navItemType = "HOME"
    ),
    NavigationEntryEntity(
        id = 5,
        parentId = 2,
        title = "Product Subitem",
        path = "https://product.subitem1",
        navItemType = "PRODUCT"
    )
)

val mappedNavEntries = listOf(
    NavEntry(
        id = 1,
        title = "Home Item",
        type = NavItemType.HOME,
        url = "https://home.item",
        hasChildren = true
    ),
    NavEntry(
        id = 2,
        title = "Product Item",
        type = NavItemType.PRODUCT,
        url = "https://product.item",
        hasChildren = true
    ),
    NavEntry(
        id = 3,
        title = "Home Subitem 1",
        type = NavItemType.HOME,
        url = "https://home.subitem1"
    ),
    NavEntry(
        id = 4,
        title = "Home Subitem 2",
        type = NavItemType.HOME,
        url = "https://home.subitem2"
    ),
    NavEntry(
        id = 5,
        title = "Product Subitem",
        type = NavItemType.PRODUCT,
        url = "https://product.subitem1"
    )
)

val mappedNavEntryEntities = listOf(
    NavigationEntryEntity(
        id = 0,
        title = "Home Item",
        path = "https://home.item",
        navItemType = "HOME",
        hasChildren = true
    ),
    NavigationEntryEntity(
        id = 0,
        title = "Product Item",
        path = "https://product.item",
        navItemType = "PRODUCT",
        hasChildren = true
    ),
    NavigationEntryEntity(
        id = 0,
        title = "Home Subitem 1",
        path = "https://home.subitem1",
        navItemType = "HOME"
    ),
    NavigationEntryEntity(
        id = 0,
        title = "Home Subitem 2",
        path = "https://home.subitem2",
        navItemType = "HOME"
    ),
    NavigationEntryEntity(
        id = 0,
        title = "Product Subitem",
        path = "https://product.subitem1",
        navItemType = "PRODUCT"
    )
)

// endregion
