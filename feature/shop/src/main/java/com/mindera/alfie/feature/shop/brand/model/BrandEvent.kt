package com.mindera.alfie.feature.shop.brand.model

internal sealed interface BrandEvent {

    data class OnBrandEntryClickEvent(val entry: BrandEntryUI.Entry) : BrandEvent

    data class OnBrandSearchEvent(val searchTerm: String) : BrandEvent
}
