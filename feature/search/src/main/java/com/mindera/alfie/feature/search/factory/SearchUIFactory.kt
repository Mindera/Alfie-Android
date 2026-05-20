package com.mindera.alfie.feature.search.factory

import com.mindera.alfie.core.commons.dispatcher.DispatcherProvider
import com.mindera.alfie.core.ui.media.image.ImageSizeUI
import com.mindera.alfie.core.ui.media.image.ImageUI
import com.mindera.alfie.designsystem.component.productcard.ProductCardType
import com.mindera.alfie.feature.mappers.toPriceType
import com.mindera.alfie.feature.search.model.BrandSuggestionUI
import com.mindera.alfie.feature.search.model.KeywordSuggestionUI
import com.mindera.alfie.feature.search.model.ProductSuggestionUI
import com.mindera.alfie.feature.search.model.SearchUI
import com.mindera.alfie.repository.search.model.SearchSuggestion
import com.mindera.alfie.repository.search.model.SearchSuggestions
import com.mindera.alfie.repository.shared.model.Media
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class SearchUIFactory @Inject constructor(
    private val dispatcher: DispatcherProvider
) {

    companion object {
        private const val MAX_KEYWORDS = 6
        private const val MAX_BRANDS = 6
        private const val MAX_PRODUCTS = 8
    }

    suspend operator fun invoke(
        searchTerm: String,
        searchSuggestions: SearchSuggestions,
        onProductClick: ClickEventOneArg<String>
    ): SearchUI = withContext(dispatcher.default()) {
        SearchUI(
            searchTerm = searchTerm,
            keywords = searchSuggestions.keywords.take(MAX_KEYWORDS).map { it.toUI() },
            brands = searchSuggestions.brands.take(MAX_BRANDS).map { it.toUI() },
            products = searchSuggestions.products.take(MAX_PRODUCTS).map { it.toUI(onProductClick) }
        )
    }

    private fun SearchSuggestion.Keyword.toUI() = KeywordSuggestionUI(value = value)

    private fun SearchSuggestion.Brand.toUI() = BrandSuggestionUI(
        name = name,
        slug = slug
    )

    private fun SearchSuggestion.Product.toUI(onProductClick: ClickEventOneArg<String>) = ProductSuggestionUI(
        id = id,
        slug = slug,
        productCardData = ProductCardType.Vertical(
            image = mapImage(media),
            brand = brandName,
            name = name,
            price = price.toPriceType(),
            onClick = { onProductClick(id) }
        )
    )

    private fun mapImage(media: List<Media.Image>): ImageUI {
        val image = media.firstOrNull()
        val imageSizeUI = ImageSizeUI.Custom(
            url = image?.url.orEmpty()
        )
        return ImageUI(
            images = persistentListOf(imageSizeUI),
            alt = image?.alt
        )
    }
}
