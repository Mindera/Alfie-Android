package com.mindera.alfie.feature.search

import com.mindera.alfie.core.ui.media.image.ImageSizeUI
import com.mindera.alfie.core.ui.media.image.ImageUI
import com.mindera.alfie.designsystem.component.price.PriceType
import com.mindera.alfie.designsystem.component.productcard.ProductCardType
import com.mindera.alfie.feature.search.model.BrandSuggestionUI
import com.mindera.alfie.feature.search.model.KeywordSuggestionUI
import com.mindera.alfie.feature.search.model.ProductSuggestionUI
import com.mindera.alfie.feature.search.model.SearchUI
import com.mindera.alfie.repository.product.model.Price
import com.mindera.alfie.repository.search.model.SearchSuggestion
import com.mindera.alfie.repository.search.model.SearchSuggestions
import com.mindera.alfie.repository.shared.model.Media
import com.mindera.alfie.repository.shared.model.Money
import kotlinx.collections.immutable.persistentListOf

internal val searchSuggestions = SearchSuggestions(
    keywords = listOf(
        SearchSuggestion.Keyword(
            value = "keyword",
            resultCount = 1
        )
    ),
    brands = listOf(
        SearchSuggestion.Brand(
            name = "brand",
            slug = "brand",
            resultCount = 1
        )
    ),
    products = listOf(
        SearchSuggestion.Product(
            id = "12345",
            name = "Product",
            brandName = "Brand",
            slug = "product",
            price = Price(
                amount = Money(
                    amount = 100,
                    amountFormatted = "$100",
                    currencyCode = "AUS"
                ),
                was = null
            ),
            media = listOf(
                Media.Image(
                    url = "",
                    alt = "Media"
                )
            )
        )
    )
)

internal val emptySearchSuggestions = SearchSuggestions(
    keywords = emptyList(),
    brands = emptyList(),
    products = emptyList()
)

internal val searchUI = SearchUI(
    searchTerm = "query",
    keywords = listOf(KeywordSuggestionUI(value = "keyword")),
    brands = listOf(
        BrandSuggestionUI(
            name = "brand",
            slug = "brand"
        )
    ),
    products = listOf(
        ProductSuggestionUI(
            id = "12345",
            slug = "product",
            productCardData = ProductCardType.Small(
                name = "Product",
                brand = "Brand",
                price = PriceType.Default("$100"),
                image = ImageUI(
                    images = persistentListOf(ImageSizeUI.Custom(url = "")),
                    alt = "Media"
                )
            )
        )
    )
)
