package au.com.alfie.ecomm.data.search.mapper

import au.com.alfie.ecomm.data.shared.mapper.toDomain
import au.com.alfie.ecomm.graphql.SearchSuggestionsQuery
import au.com.alfie.ecomm.repository.search.model.SearchSuggestion
import au.com.alfie.ecomm.repository.search.model.SearchSuggestions

internal fun SearchSuggestionsQuery.Suggestion.toDomain() = SearchSuggestions(
    keywords = keywords.map { it.toDomain() },
    brands = brands.map { it.toDomain() },
    products = products.map { it.toDomain() }
)

private fun SearchSuggestionsQuery.Keyword.toDomain() = SearchSuggestion.Keyword(
    value = value,
    resultCount = results
)

private fun SearchSuggestionsQuery.Brand.toDomain() = SearchSuggestion.Brand(
    name = value,
    slug = slug,
    resultCount = results
)

private fun SearchSuggestionsQuery.Product.toDomain() = SearchSuggestion.Product(
    id = id,
    name = name,
    slug = slug,
    brandName = brandName,
    price = price.priceInfo.toDomain(),
    media = media.mapNotNull { it.onImage?.imageInfo?.toDomain() }
)
