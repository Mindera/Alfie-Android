package au.com.alfie.ecomm.data.navigation

import au.com.alfie.ecomm.data.database.navigation.model.NavigationEntryEntity
import au.com.alfie.ecomm.graphql.NavEntriesByHandleQuery
import au.com.alfie.ecomm.graphql.fragment.ImageInfo
import au.com.alfie.ecomm.graphql.fragment.NavMenuItemContainer
import au.com.alfie.ecomm.graphql.fragment.NavMenuItemInfo
import au.com.alfie.ecomm.graphql.type.MediaContentType
import au.com.alfie.ecomm.graphql.type.NavMenuItemType
import au.com.alfie.ecomm.repository.navigation.model.NavEntry
import au.com.alfie.ecomm.repository.navigation.model.NavItemType

internal val navEntriesData = NavEntriesByHandleQuery.Data(
    navigation = NavEntriesByHandleQuery.Navigation(
        items = listOf(
            NavEntriesByHandleQuery.Item(
                __typename = "",
                navMenuItemContainer = NavMenuItemContainer(
                    __typename = "",
                    media = NavMenuItemContainer.Media(
                        __typename = "",
                        onImage = NavMenuItemContainer.OnImage(
                            __typename = "",
                            imageInfo = ImageInfo(
                                url = "https://media.image",
                                mediaContentType = MediaContentType.IMAGE,
                                alt = null
                            )
                        )
                    ),
                    items = listOf(
                        NavMenuItemContainer.Item(
                            __typename = "",
                            navMenuItemInfo = NavMenuItemInfo(
                                title = "Home Subitem 1",
                                type = NavMenuItemType.HOME,
                                url = "https://home.subitem1"
                            )
                        ),
                        NavMenuItemContainer.Item(
                            __typename = "",
                            navMenuItemInfo = NavMenuItemInfo(
                                title = "Home Subitem 2",
                                type = NavMenuItemType.HOME,
                                url = "https://home.subitem2"
                            )
                        )
                    ),
                    attributes = emptyList(),
                    navMenuItemInfo = NavMenuItemInfo(
                        title = "Home Item",
                        type = NavMenuItemType.HOME,
                        url = "https://home.item"
                    )
                )
            ),
            NavEntriesByHandleQuery.Item(
                __typename = "",
                navMenuItemContainer = NavMenuItemContainer(
                    __typename = "",
                    media = NavMenuItemContainer.Media(
                        __typename = "",
                        onImage = NavMenuItemContainer.OnImage(
                            __typename = "",
                            imageInfo = ImageInfo(
                                url = "https://media.image",
                                mediaContentType = MediaContentType.IMAGE,
                                alt = null
                            )
                        )
                    ),
                    items = listOf(
                        NavMenuItemContainer.Item(
                            __typename = "",
                            navMenuItemInfo = NavMenuItemInfo(
                                title = "Product Subitem",
                                type = NavMenuItemType.PRODUCT,
                                url = "https://product.subitem1"
                            )
                        )
                    ),
                    attributes = emptyList(),
                    navMenuItemInfo = NavMenuItemInfo(
                        title = "Product Item",
                        type = NavMenuItemType.PRODUCT,
                        url = "https://product.item"
                    )
                )
            )
        ),
        attributes = emptyList()
    )
)

internal val navEntries = listOf(
    NavEntry(
        id = 0,
        title = "Home Item",
        type = NavItemType.HOME,
        url = "https://home.item",
        items = listOf(
            NavEntry(
                id = 0,
                title = "Home Subitem 1",
                type = NavItemType.HOME,
                url = "https://home.subitem1"
            ),
            NavEntry(
                id = 0,
                title = "Home Subitem 2",
                type = NavItemType.HOME,
                url = "https://home.subitem2"
            )
        )
    ),
    NavEntry(
        id = 0,
        title = "Product Item",
        type = NavItemType.PRODUCT,
        url = "https://product.item",
        items = listOf(
            NavEntry(
                id = 0,
                title = "Product Subitem",
                type = NavItemType.PRODUCT,
                url = "https://product.subitem1"
            )
        )
    )
)

internal val navEntryEntitiesFromGraph = listOf(
    NavigationEntryEntity(
        id = 0,
        parentId = 0,
        title = "Home Item",
        navItemType = NavItemType.HOME.name,
        path = "https://home.item",
        items = listOf(
            NavigationEntryEntity(
                id = 0,
                title = "Home Subitem 1",
                navItemType = NavItemType.HOME.name,
                path = "https://home.subitem1"
            ),
            NavigationEntryEntity(
                id = 0,
                title = "Home Subitem 2",
                navItemType = NavItemType.HOME.name,
                path = "https://home.subitem2"
            )
        )
    ),
    NavigationEntryEntity(
        id = 0,
        parentId = 0,
        title = "Product Item",
        navItemType = NavItemType.PRODUCT.name,
        path = "https://product.item",
        items = listOf(
            NavigationEntryEntity(
                id = 0,
                title = "Product Subitem",
                navItemType = NavItemType.PRODUCT.name,
                path = "https://product.subitem1"
            )
        )
    )
)

val navEntryEntities = listOf(
    NavigationEntryEntity(
        id = 1,
        parentId = 0,
        title = "Home Item",
        path = "https://home.item",
        navItemType = "HOME"
    ),
    NavigationEntryEntity(
        id = 2,
        parentId = 0,
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
        parentId = 0,
        title = "Home Item",
        path = "https://home.item",
        navItemType = "HOME"
    ),
    NavigationEntryEntity(
        id = 0,
        parentId = 0,
        title = "Product Item",
        path = "https://product.item",
        navItemType = "PRODUCT"
    ),
    NavigationEntryEntity(
        id = 0,
        parentId = 0,
        title = "Home Subitem 1",
        path = "https://home.subitem1",
        navItemType = "HOME"
    ),
    NavigationEntryEntity(
        id = 0,
        parentId = 0,
        title = "Home Subitem 2",
        path = "https://home.subitem2",
        navItemType = "HOME"
    ),
    NavigationEntryEntity(
        id = 0,
        parentId = 0,
        title = "Product Subitem",
        path = "https://product.subitem1",
        navItemType = "PRODUCT"
    )
)
