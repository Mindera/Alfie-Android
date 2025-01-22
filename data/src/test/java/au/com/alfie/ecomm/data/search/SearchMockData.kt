package au.com.alfie.ecomm.data.search

import au.com.alfie.ecomm.data.database.search.model.RecentSearchEntity
import au.com.alfie.ecomm.graphql.SearchSuggestionsQuery
import au.com.alfie.ecomm.graphql.fragment.ImageInfo
import au.com.alfie.ecomm.graphql.fragment.MoneyInfo
import au.com.alfie.ecomm.graphql.fragment.PriceInfo
import au.com.alfie.ecomm.graphql.type.MediaContentType
import au.com.alfie.ecomm.repository.product.model.Price
import au.com.alfie.ecomm.repository.search.model.RecentSearch
import au.com.alfie.ecomm.repository.search.model.SearchSuggestion
import au.com.alfie.ecomm.repository.search.model.SearchSuggestions
import au.com.alfie.ecomm.repository.shared.model.Media
import au.com.alfie.ecomm.repository.shared.model.Money

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
                                amount = 100,
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
