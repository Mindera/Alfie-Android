package com.mindera.alfie.feature.shop

import com.mindera.alfie.core.commons.string.StringResource
import com.mindera.alfie.feature.shop.category.model.CategoryEntryUI
import com.mindera.alfie.feature.shop.category.model.CategoryUIState
import com.mindera.alfie.repository.navigation.model.NavEntry
import com.mindera.alfie.repository.navigation.model.NavItemType.HOME
import com.mindera.alfie.repository.navigation.model.NavItemType.PRODUCT
import kotlinx.collections.immutable.persistentListOf

internal val navEntries = listOf(
    NavEntry(
        id = 1,
        title = "Home Item",
        type = HOME,
        url = "https://home.item",
        hasChildren = true,
        items = listOf(
            NavEntry(
                id = 3,
                title = "Home Subitem 1",
                type = HOME,
                url = "https://home.subitem1"
            ),
            NavEntry(
                id = 4,
                title = "Home Subitem 2",
                type = HOME,
                url = "https://home.subitem2"
            )
        )
    ),
    NavEntry(
        id = 2,
        title = "Product Item",
        type = PRODUCT,
        url = "https://product.item",
        hasChildren = true,
        items = listOf(
            NavEntry(
                id = 5,
                title = "Product Subitem",
                type = PRODUCT,
                url = "https://product.subitem1"
            )
        )
    )
)

internal val shopEntries = persistentListOf(
    CategoryEntryUI(
        id = 1,
        title = StringResource.fromText("Home Item"),
        path = "https://home.item",
        hasChildren = true
    ),
    CategoryEntryUI(
        id = 2,
        title = StringResource.fromText("Product Item"),
        path = "https://product.item",
        hasChildren = true
    )
)

internal val categoryUiState = CategoryUIState.Data(
    title = StringResource.fromText("Title"),
    entries = shopEntries,
    isLoading = false
)
