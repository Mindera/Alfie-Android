package com.mindera.alfie.data.search.mapper

import com.mindera.alfie.data.shared.mapper.toDomain
import com.mindera.alfie.graphql.SearchSuggestionsQuery
import com.mindera.alfie.repository.search.model.SearchSuggestion
import com.mindera.alfie.repository.search.model.SearchSuggestions

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
