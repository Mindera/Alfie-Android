package com.mindera.alfie.feature.plp

import com.mindera.alfie.core.ui.media.image.ImageSizeUI
import com.mindera.alfie.core.ui.media.image.ImageUI
import com.mindera.alfie.designsystem.component.price.PriceType
import com.mindera.alfie.designsystem.component.productcard.ProductCardType
import com.mindera.alfie.feature.plp.model.ProductListEntryUI
import com.mindera.alfie.feature.plp.model.ProductListUI
import com.mindera.alfie.repository.productlist.model.ProductListEntry
import com.mindera.alfie.repository.productlist.model.ProductListLayoutMode
import com.mindera.alfie.repository.productlist.model.ProductListMetadata
import com.mindera.alfie.repository.productlist.model.ProductListPriceRange
import com.mindera.alfie.repository.productlist.model.ProductSortOption
import com.mindera.alfie.repository.shared.model.Media
import kotlinx.collections.immutable.persistentListOf

internal val products = listOf(
    ProductListEntry(
        id = "123456",
        slug = "123456-product",
        name = "Product name",
        brandName = "Brand",
        productType = "Clothing",
        primaryImage = Media.Image(
            url = "https://www.alfie.com/productimages/thumb/1/2666503_22841458_13891526.jpg",
            alt = "Product image"
        ),
        priceRange = ProductListPriceRange(
            minAmount = 100.0,
            maxAmount = 200.0,
            currencyCode = "AUD"
        ),
        inventoryTotal = 10,
        tags = listOf("Best Seller")
    ),
    ProductListEntry(
        id = "654321",
        slug = "654321-product",
        name = "Product 2",
        brandName = "Brand 2",
        productType = null,
        primaryImage = null,
        priceRange = ProductListPriceRange(
            minAmount = 100.0,
            maxAmount = 100.0,
            currencyCode = "AUD"
        ),
        inventoryTotal = null,
        tags = emptyList()
    )
)

internal val productListMetadata = ProductListMetadata(
    totalResults = 2
)

internal val productsVerticalUI = listOf(
    ProductListEntryUI(
        id = "123456",
        productCardData = ProductCardType.Vertical(
            name = "Product name",
            brand = "Brand",
            price = PriceType.Range(
                startPrice = "A$100.00",
                endPrice = "A$200.00"
            ),
            image = ImageUI(
                images = persistentListOf(
                    ImageSizeUI.Custom(
                        url = "https://www.alfie.com/productimages/thumb/1/2666503_22841458_13891526.jpg"
                    )
                ),
                alt = "Product image"
            ),
            onFavoriteClick = { },
            label = "Best Seller"
        )
    ),
    ProductListEntryUI(
        id = "654321",
        productCardData = ProductCardType.Vertical(
            name = "Product 2",
            brand = "Brand 2",
            price = PriceType.Default(price = "A$100.00"),
            image = ImageUI(
                images = persistentListOf(ImageSizeUI.Custom(url = "")),
                alt = null
            ),
            onFavoriteClick = { },
            label = null
        )
    )
)

internal val gridProductListUI = ProductListUI(
    resultCount = 2,
    isLoadingMetadata = false,
    layoutMode = ProductListLayoutMode.GRID,
    compactColumnCount = 2,
    nonCompactColumnCount = 3,
    wishlistIds = emptySet(),
    selectedSort = ProductSortOption.RECOMMENDED,
    selectedFilters = null,
    availableFilters = emptyList(),
    showRefine = false
)

internal val columnProductListUI = ProductListUI(
    resultCount = 2,
    isLoadingMetadata = false,
    layoutMode = ProductListLayoutMode.COLUMN,
    compactColumnCount = 1,
    nonCompactColumnCount = 2,
    wishlistIds = emptySet(),
    selectedSort = ProductSortOption.RECOMMENDED,
    selectedFilters = null,
    availableFilters = emptyList(),
    showRefine = false
)
