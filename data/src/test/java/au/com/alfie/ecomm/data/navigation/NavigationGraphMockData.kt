package au.com.alfie.ecomm.data.navigation

import au.com.alfie.ecomm.data.database.navigation.model.NavigationEntryEntity
import au.com.alfie.ecomm.graphql.NavEntriesByHandleQuery
import au.com.alfie.ecomm.graphql.type.NavMenuItemType
import au.com.alfie.ecomm.repository.navigation.model.NavEntry
import au.com.alfie.ecomm.repository.navigation.model.NavItemType

internal val navEntriesData = NavEntriesByHandleQuery.Data(
    navigation = listOf(
        NavEntriesByHandleQuery.Navigation(
            title = "Home Item",
            url = "https://home.item",
            type = NavMenuItemType.HOME,
            items = emptyList(),
            attributes = emptyList()
        ),
        NavEntriesByHandleQuery.Navigation(
            title = "Product Item",
            url = "https://product.item",
            type = NavMenuItemType.PRODUCT,
            items = emptyList(),
            attributes = emptyList()
        )
    )
)

internal val navEntries = listOf(
    NavEntry(
        id = 0,
        title = "Home Item",
        type = NavItemType.HOME,
        url = "https://home.item"
    ),
    NavEntry(
        id = 0,
        title = "Product Item",
        type = NavItemType.PRODUCT,
        url = "https://product.item"
    )
)

internal val navEntryEntitiesFromGraph = listOf(
    NavigationEntryEntity(
        id = 0,
        title = "Home Item",
        navItemType = NavItemType.HOME.name,
        path = "https://home.item"
    ),
    NavigationEntryEntity(
        id = 0,
        title = "Product Item",
        navItemType = NavItemType.PRODUCT.name,
        path = "https://product.item"
    )
)

val navEntryEntities = listOf(
    NavigationEntryEntity(
        id = 1,
        title = "Home Item",
        path = "https://home.item",
        navItemType = "HOME"
    ),
    NavigationEntryEntity(
        id = 2,
        title = "Product Item",
        path = "https://product.item",
        navItemType = "PRODUCT"
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
        url = "https://home.item"
    ),
    NavEntry(
        id = 2,
        title = "Product Item",
        type = NavItemType.PRODUCT,
        url = "https://product.item"
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
        navItemType = "HOME"
    ),
    NavigationEntryEntity(
        id = 0,
        title = "Product Item",
        path = "https://product.item",
        navItemType = "PRODUCT"
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
