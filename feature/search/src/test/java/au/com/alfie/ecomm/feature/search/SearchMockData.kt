package au.com.alfie.ecomm.feature.search

import au.com.alfie.ecomm.core.ui.media.image.ImageSizeUI
import au.com.alfie.ecomm.core.ui.media.image.ImageUI
import au.com.alfie.ecomm.designsystem.component.productcard.ProductCardType
import au.com.alfie.ecomm.feature.search.model.BrandSuggestionUI
import au.com.alfie.ecomm.feature.search.model.KeywordSuggestionUI
import au.com.alfie.ecomm.feature.search.model.ProductSuggestionUI
import au.com.alfie.ecomm.feature.search.model.SearchUI
import au.com.alfie.ecomm.repository.product.model.Price
import au.com.alfie.ecomm.repository.search.model.SearchSuggestion
import au.com.alfie.ecomm.repository.search.model.SearchSuggestions
import au.com.alfie.ecomm.repository.shared.model.Media
import au.com.alfie.ecomm.repository.shared.model.Money
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
                price = null,
                image = ImageUI(
                    images = persistentListOf(ImageSizeUI.Custom(url = "")),
                    alt = "Media"
                )
            )
        )
    )
)
