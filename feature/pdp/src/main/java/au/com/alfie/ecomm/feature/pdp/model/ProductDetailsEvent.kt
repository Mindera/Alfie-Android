package au.com.alfie.ecomm.feature.pdp.model

internal sealed interface ProductDetailsEvent {

    data class OnAddToBagClick(val item: ProductDetailsUI) : ProductDetailsEvent

    data object OnShareClick : ProductDetailsEvent

    data class OnColorClick(val index: Int) : ProductDetailsEvent

    data class OnSectionClick(val item: ProductDetailsSectionItem) : ProductDetailsEvent

    data object OnFavoriteClick : ProductDetailsEvent

    data class OnSizeSelect(val sizeUI: SizeUI) : ProductDetailsEvent
}
