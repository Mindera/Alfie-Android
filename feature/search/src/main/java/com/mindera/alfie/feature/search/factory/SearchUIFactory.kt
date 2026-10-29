package com.mindera.alfie.feature.search.factory

import com.mindera.alfie.core.commons.dispatcher.DispatcherProvider
import com.mindera.alfie.core.ui.event.ClickEventOneArg
import com.mindera.alfie.designsystem.component.price.PriceType
import com.mindera.alfie.designsystem.component.productcard.ProductCardType
import com.mindera.alfie.feature.mappers.toImageUI
import com.mindera.alfie.feature.search.model.BrandSuggestionUI
import com.mindera.alfie.feature.search.model.KeywordSuggestionUI
import com.mindera.alfie.feature.search.model.ProductSuggestionUI
import com.mindera.alfie.feature.search.model.SearchUI
import com.mindera.alfie.repository.search.model.SearchSuggestion
import com.mindera.alfie.repository.search.model.SearchSuggestions
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class SearchUIFactory @Inject constructor(
    private val dispatcher: DispatcherProvider
) {

    suspend operator fun invoke(
        searchTerm: String,
        searchSuggestions: SearchSuggestions,
        onProductClick: ClickEventOneArg<String>
    ): SearchUI = withContext(dispatcher.default()) {
        // The overlay keys its list items on these values, and a repeated key crashes a
        // LazyColumn. The BFF doesn't promise uniqueness, so duplicates are dropped here.
        SearchUI(
            searchTerm = searchTerm,
            keywords = searchSuggestions.keywords
                .distinctBy { it.value.lowercase() }
                .map { it.toUI() }
                .toImmutableList(),
            brands = searchSuggestions.brands
                .distinctBy { it.key }
                .map { it.toUI() }
                .toImmutableList(),
            products = searchSuggestions.products
                .distinctBy { it.id }
                .map { it.toUI(onProductClick) }
                .toImmutableList()
        )
    }

    private fun SearchSuggestion.Keyword.toUI() = KeywordSuggestionUI(value = value)

    private fun SearchSuggestion.Brand.toUI() = BrandSuggestionUI(
        key = key,
        name = name
    )

    private fun SearchSuggestion.Product.toUI(onProductClick: ClickEventOneArg<String>) = ProductSuggestionUI(
        id = id,
        slug = slug,
        productCardData = ProductCardType.Vertical(
            image = images.firstOrNull().toImageUI(),
            brand = brandName.orEmpty(),
            name = name,
            // The BFF marks price optional; an unpriced suggestion leaves the price row out.
            price = price?.let { PriceType.Default(price = it.amountFormatted) },
            onClick = { onProductClick(slug) }
        )
    )
}
