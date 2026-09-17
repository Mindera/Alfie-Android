package com.mindera.alfie.feature.pdp.factory

import com.mindera.alfie.core.commons.dispatcher.DispatcherProvider
import com.mindera.alfie.core.ui.event.ClickEventOneArg
import com.mindera.alfie.designsystem.component.productcard.ProductCardType
import com.mindera.alfie.feature.mappers.toImageUI
import com.mindera.alfie.feature.mappers.toPriceType
import com.mindera.alfie.feature.pdp.model.RelatedProductUI
import com.mindera.alfie.repository.productlist.model.ProductListEntry
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Maps recommendations onto the same vertical product card the PLP grid uses, so the section
 * stays visually identical to every other product grid in the app.
 */
internal class RelatedProductsUIFactory @Inject constructor(
    private val dispatcher: DispatcherProvider
) {

    suspend operator fun invoke(
        entries: List<ProductListEntry>,
        wishlistedSlugs: Collection<String>,
        onProductClick: ClickEventOneArg<String>,
        onFavoriteClick: ClickEventOneArg<String>
    ): ImmutableList<RelatedProductUI> = withContext(dispatcher.default()) {
        entries.map { entry ->
            RelatedProductUI(
                slug = entry.slug,
                isWishlisted = wishlistedSlugs.contains(entry.slug),
                productCardData = ProductCardType.Vertical(
                    brand = entry.brandName.orEmpty(),
                    name = entry.name,
                    price = entry.priceRange.toPriceType(),
                    image = entry.primaryImage.toImageUI(),
                    onClick = { onProductClick(entry.slug) },
                    onFavoriteClick = { onFavoriteClick(entry.slug) },
                    label = entry.tags.firstOrNull()
                )
            )
        }.toImmutableList()
    }
}
