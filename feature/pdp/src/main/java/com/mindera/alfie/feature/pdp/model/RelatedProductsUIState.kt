package com.mindera.alfie.feature.pdp.model

import androidx.compose.runtime.Stable
import com.mindera.alfie.designsystem.component.productcard.ProductCardType
import kotlinx.collections.immutable.ImmutableList

/**
 * "You might also like" loads independently of the product itself: it must never block the PDP
 * nor fail it, so an error — or an empty recommendation set — simply drops the section
 * ([Hidden]) rather than surfacing an error state.
 */
@Stable
internal sealed interface RelatedProductsUIState {

    data object Hidden : RelatedProductsUIState

    data object Loading : RelatedProductsUIState

    data class Loaded(val items: ImmutableList<RelatedProductUI>) : RelatedProductsUIState
}

@Stable
internal data class RelatedProductUI(
    val slug: String,
    val productCardData: ProductCardType.Vertical,
    val isWishlisted: Boolean = false
)
