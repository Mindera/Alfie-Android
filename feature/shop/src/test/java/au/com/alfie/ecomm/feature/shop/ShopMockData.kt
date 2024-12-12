package au.com.alfie.ecomm.feature.shop

import au.com.alfie.ecomm.core.commons.string.StringResource
import au.com.alfie.ecomm.feature.shop.brand.model.BrandEntryUI
import au.com.alfie.ecomm.feature.shop.brand.model.BrandUIState
import au.com.alfie.ecomm.feature.shop.category.model.CategoryEntryUI
import au.com.alfie.ecomm.feature.shop.category.model.CategoryUIState
import au.com.alfie.ecomm.feature.shop.model.ShopUI
import au.com.alfie.ecomm.repository.navigation.model.NavEntry
import au.com.alfie.ecomm.repository.navigation.model.NavItemType.HOME
import au.com.alfie.ecomm.repository.navigation.model.NavItemType.PRODUCT
import au.com.alfie.ecomm.repository.shared.model.Brand
import kotlinx.collections.immutable.persistentListOf

internal const val BASE_URL = "https://www.alfie.com"

internal val navEntries = listOf(
    NavEntry(
        id = 1,
        title = "Home Item",
        type = HOME,
        url = "https://home.item",
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
        path = "https://home.item"
    ),
    CategoryEntryUI(
        id = 2,
        title = StringResource.fromText("Product Item"),
        path = "https://product.item"
    )
)

internal val categoryUiState = CategoryUIState.Data(
    title = StringResource.fromText("Title"),
    entries = shopEntries,
    isLoading = false
)

internal val brands = listOf(
    Brand(
        id = "123",
        name = "Adidas",
        slug = "adidas"
    ),
    Brand(
        id = "234",
        name = "Ardidas",
        slug = "ardidas"
    ),
    Brand(
        id = "345",
        name = "Balaclava",
        slug = "balaclava"
    ),
    Brand(
        id = "456",
        name = "Batidas",
        slug = "batidas"
    ),
    Brand(
        id = "567",
        name = "Gucci",
        slug = "gucci"
    )
)

internal val brandUiState = BrandUIState.Data(
    entries = persistentListOf(
        BrandEntryUI.Divider(character = 'A'),
        BrandEntryUI.Entry(
            id = "123",
            name = "Adidas",
            slug = "adidas"
        ),
        BrandEntryUI.Entry(
            id = "234",
            name = "Ardidas",
            slug = "ardidas"
        ),
        BrandEntryUI.Divider(character = 'B'),
        BrandEntryUI.Entry(
            id = "345",
            name = "Balaclava",
            slug = "balaclava"
        ),
        BrandEntryUI.Entry(
            id = "456",
            name = "Batidas",
            slug = "batidas"
        ),
        BrandEntryUI.Divider(character = 'G'),
        BrandEntryUI.Entry(
            id = "567",
            name = "Gucci",
            slug = "gucci"
        )
    ),
    isLoading = false
)

internal val shopUi = ShopUI(
    servicesUrl = "$BASE_URL/${ShopUIFactory.SERVICES_WEB_URL}"
)
