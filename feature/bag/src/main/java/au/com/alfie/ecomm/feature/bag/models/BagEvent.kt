package au.com.alfie.ecomm.feature.bag.models

internal sealed interface BagEvent {

    data class OnProductClick(val productId: String) : BagEvent
}