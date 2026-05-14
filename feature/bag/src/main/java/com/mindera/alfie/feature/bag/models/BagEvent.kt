package com.mindera.alfie.feature.bag.models

internal sealed interface BagEvent {

    data class OnProductClick(val productId: String) : BagEvent
}