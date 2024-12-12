package au.com.alfie.ecomm.feature.search.factory

import au.com.alfie.ecomm.core.commons.dispatcher.DispatcherProvider
import au.com.alfie.ecomm.core.commons.extension.orZero
import au.com.alfie.ecomm.core.ui.media.image.ImageSizeUI
import au.com.alfie.ecomm.core.ui.media.image.ImageUI
import au.com.alfie.ecomm.designsystem.component.productcard.ProductCardType
import au.com.alfie.ecomm.feature.search.model.BrandSuggestionUI
import au.com.alfie.ecomm.feature.search.model.KeywordSuggestionUI
import au.com.alfie.ecomm.feature.search.model.ProductSuggestionUI
import au.com.alfie.ecomm.feature.search.model.SearchUI
import au.com.alfie.ecomm.repository.search.model.SearchSuggestion
import au.com.alfie.ecomm.repository.search.model.SearchSuggestions
import au.com.alfie.ecomm.repository.shared.model.Media
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
        searchSuggestions: SearchSuggestions
    ): SearchUI = withContext(dispatcher.default()) {
        SearchUI(
            searchTerm = searchTerm,
            keywords = searchSuggestions.keywords.take(MAX_KEYWORDS).map { it.toUI() },
            brands = searchSuggestions.brands.take(MAX_BRANDS).map { it.toUI() },
            products = searchSuggestions.products.take(MAX_PRODUCTS).map { it.toUI() }
        )
    }

    private fun SearchSuggestion.Keyword.toUI() = KeywordSuggestionUI(value = value)

    private fun SearchSuggestion.Brand.toUI() = BrandSuggestionUI(
        name = name,
        slug = slug
    )

    private fun SearchSuggestion.Product.toUI() = ProductSuggestionUI(
        id = id,
        slug = slug,
        productCardData = ProductCardType.Small(
            image = mapImage(media),
            brand = brandName,
            name = name,
            price = null
        )
    )

    private fun mapImage(media: List<Media.Image>): ImageUI {
        val image = media.firstOrNull()
        val imageSizeUI = ImageSizeUI.Custom(
            url = image?.url.orEmpty(),
            width = image?.width.orZero()
        )
        return ImageUI(
            images = persistentListOf(imageSizeUI),
            alt = image?.alt
        )
    }
}
