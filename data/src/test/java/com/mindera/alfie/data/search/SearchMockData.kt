package com.mindera.alfie.data.search

import com.mindera.alfie.data.database.search.model.RecentSearchEntity
import com.mindera.alfie.graphql.SearchSuggestionsQuery
import com.mindera.alfie.graphql.fragment.ImageInfo
import com.mindera.alfie.graphql.fragment.MoneyInfo
import com.mindera.alfie.graphql.fragment.PriceInfo
import com.mindera.alfie.graphql.type.MediaContentType
import com.mindera.alfie.repository.product.model.Price
import com.mindera.alfie.repository.search.model.RecentSearch
import com.mindera.alfie.repository.search.model.SearchSuggestion
import com.mindera.alfie.repository.search.model.SearchSuggestions
import com.mindera.alfie.repository.shared.model.Media
import com.mindera.alfie.repository.shared.model.Money

internal val listRecentSearchEntity = listOf(
    RecentSearchEntity(
        searchTerm = "search #1",
        searchTimestamp = 12345L,
        type = RecentSearchEntity.Type.QUERY
    ),
    RecentSearchEntity(
        searchTerm = "search #2",
        searchTimestamp = 123456L,
        type = RecentSearchEntity.Type.BRAND,
        slug = "brand-slug"
    )
)

internal val listRecentSearch = listOf(
    RecentSearch.Query("search #1"),
    RecentSearch.Brand(
        searchTerm = "search #2",
        slug = "brand-slug"
    )
)

internal val searchSuggestionsData = SearchSuggestionsQuery.Data(
    suggestion = SearchSuggestionsQuery.Suggestion(
        keywords = listOf(
            SearchSuggestionsQuery.Keyword(
                value = "keyword",
                results = 1
            )
        ),
        brands = listOf(
            SearchSuggestionsQuery.Brand(
                value = "brand",
                slug = "brand",
                results = 1
            )
        ),
        products = listOf(
            SearchSuggestionsQuery.Product(
                id = "12345",
                name = "Product",
                brandName = "Brand",
                slug = "product",
                price = SearchSuggestionsQuery.Price(
                    __typename = "Price",
                    priceInfo = PriceInfo(
                        amount = PriceInfo.Amount(
                            __typename = "Amount",
                            moneyInfo = MoneyInfo(
                                amount = 10000,
                                amountFormatted = "$100",
                                currencyCode = "AUS"
                            )
                        ),
                        was = null
                    )
                ),
                media = listOf(
                    SearchSuggestionsQuery.Medium(
                        __typename = "Image",
                        onImage = SearchSuggestionsQuery.OnImage(
                            __typename = "Medium",
                            imageInfo = ImageInfo(
                                url = "",
                                alt = "Media",
                                mediaContentType = MediaContentType.IMAGE
                            )
                        )
                    )
                )
            )
        )
    )
)

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
                    amount = 100.0,
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
