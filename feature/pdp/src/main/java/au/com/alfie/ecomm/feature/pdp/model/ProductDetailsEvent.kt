package au.com.alfie.ecomm.feature.pdp.model

internal sealed interface ProductDetailsEvent {

    data object OnAddToBagClick : ProductDetailsEvent

    data object OnShareClick : ProductDetailsEvent

    data class OnColorClick(val index: Int) : ProductDetailsEvent

    data class OnSectionClick(val item: ProductDetailsSectionItem) : ProductDetailsEvent

    data class OnFavoriteClick(val productId: String) : ProductDetailsEvent

    data class OnSizeSelect(val sizeUI: SizeUI) : ProductDetailsEvent
}
