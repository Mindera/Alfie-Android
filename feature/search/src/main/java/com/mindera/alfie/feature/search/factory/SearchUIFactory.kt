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
        SearchUI(
            searchTerm = searchTerm,
            keywords = searchSuggestions.keywords.map { it.toUI() }.toImmutableList(),
            brands = searchSuggestions.brands.map { it.toUI() }.toImmutableList(),
            products = searchSuggestions.products.map { it.toUI(onProductClick) }.toImmutableList()
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
            // The BFF marks price optional; an unpriced suggestion renders a blank price line.
            price = PriceType.Default(price = price?.amountFormatted.orEmpty()),
            onClick = { onProductClick(slug) }
        )
    )
}
